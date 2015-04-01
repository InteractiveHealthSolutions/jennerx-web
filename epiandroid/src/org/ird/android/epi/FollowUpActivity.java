package org.ird.android.epi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.NetworkProgressDialog;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.communication.HTTPSender;
import org.ird.android.epi.communication.INetworkUser;
import org.ird.android.epi.communication.ResponseReader;
import org.ird.android.epi.communication.elements.ResponseStatus;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.fragments.SupplementaryVaccineFragment;
import org.ird.android.epi.fragments.VaccineScheduleFragment;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccination;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.validation.VaccinationValidator;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

public class FollowUpActivity extends TabActivity implements IDialogListener, OnCheckedChangeListener, IBaseForm, INetworkUser, OnTabChangeListener
{
	public static final String DEMOGRAPHICS = "demog";
	public static final String PROGRAM_DETAILS = "progDet";
	public static final String IMMUNIZATOINS = "immuniz";

	protected Resources res;
	private TabHost tabFollowUp;

	private static NetworkProgressDialog progress;

	// EditTexts
	private EditText txtProjectId;

	private EditText txtchildName;
	private EditText txtFatherName;
	private EditText txtDOB;
	private EditText txtEpiNo;
	private EditText txtPrimaryNo;
	private EditText txtSecondaryNo;

	// TextViews
	private TextView txtViewPrimaryNo;
	private TextView txtViewSecondaryNo;

	// CheckBoxs
	private CheckBox chkBoxSmsReminder;

	Calendar cal = Calendar.getInstance();

	// next vaccine tab variables
	private static ViewGroup layoutNextVaccineContainer;

	VaccineScheduleFragment schedule;
	SupplementaryVaccineFragment supplementary;

	Child _child;

	// For Lottery
	Map<String, String> lotteries;

