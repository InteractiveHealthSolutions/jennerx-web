package org.ird.android.epi.dal;

import java.util.ArrayList;
import java.util.List;
import ord.ird.android.epi.db.LocationDbHelper;
import org.ird.android.epi.model.Location;
import org.ird.android.epi.model.LocationType;
import android.content.Context;

public class LocationService
{
	private static 	List<Location>		locations		=	null;
	private static  List<LocationType>	locationTypes	=	null;
	
	private static	List<Location>		cities			=	null;
	private static	List<Location>		towns			=	null;
	private static	List<Location>		ucs				=	null;
	
	private static 	int					cityId			=	-1	;
	private static 	int					ucId			=	-1	;
	private static 	int					townId			=	-1	;
	
	public static List<Location> getAllLocations(Context cxt)
	{
		if(locations == null || locations.size()==0)
		{
			LocationDbHelper helper = new LocationDbHelper(cxt);
			locations = helper.getAllLocations();
			
		}
		return locations;
	}
	
	public static List<LocationType> getAllLocationTypes(Context cxt)
	{
		if(locationTypes == null || locationTypes.size() ==0)
		{
			LocationDbHelper helper = new LocationDbHelper(cxt);
			locationTypes = helper.getAllLocationTypes();
		}
		return locationTypes;
	}
	
	public static Location getLocationById(Context cxt, int id)
	{
		Location location=null;
		getAllLocations(cxt);
		for(Location l: locations)
		{
			if(l.getId() == id)
				return l;
		}
		return location;
	}
	
	public static List<Location> getAllCity(Context cxt)
	{
		getAllLocations(cxt);
		getCityLocationType(cxt);
		
		if(cities == null || cities.size()==0)
		{
			cities = new ArrayList<Location>();
			for(Location l: locations)
			{
				//add all cities to the list if their parent type is a citylocation type
				if(l.getLocationType() == cityId)
				{
					cities.add(l);
				}
			}
		}
		
		return cities;
	}
	
	public static List<Location> getAllTown(Context cxt)
	{
		getAllLocations(cxt);
		getTownId(cxt);
				
		if(towns == null || towns.size() ==0)
		{
			towns=new ArrayList<Location>();
			
			for(Location l: locations)
			{
				//add all cities to the list if their parent type is a citylocation type
				if(l.getLocationType() == townId)
				{
					towns.add(l);
				}
			}
		}
		
		return towns;
	}
	
	public static List<Location> getAllUcs(Context cxt)
	{
		getAllLocations(cxt);
		getUcId(cxt);
				
		if(ucs == null || ucs.size() ==0)
		{
			ucs=new ArrayList<Location>();
			
			for(Location l: locations)
			{
				//add all cities to the list if their parent type is a citylocation type
				if(l.getLocationType() == ucId)
				{
					ucs.add(l);
				}
			}
		}
		return ucs;
	}
	
	private static void getCityLocationType(Context cxt)
	{
		if(cityId == -1)
		{
			List<LocationType>locationTypes =	getAllLocationTypes(cxt);
			for(LocationType locType : locationTypes)
			{
				if(locType.getTypeName().equalsIgnoreCase("city"))
				{
					cityId=locType.getLocationTypeId();
					return;
				}
			}
		}
	}
	
	private static void getTownId(Context cxt)
	{
		if(townId == -1)
		{
			List<LocationType>locationTypes =	getAllLocationTypes(cxt);
			for(LocationType locType : locationTypes)
			{
				if(locType.getTypeName().equalsIgnoreCase("town"))
				{
					townId=locType.getLocationTypeId();
					return;
				}
			}
		}		
	}
	
	
	private static void getUcId(Context cxt)
	{
		if(ucId == -1)
		{
			List<LocationType>locationTypes =	getAllLocationTypes(cxt);
			for(LocationType locType : locationTypes)
			{
				if(locType.getTypeName().equalsIgnoreCase("uc"))
				{
					ucId=locType.getLocationTypeId();
					return;
				}
			}
		}	
	}
	
	public static List<Location> getAllChildren(Context cxt, Location parent)
	{
		List<Location> children = new ArrayList<Location>();
		
		if(parent==null)
			return null;
		
		getAllLocations(cxt);
		for(Location l : locations)
		{
			if(l.getParentId() == parent.getId())
			{
				children.add(l);
			}
		}
		return children;
	}
}
