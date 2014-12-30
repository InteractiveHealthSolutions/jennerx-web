package org.ird.android.epi.model;

public class LocationType
{
	String typeName;
	int locationTypeId;
	
	public String getTypeName()
	{
		return typeName;
	}
	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}
	public int getLocationTypeId()
	{
		return locationTypeId;
	}
	public void setLocationTypeId(int locationTypeId)
	{
		this.locationTypeId = locationTypeId;
	}
	
	@Override
	public String toString()
	{
		return typeName;
	}
}
