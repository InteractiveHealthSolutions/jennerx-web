package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.beans.VaccineRegistrationWrapper;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@SessionAttributes("command")
@RequestMapping("/editCalendarVaccine")
public class EditCalendarVaccineController extends DataEditFormController{

	EditCalendarVaccineController() {
		super(new DataEditForm("calendar_vaccine", "Calendar Vaccine (Edit)", SystemPermissions.CORRECT_VACCINES));
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
				
//				System.out.println(" vaccine gap " + vaccineGap.getValue() + "  "  + vaccineGap.getGapTimeUnit());
				
				if(vaccineGap.getValue() == 0 && vaccineGap.getGapTimeUnit() == null) {
					iterator.remove();
				}
			}
		}

		new CalendarVaccineValidator().validate(wrapper, results, false);
		if(results.hasErrors()){	
			System.out.println(results.toString());
			modelAndView.addObject("binding_errors", true);
			return showForm(modelAndView, "dataForm");	
		}

		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		ServiceContext sc = Context.getServices();
		try {
			
			ServiceContext _sc = Context.getServices();
			List<VaccineGap> vacGaps_deL =	_sc.getCustomQueryService().getDataByHQL("from VaccineGap where vaccineId = " + wrapper.getVaccineId() + " and vaccinationcalendarId = " + wrapper.getVaccinationCalendarId());
			for (VaccineGap vg : vacGaps_deL) {
				_sc.getCalendarVaccineService().deleteVaccineGap(vg);
			}
			List<VaccinePrerequisite> vacPreq_deL = _sc.getCustomQueryService().getDataByHQL("from VaccinePrerequisite where vaccinePrerequisiteId.vaccineId = " + wrapper.getVaccineId() + " and vaccinePrerequisiteId.vaccinationcalendarId = " + wrapper.getVaccinationCalendarId());
			for (VaccinePrerequisite vpr : vacPreq_deL) {
				_sc.getCalendarVaccineService().deleteVaccinePrerequisite(vpr);
			}
			_sc.commitTransaction();
			_sc.closeSession();
			
			if(wrapper.getVaccineGapList() !=null && wrapper.getVaccineGapList().size() >0){
				for(VaccineGap vp :wrapper.getVaccineGapList()){
					sc.getCustomQueryService().saveOrUpdate(vp);
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

					sc.getCustomQueryService().saveOrUpdate(vpr);
				}
			}
			sc.commitTransaction();
			
			String calendarId = request.getParameter("calendarId");
			return new ModelAndView(new RedirectView("viewVaccinationCalendar.htm?calendarId="+calendarId));

		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");

		} finally {
			sc.closeSession();
		}
	}

	protected VaccineRegistrationWrapper formBackingObject(HttpServletRequest request)
	{
		String calendarId = request.getParameter("calendarId");
		String vaccineId = request.getParameter("cvaccineId");
		VaccineRegistrationWrapper vrw = new VaccineRegistrationWrapper();
		if(calendarId != null){
			vrw.setVaccinationCalendarId(Integer.parseInt(calendarId));
		}
		if(vaccineId != null){
			vrw.setVaccineId(Integer.parseInt(vaccineId));
		}
		return vrw;
	}

	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			
			String calendarId = request.getParameter("calendarId");
			String vaccineId = request.getParameter("cvaccineId");
			
			List<VaccineGap> vaccineGapList= sc.getCustomQueryService().getDataByHQL("from VaccineGap where vaccineId = " + vaccineId + " and vaccinationcalendarId = " + calendarId);
			model.addAttribute("vaccineGapList" , vaccineGapList);
			List<VaccineGapType> vaccineGapTypeL = sc.getCustomQueryService().getDataByHQL("from VaccineGapType") ;
			model.addAttribute("vaccineGapTypeList" , vaccineGapTypeL);
			List<VaccinationCalendar> vaccinationCalendarL = sc.getCustomQueryService().getDataByHQL("from VaccinationCalendar") ;
			model.addAttribute("vaccinationCalendarList" , vaccinationCalendarL);
			List<Short> vpRecords = sc.getCustomQueryService().getDataByHQL("select vaccinePrerequisiteId.vaccinePrerequisiteId from VaccinePrerequisite where vaccinePrerequisiteId.vaccineId = " + vaccineId + " and vaccinePrerequisiteId.vaccinationcalendarId = " + calendarId);
			model.addAttribute("preReq_selected",vpRecords);
			List<HashMap> vaccineL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine where vaccine_entity like 'CHILD%' OR vaccine_entity is null ORDER BY vaccineId "); 
			model.addAttribute("vaccineList" , vaccineL);
			
			List<HashMap> vacPreReq = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine where vaccine_entity like 'CHILD%' AND vaccineId IN "
					+ "(SELECT distinct(vaccineId) FROM vaccinegap where vaccinationcalendarId = "+ calendarId +" ) ORDER BY vaccineId"); 
			model.addAttribute("vacPreReq" , vacPreReq);
			
			Map<VaccineGapType, VaccineGap> map = new LinkedHashMap<VaccineGapType, VaccineGap>();
			for (VaccineGapType vgt : vaccineGapTypeL) {
				map.put(vgt, null);
				for (VaccineGap vg : vaccineGapList) {
					if(vgt.getVaccineGapTypeId() == vg.getId().getVaccineGapTypeId()){
						map.put(vgt, vg);
					}
				}
			}
			model.addAttribute("map" , map);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
}

