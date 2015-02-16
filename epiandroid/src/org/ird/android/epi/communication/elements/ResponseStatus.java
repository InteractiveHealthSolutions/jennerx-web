package org.ird.android.epi.communication.elements;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.R;

public enum ResponseStatus
{
	// public static final int INVALID = 0;
	// public static final int STATUS_SUCCESS= 1;
	// public static final int STATUS_FAILURE= 2;
	// public static final int STATUS_DATA_ERROR= 3;
	// public static final int STATUS_INTERNAL_ERROR= 4;
	// public static final int STATUS_INCORRECT_DATA_FORMAT_ERROR= 5;
	// public static final int STATUS_UNRECOGNISED_VERSION= 6;
	// public static final int STATUS_INCORRECT_CREDENTIALS= 7;

	/* Following are errors related to DB and server
	 * so their status code will be
	 * more relevant as status message */
	
	INVALID(0, "0"),
	STATUS_SUCCESS(1, "1"),
	STATUS_FAILURE(2, "2"),
	STATUS_DATA_ERROR(3, "3"),
	STATUS_INTERNAL_ERROR(4, "4"),
	STATUS_INCORRECT_DATA_FORMAT_ERROR(5, "5"),

	/* Following are errors due to old/invalid version application
	 * or wrong credentials */

	STATUS_UNRECOGNISED_VERSION(6, "Application ka version ghalat hai"),
	STATUS_INCORRECT_CREDENTIALS(7, "Username ya Password durust nahee hai"),
	STATUS_OLD_VERSION(8, "Application ka version purana hai");


	private final Integer id;
	private final String message;

	ResponseStatus(Integer id, String message)
	{
		this.id = id;
		this.message = message;
	}

	public Integer getId()
	{
		return this.id;
	}

	public String getMessage()
	{
		return message;
	}
}
