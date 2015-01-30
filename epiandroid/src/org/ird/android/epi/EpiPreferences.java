package org.ird.android.epi;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EpiPreferences extends PreferenceActivity
{	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.application_preferences);
	}

}
