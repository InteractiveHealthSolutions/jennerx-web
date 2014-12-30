package org.ird.android.epi;

import org.ird.android.epi.common.ArrayValues;

import android.app.TabActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;

public class ImmunizaionRegistryActivity extends TabActivity
{
	
	protected Resources res;
	private TabHost tabFollowUp;
	ArrayAdapter arrayAdapter;
	
	Spinner sp1,sp2,sp3,sp4, spMain;
	ListView listMain;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_history);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		res = getResources();
		
		tabFollowUp = getTabHost();		
		TabHost.TabSpec tabSpec;
		
		//Set the tab for fetching data from server
		tabSpec = tabFollowUp.newTabSpec("Demographics");
		tabSpec.setIndicator("Biodata");
		tabSpec.setContent(R.id.tabDemoBiodata);
		//add to TabHost
		tabFollowUp.addTab(tabSpec);	
		
		//Set the tab for fetching data from server
		tabSpec = tabFollowUp.newTabSpec("History");
		tabSpec.setIndicator("History");
		tabSpec.setContent(R.id.tabDemoVac);
		//add to TabHost
		tabFollowUp.addTab(tabSpec);
		
		//Create tab for Program
		tabSpec = tabFollowUp.newTabSpec("Details");
		tabSpec.setIndicator("History Details");
		tabSpec.setContent(R.id.tabDemoHistory);			
		tabFollowUp.addTab(tabSpec);
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayValues.getVaccines());
		spMain =(Spinner)findViewById(R.id.spinnerMain);
		listMain =(ListView)findViewById(R.id.listMain);
		listMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		sp1 =(Spinner)findViewById(R.id.spinner1);
		sp2 =(Spinner)findViewById(R.id.spinner2);
		sp3 =(Spinner)findViewById(R.id.spinner3);
		sp4 =(Spinner)findViewById(R.id.spinner4);
		sp1.setAdapter(arrayAdapter);
		sp2.setAdapter(arrayAdapter);
		sp3.setAdapter(arrayAdapter);
		sp4.setAdapter(arrayAdapter);
		
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, ArrayValues.getVaccines());
		listMain.setAdapter(arrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.immunizaion_registry, menu);
		return true;
	}

}
