package org.ird.android.epi;

import java.util.Date;

import org.ird.android.epi.alert.VaccineDatePicker;
import org.ird.android.epi.alert.VaccineScheduleApdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

public class VaccineScheduleDetailActitivty extends Activity{

	EditText edtTextDate;
	Spinner sp;
	CheckBox chkBxMissing;
	
	public static final String CENTRE_KNOWN="centreknw";
	public static final String VACCINATION_DATE="vcdate";
	public static final String CENTRE_NAME="centrname";
	private int position;
	
	private boolean isMissing = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vaccine_schdule_details_layout);
	
		this.position = savedInstanceState.getInt(VaccineScheduleApdapter.ROW_POSITION);
		
		edtTextDate = (EditText)findViewById(R.id.edtTxtDateGivenn);
		sp = (Spinner)findViewById(R.id.spCentreGiven);
		//chkBxMissing = (CheckBox)findViewById(R.id.chkBoxDateNotKnow);
		
		String[] centres = new String[]{"Korangi", "Baldia", "Bin Qasim","Sir Syed","Other Centre"};
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, centres);
		sp.setAdapter(adapter);
		
		if(edtTextDate!=null)
			edtTextDate.setOnClickListener(new VaccineDatePicker(this, edtTextDate, new Date(),null,null));
		
		if(chkBxMissing!=null)
			chkBxMissing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if(buttonView.isChecked())
					{
						isMissing=true;
						edtTextDate.setText("");
						edtTextDate.setEnabled(false);
					}
					else
					{
						isMissing=false;
						edtTextDate.setEnabled(true);
					}
				}
			});
	}
	
	
	private boolean validate()
	{
		if(edtTextDate.getText() == null || "".equals(edtTextDate.getText().toString().trim()))
		{
			return false;
		}
		
		if("".equals(sp.getSelectedItem().toString()))
				return false;
		
		return true;
	}
	
	public void click(View v)
	{
		if(v.getId() ==  R.id.btnSaveVaccineDetails)
		{
			if(validate())
			{
				//TODO: add values in Intent here and setResult(RESULT_OK, intent)
			}
			else
			{
				//TODO:show error message here				
			}
		}
		else if(v.getId() ==  R.id.btnCancelVaccineDetails)
		{
			super.finish();
		}
	}
	
	@Override
	public void finish()	
	{	
		Intent intent = new Intent();
		intent.putExtra(CENTRE_KNOWN, chkBxMissing.isChecked());
		intent.putExtra(VACCINATION_DATE, edtTextDate.getText().toString());
		intent.putExtra(CENTRE_NAME, sp.getSelectedItem().toString());
		intent.putExtra(VaccineScheduleApdapter.ROW_POSITION, this.position);
		
		setResult(RESULT_OK, intent);
		super.finish();
	}
}
