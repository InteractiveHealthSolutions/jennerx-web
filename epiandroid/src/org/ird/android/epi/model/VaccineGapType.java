package org.ird.android.epi.model;

public class VaccineGapType
{
	private Integer id;
	private String name;

	public VaccineGapType(Integer id, String name)
	{
		this.id = id;
		this.name = name;
	}

	/**
	 * Use this constructor for saving VaccineGap into sqlite DB
	 * 
	 * @param id
	 */
	public VaccineGapType(Integer id)
	{
		this.id = id;
	}


	public Integer getId()
	{
		return id;
	}

	// public void setId(Integer id)
	// {
	// this.id = id;
	// }

	public String getName()
	{
		return name;
	}

	// public void setName(String name)
	// {
	// this.name = name;
	// }
}
