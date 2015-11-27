package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.VCenterRegistrationWrapper;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class EditVaccinationCenterController  extends SimpleFormController{

	private static final FormType formType = FormType.VACCINATIONCENTER_CORRECT;
	private Date dateFormStart = new Date();
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		VCenterRegistrationWrapper vaccw = (VCenterRegistrationWrapper) command;
		
		ServiceContext sc = Context.getServices();
		try{
			vaccw.getVaccinationCenter().setEditor(user.getUser());
			sc.getVaccinationService().updateVaccinationCenter(vaccw.getVaccinationCenter());
			sc.commitTransaction();
			
			
			List<VaccinationCenterVaccineDay> list2 = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccw.getVaccinationCenter().getMappedId(), null, null, false);
			for (int i = 0; i < list2.size(); i++) {
				sc.getVaccinationService().deleteVaccinationCenterVaccineDay(list2.get(i));
			}
			
			sc.commitTransaction();
			
			for (Map<String, Object> vdml : vaccw.getVaccineDayMapList()) {
				String[] strarr = (String[]) vdml.get("daylist");
				Vaccine vaccine = (Vaccine)vdml.get("vaccine");
				
				Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();

				for (String dayname : strarr) {
					if(!StringUtils.isEmptyOrWhitespaceOnly(dayname)){
						VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
						vcd.setId(new VaccinationCenterVaccineDayId(vaccw.getVaccinationCenter().getMappedId(), vaccine.getVaccineId(), getDaySelected(dayname, vaccw.getCalendarDays()).getDayNumber()));
						
						av.add(vcd);
					}
				}
				
				for (VaccinationCenterVaccineDay vcdli : av) {
					sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
				}
			}
			
			sc.commitTransaction();
			
			GlobalParams.DBLOGGER.info(IRUtils.convertToString(vaccw.getVaccinationCenter()), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, formType, user.getUser().getUsername()));
			for (Map<String, Object> vdml : vaccw.getVaccineDayMapList()) {
				String[] strarr = (String[]) vdml.get("daylist");
				Vaccine vaccine = (Vaccine)vdml.get("vaccine");
				GlobalParams.DBLOGGER.info("Vaccine="+vaccine.getName()+";Days="+Arrays.toString(strarr), LoggerUtils.getLoggerParams(LogType.TRANSACTION_INSERT, formType, user.getUser().getUsername()));
			}

			return new ModelAndView(new RedirectView("viewVaccinationCenters.htm?action=search&vaccinationcenterid="+vaccw.getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier()));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	private CalendarDay getDaySelected(String dayName, List<CalendarDay> calendarDays){
		for (CalendarDay calendarDay : calendarDays) {
			if(calendarDay.getDayFullName().equalsIgnoreCase(dayName)
					||calendarDay.getDayShortName().equalsIgnoreCase(dayName)){
				return calendarDay;
			}
		}
		return null;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		dateFormStart = new Date();
		
		int centerId = Integer.parseInt(request.getParameter("rid"));
		ServiceContext sc = Context.getServices();
		try{
			VaccinationCenter cen = sc.getVaccinationService().findVaccinationCenterById(centerId, false, new String[]{"idMapper"});
			List<VaccinationCenterVaccineDay> vcvd = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(cen.getMappedId(), null, null, true);
			
			return new VCenterRegistrationWrapper(cen, vcvd);
		}
		catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccination center. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		return new VCenterRegistrationWrapper();
	}
/*	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println(errors.getAllErrors());
		return null;
	}*/
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		
		return new HashMap<String, Object>();
	}
}
