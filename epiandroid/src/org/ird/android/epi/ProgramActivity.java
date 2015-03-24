package org.ird.android.epi;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class ProgramActivity extends Activity implements
		OnCheckedChangeListener
{
	private static final String TAG_PROGRAM_DETAILS = ProgramActivity.class
			.getSimpleName();

	boolean _sms;
	String _mobile;
	String _landline;
	String _secondary;
	boolean isValidated;
	boolean _sameCentre;

	public static final String SMS_APPROVAL = "sms";
	public static final String SAME_CENTRE = "samcentr";
	public static final String MOBILE_NUMBER = "mobno";
	public static final String LANDLINE_NUMBER = "landno";
	public static final String IS_VALID = "isvalid";

	// textviews
	private TextView txtViewPrimaryNo;
	private TextView txtViewSecondaryNo;

	// checkboxes
	private CheckBox chkBoxSameCenter;
	private CheckBox chkBoxIsChildNamed;
	private CheckBox chkBoxSmsReminder;

	// edittexts
	private EditText txtPrimaryNo;
	private EditText txtSecondaryNo;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_layout);

		chkBoxSameCenter = (CheckBox) findViewById(R.id.checkBoxSameCenter);
		chkBoxSmsReminder = (CheckBox) findViewById(R.id.checkBoxSmsReminder);

		txtViewPrimaryNo = (TextView) findViewById(R.id.textViewPrimaryNo);
		txtPrimaryNo = (EditText) findViewById(R.id.editTextPrimaryNo);

		txtViewSecondaryNo = (TextView) findViewById(R.id.textViewSecondaryNo);
		txtSecondaryNo = (EditText) findViewById(R.id.editTextSecondaryNo);


		txtPrimaryNo.setVisibility(View.GONE);
		txtViewPrimaryNo.setVisibility(View.GONE);
		txtSecondaryNo.setVisibility(View.GONE);
		txtViewSecondaryNo.setVisibility(View.GONE);

		chkBoxSmsReminder.setOnCheckedChangeListener(this);
		fillFields(getIntent().getExtras());
	}

	private void fillFields(Bundle data)
	{
		// try to populate using the default values from parent Activity
		try
		{

			_sms = data.getBoolean(SMS_APPROVAL);
			chkBoxSmsReminder.setChecked(_sms);

			_sameCentre = data.getBoolean(SAME_CENTRE);
			chkBoxSameCenter.setChecked(_sameCentre);

			txtPrimaryNo.setText(data.getString(MOBILE_NUMBER));

			txtSecondaryNo.setText(data.getString(LANDLINE_NUMBER));
		}
		catch (Exception ex)
		{
			Log.e(TAG_PROGRAM_DETAILS, "Error setting default values");
			Log.e(TAG_PROGRAM_DETAILS, ex.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.program, menu);
		return true;
	}

	private boolean validateProgramDetails()
	{
		_mobile = txtPrimaryNo.getText().toString();
		_secondary = txtSecondaryNo.getText().toString();

		boolean allValid = true;
		
		// only need to validate the numbers as an unchecked checkbox means a no
		// by-default
		if (chkBoxSmsReminder.isChecked())
		{
			ValidatorUtil validator = new ValidatorUtil(this);
			ValidatorResult result;

			// mobile number
			result = validator.validateMobile(txtPrimaryNo.getText().toString());
			if (!result.isValid())
			{
				allValid = false;
				txtPrimaryNo.setError(result.getMessage());
			}

			// Secondary mobile number is optional and its rules are given below:
			// Proper mobile number should be given			
			// Otherwise it should be left blank

			if (txtSecondaryNo.getText().toString().isEmpty() == false)
			{
				result = validator.validateMobile(txtSecondaryNo.getText().toString());
				if (!result.isValid())
				{
					allValid = false;
					txtSecondaryNo.setError(result.getMessage());
				}
			}
		}
		return allValid;
	}

	public void save(View button)
	{
		boolean validated = validateProgramDetails();
		if (!validated)
		{
			final AlertDialog dialog = EpiUtils.showYesNoDialog(this,
					"Errors exist in information provided, are you sure"
							+ " you wish to proceed?", "Attention!");
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
		intent.putExtra(SMS_APPROVAL, chkBoxSmsReminder.isChecked());
		intent.putExtra(SAME_CENTRE, chkBoxSameCenter.isChecked());
		intent.putExtra(MOBILE_NUMBER, _mobile);
		intent.putExtra(LANDLINE_NUMBER, _secondary);

		setResult(RESULT_OK, intent);
		super.finish();
	}

	public void cancel(View button)
	{
		setResult(RESULT_CANCELED);
		super.finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if (buttonView.getId() == chkBoxSmsReminder.getId())
		{
			txtViewPrimaryNo.setEnabled(isChecked);
			txtSecondaryNo.setEnabled(isChecked);
			if (isChecked)
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
	}

}
