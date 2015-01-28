package org.ird.android.epi;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FragmentTestActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_test_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fragment_test, menu);
		return true;
	}

}
