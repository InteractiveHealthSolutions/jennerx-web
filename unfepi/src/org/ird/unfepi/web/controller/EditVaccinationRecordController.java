package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.reporting.ExceptionUtil;
import org.ird.unfepi.utils.reporting.LoggerUtil;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditVaccinationRecordController extends SimpleFormController{
	
	private static final FormType formType = FormType.FOLLOWUP_CORRECT;
			
	@SuppressWarnings("deprecation")
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);

		ServiceContext sc = Context.getServices();
		Vaccination currvacc=(Vaccination)command;
		try{
			//Date vaccinationDate=(Date) request.getSession().getAttribute("vaccinationDate"+currvacc.getVaccinationRecordNum());
			Date vaccinationDuedate=(Date) request.getSession().getAttribute("vaccinationDuedate"+currvacc.getVaccinationRecordNum());
			//Child child=(Child) request.getSession().getAttribute("child"+currvacc.getVaccinationRecordNum());

			try{
				request.getSession().removeAttribute("vaccinationDate"+currvacc.getVaccinationRecordNum());
				request.getSession().removeAttribute("vaccinationDuedate"+currvacc.getVaccinationRecordNum());
				request.getSession().removeAttribute("child"+currvacc.getVaccinationRecordNum());
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			currvacc.setEditor(user.getUser());
			
			sc.getVaccinationService().updateVaccinationRecord(currvacc);
			
			if(currvacc.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING))
			{
				List<ReminderSms> rsms=sc.getReminderService().findByCriteria(null, null, false, null, currvacc.getVaccinationRecordNum(), false, null); 	
				String prefTime = currvacc.getPreferredReminderTiming();
				if(prefTime == null){
					try{
					prefTime = sc.getChildService().findLotterySmsByChild(currvacc.getChildId(), true, 0, 2, null).get(0).getPreferredSmsTiming();
					}
					catch (Exception e) {
						e.printStackTrace();
						GlobalParams.FILELOGGER.error(formType.name() + " No lotterySms preference info available ", e);
					}
				}	
				//verify rsms are equal to daynums in arm
				for (ReminderSms r : rsms) {
					if(r.getReminderStatus().equals(REMINDER_STATUS.PENDING)){
						int hour = r.getDueDate().getHours();
						int min = r.getDueDate().getMinutes();
						int sec = r.getDueDate().getSeconds();
						
						Calendar cal=Calendar.getInstance();
						cal.setTime(new Date(currvacc.getVaccinationDuedate().getTime()));
						cal.set(Calendar.HOUR_OF_DAY, hour);
						cal.set(Calendar.MINUTE, min);
						cal.set(Calendar.SECOND, sec);
						cal.add(Calendar.DATE, r.getDayNumber());
						
						r.setDueDate(cal.getTime());
						r.setEditor(user.getUser());
						sc.getReminderService().updateReminderSmsRecord(r);
					}
					else {
						r.setDescription((r.getDescription() == null?"":r.getDescription())+"vaccination due date changed from "+vaccinationDuedate+".");
					}
				}
			}

			sc.commitTransaction();
		}
		catch (Exception e) {
			sc.rollbackTransaction();

			e.printStackTrace();
			LoggerUtil.logIt(ExceptionUtil.getStackTrace(e));
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
		
		return new ModelAndView(new RedirectView("viewVaccinationRecord.htm?action=search&editOrUpdateMessage=VaccinationRecordEditedSuccessfully&recordNum="+currvacc.getVaccinationRecordNum()));
	}
/*	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println(errors.getAllErrors());
		return null;
	}*/
	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA), true));
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA), true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(WebGlobals.BOOLEAN_CONVERTER_TRUE_STRING, WebGlobals.BOOLEAN_CONVERTER_FALSE_STRING, true));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception 
	{
		Integer vaccId=Integer.parseInt(request.getParameter("vaccRecNum"));
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		Vaccination pvacc=new Vaccination();

		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			ServiceContext sc = Context.getServices();
			try{
				pvacc=sc.getVaccinationService().getVaccinationRecord(vaccId,false, new String[]{"vaccine"}, null);
				if(pvacc==null){
					request.setAttribute("errorMessage", "Oops some error occurred. Child Vaccination record requested for was not found.");
					return new Vaccination();
				}
				
				if(pvacc != null) {
					ControllerUIHelper.prepareEditVaccinationDisplayObjects(request, pvacc, sc);
				}
				
				if(pvacc.getVaccinationDate() != null){
					long vaccdatems = pvacc.getVaccinationDate().getTime();
					request.getSession().setAttribute("vaccinationDate"+pvacc.getVaccinationRecordNum(), new Date(vaccdatems));
				}
				
				long vaccduedatems = pvacc.getVaccinationDuedate().getTime();
				request.getSession().setAttribute("vaccinationDuedate"+pvacc.getVaccinationRecordNum(), new Date(vaccduedatems));
				
				return pvacc;
			}
			catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Child Vaccination record threw exception:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		return pvacc;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		boolean session_expired=false;
		Map<String, Object> model=new HashMap<String, Object>();
		
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			session_expired=true;
			request.setAttribute("session_expired", session_expired);
		}
		else{
			Vaccination vacc = (Vaccination) command;
			model.put("vaccine", vacc.getVaccine());
			
			ServiceContext sc = Context.getServices();
			try{
				ControllerUIHelper.prepareVaccinationReferenceData(request, model, 
						sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}), 
						sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
				
				if(request.getParameter("isfirstVaccinationinh") == null){
					//
					/////////////////////////////
					////////////////////////////////
					//TODO model.put("isfirstVaccination",  isFirstVaccination(sc,vacc.getChildId(),vacc.getVaccinationRecordNum()));
				}
				else{
					model.put("isfirstVaccination",  request.getParameter("isfirstVaccinationinh"));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessagev", "An error occurred while retrieving reference data. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}
		
		
		return model;
	}
}
