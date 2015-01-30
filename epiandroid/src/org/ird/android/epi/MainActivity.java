package org.ird.android.epi;

import org.ird.android.epi.R;
import org.ird.android.epi.common.SecurityUtils;
import org.ird.android.epi.nfc.EPINfcTags;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private static String LOG_MAIN_MENU = "MainMenu";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/*
	 * @Override
	 * public boolean onCreateOptionsMenu(Menu menu) {
	 * // Inflate the menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.login, menu);
	 * return true;
	 * }
	 */

	public void openForm(View formClicked)
	{
		if (formClicked.getId() == R.id.btnEnrollment || formClicked.getId() == R.id.imageViewCheck)
		{
			startIntent(EnrollmentActivity.class);
		}
		else if (formClicked.getId() == R.id.btnFollowUp || formClicked.getId() == R.id.imageViewfollowup)
		{
			startIntent(FollowUpActivity.class);
		}
		else if (formClicked.getId() == R.id.btnUpdateID || formClicked.getId() == R.id.imageViewUpdateId)
		{
			startIntent(IdChangeActivity.class);
		}
		else if (formClicked.getId() == R.id.btnLogout || formClicked.getId() == R.id.imageViewLogout)
		{
			logout();
		}
		else if (formClicked.getId() == R.id.btnClose || formClicked.getId() == R.id.imageViewExit)
		{
			onBackPressed();
		}
		else if (formClicked.getId() == R.id.btnSchedule || formClicked.getId() == R.id.imageViewCalendar)
		{
			startIntent(StaticScheduleActivity.class);
		}

	}

	private void startIntent(Class activityToStart)
	{
		Intent intent = new Intent(MainActivity.this, activityToStart);
		startActivity(intent);
	}

	/*
	 * @Override public boolean onMenuItemClick(MenuItem item) { // TODO
	 * Auto-generated method stub return false; }
	 */

	/*
	 * @Override
	 * public boolean onOptionsItemSelected(MenuItem item) {
	 * Intent intent;
	 * 
	 * switch (item.getItemId()) {
	 * case R.id.action_settings:
	 * intent = new Intent(this, EpiPreferences.class);
	 * startActivity(intent);
	 * break;
	 * 
	 * case R.id.action_about:
	 * intent = new Intent(this, AboutAtivity.class);
	 * startActivity(intent);
	 * break;
	 * }
	 * return false;
	 * }
	 */

	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(this).setTitle(R.string.exit).setMessage(R.string.exitText).setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						LoginActivity.EXIT_REQUEST = true;
						finish();
					}
				}).create().show();
	}

	private void logout()
	{
		new AlertDialog.Builder(this).setTitle(R.string.logout).setMessage(R.string.logoutText).setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						finish();
					}
				}).create().show();
	}

}
