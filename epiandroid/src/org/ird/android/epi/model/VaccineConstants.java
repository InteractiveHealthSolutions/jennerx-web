package org.ird.android.epi.model;

public class VaccineConstants
{
	// GAP DURATION TIME UNITS
	public static final String GAP_TIME_WEEK = "WEEK";
	public static final String GAP_TIME_MONTH = "MONTH";
	public static final String GAP_TIME_DAY = "DAY";
	public static final String GAP_TIME_YEAR = "YEAR";

	/*
	 * GAP TYPES
	 * Need to change their values
	 * if name of VaccineGapType
	 * is changed in MySql DB
	 */

	public static final String GAP_TYPE_BIRTHDATE = "Birthdate Gap";
	
	public static final String GAP_TYPE_NEXT_VACCINE = "Next Vaccine Gap";
	
	public static final String GAP_TYPE_PREVIOUS_VACCINE = "Previous Vaccine Gap";

	public static final String GAP_TYPE_STANDARD = "Standard Gap";

	public static final String GAP_TYPE_LATE = "Vaccine Expiry Gap";


}
