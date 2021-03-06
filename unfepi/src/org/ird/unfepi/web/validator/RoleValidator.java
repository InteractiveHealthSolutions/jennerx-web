package org.ird.unfepi.web.validator;

import org.ird.unfepi.model.Role;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class RoleValidator implements Validator{
	
	public boolean supports(Class cls) {
		return Role.class.equals(cls);
	}

	public void validate(Object command, Errors error) {
		
		Role role=(Role)command;
		
		if( StringUtils.isEmptyOrWhitespaceOnly(role.getRolename()) ){
			error.rejectValue("rolename" , "error.role.empty-name" , "Role name must be provided");
		}
		else if(!DataValidation.validate(REG_EX.WORD, role.getRolename())){
			error.rejectValue("rolename" , "error.role.invalid-name" , "Role name is invalid");
		}
		else if(role.getRolename().trim().equalsIgnoreCase("admin")
				|| role.getRolename().trim().equalsIgnoreCase("administrator")){
			error.rejectValue("rolename" , "" , "No new role can be defined or edited with name admin or administrator.");
		}
 	}
}
