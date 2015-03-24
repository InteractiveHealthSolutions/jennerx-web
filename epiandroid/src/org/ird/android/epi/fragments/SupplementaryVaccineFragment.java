package org.ird.android.epi.fragments;

import java.util.ArrayList;
import java.util.Date;

import org.ird.android.epi.R;
import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.validation.VaccinationValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SupplementaryVaccineFragment extends Fragment
{
	Button referesh;
	Button btnSelectAll;
	String stringDate;

	CheckBox[] vacChkBoxes;
	Vaccine[] suppVaccines;

	/**
	 * to allow list to be properly updated when start from enrollment or
	 * follow-up activity.
	 */
	public Boolean isListUpdated = true;

	private Context context;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.context = getActivity().getApplicationContext();

		/*
		 * Number of check-boxes should be same as number of supplementary vaccines
		 */
		suppVaccines = VaccineService.getAllSupplementaryVaccines(context);
		vacChkBoxes = new CheckBox[suppVaccines.length];
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * Had to Override this method due to addition of refresh button in the default layout of
	 * ListFragment
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.supplementary_vaccination_main_layout, container);

		TextView txtView;

		Date date = new Date();
		stringDate = DateTimeUtils.DateToString(date, null);

		/*
		 * One linearLayout with textView (to show date)
		 * and checkbox for vaccines
		 */
		for (int i = 0; i < suppVaccines.length; i++)
		{
			LinearLayout lLayout = new LinearLayout(context);

			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			int px = EpiUtils.convertDptoPx(context, 5);
			linearParams.setMargins(px, px, px, px);

			lLayout.setLayoutParams(linearParams);

			/*
			 * Converting dp in px.
			 */

			px = EpiUtils.convertDptoPx(context, 8);


			lLayout.setPadding(px, 0, 0, 0);

			lLayout.setBackgroundColor(getResources().getColor(R.color.Azure));
			lLayout.setOrientation(LinearLayout.VERTICAL);

			vacChkBoxes[i] = (CheckBox) inflater.inflate(R.layout.checkbox_right_layout, null);

			LinearLayout.LayoutParams commonParams = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			vacChkBoxes[i].setLayoutParams(commonParams);

			int sdk = android.os.Build.VERSION.SDK_INT;

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
			{
				vacChkBoxes[i].setBackgroundDrawable(null);
			}
			else
			{
				vacChkBoxes[i].setBackground(null);
			}

			vacChkBoxes[i].setTextColor(Color.BLACK);
			vacChkBoxes[i].setTypeface(null, Typeface.BOLD);

			vacChkBoxes[i].setText(suppVaccines[i].getName());

			txtView = new TextView(context);

			txtView.setTextColor(Color.BLACK);
			txtView.setText(stringDate);

			px = EpiUtils.convertDptoPx(context, 5);

			commonParams.setMargins(px, px, px, px);
			txtView.setLayoutParams(commonParams);


			lLayout.addView(vacChkBoxes[i], 0);
			lLayout.addView(txtView, 1);

			LinearLayout sinlgeLnrLayout = (LinearLayout) view.findViewById(R.id.singelLnrLayout);
			sinlgeLnrLayout.addView(lLayout, i);

		}

		referesh = (Button) view.findViewById(R.id.btnRefrsh);

		referesh.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				for (CheckBox c : vacChkBoxes)
				{
					c.setChecked(false);
				}
			}
		});

		
		// Registering click event for "SelectAll" button
		btnSelectAll = (Button) view.findViewById(R.id.btnSelectAll);

		btnSelectAll.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				for (CheckBox c : vacChkBoxes)
				{
					c.setChecked(true);
				}
			}
		});

		return view;
	}

	public void validationsBeforeActivity(VaccineScheduleRow row, ArrayList<VaccineScheduleRow> rows)
	{

		VaccinationValidator.PrerequisiteValidationResult result = VaccinationValidator.checkPrerequisites(row.getVaccineName(), rows, context);

		boolean missingPrerequisite;
		missingPrerequisite = result.hasErrors;

		if (!missingPrerequisite)
		{
			row.setEligible(true);
		}
		else
		{
			row.setEligible(false);
		}
	}

	public JSONArray getVaccinations()
	{
		JSONArray array = new JSONArray();
		JSONObject temp = null;

		try
		{
			for (CheckBox chkBox : vacChkBoxes)
			{
				if (chkBox.isChecked())
				{
					temp = new JSONObject();
					temp.put(RequestElements.VACCINATION_STATUS, VaccinationStatus.VACCINATED);
					temp.put(RequestElements.VACCINENAME, chkBox.getText().toString());
					temp.put(RequestElements.DATE_OF_VACCINATION, stringDate);
					
					// temp.put(RequestElements.NEXT_ALLOTTED_DATE, null);
					// TODO: add actual centre id here
					
					temp.put(RequestElements.VACCINATION_CENTER, GlobalConstants.VACCINATION_CENTRE_ID);

					array.put(temp);
				}
			}
		}

		catch (Exception e)
		{
			Log.e(VaccineScheduleFragment.class.getSimpleName(), e.getMessage());
			Log.e(VaccineScheduleFragment.class.getSimpleName(), "Error getting vaccinations from schedule");
		}
		return array;
	}

}
