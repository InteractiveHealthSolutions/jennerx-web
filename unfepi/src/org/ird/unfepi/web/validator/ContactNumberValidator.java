package org.ird.unfepi.web.validator;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ContactNumberValidator implements Validator{
	
	public boolean supports(Class cls) {
		return ContactNumber.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{
		ContactNumber con = (ContactNumber) command;

		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateContactNumberForm(DataEntrySource.WEB, con, null, error, sc);
		}
		finally {
			sc.closeSession();
		}
	}
}

