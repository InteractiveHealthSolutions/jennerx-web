/*package org.ird.unfepi.autosys.incentive;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.ChildLotteryRunner;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.DownloadableType;
import org.ird.unfepi.GlobalParams.LotteryType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.report.CsveeRow;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.FileUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.mysql.jdbc.StringUtils;

public class PendingChildIncentiveGeneratorJob  implements Job {

	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException {
		Session ses = Context.getNewSession();
		ServiceContext sc = Context.getServices();
		try{
			
			String hql = "FROM Vaccination v " +
					" LEFT JOIN FETCH v.vaccine vc " +
					" LEFT JOIN FETCH v.child c " +
					" LEFT JOIN FETCH c.idMapper i " +
					" LEFT JOIN FETCH v.vaccinator vtor " +
					" LEFT JOIN FETCH vtor.idMapper ivtor " +
					" WHERE v.vaccinationStatus IN ( '"+VACCINATION_STATUS.VACCINATED+"' )" +
					" AND v.hasApprovedLottery = true " +
					" AND NOT EXISTS (FROM ChildLottery WHERE vaccinationRecordNum=v.vaccinationRecordNum) ";
			
			ScrollableResults results = ses.createQuery(hql)
		            .setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
			
			String fileName = "PendingLotteryGen_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date());
			File mfile = FileUtils.getFile(fileName+".zip", GlobalParams.INCENTIVE_CSV_DIR);
			FileOutputStream fop = null;
			try {
				fop = new FileOutputStream(mfile);
			}
			catch (IOException e) {
				e.printStackTrace();
			}  
			
			ZipOutputStream zip = new ZipOutputStream(fop);

			zip.putNextEntry(new ZipEntry(fileName+".csv"));
			
			CsveeRow headerRow = new CsveeRow();
			headerRow.addRowElement("encounter_Id".toUpperCase());
			headerRow.addRowElement("Form_Filled_For".toUpperCase());
			headerRow.addRowElement("Form_Filled_Or_Requested_By".toUpperCase());
			headerRow.addRowElement("Form_Filled_By_User_ID".toUpperCase());
			headerRow.addRowElement("encounter_Type".toUpperCase());
			headerRow.addRowElement("date_Encounter_End".toUpperCase());
			headerRow.addRowElement("date_Encounter_Entered".toUpperCase());
			headerRow.addRowElement("detail".toUpperCase());
			// Remaining would be generated dynamically along with data
			int i = 0;

			while(results.next()){
				Vaccination vacc = (Vaccination) results.get(0);
				// Enrollment Vaccine Should NEVER be null
				short enrVacId = vacc.getChild().getEnrollmentVaccineId();
				Float criteriaValueTimeliness = null;
				
				// Validate Criteria Timeliness .......
				// FOR ENROLLMENT doesnt matter... should be 0
				if(enrVacId != vacc.getVaccineId()){
					criteriaValueTimeliness = (float)DateUtils.differenceBetweenIntervals(vacc.getVaccinationDate(), vacc.getVaccinationDuedate(), TIME_INTERVAL.DATE);
				}
				else { //WE would assume that child is on time for all vaccinations on enrollment of MISSING_ENROLLMENT or EXISTING
					criteriaValueTimeliness = (float) 0;
				}
				
				User defUser = new User(Integer.parseInt(Context.getSetting(GlobalParams.DAEMON_USER_ID_SETTING, "")));
				// Run Lottery
				ChildLotteryRunner lotteryRes = ChildLotteryRunner.runLottery(DataEntrySource.WEB, vacc, vacc.getVaccine(), criteriaValueTimeliness ,defUser, sc);
				
				Map<String, Object> encm = EncounterUtil.createLotteryGeneratorEncounter(LotteryType.EXISTING, vacc.getChild().getIdMapper().getIdentifiers().get(0).getIdentifier(), vacc.getChild(), vacc, vacc.getVaccine(), "Approved lottery run by auto lottery generator;", "AUTO", "", null, null, lotteryRes, criteriaValueTimeliness, DataEntrySource.WEB, new Date(), defUser, sc);
				
				CsveeRow dataRow = new CsveeRow();
				Encounter e = (Encounter) encm.get("encounter");
				List<EncounterResults> encr = (List<EncounterResults>) encm.get("encounterresults");
				dataRow.addRowElement(e.getId().getEncounterId());
				dataRow.addRowElement(vacc.getChild().getIdMapper().getIdentifiers().get(0).getIdentifier());
				dataRow.addRowElement(vacc.getVaccinator().getIdMapper().getIdentifiers().get(0).getIdentifier());
				dataRow.addRowElement("AUTO");
				dataRow.addRowElement(e.getEncounterType());
				dataRow.addRowElement(e.getDateEncounterEnd());
				dataRow.addRowElement(e.getDateEncounterEntered());
				dataRow.addRowElement(e.getDetail());
				for (EncounterResults encounterResult : encr) {
					if(i==0){
						String heading = StringUtils.isEmptyOrWhitespaceOnly(encounterResult.getDisplayName())?encounterResult.getId().getElement():encounterResult.getDisplayName();
						headerRow.addRowElement(heading);
					}
					dataRow.addRowElement(encounterResult.getValue());
				}


				if(i==0){
					zip.write(headerRow.getRowAsSB().toString().getBytes());
				}
				i++;

				zip.write(dataRow.getRowAsSB().toString().getBytes());
			}

			zip.closeEntry();
			
			zip.flush();
			zip.close();
			
			sc.commitTransaction();

			Transaction tx = ses.beginTransaction();
			DownloadableReport dorp = new DownloadableReport();
			dorp.setCreatedDate(new Date());
			dorp.setDownloadableName(mfile.getName());
			dorp.setDownloadablePath(mfile.getPath());
			dorp.setDownloadableType(DownloadableType.INCENTIVE_REPORT.name());
			dorp.setSizeBytes((int) mfile.length());
			ses.save(dorp);
			
			tx.commit();

			
			// reschedule if cron changed
			String latestCron = Context.getSetting(GlobalParams.PENDING_INCENTIVE_GENERATOR_CRON_SETTING, GlobalParams.PENDING_INCENTIVE_GENERATOR_DEFAULT_CRON);
			if(!((CronTrigger)jxc.getTrigger()).getCronExpression().equalsIgnoreCase(latestCron)){
				try 
				{
					CronTrigger pendIncentGenTrigger = new CronTrigger(GlobalParams.PENDING_INCENTIVE_GENERATOR_TRIGGERNAME, GlobalParams.PENDING_INCENTIVE_GENERATOR_TRIGGERGROUP, latestCron);
					JobDetail job = jxc.getScheduler().getJobDetail(GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBNAME, GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBGROUP);
					jxc.getScheduler().deleteJob(GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBNAME, GlobalParams.PENDING_INCENTIVE_GENERATOR_JOBGROUP);
					jxc.getScheduler().scheduleJob(job, pendIncentGenTrigger);
					
					GlobalParams.INCENTIVEJOBLOGGER.info("Cron Updated from "+latestCron+" to "+pendIncentGenTrigger.getCronExpression());
				} 
				catch (ParseException e) {
					e.printStackTrace();
					GlobalParams.INCENTIVEJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
				} 
				catch (SchedulerException e) {
					e.printStackTrace();
					GlobalParams.INCENTIVEJOBLOGGER.error(jxc.getJobDetail().getFullName() ,e);
				}
			}
		}
		catch (Exception e) {
			GlobalParams.INCENTIVEJOBLOGGER.error("Error running job:" ,e);
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
			ses.close();
		}
	}

}
*/