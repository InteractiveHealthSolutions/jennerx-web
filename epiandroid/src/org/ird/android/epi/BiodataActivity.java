package org.ird.android.epi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BiodataActivity extends Activity implements OnCheckedChangeListener, OnItemSelectedListener
{
	private int REQUEST_CALCULATE_AGE = 1;

	public static String PROJECT_ID = "projId";
	public static String EPI_NO = "epiNum";
	public static String CHILD_FIRST_NAME = "chldNmFst";
	public static String IS_CHILD_NAMED = "chldNmed";
	public static String FATHER_FIRST_NAME = "fthrNmFtst";
	public static String IS_ESTIMATED_DOB = "isEstmdtdDob";
	public static String CHILD_DOB = "chldDob";
	public static String GENDER = "gndr";
	public static String IS_VALID = "isVld";

	private String _projectId;
	private String _epino;
	private String _childFirstName;
	private boolean _isChildNamed;
	private String _fatherFirstName;
	private String _gender;
	private Date _dob;
	private boolean _isEstimatedDOB;

	// calculated age fields
	private String _days = null;
	private String _weeks = null;
	private String _months = null;
	private String _years = null;

	// ArrayAdapter
	private ArrayAdapter<String> arrayAdapter;

	// Spinners
	Spinner spGender;

	// EditTexts
	private EditText txtProjectId;
	private EditText txtEpiNo;
	private EditText txtDateOfBirth;
	private EditText txtChildFirstName;
	private EditText txtFatherFirstName;

	// TextViews
	private TextView txtViewProjectID;
	private TextView txtViewChildFirstName;
	private TextView txtViewChildLastName;
	private TextView txtViewFatherFirstName;
	private TextView txtViewFatherLastName;
	private TextView txtViewGender;
	private TextView txtViewDateOfBirth;

	// CheckBoxes
	private CheckBox chkBoxIsChildNamed;

	Calendar cal = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.biodata_layout);

		txtProjectId = (EditText) findViewById(R.id.txtProjID);

		txtEpiNo = (EditText) findViewById(R.id.editTextEpiNo);

		txtDateOfBirth = (EditText) findViewById(R.id.editTextDateOfBirth);
		txtViewDateOfBirth = (TextView) findViewById(R.id.textViewDateOfBirth);

		chkBoxIsChildNamed = (CheckBox) findViewById(R.id.checkBoxIsChildNamed);

		txtChildFirstName = (EditText) findViewById(R.id.editTextChildFirstName);
		txtViewChildFirstName = (TextView) findViewById(R.id.textViewChildFirstName);

		txtViewFatherFirstName = (TextView) findViewById(R.id.textViewFatherFirstName);
		txtFatherFirstName = (EditText) findViewById(R.id.editTextFatherFirstName);

		txtViewGender = (TextView) findViewById(R.id.textViewGender);
		spGender = (Spinner) findViewById(R.id.spGender);
		chkBoxIsChildNamed.setOnCheckedChangeListener(this);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array_gender));
		spGender.setAdapter(arrayAdapter);
		spGender.setOnItemSelectedListener(this);

		fillFields(getIntent().getExtras());
	}

	private void fillFields(Bundle data)
	{
		if (data != null)
		{
			try
			{
				_projectId = data.getString(BiodataActivity.PROJECT_ID);
				txtProjectId.setText(_projectId);

				_epino = data.getString(BiodataActivity.EPI_NO);
				txtEpiNo.setText(_epino);

				_fatherFirstName = data.getString(BiodataActivity.FATHER_FIRST_NAME);
				txtFatherFirstName.setText(_fatherFirstName);

				_isChildNamed = data.getBoolean(IS_CHILD_NAMED);
				chkBoxIsChildNamed.setChecked(!_isChildNamed);

				_childFirstName = data.getString(CHILD_FIRST_NAME);
				txtChildFirstName.setText(_childFirstName);

				toggleChildName(_isChildNamed);

				_dob = DateTimeUtils.StringToDate(data.getString(BiodataActivity.CHILD_DOB), null);
				txtDateOfBirth.setText(DateTimeUtils.DateToString(_dob, null));

				_gender = data.getString(BiodataActivity.GENDER);
				if (getResources().getString(R.string.gender_male).equalsIgnoreCase(_gender))
					spGender.setSelection(0);
				else if (getResources().getString(R.string.gender_female).equalsIgnoreCase(_gender))
					spGender.setSelection(1);

			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.e(BiodataActivity.class.getSimpleName(), e.getMessage());
				Log.e(BiodataActivity.class.getSimpleName(), "Error opening saved information");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.biodata, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub

	}

	public void chooseDate(View widget)
	{

		IDialogListener dialogListener = new IDialogListener()
		{

			@Override
			public void onDialogPositiveClick(Map... o)
			{
				// new DatePickerDialog(BiodataActivity.this, d,
				// cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				// cal.get(Calendar.DAY_OF_MONTH)).show();

				DatePickerDialog dialog = new DatePickerDialog(BiodataActivity.this, d, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				dialog.getDatePicker().setMaxDate(new Date().getTime());

				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
				Date minDate = null;
				try
				{
					minDate = (Date) formatter.parse("01-JAN-2010");
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
				dialog.getDatePicker().setMinDate(minDate.getTime());
				dialog.show();
				txtDateOfBirth.setText(EpiUtils.getDateFormat().format(cal.getTime()));

			}

			@Override
			public void onDialogNeutralClick(Map... o)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onDialogNegativeClick(Map... o)
			{
				// TODO Auto-generated method stub

			}
		};
		if (txtDateOfBirth.getText() != null && !("".equals(txtDateOfBirth.getText().toString())))
		{
			EpiUtils.showYesNoDialog(BiodataActivity.this, "Changing the date of birth will reset the vaccinations. Do you want to continue?", "Alert", "Yes", "No", dialogListener).show();
		}
		else
		{
			DatePickerDialog datePickerDialog = new DatePickerDialog(BiodataActivity.this, d,
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));

			// setting todays date as a max date for calendar
			datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
			datePickerDialog.show();

			txtDateOfBirth.setText(EpiUtils.getDateFormat().format(cal.getTime()));
		}

	}

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		{
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDateField();
		}
	};

	public void startAgeCalculation(View widget)
	{

		IDialogListener dialogListener = new IDialogListener()
		{

			@Override
			public void onDialogPositiveClick(Map... o)
			{
				Intent intent = new Intent(BiodataActivity.this, CalculateAgeAcitivity.class);
				startActivityForResult(intent, REQUEST_CALCULATE_AGE);

			}

			@Override
			public void onDialogNeutralClick(Map... o)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onDialogNegativeClick(Map... o)
			{
				// TODO Auto-generated method stub

			}
		};
		if (txtDateOfBirth.getText() != null && !("".equals(txtDateOfBirth.getText().toString())))
		{
			EpiUtils.showYesNoDialog(BiodataActivity.this, "Changing the date of birth will reset the vaccinations. Do you want to continue?", "Alert", "Yes", "No", dialogListener).show();
		}
		else
		{
			Intent intent = new Intent(BiodataActivity.this, CalculateAgeAcitivity.class);
			startActivityForResult(intent, REQUEST_CALCULATE_AGE);
		}

	}

	private void updateDateField()
	{
		txtDateOfBirth.setText(EpiUtils.getDateFormat().format(cal.getTime()));
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean checked)
	{
		if (view.getId() == R.id.checkBoxIsChildNamed)
		{
			toggleChildName(!view.isChecked());
		}
	}

	private void toggleChildName(boolean isNamed)
	{
		// if (!isNamed)
		if (isNamed == false)
		{
			txtChildFirstName.setVisibility(View.GONE);
			txtChildFirstName.setText("");
			txtViewChildFirstName.setVisibility(View.GONE);
		}
		else
		{
			txtViewChildFirstName.setVisibility(View.VISIBLE);
			txtChildFirstName.setEnabled(true);
			txtChildFirstName.setVisibility(View.VISIBLE);
		}
	}

	private boolean validateDemographics()
	{
		ValidatorResult result = null;
		boolean allValid = true;

		// Date of Birth
		String dateOfBirth = txtDateOfBirth.getText().toString();
		_dob = DateTimeUtils.StringToDate(dateOfBirth, DateTimeUtils.DATE_FORMAT_SHORT);
		ValidatorUtil validator = new ValidatorUtil(this);

		result = validator.validateDateOfBirth(_dob);
		if (!result.isValid())
		{
			allValid = false;
			txtDateOfBirth.setError(result.getMessage());
		}

		// Project ID
		_projectId = txtProjectId.getText().toString();
		result = validator.validateChildId(_projectId);
		if (!result.isValid())
		{
			allValid = false;
			txtProjectId.setError(result.getMessage());
		}

		// EPI Np
		_epino = txtEpiNo.getText().toString();
		result = validator.validateEpiNo(_epino);
		if (!result.isValid())
		{
			allValid = false;
			txtEpiNo.setError(result.getMessage());
		}

		if (chkBoxIsChildNamed.isChecked() == false)
		{
			// Child Name
			_childFirstName = txtChildFirstName.getText().toString().trim();
			result = validator.validateName(_childFirstName);
			if (!result.isValid())
			{
				allValid = false;
				txtChildFirstName.setError(getResources().getString(R.string.ru_validation_error_child_first_name_empty));
			}

		}

		// Father name
		_fatherFirstName = txtFatherFirstName.getText().toString().trim();
		result = validator.validateName(_fatherFirstName);
		if (!result.isValid())
		{
			allValid = false;
			txtFatherFirstName.setError(getResources().getString(R.string.ru_validation_error_father_first_name_empty));
		}
		// No one born before 2010 can be registered in the program
		Date minDate = DateTimeUtils.StringToDate("01/01/2010", DateTimeUtils.DATE_FORMAT_SHORT);

		if (_dob != null)
		{
			if (_dob.getTime() < minDate.getTime())
			{
				allValid = false;
				txtDateOfBirth.setError(getResources().getString(R.string.minDate));
			}
		}

		return allValid;
	}

	public void save(View button)
	{
		boolean validated = validateDemographics();
		if (!validated)
		{
			final AlertDialog dialog = EpiUtils.showYesNoDialog(this, "Errors exist in information provided, are you sure" + " you wish to proceed?", "Attention!");
			dialog.setButton("Yes", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					sendIntent(false);
				}
			});
			dialog.setButton2("No", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					dialog.dismiss();
				}
			});
			dialog.show();
		}
		else
		{
			sendIntent(true);
		}
	}

	private void sendIntent(boolean isValid)
	{
		Intent intent = new Intent();
		intent.putExtra(IS_VALID, isValid);
		intent.putExtra(PROJECT_ID, _projectId);
		intent.putExtra(EPI_NO, _epino);
		intent.putExtra(IS_CHILD_NAMED, !chkBoxIsChildNamed.isChecked());
		intent.putExtra(CHILD_FIRST_NAME, _childFirstName);
		intent.putExtra(GENDER, spGender.getSelectedItem().toString().toLowerCase());
		intent.putExtra(FATHER_FIRST_NAME, _fatherFirstName);
		intent.putExtra(IS_ESTIMATED_DOB, _isEstimatedDOB);
		intent.putExtra(CHILD_DOB, DateTimeUtils.DateToString(_dob, null));

		setResult(RESULT_OK, intent);
		super.finish();
	}

	public void cancel(View button)
	{
		setResult(RESULT_CANCELED);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		// Check which request we're responding to
		if (requestCode == REQUEST_CALCULATE_AGE)
		{
			Date dtDOB = null;
			// Make sure the request was successful
			if (resultCode == RESULT_OK)
			{

				String dateRes = data.getStringExtra(CalculateAgeAcitivity.CALCULATED_AGE);
				/* Get the calculated Date of Birth */
				if (dateRes != null && (!"".equalsIgnoreCase(dateRes)))
				{
					try
					{
						dtDOB = EpiUtils.getDateFormat().parse(dateRes);
						_isEstimatedDOB = true;
					}
					catch (ParseException e)
					{
						dtDOB = new Date();
						Log.e("Biodata Activity", e.getMessage());
					}
				}
				else
				{
					dtDOB = new Date();
					Log.w(getString(R.string.alert_age_not_calculated), "Biodata Activity");
				}

				/* Get individual apprx. age fields */
				_days = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.DAYS, 0));
				_weeks = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.WEEKS, 0));
				_months = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.MONTHS, 0));
				_years = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.YEARS, 0));

			}
			// Set the date in the datepicker
			txtDateOfBirth.setText(EpiUtils.getDateFormat().format(dtDOB));

		}
		else if (requestCode == Barcode.BARCODE_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String str = data.getStringExtra(Barcode.SCAN_RESULT);
				this._projectId = str;
				txtProjectId.setText(str);
			}
		}

		/**
		 * @Sadiq To readjust the scrollview. it is what it is. Need to work on
		 *        this
		 */

		// TODO : fix this issue.

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	public void scan(View control)
	{
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
		/*
		 * intent.putExtra(Barcode.WIDTH, 300); intent.putExtra(Barcode.WIDTH,
		 * 300);
		 */
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		String epiNo = txtEpiNo.getText().toString();
		if (epiNo == null || epiNo.trim().length() <= 0)
			txtEpiNo.setText(cal.get(cal.YEAR) + "");

	}
}
