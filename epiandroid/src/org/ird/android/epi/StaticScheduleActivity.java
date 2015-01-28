package org.ird.android.epi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ord.ird.android.epi.db.CentreDBHelper;

import org.ird.android.epi.R;
import org.ird.android.epi.VaccinationDetails;
import org.ird.android.epi.VaccinationStatus;
import org.ird.android.epi.alert.StaticScheduleAdapter;
import org.ird.android.epi.alert.VaccineScheduleApdapter;
import org.ird.android.epi.alert.VaccineScheduleRow;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.dal.VaccineService;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Child;
import org.ird.android.epi.model.Vaccination;
import org.ird.android.epi.model.Vaccine;
import org.ird.android.epi.model.VaccineConstants;
import org.ird.android.epi.model.VaccineGap;
import org.ird.android.epi.model.VaccineHelper;
import org.ird.android.epi.validation.VaccinationValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class StaticScheduleActivity extends ListActivity
{

	Vaccine[] vaccines = null;
	ArrayList<Vaccine> listRows;
	StaticScheduleAdapter vaccinesAdapter = null;
	VaccineGap[] vacGap;
	Context cxt;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.cxt = getApplicationContext();
		listRows = new ArrayList<Vaccine>();
		vacGap = VaccineService.getAllGaps(this.cxt);
		updateSchedule();
	}

	public void updateSchedule()
	{

		vaccines = VaccineHelper.getSortedVaccines(this.cxt);
		for (Vaccine vac : vaccines)
		{
			listRows.add(vac);
		}
		vaccinesAdapter = new StaticScheduleAdapter(StaticScheduleActivity.this, listRows, vacGap);
		setListAdapter(vaccinesAdapter);
	}

}
