package org.ird.unfepi.web.validator;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Storekeeper;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StorekeeperValidator  implements Validator{

	public boolean supports(Class clazz) {
		return Storekeeper.class.equals(clazz);
	}

	public void validate(Object command, Errors error) {}
	
	public void validateRegistration(String programId, String birthdateOrAge, String ageYears, Storekeeper stork, Errors error){
		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateStorekeeperRegistrationForm(DataEntrySource.WEB, programId, stork, birthdateOrAge, ageYears, null, error, sc, false);
		}
		finally{
			sc.closeSession();
		}
	}
	
	public void validateStorekeeperCredentials(String loginIdGiven, String passwordGiven, String passwordConfirm, Errors errors){
		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateLoginCredentials(DataEntrySource.WEB, loginIdGiven, passwordGiven, passwordConfirm, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
	}
}
