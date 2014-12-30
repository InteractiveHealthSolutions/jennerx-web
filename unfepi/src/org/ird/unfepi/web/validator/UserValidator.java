package org.ird.unfepi.web.validator;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator{

	public boolean supports(Class cls) {
		return User.class.equals(cls);
	}

	public void validate (Object command, Errors error) {}
	
	public void validateEdit(String programId, User userUnderEdit, String userUnderEditRole, User editorUser, Role editorRole, Errors webErrors){
		ServiceContext sc = Context.getServices();
		try{
			Role userUnderEditRoleObj = userUnderEditRole == null ? null : sc.getUserService().getRole(userUnderEditRole, true, null);
			ValidatorUtils.validateUserEdit(DataEntrySource.WEB, programId, userUnderEdit, userUnderEditRoleObj , editorUser, editorRole, null, webErrors, sc);
		}
		finally {
			sc.closeSession();
		}
	}
	
	public void validateRegistration(String programId, String confirmPwd, User userUnderEdit, String userUnderEditRole, User editorUser, Role editorRole, Errors webErrors){
		ServiceContext sc = Context.getServices();
		try{
			Role userUnderEditRoleObj = userUnderEditRole == null ? null : sc.getUserService().getRole(userUnderEditRole, true, null);
			ValidatorUtils.validateUserRegistration(DataEntrySource.WEB, programId, confirmPwd, userUnderEdit, userUnderEditRoleObj , editorUser, editorRole, null, webErrors, sc);
		}
		finally {
			sc.closeSession();
		}
	}
}
