package org.ird.android.epi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.android.epi.alert.NetworkProgressDialog;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.common.SharedPreferencesReader;
import org.ird.android.epi.communication.HTTPSender;
import org.ird.android.epi.communication.INetworkUser;
import org.ird.android.epi.communication.ResponseReader;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.communication.elements.ResponseStatus;
import org.ird.android.epi.model.LotteryWon;
import org.ird.android.epi.validation.ValidatorResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

@Deprecated
public class StorekeeperActivity extends Activity implements INetworkUser, OnClickListener, IBaseForm
{
	// Views
	EditText txtId;
	ImageView imgFetch;
	Button btnConsume, btnCancel;
	ImageView imgScan;
	/*
	 * RadioButton radOldId;
	 * RadioButton radNewId;
	 */
	RadioGroup groupRad;
	LotteryWon[] lotteries = null;

	// Views for dynamic lotteries
	LinearLayout vertical;
	ScrollView scrollview;


	// Flag for Network operation
	byte MODE;
	byte MODE_FETCH_LOTTERY = 1;
	byte MODE_SEND_CONSUMPTION = 2;

	private static NetworkProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.storekeeper_layout);

		initControls();
		setListeners();
	}

	private void setListeners()
	{
		// RadioButtons
		/*
		 * if(radNewId!=null&& radOldId!=null)
		 * {
		 * radNewId.setOnCheckedChangeListener(this);
		 * radOldId.setOnCheckedChangeListener(this);
		 * }
		 */

		if (imgScan != null)
			imgScan.setOnClickListener(this);
		// Buttons
		if (btnCancel != null)
			btnCancel.setOnClickListener(this);
		if (btnConsume != null)
			btnConsume.setOnClickListener(this);
		if (imgFetch != null)
			imgFetch.setOnClickListener(this);

	}

	private void initControls()
	{
		// ID
		txtId = (EditText) findViewById(R.id.editTextStkID);

		imgScan = (ImageView) findViewById(R.id.imgStkScan);

		// Buttons
		imgFetch = (ImageView) findViewById(R.id.btnStkFetch);
		btnCancel = (Button) findViewById(R.id.btnStkCancel);
		btnConsume = (Button) findViewById(R.id.btnStkOk);

		// RadionButtons
		/*
		 * radNewId = (RadioButton)findViewById(R.id.radStkNewID);
		 * radOldId = (RadioButton)findViewById(R.id.radStkOldId);
		 */

		scrollview = (ScrollView) findViewById(R.id.scrollViewLotteries);
		vertical = (LinearLayout) scrollview.findViewById(R.id.layoutLottery);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.storekeeper, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Barcode.BARCODE_REQUEST_CODE)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				String str = data.getStringExtra(Barcode.SCAN_RESULT);
				// TODO: do something with the data received
			}
		}
	}

	public void scan(View control)
	{
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
	}

	/*
	 * @Override
	 * public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	 * {
	 * if(buttonView.getId() == radOldId.getId())
	 * {
	 * radNewId.setChecked(!isChecked);
	 * }
	 * else if(buttonView.getId() == radNewId.getId())
	 * {
	 * radOldId.setChecked(!isChecked);
	 * }
	 * }
	 */

	public void onClick(View control)
	{
		if (control.getId() == imgScan.getId())
		{
			scan(control);
		}
		else if (control.getId() == imgFetch.getId())
		{
			if (validateForm())
			{
				MODE = MODE_FETCH_LOTTERY;
				sendToServer(createPayload());
			}
		}
		else if (control.getId() == btnCancel.getId())
		{
			onBackPressed();
		}
		else if (control.getId() == btnConsume.getId())
		{
			if (validateLotteryConsumption() && validateForm())
			{
				MODE = MODE_SEND_CONSUMPTION;
				sendToServer(createPayload());
			}
		}
	}

	private boolean validateForm()
	{
		boolean isValid = true;
		if (txtId.getText() == null || "".equalsIgnoreCase(txtId.getText().toString().trim()))
		{
			isValid = false;
			txtId.setError("ID khali nahee ho sakti");
		}

		// remove the error if validation passed
		if (isValid)
			txtId.setError(null);

		return isValid;
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
			AlertDialog alert = EpiUtils.showDismissableDialog(this, response, "No Response From Server");
			alert.show();
		}

		// Outcome 2: Success message from server. Check ResponseParams
		else if (ResponseStatus.STATUS_SUCCESS.getId().equals(responseCode))
		{
			try
			{
				mapParams = reader.readParams(response);
				if (mapParams != null)
				{
					for (String key : mapParams.keySet())
					{
						if (key.equalsIgnoreCase(RequestElements.LOTTERIES_AVAILABLE))
						{
							JSONArray array = (JSONArray) mapParams.get(key);
							fetchLotteries(array);
							showAvailableLottery();
						}
					}
				}
				else
				// no params are retured if consumed or no lottery is available
				{
					// check mode to determine the response
					if (MODE == MODE_FETCH_LOTTERY)
					{
						if (vertical != null)
							vertical.removeAllViews();
						btnConsume.setEnabled(false);
						EpiUtils.showDismissableDialog(this, "No lottery is available currently.", "Sorry").show();
					}
					else if (MODE == MODE_SEND_CONSUMPTION)
					{
						/*
						 * TODO:
						 * 1.Inform successful lottery consumption
						 * 2.Update lottery list
						 */
						AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setMessage("Lottery succesfully used. Refreshing the lottery list now");
						builder.setTitle("Lottery Used");
						builder.setPositiveButton("Ok", new AlertDialog.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								MODE = MODE_FETCH_LOTTERY;
								sendToServer(createPayload());
								dialog.dismiss();
							}
						});
						builder.show();
					}
					return;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.e(StorekeeperActivity.class.getSimpleName(), "Error while processing response from server");
				EpiUtils.showDismissableDialog(this, getResources().getString(R.string.login_server_response_processing_error), "Error").show();
				return;
			}

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

		// Outcome 3: Failure due to data sent from mobile. ResponseParams likely
		else if (ResponseStatus.STATUS_FAILURE.getId().equals(responseCode))
		{
			try
			{
				mapParams = reader.readParams(response);
				if (mapParams != null && mapParams.size() > 0)
				{
					StringBuilder errors = new StringBuilder();
					errors.append("Following error(s) occured in server:");
					for (String key : mapParams.keySet())
					{
						errors.append("\n");
						errors.append(mapParams.get(key));
					}
					EpiUtils.showDismissableDialog(this, errors.toString(), "Error(s) Occurred").show();
				}
				else
				{
					EpiUtils.showDismissableDialog(this, "Unknown error at server", "Error Occurred").show();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				EpiUtils.showDismissableDialog(this, getResources().getString(R.string.login_server_response_processing_error), "Error").show();
				Log.e(StorekeeperActivity.class.getSimpleName(), "error processing response from server");
			}
		}

		// Outcome 4: Some other error occured in server. Show error code which is sent in the response
		else
		{
			EpiUtils.showDismissableDialog(this, "Unknown Error from server", "Error").show();
		}
	}

	private void showAvailableLottery()
	{
		if (lotteries == null)
			return;
		// check controls are not null
		if (scrollview == null || vertical == null)
			return;

		vertical.removeAllViews();

		LinearLayout layout;
		TextView code;
		TextView amount;
		CheckBox selectBox;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);


		for (int i = 0; i < lotteries.length; i++)
		{
			layout = new LinearLayout(this);
			layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setWeightSum(3);
			layout.setTag(lotteries[i]);

			code = new TextView(this);
			code.setLayoutParams(layoutParams);
			code.setText(lotteries[i].getVerrificationCode());
			layout.addView(code);

			amount = new TextView(this);
			amount.setLayoutParams(layoutParams);
			amount.setText(lotteries[i].getAmount());
			layout.addView(amount);

			selectBox = new CheckBox(this);
			selectBox.setText("");
			selectBox.setLayoutParams(layoutParams);
			selectBox.setSelected(false);
			selectBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					ViewGroup parent = (ViewGroup) buttonView.getParent();
					if (parent != null)
					{
						try
						{
							LotteryWon tag = (LotteryWon) parent.getTag();
							if (tag != null)
								tag.setSelected(isChecked);
						}
						catch (Exception ex)
						{
							Log.e(StorekeeperActivity.class.getSimpleName(), ex.getMessage());
						}
					}
				}
			});
			layout.addView(selectBox);
			vertical.addView(layout);
		}
	}

	@Override
	public void sendToServer(Map params)
	{
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
			address.append(GlobalConstants.REST_STOREKEEPER);
			Log.i(FollowUpActivity.class.getSimpleName(), "Server address:" + address.toString());
			HTTPSender sender = null;

			// decide HTTP method as per the current mode
			if (MODE == MODE_FETCH_LOTTERY)
				sender = new HTTPSender(this, HTTPSender.METHOD_GET, address.toString());
			else if (MODE == MODE_SEND_CONSUMPTION)
				sender = new HTTPSender(this, HTTPSender.METHOD_POST, address.toString());

			// dont try to send if the mode was not valid
			if (sender != null)
				sender.execute(params);
		}
		catch (Exception e)
		{
			Log.e(FollowUpActivity.class.getSimpleName(), "Error occurred while submitting Enrollment to server : \n" + e.getMessage());
		}
	}

	private void fetchLotteries(JSONArray json) throws JSONException
	{
		LotteryWon[] array = new LotteryWon[json.length()];
		LotteryWon temp;
		JSONObject obj;

		try
		{
			for (int i = 0; i < json.length(); i++)
			{
				obj = (JSONObject) json.get(i);
				String code, amount, recordNum;
				code = obj.getString(RequestElements.VERIFICATION_CODE);
				amount = obj.getString(RequestElements.AMOUNT_WON);
				recordNum = obj.getString(RequestElements.VACCINATION_RECORD_NUM);
				temp = new LotteryWon();
				temp.setAmount(amount);
				temp.setSelected(false);
				temp.setVaccinationRecordNum(recordNum);
				temp.setVerrificationCode(code);
				array[i] = temp;
			}
		}
		catch (Exception e)
		{
			Log.e(StorekeeperActivity.class.getSimpleName(), e.getMessage());
			e.printStackTrace();
		}
		this.lotteries = array;
		// Enable the consumed button //TODO: Move this to the showAvailableLottery method
		if (lotteries.length > 0)
			btnConsume.setEnabled(true);
	}

	private boolean validateLotteryConsumption()
	{
		boolean isValid = true;
		int countSelected = 0;
		for (int i = 0; i < this.lotteries.length; i++)
		{
			if (lotteries[i].isSelected())
			{
				countSelected++;
			}
		}
		if (countSelected == 0)
		{
			isValid = false;
			EpiUtils.showDismissableDialog(this, "Please select at least one verification code to consume", "Error").show();
		}
		return isValid;
	}

	@Override
	public List<ValidatorResult> validate()
	{
		return null;
	}

	@Override
	public Map createPayload()
	{
		Map<Object, Object> params = new HashMap<Object, Object>();
		// fill params as per the operation
		if (MODE == MODE_FETCH_LOTTERY)
		{
			params.put(RequestElements.CHILD_ID.toString(), txtId.getText().toString());
		}
		else if (MODE == MODE_SEND_CONSUMPTION)
		{
			params.put(RequestElements.CHILD_ID.toString(), txtId.getText().toString());
			JSONArray lotteriesArray = new JSONArray();
			JSONObject temp;
			for (int i = 0; i < this.lotteries.length; i++)
			{
				if (this.lotteries[i].isSelected())
				{
					temp = new JSONObject();
					try
					{
						temp.put(RequestElements.VERIFICATION_CODE, lotteries[i].getVerrificationCode());
						temp.put(RequestElements.VACCINATION_RECORD_NUM, lotteries[i].getVaccinationRecordNum());
						lotteriesArray.put(temp);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}
			params.put(RequestElements.LOTTERY_CONSUMED.toString(), lotteriesArray);

			// getting UserId from preferences
			// SharedPreferences sharedPref = getSharedPreferences("Application Config", MODE_PRIVATE);
			// Integer userId = SharedPreferencesReader.readUserId(sharedPref);
			// params.put(RequestElements.LG_USERID, userId);

			params.put(RequestElements.LG_USERID, GlobalConstants.USER_PROGRAM_ID);

		}
		return params;
	}

	@Override
	public void handleException(Exception ex)
	{
	}

}
