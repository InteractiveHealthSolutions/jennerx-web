package org.ird.android.epi;

import java.util.Map;

import org.ird.android.epi.alert.IDialogListener;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.fragments.VaccineScheduleFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestActivity extends Activity implements IDialogListener{

	VaccineScheduleFragment fragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_test);
		EditText tvw = (EditText)findViewById(R.id.textViewUserName);
		tvw.setError("This is an error");
		fragment = (VaccineScheduleFragment)getFragmentManager().findFragmentById(R.id.fragmentTestSchedule);
//		if(fragment!= null)
//		{
//			Child ch = ChildService.getChildById("123456");
//			fragment.setChild(ch);
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	public void click(View widget)
	{
		
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE,Barcode.QR_MODE);
		
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
		

		//AlertDialog alert = EpiUtils.showAlert(this, "I am clicked", "alert", this);
		//AlertDialog alert = EpiUtils.showYesNoDialog(this, "Do you want to proceed", "Question?", "Proceed",null, this);
		//alert.show();
	}

	@Override
	public void onDialogPositiveClick(Map...m) {
		Toast.makeText(this, "I positively clicked", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDialogNegativeClick(Map...m) {
		Toast.makeText(this, "I negatively clicked", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onDialogNeutralClick(Map...m) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int request, int result, Intent intent)
	{
		if(request == Barcode.BARCODE_REQUEST_CODE)
		{
			if (result== RESULT_OK)
			{
				String str = intent.getStringExtra (Barcode.SCAN_RESULT);
				Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
