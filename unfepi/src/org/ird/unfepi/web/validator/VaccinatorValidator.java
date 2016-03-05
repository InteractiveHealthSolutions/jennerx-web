package org.ird.unfepi.web.validator;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Vaccinator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VaccinatorValidator implements Validator{

	public boolean supports(Class clazz) {
		return Vaccinator.class.equals(clazz);
	}

	@Override
	public void validate (Object arg0, Errors arg1) {
		
	}
	
	public void validateRegistration(String programId, String birthdateOrAge, String ageYears, Vaccinator vaccinator, Errors error){
		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateVaccinatorRegistrationForm(DataEntrySource.WEB, programId, vaccinator, birthdateOrAge, ageYears, null, error, sc, false);
		}
		finally{
			sc.closeSession();
		}
	}
	
	public void validateVaccinatorCredentials(String loginIdGiven, String passwordGiven, String passwordConfirm, Errors errors){
		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateLoginCredentials(DataEntrySource.WEB, loginIdGiven, passwordGiven, passwordConfirm, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
	}
}
