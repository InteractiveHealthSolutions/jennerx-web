package org.ird.android.epi.fragments;

import java.util.Date;

import org.ird.android.epi.R;
import org.ird.android.epi.barcode.Barcode;
import org.ird.android.epi.common.DateTimeUtils;
import org.ird.android.epi.model.Child;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class ChildHeaderFragment extends Fragment
{
	private String projectId;
	
	//age related Strings
	private static final String YEAR="y";
	private static final String MONTH="m";
	private static final String WEEK ="w";
	private static final String DAY ="d";
	
	//properties
	Date _dob;
	String _epiNo;
	String _projectId;
	String _gender;
	Child _child;
	
	//controls
	View layout 		=	null;
	TextView txtAge		=	null;
	ImageView imgGender	=	null;
	TextView txtGender	=	null;
	TextView txtId		=	null;
	TextView txtEpi		=	null;
	TextView txtName	=	null;
	Button	btnScan		= 	null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		layout =inflater.inflate(R.layout.child_header_layout, container,false); 
		if(layout !=null)//the layout was inflated successfully
		{
			txtName = (TextView)layout.findViewById(R.id.txtVwName);
			txtAge = (TextView)layout.findViewById(R.id.txtVwAge);
			imgGender = (ImageView)layout.findViewById(R.id.imgGender);
			txtGender =  (TextView)layout.findViewById(R.id.txtVwGChar);
			txtId = (TextView)layout.findViewById(R.id.txtVwQR);
			txtEpi = (TextView)layout.findViewById(R.id.txtVwEpiValue);
		}
		return layout;
	}
	
	public ChildHeaderFragment()
	{
		super();
	}
	
	public ChildHeaderFragment(Child child)
	{
		this._child=child;
		this._projectId = child.getProjectId();
		this._gender=child.getGender();
		this._dob=child.getDateOfBirth();
	}
	
	private void setErrorOnHeader()
	{
		if(layout==null)
			return;
		layout.setBackgroundColor(getResources().getColor(R.color.Tomato));		
	}
	
	private void RemoveErrorFromHeader()
	{
		if(layout==null)
			return;
		layout.setBackgroundColor(getResources().getColor(R.color.LightGrey));
	}
	
	public ChildHeaderFragment(String id, Date dob, String gender)
	{
		this._projectId = id;
		this._gender=gender;
		this._dob=dob;
		fillHeader();
	}
	
	public void updateHeader(String epiNo, String id, Date dob, String gender)
	{
		this._epiNo = epiNo;
		this._projectId = id;
		this._gender=gender;
		this._dob=dob;
		fillHeader();
	}
	
	private boolean validateChild()
	{
		boolean isValid=true;
		
		if(_child==null)
			isValid=false;
		if(_child.getProjectId()==null || "".equals(_child.getProjectId().trim()))
			isValid=false;
		if(_child.getGender()==null || "".equals(_child.getGender().trim()))
			isValid=false;
		if(_child.getDateOfBirth()==null )
			isValid=false;
		
		return isValid;
	}
	
	public void updateHeader(Child child)
	{
		this._child = child;
		
		//Try to update the header even if validation fails
		try
		{
			updateHeader(_child.getEpiNumber(), _child.getProjectId(), _child.getDateOfBirth(), _child.getGender());			
		}
		catch(Exception ex)
		{
			Log.e(ChildHeaderFragment.class.getSimpleName(), ex.getMessage());
			Log.e(ChildHeaderFragment.class.getSimpleName(), "Error updating child's header");
		}
	}
	private void fillHeader()
	{
		setName();
		setAge();
		setGender();
		setProjectID();
		setEpiNo();
	}
	
	private void setEpiNo()
	{
		if(txtEpi==null)
			return;
		txtEpi.setText(this._epiNo);
	}
	
	private void setAge()
	{
		/*if(txtAge==null || _dob ==null)
		{
			return;
		}
		if(_dob.getTime() > new Date().getTime())
		{
			EpiUtils.showDismissableDialog(getActivity(), "Cant date of birth of future", "Error").show();
			return;
		}
		
		StringBuilder ageInWords = new StringBuilder();
		int days;
		int mod;
		int age;
		//default values for flags
		boolean showDays=true;
		boolean showYears=false;
		
		//get the age in days
		age = DateTimeUtils.daysBetween(new Date(), _dob);
		
		//use modulus to determine the right String to show
		//check for year
		mod = age/365;
		if(mod >0 )
		{
			showDays=false;
			ageInWords.append(mod + YEAR);
		}
		else
		{
			mod =age;//reset mod, treat for months only
		}
	
		//check for month
		if(mod%30>0)
		{
			days=mod%30;
			mod = mod %30;
			if(mod>0)
			{
				if(showYears)
					ageInWords.append(mod + MONTH + " ");
				else
				{
					ageInWords.append(", " + mod + MONTH + " ");
					//No need to show days if dealing with years 
					showDays=true;						
				}
			}
		}*/	
		
		//TODO: add code here to calculate days
		StringBuilder age=new StringBuilder();
		if(_dob!=null)
		{
			int months = DateTimeUtils.getMonthsDifference(_dob, new Date());
			age.append(months + " months");
			age.append("\n");
			age.append(DateTimeUtils.DateToString(_dob, null));
		}
		else
		{
			age.append("N/A");
		}
		txtAge.setText(age.toString());
	}
	private void setGender()
	{
		if(imgGender==null)
			return;
		
		if(getResources().getString(R.string.gender_male).equalsIgnoreCase(_gender))
		{
			imgGender.setImageDrawable(getResources().getDrawable(R.drawable.male));
			txtGender.setText("M");
		}
		else if(getResources().getString(R.string.gender_female).equalsIgnoreCase(_gender))
		{
			imgGender.setImageDrawable(getResources().getDrawable(R.drawable.female));
			txtGender.setText("F");
		}
		else
		{
			txtGender.setText("N/A");
		}
	}
	
	private void setProjectID()
	{
		if(txtId== null)
			return;
		txtId.setText(_projectId);
	}
	
	private void setName()
	{
		StringBuilder name= new StringBuilder();		
		
		if(_child==null)
			return;
		if(_child.isNamed())
		{
			name.append(_child.getChildFirstName() );
/*			name.append(" ");
			name.append(_child.getChildLastName() );*/
		}
		else
		{
			name.append("N/A");
		}
		txtName.setText(name.toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	  
	}
	
	public void setProjectId(String id)
	{
		txtId.setText(id);
		this._projectId=id;
		if(this._child!=null)
			this._child.setProjectId(id);
	}
	
	public String getProjectID()
	{
		return txtId.getText().toString().trim();
	}
	
	public void scan(View control)
	{
		Intent intent = new Intent(Barcode.BARCODE_INTENT);
		intent.putExtra(Barcode.SCAN_MODE,Barcode.QR_MODE);		
		startActivityForResult(intent, Barcode.BARCODE_REQUEST_CODE);
	}
}
