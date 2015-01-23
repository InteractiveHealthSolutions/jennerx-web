package org.ird.android.epi.alert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.ird.android.epi.R;
import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.VaccineName;
import org.ird.android.epi.validation.VaccinationValidator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VaccineScheduleApdapter extends ArrayAdapter<VaccineScheduleRow>
{
	Context cxt;
	Child child;
	ArrayList<VaccineScheduleRow> rows;
	// String[] centres = new String[] { "Korangi", "Baldia", "Bin Qasim",
	// "Sir Syed" };
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	// Vaccination[] vaccinations;

	int[] vaccineDays;
	static final int REQUEST_CODE_ADDVACINE = 1;
	static final int REQUEST_CODE_EDITVACINE = 2;
	public static final String ROW_POSITION = "rowpos";

	public VaccineScheduleApdapter(Context context, ArrayList<VaccineScheduleRow> listRows, Child ch)
	{
		super(context, R.layout.vaccine_schedule_row_layout, listRows);
		this.cxt = context;
		this.rows = listRows;
		this.child = ch;
	}

	public void setChild(Child child)
	{
		this.child = child;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final LayoutInflater inflater = (LayoutInflater) this.cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.vaccine_schedule_row_layout, null);
		}

		final VaccineScheduleRow row = rows.get(position);

		if (convertView != null)
		{
			// set tag for the converter
			convertView.setTag(rows.get(position));

			// Get views from the inflated row layout
			TextView txtVwVaccineName = (TextView) convertView.findViewById(R.id.tvVaccineName);
			ImageView imgViewCheck = (ImageView) convertView.findViewById(R.id.imageViewCheck);

			if (row.getStatus() != null)
			{
				if (row.status.equals(VaccinationStatus.SCHEDULED.name()))
					imgViewCheck.setVisibility(View.VISIBLE);
				else
					imgViewCheck.setVisibility(View.GONE);
			}

			else
			{
				imgViewCheck.setVisibility(View.GONE);
			}

			final TextView txtVwDateValue = (TextView) convertView.findViewById(R.id.tvDateActual);

			// set vaccine name

			String rowName = row.getVaccineName();
			txtVwVaccineName.setText(VaccineName.valueOf(rowName).getVaccine());

			// Set 'Given Date' if vaccine already given, Other set 'Due Date'
			if (row.isGiven())
			{
				if (row.status.equals(VaccinationStatus.RETRO_DATE_MISSING.name()))
					txtVwDateValue.setText("Retro: No date specified");
				else if (row.status.equals(VaccinationStatus.RETRO.name()))
					txtVwDateValue.setText("Retro:  " + DateTimeUtils.DateToString(row.getVaccinationDate(), null));
				else
					txtVwDateValue.setText("Vaccinated On:  " + DateTimeUtils.DateToString(row.getVaccinationDate(), null));

				// set background color of the row
				convertView.setBackgroundColor(this.cxt.getResources().getColor(R.color.YellowGreen));

			}
			else
			{
				if (row.isEligible() && row.isApplicable)
				{
					// red background color of the row if date has passed

					if (row.getDueDate() != null && row.getDueDate().getTime() < new Date().getTime())
					{
						convertView.setBackgroundColor(this.cxt.getResources().getColor(R.color.Tomato));
						txtVwDateValue.setText("Due:  " + DateTimeUtils.DateToString(row.getDueDate(), null));
					}
					else
					// otherwise set Light blue
					{
						convertView.setBackgroundColor(this.cxt.getResources().getColor(R.color.LightBlue));
						txtVwDateValue.setText("Due:  " + DateTimeUtils.DateToString(row.getDueDate(), null));
					}
				}

				else if (!row.isEligible())
				{
					convertView.setBackgroundColor(this.cxt.getResources().getColor(R.color.LightGrey));
					// txtVwDateValue.setText("Not allowed: Previous vaccine not given.");
					txtVwDateValue.setText("");
				}
				// check if row has "scheduled" status then don't set white color
				// condition need to be add with below condition VaccinationStatus.SCHEDULED.name().equals(row.status)
				else if (!row.isApplicable)
				{
					convertView.setBackgroundColor(this.cxt.getResources().getColor(R.color.AntiqueWhite));
					txtVwDateValue.setText("Bache kee umer ziada hogai hai.");
				}
			}
		}

		return convertView;
	}
}
