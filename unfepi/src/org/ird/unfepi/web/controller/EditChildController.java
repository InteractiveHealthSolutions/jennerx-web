package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.ChildDataBean;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.ValidatorUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class EditChildController extends DataEditFormController {
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		LoggedInUser user=UserSessionUtils.getActiveUser(request);

		ServiceContext sc = Context.getServices();

		ChildDataBean cb = (ChildDataBean) command;

		String editSection = request.getParameter("editSection");
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly(editSection)){
				if(editSection.equalsIgnoreCase("biographic")){
					Child child= cb.getChild();
					if(StringUtils.isEmptyOrWhitespaceOnly(child.getFirstName())){
						child.setFirstName("NO NAME");
					}
					child.setEditor(user.getUser());
					
					sc.getChildService().updateChild(child);

					GlobalParams.DBLOGGER.info(IRUtils.convertToString(child), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CHILD_BIOGRAPHIC_CORRECT, user.getUser().getUsername()));
				}
				else if(editSection.equalsIgnoreCase("address")){
					Address add = ((ChildDataBean)command).getAddress();
					add.setEditor(user.getUser());
					sc.getDemographicDetailsService().updateAddress(add);
					
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(add), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.ADDRESS_CORRECT, user.getUser().getUsername()));
				}
				else if(editSection.equalsIgnoreCase("program")){
					Child child = cb.getChild();

					STATUS uneditedChildStatus = (STATUS) request.getSession().getAttribute("uneditedChildStatus"+child.getMappedId());
					Boolean uneditedChildReminderPreference = (Boolean) request.getSession().getAttribute("uneditedChildReminderPreference"+child.getMappedId());
					Boolean uneditedChildIncentivePreference = (Boolean) request.getSession().getAttribute("uneditedChildIncentivePreference"+child.getMappedId());

					child.setEditor(user.getUser());
					
					//child terminated from followup? cancel all vaccine reminders
					if(uneditedChildStatus.name().equalsIgnoreCase(STATUS.FOLLOW_UP.name())
							&& child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name())){
						List<ReminderSms> reml = sc.getReminderService().findReminderSmsRecordByCriteria(child.getMappedId(), null, null, new ReminderType[]{ReminderType.NEXT_VACCINATION_REMINDER}, null, null, null, null, null, REMINDER_STATUS.SCHEDULED, false, 0, 100, false, null);
					
						for (ReminderSms reminderSms : reml) {
							reminderSms.setReminderStatus(REMINDER_STATUS.CANCELLED);
							reminderSms.setSmsCancelReason((reminderSms.getSmsCancelReason()==null?"":reminderSms.getSmsCancelReason())+new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date())+" Child status "+child.getStatus()+";");
						
							sc.getReminderService().updateReminderSmsRecord(reminderSms);
						}
						
						GlobalParams.DBLOGGER.info(IRUtils.convertToString(child), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CHILD_BIOGRAPHIC_CORRECT, user.getUser().getUsername()));
					}
					sc.getChildService().updateChild(child);

					if(cb.getPreference().getHasApprovedReminders() != uneditedChildReminderPreference){
						ControllerUIHelper.doChangePreference(cb.getPreference(), user.getUser(), sc);
						
						GlobalParams.DBLOGGER.info(IRUtils.convertToString(cb.getPreference()), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CHANGE_PREFERENCE, user.getUser().getUsername()));
					}
					
					ControllerUIHelper.handleNonEnrollmentContactInfo(cb.getPreference(), cb.getContactPrimary(), cb.getContactSecondary(), user.getUser(), sc);
					
					GlobalParams.DBLOGGER.info(IRUtils.convertToString(cb), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.CONTACT_NUMBER_CORRECT, user.getUser().getUsername()));
				}
				else if(editSection.equalsIgnoreCase("vaccination")){
					for (Vaccination ved : cb.getVaccinations()) {
						ved.setEditor(user.getUser());
						sc.getVaccinationService().updateVaccinationRecord(ved);
						
						if(ved.getVaccinationStatus().equals(VACCINATION_STATUS.SCHEDULED)){
							List<ReminderSms> rsms=sc.getReminderService().findByCriteria(null, null, false, null, ved.getVaccinationRecordNum(), false, null); 	
							//verify rsms are equal to daynums in arm
							for (ReminderSms r : rsms) {
								if(r.getReminderStatus().equals(REMINDER_STATUS.SCHEDULED)){
									int hour = r.getDueDate().getHours();
									int min = r.getDueDate().getMinutes();
									int sec = r.getDueDate().getSeconds();
									
									Calendar cal=Calendar.getInstance();
									cal.setTime(new Date(ved.getVaccinationDuedate().getTime()));
									cal.set(Calendar.HOUR_OF_DAY, hour);
									cal.set(Calendar.MINUTE, min);
									cal.set(Calendar.SECOND, sec);
									cal.add(Calendar.DATE, r.getDayNumber());
									
									if(!DateUtils.datesEqual(r.getDueDate(), cal.getTime())){
										r.setDueDate(cal.getTime());
										r.setEditor(user.getUser());
										sc.getReminderService().updateReminderSmsRecord(r);
									}
								}
								else {
									r.setDescription((r.getDescription() == null?"":r.getDescription())+"-vaccination due date changed.");
								}
							}
						}
						
						GlobalParams.DBLOGGER.info(IRUtils.convertToString(ved), LoggerUtils.getLoggerParams(LogType.TRANSACTION_UPDATE, FormType.FOLLOWUP_CORRECT, user.getUser().getUsername()));
					}
				}
				sc.commitTransaction();
			}
			String editmessage="Child Edited Successfully";
	
			return new ModelAndView(new RedirectView("childDashboard.htm?action=search&editOrUpdateMessage="+editmessage+"&childId="+request.getParameter("programId")));
		}
		catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		ChildDataBean cb = (ChildDataBean) command;
		ServiceContext sc = Context.getServices();

		try{
			String editSection = request.getParameter("editSection");
			if(editSection.equalsIgnoreCase("biographic")){
				ValidatorUtils.validateBiographics(DataEntrySource.WEB, cb.getChildNamed(), cb.getChild(), false, cb.getBirthdateOrAge(), cb.getChildagey(), cb.getChildagem(), cb.getChildagew(), cb.getChildaged(), null, errors, sc, true);
			}
			else if(editSection.equalsIgnoreCase("address")){
				ValidatorUtils.validateAddress(DataEntrySource.WEB, cb.getAddress(), null, errors, true);
			}
			else if(editSection.equalsIgnoreCase("program")){
				ValidatorUtils.validateChildNIC(DataEntrySource.WEB, cb.getChild().getMappedId(), cb.getChild().getNic(), false, null, errors, true, sc);
				ValidatorUtils.validateChildStatus(DataEntrySource.WEB, cb.getChild(), null, errors, true);
				ValidatorUtils.validateReminderAndContactInfo(DataEntrySource.WEB, cb.getPreference(), cb.getContactPrimary(), cb.getContactSecondary(), null, errors, sc, false);
			}
			else if(editSection.equalsIgnoreCase("vaccination")){
				for (Vaccination v : cb.getVaccinations()) {
					ValidatorUtils.validateVaccination(DataEntrySource.WEB, cb.getChild(), v, null, errors, sc, true);
				}
			}
		}
		finally{
			sc.closeSession();
		}

		return super.processFormSubmission(request, response, command, errors);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String programId=request.getParameter("programId");
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		
		ChildDataBean ew = new ChildDataBean();

		if(user==null){
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		}else{
			ServiceContext sc = Context.getServices();
			Child p;
			try{
				p = sc.getChildService().findChildById(programId, false, new String[]{"idMapper", "enrollmentVaccine"});
				if(p==null){
					request.setAttribute("errorMessage", "Oops .. Some error occurred. Child was not found.");
				}
				ew.setChild(p);
				List<Address> addl = sc.getDemographicDetailsService().getAddress(p.getMappedId(), false, null);
				ew.setAddress(addl.size()>0?addl.get(0):new Address());
				ew.setPreference(sc.getChildService().findLotterySmsByChild(p.getMappedId(), false, 0, 10, null).get(0));
				List<ContactNumber> conl = sc.getDemographicDetailsService().getContactNumber(p.getMappedId(), true, null);
				for (ContactNumber cn : conl) {
					if(cn.getNumberType().name().equalsIgnoreCase(ContactType.PRIMARY.name())){
						ew.setContactPrimary(cn.getNumber());
					}
					else if(cn.getNumberType().name().equalsIgnoreCase(ContactType.SECONDARY.name())){
						ew.setContactSecondary(cn.getNumber());
					}
				}
				
				List<Vaccination> vl = sc.getVaccinationService().findByCriteria(p.getMappedId(), null, null, 0, 20, false, new String[]{"vaccine"});
				Collections.reverse(vl);
				ew.setVaccinations(vl);
			}
			catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		
			request.getSession().setAttribute("uneditedChildStatus"+ew.getChild().getMappedId(), ew.getChild().getStatus());
			request.getSession().setAttribute("uneditedChildReminderPreference"+ew.getChild().getMappedId(), ew.getPreference().getHasApprovedReminders());
			request.getSession().setAttribute("uneditedChildIncentivePreference"+ew.getChild().getMappedId(), ew.getPreference().getHasApprovedLottery());
			
		}

		return ew;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			model.put("editSection", request.getParameter("editSection"));
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, 100, true, new String[]{"idMapper"}));
			
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		}
		finally{
			sc.closeSession();
		}
		
		return model;
	}
	
}
