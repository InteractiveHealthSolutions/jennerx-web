package org.ird.android.epi.dal;

import java.util.List;

import ord.ird.android.epi.db.CentreDBHelper;
import ord.ird.android.epi.db.LocationDbHelper;

import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Location;

import android.content.Context;

public class CentreService
{
	private static List<Centre>centres=null;
	
	public static List<Centre> getAllLocations(Context cxt)
	{
		if(centres == null || centres.size()==0)
		{
			CentreDBHelper helper = new CentreDBHelper(cxt);
			centres = helper.getCentres();
		}
		return centres;
	}
	
	public static Centre getCentreById(Context cxt, int id)
	{
		getAllLocations(cxt);
		for(Centre c : centres)
		{
			if(c.getCentreId() ==  id)
				return c;
		}
		return null;
	}
	
	public static Centre getCentreByName(Context cxt, String name)
	{
		getAllLocations(cxt);
		for(Centre c : centres)
		{
			if(c.getName().contains(name))
				return c;
		}
		return null;
	}
}
