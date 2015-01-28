package org.ird.android.epi;

import java.util.List;
import java.util.Map;

import org.ird.android.epi.validation.ValidatorResult;

public interface IBaseForm {
	
	/**
	 * Method in which the form's data is sent to the server
	 * @param Map A map of values of filled fields in a key, value pairs
	 */
	public void sendToServer(Map params);
	
	/**
	 * Method for client-side validation for filled fields
	 * before the form is sent to the server 
	 * @return Boolean indicating the validation was passed or not
	 */
	public List<ValidatorResult> validate();

	public Map createPayload();
	
	public void handleException(Exception ex);
}
