package org.ird.unfepi.autosys;

import java.text.ParseException;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.reporting.DataDumperJob;
import org.ird.unfepi.autosys.reporting.NotifierReschedularJob;
import org.ird.unfepi.context.Context;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class ReportingSystem 
{
	private static ReportingSystem _instance;

	private Scheduler schedular;
	
	private ReportingSystem(){
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	public static void instantiateReportingSystem(Scheduler scheduler) throws ParseException, SchedulerException{
		if(_instance == null)
		{
			_instance = new ReportingSystem();
			_instance.schedular = scheduler;
			
			// *********** Notifier Rescheduler JOb
			String notifierReschedularCron = Context.getSetting(GlobalParams.NOTIFER_RESCH_CRON_SETTING, GlobalParams.NOTIFER_RESCH_DEFAULT_CRON);
			//_instance.notifierManager = NotifierManager.instantiateNotifierManager(scheduler, notifierReschedularCron);
			JobDetail notifierReschedularJob = new JobDetail(GlobalParams.NOTIFIER_RESCHED_JOBNAME, GlobalParams.NOTIFIER_RESCHED_JOBGROUP, NotifierReschedularJob.class);
			CronTrigger notifierReschedularTrigger = new CronTrigger(GlobalParams.NOTIFIER_RESCHED_TRIGGERNAME, GlobalParams.NOTIFIER_RESCHED_TRIGGERGROUP, notifierReschedularCron);
			try{
				_instance.schedular.scheduleJob(notifierReschedularJob, notifierReschedularTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}
			
			// *********** Data DUMPER Rescheduler JOb
			String dataDumperCron = Context.getSetting(GlobalParams.DATA_DUMPER_CRON_SETTING, GlobalParams.DATA_DUMPER_DEFAULT_CRON);
			JobDetail dataDumperJob = new JobDetail(GlobalParams.DATA_DUMPER_JOBNAME, GlobalParams.DATA_DUMPER_JOBGROUP, DataDumperJob.class);
			CronTrigger dataDumperTrigger = new CronTrigger(GlobalParams.DATA_DUMPER_TRIGGERNAME, GlobalParams.DATA_DUMPER_TRIGGERGROUP, dataDumperCron);
			try{
				_instance.schedular.scheduleJob(dataDumperJob, dataDumperTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}
						
		}
		else
		{
			throw new InstantiationError("An instance of ReportingSystem already exists");
		}
	}

	public static ReportingSystem getInstance() {
		if(_instance == null){
			throw new InstantiationError("ReportingSystem not instantiated");
		}
		return _instance;
	}

	/*public Properties getProps() {
		return props;
	}*/

	public Scheduler getSchedular() {
		return schedular;
	}

	/*public NotifierManager getNotifierManager() {
		return notifierManager;
	}*/
}
