package org.ird.unfepi.web.validator;

import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LocationValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		return Location.class.equals(arg0);
	}

	@Override
	public void validate(Object command, Errors error) {
		Location loc = (Location) command;
		
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, loc.getName())){
			error.rejectValue("name" , "" , ErrorMessages.LOCATION_NAME_INVALID);
		}

		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, loc.getFullName())){
			error.rejectValue("vaccinationCenter.fullName" , "" , ErrorMessages.LOCATION_FULLNAME_INVALID);
		}
	}
}
