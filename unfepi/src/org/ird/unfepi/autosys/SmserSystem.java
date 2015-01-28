package org.ird.unfepi.autosys;

import java.text.ParseException;
import java.util.Timer;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.autosys.smser.ReminderPusherJob;
import org.ird.unfepi.autosys.smser.ReminderUpdaterJob;
import org.ird.unfepi.autosys.smser.ResponseReaderJob;
import org.ird.unfepi.autosys.smser.UserSmsUpdaterJob;
import org.ird.unfepi.context.Context;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class SmserSystem 
{
	private static SmserSystem _instance;

	private Scheduler schedular;
	//private ReminderManager reminderManager;
	//private ResponseManager responseManager;
	
	private SmserSystem(){
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	public static void instantiateSmserSystem(Scheduler scheduler) throws ParseException, SchedulerException {
		if(_instance == null)
		{
			_instance = new SmserSystem();
			_instance.schedular = scheduler;
			
			//*****************Reminders Jobs
			String reminderPushCron = Context.getSetting(GlobalParams.REMINDER_PUSHER_CRON_SETTING, GlobalParams.REMINDER_PUSHER_DEFAULT_CRON);
			String reminderUpdateCron = Context.getSetting(GlobalParams.REMINDER_UPDATER_CRON_SETTING, GlobalParams.REMINDER_UPDATER_DEFAULT_CRON);
			//_instance.reminderManager = ReminderManager.instantiateReminderManager(scheduler, reminderPushCron, reminderUpdateCron);
			
			JobDetail reminderPusherJob = new JobDetail(GlobalParams.REMINDER_PUSHER_JOBNAME, GlobalParams.REMINDER_PUSHER_JOBGROUP, ReminderPusherJob.class);
			CronTrigger reminderPusherTrigger = new CronTrigger(GlobalParams.REMINDER_PUSHER_TRIGGERNAME, GlobalParams.REMINDER_PUSHER_TRIGGERGROUP, reminderPushCron);
			try{
				_instance.schedular.scheduleJob(reminderPusherJob, reminderPusherTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}			
			JobDetail reminderUpdaterJob = new JobDetail(GlobalParams.REMINDER_UPDATER_JOBNAME, GlobalParams.REMINDER_UPDATER_JOBGROUP, ReminderUpdaterJob.class);
			CronTrigger reminderUpdaterTrigger = new CronTrigger(GlobalParams.REMINDER_UPDATER_TRIGGERNAME, GlobalParams.REMINDER_UPDATER_TRIGGERGROUP, reminderUpdateCron);
			try{
				_instance.schedular.scheduleJob(reminderUpdaterJob, reminderUpdaterTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}	
			
			//*****************Usersms Jobs
			Timer usersmstimer = new Timer();
			usersmstimer.schedule(new UserSmsUpdaterJob(), 1000*60*5, 1000*60*5);
			
			//***************** Response Reader Job
			String responseReaderCron = Context.getSetting(GlobalParams.RESPONSE_READER_CRON_SETTING, GlobalParams.RESPONSE_READER_DEFAULT_CRON);
			//_instance.responseManager = ResponseManager.instantiateResponseManager(scheduler, responseReaderCron);
			JobDetail responseReaderJob = new JobDetail(GlobalParams.RESPONSE_READER_JOBNAME, GlobalParams.RESPONSE_READER_JOBGROUP, ResponseReaderJob.class);
			CronTrigger responseReaderTrigger = new CronTrigger(GlobalParams.RESPONSE_READER_TRIGGERNAME, GlobalParams.RESPONSE_READER_TRIGGERGROUP, responseReaderCron);
			try{
				_instance.schedular.scheduleJob(responseReaderJob, responseReaderTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		else
		{
			throw new InstantiationError("An instance of SmserSystem already exists");
		}
	}
	
	public static SmserSystem getInstance() {
		if(_instance == null){
			throw new InstantiationError("SmserSystem not instantiated");
		}
		return _instance;
	}

	public Scheduler getSchedular() {
		return schedular;
	}

	/*public ReminderManager getReminderManager() {
		return reminderManager;
	}
*/
/*	public ResponseManager getResponseManager() {
		return responseManager;
	}*/
}
