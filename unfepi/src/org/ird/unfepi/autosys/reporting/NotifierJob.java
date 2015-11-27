package org.ird.unfepi.autosys.reporting;

import java.io.IOException;
import java.text.ParseException;

import javax.mail.MessagingException;
import javax.naming.OperationNotSupportedException;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.utils.NotifierUtil;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierType;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.itextpdf.text.DocumentException;

public class NotifierJob implements Job
{
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		ServiceContext sc = Context.getServices();
		try{
			//notifier must exist in DB
			Notifier not = sc.getReportService().findByCritria(jxc.getJobDetail().getName(), null, null, true, 0, Integer.MAX_VALUE, new String[]{"notifierRecipient"}).get(0);
			
			GlobalParams.NOTIFIERJOBLOGGER.info("Running Job: "+not.getNotifierName());
	
			if(not.getNotifierType().equals(NotifierType.EMAIL_CSV)){
				try {
					NotifierUtil.notifyViaCsvEmail(not);
				} catch (IOException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				} catch (MessagingException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				}
			}
			else if(not.getNotifierType().equals(NotifierType.EMAIL_PDF)){
				try {
					NotifierUtil.notifyViaPdfEmail(not);
				} catch (IOException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				} catch (DocumentException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				} catch (MessagingException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				}
			}
			else if(not.getNotifierType().equals(NotifierType.SMS)){
				try {
					NotifierUtil.notifyViaSms(not);
				} catch (OperationNotSupportedException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				}
			}
			
			// reschedule if cron changed
			String latestCron = not.getNotifierCron();
			if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
				try 
				{
					CronTrigger notifierTrigger = new CronTrigger(not.getNotifierName(), GlobalParams.NOTIFIER_TRIGGERGROUP, latestCron);
					JobDetail job = jxc.getScheduler().getJobDetail(not.getNotifierName(), GlobalParams.NOTIFIER_JOBGROUP);
					jxc.getScheduler().deleteJob(not.getNotifierName(), GlobalParams.NOTIFIER_JOBGROUP);
					jxc.getScheduler().scheduleJob(job, notifierTrigger);
				} 
				catch (ParseException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName()+"--latestCron:"+latestCron ,e);
					e.printStackTrace();
				} 
				catch (SchedulerException e) {
					GlobalParams.NOTIFIERJOBLOGGER.error(not.getNotifierName() ,e);
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			GlobalParams.NOTIFIERJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
}
