package org.ird.android.epi.common;

public class ArrayValues 
{
	//TOWNS
	private static final String TOWN_KORANGI="Korangi";
	private static final String TOWN_LANDHI = "Landhi";
	private static final String TOWN_BIN_QASIM="Bin Qasim";
	private static final String OTHER ="Other";
	
	//CITY
	private static final String CITY_KARACHI="Karachi";
	
	//GENDER
	private static final String GENDER_MALE="Male";
	private static final String GENDER_FEMALE="Female";
	
	
	//VACCINES
	public static final String VACCINE_BCG="BCG";
	public static final String VACCINE_P1="Penta1";
	public static final String VACCINE_P2="Penta2";
	public static final String VACCINE_P3="Penta3";
	public static final String VACCINE_M1="Measles1";
	public static final String VACCINE_M2="Measles2";
	
	public static String[] getTown()
	{
		String[] towns = {TOWN_KORANGI,TOWN_LANDHI,TOWN_BIN_QASIM,OTHER};
		return towns;
	}
	
	public static String[] getCity()
	{
		String[] city = {"Karachi"};
		return city;
	}
	
	
	public static String[] getGenders()
	{
		String[] gender ={GENDER_MALE,GENDER_FEMALE};
		return gender;		
	}
	
	public static String[] getVaccines()
	{
		String[] vaccines = {VACCINE_BCG,VACCINE_P1,VACCINE_P2,VACCINE_P3,VACCINE_M1,VACCINE_M2};
		return vaccines;
	}

}
