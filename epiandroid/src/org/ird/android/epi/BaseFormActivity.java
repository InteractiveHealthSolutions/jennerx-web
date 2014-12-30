package org.ird.android.epi;

import java.util.Map;

public abstract class BaseFormActivity {
	
	/**
	 * Method in which the form's data is sent to the server
	 * @param Map A map of values of filled fields in a key, value pairs
	 */
	public void sendToServer(Map params)
	{
		
	}
	
	/**
	 * Method for client-side validation for filled fields
	 * before the form is sent to the server 
	 * @return Boolean indicating the validation was passed or not
	 */
	public boolean validate()
	{
		return false;
	}	

}
