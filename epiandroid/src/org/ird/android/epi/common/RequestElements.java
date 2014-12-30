package org.ird.android.epi.common;

public class RequestElements {
	//APPLICATION
	public static final String APP_VER = "appver";
	public static final String APP_TYPE = "appType";
	public static final String REQ_TYPE = "rqtyp";
	public static final String REQ_TYPE_FETCH = "rqtftch";
	public static final String REQ_TYPE_SUBMIT = "rqtsbmt";
	public static final String REQ_USER = "rqtusr";

	public static final String FETCH_FORM_TYPE = "ftfrmtp";
	public static final String SUBMIT_FORM_TYPE = "sbfrmtp";

	public static final String LG_USERNAME = "urnm";
	public static final String LG_USERID = "userID"; 
	public static final String LG_PASSWORD = "urpwd";
	public static final String LG_PHONETIME = "phndttm";

	public static final String UV_CHILDID = "chlid";
	public static final String UV_CURR_VACCINE_RECIEVED = "vccrcvdnam";
	public static final String UV_VACCINATION_STATUS = "vccsts";
	public static final String UV_CURR_VACCINATION_DATE = "curvccdt";
	public static final String UV_IS_LAST_VACCINATION = "islstvcc";
	public static final String UV_NEXT_VACCINE = "nxtvccnam";
	public static final String UV_NEXT_VACCINATION_DATE = "nxtvccdudt";
	public static final String UV_REASON_NOT_VACCINATED = "rsnntvcc";

	public static final String QUERY_ID_TYPE = "qidtyp";

	public static final String CHILD_ID = "chlid";
	public static final String MR_NUMBER = "chlmr";

	// Screening Form 
	public static final String CHILDID = "ChildID";
	public static final String CHILD_HEALTHY = "childHealthy";
	public static final String SCREENING_DATE = "screeningDate";
	public static final String VACCINATOR = "vaccinator";
	public static final String VACCINATION_CENTER = "vaccinationCenter";

	// Enrollment Form 
	public static final String CHILD_PROG_ID = "childProgID";
	public static final String EPI_NO = "epiNo";
	public static final String CHILD_RELATIONSHIP = "childRelation";
	public static final String CHILD_RELATIONSHIP_OTHER = "childRelationOther";
	public static final String CHILD_FIRST_NAME = "chdFirstName";
	public static final String CHILD_LAST_NAME = "chdLastName";
	public static final String FATHER_FIRST_NAME = "fatherFirstName";
	public static final String FATHER_LAST_NAME = "fatherLastName";
	public static final String MOTHER_FIRST_NAME = "motherFirstName";
	public static final String MOTHER_LAST_NAME = "motherLastName";
	public static final String CHILD_GENDER = "childGender";
	public static final String DOB = "dob";
	public static final String IS_BIRHTDATE_ESTIMATED = "estimatedDOB";
	public static final String DOB_YEAR = "dob_year";
	public static final String DOB_MONTH = "dob_month";
	public static final String DOB_WEEK = "dob_week";
	public static final String DOB_DAY = "dob_day";
	public static final String ADD_HOUSENO = "address_hno";
	public static final String ADD_STREET = "address_street";
	public static final String ADD_SECTOR = "address_sector";
	public static final String ADD_COLONY = "address_colony";
	public static final String ADD_TOWN = "address_town";
	public static final String ADD_UC = "address_UC";
	public static final String ADD_LANDMARK = "address_landmark";
	public static final String ADD_CITY = "address_city";
	public static final String RELIGION = "religion";
	public static final String RELIGION_OTHER = "religionOther";
	public static final String LANGUAGE = "language";
	public static final String LANGUAGE_OTHER = "languageOther";
	public static final String VACCINATION_GIVEN = "vaccinationGiven";
	public static final String POLIO_GIVEN = "polioGiven";
	public static final String PCV_GIVEN = "pcvGiven";
	public static final String DATE_OF_VACCINATION = "dateOfVacc";
	public static final String NEXT_VACCINATION = "nxtVacc";
	public static final String NEXT_ALLOTTED_DATE = "nextAllottedDate";
	public static final String BIRTH_WEIGHT = "birthWeight";
	public static final String BIRTH_HEIGHT = "birthHeight";

