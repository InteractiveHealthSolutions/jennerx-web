package org.ird.unfepi.autosys.reporting;

import java.text.ParseException;
import java.util.List;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class NotifierReschedularJob implements Job{

	String[] jbarr;
	List<Notifier> list;
	
	private boolean existsInSchduler(String jobname){
		for (String jb : jbarr) {
			if(jb.equalsIgnoreCase(jobname)){
				return true;
			}
		}
		return false;
	}
	
	private boolean existsInNotifierList(String jobname){
		for (Notifier nt : list) {
			if(nt.getNotifierName().equalsIgnoreCase(jobname)){
				return true;
			}
		}
		return false;
	}
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException 
	{
		ServiceContext sc = Context.getServices();

		try{
			GlobalParams.NOTIFIERJOBLOGGER.info("Running rscheduler job");

			list = sc.getReportService().findByCritria(null, null, NotifierStatus.ACTIVE, true, 0, Integer.MAX_VALUE, null);
			try {
				jbarr = jxc.getScheduler().getJobNames(GlobalParams.NOTIFIER_JOBGROUP);
			} catch (SchedulerException e1) {
				e1.printStackTrace();
				GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e1);
			}
			//schedule all notifiers
			for (Notifier nt : list) 
			{
				if(!existsInSchduler(nt.getNotifierName()))
				{
					JobDetail notifierJob = new JobDetail(nt.getNotifierName(), GlobalParams.NOTIFIER_JOBGROUP, NotifierJob.class);
					CronTrigger notifierTrigger = null;
					try {
						notifierTrigger = new CronTrigger(nt.getNotifierName(), GlobalParams.NOTIFIER_TRIGGERGROUP, nt.getNotifierCron());
					} catch (ParseException e) {
						e.printStackTrace();
						GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
					}
					try {
						jxc.getScheduler().scheduleJob(notifierJob, notifierTrigger);
					} catch (SchedulerException e) {
						e.printStackTrace();
						GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
					}
				}
				else{//job exists update trigger
					String latestCron = nt.getNotifierCron();
					if(!((CronTrigger)jxc.getScheduler().getTrigger(nt.getNotifierName(), GlobalParams.NOTIFIER_TRIGGERGROUP)).getCronExpression().equalsIgnoreCase(latestCron)){
						try 
						{
							CronTrigger notifierTrigger = new CronTrigger(nt.getNotifierName(), GlobalParams.NOTIFIER_TRIGGERGROUP, latestCron);
							JobDetail job = jxc.getScheduler().getJobDetail(nt.getNotifierName(), GlobalParams.NOTIFIER_JOBGROUP);
							jxc.getScheduler().deleteJob(nt.getNotifierName(), GlobalParams.NOTIFIER_JOBGROUP);
							jxc.getScheduler().scheduleJob(job, notifierTrigger);
						} 
						catch (ParseException e) {
							GlobalParams.NOTIFIERJOBLOGGER.error(nt.getNotifierName()+"--latestCron:"+latestCron ,e);
							e.printStackTrace();
						} 
						catch (SchedulerException e) {
							GlobalParams.NOTIFIERJOBLOGGER.error(nt.getNotifierName() ,e);
							e.printStackTrace();
						}
					}
				}
			
				//remove all from scheduler that are not needed/discarded now
				for (String schjb : jbarr) {
					if(!existsInNotifierList(schjb)){
						try {
							jxc.getScheduler().deleteJob(schjb, GlobalParams.NOTIFIER_JOBGROUP);
						} catch (SchedulerException e) {
							e.printStackTrace();
							GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
						}
					}
				}
				
				// reschedule if cron changed
				String latestCron = Context.getSetting(GlobalParams.NOTIFER_RESCH_CRON_SETTING, GlobalParams.NOTIFER_RESCH_DEFAULT_CRON);
				if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
					try 
					{
						CronTrigger notifierReschedularTrigger = new CronTrigger(GlobalParams.NOTIFIER_RESCHED_TRIGGERNAME, GlobalParams.NOTIFIER_RESCHED_TRIGGERGROUP, latestCron);
						JobDetail job = jxc.getScheduler().getJobDetail(GlobalParams.NOTIFIER_RESCHED_JOBNAME, GlobalParams.NOTIFIER_RESCHED_JOBGROUP);
						jxc.getScheduler().deleteJob(GlobalParams.NOTIFIER_RESCHED_JOBNAME, GlobalParams.NOTIFIER_RESCHED_JOBGROUP);
						jxc.getScheduler().scheduleJob(job, notifierReschedularTrigger);
					} 
					catch (ParseException e) {
						e.printStackTrace();
						GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
					} 
					catch (SchedulerException e) {
						e.printStackTrace();
						GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			GlobalParams.NOTIFIERJOBLOGGER.error("Error running rscheduler job" ,e);
		}
		finally{
			sc.closeSession();
		}
	}

}
