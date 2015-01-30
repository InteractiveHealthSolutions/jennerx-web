package org.ird.android.epi.model;

public class Location
{
	int id;
	String fullName;
	int parentId;
	int locationType;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getFullName()
	{
		return fullName;
	}
	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}
	public int getParentId()
	{
		return parentId;
	}
	public void setParentId(int parentId)
	{
		this.parentId = parentId;
	}
	
	public int getLocationType()
	{
		return locationType;
	}
	public void setLocationType(int locationType)
	{
		this.locationType = locationType;
	}
	@Override
	public String toString()
	{
		return fullName;
	}
}
