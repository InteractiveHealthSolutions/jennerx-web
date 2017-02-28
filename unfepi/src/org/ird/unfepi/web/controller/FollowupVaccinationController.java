package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.ItemsDistributed;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.validator.VaccinationValidator;
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

import com.mysql.jdbc.StringUtils;


@Controller
@SessionAttributes("command")
@RequestMapping("/followupVaccination")
public class FollowupVaccinationController extends DataEntryFormController{
	
	private static final FormType formType = FormType.FOLLOWUP_ADD;
	private Date dateFormStart = new Date();
	
	FollowupVaccinationController(){
		super(new DataEntryForm("followup", "Follow Up", SystemPermissions.ADD_VACCINATIONS));
	}

	@RequestMapping(value="/oldVaccineList/{childId}" , method=RequestMethod.GET)
	@ResponseBody
	public String getVaccineList(@PathVariable Integer childId){
		ServiceContext sc = Context.getServices();
		List preVacList = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM Vaccination WHERE childId = "+childId+" AND vaccinationStatus NOT IN('NOT_VACCINATED','NOT_GIVEN','INVALID_DOSE')");
		JSONArray data =  new JSONArray();
		for (Object object : preVacList) {
			data.put(new JSONObject((HashMap)object));
//			System.out.println(new JSONObject((HashMap)object).toString());
		}
		sc.closeSession();
		return data.toString();
	}
	
	@RequestMapping(value="/isPreventionActivityOn/{childId}/{visitDate}" , method=RequestMethod.GET)
	@ResponseBody
	public String isPreventionActivityOn(@PathVariable Integer childId, @PathVariable String visitDate){
		ServiceContext sc = Context.getServices();
		
		try {
			Date date = new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(visitDate);
//			System.out.println(childId  + "   " +visitDate + "   "  + WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(date));
			
			List<ItemsDistributed> records = sc.getCustomQueryService().getDataByHQL(
					"from ItemsDistributed where itemDistributedId.mappedId ="+childId+ 
					" and itemDistributedId.distributedDate ='" +WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(date)+"'");
//			System.out.println(records.size());
			
			if(records == null || records.size() == 0 ){
				return "true";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		
		return "false";
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView followupVaccinationView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")VaccinationCenterVisit centerVisit, BindingResult results, 
									HttpServletRequest request, HttpServletResponse response,
									ModelAndView modelAndView)	throws Exception{
		
//		if(centerVisit.getItemsDistributedL() != null && centerVisit.getItemsDistributedL().size() >0){
//			System.out.println("item list size " + centerVisit.getItemsDistributedL().size());
//			for(ItemsDistributed itm : centerVisit.getItemsDistributedL()){
//				System.out.println(itm.getItemDistributedId().getDistributedDate() + " " + itm.getItemDistributedId().getItemRecordNum() + " " + itm.getQuantity()+ " " + itm.getItemDistributedId().getMappedId());
//			}
//		}
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		
		try{
			List<VaccineSchedule> vaccineSchedule = (List<VaccineSchedule>) request.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+centerVisit.getUuid());
			
//			for (VaccineSchedule vs : vaccineSchedule) {
//				vs.printVaccineSchedule();
//			}
			Iterator<VaccineSchedule> iter = vaccineSchedule.iterator();
			while (iter.hasNext()) {
				VaccineSchedule vsh = iter.next();
				if (vsh.getStatus().equals(VaccineStatusType.SCHEDULED.name()) || vsh.getStatus().equals(VaccineStatusType.NOT_ALLOWED.name()) || (vsh.getStatus() == null?true:vsh.getStatus().length()==0)) {
					iter.remove();
				}
			}
			
			new VaccinationValidator().validateVaccinationForm(centerVisit, vaccineSchedule, results, request);
			if(results.hasErrors()){
				return showForm(modelAndView, "dataForm");	
			}
			
			Child child = (Child) request.getSession().getAttribute("childfollowup");
			if(!StringUtils.isEmptyOrWhitespaceOnly(request.getParameter("cnic"))){
				Child c = sc.getChildService().findChildById(child.getMappedId(), false, null);
				c.setNic(request.getParameter("cnic"));
				sc.getChildService().updateChild(c);
			}
			
			Integer healthProgramId = centerVisit.getHealthProgramId();
			Integer roundId = (Integer) sc.getCustomQueryService().getDataByHQL("select roundId from Round where isActive = true and healthProgramId = " + healthProgramId).get(0);
						
			ControllerUIHelper.doFollowup(DataEntrySource.WEB, centerVisit, vaccineSchedule, roundId, dateFormStart, user.getUser(), sc);
			sc.commitTransaction();
			
			String editmessage="Child Followed up successfully. ";
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+child.getIdMapper().getIdentifiers().get(0).getIdentifier()));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		}
		finally{
			sc.closeSession();
		}
	}

	protected VaccinationCenterVisit formBackingObject(HttpServletRequest request) {
		dateFormStart = new Date();
		
		String child_id=request.getParameter("child_id");//child-program-id
		VaccinationCenterVisit vcv = new VaccinationCenterVisit();
		Child child = new Child();
		Vaccination previousVaccination = new Vaccination();
		Vaccination previousRecord = null;
		
		ServiceContext sc = Context.getServices();
		try{
			child = sc.getChildService().findChildById(Integer.parseInt(child_id), true, new String[]{"idMapper"});
			ControllerUIHelper.prepareFollowupDisplayObjects(request, child, sc);
			previousVaccination = ControllerUIHelper.getPreviousVaccination(child.getMappedId(), sc);	
			
			if(previousVaccination == null){
				previousRecord = ControllerUIHelper.getPreviousVaccinationRecord(child.getMappedId(), sc);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "Oops .. Some error occurred. Exception message is:"+e.getMessage());
			request.setAttribute("shouldenableVaccination", false);
		}finally{
			sc.closeSession();
		}
		
		if (previousVaccination != null) {
			vcv.setHealthProgramId(previousVaccination.getRound().getHealthProgramId());
			vcv.setVaccinationCenterId(previousVaccination.getVaccinationCenterId());
			vcv.setVaccinatorId(previousVaccination.getVaccinatorId());
		} else if (previousRecord != null) {
			//if previously only invalid_dose, not_vaccinated, not_given exist
			vcv.setHealthProgramId(previousRecord.getRound().getHealthProgramId());
			vcv.setVaccinationCenterId(previousRecord.getVaccinationCenterId());
			vcv.setVaccinatorId(previousRecord.getVaccinatorId());
		}
		
		vcv.setChildId(child.getMappedId());
		
		return vcv;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try{	
			
			
			List<HealthProgram> healthprograms = sc.getCustomQueryService().getDataByHQL("from HealthProgram");
			model.addAttribute("healthprograms", healthprograms);			
			
//			List<Vaccine> vaccinesL = sc.getCustomQueryService().getDataByHQL("FROM Vaccine where vaccine_entity like 'CHILD%' and isSupplementary = 0") ;
//			model.addAttribute("vaccineList", vaccinesL);
			
			model.addAttribute("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenterOrdered(true, new String[]{"idMapper"}));
			model.addAttribute("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			model.addAttribute("itemStocks", sc.getCustomQueryService().getDataByHQL("from ItemStock"));
		
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}		
	}
}

