package org.ird.android.epi.fragments;

import org.ird.android.epi.R;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NextVaccinationFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.next_vaccine_row_layout, container,false);
	}
	
	public NextVaccinationFragment()
	{
		
	}
}
