package org.ird.android.epi.common;

public class GlobalConstants 
{
	//Set to 0 by default. Should be changed to reflect the currently logged in user after 
	public static Integer USER_PROGRAM_ID=0;
	
	//Id of the centre this user is associated with. Applies only to vaccinator
	public static Integer VACCINATION_CENTRE_ID=0;
	
	//Array of locations
	String[][] locations;
	
	//Is set on successful login. Can change as per the currently logged in 
	public static String USER_ROLE="";
	
	public static int CURRENT_CHILD_ID =0;
	
	//TODO Make a preference for this too in Advanced section of the Preferences activity
	public static final String DATE_FORMAT= "dd/MM/yyyy";
	
	
	//TODO Create properties in Preferences for these. User should be allowed to customize this as and when needed
	public static final int DAY_MINIMUM=1;
	public static final int DAY_MAXIMUM=30;
	
	public static final int MONTH_MAXIMUM=11;
	public static final int MONTH_MINIMUM=1;
	
	public static final int WEEK_MINIMUM=1;
	public static final int WEEK_MAXIMUM=3;
	
	public static final int YEAR_MINIMUM=1;
	public static final int YEAR_MAXIMUM=5;	
	
	public static final String EPI_CHILD_ID = "childID";
	public static final String EPI_LOTTERY = "lottery";
	
	public static final String REST_ENROLLMENT="/enrollment";
	public static final String REST_FOLLOWUP="/followup";
	public static final String REST_ID_CHANGE="/identifier";
	public static final String REST_LOGIN="/login";
	public static final String REST_METADATA="/metadata";
	public static final String REST_STOREKEEPER="/storekeeper";
	
	
	public static final String ARGUMENT_VACCINE_NAME="vaccinename";
	public static final String ARGUMENT_CENTRE_NAME="centre";
	public static final String ARGUMENT_IMAGE_ID="imageid";
	
	public static final String VACCINE_TAG_BCG="BCG";
	public static final String VACCINE_TAG_P1="Penta1";
	public static final String VACCINE_TAG_P2="Penta2";
	public static final String VACCINE_TAG_P3="Penta3";
	public static final String VACCINE_TAG_M1="Measles1";
	public static final String VACCINE_TAG_M2="Measles2";
	
	public static final String GENDER_MALE="m";
	public static final String GENDER_FEMALE="f";
	
		
	//public static final String REST_DAILY_SUMMARY="/enrollment";
	
	
}
