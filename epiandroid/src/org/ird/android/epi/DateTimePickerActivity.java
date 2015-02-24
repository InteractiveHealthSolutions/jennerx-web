package org.ird.android.epi;

import java.text.DateFormat;
import java.util.Calendar;

import org.ird.android.epi.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DateTimePickerActivity extends Activity
{

	private Button btnTime;
	private Button btnDate;
	private TextView txtView;
	DateFormat formatDateTime = DateFormat.getDateTimeInstance();
	Calendar cal = Calendar.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_picker_layout);

		// btnTime = (Button)findViewById(R.id.btnTime);
		btnDate = (Button) findViewById(R.id.btnDate);
		txtView = (TextView) findViewById(R.id.txtViewtime);
		updateLabel();
	}

	public void chooseDate(View widget)
	{
		new DatePickerDialog(DateTimePickerActivity.this,
				d,
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH)).show();
	}

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};

	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
			cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			cal.set(Calendar.MINUTE, minute);
			updateLabel();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date_time_picker, menu);
		return true;
	}

	private void updateLabel()
	{
		txtView.setText(formatDateTime.format(cal.getTime()));
	}

}
