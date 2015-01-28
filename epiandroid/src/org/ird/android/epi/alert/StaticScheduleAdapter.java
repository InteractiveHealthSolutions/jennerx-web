package org.ird.android.epi.alert;

import java.util.ArrayList;

import org.ird.android.epi.R;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineGap;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StaticScheduleAdapter extends ArrayAdapter<Vaccine> {
	Context cxt;
	ArrayList<Vaccine> rows;
	VaccineGap[] vacGap;

	public StaticScheduleAdapter(Context context, ArrayList<Vaccine> listRows, VaccineGap[] vacGap) {
		super(context, R.layout.vaccine_schedule_row_layout, listRows);
		this.cxt = context;
		this.rows = listRows;
		this.vacGap = vacGap;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) this.cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.vaccine_schedule_row_layout, null);
		}

		Vaccine vac = rows.get(position);

		if (convertView != null) {
			// set tag for the converter

			// Get views from the inflated row layout
			TextView txtVwVaccineName = (TextView) convertView.findViewById(R.id.tvVaccineName);
			final TextView txtVwDateValue = (TextView) convertView.findViewById(R.id.tvDateActual);
			txtVwVaccineName.setText(vac.getName());
			String scheduleText = "";
			for (VaccineGap gap : vacGap) {
				if (gap.getVacine().getName().equals(rows.get(position).getName())) {
					if (gap.getValue() > 0)
						scheduleText = "<b>" + gap.getValue() + " " + gap.getTimeUnit() + "S</b>" + " from Date of Birth";
					else
						scheduleText = "<b>" + gap.getValue() + " DAY</b> " + "\t  from Date of Birth";
					break;
				}

			}

			txtVwDateValue.setText(Html.fromHtml(scheduleText));
			txtVwDateValue.setTextColor(cxt.getResources().getColor(R.color.PaleBlue));
			

		}

		return convertView;
	}

}
