package org.ird.android.epi.communication.elements;

public class ResponseElements {
	public static final String XML_RESPONSE_ROOT_TAG = "imrs";

	public static final String RESPONSE_STATUS = "status";
	public static final String RESPONSE_STATUS_ERROR = "error";
	public static final String RESPONSE_STATUS_SUCCESS = "success";
	public static final String RESPONSE_MESSAGE = "msg";

	public static final String LG_USERNAME = new String("urnm");
	public static final String LG_USERID = new String("userID");
	public static final String LG_PASSWORD = new String("urpwd");
	public static final String LG_PHONETIME = new String("dttim");
	public static final String IS_USER_VACCINATOR = new String("isVaccinator");
	public static final String CENTRE_ID = new String("centreid");
	
	
	public static final String UV_CHILD_ID = new String("chlid");
	public static final String UV_CHILD_NAME = new String("chlflnam");
	public static final String UV_CHILD_LAST_NAME = new String("childLastName");
	public static final String UV_CHILD_VACCINATION_DUE_DATE = new String(
			"dueDate");
	public static final String UV_VACCINE_ID = new String("vaccineID");
	public static final String VERIFICATION_CODE_FOR_MOTHER = new String(
			"codeForMother");
	public static final String LOTTERY_AMOUNT_WON = new String("amount");
	public static final String CHILD_GENDER = new String("gender");
	public static final String ALL_VAC_COMPLETED = new String("vacCompleted");
	public static final String VACCINE_NAME = new String("vaccine");

	// For revised follow up form
	public static final String PRIMARY_CELL_NO = new String("primaryCellNo");
	public static final String LOTTERY_APPROVAL = new String("lotteryApproval");
	public static final String SMS_REMINDER_APPROVAL = new String(
			"smsReminderApproval");
	public static final String LAST_VACCINE = new String("lastVaccine");
	public static final String FATHER_FIRST_NAME = new String("fatherFirstName");
	public static final String FATHER_LAST_NAME = new String("fatherLastName");
	public static final String CHILD_DOB = new String("childDOB");
	public static final String CHILD_DOB_IN_MILLIS = new String(
			"childDOBInMillis");
	public static final String DUE_DATE_IN_MILLS = new String("dueDateinMillis");
	public static final String EPI_NO = new String("epiNo");

	public static final String UV_PREV_VACC_DETAILS = new String("prvvccdt");
	public static final String UV_CUR_VACC_DUEDATE = new String("curvccduedt");
	public static final String UV_CUR_VACC_NAME = new String("curvccnam");
	public static final String UV_VACC_RECIEVED_NAMES = new String("vccrcvdnam");
	
	

}
