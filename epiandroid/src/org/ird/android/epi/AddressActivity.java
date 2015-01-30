package org.ird.android.epi;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.dal.LocationService;
import org.ird.android.epi.model.Location;
import org.ird.android.epi.validation.ValidatorResult;
import org.ird.android.epi.validation.ValidatorUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddressActivity extends Activity implements OnItemSelectedListener
{
	public static final String ISVALID = "isvalid";
	public static final String ADDRESS_HOUSE = "house";
	public static final String ADDRESS_STREET = "street";
	public static final String ADDRESS_SECTOR = "sector";
	public static final String ADDRESS_COLONY = "colony";
	public static final String ADDRESS_LANDMARK = "landmark";
	public static final String ADDRESS_TOWN = "town";
	public static final String ADDRESS_UC = "uc";
	public static final String ADDRESS_CITY = "city";

	// Fields to set the form
	private String _house;
	private String _street;
	private String _sector;
	private String _colony;
	private String _landmark;
	private String _town;
	private String _uc = null;
	private String _city;

	// controls used in the form
	EditText edtTxtHouse;
	EditText edtTxtStreet;
	EditText edtTxtSector;
	EditText edtTxtLandmark;
	EditText edtTxtColony;

	Spinner spCity;
	Spinner spTown;
	Spinner spUC;

	private ValidatorUtil validator;
	private ValidatorResult result;
	private ArrayAdapter<Location> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_layout);
		edtTxtHouse = (EditText) findViewById(R.id.editTextHouseNo);
		edtTxtStreet = (EditText) findViewById(R.id.editTextStreet);
		edtTxtSector = (EditText) findViewById(R.id.editTextSector);
		edtTxtColony = (EditText) findViewById(R.id.editTextColony);
		edtTxtLandmark = (EditText) findViewById(R.id.editTextLandmark);

		spTown = (Spinner) findViewById(R.id.spinnerTown);
		spUC = (Spinner) findViewById(R.id.spinnerUC);
		spCity = (Spinner) findViewById(R.id.spinnerCity);

		// TODO: add null pointer check


		try
		{
			arrayAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, LocationService.getAllTown(this));
			spTown.setAdapter(arrayAdapter);

			arrayAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, LocationService.getAllCity(this));
			spCity.setAdapter(arrayAdapter);

			if (LocationService.getAllTown(this).size() < 1 || LocationService.getAllCity(this).size() < 1)
			{
				EpiUtils.showDismissableDialog(this, "Locations not found, you need to re-sync your application from the Login Screen.", "Error").show();
				return;
			}


			spCity.setOnItemSelectedListener(this);
			spTown.setOnItemSelectedListener(this);


			validator = new ValidatorUtil(this);

			fillFields(getIntent().getExtras());
		}
		catch (SQLException e)
		{
			EpiUtils.showDismissableDialog(this, "Locations not found, you need to re-sync your application from the Login Screen.", "Error").show();
			return;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address, menu);
		return true;
	}

	private void fillFields(Bundle data)
	{
		_house = data.getString(ADDRESS_HOUSE);
		_street = data.getString(ADDRESS_STREET);
		_sector = data.getString(ADDRESS_SECTOR);
		_colony = data.getString(ADDRESS_COLONY);
		_landmark = data.getString(ADDRESS_LANDMARK);
		_town = data.getString(ADDRESS_TOWN);
		_uc = data.getString(ADDRESS_UC);
		_city = data.getString(ADDRESS_CITY);

		edtTxtHouse.setText(_house);
		edtTxtStreet.setText(_street);
		edtTxtLandmark.setText(_landmark);
		edtTxtSector.setText(_sector);
		edtTxtColony.setText(_colony);

		Location location = null;
		if (_town != null)
		{
			location = LocationService.getLocationById(this, Integer.valueOf(_town));
			if (location != null)
				spTown.setSelection(EpiUtils.getIndexByValue(spTown, location.toString()));
		}

		// if(_town!=null)
		// {
		// setUC(_town);
		// spUC.setSelection(EpiUtils.getIndexByValue(spUC, _uc));
		// }
		if (_city != null)
		{
			location = LocationService.getLocationById(this, Integer.valueOf(_city));
			if (location != null)
				spCity.setSelection(EpiUtils.getIndexByValue(spCity, _city));
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View target, int position, long rowId)
	{
		// handling City click
		if (parent.getId() == spCity.getId())
		{
			arrayAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, LocationService.getAllChildren(this, (Location) spCity.getSelectedItem()));
			spTown.setAdapter(arrayAdapter);
			// handle last selected Town

			if (_city != null)
			{
				Location location;
				if (_town == null)
					location = LocationService.getLocationById(this, Integer.valueOf(_city));
				else
					location = LocationService.getLocationById(this, Integer.valueOf(_town));

				spTown.setSelection(EpiUtils.getIndexByValue(spTown, location.toString()));

			}
		}

		// handling Town click
		if (parent.getId() == spTown.getId())
		{
			arrayAdapter = new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1, LocationService.getAllChildren(this, (Location) spTown.getSelectedItem()));
			spUC.setAdapter(arrayAdapter);
			// handle last selected UC

			if (_town != null)
			{
				Location location;
				if (_uc == null)
				{
					location = LocationService.getLocationById(this, Integer.valueOf(_town));
				}

				else
				{
					location = LocationService.getLocationById(this, Integer.valueOf(_uc));
				}

				spUC.setSelection(EpiUtils.getIndexByValue(spUC, location.toString()));
			}
		}
	}

	private void setUC(String selectedTown)
	{
		// //dynamically locate the relevant array for the UC
		// //1) remove the spaces from the name (2) remove word "town" (3)
		// append word "array_"
		// String ucName = "array_"
		// + selectedTown.toLowerCase().replaceAll("[ +]", "")
		// .replaceAll("town", "");
		//
		// //find the array's id by providing the full name for the array
		// int idOfArray = getResources().getIdentifier(
		// "org.ird.android.epi:array/" + ucName, null, null);
		//
		// //load the array
		// String[] ucArray = getResources().getStringArray(idOfArray);
		//
		// //populate the array by using ArrayAdapter
		// arrayAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, ucArray);
		// spUC.setAdapter(arrayAdapter);
	}

	public void onSubmit(View control)
	{
		boolean validated = validateAddressDetails();
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

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{

	}

	@Override
	public void finish()
	{
		super.finish();
	}

	private void sendIntent(boolean isValid)
	{
		if (!isValid)
		{
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
		else
		{
			Intent intent = new Intent();
			intent.putExtra(ISVALID, isValid);
			intent.putExtra(ADDRESS_HOUSE, edtTxtHouse != null ? edtTxtHouse.getText().toString() : null);
			intent.putExtra(ADDRESS_STREET, edtTxtStreet != null ? edtTxtStreet.getText().toString() : null);
			intent.putExtra(ADDRESS_SECTOR, edtTxtSector != null ? edtTxtSector.getText().toString() : null);
			intent.putExtra(ADDRESS_COLONY, edtTxtColony != null ? edtTxtColony.getText().toString() : null);
			intent.putExtra(ADDRESS_LANDMARK, edtTxtLandmark != null ? edtTxtLandmark.getText().toString() : null);
			intent.putExtra(ADDRESS_TOWN, spTown != null ? ((Location) spTown.getSelectedItem()).getId() : null);
			intent.putExtra(ADDRESS_UC, spUC != null ? ((Location) spUC.getSelectedItem()).getId() : null);
			intent.putExtra(ADDRESS_CITY, spCity != null ? ((Location) spCity.getSelectedItem()).getId() : null);

			setResult(RESULT_OK, intent);
			super.finish();
		}



	}

	private boolean validateAddressDetails()
	{
		boolean isAllValid = true;


		if (spTown.getSelectedItem() == null)
		{
			isAllValid = false;
			result = new ValidatorResult();
			String mesg = getResources().getString(R.string.ru_validation_error_town_empty);
			result.setMessage(mesg);
			result.setIsValid(false);
			result.setDismissable(false);

		}
		if (spCity.getSelectedItem() == null)
		{
			isAllValid = false;
			result = new ValidatorResult();
			String mesg = getResources().getString(R.string.ru_validation_error_town_empty);
			result.setMessage(mesg);
			result.setIsValid(false);
			result.setDismissable(false);
		}
		return isAllValid;
	}

	public void cancel(View button)
	{
		setResult(RESULT_CANCELED);
		super.finish();
	}
}