	public static final String SMS_REMINDER_APP = "smsReminderApp";
	public static final String SMS_REMINDER_APP_DENIAL_REASON = "smsReminderAppDenialReason";
	public static final String SMS_REMINDER_APP_DENIAL_REASON_OTHER = "smsReminderAppDenialReasonOther";
	public static final String SMS_SENDING_TIME = "smsSendingTime";
	public static final String CELL_PHONE_ACCESS = "cellPhoneAccess";
	public static final String CELL_PHONE_HOLDER_RELATION = "cellPhoneHolderRelation";
	public static final String CELL_PHONE_HOLDER_RELATION_Other = "cellPhoneHolderRelationOther";
	public static final String REMINDER_PRIMARY_NUMBER = "reminderPrimaryNo";
	public static final String REMINDER_SECONDARY_NUMBER = "reminderSecondaryNo";

	public static final String LOTTERY_APPROVAL = "lotteryApproval";
	public static final String LOTTERY_DENIAL_REASON = "lotteryDenialReason";
	public static final String LOTTERY_DENIAL_REASON_OTHER = "lotteryDenialReasonOther";
	public static final String VERIFICATION_CODE = "verificationCOde";
	public static final String AMOUNT_WON_ON_ENROLMENT = "amountWonOnEnrolment";
	public static final String UPDATE_VERIFICATION_CODE = "UpdateVCode";
	public static final String VACCINEID = "vaccineID";
	public static final String VACCINENAME = "vaccineName";
	public static final String IS_VACCINE_ON_TIME = "isVaccOnTime";
	public static final String ENROLLEMNT_CENTRE = "enrollmentCentre";

	// FollowUp
	public static final String VACCINATION_GIVEN_TODAY = "vaccinationGivenToday";
	public static final String REASON_IF_VACCINATION_NOT_GIVEN = "reasonIfNotGiven";
	public static final String REASON_IF_VACCINATION_NOT_GIVEN_OTHER = "reasonIfNotGivenOther";
	public static final String CHILD_BROUGHT_BY = "childBroughtBy";
	public static final String IS_FIRST_VISIT = "isFirstVisit";
	public static final String PRIMARY_CELL_CHANGED = "primaryCellChanged";
	public static final String SMS_REMINDER_CHANGED = "smsReminderChanged";
	public static final String LOTTERY_CHANGED = "lotteryChanged";
	public static final String NEW_EPI_NO_GIVEN = "newEpiNoGiven";
	public static final String NEW_EPI_NO = "newEpiNo";

	public static final String VACCINATOR_CODE = "vaccinatorCode";
	public static final String SUMMARY_DATE = "summarydate";
	public static final String ENROLMENT_Center = "enrolment";
	public static final String BCG_VISITED = "bcgVisited";
	public static final String BCG_ENROLLED = "bcgEnrolled";
	public static final String PENTA1_VISITED = "penta1Visited";
	public static final String PENTA1_ENROLED = "penta1Enroled";
	public static final String PENTA1_FOLLOWUP = "penta1Followup";
	public static final String PENTA2_VISITED = "penta2Visited";
	public static final String PENTA2_FOLLOWUP = "penta2Followup";
	public static final String PENTA3_VISITED = "penta3Visited";
	public static final String PENTA3_FOLLOWUP = "penta3Followup";
	public static final String MEASLES1_VISITED = "Measles1Visited";
	public static final String MEASLES1_FOLLOWUP = "Measles1Followup";
	public static final String MEASLES2_VISITED = "Measles2Visited";
	public static final String MEASLES2_FOLLOWUP = "Measles2Followup";
	public static final String BCG_REMINDER_SMS = "BCGReminderSMS";
	public static final String PENTA_REMINDERS_SMS = "PentaReminderSMS";
	public static final String BCG_LOTTERY = "BCGLottery";
	public static final String PENTA_LOTTERY = "PentaReminderSMS";
	
	//DAILY SUMMARY
	public static final String TOTAL_ENROL_PER_DAY = "TotalEnrolmentPerDay";
	public static final String TOTAL_FOLLOW_PER_DAY = "TotalFollowPerDay";
	public static final String TOTAL_TT_TODAY = "TotalTT_Today";
	public static final String TOTAL_OPV_TODAY = "TotalOPV_Today";
	public static final String TOTAL_VISITED_TODAY = "TotalVisitedToday";	
	public static final String TOTAL_OPV0 = "TotalOPV0";
	public static final String TOTAL_OPV1 = "TotalOPV1";
	public static final String TOTAL_OPV2 = "TotalOPV2";
	public static final String TOTAL_OPV3 = "TotalOPV3";
	public static final String TOTAL_TT1 = "TotalTT1";
	public static final String TOTAL_TT2 = "TotalTT2";
	public static final String TOTAL_TT3 = "TotalTT3";
	public static final String TOTAL_TT4 = "TotalTT4";
	public static final String TOTAL_TT5 = "TotalTT5";

	// Shopkeeper Forms
	public static final String GET_AMOUNT = "GetAmount";

}
