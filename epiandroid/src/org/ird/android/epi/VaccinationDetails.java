package org.ird.android.epi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ord.ird.android.epi.db.CentreDBHelper;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.VaccineDatePicker;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.VaccineName;
import org.ird.android.epi.validation.VaccinationValidator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class VaccinationDetails extends Activity
{

	EditText edtTextDate;
	Spinner spCentre;
	Spinner spStatus;
	CheckBox chkBxMissing;
	TextView tvCentre;
	TextView tvStatus;
	TextView tvDate;
	TextView tvVacTitle;

	public static final String CENTRE_KNOWN = "centreknw";
	public static final String VACCINATION_DATE = "vcdate";
	public static final String CENTRE_NAME = "centrname";

	private Integer position;

	private boolean isMissing = false;

	private Bundle bundle;

	private Integer pos;
	private ArrayList<VaccineScheduleRow> listRow;
	private Child child;

	private CentreDBHelper centreDbHelper;
	private List<Centre> centres;

	final boolean[] answer = new boolean[1];

	private Context context;

	// private ArrayList<VaccinationStatus> statusList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vaccine_schdule_details_layout);
		this.context = getApplicationContext();

		bundle = getIntent().getExtras();
		pos = bundle.getInt("itemPos");
		listRow = (ArrayList<VaccineScheduleRow>) bundle.getSerializable("listRow");
		child = (Child) bundle.getSerializable("child");

		edtTextDate = (EditText) findViewById(R.id.edtTxtDateGivenn);
		spCentre = (Spinner) findViewById(R.id.spCentreGiven);
		spStatus = (Spinner) findViewById(R.id.spVacStatus);
		chkBxMissing = (CheckBox) findViewById(R.id.chkBoxDateNotKnow);
		tvStatus = (TextView) findViewById(R.id.tvVacStatus);
		tvCentre = (TextView) findViewById(R.id.tvTextCentre);
		tvDate = (TextView) findViewById(R.id.tvDateGivenn);
		tvVacTitle = (TextView) findViewById(R.id.tvVacTitle);

		tvVacTitle.setText(VaccineName.valueOf(listRow.get(pos).getVaccineName()).getVaccine());

		/**
		 * use this if a customized list is required for not applicable vaccine.
		 */

		/*
		 * if (!listRow.get(pos).isApplicable()) { statusList = new
		 * ArrayList<VaccinationStatus>();
		 * statusList.add(VaccinationStatus.VACCINATED);
		 * statusList.add(VaccinationStatus.RETRO);
		 * statusList.add(VaccinationStatus.RETRO_DATE_MISSING);
		 * ArrayAdapter<VaccinationStatus> adapterStatus = new
		 * ArrayAdapter<VaccinationStatus>(this,
		 * android.R.layout.simple_list_item_1, statusList);
		 * spStatus.setAdapter(adapterStatus); } else {
		 */
		ArrayAdapter<VaccinationStatus> adapterStatus = new ArrayAdapter<VaccinationStatus>(this, android.R.layout.simple_list_item_1, VaccinationStatus.values());
		spStatus.setAdapter(adapterStatus);
		// }

		try
		{
			centreDbHelper = new CentreDBHelper(this);
			centres = centreDbHelper.getCentres();

			ArrayAdapter<Centre> adapter = new ArrayAdapter<Centre>(this, android.R.layout.simple_list_item_1, centreDbHelper.getCentres());
			spCentre.setAdapter(adapter);

			if (centreDbHelper.getCentres().size() < 1)
			{
				EpiUtils.showDismissableDialog(this, "Locations not found, you need to re-sync your application from the Login Screen.", "Error").show();
				return;
			}
		}
		catch (SQLException e)
		{
			EpiUtils.showDismissableDialog(this, "Locations not found, you need to re-sync your application from the Login Screen.", "Error").show();
			return;
		}

		/**
		 * filling the fields on edit
		 */

		init();

		/**
		 * Listener to change the layout of the form on selection dynamically.
		 */
		spStatus.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{

				if (parent.getItemAtPosition(position).equals(VaccinationStatus.VACCINATED))
				{

					spCentre.setVisibility(View.GONE);
					tvCentre.setVisibility(View.GONE);
					chkBxMissing.setVisibility(View.GONE);
					edtTextDate.setVisibility(View.GONE);
					tvDate.setVisibility(View.GONE);
					chkBxMissing.setChecked(false);
					spCentre.setSelected(false);
					edtTextDate.setText(DateTimeUtils.DateToString(new Date(), null));

				}
				else if (parent.getItemAtPosition(position).equals(VaccinationStatus.RETRO))
				{

					spCentre.setVisibility(View.VISIBLE);
					tvCentre.setVisibility(View.VISIBLE);
					edtTextDate.setVisibility(View.VISIBLE);
					tvDate.setVisibility(View.VISIBLE);
					chkBxMissing.setVisibility(View.GONE);
					chkBxMissing.setChecked(false);
					if (listRow.get(position).getVaccinationDate() != null || "".equals(listRow.get(position).getVaccinationDate()))
						edtTextDate.setText(DateTimeUtils.DateToString(new Date(), null));

				}
				else if (parent.getItemAtPosition(position).equals(VaccinationStatus.RETRO_DATE_MISSING))
				{
					spCentre.setVisibility(View.VISIBLE);
					tvCentre.setVisibility(View.VISIBLE);
					chkBxMissing.setVisibility(View.GONE);
					chkBxMissing.setChecked(true);
					edtTextDate.setVisibility(View.GONE);
					tvDate.setVisibility(View.GONE);
					edtTextDate.setText(null);

				}
				else if (parent.getItemAtPosition(position).equals(VaccinationStatus.SCHEDULED))
				{

					spCentre.setVisibility(View.GONE);
					tvCentre.setVisibility(View.GONE);
					chkBxMissing.setVisibility(View.GONE);
					chkBxMissing.setChecked(false);
					tvDate.setVisibility(View.VISIBLE);
					edtTextDate.setVisibility(View.VISIBLE);
					// Checking if the due date is not bigger than the current
					// date then set new Date in edtTextDate
					if (listRow.get(pos).getDueDate().getTime() >= new Date().getTime())
						edtTextDate.setText(DateTimeUtils.DateToString(listRow.get(pos).getDueDate(), DateTimeUtils.DATE_FORMAT_SHORT));
					else
						edtTextDate.setText(DateTimeUtils.DateToString(new Date(), DateTimeUtils.DATE_FORMAT_SHORT));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				spStatus.setSelection(VaccinationStatus.VACCINATED.ordinal());
			}

		});

		if (edtTextDate != null)
			edtTextDate.setOnClickListener(new VaccineDatePicker(this, edtTextDate, new Date(), null, child.getDateOfBirth()));

		if (chkBxMissing != null)
			chkBxMissing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (buttonView.isChecked())
					{
						isMissing = true;
						edtTextDate.setText("");
						edtTextDate.setEnabled(false);
					}
					else
					{
						isMissing = false;
						edtTextDate.setEnabled(true);
					}
				}
			});
	}

	private boolean validate()
	{
		// for retro and retro no date only
		if (!chkBxMissing.isChecked())
		{
			if (edtTextDate.getText() == null || "".equals(edtTextDate.getText().toString().trim()))
			{
				EpiUtils.showDismissableDialog(this, "Can't proceed witout a date, please select a date first.", "Error").show();
				return false;
			}

		}

		if (spCentre.getSelectedItem() == null)
		{
			EpiUtils.showDismissableDialog(this, "No Centre found. Please re-sync your application from login screen.", "Error").show();
			return false;
		}

		if ("".equals(spCentre.getSelectedItem().toString()))
		{
			EpiUtils.showDismissableDialog(this, "Center must be selected.", "Error").show();
			return false;
		}

		if ("".equals(spStatus.getSelectedItem().toString()))
		{
			EpiUtils.showDismissableDialog(this, "Status must be selected.", "Error").show();
			return false;
		}

		return true;
	}

	public void click(View v)
	{
		if (v.getId() == R.id.btnSaveVaccineDetails)
		{
			if (validate())
			{
				finish();
			}
			else
			{
				// TODO:show error message here
			}
		}
		else if (v.getId() == R.id.btnCancelVaccineDetails)
		{
			onBackPressed();
		}
	}

	@Override
	public void finish()
	{

		Boolean isValid = validateRows(listRow.get(pos), listRow, child);

		if (isValid)
			isValid = updateFields(listRow.get(pos));

		if (isValid)
		{
			Intent intent = new Intent();
			intent.putExtra("listRow", listRow);
			intent.putExtra("itemPos", pos);
			setResult(RESULT_OK, intent);
			super.finish();
		}
	}

	public boolean validateRows(VaccineScheduleRow row, ArrayList<VaccineScheduleRow> rows, Child child)
	{
		/**
		 * assigning values to rows based on the selection of spinner
		 */
		if (spStatus.getSelectedItem().equals(VaccinationStatus.RETRO_DATE_MISSING))
		{
			return true;
		}
		else
		{
			boolean birthdateValidated = false;
			boolean previousVaccineValidated = false;
			Date dt = DateTimeUtils.StringToDate(edtTextDate.getText().toString(), null);
		
			// TODO: validate the date here:

			birthdateValidated = VaccinationValidator.checkBirthdateGap(dt, rows, row, child.getDateOfBirth(), context);
			previousVaccineValidated = VaccinationValidator.checkPreviousVaccineGap(dt, row, rows, context);
			if (!chkBxMissing.isChecked())
			{

				if (dt == null)
				{
					EpiUtils.showDismissableDialog(this, "Can't proceed witout a date, please select a date first.", "Error").show();
					return false;
				}

				// TODO: update child record first
				if (child.getDateOfBirth() != null)
				{
					if (dt.getTime() < child.getDateOfBirth().getTime())
					{
						EpiUtils.showDismissableDialog(this, "Date cannot be less than the birthdate.", "Error").show();
						return false;
					}
				}
				
				// Test validation results
				if (!birthdateValidated)
				{
					EpiUtils.showDismissableDialog(this, "Incorrect age for this vaccine", "Error").show();
					return false;

				}
				/*
				 * if (!row.isApplicable()) {
				 * if (spStatus.getSelectedItem().equals(VaccinationStatus.SCHEDULED) || spStatus.getSelectedItem().equals(VaccinationStatus.VACCINATED)) {
				 * EpiUtils.showDismissableDialog(this, "Please select Retro or Retro date missing in order to continue.", "Error").show();
				 * return false;
				 * }
				 * 
				 * }
				 */

				if (!previousVaccineValidated)
				{
					EpiUtils.showYesNoDialog(this, "Insufficient previous vaccine gap. Do you want to continue?", "Alert", "Yes", "No", new IDialogListener()
					{

						@Override
						public void onDialogPositiveClick(Map... o)
						{
							answer[0] = true;
						}

						@Override
						public void onDialogNeutralClick(Map... o)
						{
							// TODO Auto-generated method stub

						}

						@Override
						public void onDialogNegativeClick(Map... o)
						{
							answer[0] = false;

						}
					}).show();
					return answer[0];

				}

			}

		}

		return true;

	}

	@Override
	public void onBackPressed()
	{
		Intent intent = new Intent();
		intent.putExtra("listRow", listRow);
		intent.putExtra("itemPos", pos);
		setResult(RESULT_CANCELED, intent);
		super.finish();
	}

	public void init()
	{
		/**
		 * this baby is going to handle the initialization process of a dirty
		 * row.
		 */

		// TODO: Commented out section in this part is to be used for the customization of the applicable state.
		if (listRow.get(pos).getStatus() != null)
		{
			if (listRow.get(pos).getStatus().equals(VaccinationStatus.VACCINATED.name()))
			{
				// if (listRow.get(pos).isApplicable())
				spStatus.setSelection(VaccinationStatus.VACCINATED.ordinal());
				/*
				 * else
				 * spStatus.setSelection(statusList.indexOf(VaccinationStatus
				 * .VACCINATED));
				 */

				edtTextDate.setText(DateTimeUtils.DateToString(new Date(), null));

			}
			else if (listRow.get(pos).getStatus().equals(VaccinationStatus.SCHEDULED.name()))
			{

				spStatus.setSelection(VaccinationStatus.SCHEDULED.ordinal());
				edtTextDate.setText(DateTimeUtils.DateToString(listRow.get(pos).getDueDate(), null));

			}
			else if (listRow.get(pos).getStatus().equals(VaccinationStatus.RETRO.name()))
			{

				// if (listRow.get(pos).isApplicable())
				spStatus.setSelection(VaccinationStatus.RETRO.ordinal());
				/*
				 * else
				 * spStatus.setSelection(statusList.indexOf(VaccinationStatus
				 * .RETRO));
				 */
				edtTextDate.setText(DateTimeUtils.DateToString(listRow.get(pos).getVaccinationDate(), null));

				for (Centre centre : centres)
				{
					if (centre.getName().contains(listRow.get(pos).getCenter()))
					{
						spCentre.setSelection(centres.indexOf(centre));
						return;
					}

				}

			}
			else if (listRow.get(pos).getStatus().equals(VaccinationStatus.RETRO_DATE_MISSING.name()))
			{

				// if (listRow.get(pos).isApplicable())
				spStatus.setSelection(VaccinationStatus.RETRO_DATE_MISSING.ordinal());
				/*
				 * else
				 * spStatus.setSelection(statusList.indexOf(VaccinationStatus
				 * .RETRO_DATE_MISSING));
				 */
				edtTextDate.setText(null);

				for (Centre centre : centres)
				{
					if (centre.getName().contains(listRow.get(pos).getCenter()))
					{
						spCentre.setSelection(centres.indexOf(centre));
						return;
					}

				}

			}
		}

	}

	public boolean updateFields(VaccineScheduleRow row)
	{

		if (spStatus.getSelectedItem().equals(VaccinationStatus.RETRO_DATE_MISSING))
		{
			row.setStatus(VaccinationStatus.RETRO_DATE_MISSING.name());
			row.setVaccinationDate(null);
			row.setCenter(spCentre.getSelectedItem().toString());
			row.setGiven(true);
			row.setSelected(true);
			row.setApplicable(true);
		}
		else
		{
			Date dt = DateTimeUtils.StringToDate(edtTextDate.getText().toString(), null);
			Date today = DateTimeUtils.getTodaysDateWithoutTime();
			if (spStatus.getSelectedItem().equals(VaccinationStatus.SCHEDULED))
			{
				if (dt.getTime() > today.getTime())
				{

					// Making sure that Vaccination is not being scheduled on a
					// SundayT
					Calendar c = Calendar.getInstance();
					c.setTime(dt);
					int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

					if (dayOfWeek == 1)
					{
						EpiUtils.showDismissableDialog(this, "Cannot schedule vaccination on Sunday.", "Error").show();
						return false;
					}

					else
					{
						row.setDueDate(dt);
						row.setStatus(VaccinationStatus.SCHEDULED.name());
						row.setCenter(null);
						row.setGiven(false);
						row.setApplicable(true);
					}
				}

				else
				{
					EpiUtils.showDismissableDialog(this, "Date must be greater than the current date for the status to be set as Scheduled", "Error").show();
					return false;

				}

			}

			else if (spStatus.getSelectedItem().equals(VaccinationStatus.VACCINATED))
			{
				if (dt.getTime() == today.getTime())
				{
					row.setStatus(VaccinationStatus.VACCINATED.name());
					row.setVaccinationDate(dt);
					row.setCenter(null);
					row.setGiven(true);
					row.setApplicable(true);
				}
				else
				{
					EpiUtils.showDismissableDialog(this, "Date must be same as the current date for the status to be set as Vaccinated", "Error").show();
					return false;

				}

			}
			else if (spStatus.getSelectedItem().equals(VaccinationStatus.RETRO))
			{

				/* If today's date then past then this a RETRO vaccine */
				if (dt.getTime() < today.getTime())
				{
					row.setStatus(VaccinationStatus.RETRO.name());
					row.setVaccinationDate(dt);
					row.setCenter(spCentre.getSelectedItem().toString());
					row.setGiven(true);
					row.setApplicable(true);
				}
				else
				{
					EpiUtils.showDismissableDialog(this, "Date must be less than the current date for the status to be set as retro", "Error").show();
					return false;

				}

			}

			row.setSelected(true);

		}
		return true;
	}

}
