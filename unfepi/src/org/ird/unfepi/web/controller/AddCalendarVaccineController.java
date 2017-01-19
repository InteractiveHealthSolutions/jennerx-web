package org.ird.unfepi.web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.VaccineRegistrationWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinationCalendar;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccineGapType;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.model.VaccinePrerequisiteId;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.CalendarVaccineValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
//@SessionAttributes("command")
@RequestMapping("/addCalendarVaccine")
public class AddCalendarVaccineController extends DataEntryFormController{
	
	private static final FormType formType = FormType.VACCINE_SCHEDULE_ADD;
	private Date dateFormStart = new Date();
	
	public AddCalendarVaccineController() {
		super(new DataEntryForm("calendar_vaccine", "Calendar Vaccine (New)", SystemPermissions.ADD_VACCINE_SCHEDULE));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addCalendarVaccineView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")VaccineRegistrationWrapper wrapper, BindingResult results,
								 HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) throws Exception {
		
		if(wrapper.getVaccineGapList() !=null && wrapper.getVaccineGapList().size() >0){
			for (Iterator<VaccineGap> iterator = wrapper.getVaccineGapList().iterator(); iterator.hasNext();) {
				VaccineGap vaccineGap = iterator.next();
				if(vaccineGap.getValue() == 0 && vaccineGap.getGapTimeUnit() == null) {
					iterator.remove();
				}
			}
		}
		
		new CalendarVaccineValidator().validate(wrapper, results);
		if(results.hasErrors()){	
//			System.out.println(results.toString());
			
			if(wrapper.getVaccinePrerequisites() != null){
				modelAndView.addObject("preReq_selected", Arrays.asList(wrapper.getVaccinePrerequisites()));
			}
			modelAndView.addObject("binding_errors", true);
			return showForm(modelAndView, "dataForm");	
		}
		
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try {
			
			if(wrapper.getVaccineGapList() !=null && wrapper.getVaccineGapList().size() >0){
				for(VaccineGap vp :wrapper.getVaccineGapList()){
					sc.getCustomQueryService().save(vp);
				}
			}
			
			if(wrapper.getVaccinePrerequisites() != null && wrapper.getVaccinePrerequisites().length >0){
				for (String preq : wrapper.getVaccinePrerequisites()) {
					
					VaccinePrerequisiteId vpr_id = new VaccinePrerequisiteId();
					vpr_id.setVaccineId(wrapper.getVaccineId().shortValue());
					vpr_id.setVaccinePrerequisiteId(Short.parseShort(preq));
					vpr_id.setVaccinationcalendarId(wrapper.getVaccinationCalendarId());
					
					VaccinePrerequisite vpr = new VaccinePrerequisite();
					vpr.setVaccinePrerequisiteId(vpr_id);
					vpr.setMandatory(false);
					
					sc.getCustomQueryService().save(vpr);
				}
			}
			
			sc.commitTransaction();
//			EncounterUtil.createVaccineRegistrationEncounter(vaccine, DataEntrySource.WEB, dateFormStart, user.getUser(), sc);
//			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccine), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			
			return new ModelAndView(new RedirectView("viewVaccinationCalendar.htm?calendarId="+wrapper.getVaccinationCalendarId()));
			
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
		
		} finally {
			sc.closeSession();
		}
	}
	
	protected VaccineRegistrationWrapper formBackingObject(HttpServletRequest request)
	{
		String calendarId = request.getParameter("calendarId");
		
		dateFormStart = new Date();
		VaccineRegistrationWrapper vrw = new VaccineRegistrationWrapper();
		if(calendarId != null){
			vrw.setVaccinationCalendarId(Integer.parseInt(calendarId));
		}
		return vrw;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			
			String calendarId = request.getParameter("calendarId");
//			model.addAttribute("calendarId", calendarId);
			List<VaccinationCalendar> vaccinationCalendarL = sc.getCustomQueryService().getDataByHQL("from VaccinationCalendar where calenderId = " + calendarId) ;
			model.addAttribute("vaccinationCalendarList" , vaccinationCalendarL);
			List<VaccineGapType> vaccineGapTypeL = sc.getCustomQueryService().getDataByHQL("from VaccineGapType") ;
			model.addAttribute("vaccineGapTypeList" , vaccineGapTypeL);
			
			List<HashMap> vaccineL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine where vaccine_entity like 'CHILD%' OR vaccine_entity is null ORDER BY vaccineId "); 
			model.addAttribute("vaccineList" , vaccineL);
			
			List<HashMap> vacPreReq = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine where vaccine_entity like 'CHILD%' AND vaccineId IN "
					+ "(SELECT distinct(vaccineId) FROM vaccinegap where vaccinationcalendarId = "+ calendarId +" ) ORDER BY vaccineId"); 
			model.addAttribute("vacPreReq" , vacPreReq);
			 
			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
}
