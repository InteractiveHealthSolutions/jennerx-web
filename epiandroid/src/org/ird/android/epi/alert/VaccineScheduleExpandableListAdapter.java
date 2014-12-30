package org.ird.android.epi.alert;

import org.ird.android.epi.common.EpiUtils;
import org.ird.android.epi.model.Vaccine;

import org.ird.android.epi.R;
import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VaccineScheduleExpandableListAdapter extends BaseExpandableListAdapter{

	LayoutInflater inflater;
	SparseArray<Vaccine> vaccines;
	Activity act;
	public VaccineScheduleExpandableListAdapter(SparseArray<Vaccine> vaccines, Activity activity)
	{
		this.vaccines = vaccines;
		this.act = activity;
	}
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return vaccines.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		
		if(convertView == null)
			{
				this.inflater = this.act.getLayoutInflater();
				convertView = this.inflater.inflate(R.layout.vaccine_schedule_row_expanded_layout, null);
				Spinner spCentres = (Spinner)convertView.findViewById(R.id.spCentre);
				if(spCentres!=null)
				{
					String[] centres = this.act.getResources().getStringArray(R.array.array_korangi);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.act.getApplicationContext(), spCentres.getId(),centres);
					spCentres.setAdapter(adapter);
				}
						
//				Vaccine vac = (Vaccine)getGroup(groupPosition);
//				if(vac != null && convertView !=null)
//				{
//					TextView txtVw;
//					txtVw = (TextView)convertView.findViewById(id)
//				}
				
				convertView.setOnClickListener(new OnClickListener() {
				      @Override
				      public void onClick(View v) {
				    	  Toast.makeText(v.getContext(),"I am clicked",  Toast.LENGTH_SHORT).show();
				      }
				    });
				
			}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(vaccines.get(groupPosition)!=null)
			return 1;
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return vaccines.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return vaccines.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
		      View convertView, ViewGroup parent) {
		if(this.inflater == null)
		{
			this.inflater = this.act.getLayoutInflater();
		}
		
		if(convertView == null)
		{
			convertView = this.inflater.inflate(R.layout.vaccine_schedule_row_layout, null);
			Vaccine vac = vaccines.get(groupPosition);
			if(vac !=null)
			{
				TextView txtVw;
				txtVw = (TextView)convertView.findViewById(R.id.tvVaccineName);
				txtVw.setText(vac.getName());
				
				txtVw = (TextView)convertView.findViewById(R.id.tvDateGiven);
				if(vac.isGiven())
				{
					txtVw.setText("Given Date:");
					txtVw = (TextView)convertView.findViewById(R.id.tvDateActual);
					txtVw.setText( vac.getVaccinationDate().toString());
				}
				else
				{
					txtVw.setText("Due Date:");
					txtVw = (TextView)convertView.findViewById(R.id.tvDateActual);
					txtVw.setText(vac.getDueDate().toString());
				}
				
			}			
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	 @Override
	  public void onGroupCollapsed(int groupPosition) {
	    super.onGroupCollapsed(groupPosition);
	  }
	
	  @Override
	  public void onGroupExpanded(int groupPosition) {
	    super.onGroupExpanded(groupPosition);
	  }

}
