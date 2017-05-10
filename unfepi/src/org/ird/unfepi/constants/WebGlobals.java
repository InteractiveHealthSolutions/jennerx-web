package org.ird.unfepi.constants;

import java.text.SimpleDateFormat;


public class WebGlobals {
	public static final String URL_VIEW_SCREENING = "viewScreenings.htm";
	public static final String URL_VIEW_VACCINATION = "viewVaccinationRecord.htm";
	public static final String URL_VIEW_CHILDREN = "viewChildren.htm";
	public static final String URL_VIEW_REMINDERSMS = "viewReminderSmsRecord.htm";
	public static final String URL_VIEW_DAILY_SUMMARY = "viewDailySummaries.htm";
	public static final String URL_VIEW_CHILD_RESPONSE = "viewChildResponses.htm";
	public static final String URL_VIEW_ROLE = "viewRoles.htm";
	public static final String URL_VIEW_USERS = "viewUsers.htm";
	public static final String URL_VIEW_USERSMSES = "viewUserSmses.htm";
	public static final String URL_VIEW_VACCINATORS = "viewVaccinators.htm";
	public static final String URL_VIEW_STOREKEEPERS = "viewStorekeepers.htm";
	public static final String URL_VIEW_REMINDERS = "viewReminders.htm";
	public static final String URL_VIEW_VACCINATION_CENTER = "viewVaccinationCenters.htm";
	public static final String URL_VIEW_DOWNLOADABLES = "viewDownloadables.htm";
	public static final String URL_VIEW_ENCOUNTERS = "viewEncounters.htm";
	public static final String URL_LOTTERY_GEN_ENCOUNTERS = "lotteryGeneratorPendingForms.htm";
	public static final String URL_VIEW_VACCINATOR_INCENTIVES = "viewVaccinatorIncentives.htm";
	public static final String URL_VIEW_VACCINATOR_INCENTIVE_TRANSACTIONS = "viewVaccinatorIncentiveTransactions.htm";
	public static final String URL_VIEW_STOREKEEPER_INCENTIVES = "viewStorekeeperIncentives.htm";
	public static final String VERSION_CSS_JS = "3.2";
	public static final String VERSION_WEB_APP = "3.2.1b";

	public static final String CLR_CHILD_CONTACT_NUMBER_ADD_FORM_DARK = "#41DAF5";
	public static final String CLR_CHILD_CONTACT_NUMBER_CORRECT_FORM_DARK = "#41DAF5";
	public static final String CLR_STOREKEEPER_CONTACT_NUMBER_ADD_FORM_DARK = "#FF7A7A";
	public static final String CLR_STOREKEEPER_CONTACT_NUMBER_CORRECT_FORM_DARK = "#FF7A7A";
	public static final String CLR_VACCINATOR_CONTACT_NUMBER_ADD_FORM_DARK = "#FF7A7A";
	public static final String CLR_VACCINATOR_CONTACT_NUMBER_CORRECT_FORM_DARK = "#FF7A7A";
	
