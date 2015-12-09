package org.ird.unfepi.autosys.smser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.utils.ReminderUtils;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.mysql.jdbc.StringUtils;

public class ReminderPusherJob implements Job
{
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		ServiceContext sc = Context.getServices();
		try{
			Calendar cal1 = Calendar.getInstance();// date before 1 years
			cal1.add(Calendar.YEAR, -1);
			cal1.set(Calendar.HOUR_OF_DAY, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.SECOND, 0);
			
			Calendar cal2 = Calendar.getInstance();
			cal2.set(Calendar.HOUR_OF_DAY, 23);// date of today
			cal2.set(Calendar.MINUTE, 59);
			cal2.set(Calendar.SECOND, 59);
			
			GlobalParams.REMINDERJOBLOGGER.info("Running Job: "+jxc.getJobDetail().getFullName());

			List<ReminderSms> remlist = sc.getReminderService().findReminderSmsRecordByCriteria(null, null, null, null, null, cal1.getTime(), cal2.getTime(),null , null, REMINDER_STATUS.PENDING, false, 0, Integer.MAX_VALUE, false, new String[]{"vaccination", "reminder"});
			
			GlobalParams.REMINDERJOBLOGGER.info("Fetched "+remlist.size()+" PENDING sms");
			
			for (ReminderSms rem : remlist) 
			{
				try{
					String recipient = "";
					try {
						recipient = sc.getDemographicDetailsService().getUniquePrimaryContactNumber(rem.getVaccination().getChildId(), true, null).getNumber();
					}
					catch (Exception e) {
						e.printStackTrace();
						GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);

						String msg = "Exception while finding PRIMARY contact number for child "+rem.getVaccination().getChildId()+":"+e.getMessage()+" for reminder"+rem.getReminder().getRemindername() + "Reminder Sms ID:"+rem.getRsmsRecordNum();
						
						try{
							EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread(rem.getReminder().getRemindername() + "Exception finding PRIMARY contact for child "+rem.getVaccination().getChildId(), msg);
						}
						catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
					String referenceNumber = null;
					String text = ReminderUtils.pickupRandomReminderText(rem, sc);
					int validityPeriod = 0;
					PeriodType vp = null;
					
					Boolean createReminder = false;
					
					// LOTTERY SMSES and there is a valid recipient else should wait for it forever
					if (rem.getReminder().getReminderType().equals(ReminderType.LOTTERY_WON_REMINDER)) {
						if (!(recipient == null || recipient.length() < 6)) 
						{
							createReminder = true;
							
							validityPeriod = 52;
							vp = PeriodType.WEEK;
						}
						else if(StringUtils.isEmptyOrWhitespaceOnly(recipient) || recipient.length() < 9){
							createReminder = false;
							rem.setRecipient(recipient);
							rem.setReminderStatus(REMINDER_STATUS.MISSED);
							rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+"Invalid Cell Num;");
						}
						else if(StringUtils.isEmptyOrWhitespaceOnly(rem.getVaccination().getChild().getNic()) 
								|| rem.getVaccination().getChild().getNic().length() < 12){
							createReminder = false;
							rem.setRecipient(recipient);
							rem.setReminderStatus(REMINDER_STATUS.MISSED);
							rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+"Invalid CNIC;");
						}
					}
					// VACCINATION REMINDERS
					else if (rem.getReminder().getReminderType().equals(ReminderType.NEXT_VACCINATION_REMINDER)) 
					{
						if (!(recipient == null || recipient.length() < 6)) {
							
							List<LotterySms> lsl = sc.getChildService().findLotterySmsByChild(rem.getVaccination().getChildId(), true, 0, 2,  null);
							final long remmillis = rem.getDueDate().getTime();
							
							int dayDifferenceNowAndDuedate = DateUtils.differenceBetweenIntervals(new Date(), new Date(remmillis), TIME_INTERVAL.DAY);

							// whether to send sms or not
							// if daynum is less than 0  and todays date got greater than reminder duedate then mark it MISSED
							// or if daynum is greater than or equal to 0 and reminder duedate have passed X days then mark it MISSED
							if((rem.getDayNumber() < 0 && dayDifferenceNowAndDuedate > 0)
									|| (rem.getDayNumber() >= 0 && dayDifferenceNowAndDuedate > GlobalParams.MAX_REMINDER_1_7_VALIDITY_DAYS))
							{
								rem.setRecipient(recipient);
								rem.setReminderStatus(REMINDER_STATUS.MISSED);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date())+"Remindersms missed the day;");
								rem.setHoursDifference(DateUtils.differenceBetweenIntervals(new Date(), new Date(remmillis), TIME_INTERVAL.HOUR));
							}
							else if(lsl.size() > 0 && lsl.get(0).getHasApprovedReminders() != null && !lsl.get(0).getHasApprovedReminders()){
								rem.setRecipient(recipient);
								rem.setReminderStatus(REMINDER_STATUS.OPTED_OUT);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date())+"Reminder Appoval "+lsl.get(0).getHasApprovedReminders()+";");
							}
							// vaccination was not found PENDING on the day then reminder should be marked as CANCELLED
							else if(!rem.getVaccination().getVaccinationStatus().equals(VACCINATION_STATUS.PENDING)){
								rem.setRecipient(recipient);
								rem.setReminderStatus(REMINDER_STATUS.CANCELLED);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date())+" Vaccination status "+rem.getVaccination().getVaccinationStatus()+";");
							}
							// Child was not found under FOLLOW_UP on the day then reminder should be marked as CANCELLED
							else if(!rem.getVaccination().getChild().getStatus().equals(STATUS.FOLLOW_UP)){
								rem.setRecipient(recipient);
								rem.setReminderStatus(REMINDER_STATUS.CANCELLED);
								rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date())+" Child status "+rem.getVaccination().getChild().getStatus()+";");
							}
							else{
								createReminder = true;
								
								validityPeriod = 10;
								vp = PeriodType.DAY;
								if(rem.getDayNumber() == -1){
									//to give threshold of 1 hour to reminders 
									//to prevent the least chance of getting MISSED (if they are scheduled at the very end of day)
									validityPeriod = 1;
								}
								else {
									validityPeriod = GlobalParams.MAX_REMINDER_1_7_VALIDITY_DAYS;
								}
							}
						}
						else {// if no valid recipient number mark as MISSED
							rem.setRecipient(recipient);
							rem.setReminderStatus(REMINDER_STATUS.MISSED);
							rem.setSmsCancelReason((rem.getSmsCancelReason()==null?"":rem.getSmsCancelReason())+"Invalid Cell Num;");
						}
					}
					
					if (createReminder) {
						TarseelServices tsc = TarseelContext.getServices();
						try{
							referenceNumber  = tsc.getSmsService().createNewOutboundSms(recipient, text, rem.getDueDate(), Priority.HIGH, validityPeriod, vp, GlobalParams.SMS_TARSEEL_PROJECT_ID, null);
							tsc.commitTransaction();
							
							rem.setReferenceNumber(referenceNumber);
							rem.setReminderStatus(REMINDER_STATUS.LOGGED);
						}
						catch(Exception e){
							e.printStackTrace();
							GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
							GlobalParams.REMINDERJOBLOGGER.error(IRUtils.convertToString(rem));
						}
						finally{
							tsc.closeSession();
						}
					}
					
					sc.getReminderService().updateReminderSmsRecord(rem);
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
					String msg = "Exception while pushing reminder "+e.getMessage();
					
					try{
						EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("Exception while pushing reminder "+e.getMessage(), msg+":rsmsnum:"+rem.getRsmsRecordNum());
					}
					catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}//end for
			
			sc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
		}
		finally{
			sc.closeSession();
		}
		
		// reschedule if cron changed
		String latestCron = Context.getSetting(GlobalParams.REMINDER_PUSHER_CRON_SETTING, GlobalParams.REMINDER_PUSHER_DEFAULT_CRON);
		if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
			try 
			{
				CronTrigger reminderPusherTrigger = new CronTrigger(GlobalParams.REMINDER_PUSHER_TRIGGERNAME, GlobalParams.REMINDER_PUSHER_TRIGGERGROUP, latestCron);
				JobDetail job = jxc.getScheduler().getJobDetail(GlobalParams.REMINDER_PUSHER_JOBNAME, GlobalParams.REMINDER_PUSHER_JOBGROUP);
				jxc.getScheduler().deleteJob(GlobalParams.REMINDER_PUSHER_JOBNAME, GlobalParams.REMINDER_PUSHER_JOBGROUP);
				jxc.getScheduler().scheduleJob(job, reminderPusherTrigger);
				
				GlobalParams.REMINDERJOBLOGGER.info("Cron Updated from "+latestCron+" to "+reminderPusherTrigger.getCronExpression());
			} 
			catch (ParseException e) {
				e.printStackTrace();
				GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
			} 
			catch (SchedulerException e) {
				e.printStackTrace();
				GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
			}
		}
	}
}
