/*package org.ird.unfepi.autosys;

import java.text.ParseException;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public class IncentiveSystem 
{
	private static IncentiveSystem _instance;

	private Scheduler schedular;
	
	private IncentiveSystem(){
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	public static void instantiateIncentiveSystem(Scheduler scheduler) throws ParseException, SchedulerException{
		if(_instance == null)
		{
			_instance = new IncentiveSystem();
			_instance.schedular = scheduler;
			
			// *********** Incentive Rescheduler JOb
			String pendIncentGenCron = Context.getSetting(GlobalParams.PENDING_INCENTIVE_GENERATOR_CRON_SETTING, GlobalParams.PENDING_INCENTIVE_GENERATOR_DEFAULT_CRON);
			JobDetail pendIncentGenJob = new JobDetail(GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBNAME, GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBGROUP, PendingChildIncentiveGeneratorJob.class);
			CronTrigger pendIncentGenTrigger = new CronTrigger(GlobalParams.PENDING_INCENTIVE_GENERATOR_TRIGGERNAME, GlobalParams.PENDING_INCENTIVE_GENERATOR_TRIGGERGROUP, pendIncentGenCron);
			try{
				_instance.schedular.scheduleJob(pendIncentGenJob, pendIncentGenTrigger);
			}catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		else
		{
			throw new InstantiationError("An instance of IncentiveSystem already exists");
		}
	}

	public static IncentiveSystem getInstance() {
		if(_instance == null){
			throw new InstantiationError("IncentiveSystem not instantiated");
		}
		return _instance;
	}

	public Scheduler getSchedular() {
		return schedular;
	}
}
*/