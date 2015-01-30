package org.ird.android.epi.validation;

public class ValidatorResult
{
	private Boolean isValid;
	private String message;
	private boolean isDismissable;
	
	public Boolean isValid()
	{
		return isValid;
	}
	public void setIsValid(Boolean result)
	{
		this.isValid = result;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public boolean isDismissable()
	{
		return isDismissable;
	}
	public void setDismissable(boolean isDismissable)
	{
		this.isDismissable = isDismissable;
	}
		
	
}