	// Flags form management
	boolean isFinished = false;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follow_up_layout);
		this.context = getApplicationContext();

		res = getResources();
		tabFollowUp = getTabHost();

		TabHost.TabSpec tabSpec;
		// Set the tab for fetching data from server
		tabSpec = tabFollowUp.newTabSpec("Demographics");
		tabSpec.setIndicator("Biodata");
		tabSpec.setContent(R.id.tabChildData);
		// add to TabHost
		tabFollowUp.addTab(tabSpec);

		// Create tab for Vaccination schedule
		tabSpec = tabFollowUp.newTabSpec("Vaccinations");
		tabSpec.setIndicator("Vaccinations");
		tabSpec.setContent(R.id.tabNextVaccines);
		tabFollowUp.addTab(tabSpec);

		// Create tab for Supplementary Vaccination
		tabSpec = tabFollowUp.newTabSpec("Vaccinations");
		tabSpec.setIndicator("Supplementary");
		tabSpec.setContent(R.id.tabSupplementaryVaccines);
		tabFollowUp.addTab(tabSpec);

		schedule = (VaccineScheduleFragment) getFragmentManager().findFragmentById(R.id.fragmentVac);
		supplementary = (SupplementaryVaccineFragment) getFragmentManager().findFragmentById(R.id.fragmentSupplementaryVac);


		getControls();

		/*
		 * Note: Initialize following EditText objects before disabling them.
		 * Disabling EditText of Child and Father Name
		 * to prevent changes.
		 */

		txtchildName.setEnabled(false);
		txtFatherName.setEnabled(false);

		setListeners();

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		changeControlsState(false);
	}

	@Override
	public void finish()
	{
		if (this.schedule != null)
		{
			schedule.setChild(null);
			schedule.updateSchedule();
		}
		super.finish();
	}


	/**
	 * Initializing form controls
	 */
	private void getControls()
	{
		txtProjectId = (EditText) findViewById(R.id.edtTextProjectId);

		txtEpiNo = (EditText) findViewById(R.id.editTextLastEPINo);
		txtDOB = (EditText) findViewById(R.id.edtTextDOBFUP);

		txtchildName = (EditText) findViewById(R.id.editTextChildFullName);
		txtFatherName = (EditText) findViewById(R.id.textViewFatherFullName);

		chkBoxSmsReminder = (CheckBox) findViewById(R.id.chkboxSMSFUP);

		txtPrimaryNo = (EditText) findViewById(R.id.editTexMobileNumber);
		txtViewPrimaryNo = (TextView) findViewById(R.id.textViewMobileNumber);

		txtSecondaryNo = (EditText) findViewById(R.id.editTextSecondaryNumber);
		txtViewSecondaryNo = (TextView) findViewById(R.id.textViewSecondaryNumber);
	}

	private void setListeners()
	{
		TabHost tabHost = getTabHost();

		tabHost.setOnTabChangedListener(this);

		chkBoxSmsReminder.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.form_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.menu_item_close:
			onBackPressed();
			break;

		case R.id.menu_item_upload:
			if (!isFinished)
			{
				boolean validated;
				validated = validateForm();
				if (validated)
				{
					sendToServer(createPayload());
				}
			}
			else
			{
				EpiUtils.showDismissableDialog(this, getResources().getString(R.string.form_already_submitted), "Error").show();
			}
			break;
		}
		return true;
	}

	private boolean validateForm()
	{
		boolean allClear = true;
		ValidatorUtil validator = new ValidatorUtil(this);
		ValidatorResult result;

		result = validator.validateEpiNo(txtEpiNo.getText().toString());
		if (!result.isValid())
		{
			txtEpiNo.setError(result.getMessage());
			allClear = false;
		}

		result = validator.validateChildId(txtProjectId.getText().toString());
		if (!result.isValid())
		{
			txtProjectId.setError(result.getMessage());
			allClear = false;
		}

		// TODO: Need to see validation rule for Primary Number
		// Validation rules for Primary mobile number:
		// Proper mobile number should be given if SMS Reminder is checked
		// Otherwise it should be left blank

		if (chkBoxSmsReminder.isChecked() == true)
		{
			result = validator.validateMobile(txtPrimaryNo.getText().toString());
			if (!result.isValid())
			{
				allClear = false;
				txtPrimaryNo.setError(result.getMessage());
			}
		}

		else if (txtPrimaryNo.getText().toString().isEmpty() == false)
		{
			// mobile number
			result = validator.validateMobile(txtPrimaryNo.getText().toString());
			if (!result.isValid())
			{
				allClear = false;
				txtPrimaryNo.setError(result.getMessage());
			}
		}

		// Secondary mobile number is optional and its rules are given below:
		// Proper mobile number should be given
		// Otherwise it should be left blank

		if (txtSecondaryNo.getText().toString().isEmpty() == false)
		{
			result = validator.validateMobile(txtSecondaryNo.getText().toString());
			if (!result.isValid())
			{
				allClear = false;
				txtSecondaryNo.setError(result.getMessage());
			}
		}

		List<VaccineScheduleRow> rows = schedule.getRows();

		if (!VaccinationValidator.checkMissingEligibleVaccines(this, rows))
			allClear = false;

		if (!VaccinationValidator.checkAnyVaccinesGivenToday(this, rows))
			allClear = false;

		if (!allClear)
		{
			EpiUtils.showDismissableDialog(this, "Please correct all errors in the form", "Error").show();
		}
		return allClear;
	}

	@Override
	public void onDialogPositiveClick(Map... o)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogNegativeClick(Map... o)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogNeutralClick(Map... o)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(CompoundButton widget, boolean checked)
	{
		// SMS Reminder
		if (widget.getId() == R.id.checkBoxSmsReminder)
		{
			if (checked)
			{
				txtPrimaryNo.setVisibility(View.VISIBLE);
				txtViewPrimaryNo.setVisibility(View.VISIBLE);
				txtSecondaryNo.setVisibility(View.VISIBLE);
				txtViewSecondaryNo.setVisibility(View.VISIBLE);
			}
			else
			{
				txtPrimaryNo.setVisibility(View.GONE);
				txtViewPrimaryNo.setVisibility(View.GONE);
				txtSecondaryNo.setVisibility(View.GONE);
				txtViewSecondaryNo.setVisibility(View.GONE);
			}
		}

		if (widget.getId() == chkBoxSmsReminder.getId())
		{
			if (checked == false)
			{
				// If Sms Reminder is not checked then Primary No. should not show any error
				txtPrimaryNo.setError(null);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendToServer(Map params)
	{
		try
		{
			// show animation
			progress = new NetworkProgressDialog(this);
			progress.setMessage(getResources().getString(R.string.alert_wait));
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					progress.run();
				}
			});

			// create server address: <server-url>+<webservice-path>
			StringBuilder address = new StringBuilder();
			address.append(new EpiUtils().getServerAddress(this));
			address.append(GlobalConstants.REST_FOLLOWUP);
			Log.i(FollowUpActivity.class.getSimpleName(), "Server address:" + address.toString());
			HTTPSender sender = new HTTPSender(this, HTTPSender.METHOD_POST, address.toString());
			sender.execute(params);
		}
		catch (Exception e)
		{
			Log.e(FollowUpActivity.class.getSimpleName(), "Error occurred while submitting Follow-up form to server : \n" + e.getMessage());
		}
	}

	@Override
	public List<ValidatorResult> validate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map createPayload()
	{
		HashMap<String, Object> params = new HashMap<String, Object>();
		try
		{
			params.put(RequestElements.LG_USERID, GlobalConstants.USER_PROGRAM_ID);

			// TODO: Add mechanism to save enrollment centres on mobile.
			params.put(RequestElements.ENROLLEMNT_CENTRE, GlobalConstants.VACCINATION_CENTRE_ID);
			params.put(RequestElements.CHILD_ID, txtProjectId.getText().toString());
			params.put(RequestElements.EPI_NO, txtEpiNo.getText().toString());
			boolean smsApproval = chkBoxSmsReminder.isChecked();
			params.put(RequestElements.SMS_REMINDER_APP, Boolean.toString(smsApproval));
			params.put(RequestElements.PRIMARY_NUMBER, txtPrimaryNo.getText() != null ? txtPrimaryNo.getText().toString() : "");
			params.put(RequestElements.SECONDARY_NUMBER, txtSecondaryNo.getText() != null ? txtSecondaryNo.getText().toString() : "");

			params.put(RequestElements.VACCINATION_SUPPLEMENTARY, supplementary != null ? supplementary.getVaccinations() : null);
			params.put(RequestElements.VACCINATION_SCHEDULE, schedule != null ? schedule.getVaccinations() : null);
		}
		catch (Exception ex)
		{
			Log.e(FollowUpActivity.class.getSimpleName(), ex.getMessage());
			EpiUtils.showDismissableDialog(this, "Error in sending form to server", "Error");
		}
		return params;
	}

	@Override
	public void handleException(Exception ex)
	{
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Barcode.BARCODE_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String qrCode = data.getStringExtra(Barcode.SCAN_RESULT);
				txtProjectId.setText(qrCode);
			}
		}
	}

	/**
	 * Method used for sending Project Id to get information regarding the child
	 * (biodata, program and vaccinations)
	 * 
	 * @param view
	 */
	public void fetchData(View view)
	{
		ValidatorUtil validator = new ValidatorUtil(this);
		ValidatorResult result;
		result = validator.validateChildId(txtProjectId.getText().toString());
		if (!result.isValid())
		{
			txtProjectId.setError(result.getMessage());
			EpiUtils.showDismissableDialog(this, "Error in Child ID", "error").show();
			return;
		}
		txtProjectId.setError(null);

		// Create Hashmap for sending to server
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put(RequestElements.CHILD_ID, txtProjectId.getText().toString().trim());

		// sending data to server
		try
		{
			// show animation
			progress = new NetworkProgressDialog(this);
			progress.setMessage(getResources().getString(R.string.alert_wait));
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					progress.run();
				}
			});

			// create server address: <server-url>+<webservice-path>
			StringBuilder address = new StringBuilder();
			address.append(new EpiUtils().getServerAddress(this));
			address.append(GlobalConstants.REST_FOLLOWUP);
			Log.i(FollowUpActivity.class.getSimpleName(), "Server address:" + address.toString());
			HTTPSender sender = new HTTPSender(this, HTTPSender.METHOD_GET, address.toString());
			sender.execute(params);
		}
		catch (Exception e)
		{
			Log.e(FollowUpActivity.class.getSimpleName(), "Error occurred while submitting Enrollment to server : \n" + e.getMessage());
		}
	}

	public void scan(View control)
	{
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
	}

	@Override
	public void responseRecieved(String response)
	{
		changeControlsState(true);

		String requestType = null;
		if (progress != null)
		{
			progress.dismiss();
		}
		ResponseReader reader = new ResponseReader(this);
		// status of the request
		int responseCode = reader.readStatus(response);
		// parameters in response
		Map<String, Object> mapParams;

		// Outcome 1: could not connect to server or exception occured when
		// connecting to it
		if (response == null)
		{
			// Toast.makeText(this,
			// getString(R.string.login_error_connecting_server),
			// Toast.LENGTH_LONG).show();
			AlertDialog alert = EpiUtils.showAlert(this, response, "No Response From Server", this);
			alert.show();
		}

		// Outcome 2: Success message from server. Check ResponseParams
		else if (ResponseStatus.STATUS_SUCCESS.getId().equals(responseCode))
		{
			// TODO:Update Vaccination Schedule
			mapParams = reader.readParams(response);
			if (mapParams != null)
			{
				for (String key : mapParams.keySet())
				{
					if (key.equalsIgnoreCase(DEMOGRAPHICS))
					{
						updateBiodata((JSONObject) mapParams.get(key));
					}
					else if (key.equalsIgnoreCase(IMMUNIZATOINS))
					{
						updateVaccinationSchedule(((JSONArray) mapParams.get(key)));
					}
					else if (key.equalsIgnoreCase(PROGRAM_DETAILS))
					{
						updateProgramPreferences((JSONObject) mapParams.get(key));
					}
					else if (key.equalsIgnoreCase(RequestElements.REQ_TYPE))
					{
						requestType = (String) mapParams.get(key);
					}
				}
			}
			// ONLY PROCEED TO LOTTERY AND FORM CLOSING IF THE RESPONSE WAS FOR
			// A SUCCESSFUL FORM SUBMISSION
			if (RequestElements.REQ_TYPE_FETCH.equalsIgnoreCase(requestType))
			{
				return;
			}

			isFinished = true;

			EpiUtils.showDismissableDialog(this, getResources().getString(R.string.form_submitted), "Success").show();

		}
		else if (ResponseStatus.STATUS_FAILURE.getId().equals(responseCode))
		{
			try
			{
				mapParams = reader.readParams(response);
				StringBuilder errors = new StringBuilder();
				errors.append("Following error(s) occured in server:");
				for (String key : mapParams.keySet())
				{
					errors.append("\n");
					errors.append(mapParams.get(key));
				}
				EpiUtils.showAlert(this, errors.toString(), "Error(s) Occurred", this).show();
			}
			catch (Exception e)
			{
				Log.e(FollowUpActivity.class.getSimpleName(), e.getMessage());
				Log.e(FollowUpActivity.class.getSimpleName(), "error processing response from server");
			}
		}

		else
		{
			// getting appropriate enum by using received response code.
			ResponseStatus recievedResponseStatus = ResponseStatus.values()[responseCode];

			EpiUtils.showDismissableDialog(this, "Response from server: Response Code = " + recievedResponseStatus.getId()
					+ "\n" + recievedResponseStatus.getMessage(), "Alert").show();
		}
	}

	private void updateBiodata(JSONObject biodata)
	{
		try
		{
			String gender = biodata.getString(RequestElements.CHILD_GENDER).toLowerCase();
			String epiNo = biodata.getString(RequestElements.EPI_NO).toString();
			String fatherFirstName = biodata.getString(RequestElements.FATHER_FIRST_NAME);
			fatherFirstName = fatherFirstName == null ? "" : fatherFirstName;

			/*
			 * String fatherLastName =
			 * biodata.getString(RequestElements.FATHER_LAST_NAME);
			 * fatherLastName = fatherLastName==null?"":fatherLastName;
			 */

			String childFirstName = biodata.getString(RequestElements.CHILD_FIRST_NAME);
			childFirstName = childFirstName == null ? "" : childFirstName;

			/*
			 * String childLastName=
			 * biodata.getString(RequestElements.CHILD_LAST_NAME); childLastName
			 * = childLastName==null?"":childLastName;
			 */

			String strDOB = ((String) biodata.getString(RequestElements.DOB));
			String fatherName = fatherFirstName;
			String childName = childFirstName;
			boolean sms, lottery;

			txtEpiNo.setText(epiNo == null ? "" : epiNo);
			txtFatherName.setText(fatherName);
			txtchildName.setText(childName);
			txtDOB.setText(strDOB);

			_child = new Child();
			_child.setProjectId(txtProjectId.getText().toString());
			_child.setDateOfBirth(DateTimeUtils.StringToDate(strDOB, null));
			_child.setChildFirstName(childFirstName);
			// _child.setChildLastName(childLastName);
			_child.setFatherFirstName(fatherFirstName);
			// _child.setFatherLastName(fatherLastName);
			_child.setGender(gender);

			schedule.setChild(_child);

			// lottery = biodata.getBoolean(RequestElements.LOTTERY_APPROVAL);
			// sms = biodata.getBoolean(RequestElements.SMS_REMINDER_APP);
			// if(sms)
			// {
			// txtPrimaryNo.setText(biodata.getString(RequestElements.REMINDER_PRIMARY_NUMBER));
			// txtSecondaryNo.setText(biodata.getString(RequestElements.REMINDER_SECONDARY_NUMBER));
			// }

		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateProgramPreferences(JSONObject preferences)
	{
		String primaryNumber;
		String secondaryNumber;
		boolean lottery;
		boolean sms;
		boolean sameCentre;

		try
		{
			// obtain values from JSON
			primaryNumber = preferences.getString(RequestElements.PRIMARY_NUMBER);
			secondaryNumber = preferences.getString(RequestElements.SECONDARY_NUMBER);
			lottery = Boolean.parseBoolean(preferences.getString(RequestElements.LOTTERY_APPROVAL).toString());
			sms = Boolean.parseBoolean(preferences.getString(RequestElements.SMS_REMINDER_APP).toString());

			// update controls
			chkBoxSmsReminder.setChecked(sms);
			txtPrimaryNo.setText(primaryNumber);
			txtSecondaryNo.setText(secondaryNumber);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param vaccinations
	 */
	private void updateVaccinationSchedule(JSONArray vaccinations)
	{
		List<Vaccination> vaccinationList = new ArrayList<Vaccination>();
		JSONObject tempObj;
		Vaccination tempVacc;

		String centre;
		String strDueDate;
		String strVacDate;
		Vaccine vaccine;
		String vaccineName;
		String status;

		try
		{
			for (int i = 0; i < vaccinations.length(); i++)
			{
				tempObj = vaccinations.getJSONObject(i);
				if (tempObj == null)
					continue;

				// if vaccine is supplementary then don't add it
				// we will handle them separately

				vaccineName = tempObj.getString(RequestElements.VACCINENAME);

				if (VaccineService.isSupplementaryVaccine(vaccineName, context))
				{
					continue;
				}

				// Get Vaccination related fields from JSON sent from server

				strDueDate = tempObj.getString(RequestElements.NEXT_ALLOTTED_DATE);
				strVacDate = tempObj.getString(RequestElements.DATE_OF_VACCINATION);
				// vaccineName = tempObj.getString(RequestElements.VACCINENAME);
				centre = tempObj.getString(RequestElements.VACCINATION_CENTER);
				status = tempObj.getString(RequestElements.VACCINATION_STATUS);

				// Prepare Vaccination object

				tempVacc = new Vaccination();
				tempVacc.setDueDate(DateTimeUtils.StringToDate(strDueDate, null));
				tempVacc.setVaccinationDate(DateTimeUtils.StringToDate(strVacDate, null));
				vaccine = VaccineService.getVaccineByName(vaccineName, context);
				tempVacc.setGivenVaccine(vaccine);
				tempVacc.setCentre(centre);

				/**
				 * Checking status and setting isEditable to ensure that rows
				 * are not editable unless they are still pending.T
				 */
				if (status.equals("PENDING"))
					tempVacc.setIsEditable(true);
				else
					tempVacc.setIsEditable(false);

				vaccinationList.add(tempVacc);

			}

			// TODO: use this vaccinatoins List to update the schedule

			if (schedule == null)
			{
				schedule = new VaccineScheduleFragment();
			}
			schedule.setVaccinations(vaccinationList);
			schedule.updateSchedule();
		}
		catch (Exception e)
		{
			handleScheduleError(e);
		}
	}

	private void handleScheduleError(Exception e)
	{
		String message = "Error updating schedule for child. Please contact " + "yout application provider";
		EpiUtils.showDismissableDialog(this, message, "Error");
		Log.e(FollowUpActivity.class.getSimpleName(), message);
		Log.e(FollowUpActivity.class.getSimpleName(), e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void onTabChanged(String arg0)
	{
		int i = getTabHost().getCurrentTab();
		int indexVaccines = 1;
		// Vaccinations tab is selected
		if (i >= indexVaccines)
		{
			if (_child == null)
			{
				EpiUtils.showDismissableDialog(this, "Please fetch a child's record first", "Error").show();
				getTabHost().setCurrentTab(0);
				return;
			}
		}
	}

	public void refresh(View v)
	{
		IDialogListener dialogListener = new IDialogListener()
		{

			@Override
			public void onDialogPositiveClick(Map... o)
			{
				schedule.setRow();

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

		EpiUtils.showYesNoDialog(this, "This will reset all the rows. Do you want to continue?", "Alert", "Yes", "No", dialogListener).show();

	}


	/**
	 * Use to change state of EPI no, DOB, Child and Father name, Sms check box and both contact number fields
	 * 
	 */
	private void changeControlsState(Boolean newState)
	{
		txtEpiNo.setEnabled(newState);
		txtDOB.setEnabled(newState);

		// CheckBoxs
		chkBoxSmsReminder.setEnabled(newState);

		// EditText
		txtPrimaryNo.setEnabled(newState);
		txtSecondaryNo.setEnabled(newState);
	}
}
