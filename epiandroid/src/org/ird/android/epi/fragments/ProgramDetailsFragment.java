package org.ird.android.epi.fragments;

import org.ird.android.epi.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgramDetailsFragment extends Fragment
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.program_details_layout, container,false);
		
	}

	public ProgramDetailsFragment()
	{
		super();
	}
}
