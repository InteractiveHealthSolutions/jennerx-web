package org.ird.android.epi.model;

public class Centre
{
	private int centreId;
	private String name;
	
	public int getCentreId()
	{
		return centreId;
	}
	public void setCentreId(int centreId)
	{
		this.centreId = centreId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	

}
