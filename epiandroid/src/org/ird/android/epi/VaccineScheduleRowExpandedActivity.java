package org.ird.android.epi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class VaccineScheduleRowExpandedActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vaccine_schedule_row_expanded_layout);
		
		Spinner spCentres = (Spinner)findViewById(R.id.spCentre);
		if(spCentres!=null)
		{
			String[] centres = getResources().getStringArray(R.array.array_korangi);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), spCentres.getId(),centres);
			spCentres.setAdapter(adapter);
		}
		
	}

}
