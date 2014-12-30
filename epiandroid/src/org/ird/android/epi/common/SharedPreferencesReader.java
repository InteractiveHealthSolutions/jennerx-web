package org.ird.android.epi.common;

import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesReader
{
	// public static boolean writeSharedPreferences()
	// {
	// boolean changed = false;
	//
	// SharedPreferences sharedPref = null;
	// sharedPref = getSharedPreferences("Application Config", MODE_PRIVATE);
	// editor = sharedPref.edit();
	// editor.putBoolean("isFirstTime", true);
	// editor.commit();
	//
	// return changed;
	// }

	public static Integer readUserId(SharedPreferences sharedPref)
	{
		Integer userId = 0;

		try
		{
			userId = sharedPref.getInt("userid_pref", 0);
		}

		catch (Exception ex)
		{
			Log.e("userid_pref", ex.getMessage());
		}


		return userId;
	}
}
