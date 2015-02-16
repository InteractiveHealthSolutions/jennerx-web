package org.ird.android.epi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.NetworkProgressDialog;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.communication.HTTPSender;
import org.ird.android.epi.communication.INetworkUser;
import org.ird.android.epi.communication.ResponseReader;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.communication.elements.ResponseStatus;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class IdChangeActivity extends Activity implements IBaseForm, INetworkUser, OnClickListener
{
	EditText txtOld;
	EditText txtNew;

	Button btnScan, btnUpdate, btnBack;

	private static NetworkProgressDialog progress;

	private static String TAG_ID_CHANGE = IdChangeActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identifier_change_layout);

		txtOld = (EditText) findViewById(R.id.editTextOldId);
		txtNew = (EditText) findViewById(R.id.editTextNewId);
		btnUpdate = (Button) findViewById(R.id.btnUpdateNewId);
		btnScan = (Button) findViewById(R.id.btnScanNewID);
		btnBack = (Button) findViewById(R.id.btnBackNewId);

		btnUpdate.setOnClickListener(this);
		btnScan.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Barcode.BARCODE_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String qrCode = data.getStringExtra(Barcode.SCAN_RESULT);
				txtNew.setText(qrCode);
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.id_change, menu);
		return true;
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

		// Outcome 1: could not connect to server or exception occured when connecting to it
		if (response == null)
		{
			// Toast.makeText(this, getString(R.string.login_error_connecting_server), Toast.LENGTH_LONG).show();
			EpiUtils.showDismissableDialog(this, "No response received from server", "Error").show();

		}

		// Outcome 2: Success message from server. Check ResponseParams
		else if (ResponseStatus.STATUS_SUCCESS.getId().equals(responseCode))
		{

			// show alert
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("ID updated successfully");
			builder.setTitle("Success");
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

		// Outcome 3: Failure due to data sent from mobile. ResponseParams likely
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
				EpiUtils.showDismissableDialog(this, errors.toString(), "Error(s) Occurred").show();
			}
			catch (Exception e)
			{
				Log.e(TAG_ID_CHANGE, e.getMessage());
				Log.e(TAG_ID_CHANGE, "error processing response from server");
			}
		}

		// Outcome 4: Some other error occured in server. Show error code which is sent in the response
		else
		{
			EpiUtils.showDismissableDialog(this, "Error occured at server. Error code is: " + responseCode, "Error").show();
		}
	}

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
			address.append(GlobalConstants.REST_FOLLOWUP + GlobalConstants.REST_ID_CHANGE);
			Log.i(FollowUpActivity.class.getSimpleName(), "Server address:" + address.toString());
			HTTPSender sender = new HTTPSender(this, HTTPSender.METHOD_GET, address.toString());
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
		params.put(RequestElements.CHILD_ID, txtNew.getText().toString());
		params.put(RequestElements.CHILD_NFC_ID, txtOld.getText().toString());
		return params;
	}

	@Override
	public void handleException(Exception ex)
	{
		// TODO Auto-generated method stub

	}

	private boolean validateForm()
	{
		boolean isValid = true;
		// reset the error messages
		txtOld.setError(null);
		txtNew.setError(null);

		// Old ID
		if (txtOld.getText() == null || "".equalsIgnoreCase(txtOld.getText().toString()))
		{
			txtOld.setError("ID khali nahee ho sakti");
			isValid = false;
		}
		else
		{
			if (txtOld.length() < 14)
			{
				txtOld.setError("ID 14 digit ki honee chahiye");
				isValid = false;
			}
		}

		// New Id
		ValidatorUtil validator = new ValidatorUtil(this);
		ValidatorResult result;
		result = validator.validateChildId(txtNew.getText().toString());
		if (!result.isValid())
		{
			txtNew.setError(result.getMessage());
			isValid = false;
		}

		return isValid;
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == btnUpdate.getId())
		{
			if (validateForm())
			{
				sendToServer(createPayload());
			}
		}
		else if (v.getId() == btnBack.getId())
		{
			EpiUtils.showYesNoDialog(this, "Are you sure you wish to exit the form?", "Confirm", "Yes", "No", new IDialogListener()
			{
				@Override
				public void onDialogPositiveClick(Map... o)
				{
					onBackPressed();
				}

				@Override
				public void onDialogNeutralClick(Map... o)
				{
				}

				@Override
				public void onDialogNegativeClick(Map... o)
				{
					// do nothing, stay here
				}
			}).show();
		}
		else if (v.getId() == btnScan.getId())
		{
			Intent intent = new Intent(Barcode.BARCODE_INTENT);
			intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
			startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
		}

	}

}
