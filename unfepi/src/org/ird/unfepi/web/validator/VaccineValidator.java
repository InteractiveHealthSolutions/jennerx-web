package org.ird.unfepi.web.validator;

import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VaccineValidator implements Validator {

    public boolean supports(Class clazz) {
        return Vaccine.class.equals(clazz);
    }

	public void validate(Object command, Errors error) {
		Vaccine v = (Vaccine) command;
		
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, v.getName())){
			error.rejectValue("name" , "" , ErrorMessages.VACCINE_NAME_INVALID);
		}
		
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, v.getShortName(), 3 , 10)){
			error.rejectValue("shortName" , "" , ErrorMessages.VACCINE_SHORTNAME_INVALID);
		}
		
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, v.getFullName(), 3 , 30)){
			error.rejectValue("fullName" , "" , ErrorMessages.VACCINE_FULLNAME_INVALID);
		}
		
		
	}
}
