package org.ird.android.epi;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.ird.android.epi.communication.HTTPSender;
import org.ird.android.epi.communication.INetworkUser;
import org.ird.android.epi.communication.ResponseReader;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.communication.elements.ResponseStatus;
import org.ird.android.epi.fragments.ChildHeaderFragment;
import org.ird.android.epi.fragments.SupplementaryVaccineFragment;
import org.ird.android.epi.fragments.VaccineScheduleFragment;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.nfc.INFCImplmenter;
import org.ird.android.epi.validation.Regex;
import org.ird.android.epi.validation.VaccinationValidator;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

public class EnrollmentActivity extends TabActivity implements OnMenuItemClickListener, OnTabChangeListener, OnCheckedChangeListener, OnClickListener, INetworkUser, IBaseForm,
		IDialogListener,
		INFCImplmenter, OnItemSelectedListener
{

	protected Resources res;

	// Image VIEWS
	ImageView imgBiodataStatus;
	ImageView imgAddressStatus;
	ImageView imgProgramStatus;
	private String _projectId;
	private String _epiNo;

	// biodata fields
	private String _childFirstName = null;
	// private String _childLastName = null;
	private boolean _isChildNamed = true;
	private String _fatherFirstName = null;
	// private String _fatherLastName = null;
	private String _gender = "";
	private Date _dob = null;
	private boolean _isEstimatedDOB = false;
	private boolean _biodataValidated = true;

	// address fields
	boolean _addressValidated;
	String _houseNo = null;
	String _street = null;
	String _colony = null;
	String _sector = null;
	String _landmark = null;
	String _uc = null;
	String _town = null;
	String _city = null;

	// program details fields
	boolean _progamDetailsValidated;
	boolean _sms;
	String _mobile;
	// String _landline;
	String _secondary;
	boolean isValidated;
	boolean _sameCentre;

	// calculated age fields
	private String _days = null;
	private String _weeks = null;
	private String _months = null;
	private String _years = null;

	// Child Header
	ChildHeaderFragment header;

	// the model's Child object
	Child _theChild;

	// VaccinionSchedule
	VaccineScheduleFragment schedule;

	// Supplementary Vaccinion
	SupplementaryVaccineFragment supplementary;

	// tab
	TabHost tabHostEnrollment;

	// MenuItems
	// Button itemUpload;

	Calendar cal = Calendar.getInstance();

	private ArrayAdapter<String> arrayAdapter;
	// Request for starting new Activity, use in onActivityResult
	private int REQUEST_CALCULATE_AGE = 1;
	private int REQUEST_GET_ADDRESS = 2;
	private int REQUEST_NFC_WRITE = 3;
	private int REQUEST_BIODATA = 4;
	private int REQUEST_PROGRAM_DETAILS = 5;

	private static final String TAG_ENROLLMENT = "EnrollmentActivity";
	private static NetworkProgressDialog progress;
	private boolean isBirthdateEstimated = false;

	// For validation
	private static ValidatorUtil validator;

	// For Preferences
	SharedPreferences preferences;

	private boolean isFinished = false;

	private boolean forceClose = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enrollment_layout);

		try
		{
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			// Initialize Controls
			imgBiodataStatus = (ImageView) findViewById(R.id.imgStatusBio);
			imgAddressStatus = (ImageView) findViewById(R.id.imgStatusAddress);
			imgProgramStatus = (ImageView) findViewById(R.id.imgStatusProgram);

			TabHost tabHostEnrollment = (TabHost) findViewById(android.R.id.tabhost);
			res = getResources();
			TabHost.TabSpec tabSpec;

			// child header
			header = (ChildHeaderFragment) getFragmentManager().findFragmentById(R.id.fragEnrolHeader);

			// VaccinationSchedule
			schedule = (VaccineScheduleFragment) getFragmentManager().findFragmentById(R.id.fragmentScheduleEnrollment);
			// Supplementary Vaccine
			supplementary = (SupplementaryVaccineFragment) getFragmentManager().findFragmentById(R.id.fragmentSupplemetaryEnrollment);

			// if(fragment !=null)
			// {
			// Child ch = ChildService.getChildById("123456");
			// fragment.setChild(ch);
			// }

			// Set the demographics tab
			tabSpec = tabHostEnrollment.newTabSpec("Child");
			tabSpec.setIndicator("Child");
			tabSpec.setContent(R.id.tabEnrollmentHome);
			// add to TabHost
			tabHostEnrollment.addTab(tabSpec);

			// Set the vaccine tab
			tabSpec = tabHostEnrollment.newTabSpec("Vaccinations");
			tabSpec.setIndicator("Vaccinations");
			tabSpec.setContent(R.id.tabVaccineSchedule);
			// add to TabHost
			tabHostEnrollment.addTab(tabSpec);

			// Set the supplementary vaccine tab
			tabSpec = tabHostEnrollment.newTabSpec("Supplementary");
			tabSpec.setIndicator("Supplementary");
			tabSpec.setContent(R.id.tabSupplemetaryVaccineSchedule);
			// add to TabHost
			tabHostEnrollment.addTab(tabSpec);

			// Add Listeners
			tabHostEnrollment.setOnTabChangedListener(this);

			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.array_gender));
			validator = new ValidatorUtil(this);

		}
		catch (Exception e)
		{
			Log.e("epi", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.form_menu, menu);
		return true;
	}

	private void updateHeader()
	{
		if (header != null)
		{
			header.updateHeader(_theChild);
		}
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
						isBirthdateEstimated = true;
					}
					catch (ParseException e)
					{
						dtDOB = new Date();
						Log.e(TAG_ENROLLMENT, e.getMessage());
					}
				}
				else
				{
					dtDOB = new Date();
					Log.w(getString(R.string.alert_age_not_calculated), TAG_ENROLLMENT);
				}

				/* Get individual apprx. age fields */
				_days = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.DAYS, 0));
				_weeks = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.WEEKS, 0));
				_months = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.MONTHS, 0));
				_years = String.valueOf(data.getIntExtra(CalculateAgeAcitivity.YEARS, 0));
			}
		}

		// Result from Adddres Activity
		else if (requestCode == REQUEST_GET_ADDRESS)
		{
			if (resultCode == RESULT_OK)
			{
				_addressValidated = data.getBooleanExtra(AddressActivity.ISVALID, true);
				_houseNo = data.getStringExtra(AddressActivity.ADDRESS_HOUSE);
				_street = data.getStringExtra(AddressActivity.ADDRESS_STREET);
				_sector = data.getStringExtra(AddressActivity.ADDRESS_SECTOR);
				_colony = data.getStringExtra(AddressActivity.ADDRESS_COLONY);
				_town = String.valueOf(data.getIntExtra(AddressActivity.ADDRESS_TOWN, -1));
				_uc = String.valueOf(data.getIntExtra(AddressActivity.ADDRESS_UC, -1));
				_city = String.valueOf(data.getIntExtra(AddressActivity.ADDRESS_CITY, -1));
				_landmark = data.getStringExtra(AddressActivity.ADDRESS_LANDMARK);

				if (_addressValidated)
				{
					imgAddressStatus.setImageDrawable(getResources().getDrawable(R.drawable.valid));
				}
				else
				{
					imgAddressStatus.setImageDrawable(getResources().getDrawable(R.drawable.error));
				}
			}
			else if (resultCode == RESULT_CANCELED)
			{
				// Do nothing
			}
		}
		else if (requestCode == Barcode.BARCODE_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String str = data.getStringExtra(Barcode.SCAN_RESULT);
				header.setProjectId(str);
				_projectId = str;
			}
		}

		else if (requestCode == REQUEST_BIODATA)
		{
			if (resultCode == RESULT_OK)// the activity was not cancelled by the
										// user
			{
				if (data == null) // no bundle hence nothing to fill
					return;
				try
				{
					// flag for checking the data entered passed validation
					_projectId = data.getStringExtra(BiodataActivity.PROJECT_ID);
					_epiNo = data.getStringExtra(BiodataActivity.EPI_NO);
					_biodataValidated = data.getBooleanExtra(BiodataActivity.IS_VALID, true);
					_fatherFirstName = data.getStringExtra(BiodataActivity.FATHER_FIRST_NAME);
					// _fatherLastName =
					// data.getStringExtra(BiodataActivity.FATHER_LAST_NAME);
					_isChildNamed = data.getBooleanExtra(BiodataActivity.IS_CHILD_NAMED, true);
					_isEstimatedDOB = data.getBooleanExtra(BiodataActivity.IS_ESTIMATED_DOB, true);
					_childFirstName = data.getStringExtra(BiodataActivity.CHILD_FIRST_NAME);
					// _childLastName =
					// data.getStringExtra(BiodataActivity.CHILD_LAST_NAME);

					// if the date changes then the schedule will be updated
					if (_dob != null)
					{
						if (_dob.getTime() != DateTimeUtils.StringToDate(data.getStringExtra(BiodataActivity.CHILD_DOB), null).getTime())
							schedule.isListUpdated = true;
					}

					_dob = DateTimeUtils.StringToDate(data.getStringExtra(BiodataActivity.CHILD_DOB), null);
					_gender = data.getStringExtra(BiodataActivity.GENDER);

					if (_biodataValidated)
					{
						imgBiodataStatus.setImageDrawable(getResources().getDrawable(R.drawable.valid));
					}
					else
					{
						imgBiodataStatus.setImageDrawable(getResources().getDrawable(R.drawable.error));
					}
					updateChild();
					updateHeader();
					if (schedule != null)
					{
						schedule.setChild(_theChild);
						ArrayList<VaccineScheduleRow> listRows = schedule.updateSchedule();
						// setting schedulable rows to scheduled
						schedule.setAutoScheduledVaccines(listRows);
					}
					if (schedule == null)
					{
						schedule.setChild(_theChild);
						// schedule.updateSchedule();

						ArrayList<VaccineScheduleRow> listRows = schedule.updateSchedule();

						// setting schedulable rows to scheduled
						schedule.setAutoScheduledVaccines(listRows);
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					Log.e(TAG_ENROLLMENT, "Error saving Biodata information");
					Log.e(TAG_ENROLLMENT, ex.getMessage());
				}
				catch (Throwable th)
				{
					th.printStackTrace();
					Log.e(TAG_ENROLLMENT, "Error saving Biodata information");
					Log.e(TAG_ENROLLMENT, th.getMessage());
				}
			}
		}
		else if (requestCode == REQUEST_PROGRAM_DETAILS)
		{
			if (resultCode == RESULT_OK)
			{
				try
				{
					_progamDetailsValidated = data.getBooleanExtra(ProgramActivity.IS_VALID, true);
					_sameCentre = data.getBooleanExtra(ProgramActivity.SAME_CENTRE, true);
					_sms = data.getBooleanExtra(ProgramActivity.SMS_APPROVAL, true);
					_mobile = data.getStringExtra(ProgramActivity.MOBILE_NUMBER);
					_secondary = data.getStringExtra(ProgramActivity.LANDLINE_NUMBER);
					if (_progamDetailsValidated)
					{
						imgProgramStatus.setImageDrawable(getResources().getDrawable(R.drawable.valid));
					}
					else
					{
						imgProgramStatus.setImageDrawable(getResources().getDrawable(R.drawable.error));
					}
				}
				catch (Exception ex)
				{
					Log.e(TAG_ENROLLMENT, ex.getMessage());
					Log.e(TAG_ENROLLMENT, "Error while setting Program Details ");
				}
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton widget, boolean checked)
	{
	}

	private void updateChild()
	{
		try
		{
			if (_theChild == null)
				_theChild = new Child();

			_theChild.setProjectId(_projectId);
			_theChild.setDateOfBirht(_dob);
			_theChild.setNamed(_isChildNamed);
			if (_isChildNamed)
			{
				_theChild.setChildFirstName(_childFirstName);
				// _theChild.setChildLastName(_childLastName);
			}

			_theChild.setFatherFirstName(_fatherFirstName);
			// _theChild.setFatherFirstName(_fatherLastName);
			_theChild.setGender(_gender);
			_theChild.setEpiNumber(_epiNo);
		}
		catch (Exception e)
		{
			Log.e(TAG_ENROLLMENT, e.getMessage());
			Log.e(TAG_ENROLLMENT, "Error updating child");
		}
	}

	public void openProgramDetails(View view)
	{
		Intent intent = new Intent(this, ProgramActivity.class);
		intent.putExtra(ProgramActivity.MOBILE_NUMBER, _mobile);
		intent.putExtra(ProgramActivity.LANDLINE_NUMBER, _secondary);
		intent.putExtra(ProgramActivity.SMS_APPROVAL, _sms);
		intent.putExtra(ProgramActivity.SAME_CENTRE, _sameCentre);

		startActivityForResult(intent, REQUEST_PROGRAM_DETAILS);
	}

	public void openAddressActivity(View view)
	{
		Intent intent = new Intent(this, AddressActivity.class);
		intent.putExtra(AddressActivity.ADDRESS_STREET, _street);
		intent.putExtra(AddressActivity.ADDRESS_SECTOR, _sector);
		intent.putExtra(AddressActivity.ADDRESS_COLONY, _colony);
		intent.putExtra(AddressActivity.ADDRESS_HOUSE, _houseNo);
		intent.putExtra(AddressActivity.ADDRESS_TOWN, _town);
		intent.putExtra(AddressActivity.ADDRESS_UC, _uc);
		intent.putExtra(AddressActivity.ADDRESS_CITY, _city);
		intent.putExtra(AddressActivity.ADDRESS_LANDMARK, _landmark);

		startActivityForResult(intent, REQUEST_GET_ADDRESS);
	}

	@Override
	public void responseRecieved(String response)
	{
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
			isFinished = true;
			// show alert
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(getResources().getString(R.string.form_submitted));
			builder.setTitle("Alert");
			builder.setPositiveButton("Ok", new AlertDialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			builder.show();
		}

		// Outcome 3: Failure due to data sent from mobile. ResponseParams
		// likely
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
				Log.e(TAG_ENROLLMENT, e.getMessage());
				Log.e(TAG_ENROLLMENT, "error processing response from server");
			}
		}

		// Outcome 4: Some other error occured in server. Show error code which
		// is sent in the response
		else
		{
			// getting appropriate enum by using received response code.
			ResponseStatus recievedResponseStatus = ResponseStatus.values()[responseCode];

			// EpiUtils.showServerSideError(this, responseCode, this).show();
			EpiUtils.showDismissableDialog(this, "Response from server: Response Code = " + recievedResponseStatus.getId()
					+ "\n" + recievedResponseStatus.getMessage(), "Alert").show();
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
			address.append(GlobalConstants.REST_ENROLLMENT);
			Log.i(TAG_ENROLLMENT, "Server address:" + address.toString());
			HTTPSender sender = new HTTPSender(this, HTTPSender.METHOD_POST, address.toString());
			sender.execute(params);
		}
		catch (Exception e)
		{
			Log.e(TAG_ENROLLMENT, "Error occurred while submitting Enrollment to server : \n" + e.getMessage());
		}
	}

	private List<ValidatorResult> validateAddress()
	{
		ArrayList<ValidatorResult> result = new ArrayList<ValidatorResult>();

		ValidatorResult isValidTown = validator.validateAddressTown(_town);
		ValidatorResult isValidCity = validator.validateAddressCity(_city);

		if (!isValidCity.isValid() && !isValidTown.isValid())
		{
			result.add(isValidCity);
			result.add(isValidTown);
		}
		return result;
	}

	public void openBiodataActivity(View view)
	{
		Intent intent = new Intent(this, BiodataActivity.class);
		// TODO: add a mechanism for passing interface here
		intent.putExtra(BiodataActivity.IS_VALID, _biodataValidated);
		intent.putExtra(BiodataActivity.PROJECT_ID, _projectId);
		intent.putExtra(BiodataActivity.EPI_NO, _epiNo);
		intent.putExtra(BiodataActivity.IS_CHILD_NAMED, _isChildNamed);
		intent.putExtra(BiodataActivity.CHILD_FIRST_NAME, _childFirstName);
		// intent.putExtra(BiodataActivity.CHILD_LAST_NAME, _childLastName);
		intent.putExtra(BiodataActivity.GENDER, _gender);
		intent.putExtra(BiodataActivity.FATHER_FIRST_NAME, _fatherFirstName);
		// intent.putExtra(BiodataActivity.FATHER_LAST_NAME, _fatherLastName);
		intent.putExtra(BiodataActivity.CHILD_DOB, DateTimeUtils.DateToString(_dob, null));

		startActivityForResult(intent, REQUEST_BIODATA);
	}

	public void showError(View control)
	{
		if (control.getTag() != null & (control.getTag() instanceof String))
		{
			String msg = control.getTag().toString();
			AlertDialog alert = EpiUtils.showAlert(this, msg, "Error", this);
			alert.show();
		}
	}

	private boolean validateForm()
	{
		String genericErrorMessage = "Error(s) in Form. Please check for missing data of "
				+ "Biodata, Address and Reminder sections";

		boolean allClear = true;

		if (_addressValidated != true || _biodataValidated != true || _progamDetailsValidated != true)
			allClear = false;

		List<VaccineScheduleRow> rows = schedule.getRows();

		if (!VaccinationValidator.checkMissingEligibleVaccines(this, rows))
			allClear = false;

		if (!VaccinationValidator.checkAnyVaccinesGivenToday(this, rows))
			allClear = false;

		if (!allClear)
		{
			EpiUtils.showDismissableDialog(this, genericErrorMessage, "Error").show();
		}

		else if (!header.getProjectID().matches(Regex.NUMERIC.toString()))
		{
			EpiUtils.showDismissableDialog(this, genericErrorMessage, "Error").show();
			allClear = false;
		}

		return allClear;
	}

	@Override
	public List<ValidatorResult> validate()
	{
		List<ValidatorResult> list = new ArrayList<ValidatorResult>();
		StringBuilder errMsg = new StringBuilder();
		StringBuilder warningMsg = new StringBuilder();
		StringBuilder finalMsg = new StringBuilder();
		StringBuilder finalTitle = new StringBuilder();
		// list.addAll(0,validateDemographics());
		list.addAll(validateAddress());

		if (list.size() == 0)
		{
			return list;
		}
		int errorCount = 0;
		int warningCount = 0;
		for (ValidatorResult result : list)
		{
			if (!result.isDismissable())
			{
				errorCount++;
				errMsg.append(errorCount + ". " + result.getMessage());
				errMsg.append("\n");
			}
			else
			{
				warningCount++;
				warningMsg.append(warningCount + ". " + result.getMessage());
				warningMsg.append("\n");
			}
		}
		// Set Title & Body of dialog which will show the warnings and errors
		if (errorCount > 0 && warningCount > 0)
		{
			finalTitle.append(errorCount + " error(s) and " + warningCount + " warnings found.");
			finalMsg.append("Errors:\n");
			finalMsg.append(errMsg.toString());
			finalMsg.append("Warnings:\n");
			finalMsg.append(warningMsg.toString());

		}
		else if (errorCount > 0 && warningCount == 0)
		{
			finalTitle.append(errorCount + " error(s) found");
			finalMsg.append("Errors:\n");
			finalMsg.append(errMsg.toString());

		}
		else if (errorCount == 0 && warningCount > 0)
		{
			finalTitle.append(warningCount + " warning(s) found");
			finalMsg.append("Warnings:\n");
			finalMsg.append(warningMsg.toString());
		}
		EpiUtils.showAlert(this, finalMsg.toString(), finalTitle.toString(), this).show();

		// address
		// boolean isValidAddress = validateAddress();

		// PROGRAM DETAILS TAB
		return list;

	}



	@Override
	public void onDialogPositiveClick(Map... o)
	{
		if (isFinished)
		{
			this.finish();
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			isFinished = false;
		}
	}

	@Override
	public void onDialogNegativeClick(Map... o)
	{
		// do nothing, stay on the form
	}

	@Override
	public void onDialogNeutralClick(Map... o)
	{

	}

	@Override
	public Map createPayload()
	{
		HashMap<String, Object> params = new HashMap<String, Object>();

		try
		{
			// demographics

			params.put(RequestElements.REQUEST_DATE_FORMAT, GlobalConstants.DATE_FORMAT);			

			params.put(RequestElements.LG_USERID, GlobalConstants.USER_PROGRAM_ID);

			params.put(RequestElements.ENROLLEMNT_CENTRE, GlobalConstants.VACCINATION_CENTRE_ID);
			if (getResources().getString(R.string.gender_male).equalsIgnoreCase(_gender))
			{
				params.put(RequestElements.CHILD_GENDER, "M");
			}
			else if (getResources().getString(R.string.gender_female).equalsIgnoreCase(_gender))
			{
				params.put(RequestElements.CHILD_GENDER, "F");
			}
			params.put(RequestElements.CHILD_PROG_ID, header.getProjectID());
			if (_isChildNamed)
			{
				params.put(RequestElements.CHILD_NAMED, Boolean.TRUE.toString());
				params.put(RequestElements.CHILD_FIRST_NAME, _childFirstName);
				// params.put(RequestElements.CHILD_LAST_NAME, _childLastName);
			}
			else
			{
				params.put(RequestElements.CHILD_NAMED, Boolean.FALSE.toString());
			}

			params.put(RequestElements.FATHER_FIRST_NAME, _fatherFirstName);
			// params.put(RequestElements.FATHER_LAST_NAME, _fatherLastName);

			params.put(RequestElements.DOB, DateTimeUtils.DateToString(_dob, null));
			params.put(RequestElements.IS_BIRHTDATE_ESTIMATED, String.valueOf(_isEstimatedDOB));
			params.put(RequestElements.DOB_DAY, _days);
			params.put(RequestElements.DOB_WEEK, _weeks);
			params.put(RequestElements.DOB_MONTH, _months);
			params.put(RequestElements.DOB_YEAR, _years);
			params.put(RequestElements.EPI_NO, _epiNo);

			// address
			params.put(RequestElements.ADD_HOUSENO, _houseNo);
			params.put(RequestElements.ADD_STREET, _street);
			params.put(RequestElements.ADD_SECTOR, _sector);
			params.put(RequestElements.ADD_COLONY, _colony);
			params.put(RequestElements.ADD_UC, _uc);
			params.put(RequestElements.ADD_TOWN, _town);
			params.put(RequestElements.ADD_CITY, _city);


			params.put(RequestElements.VACCINATION_SUPPLEMENTARY, supplementary != null ? supplementary.getVaccinations() : null);
		
			params.put(RequestElements.VACCINATION_SCHEDULE, schedule != null ? schedule.getVaccinations() : null);		

			// params.put(RequestElements.NEXT_VACCINE, getNextVaccines());

			// program details
			params.put(RequestElements.SAME_CENTER, String.valueOf(_sameCentre));
			params.put(RequestElements.SMS_REMINDER_APP, Boolean.toString(_sms));
			params.put(RequestElements.PRIMARY_NUMBER, _mobile);
			params.put(RequestElements.SECONDARY_NUMBER, _secondary);
		}
		catch (Exception e)
		{
			Log.e(TAG_ENROLLMENT, e.getMessage());
			e.printStackTrace();
		}
		return params;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item)
	{
		// if(item.getItemId() == itemUpload.getItemId())
		// {
		// Toast.makeText(this, "uploaded", Toast.LENGTH_LONG).show();
		// }
		//
		return true;
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			break;

		case R.id.menu_item_close:
			onBackPressed();
			break;

		case R.id.menu_item_upload:

			if (!isFinished)
			{
				if (validateForm())
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

	@Override
	public void responseRecieved(String response, int code)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View target, int position, long rowId)
	{

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	@Override
	public void handleException(Exception ex)
	{
		Log.e(TAG_ENROLLMENT, ex.getMessage());
	}

	@Override
	public void finish()
	{
		if (forceClose)
			super.finish();

		if (isFinished == false)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Form not submitted yet. Are you sure you wish to exit the form?");
			builder.setIcon(R.drawable.alert);
			builder.setTitle("ALERT");
			builder.setPositiveButton("Yes", new AlertDialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					/*
					 * TODO: this is an ugly solution. Devise a more graceful
					 * strategy for moving out of finish() method based on
					 * user's choice.
					 */
					forceClose = true;
					EnrollmentActivity.this.finish();
				}
			});
			builder.setNegativeButton("No", new AlertDialog.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					return;
				}
			});
			builder.create().show();
		}
		else
		{
			super.finish();
		}
	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == getTabWidget().getChildAt(1).getId())
		{
			Toast.makeText(this, "Vaccinations clicked", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onTabChanged(String tabId)
	{
		int i = getTabHost().getCurrentTab();
		int indexVaccines = 1;
		// Vaccinations tab is selected
		if (i >= indexVaccines)
		{
			if (_theChild == null)
			{
				EpiUtils.showDismissableDialog(this, "Please enter Biodata first", "Error").show();
				getTabHost().setCurrentTab(0);
				return;
			}
			else if (_theChild.getDateOfBirth() == null)
			{
				EpiUtils.showDismissableDialog(this, "Child's date of birth must be provided to view vaccination schedule.", "Error").show();
				getTabHost().setCurrentTab(0);
				return;
			}
		}

	}

	public void scan(View control)
	{
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
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
}
