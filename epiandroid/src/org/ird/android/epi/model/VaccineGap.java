package org.ird.android.epi.model;

public class VaccineGap
{

	int priority;
	// int gapTypeId;
	// String gapType;
	VaccineGapType gapType;

	String timeUnit;
	int value;
	Vaccine vacine;
	boolean isMandatory;

	public VaccineGap(Vaccine vaccine, int priority, VaccineGapType gapType,
			String timeUnit, int value, boolean isMandatory)
	{
		this.vacine = vaccine;
		this.priority = priority;
		this.gapType = gapType;
		this.timeUnit = timeUnit;
		this.value = value;
		this.isMandatory = isMandatory;
	}

	/**
	 * this constructor is supposed to be used only for saving vaccineGap in sqlite DB
	 * 
	 * @param vaccine
	 * @param gapType
	 * @param timeUnit
	 * @param value
	 */
	public VaccineGap(Vaccine vaccine, VaccineGapType gapType, String timeUnit, int value)
	{
		this.vacine = vaccine;
		// this.priority = priority;
		this.gapType = gapType;
		// this.gapTypeId = gapTypeId;
		this.timeUnit = timeUnit;
		this.value = value;
		// this.isMandatory = isMandatory;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public VaccineGapType getGapType()
	{
		return gapType;
	}

	public void setGapType(VaccineGapType gapType)
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
