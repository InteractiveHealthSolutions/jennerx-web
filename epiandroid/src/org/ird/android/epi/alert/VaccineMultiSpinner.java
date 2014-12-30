package org.ird.android.epi.alert;

import java.util.ArrayList;
import java.util.Arrays;

import org.ird.android.epi.R;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

public class VaccineMultiSpinner extends SelvaMultiSpinner
{	
	String vaccines[]=null;
	private static final String DEFAULT_VACCINES_TEXT="Select Vaccines";
	public VaccineMultiSpinner(Context context)
	{
		super(context);
		if(vaccines==null)
        	vaccines = getResources().getStringArray(R.array.all_vaccines);
		super.setItems(Arrays.asList(vaccines), DEFAULT_VACCINES_TEXT,(ISelvaMultiSpinnerListener) this);
	}
	
	public VaccineMultiSpinner (Context arg0, AttributeSet arg1)
    {
        super(arg0, arg1);
        if(vaccines==null)
        	vaccines = getResources().getStringArray(R.array.all_vaccines);
        super.setItems(Arrays.asList(vaccines), DEFAULT_VACCINES_TEXT,(ISelvaMultiSpinnerListener) this);
    }

    public VaccineMultiSpinner (Context arg0, AttributeSet arg1, int arg2) 
    {
        super(arg0, arg1, arg2);
        if(vaccines==null)
        	vaccines = getResources().getStringArray(R.array.all_vaccines);
        super.setItems(Arrays.asList(vaccines), DEFAULT_VACCINES_TEXT,(ISelvaMultiSpinnerListener) this);
    }
}
