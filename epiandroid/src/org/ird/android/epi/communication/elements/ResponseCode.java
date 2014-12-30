package org.ird.android.epi.communication.elements;

public enum ResponseCode
{
	INVALID(0), 
	STATUS_SUCCESS(1),
	STATUS_FAILURE(2),
	STATUS_DATA_ERROR(3),
	STATUS_INTERNAL_ERROR(4),
	STATUS_INCORRECT_DATA_FORMAT_ERROR(5),
	STATUS_UNRECOGNISED_VERSION(6),
	STATUS_INCORRECT_CREDENTIALS(7);

	private int status;
	
	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	ResponseCode (int s)
	{
		this.status =s;
	}
}
