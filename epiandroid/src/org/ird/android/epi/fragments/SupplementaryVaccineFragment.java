package org.ird.android.epi.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ord.ird.android.epi.db.CentreDBHelper;

import org.ird.android.epi.R;
import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.VaccineScheduleApdapter;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.GlobalConstants;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccination;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.validation.VaccinationValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SupplementaryVaccineFragment extends Fragment {

	List<CheckBox> vaccines;
	Child child;
	Vaccination[] vaccinations = null;
	ArrayList<VaccineScheduleRow> listRows = null;
	ArrayList<VaccineScheduleRow> listRowsDuplicate = null;
	VaccineScheduleApdapter vaccinesAdapter = null;
	Button referesh;
	String d;
	
	CheckBox cbMeasles, cbOPV, cbIPV;
	TextView tvMeasles, tvOPV, tvIPV;

	/**
	 * to allow list to be properly updated when start from enrollment or
	 * follow-up activity.
	 */
	public Boolean isListUpdated = true;

	private CentreDBHelper centreDbHelper;
	private List<Centre> centres;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		centreDbHelper = new CentreDBHelper(getActivity());
		centres = centreDbHelper.getCentres();

	}

	/*
	 * Had to Override this method due to addition of refresh button in the default layout of
	 * ListFragment
	 * (non-Javadoc)
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		Vaccine supplementaryMeasles = new Vaccine();
		Vaccine supplementaryOPV = new Vaccine();
		Vaccine supplementaryIPV = new Vaccine();
		supplementaryMeasles.setName("Measles");
		supplementaryOPV.setName("OPV");
		supplementaryIPV.setName("IPV");
		
		
		
		View view = inflater.inflate(R.layout.supplementary_vaccination_main_layout, container);
		cbMeasles = (CheckBox) view.findViewById(R.id.cbNameMeasles);
		cbOPV = (CheckBox) view.findViewById(R.id.cbNameOPV);
		cbIPV = (CheckBox) view.findViewById(R.id.cbNameIPV);
		cbMeasles.setText(supplementaryMeasles.getName());
		cbOPV.setText(supplementaryOPV.getName());
		cbIPV.setText(supplementaryIPV.getName());
		
		vaccines = new ArrayList<CheckBox>();
		vaccines.add(cbMeasles);
		vaccines.add(cbOPV);
		vaccines.add(cbIPV);
		
		tvMeasles = (TextView) view.findViewById(R.id.tvDateMeasles);
		tvOPV = (TextView) view.findViewById(R.id.tvDateOPV);
		tvIPV = (TextView) view.findViewById(R.id.tvDateIPV);
		
		Date date = new Date();
		d = DateTimeUtils.DateToString(date, null);
		tvMeasles.setText(d);
		tvOPV.setText(d);
		tvIPV.setText(d);
				
		referesh = (Button) view.findViewById(R.id.btnRefrsh);
		
		referesh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				for(CheckBox c: vaccines) {
					c.setChecked(false);
				}
				
			}
		});
		
		
		return view;
	}

	public void setChild(Child ch) {
		this.child = ch;
	}
	
	public void validationsBeforeActivity(VaccineScheduleRow row, ArrayList<VaccineScheduleRow> rows) {
		
		VaccinationValidator.PrerequisiteValidationResult result = VaccinationValidator.checkPrerequisites(row.getVaccineName(), rows);
		
		boolean missingPrerequisite;
		missingPrerequisite = result.hasErrors;
		
		if (!missingPrerequisite) {
			row.setEligible(true);
		} else {
			row.setEligible(false);
		}
	}
	
	public JSONArray getVaccinations() {
		JSONArray array = new JSONArray();
		JSONObject temp = null;

		try {
			for (CheckBox row: vaccines ) {
				if (row.isChecked()) {
					temp = new JSONObject();
					temp.put(RequestElements.VACCINATION_STATUS, VaccinationStatus.VACCINATED);
					temp.put(RequestElements.VACCINENAME, row.getText().toString());
					temp.put(RequestElements.DATE_OF_VACCINATION, d);
					temp.put(RequestElements.NEXT_ALLOTTED_DATE,  null);
					// TODO: add actual centre id here
					temp.put(RequestElements.VACCINATION_CENTER, GlobalConstants.VACCINATION_CENTRE_ID);

					array.put(temp);
				}
			}
			
			
		} catch (Exception e) {
			Log.e(VaccineScheduleFragment.class.getSimpleName(), e.getMessage());
			Log.e(VaccineScheduleFragment.class.getSimpleName(), "Error getting vaccinations from schedule");
		}
		return array;
	}
	
	
	

}
