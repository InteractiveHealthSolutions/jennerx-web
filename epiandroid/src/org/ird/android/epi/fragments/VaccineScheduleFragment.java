package org.ird.android.epi.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ord.ird.android.epi.db.CentreDBHelper;

import org.ird.android.epi.BiodataActivity;
import org.ird.android.epi.R;
import org.ird.android.epi.VaccinationDetails;
import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.alert.VaccineScheduleApdapter;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccination;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineHelper;
import org.ird.android.epi.validation.VaccinationValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Saad
 *
 */
/**
 * @author Saad
 *
 */
public class VaccineScheduleFragment extends ListFragment implements
		OnItemLongClickListener
{

	Vaccine[] vaccines = null;
	Child child;
	Vaccination[] vaccinations = null;
	ArrayList<VaccineScheduleRow> listRows = null;
	ArrayList<VaccineScheduleRow> listRowsDuplicate = null;
	VaccineScheduleApdapter vaccinesAdapter = null;

	/**
	 * to allow list to be properly updated when start from enrollment or
	 * follow-up activity.
	 */
	public Boolean isListUpdated = true;

	private CentreDBHelper centreDbHelper;
	private List<Centre> centres;

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		updateSchedule();

		getListView().setOnItemLongClickListener(this);
		centreDbHelper = new CentreDBHelper(getActivity());
		centres = centreDbHelper.getCentres();
	}

	/*
	 * Had to Override this method due to addition of refresh button in the
	 * default layout of ListFragment (non-Javadoc)
	 * 
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		inflater.getContext().getSystemService(
				getActivity().LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.vaccination_main_layout,
				container);
		return view;
	}

	public void setChild(Child ch)
	{
		this.child = ch;
	}

	public ArrayList<VaccineScheduleRow> updateSchedule()
	{
		Boolean previousVaccineValidated = false;
		/**
		 * isListUpdated : tricky little bugger .. set as false to begin with,
		 * it allows the listrows to be filled once. Used to allow list to be
		 * updated once again when vaccinations are received from follow up.
		 */

		if (isListUpdated)
			listRows = generateDefaultSchedule();

		/**
		 * Validate every row before being passed on to the adapter. Pre-req
		 * check to be presice.
		 */
		for (VaccineScheduleRow row : listRows)
		{
			validationsBeforeActivity(row, listRows);
			validateLateGap(row);

		}

		/**
		 * Assigning default dates to each row if their status is not set as
		 * selected. status is set as selected when a row is clicked and is
		 * updated successfully.
		 */

		if (child != null)
		{
			for (VaccineScheduleRow row : listRows)
			{
				if (!row.isSelected())
				{
					Date defaultDate = VaccineHelper.getDueDate(child != null ? child.getDateOfBirth() : null, row, listRows);

					row.setDueDate(defaultDate);
				}
			}
		}

		if (listRowsDuplicate == null || isListUpdated)
			listRowsDuplicate = listRows;

		/**
		 * passing to adapter.
		 */

		vaccinesAdapter = new VaccineScheduleApdapter(this.getActivity(), listRows, this.child);
		setListAdapter(vaccinesAdapter);

		return listRows;
	}

	private ArrayList<VaccineScheduleRow> generateDefaultSchedule()
	{
		// Fill get vaccines if they are not yet filled
		if (this.vaccines == null)
		{

			this.vaccines = VaccineHelper.getSortedVaccines();
		}

		listRows = new ArrayList<VaccineScheduleRow>();
		VaccineScheduleRow tempRow;
		Vaccination tempVac;
		// Create a row for each Vaccine
		for (Vaccine v : this.vaccines)
		{
			tempRow = new VaccineScheduleRow();

			// Check vaccination for the vaccine
			tempVac = checkVaccinenGiven(v);

			// if a vaccination record exists then fill relevant information for
			// the row
			if (tempVac != null)
			{
				tempRow.setCenter(tempVac.getCentre());
				tempRow.setDueDate(tempVac.getDueDate());
				tempRow.setVaccinationDate(tempVac.getVaccinationDate());
				tempRow.setIsEditable(tempVac.getIsEditable());
				tempRow.setSelected(true);
				tempRow.setApplicable(true);

				if (tempVac.getIsEditable())
				{
					tempRow.setStatus(VaccinationStatus.SCHEDULED.name());
				}
				else
				{
					if (tempVac.getVaccinationDate() != null)
						tempRow.setStatus(VaccinationStatus.VACCINATED.name());
					else
						tempRow.setStatus(VaccinationStatus.RETRO_DATE_MISSING.name());

					tempRow.setGiven(true);
				}
			}

			else
			{
				// if the child has been provided send child's date of birth
				// otherwise null, VaccineHelper
				// would set today's date by by default if it receives a null
				// date
				Date defaultDate = VaccineHelper.getDueDatefromBirth(v.getName(), child != null ? child.getDateOfBirth() : null);
				tempRow.setDueDate(defaultDate);
				tempRow.setIsEditable(true);
			}
			if (this.child != null)
			{
				tempRow.setChildId(child.getProjectId());
				tempRow.setDateOfBirth(child.getDateOfBirth());
			}
			else
			{
				tempRow.setDateOfBirth(new Date());
			}
			tempRow.setVaccineName(v.getName());
			listRows.add(tempRow);
		}
		listRowsDuplicate = null;
		isListUpdated = false;
		return listRows;
	}

	@Override
	public void onListItemClick(ListView l, View v, final int pos, long id)
	{
		// check a child is associated with this schdedule
		if (child == null || "".equalsIgnoreCase(child.getProjectId().trim()))
		{
			EpiUtils.showDismissableDialog(
					getActivity(),
					"No child with valid ID provided, you can only view the schedule",
					"Sorry").show();
			return;
		}
		else if (!listRows.get(pos).isEligible())
		{// if not eligible, do not
			// proceed
			EpiUtils.showDismissableDialog(
					getActivity(),
					"This vaccine can not be given now, please check age of child OR"
							+ " missing required vaccines", "Sorry").show();
			return;
		}
		else if (listRows.get(pos).isEditable())
		{

			if (!listRows.get(pos).isApplicable())
			{
				IDialogListener dialogListener = new IDialogListener()
				{
					@Override
					public void onDialogPositiveClick(Map... o)
					{
						Intent intent = new Intent(getActivity(),
								VaccinationDetails.class);
						intent.putExtra("listRow", listRows);
						intent.putExtra("itemPos", pos);
						intent.putExtra("child", child);
						startActivityForResult(intent, 101);
					}

					@Override
					public void onDialogNeutralClick(Map... o)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void onDialogNegativeClick(Map... o)
					{
						return;

					}
				};

				// EpiUtils.showYesNoDialog(getActivity(),
				// "Child is too old for this vaccine. Are you sure you want to continue?",
				// "Alert", "Yes", "No", dialogListener).show();
				String childOldConfirmation = "Iss bache kee umer vaccine kee muqarara mudat se ziada hochuki hai. Kya aap phir bhi tika lagana chaheingay?";
				EpiUtils.showYesNoDialog(getActivity(), childOldConfirmation,
						"Alert", "Yes", "No", dialogListener).show();

			}
			else
			{
				Intent intent = new Intent(getActivity(),
						VaccinationDetails.class);
				intent.putExtra("listRow", this.listRows);
				intent.putExtra("itemPos", pos);
				intent.putExtra("child", child);
				startActivityForResult(intent, 101);
			}

		}
		else
		{
			Toast.makeText(getActivity(), R.string.errorMsg, Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		if (requestCode == 101)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				listRows = (ArrayList<VaccineScheduleRow>) data.getSerializableExtra("listRow");

				updateSchedule();
				// setting schedulable rows to scheduled
				setAutoScheduledVaccines(listRows);
			}
			else if (resultCode == Activity.RESULT_CANCELED)
			{
				listRows = (ArrayList<VaccineScheduleRow>) data.getSerializableExtra("listRow");

				updateSchedule();
				// setting schedulable rows to scheduled
				setAutoScheduledVaccines(listRows);

				Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void setVaccinations(List<Vaccination> vaccs)
	{
		this.vaccinations = new Vaccination[vaccs.size()];
		this.vaccinations = vaccs.toArray(this.vaccinations);

		isListUpdated = true;
	}

	private Vaccination checkVaccinenGiven(Vaccine v)
	{
		// check vaccinations were returned
		if (this.vaccinations != null)
		{
			for (Vaccination vac : vaccinations)
			{
				if (vac.getGivenVaccine().equals(v))
				{
					return vac;
				}
			}
		}
		return null;
	}

	public ArrayList<VaccineScheduleRow> getRows()
	{
		return this.listRows;
	}

	public JSONArray getVaccinations()
	{
		JSONArray array = new JSONArray();
		JSONObject temp = null;

		try
		{
			for (VaccineScheduleRow row : listRows)
			{
				if (row.isSelected())
				{
					temp = new JSONObject();
					temp.put(RequestElements.VACCINATION_STATUS, row.getStatus());
					temp.put(RequestElements.VACCINENAME, row.getVaccineName());
					temp.put(RequestElements.DATE_OF_VACCINATION, DateTimeUtils.DateToString(row.getVaccinationDate(), null));
					temp.put(RequestElements.NEXT_ALLOTTED_DATE, DateTimeUtils.DateToString(row.getDueDate(), null));
					// TODO: add actual centre id here
					temp.put(RequestElements.VACCINATION_CENTER, getCentreId(row));

					array.put(temp);
				}
			}
		}
		catch (Exception e)
		{
			Log.e(VaccineScheduleFragment.class.getSimpleName(), e.getMessage());
			Log.e(VaccineScheduleFragment.class.getSimpleName(),
					"Error getting vaccinations from schedule");
		}
		return array;
	}

	public void validationsBeforeActivity(VaccineScheduleRow row,
			ArrayList<VaccineScheduleRow> rows)
	{

		/**
		 * Validate Prereq vaccine
		 * 
		 */

		VaccinationValidator.PrerequisiteValidationResult result = VaccinationValidator
				.checkPrerequisites(row.getVaccineName(), rows);

		boolean missingPrerequisite;
		missingPrerequisite = result.hasErrors;

		if (!missingPrerequisite)
		{
			row.setEligible(true);
		}
		else
		{
			row.setEligible(false);
			row.setStatus(null);

			row.setGiven(false);

			row.setSelected(false);
		}
	}

	/**
	 * auto scheduling or row
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (listRows.get(position).isEditable() && listRows.get(position).isApplicable() && listRows.get(position).isEligible()
				&& listRows.get(position).getDueDate().getTime() > new Date().getTime()
				&& listRows.get(position).isSelected() != true
				&& !(child == null || "".equalsIgnoreCase(child.getProjectId().trim())))
		{

			// Making sure that Vaccination is not being scheduled on a Sunday
			Calendar c = Calendar.getInstance();
			c.setTime(listRows.get(position).getDueDate());
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 1)
			{
				c.setTime(listRows.get(position).getDueDate());
				c.add(Calendar.DATE, 1);
				listRows.get(position).setDueDate(c.getTime());
			}
			listRows.get(position).setStatus(VaccinationStatus.SCHEDULED.name());
			listRows.get(position).setSelected(true);

			Toast.makeText(getActivity(), listRows.get(position).getVaccineName() + " has been scheduled succesfully", Toast.LENGTH_SHORT).show();
			updateSchedule();

			return true;
		}
		else if (listRows.get(position).isEditable() && listRows.get(position).isEligible()
				&& listRows.get(position).isSelected() == true)
		{
			if (listRows.get(position).getStatus() != null)
			{
				if (listRows.get(position).getStatus().equals(VaccinationStatus.SCHEDULED.name()))
				{
					listRows.get(position).setStatus(null);
					listRows.get(position).setSelected(false);
					updateSchedule();
					return true;
				}
			}
			else
			{
				return false;
			}

		}

		return false;
	}

	/**
	 * 
	 * @param row
	 * @return centre id for the vaccination centre selected.
	 */
	private int getCentreId(VaccineScheduleRow row)
	{

		int id = 0;
		for (Centre centre : centres)
		{
			if (centre.getName().equals(row.getCenter()))
			{
				id = centre.getCentreId();
				return id;
			}
		}

		return id;

	}

	public void setRow()
	{
		listRows = listRowsDuplicate;
		updateSchedule();
	}

	/**
	 * It set vaccines to sheduled if they are applicable
	 * 
	 * @param listRows
	 */
	public void setAutoScheduledVaccines(ArrayList<VaccineScheduleRow> listRows)
	{
		for (VaccineScheduleRow row : listRows)
		{
			if (row != null)
			{
				if (row.isEditable() && row.isApplicable() && row.isEligible()
						&& row.getDueDate().getTime() > new Date().getTime() && row.isSelected() != true
						&& !(child == null || "".equalsIgnoreCase(child.getProjectId().trim())))
				{
					// Making sure that Vaccination is not being scheduled on a
					// Sunday
					Calendar c = Calendar.getInstance();
					c.setTime(row.getDueDate());
					int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
					if (dayOfWeek == 1)
					{
						c.setTime(row.getDueDate());
						c.add(Calendar.DATE, 1);
						row.setDueDate(c.getTime());
					}
					row.setStatus(VaccinationStatus.SCHEDULED.name());
					row.setSelected(true);
				}
			}
		}
	}

	public void validateLateGap(VaccineScheduleRow row)
	{
		/**
		 * Validate Late Vaccine Gap
		 */
		if (child != null && !row.isSelected() && child.getDateOfBirth() != null)
		{
			Boolean isLate = VaccinationValidator.checkLateVaccinationGap(row, child.getDateOfBirth());
			if (isLate)
				row.setApplicable(false);
			else
				row.setApplicable(true);

		}
	}

}