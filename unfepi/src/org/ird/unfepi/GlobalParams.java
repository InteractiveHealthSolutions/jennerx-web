package org.ird.unfepi;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalParams 
{
	public static Properties UNFEPI_PROPERTIES;
	
	public enum DailySummaryVaccineGivenTableVariables{
		TOTAL_BCG("BCG"), 
		TOTAL_PENTA1("PENTA1"), 
		TOTAL_PENTA2("PENTA2"), 
		TOTAL_PENTA3("PENTA3"), 
		TOTAL_MEASLES1("MEASLES1"), 
		TOTAL_MEASLES2("MEASLES2"), 
		TOTAL_OPV0("OPV0"), 
		TOTAL_OPV1("OPV1"), 
		TOTAL_OPV2("OPV2"),
		TOTAL_OPV3("OPV3"),
		TOTAL_TT1("TT1"),
		TOTAL_TT2("TT2"),
		TOTAL_TT3("TT3"),
		TOTAL_TT4("TT4"),
		TOTAL_TT5("TT5"),
		TOTAL_VACCINE_GIVEN("TOTAL_VACCINATED");

		private String REPRESENTATION;
		
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}
		
		private DailySummaryVaccineGivenTableVariables(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static DailySummaryVaccineGivenTableVariables findEnum(String representationString){
			for (DailySummaryVaccineGivenTableVariables en : DailySummaryVaccineGivenTableVariables.values()) {
				if(en.REPRESENTATION.equalsIgnoreCase(representationString)){
					return en;
				}
			}
			return null;
		}
	}
	
	public enum SearchFilter{
		CELL_NUMBER,
		SMS_STATUS,
		RECIPIENT,
		ORIGINATOR,
		
		DATE1_FROM,
		DATE1_TO,
		DATE2_FROM,
		DATE2_TO,
		DATE3_FROM,
		DATE3_TO,
		
		GENDER, 
		
		USERNAME,
		NAME_PART,
		PROGRAM_ID,
		USER_STATUS,
		
		ROLE_NAME,
		
		STORE_NAME,
		STOREKEEPER,
		
		VACCINATOR,
		
		AMOUNT1_UPPER,
		AMOUNT1_LOWER,
		
		VACCINATION_CENTER,
		CENTER_TYPE,
		
		DATA_ENTRY_SOURCE,
		ENCOUNTER_TYPE,
		
		TYPE,
		
		SEARCH_FILTER,
		COLUMNS,
		;
		
		public static String FILTER_PREFIX(){
			return "filter.";
		}
		public String FILTER_NAME () {
			return FILTER_PREFIX()+name().toLowerCase();
		}
	}
	
	public enum ServiceType {
		DATA_ENTRY,
		DATA_EDIT,
		DATA_VIEW,
		DATA_SEARCH,
	}
	public enum IdentifierType {
		CHILD_PROJECT_ID,
		WOMEN_PROJECT_ID,
		OLD_CHILD_PROJECT_ID,
		VACCINATOR_PROJECT_ID,
		CENTER_PROJECT_ID,
		STOREKEEPER_PROJECT_ID,
		USER_PROJECT_ID,
	}
	public enum SmsTiming{
		ANY,
		MORNING,
		AFTERNOON,
		EVENING,
	}
	
	public enum VariableSettingType{
		EP_CHARGES,
		ENCOUNTER_TYPE,
		DATA_INCONSISTENCY
	}
	
	public enum IncentiveWorkType{
		CHILD_LOTTERY_WINNINGS,
		CHILD_LOTTERY_DISTINCT_CHILDREN,
		CHILD_LOTTERY_WON_AMOUNT,
		TOTAL_VACCINATIONS,
		TOTAL_AMOUNT_DUE
	}
	public enum DownloadableType {

		PDF_NOTIFIER,
		CSV_NOTIFIER,
		INCENTIVE_REPORT,
		DATA_DUMP,
		OTHER,
		DEFAULT
	}
	
	public enum LotteryType{
		MISSING_ENROLLMENT,
		PENDING_FOLLOWUP,
		EXISTING
	}
	
	public static final String REMINDER_PUSHER_JOBNAME = "qreminder_pusher";
	public static final String REMINDER_PUSHER_JOBGROUP = "qreminder";
	public static final String REMINDER_PUSHER_TRIGGERNAME = "qreminder_pusher";
	public static final String REMINDER_PUSHER_TRIGGERGROUP = "qreminder";
	
	public static final String REMINDER_UPDATER_JOBNAME = "qreminder_updater";
	public static final String REMINDER_UPDATER_JOBGROUP = "qreminder";
	public static final String REMINDER_UPDATER_TRIGGERNAME = "qreminder_updater";
	public static final String REMINDER_UPDATER_TRIGGERGROUP = "qreminder";
	
	public static final String NOTIFIER_RESCHED_JOBNAME = "qnotifier_rescheduler";
	public static final String NOTIFIER_RESCHED_JOBGROUP = "qnotifier";
	public static final String NOTIFIER_RESCHED_TRIGGERNAME = "qnotifier_rescheduler";
	public static final String NOTIFIER_RESCHED_TRIGGERGROUP = "qnotifier";
	
	public static final String NOTIFIER_JOBGROUP = "qnotifier";
	public static final String NOTIFIER_TRIGGERGROUP = "qnotifier";
	
	public static final String DATA_DUMPER_JOBNAME = "qdata_dupmer";
	public static final String DATA_DUMPER_JOBGROUP = "qreporter";
	public static final String DATA_DUMPER_TRIGGERNAME = "qdata_dupmer";
	public static final String DATA_DUMPER_TRIGGERGROUP = "qreporter";
	
	public static final String PENDING_INCENTIVE_GENERATOR_JOBNAME = "qpending_incentive_gen";
	public static final String PENDING_INCENTIVE_GENERATOR_JOBGROUP = "qincentive";
	public static final String PENDING_INCENTIVE_GENERATOR_TRIGGERNAME = "qpending_incentive_gen";
	public static final String PENDING_INCENTIVE_GENERATOR_TRIGGERGROUP = "qincentive";
	
	public static final String RESPONSE_READER_JOBNAME = "qresponse_reader";
	public static final String RESPONSE_READER_JOBGROUP = "qresponse";
	public static final String RESPONSE_READER_TRIGGERNAME = "qresponse_reader";
	public static final String RESPONSE_READER_TRIGGERGROUP = "qresponse";
	
	public static final String VACCINATOR_LOTTERY_JOBNAME = "qvaccinator_lottery";
	public static final String VACCINATOR_LOTTERY_JOBGROUP = "qincentive";
	public static final String VACCINATOR_LOTTERY_TRIGGERNAME = "qvaccinator_lottery";
	public static final String VACCINATOR_LOTTERY_TRIGGERGROUP = "qincentive";
	
	
	public static final String STOREKEEPER_INCENTIVE_JOBNAME = "qstorekeeper_incentive";
	public static final String STOREKEEPER_INCENTIVE_JOBGROUP = "qincentive";
	public static final String STOREKEEPER_INCENTIVE_TRIGGERNAME = "qstorekeeper_incentive";
	public static final String STOREKEEPER_INCENTIVE_TRIGGERGROUP = "qincentive";
	
	public static final String DAEMON_USER_ID_SETTING = "daemon.user.user-id";

	public static final String REMINDER_PUSHER_CRON_SETTING = "reminder.pusher.push-cron";
	public static final String REMINDER_PUSHER_DEFAULT_CRON = "0 0 0/4 * * ?";

	public static final String REMINDER_UPDATER_CRON_SETTING = "reminder.updater.update-cron";
	public static final String REMINDER_UPDATER_DEFAULT_CRON = "0 0/5 * * * ?";

	public static final String NOTIFER_RESCH_CRON_SETTING = "notifier.reschedular.resch-cron";
	public static final String NOTIFER_RESCH_DEFAULT_CRON = "0 0 0/6 * * ?";

	public static final String DATA_DUMPER_CRON_SETTING = "data.dumper.dump-cron";
	public static final String DATA_DUMPER_DEFAULT_CRON = "0 0 0 * * ?";
	
	public static final String PENDING_INCENTIVE_GENERATOR_CRON_SETTING = "incentive.pending-incentive.generate-incentive-cron";
	public static final String PENDING_INCENTIVE_GENERATOR_DEFAULT_CRON = "0 0 13 * * ?";
	
	public static final String VACCINATOR_LOTTERY_INTERVAL_SETTING = "incentive.vaccinator-lottery.interval";
	public static final String VACCINATOR_LOTTERY_DEFAULT_INTERVAL = "14";
	public static final String VACCINATOR_LOTTERY_TIME_SETTING = "incentive.vaccinator-lottery.time";
	public static final String VACCINATOR_LOTTERY_DEFAULT_TIME = "18:00:00";
	
	public static final String STOREKEEPER_INCENTIVE_INTERVAL_SETTING = "incentive.storekeeper-incentive.interval";
	public static final String STOREKEEPER_INCENTIVE_DEFAULT_INTERVAL = "14";
	public static final String STOREKEEPER_INCENTIVE_TIME_SETTING = "incentive.storekeeper-incentive.time";
	public static final String STOREKEEPER_INCENTIVE_DEFAULT_TIME = "18:00:00";
	
	//public static final String NOTIFIER_DEFAULT_CRON = "0 0/30 * * * ?";

	public static final String RESPONSE_READER_CRON_SETTING = "response.reader.read-cron";
	public static final String RESPONSE_READER_DEFAULT_CRON = "0 0/1 * * * ?";
	
	public static final String ADMIN_EMAIL_PROPERTY = "admin.email-address";

	public static final int MAX_REMINDER_1_7_VALIDITY_DAYS = 6;
	
	public static final String SMS_TARSEEL_PROJECT_NAME = "unfepi";
	public static final int SMS_TARSEEL_PROJECT_ID =  1;
	
	//-------------------------------------------------------------------
	
	public static final String	MODE_BIRTHDATE_AGE = "Age";
	public static final String	MODE_BIRTHDATE_DOB = "Birthdate";
	
	public static final String	APPLICATION_YES="Yes";
	public static final String	APPLICATION_NO="No";
	public static final String	APPLICATION_DONT_KNOW="Dont Know";
	
	public static final String	PENTA3_NAME_IN_DB	= "penta3";
	public static final String	MEASLES1_NAME_IN_DB	= "measles1";
	public static final String	MEASLES2_NAME_IN_DB	= "measles2";
	
	public static final String	REMINDER_LOTTERY_SMS_NAME_IN_DB	= "REM_LOTTERY_WON_SMS";

	public static final int TEAM_USER_PROGRAMID_LENGTH = 3;
	public static final String TEAM_USER_PROGRAMID_REGEX = "[1-9].{"+(TEAM_USER_PROGRAMID_LENGTH-1)+"}";

	public static final int CHILD_PROGRAMID_LENGTH = 14;
	/** 6 digit numeric sequence starting with 1 */
	public static final String CHILD_PROGRAMID_REGEX = "[1-9].{"+(CHILD_PROGRAMID_LENGTH-1)+"}";
	public static final String CHILD_ROLE_NAME = "child";
	
	public static final String WOMEN_PROGRAMID_REGEX = "[a-zA-Z]{1}[0-9]{13}";

	public static final int STOREKEEPER_PROGRAMID_LENGTH = 6;
	public static final String STOREKEEPER_PROGRAMID_REGEX = "[0-9]{"+(STOREKEEPER_PROGRAMID_LENGTH)+"}";
	public static final String STOREKEEPER_ROLE_NAME = "storekeeper";

	public static final int VACCINATOR_PROGRAMID_LENGTH = 7;
	public static final String VACCINATOR_PROGRAMID_REGEX = "[0-9]{"+(VACCINATOR_PROGRAMID_LENGTH)+"}";
	public static final String VACCINATOR_ROLE_NAME = "vaccinator";
	
	public static final String OTHER_ROLE_NAME = "other";
	
	public static final int VACCINATION_CENTER_PROGRAMID_LENGTH = 5;
	public static final String VACCINATION_CENTER_ROLE_NAME = "vaccination_center";

	public static final int NIC_LENGTH = 13;

	public static final Logger DBLOGGER = LoggerFactory.getLogger("dbAppender");
	public static final Logger FILELOGGER = LoggerFactory.getLogger("fileAppender");
	public static final Logger NOTIFIERJOBLOGGER = LoggerFactory.getLogger("notifierjobAppender");
	public static final Logger REMINDERJOBLOGGER = LoggerFactory.getLogger("reminderjobAppender");
	public static final Logger RESPONSEJOBLOGGER = LoggerFactory.getLogger("responsejobAppender");
	public static final Logger INCENTIVEJOBLOGGER = LoggerFactory.getLogger("incentivejobAppender");
	public static final Logger EMAILSLOGGER = LoggerFactory.getLogger("emailsAppender");
	public static final Logger MOBILELOGGER = LoggerFactory.getLogger("mobileAppender");

	public static final String STOREKEEPER_INCENTIVE_DOWNLOADABLE_CSV_INITIALS = "StorekeeperIncentiveEventCSV";
	public static final String VACCINATOR_INCENTIVE_DOWNLOADABLE_CSV_INITIALS = "VaccinatorIncentiveEventCSV";

	public static final String NOTIFIER_CSV_DIR = "notifiercsv";
	public static final String NOTIFIER_PDF_DIR = "notifierpdf";
	public static final String INCENTIVE_CSV_DIR = "incentivecsv";
	public static final String DATA_DUMP_DIR = "datadump";

	public static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat CSV_FILENAME_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmm");
	public static final SimpleDateFormat DEFAULT_MOBILE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	public static final String QUERY_PROJECT_START_DATE = "select value from setting where name='project.start-date'";
	public static final String QUERY_PROJECT_TARGET_ENROLLMENTS = "select value from setting where name='project.target-enrollments'";
	public static final String QUERY_PROJECT_TARGET_EVENTS = "select value from setting where name='project.target-events'";
	public static final String QUERY_TOTAL_ENROLLMENTS = "select count(*) from child ch";
	public static final String QUERY_VACCINATIONS_RECEIVED = "select count(*) from vaccination vcrc where vaccinationStatus in ('VACCINATED', 'RETRO', 'RETRO_DATE_MISSING', 'LATE_VACCINATED')";
	//DONT delete OLD QUERIES , Perfectly working
	//public static final String QUERY_ONLY_ENROLLMENT_FILTER_SQL_v = "( v.vaccineId=1 or (v.vaccineId=2 and not exists(select vaccinationRecordNum from vaccination where vaccineId=1 and v.childId=childId)))";
	//public static final String QUERY_ONLY_ENROLLMENT_FILTER_HQL_v = "( v.vaccineId=1 or (v.vaccineId=2 and not exists(select vaccinationRecordNum from Vaccination where vaccineId=1 and v.childId=childId)))";
	//public static final String QUERY_ONLY_FOLLOWUP_FILTER_SQL_v = "(v.vaccineId<>1 AND not (v.vaccineId=2 and not exists (select vaccinationRecordNum from vaccination where v.childId=childId and vaccineId=1)))";
	//public static final String QUERY_ONLY_FOLLOWUP_FILTER_HQL_v = "(v.vaccineId<>1 AND not (v.vaccineId=2 and not exists (select vaccinationRecordNum from Vaccination where v.childId=childId and vaccineId=1)))";

	public static final String SQL_ENROLLMENT_FILTER_v = " v.vaccinationDate is not null and date(v.vaccinationDate) = (select min(date(vaccinationDate)) from vaccination where childId= v.childId) and v.vaccinationRecordNum = (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) ";
	public static final String HQL_ENROLLMENT_FILTER_v = " v.vaccinationDate is not null and date(v.vaccinationDate) = (select min(date(vaccinationDate)) from Vaccination where childId= v.childId) and v.vaccinationRecordNum = (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) ";
	public static final String SQL_FOLLOWUP_FILTER_v = " v.vaccinationRecordNum <> (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=(select min(date(vaccinationDate)) from vaccination where childId= v.childId)) ";
	public static final String HQL_FOLLOWUP_FILTER_v = " v.vaccinationRecordNum <> (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=(select min(date(vaccinationDate)) from Vaccination where childId= v.childId)) ";

	public static final String SQL_VACCINE_BIRTHDATE_GAP_ORDER = " IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) " +
			" WHEN gaptimeunit = 'week' THEN (7*value) " +
			" WHEN gaptimeunit = 'day' THEN (0*value) " +
			" WHEN gaptimeunit = 'year' THEN (365*value) " +
			" ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=this_.vaccineId), 9999)";
	// TURNING JAVA FORMATTER OFF to let SQL QUERIES formatted in SQL EDITOR STYLE
// @formatter:off
	

	
	public static final String SQL_inconsistenciesList = 
" SELECT name, " +
/*"       GROUP_CONCAT( CASE WHEN element = 'data_query' THEN value ELSE NULL END) AS DATA_QUERY, " +*/
"       GROUP_CONCAT( CASE WHEN element = 'row_count_query' THEN value ELSE NULL END) AS ROW_COUNT_QUERY, " +
"       GROUP_CONCAT( CASE WHEN element = 'description' THEN value ELSE NULL END) AS DESCRIPTION " +
" FROM variablesetting " +
" WHERE type='data_inconsistency' " +
" GROUP BY name ";
	
}