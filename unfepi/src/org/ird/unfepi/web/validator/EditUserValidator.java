package org.ird.unfepi.web.validator;

import org.ird.unfepi.model.User;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class EditUserValidator implements Validator{

	public boolean supports(Class cls) {
		return User.class.equals(cls);
	}

	public void validate( Object command , Errors error ) {

		User user = (User) command;
		if (StringUtils.isEmptyOrWhitespaceOnly( user.getUsername() )
				|| user.getUsername().contains( " " )) {
			error.rejectValue( "username" , "error.user.user-name-missing" ,
					"username cannot be empty or contain spaces" );
		}
		else {
			if (user.getUsername().length() < 4) {
				error.rejectValue( "username" ,
						"error.user.user-name-invalid-length" ,
						"username must be minimum 4 characters" );
			}
			if (!DataValidation.validate( REG_EX.WORD , user.getUsername() )) {
				error.rejectValue( "username" , "error.user.user-name-invalid" ,
						"username can only contain _,digits and alphas" );
			}
		}
		if (StringUtils.isEmptyOrWhitespaceOnly( user.getFirstName() )) {
			error.rejectValue( "firstName" , "error.user.first-name-empty" ,
					"user first name must be specified" );
		}
		else {
			if (!DataValidation.validate( REG_EX.ALPHA , user.getFirstName() )) {
				error.rejectValue( "firstName" ,
						"error.user.first-name-invalid" ,
						"user first name is invalid" );
			}
		}
		if (StringUtils.isEmptyOrWhitespaceOnly( user.getMiddleName() )) {
		}
		else {
			if (!DataValidation.validate( REG_EX.ALPHA , user.getMiddleName() )) {
				error.rejectValue( "middleName" ,
						"error.user.middle-name-invalid" ,
						"user middle name is invalid" );
			}
		}
		if (StringUtils.isEmptyOrWhitespaceOnly( user.getLastName() )) {
		}
		else {
			if (!DataValidation.validate( REG_EX.ALPHA , user.getLastName() )) {
				error.rejectValue( "lastName" , "error.user.last-name-invalid" ,
						"user last name is invalid" );
			}
		}
		if (StringUtils.isEmptyOrWhitespaceOnly( user.getEmail() )) {
			error.rejectValue( "email" , "error.user.email-empty" ,
					"email required" );
		}
		else {
			if (!DataValidation.validate( REG_EX.EMAIL , user.getEmail() )) {
				error.rejectValue( "email" , "error.user.email-invalid" ,
						"invalid email" );
			}
		}
	}
}
