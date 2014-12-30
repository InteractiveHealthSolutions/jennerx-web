package org.ird.android.epi.fragments;

import java.util.Date;

import org.ird.android.epi.R;

import android.app.Fragment;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VaccinationRowFragment extends Fragment
{
	private String vaccineName;
	private Date vaccinationDate;
	private int imageId; //used for displaying the status
	private String centreName;
	private int centreId;
	
	TextView txtVwVaccineName;
	TextView txtVwCentre;
	
	
	private static String TAG_VACCINE_ROW="VaccineRow";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{		
		System.out.print("Fragment onCreateView finished");
		Log.i(TAG_VACCINE_ROW, "Fragment onCreateView finished");
		
	
				
		return inflater.inflate(R.layout.vaccination_row_layout, container,false);
	}

	
	public String getVaccineName()
	{
		return vaccineName;
	}


	public void setVaccineName(String vaccineName)
	{
		this.vaccineName = vaccineName;
	}


	public Date getVaccinationDate()
	{
		return vaccinationDate;
	}


	public void setVaccinationDate(Date vaccinationDate)
	{
		this.vaccinationDate = vaccinationDate;
	}


	public int getImageId()
	{
		return imageId;
	}
	
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
    	super.onActivityCreated(savedInstanceState);
    	
    	Bundle data = getArguments();
		
		// Get the views
		txtVwCentre = (TextView)getView().findViewById(R.id.txtVwCentre);
		txtVwVaccineName = (TextView)getView().findViewById(R.id.txtVwVaccine);
		Integer id = (Integer)data.getInt("imageid");
		
		if(txtVwCentre != null)
		{
			txtVwCentre.setText(data.getString("centre"));
		}
		else
		{
			txtVwCentre.setText("nocentre");
		}
		
		if(txtVwVaccineName  != null)
		{
			txtVwVaccineName .setText(data.getString("vaccinename"));
			 Drawable img = getResources().getDrawable(id.intValue());
			 if(img !=null)
			 {
				 //set the image
				 txtVwVaccineName .setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
				 txtVwVaccineName .setCompoundDrawablePadding(5);
				 txtVwVaccineName .setPadding(5, 0, 0, 0);
			 }
		}
		else
		{
			txtVwVaccineName .setText("nocentre");
		}		
    }


	public void setImageId(int imageId)
	{
		try
		{
			TextView vaccineName = (TextView)getView().findViewById(R.id.txtVwVaccine);
			if(vaccineName != null)
			{
				 Drawable img = getResources().getDrawable(imageId);
				 if(img !=null)
				 {
					 //set the image
					 vaccineName.setText(".");
					 vaccineName.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
					 vaccineName.setCompoundDrawablePadding(5);
					 vaccineName.setPadding(5, 0, 0, 0);
				 }
			}
			this.imageId = imageId;
		}
		catch (NotFoundException e)
		{
			Log.e(TAG_VACCINE_ROW, "Error setting image:" + e.getMessage());
		}
		catch(Exception ex)
		{
			Log.e(TAG_VACCINE_ROW, "Error setting image:" + ex.getMessage());
		}	
	}


	public String getCentreName()
	{
		return centreName;
	}


	public void setCentreName(String centreName)
	{
		this.centreName = centreName;
	}


	public int getCentreId()
	{
		return centreId;
	}


	public void setCentreId(int centreId)
	{
		this.centreId = centreId;
	}	
	
}
