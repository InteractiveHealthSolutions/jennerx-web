package org.ird.android.epi;

import org.ird.android.epi.fragments.VaccinationRowFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.ird.android.epi.common.GlobalConstants;

public class VaccinationOverviewActivity extends Activity
{
	private FragmentManager fm;
	private static String TAG_VACCINE_ROW="VaccineRow";
	
	LinearLayout content=null;
	String tagBCG="bcg";
	String tagP1="p1";
	String tagP2="p2";
	String tagP3="p3";
	String tagM1="m1";
	String tagM2="m2";
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		
		System.out.print("Acitivity onCreate started");
		Log.i(TAG_VACCINE_ROW, "Acitivity onCreate started");		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vaccination_overview_layout);
		content = (LinearLayout)findViewById(R.id.layoutVaccinesOverview);
		fm = getFragmentManager();
		
		//Activity is being recreated after a screen orientation is rotated
		if(savedInstanceState != null)
		{
			//get the fragments array
		
		}
		
		fillRows();
		System.out.print("Acitivity onCreate finished");
		
	}
	
//	@Override
//	protected void onResume()
//	{
//		super.onResume();
//	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		//fillRows();
	}
	
	private void fillRows()
	{
		try
		{
			
			FragmentTransaction ft= fm.beginTransaction();
			Drawable d = getResources().getDrawable(R.drawable.follow_up);
			Drawable d1 = getResources().getDrawable(R.drawable.missing);
			Drawable d2 = getResources().getDrawable(R.drawable.enrollement);
			
			//Add vaccine rows for demo
			VaccinationRowFragment vacRow; 
			Bundle data;
			//BCG
			vacRow = new VaccinationRowFragment();
			
			//prepare data to sent to the fragment
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "Baldia");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.BCG));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.enrollement);
			vacRow.setArguments(data);
			
			ft.add(content.getId(), vacRow);
			
			//P1
			vacRow = new VaccinationRowFragment();
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "Baldia");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.P1));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.follow_up);
			vacRow.setArguments(data);
			ft.add(content.getId(), vacRow);
			
			//P2
			vacRow = new VaccinationRowFragment();
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "Korangi");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.P2));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.follow_up);
			vacRow.setArguments(data);		
			ft.add(content.getId(), vacRow);
			
			//P3
			vacRow = new VaccinationRowFragment();
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "Korangi");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.P3));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.follow_up);
			vacRow.setArguments(data);			
			ft.add(content.getId(), vacRow);
			
			//M1
			vacRow = new VaccinationRowFragment();
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "N/A");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.M1));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.missing);
			vacRow.setArguments(data);			
			ft.add(content.getId(), vacRow);
			
			//M2
			vacRow = new VaccinationRowFragment();
			
			data = new Bundle();
			data.putString(GlobalConstants.ARGUMENT_CENTRE_NAME, "Baldia");
			data.putString(GlobalConstants.ARGUMENT_VACCINE_NAME, getResources().getString(R.string.M2));
			data.putInt(GlobalConstants.ARGUMENT_IMAGE_ID, R.drawable.follow_up);
			vacRow.setArguments(data);			
			
			ft.add(content.getId(), vacRow);

			ft.commit();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vaccination_overview, menu);
		return true;
	}

}
