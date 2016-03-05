package org.ird.unfepi.web.validator;

import org.ird.unfepi.beans.Credentials;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class LoginValidator implements Validator {

    public boolean supports(Class clazz) {
        return Credentials.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
    	
        Credentials credentials = (Credentials) obj;
        if (StringUtils.isEmptyOrWhitespaceOnly(credentials.getUsername())) {
            errors.rejectValue("username", "error.login.empty-user","Incorrect Username.");
        } 
        else {
            if (StringUtils.isEmptyOrWhitespaceOnly(credentials.getPassword())) {
                errors.rejectValue("password","error.login.empty-pass", "Incorrect Password.");
            }
        }
     }
}