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
		CHILD_LOTTERY_WON_AMOUNT
	}
	public enum EncounterType 
	{
		ENROLLMENT,
		FOLLOWUP,
		FOLLOWUP_ADMIN,
		SCREENING,
		DAILY_SUMMARY,
		ADD_CONTACT_NUM,
		CHANGE_PREFERENCE,
		VACCINATOR_REG,
		STOREKEEPER_REG,
		VACC_CENTER_REG,
		LOTTERY_GEN,
		STK_INCENTIVIZE,
		VACC_INCENTIVIZE,
		LOTTERY_CONSUMP
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
	
	public static final String	PENTA3_NAME_IN_DB	= "penta3";
	public static final String	MEASLES1_NAME_IN_DB	= "measles1";
	public static final String	MEASLES2_NAME_IN_DB	= "measles2";
	
	public static final String	REMINDER_LOTTERY_SMS_NAME_IN_DB	= "REM_LOTTERY_WON_SMS";

	public static final int TEAM_USER_PROGRAMID_LENGTH = 3;
	public static final String TEAM_USER_PROGRAMID_REGEX = "[1-9].{"+(TEAM_USER_PROGRAMID_LENGTH-1)+"}";

	public static final int CHILD_PROGRAMID_LENGTH = 6;
	/** 6 digit numeric sequence starting with 1 */
	public static final String CHILD_PROGRAMID_REGEX = "1.{"+(CHILD_PROGRAMID_LENGTH-1)+"}";
	public static final String CHILD_ROLE_NAME = "child";

	public static final int STOREKEEPER_PROGRAMID_LENGTH = 6;
	public static final String STOREKEEPER_PROGRAMID_REGEX = "[0-9]{"+(STOREKEEPER_PROGRAMID_LENGTH)+"}";
	public static final String STOREKEEPER_ROLE_NAME = "storekeeper";

	public static final int VACCINATOR_PROGRAMID_LENGTH = 7;
	public static final String VACCINATOR_PROGRAMID_REGEX = "[0-9]{"+(VACCINATOR_PROGRAMID_LENGTH)+"}";
	public static final String VACCINATOR_ROLE_NAME = "vaccinator";
	
	public static final int VACCINATION_CENTER_PROGRAMID_LENGTH = 5;
	
	public static final int NIC_LENGTH = 13;

	public static final Logger DBLOGGER = LoggerFactory.getLogger("dbAppender");
	public static final Logger FILELOGGER = LoggerFactory.getLogger("fileAppender");
	public static final Logger NOTIFIERJOBLOGGER = LoggerFactory.getLogger("notifierjobAppender");
	public static final Logger REMINDERJOBLOGGER = LoggerFactory.getLogger("reminderjobAppender");
	public static final Logger RESPONSEJOBLOGGER = LoggerFactory.getLogger("responsejobAppender");
	public static final Logger INCENTIVEJOBLOGGER = LoggerFactory.getLogger("incentivejobAppender");
	public static final Logger EMAILSLOGGER = LoggerFactory.getLogger("emailsAppender");

	public static final String STOREKEEPER_INCENTIVE_DOWNLOADABLE_CSV_INITIALS = "StorekeeperIncentiveEventCSV";
	public static final String VACCINATOR_INCENTIVE_DOWNLOADABLE_CSV_INITIALS = "VaccinatorIncentiveEventCSV";

	public static final String NOTIFIER_CSV_DIR = "notifiercsv";
	public static final String NOTIFIER_PDF_DIR = "notifierpdf";
	public static final String INCENTIVE_CSV_DIR = "incentivecsv";
	public static final String DATA_DUMP_DIR = "datadump";

	public static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat CSV_FILENAME_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HH:mm");

	public static final String QUERY_PROJECT_START_DATE = "select value from setting where name='project.start-date'";
	public static final String QUERY_PROJECT_TARGET_ENROLLMENTS = "select value from setting where name='project.target-enrollments'";
	public static final String QUERY_PROJECT_TARGET_EVENTS = "select value from setting where name='project.target-events'";
	public static final String QUERY_TOTAL_ENROLLMENTS = "select count(*) from child ch";
	public static final String QUERY_VACCINATIONS_RECEIVED = "select count(*) from vaccination vcrc where vaccinationStatus in ('VACCINATED', 'LATE_VACCINATED')";
	//DONT delete OLD QUERIES , Perfectly working
	//public static final String QUERY_ONLY_ENROLLMENT_FILTER_SQL_v = "( v.vaccineId=1 or (v.vaccineId=2 and not exists(select vaccinationRecordNum from vaccination where vaccineId=1 and v.childId=childId)))";
	//public static final String QUERY_ONLY_ENROLLMENT_FILTER_HQL_v = "( v.vaccineId=1 or (v.vaccineId=2 and not exists(select vaccinationRecordNum from Vaccination where vaccineId=1 and v.childId=childId)))";
	//public static final String QUERY_ONLY_FOLLOWUP_FILTER_SQL_v = "(v.vaccineId<>1 AND not (v.vaccineId=2 and not exists (select vaccinationRecordNum from vaccination where v.childId=childId and vaccineId=1)))";
	//public static final String QUERY_ONLY_FOLLOWUP_FILTER_HQL_v = "(v.vaccineId<>1 AND not (v.vaccineId=2 and not exists (select vaccinationRecordNum from Vaccination where v.childId=childId and vaccineId=1)))";

	public static final String SQL_ENROLLMENT_FILTER_v = " v.vaccinationDate is not null and date(v.vaccinationDate) = (select min(date(vaccinationDate)) from vaccination where childId= v.childId) and v.vaccinationRecordNum = (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) ";
	public static final String HQL_ENROLLMENT_FILTER_v = " v.vaccinationDate is not null and date(v.vaccinationDate) = (select min(date(vaccinationDate)) from Vaccination where childId= v.childId) and v.vaccinationRecordNum = (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) ";
	public static final String SQL_FOLLOWUP_FILTER_v = " v.vaccinationRecordNum <> (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=(select min(date(vaccinationDate)) from vaccination where childId= v.childId)) ";
	public static final String HQL_FOLLOWUP_FILTER_v = " v.vaccinationRecordNum <> (select min(vaccinationRecordNum) from Vaccination where childId=v.childId and date(vaccinationDate)=(select min(date(vaccinationDate)) from Vaccination where childId= v.childId)) ";

	// TURNING JAVA FORMATTER OFF to let SQL QUERIES formatted in SQL EDITOR STYLE
// @formatter:off
	
	public static final String SQL_exporterFup = 
" SELECT i.programid as ChildID, " +
" concat(ch.firstName,' ',ch.lastName) as ChildName, " +
" concat(ch.fatherFirstName,' ',ch.fatherLastName) as ChildFatherName, " +
" ch.enrollmentVaccineId, " +
" chvc.name enrollmentVaccine, " +
" (select group_concat('--',numberType,':',number) from contactnumber where mappedid=v.childid) contactnumbersofchild, " +
" (select group_concat('H.No:',a.addHouseNumber,' Street:',a.addStreet,' Sector:',a.addSector,' Colony:',a.addColony,' Town:',a.addtown,' UC:',a.addUc,' LMARK:',a.addLandmark,' CityID:',CAST(a.cityId AS char(2))) from address a where a.mappedid=v.childid) addressofchild, " +
" v.vaccinationrecordnum as VaccinationRecordNum, " +
" v.isFirstVaccination as IsFirstVaccination, " +
" v.vaccinationDuedate as VaccinationDueDate, " +
" v.vaccinationDate as VaccinationDate, " +
"  (case when v.vaccinationDate is null and datediff(curdate(), v.vaccinationDuedate) > 0 " +
"	then cast(datediff(curdate(), v.vaccinationDuedate) as char(25)) " +
"  else null " +
"  end  ) 'Num of Days DUEDATE Passed', " +
" lstv.epiNumber AS 'Last EPI assigned', " +
" lstvvcent.programId AS 'Last Center Visited', " +
" v.vaccinationStatus as VaccinationStatus, " +
" v.timelinessStatus as Timeliness, " +
" v.timelinessFactor as TimelinessFactor, " +
" v.epiNumber as EPINumber, " +
" v.nextAssignedDate as NextAssignedDate, " +
" ivcen.programId as VaccinationCenter, " +
" ivac.programId as Vaccinator, " +
" v.vaccineId as VaccineId, " +
" vc.name as 'VaccineName', " +
" v.hasApprovedLottery as 'LotteryApproval', " +
" (case when v.vaccinationDate is not null " +
"			and date(v.vaccinationDate) = (select min(date(vaccinationDate)) from vaccination where childId= v.childId) " +
"			and v.vaccinationRecordNum = (select min(vaccinationRecordNum) from vaccination where childId=v.childId and date(vaccinationDate)=date(v.vaccinationDate)) " +
" then 'ENROLLMENT' " +
" when v.vaccinationRecordNum <> (select min(vaccinationRecordNum) from vaccination  where childId=v.childId and vaccinationDate is not null " +
"			and date(vaccinationDate)=(select min(date(vaccinationDate)) from vaccination where childId= v.childId)) " +
" then 'FOLLOWUP' Else 'NOT_FOUND' end) AS 'EncounterOrFormType' , " +
" (CASE WHEN v.vaccinationDate is null THEN 'N/A' " +
"	ELSE e.dataEntrySource END) dataEntrySource, " +
" v.polioVaccineGiven, " +
" v.pcvGiven, " +
" v.broughtByRelationshipId, " +
" rel.relationName, " +
" v.otherBroughtByRelationship, " +
" v.weight, " +
" v.height, v.description " +
" from vaccination v " +
" left join vaccine vc on v.vaccineid=vc.vaccineId " +
" left join idmapper i on v.childid=i.mappedId " +
" left join child ch on v.childId = ch.mappedId " +
" left join vaccine chvc on ch.enrollmentVaccineId=chvc.vaccineId " +
" left join idmapper ivcen on v.vaccinationCenterId=ivcen.mappedId " +
" left join idmapper ivac on v.vaccinatorId = ivac.mappedId " +
" left join relationship rel on v.broughtByRelationshipId = rel.relationshipId " +
" left join (encounterresults er " +
"				join encounter e on e.encounterid=er.encounterid and e.p1id=er.p1id and e.p2id=er.p2id and e.encounterType in ('followup','enrollment') " +
"			) " +
"		on v.childid=er.p1id and er.element like 'vaccine_name_rec%ved' and er.value = vc.name " +
" left join vaccination lstv on v.childId=lstv.childId and lstv.epiNumber is not null and lstv.vaccinationDate is not null " +
"	and lstv.vaccinationDate=(select max(vaccinationDate) from vaccination where childId=lstv.childId) " +
"	and lstv.vaccinationRecordNum = (select max(vaccinationRecordNum) from vaccination where childId=lstv.childId and vaccinationDate=lstv.vaccinationDate) " +
" left join idmapper lstvvcent on lstv.vaccinationCenterId=lstvvcent.mappedId ";
	
	public static final String SQL_inconsistenciesList = 
" SELECT name, " +
/*"       GROUP_CONCAT( CASE WHEN element = 'data_query' THEN value ELSE NULL END) AS DATA_QUERY, " +*/
"       GROUP_CONCAT( CASE WHEN element = 'row_count_query' THEN value ELSE NULL END) AS ROW_COUNT_QUERY, " +
"       GROUP_CONCAT( CASE WHEN element = 'description' THEN value ELSE NULL END) AS DESCRIPTION " +
" FROM variablesetting " +
" WHERE type='data_inconsistency' " +
" GROUP BY name ";
	
}