	public static final String CLR_ADDRESS_ADD_FORM_DARK = "";
	public static final String CLR_ADDRESS_CORRECT_FORM_DARK = "";
	public static final String CLR_CHANGE_PREFERENCE_FORM_DARK = "";
	public static final String CLR_CHILD_BIOGRAPHIC_CORRECT_FORM_DARK = "";
	public static final String CLR_DAILY_SUMMARY_ADD_FORM_DARK = "";
	public static final String CLR_DAILY_SUMMARY_CORRECT_FORM_DARK = "";
	public static final String CLR_ENROLLMENT_ADD_FORM_DARK = "";
	public static final String CLR_EPI_NUMBER_CHANGE_FORM_DARK = "";
	public static final String CLR_FOLLOWUP_ADD_FORM_DARK = "";
	public static final String CLR_FOLLOWUP_ADD_ADMIN_FORM_DARK = "";
	public static final String CLR_FOLLOWUP_CORRECT_FORM_DARK = "";
	public static final String CLR_LOTTERY_GENERATOR_FORM_DARK = "";
	public static final String CLR_LOTTERY_GENERATOR_FORM_FILL_FORM_DARK = "";
	public static final String CLR_REMINDER_CORRECT_FORM_DARK = "";
	public static final String CLR_RESET_PASSWORD_FORM_DARK = "";
	public static final String CLR_ROLE_ADD_FORM_DARK = "";
	public static final String CLR_ROLE_CORRECT_FORM_DARK = "";
	public static final String CLR_SCREENING_ADD_FORM_DARK = "";
	public static final String CLR_SCREENING_CORRECT_FORM_DARK = "";
	public static final String CLR_STOREKEEPER_ADD_FORM_DARK = "";
	public static final String CLR_STOREKEEPER_CORRRECT_FORM_DARK = "";
	public static final String CLR_USER_ADD_FORM_DARK = "";
	public static final String CLR_USER_CORRECT_FORM_DARK = "";
	public static final String CLR_VACCINATIONCENTER_ADD_FORM_DARK = "";
	public static final String CLR_VACCINATIONCENTER_CORRECT_FORM_DARK = "";
	public static final String CLR_VACCINATOR_ADD_FORM_DARK = "";
	public static final String CLR_VACCINATOR_CORRECT_FORM_DARK = "";
	public static final String CLR_VACCINE_CORRECT_FORM_DARK = "";
	
	public static final int OTHER_OPTION_ID_IN_DB = 66;
	
	public static final int MIN_EPI_NUMBER_LENGTH = 8;
	public static final int MAX_EPI_NUMBER_LENGTH = 8;

	public static final int MIN_WEIGHT = 1;
	public static final int MIN_HEIGHT = 25;
	
	public static final int MAX_WEIGHT = 20;
	public static final int MAX_HEIGHT = 100;
	
	public static final String DEFAULT_PAGING_STYLE = "altavista";
	public static final String DEFAULT_PAGING_POSITION = "top";
	public static final String DEFAULT_PAGING_INDEX = "center";
	public static final int DEFAULT_PAGING_MAX_PAGE_ITEMS = 20;
	public static final int DEFAULT_PAGING_MAX_INDEX_PAGES = 10;

	public static final String BOOLEAN_CONVERTER_TRUE_STRING = "true";
	public static final String BOOLEAN_CONVERTER_FALSE_STRING = "false";

	public static final int PAGER_PAGE_SIZE = 20;

	public static final int GRACE_HOURS = 96; // = 4 Days
	
	
	public enum PagerParams {
		pageSize,
		totalRows,
		pageNumber
	}
	
	public enum DWRParamsGeneral {
		entityRole,
		vaccinationCenter,
		date1from,
		date1to
	}
	
	public enum DGUserSmsFieldNames{
		number,
		numberType,
		roleName,
		contactNumberId,
		programId,
		mappedId
	}
	public enum DGInconsistenciesFieldNames{
		uniqueName,
		description,
		rowCount,
	}
	
//donot change the format without seeing effect in java , javascript scw, date.js codes used in application
public static final String GLOBAL_DATE_FORMAT_JAVA="dd-MM-yyyy";
public static final String GLOBAL_DATETIME_FORMAT_JAVA="dd-MM-yyyy HH:mm";
public static final String GLOBAL_DATE_FORMAT_JS="dd-mm-yy";
public static final String GLOBAL_DATE_FORMAT_SQL="yyyy-MM-dd";
public static final String GLOBAL_DATETIME_FORMAT_SQL ="yyyy-MM-dd HH:mm:ss";
public static final SimpleDateFormat GLOBAL_JAVA_DATE_FORMAT = new SimpleDateFormat(GLOBAL_DATE_FORMAT_JAVA);
public static final SimpleDateFormat GLOBAL_JAVA_DATETIME_FORMAT = new SimpleDateFormat(GLOBAL_DATETIME_FORMAT_JAVA);
public static final SimpleDateFormat GLOBAL_SQL_DATE_FORMAT = new SimpleDateFormat(GLOBAL_DATE_FORMAT_SQL);
public static final SimpleDateFormat GLOBAL_SQL_DATETIME_FORMAT = new SimpleDateFormat(GLOBAL_DATETIME_FORMAT_SQL);
}