package org.ird.android.epi.model;

public class VaccineGap {
	
	int priority;	
	String gapType;	
	String timeUnit;	
	int value;	
	Vaccine vacine;
	boolean isMandatory;
	
	public VaccineGap(Vaccine vaccine, int priority, String gapType,
			String timeUnit, int value, boolean isMandatory)
	{
		this.vacine = vaccine;
		this.priority = priority;
		this.gapType = gapType;
		this.timeUnit = timeUnit;
		this.value = value;		
		this.isMandatory = isMandatory;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public String getGapType()
	{
		return gapType;
	}

	public void setGapType(String gapType)
	{
		this.gapType = gapType;
	}

	public String getTimeUnit()
	{
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit)
	{
		this.timeUnit = timeUnit;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public Vaccine getVacine()
	{
		return vacine;
	}

	public void setVacine(Vaccine vacine)
	{
		this.vacine = vacine;
	}

	public boolean isMandatory()
	{
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory)
	{
		this.isMandatory = isMandatory;
	}
	
	
}
