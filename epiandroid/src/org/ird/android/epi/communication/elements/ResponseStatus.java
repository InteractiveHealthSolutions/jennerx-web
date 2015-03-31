package org.ird.android.epi.communication.elements;

public enum ResponseStatus
{
	/*
	 * Following are errors related to DB and server
	 * so their status code will be
	 * more relevant as status message
	 */

	STATUS_ID_CHILD_NOT_EXIST(0, "Child ID does not exist", ""),
	STATUS_SUCCESS(1, "1", ""),
	STATUS_FAILURE(2, "2", ""),
	STATUS_DATA_ERROR(3, "3", ""),

	/* Error occurs during saving values in DB
	 * This error is also occurred due to Login Id missing (a rare case) 
	 */

	STATUS_INTERNAL_ERROR(4, "4", ""),

	STATUS_INCORRECT_DATA_FORMAT_ERROR(5, "5", ""),

	/*
	 * Following are errors due to old/invalid version application
	 * or wrong credentials
	 */

	STATUS_UNRECOGNISED_VERSION(6, "Application ka version ghalat hai", ""),
	STATUS_INCORRECT_CREDENTIALS(7, "Username ya Password durust nahee hai", ""),
	STATUS_OLD_VERSION(8, "Application ka version purana hai", "");

	private final Integer id;
	private final String message;
	private final String solution;

	/**
	 * @param id Code of error
	 * @param message Message to show
	 * @param solution Solution to show if present
	 */
	ResponseStatus(Integer id, String message, String solution)
	{
		this.id = id;
		this.message = message;
		this.solution = solution;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String getMessage()
	{
		return message;
	}

	public String getSolution()
	{
		return solution;
	}
}
