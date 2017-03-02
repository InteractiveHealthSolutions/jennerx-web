package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.EnrollmentWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.ItemStock;
import org.ird.unfepi.model.ItemsDistributed;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.MuacMeasurement;
import org.ird.unfepi.model.MuacMeasurementId;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.validator.ChildValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/addchild")
@SessionAttributes("command")
public class AddChildController extends DataEntryFormController{
	
	private static final FormType formType = FormType.ENROLLMENT_ADD;
	private Date dateFormStart = new Date();
	
	AddChildController(){
		super(new DataEntryForm("enrollment", "Enrollment (New)", SystemPermissions.ADD_CHILDREN_DATA));
	}
	
	@RequestMapping(value="/round/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getRound(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
//		List record = sc.getCustomQueryService().getDataBySQLMapResult("select * from round where isActive = true and healthProgramId = " + programId);
		HashMap rounds = (HashMap) sc.getCustomQueryService().getDataBySQLMapResult("select * from round where isActive = true and healthProgramId = " + programId).get(0);
		
		JSONObject json = new JSONObject(rounds);
		sc.closeSession();
		return json.toString();
	}
	
	@RequestMapping(value="/siteList/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getSiteList(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
		List<String> current_centers = sc.getCustomQueryService().getDataBySQL("select mappedId from vaccinationcenter where mappedId in (SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ programId + "  and isActive = true)");
		sc.closeSession();
		return current_centers.toString();
	}
	
	@RequestMapping(value="/locationList/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getLocationList(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
		List<String> current_centers = sc.getCustomQueryService().getDataBySQL("select mappedId from vaccinationcenter where mappedId in (SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ programId + "  and isActive = true)");
		sc.closeSession();
		return current_centers.toString();
	}
	
	@RequestMapping(value="/vaccineList/{programId}" , method=RequestMethod.GET)
	@ResponseBody
	public String getVaccineList(@PathVariable Integer programId){
		
		ServiceContext sc = Context.getServices();
		
		Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId = " + programId).get(0);
//		List list = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM unfepi.vaccine where vaccineId in (SELECT distinct(vaccineId) FROM unfepi.vaccinegap WHERE vaccinationcalendarId = "+calendarId+" ) and vaccine_entity like '%child%'");
		
		String query = "SELECT  v.*, "
				+" (CASE  "
				+" WHEN gaptimeunit = 'month' THEN (30*value)  "
				+" WHEN gaptimeunit = 'week' THEN (7*value)  "
				+" WHEN gaptimeunit = 'day' THEN (0*value)  "
				+" WHEN gaptimeunit = 'year' THEN (365*value)  "
				+" ELSE 9999  "
				+" END )birthdategap "
				+" FROM vaccinegap g  "
				+" LEFT JOIN vaccine v ON v.vaccineId = g.vaccineId "
				+" WHERE g.vaccineGapTypeId = 1 AND g.vaccinationcalendarId = "+calendarId+" "
				+" ORDER BY birthdategap, v.standardOrder ;";
		
		List list = sc.getCustomQueryService().getDataBySQLMapResult(query);
		
		
		JSONArray data =  new JSONArray();
		for (Object object : list) {
			data.put(new JSONObject((HashMap)object));
//			System.out.println(new JSONObject((HashMap)object).toString());
		}
		sc.closeSession();
		return data.toString();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addChildFormView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", new EnrollmentWrapper());
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")EnrollmentWrapper ewr, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView,
								 HttpSession session) throws Exception {
		
		ewr.getChild().setDateEnrolled(ewr.getCenterVisit().getVisitDate());
		List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+ewr.getCenterVisit().getUuid());
		ServiceContext sc = Context.getServices();
		
		for (VaccineSchedule vs : vaccineSchedule) {
			vs.printVaccineSchedule();
		}
		
		Iterator<VaccineSchedule> iter = vaccineSchedule.iterator();
		while (iter.hasNext()) {
			VaccineSchedule vsh = iter.next();
			if (vsh.getStatus().equals(VaccineStatusType.SCHEDULED.name()) || vsh.getStatus().equals(VaccineStatusType.NOT_ALLOWED.name()) || (vsh.getStatus() == null?true:vsh.getStatus().length()==0)) {
				iter.remove();
			}
		}
//		System.out.println("\n\n");
//		for (VaccineSchedule vs : vaccineSchedule) {
//			vs.printVaccineSchedule();
//		}
		
//		System.out.println(ewr.getCenterVisit().getVaccinationCenterId() +"  " + ewr.getCenterVisit().getHealthProgramId());
		new ChildValidator().validateEnrollment(ewr, vaccineSchedule, results);		
		if(results.hasErrors()){	
//			System.out.println(results.toString());
			return showForm(modelAndView, "dataForm");	
		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		String childIdentifier = ewr.getChildIdentifier();
		Child ch = ewr.getChild();
		
		VaccinationCenterVisit centerVisit = ewr.getCenterVisit();
		Address addr = ewr.getAddress();
		
		try{
			Integer healthProgramId = ewr.getCenterVisit().getHealthProgramId();
			Integer roundId = (Integer) sc.getCustomQueryService().getDataByHQL("select roundId from Round where isActive = true and healthProgramId = " + healthProgramId).get(0);
			
			
			ControllerUIHelper.doEnrollment(DataEntrySource.WEB, roundId,
					childIdentifier, ewr.getChildNamed(), ch,
					ewr.getBirthdateOrAge(), ewr.getChildagey(),
					ewr.getChildagem(), ewr.getChildagew(), ewr.getChildaged(),
					addr, centerVisit, ewr.getCompleteCourseFromCenter(),
					vaccineSchedule, ewr.getItemsDistributedL(), ewr.getMuacMeasurement(),
					dateFormStart, user.getUser(), sc);
			sc.commitTransaction();

			String editmessage="Child Enrolled successfully. ";
			
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+childIdentifier));
			
		} catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}		
	}

	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
//			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"});
			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenterOrdered(true, new String[]{"idMapper"});		
			model.addAttribute("vaccinationCenters", centeres);
			List<Vaccinator> vaccinators = sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"});
			model.addAttribute("vaccinators", vaccinators);	
//			List<Vaccine> vaccinesL = sc.getCustomQueryService().getDataByHQL("FROM Vaccine where vaccine_entity like 'CHILD%'") ;
//			model.addAttribute("vaccineList", vaccinesL);
			List<HealthProgram> healthprograms = sc.getCustomQueryService().getDataByHQL("from HealthProgram order by name");
			model.addAttribute("healthprograms", healthprograms);
			List<Location> locations = sc.getCustomQueryService().getDataByHQL("from Location order by name");
			model.addAttribute("locations", locations);
			List<ItemStock> itemStocks = sc.getCustomQueryService().getDataByHQL("from ItemStock");
			model.addAttribute("itemStocks", itemStocks);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		
		} finally {
			sc.closeSession();
		}	
	}
}