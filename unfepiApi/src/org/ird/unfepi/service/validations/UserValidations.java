/*
 * 
 */
package org.ird.unfepi.service.validations;

import org.ird.unfepi.model.User;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;


// TODO: Auto-generated Javadoc
/**
 * The Class UserValidations.
 */
public class UserValidations {

	/**
	 * Validate user.
	 *
	 * @param user the user
	 * @throws UserServiceException the user service exception
	 */
	public static void validateUser(User user) throws UserServiceException{
		
			if(user.getUsername()==null || user.getUsername().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USERNAME_EMPTY);
			}
			/*if(user.getFirstName()==null || user.getFirstName().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USER_FIRST_NAME_MISSING);
			}*/
			if(user.getPassword()==null || user.getPassword().compareTo("")==0){
				throw new UserServiceException(UserServiceException.USER_PASSWORD_MISSING);
			}
	}
	
	/**
	 * Validate password.
	 *
	 * @param pwd the pwd
	 * @return true, if successful
	 */
	public static boolean validatePassword(String pwd){
		if(pwd==null || pwd.trim().compareTo("")==0 || pwd.length()<6){
			return false;
		}
		return DataValidation.validate(REG_EX.PASSWORD,pwd);
	}
	
}
