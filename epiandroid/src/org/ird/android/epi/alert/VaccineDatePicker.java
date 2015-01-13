package org.ird.android.epi.alert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.ird.android.epi.common.DateTimeUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
/**
 * This class is used to start the DatePickerDialog and set the value returned by it
 * in a View. Currently the Class is coded for an EditText but this can be changed.
 * 
 */
public class VaccineDatePicker implements OnClickListener, OnDateSetListener{

	private Date _defaulDate;
	private Date maxDate;
	private Date minDate;
	private int _month;
	private int _day;
	private int _year;
	private View _guiElement;
	Activity _callingActivity;
	
	public VaccineDatePicker(Context context, View control, Date defaultDate , Date maxDate , Date minDate)
	{
		this._defaulDate =defaultDate;
		this._callingActivity = (Activity)context;
		this._guiElement = control;
		this.maxDate = maxDate;
		this.minDate = minDate;
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		this._year = year;
		this._month = monthOfYear;
		this._day = dayOfMonth;
		updateControl();
	}

	@Override
	public void onClick(View v) {
		
		Calendar cal = GregorianCalendar.getInstance();		
		//set the current date by default
		cal.setTime(new Date());
		
		Date dateToSet=null;
		//first try to start the calendar using the date already present in the EditText
		if(this._guiElement!=null && this._guiElement instanceof EditText)
		{
			 Date dt = DateTimeUtils.StringToDate(((EditText)this._guiElement).getText().toString(), null);
			 if(dt!=null)
			 {
				 dateToSet=dt;
				 cal.setTime(dateToSet);
			 }
		}
		//if the date could not be retrieved from the View then use the default value passed in the contructor
		if(this._defaulDate !=null && dateToSet == null)
			cal.setTime(this._defaulDate);
		
		DatePickerDialog dialog  = new DatePickerDialog(this._callingActivity, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
		if(maxDate!=null)
			dialog.getDatePicker().setMaxDate(maxDate.getTime());
		if(minDate!=null)
			dialog.getDatePicker().setMinDate(minDate.getTime());
		dialog.show();
	}
	
	private void updateControl()
	{
		if(this._guiElement instanceof EditText)
		{
			//Create a date from the fields available
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.YEAR, _year);
			cal.set(Calendar.MONTH, _month);
			cal.set(Calendar.DAY_OF_MONTH, _day);
			//set the date in the control 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			((EditText) this._guiElement).setText(sdf.format(cal.getTime()));
		}
	}

}