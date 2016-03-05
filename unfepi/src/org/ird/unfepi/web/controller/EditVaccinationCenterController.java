
package org.ird.unfepi.web.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.beans.VCenterRegistrationWrapper;
import org.ird.unfepi.constants.FormType;
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
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class EditVaccinationCenterController extends DataEditFormController{

	private static final FormType formType = FormType.VACCINATIONCENTER_CORRECT;
	
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
			
			List<VaccinationCenterVaccineDay> list2 = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccw.getVaccinationCenter().getMappedId(), null, null, false);
			for (int i = 0; i < list2.size(); i++) {
				sc.getVaccinationService().deleteVaccinationCenterVaccineDay(list2.get(i));
				sc.flushSession();
			}
			
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
		return new ModelAndView(new RedirectView("viewVaccinationCenters.htm?action=search&"+SearchFilter.PROGRAM_ID+"="+vaccw.getVaccinationCenter().getIdMapper().getIdentifiers().get(0).getIdentifier()));
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
		int centerId = Integer.parseInt(request.getParameter("rid"));
		String vaccinedays = request.getParameter("editvaccinedays");
		ServiceContext sc = Context.getServices();
		try{
			VaccinationCenter cen = sc.getVaccinationService().findVaccinationCenterById(centerId, false, new String[]{"idMapper"});
			if(!StringUtils.isEmptyOrWhitespaceOnly(vaccinedays)){
				request.setAttribute("editvaccinedays", true);
				return new VCenterRegistrationWrapper(cen, null);
			}

			return new VCenterRegistrationWrapper(cen, sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(cen.getMappedId(), null, null, false));
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessage", "An error occurred while retrieving vaccination center. Error message is:"+e.getMessage());
		}
		finally{
			request.setAttribute("sc", sc);
			/*sc.closeSession();*///will close after page have been loaded
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
	protected Map referenceData(HttpServletRequest request) throws Exception {
		request.setAttribute("editvaccinedays", request.getParameter("editvaccinedays"));
		return super.referenceData(request);
	}
}

