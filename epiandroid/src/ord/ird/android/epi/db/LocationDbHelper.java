package ord.ird.android.epi.db;

import java.util.ArrayList;
import java.util.List;

import org.ird.android.epi.model.Location;
import org.ird.android.epi.model.LocationType;

import android.content.ContentValues;
import android.content.Context;

public class LocationDbHelper
{
	private Context context=null;	
	private DatabaseUtil dbUtil=null;
			
	private static final String TABLE_LOCATION="location";
	private static final String FIELD_LOCATION_ID="locationId";
	private static final String FIELD_LOCATION_NAME="name";
	private static final String FIELD_PARENT_ID="parent";
	
	
	private static final String TABLE_LOCATION_TYPE="locationType";
	private static final String FIELD_TYPE_ID="typeId";
	private static final String FIELD_TYPE_NAME="typeName";
	
	
	public LocationDbHelper(Context cxt)
	{
		this.context = cxt;
		if(context!=null && dbUtil == null)
		{
			dbUtil = new DatabaseUtil(context);
		}
	}
	
	public boolean saveLocations(List<Location> locations)
	{
		boolean isSaved=false;		
		ContentValues values;
		
		boolean[] flags=new boolean[locations.size()];
		int flagCounter=0;
		
		for(Location l : locations)
		{
			values = new ContentValues();
			
			values.put(FIELD_LOCATION_ID	, l.getId());
			values.put(FIELD_PARENT_ID		, l.getParentId());
			values.put(FIELD_LOCATION_NAME	, l.getFullName());
			values.put("locationType"		, l.getLocationType());
			
			flags[flagCounter] = dbUtil.insert(TABLE_LOCATION, values);
			flagCounter++;
		}
		
		isSaved = checkAllSaved(flags);
		return isSaved;
	}
	
	public boolean saveLocationTypes(List<LocationType> locTypes)
	{
		boolean isSaved=false;
		ContentValues values;
		
		boolean[] flags=new boolean[locTypes.size()];
		int flagCounter=0;
		
		for(LocationType type : locTypes)
		{
			values = new ContentValues();
			values.put(FIELD_TYPE_ID, type.getLocationTypeId());
			values.put(FIELD_TYPE_NAME, type.getTypeName());
			
			flags[flagCounter] = dbUtil.insert(TABLE_LOCATION_TYPE, values);
			flagCounter++;
		}
		
		isSaved = checkAllSaved(flags);
		return isSaved;
	}
	
	private boolean checkAllSaved(boolean[] flags)
	{
		for(boolean b : flags)
		{
			if(!b)
				return false;
		}
		return true;
	}
	
	
	public List<Location> getAllLocations()
	{
		List<Location> locations = new ArrayList<Location>();
		Location temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_LOCATION);
		
		for(int i=0; i <values.length;i ++)
		{
			temp=new Location();
			
			temp.setId(Integer.valueOf(values[i][0]));
			temp.setFullName(values[i][1]);
			temp.setParentId(Integer.valueOf(values[i][2]));
			temp.setLocationType(Integer.valueOf(values[i][3]));
			
			locations.add(temp);
		}
		
		return locations;
	}
	
	public List<LocationType> getAllLocationTypes()
	{
		DatabaseUtil dbUtil = new DatabaseUtil(context);
		List<LocationType> locTypes = new ArrayList<LocationType>();
		LocationType temp;
		String[][] values = dbUtil.getTableData("select * from " + TABLE_LOCATION_TYPE);
		
		for(int i=0; i <values.length;i ++)
		{
			temp=new LocationType();
			temp.setTypeName(values[i][0]);
			temp.setLocationTypeId(Integer.valueOf(values[i][1]));
			
			locTypes.add(temp);
		}
		
		return locTypes;
	}
}
