package org.ird.android.epi;

import org.ird.android.epi.communication.elements.RequestElements;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AboutAtivity extends Activity
{

	private static final String TAG_ABOUT="AboutActivity"; 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		
		TextView txtVw = (TextView)findViewById(R.id.txtVwAbout);
		if(txtVw!=null)
		{
			//set application version here
			
			String version;
			PackageInfo pInfo;
			try
			{
				pInfo = getPackageManager().getPackageInfo(	getPackageName(), 0);
				version = pInfo.versionName;
				txtVw.setText("The version of this software is " + pInfo.versionName);
			}
			catch (NameNotFoundException e)
			{
				Log.e(TAG_ABOUT, "Error occurred while fetching version number: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_ativity, menu);
		return true;
	}

}
