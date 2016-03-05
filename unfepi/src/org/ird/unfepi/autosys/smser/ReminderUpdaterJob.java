package org.ird.unfepi.autosys.smser;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage;
import org.irdresearch.smstarseel.data.OutboundMessage.OutboundStatus;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class ReminderUpdaterJob implements Job{

	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		ServiceContext sc = Context.getServices();
		
		GlobalParams.REMINDERJOBLOGGER.info("Running Job: "+jxc.getJobDetail().getFullName());

		try{
			List<ReminderSms> remlist = sc.getReminderService().findReminderSmsRecordByCriteria(null, null, null, null, null, null , null, null, null, REMINDER_STATUS.LOGGED, false, 0, Integer.MAX_VALUE, false, null);
			
			GlobalParams.REMINDERJOBLOGGER.info("Fetched "+remlist.size()+" LOGGED sms");

			for (ReminderSms rem : remlist) 
			{
				TarseelServices tsc = TarseelContext.getServices();
				try{
					OutboundMessage om = tsc.getSmsService().findOutboundMessageByReferenceNumber(rem.getReferenceNumber(), true);
					
					if(!om.getStatus().equals(OutboundStatus.PENDING)
							&& !om.getStatus().equals(OutboundStatus.UNKNOWN))
					{
						if(om.getStatus().equals(OutboundStatus.FAILED)){
							final long currentTime = System.currentTimeMillis();
							final long maxValidTime = om.getDueDate().getTime()+(om.getValidityPeriod()  * 60 * 60 * 1000L);
							final int hoursLeft = (int) ((maxValidTime - currentTime)/(1000 * 60 * 60L));
							
							if(hoursLeft < 0){
								rem.setReminderStatus(REMINDER_STATUS.FAILED);
								rem.setOriginator(om.getOriginator());
								rem.setRecipient(om.getRecipient());
							}
						}
						else{
							if(om.getStatus().equals(OutboundStatus.SENT)){
								rem.setReminderStatus(REMINDER_STATUS.SENT);
							}
							else {
								rem.setReminderStatus(REMINDER_STATUS.MISSED);
							}
						
							final long duedt = om.getDueDate().getTime();
							final long sntdt = om.getSentdate().getTime();
							rem.setHoursDifference(DateUtils.differenceBetweenIntervals(new Date(sntdt), new Date(duedt), TIME_INTERVAL.HOUR));
							rem.setOriginator(om.getOriginator());
							rem.setRecipient(om.getRecipient());
							rem.setSentDate(om.getSentdate());
							rem.setSmsCancelReason(om.getFailureCause());
							rem.setText(om.getText());
						}
						
						sc.getReminderService().updateReminderSmsRecord(rem);
					}//end if
					
				}
				catch(Exception e){
					e.printStackTrace();
					GlobalParams.REMINDERJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
				}
				finally{
					tsc.closeSession();
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
		String latestCron = Context.getSetting(GlobalParams.REMINDER_UPDATER_CRON_SETTING, GlobalParams.REMINDER_UPDATER_DEFAULT_CRON);
		if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
			try 
			{
				CronTrigger reminderUpdaterTrigger = new CronTrigger(GlobalParams.REMINDER_UPDATER_TRIGGERNAME, GlobalParams.REMINDER_UPDATER_TRIGGERGROUP, latestCron);
				JobDetail job = jxc.getScheduler().getJobDetail(GlobalParams.REMINDER_UPDATER_JOBNAME, GlobalParams.REMINDER_UPDATER_JOBGROUP);
				jxc.getScheduler().deleteJob(GlobalParams.REMINDER_UPDATER_JOBNAME, GlobalParams.REMINDER_UPDATER_JOBGROUP);
				jxc.getScheduler().scheduleJob(job, reminderUpdaterTrigger);

				GlobalParams.REMINDERJOBLOGGER.info("Cron Updated from "+latestCron+" to "+reminderUpdaterTrigger.getCronExpression());
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
