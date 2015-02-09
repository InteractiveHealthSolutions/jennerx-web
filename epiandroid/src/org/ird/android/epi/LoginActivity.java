package org.ird.android.epi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ord.ird.android.epi.db.DatabaseUtil;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.NetworkProgressDialog;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.common.SecurityUtils;
import org.ird.android.epi.communication.HTTPSender;
import org.ird.android.epi.communication.HTTPSender.UnsupportedHTTPMethodException;
import org.ird.android.epi.communication.INetworkUser;
import org.ird.android.epi.communication.ResponseReader;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.communication.elements.ResponseElements;
import org.ird.android.epi.communication.elements.ResponseStatus;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements android.view.View.OnClickListener, IBaseForm, INetworkUser, IDialogListener, OnSharedPreferenceChangeListener
{
	Button btnLogin;
	Button btnCancel;
	EditText txtUsername;
	EditText txtPassword;
	ImageView imgUsername;
	ImageView imgPassword;

	private String username;
	private String password;
	public static boolean EXIT_REQUEST = false;
	private String TAG_LOGIN = "LoginActivity";

	private StringBuilder _ERROR_STRING = null;

	private NetworkProgressDialog progress;

	private Boolean isLogin = true;

	/**
	 * Used to create a boolean named isFirstTime in order to allow synchronization of application when it is launched for
	 * the first time.
	 */
	private Boolean isFirstTime;
	private Editor editor;
	private SharedPreferences sharedPref;


	/**
	 * SharedPreferences which hold the preferences value.
	 */
	private SharedPreferences defaultPref;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtUsername = (EditText) findViewById(R.id.editTextUserName);
		txtPassword = (EditText) findViewById(R.id.editTextPassword);

		imgUsername = (ImageView) findViewById(R.id.imgViewUsername);
		imgPassword = (ImageView) findViewById(R.id.imgViewPassword);
		imgUsername.setOnClickListener(this);
		imgPassword.setOnClickListener(this);

		hideErrors();

		PreferenceManager.setDefaultValues(this, R.xml.application_preferences, false);
		defaultPref = PreferenceManager.getDefaultSharedPreferences(this);

		defaultPref.registerOnSharedPreferenceChangeListener(LoginActivity.this);
		sharedPref = getSharedPreferences("Application Config", MODE_PRIVATE);

		/**
		 * initializing isFirstTime from shared preference.
		 */
		isFirstTime = sharedPref.getBoolean("isFirstTime", true);
	}

	@Override
	protected void onResume()
	{
		if (EXIT_REQUEST)
		{
			EXIT_REQUEST = false;
			finish();
		}
		setDefaultUsername();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private boolean setDefaultUsername()
	{
		try
		{
			String defaultname = "";
			String defaultPass = "";

			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			defaultname = pref.getString("username_pref", username);
			defaultPass = pref.getString("password_pref", password);

			txtUsername.setText(defaultname);
			txtPassword.setText(defaultPass);

			return true;
		}
		catch (Exception e)
		{
			Log.e(TAG_LOGIN, e.getMessage());
			return false;
		}
	}

	private void saveDefaultPref()
	{
		// Store username for next login
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean user = false;
		boolean pass = false;
		if (pref.getBoolean("lastuser_pref", false))
		{
			getFields();
			Editor edit = pref.edit();
			edit.putString("username_pref", username);
			user = edit.commit();
		}

		if (pref.getBoolean("lastpass_pref", false))
		{
			getFields();
			Editor edit = pref.edit();
			edit.putString("password_pref", password);
			pass = edit.commit();
		}

		Log.d(LoginActivity.class.getSimpleName(), user + " " + pass);

	}

	private void hideErrors()
	{
		imgUsername.setVisibility(View.GONE);
		imgPassword.setVisibility(View.GONE);
		imgUsername.setTag("");
		imgPassword.setTag("");

	}

	@Override
	public void onClick(View v)
	{
		// Login button clicked
		if (v.getId() == btnLogin.getId())
		{
			doSyncIfFirstTime();
			saveDefaultPref();

			isLogin = false;

			if (validate().size() == 0)
			{
				try
				{
					Map params = createPayload();
					if (params.size() != 0)
					{
						sendToServer(params);
					}
					else
					{
						handleException(new Exception("No Parameters in payload for sending to server.Can not proceed."));
						isLogin = false;
					}
				}

				catch (Exception e)
				{
					handleException(e);
				}
			}
			else
			{
				isLogin = false;
				if (this._ERROR_STRING != null)
				{
					EpiUtils.showAlert(this, getResources().getString(R.string.login_error_credentials_format_incorrect) + ":" + this._ERROR_STRING.toString(), "Errors", this).show();
					this._ERROR_STRING = null; // clean up errors' object for
												// re-use
				}
				else
				{
					EpiUtils.showAlert(this, getResources().getString(R.string.login_error_credentials_format_incorrect), "Errors", this).show();
				}
			}
		}
		else if (v.getId() == btnCancel.getId())
		{
			finish();
		}
		else if (v.getId() == R.id.imgViewPassword || v.getId() == R.id.imgViewUsername)
		{
			EpiUtils.showAlert(this, (String) v.getTag(), "Error", this).show();
		}

	}

	@Override
	public void sendToServer(Map params)
	{
		try
		{
			if (progress == null)
			{
				progress = new NetworkProgressDialog(this);
			}
			progress.setMessage(getResources().getString(R.string.alert_wait));
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					progress.run();
				}
			});

			HTTPSender sender = new HTTPSender(this, HTTPSender.METHOD_POST, new EpiUtils().getServerAddress(this) + GlobalConstants.REST_LOGIN);
			sender.execute(params);
		}
		catch (UnsupportedHTTPMethodException e)
		{
			handleException(e);
		}
	}

	private boolean getFields()
	{
		try
		{
			username = txtUsername.getText().toString();
			password = txtPassword.getText().toString();
			return true;
		}
		catch (Exception e)
		{
			Log.e(TAG_LOGIN, e.getMessage());
			return false;
		}
	}

	@Override
	public List<ValidatorResult> validate()
	{
		List<ValidatorResult> list = new ArrayList<ValidatorResult>();
		getFields();
		ValidatorUtil validator = new ValidatorUtil(this);
		ValidatorResult result = null;
		boolean isValid = true;
		StringBuilder errors;

		result = validator.validateUserName(username);
		errors = new StringBuilder();
		if (!result.isValid())
		{
			if (isValid)
			{
				list.add(result);
				isValid = false;
				errors.append("\n" + result.getMessage());
			}
		}

		result = validator.validatePassword(password);
		if (!result.isValid())
		{
			if (isValid)
			{
				list.add(result);
				isValid = false;
				errors.append("\n" + result.getMessage());
			}
		}
		this._ERROR_STRING = errors;
		return list;
	}

	@Override
	public Map createPayload()
	{
		// prepare the credentials to be sent to Servre
		Map params = new HashMap<String, String>();
		try
		{
			// username
			params.put(RequestElements.LG_USERNAME, username);

			// encrypted password
			String encryptedPassword = SecurityUtils.encrypt(password, username);
			params.put(RequestElements.LG_PASSWORD, encryptedPassword);

			// application version
			String version;
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
			params.put(RequestElements.APP_VER, version);

			// user type
			String selectedRole = getResources().getString(R.string.vaccinator);
			GlobalConstants.USER_ROLE = selectedRole;

			params.put(RequestElements.USER_TYPE, RequestElements.USER_TYPE_VACCINATOR);


		}
		catch (NameNotFoundException e)
		{
			handleException(e);
		}
		catch (Exception e)
		{
			handleException(e);
		}

		return params;
	}

	@Override
	public void responseRecieved(String response)
	{
		// Close the dialog open
		if (progress != null)
		{
			progress.dismiss();
		}
		ResponseReader reader = new ResponseReader(this);

		// Outcome 1: could not connect to server or exception occured when
		// connecting to it
		if (response == null)
		{
			sharedPref = getSharedPreferences("Application Config", MODE_PRIVATE);
			editor = sharedPref.edit();
			editor.putBoolean("isFirstTime", true);
			editor.commit();

			AlertDialog alert = EpiUtils.showAlert(this, response, "No Response From Server", this);
			alert.show();
		}

		// Outcome 2: success message from server
		else if (ResponseStatus.STATUS_SUCCESS == reader.readStatus(response))
		{
			try
			{
				Map<String, Object> mapParams = reader.readParams(response);
				for (String key : mapParams.keySet())
				{
					if (ResponseElements.LG_USERID.toString().equalsIgnoreCase(key))
					{
						GlobalConstants.USER_PROGRAM_ID = (Integer) (mapParams.get(key));

						// User credentials are correct, so make user login

						isLogin = true;
					}

					else if (ResponseElements.CENTRE_ID.toString().equalsIgnoreCase(key))
					{
						GlobalConstants.VACCINATION_CENTRE_ID = (Integer) (mapParams.get(key));
					}
					else if ("METADATA".equalsIgnoreCase(key))
					{
						try
						{
							DatabaseUtil util = new DatabaseUtil(LoginActivity.this);
							util.buildDatabase(true);
							reader.saveMetadata((JSONObject) mapParams.get(key));
							Toast.makeText(this, R.string.appSynced, Toast.LENGTH_SHORT).show();
							// Meta-data has been downloaded, so update the flag "isFirstTime" to false and save it
							updateFirstTimeFlag();
						}
						catch (Throwable e1)
						{
							Toast.makeText(this, e1.getMessage(), Toast.LENGTH_LONG).show();
							e1.printStackTrace();
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Log.e(TAG_LOGIN, "Error while processing response from server");
				EpiUtils.showAlert(this, getResources().getString(R.string.login_server_response_processing_error), "Error", this).show();
				isLogin = false;
				return;
			}
			// Proceed as per the selected role
			Intent intent = null;
			if (GlobalConstants.USER_ROLE.equalsIgnoreCase(getResources().getString(R.string.vaccinator)) && isLogin)
			{
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
		}
		// Outcome 3: Some other error occurred in server. Show error code which
		// is sent in the response
		else
		{
			Toast.makeText(this, getString(R.string.error_server_side) + response, Toast.LENGTH_LONG).show();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent intent;
		switch (item.getItemId())
		{
		case R.id.action_settings:
			intent = new Intent(this, EpiPreferences.class);
			startActivity(intent);
			break;

		case R.id.action_synch:
			doSynch();
			break;

		case R.id.action_about:
			intent = new Intent(this, AboutAtivity.class);
			startActivity(intent);
			break;

		}
		return false;
	}

	@Override
	public void onDialogPositiveClick(Map... o)
	{

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
	public void handleException(Exception ex)
	{
		isLogin = false;
		ex.printStackTrace();
		EpiUtils.showAlert(this, getResources().getString(R.string.login_error_connecting_server) + "\n" + ex.getLocalizedMessage(), "Error", this).show();
		Log.e(TAG_LOGIN, ex.getMessage());

	}


	/**
	 * It downloads Meta-data if app is being synchrozied first time
	 * 
	 */
	public void doSyncIfFirstTime()
	{

		if (isFirstTime)
		{
			doSynch();
		}

	}

	public void doSynch()
	{
		isLogin = false;
		if (progress == null)
		{
			progress = new NetworkProgressDialog(this);
		}
		progress.setMessage(getResources().getString(R.string.alert_wait));
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				progress.run();
			}
		});

		HTTPSender sender;

		try
		{
			StringBuilder address = new StringBuilder();
			address.append(new EpiUtils().getServerAddress(this));
			address.append(GlobalConstants.REST_METADATA);
			Log.i(LoginActivity.class.getSimpleName(), "Server address:" + address.toString());
			sender = new HTTPSender(this, HTTPSender.METHOD_GET, address.toString());
			sender.execute();
		}
		catch (UnsupportedHTTPMethodException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if (!isFirstTime)
		{
			/**
			 * Our logic to trigger on "isFirstTime" depends on following scenarios
			 * 1) If user server URL is changed
			 * 2) If app is running after fresh installation (It is handled on "onClick" event of Login button)
			 */			

			Map<String, Object> map = (Map<String, Object>) sharedPreferences.getAll();
			
			// if "Complete server URL" is checked then ignore rest of the changes

			if (EpiUtils.objectToBool(map.get("use_tokenized_url")) == true)
			{
				// if new change belongs to fully_qualified_url then
				// turn on the "isFirstFlag"

				if (key.equalsIgnoreCase("fully_qualifified_url") == true)
				{
					isFirstTime = true;
				}

				else if (key.equals("use_tokenized_url") == true)
				{
					isFirstTime = true;
				}		
			}

			else
			{
				if (key.equalsIgnoreCase("appname_pref") == true)
				{
					isFirstTime = true;
				}

				else if (key.equalsIgnoreCase("ip_pref") == true)
				{
					isFirstTime = true;
				}

				else if (key.equalsIgnoreCase("port_pref") == true)
				{
					isFirstTime = true;
				}			
			}
		}
	}

	/**
	 * It performs two functions:
	 * (1) It changes value of isFirstTime value to false
	 * (2) It saves isFirstTime(false) into Shared-Preference
	 */
	private void updateFirstTimeFlag()
	{

		if (isFirstTime)
		{
			editor = sharedPref.edit();
			editor.putBoolean("isFirstTime", false);
			editor.commit();
			isFirstTime = false;
		}
	}

}
