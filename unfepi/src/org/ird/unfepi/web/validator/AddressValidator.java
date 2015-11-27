package org.ird.unfepi.web.validator;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AddressValidator implements Validator{
	
	public boolean supports(Class cls) {
		return Address.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{
		Address add = (Address) command;
		
		ValidatorUtils.validateAddress(DataEntrySource.WEB, add, null, error, false);
	}
}
