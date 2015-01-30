package org.ird.android.epi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.ServerResponseAlert;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;


public class CalculateAgeAcitivity extends FragmentActivity implements IDialogListener {

	private NumberPicker npDays,npWeeks, npMonths, npYears;
	
	int year;
	int month;
	int week;
	int day;
	String[] nums;
	public static final String CALCULATED_AGE="age";
	public static final String DAYS="days";
	public static final String WEEKS="weeks";
	public static final String MONTHS="months";
	public static final String YEARS="years";
	
	private static String dateString;
	private static String TAG_CALCULATE_AGE = "CalculateAge";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_age_layout);
		
		//set number pickers
		npDays = (NumberPicker) findViewById(R.id.numberPickerDays);
		npWeeks = (NumberPicker) findViewById(R.id.numberPickerWeeks);
		npMonths = (NumberPicker) findViewById(R.id.numberPickerMonths);
		npYears = (NumberPicker) findViewById(R.id.numberPickerYears);
		
		// days
		nums = new String[30];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i+1);

		npDays.setMinValue(1);
		npDays.setMaxValue(30);
		npDays.setWrapSelectorWheel(true);
		npDays.setDisplayedValues(nums);
		npDays.setValue(1);

		// weeks
		nums = new String[5];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i);

		npWeeks.setMinValue(0);
		npWeeks.setMaxValue(4);
		npWeeks.setWrapSelectorWheel(false);
		npWeeks.setDisplayedValues(nums);
		npWeeks.setValue(0);

		// months
		nums = new String[13];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i);

		npMonths.setMinValue(0);
		npMonths.setMaxValue(12);
		npMonths.setWrapSelectorWheel(false);
		npMonths.setDisplayedValues(nums);
		npMonths.setValue(0);

		// years
		nums = new String[6];
		for (int i = 0; i < nums.length; i++)
			nums[i] = Integer.toString(i);

		npYears.setMinValue(0);
		npYears.setMaxValue(5);
		npYears.setWrapSelectorWheel(false);
		npYears.setDisplayedValues(nums);
		npYears.setValue(0);

		//setControls();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculate_age_acitivity, menu);
		return true;
	}
	
	private void setControls()
	{
		
		EpiUtils.setNumberPickerValues(npDays, GlobalConstants.DAY_MINIMUM, GlobalConstants.DAY_MAXIMUM);
		EpiUtils.setNumberPickerValues(npWeeks, GlobalConstants.WEEK_MINIMUM, GlobalConstants.WEEK_MAXIMUM);
		EpiUtils.setNumberPickerValues(npMonths, GlobalConstants.MONTH_MINIMUM, GlobalConstants.MONTH_MINIMUM);
		EpiUtils.setNumberPickerValues(npYears, GlobalConstants.YEAR_MINIMUM, GlobalConstants.YEAR_MINIMUM);
	
	}
	
	public void setAge(View widget)
	{
		year = npYears.getValue();
		month = npMonths.getValue();
		week = npWeeks.getValue();
		day = npDays.getValue();
		
		validate();
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -(year));
		cal.add(Calendar.MONTH, -(month));
		
		//calculate weeks and days together
		int daysFromWeeks = 7 * week;
		cal.add(Calendar.DAY_OF_MONTH, -(day + daysFromWeeks));
		
		//Get the Date caculated
		Date dateOfBirth = cal.getTime();				
		
		//Get the String format of the date
		dateString = EpiUtils.getDateFormat().format(dateOfBirth); 
		
		//Show the calculated date before going back to the calling activity
		AlertDialog alert = EpiUtils.showYesNoDialog(this, getString(R.string.alert_age_correct) + ":" + dateString , "", "Proceed", "Cancel", this);				
		alert.show();		
	}

	
	
	public boolean validate() {
		
		int year = npYears.getValue();
		int month = npMonths.getValue();
		int week = npWeeks.getValue();
		int day = npDays.getValue();
		
		if(year == 0 && month == 0 && week ==0 && day ==0)
		{
			ServerResponseAlert alert =  new ServerResponseAlert();
			alert.setMessageToShow("Must enter a value greater than 0 for at least one from" +
					"Year, Month, Week or Day");
			alert.show(getFragmentManager(), "EPI");
			return false;
		}
		
		return true;
	}

	@Override
	public void onDialogPositiveClick(Map...m) 
	{
		//close the activity
		finish();
	}

	@Override
	public void onDialogNegativeClick(Map...m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNeutralClick(Map...m) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void finish()
	{
		Intent intent = new Intent();
		intent.putExtra(CALCULATED_AGE, dateString);
		intent.putExtra(DAYS , npDays.getValue());
		intent.putExtra(WEEKS, npWeeks.getValue());
		intent.putExtra(MONTHS, npMonths.getValue());
		intent.putExtra(YEARS, npYears.getValue());
		
		setResult(RESULT_OK, intent);
		super.finish();
	}


	public void handleException(Exception ex)
	{
		Log.e(TAG_CALCULATE_AGE, ex.getMessage());
	}

}
