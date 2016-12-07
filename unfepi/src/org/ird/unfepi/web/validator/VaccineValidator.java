package org.ird.unfepi.web.validator;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.ird.unfepi.web.validator.ValidatorOutput.ValidatorStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class VaccineValidator implements Validator {

    public boolean supports(Class clazz) {
        return Vaccine.class.equals(clazz);
    }
    
    @Override
	public void validate(Object command, Errors error) {
		// TODO Auto-generated method stub
		
	}

	public void validate(Object command, Errors error, boolean isNew) {
		Vaccine v = (Vaccine) command;
		ServiceContext sc = Context.getServices();
		
		
		if (StringUtils.isEmptyOrWhitespaceOnly(v.getName()) || !DataValidation.validate("[a-zA-Z0-9_-]{3,15}", v.getName())) {
			error.rejectValue("name" , "" , ErrorMessages.VACCINE_NAME_INVALID);
		}
		if (StringUtils.isEmptyOrWhitespaceOnly(v.getShortName()) || !DataValidation.validate("[a-zA-Z0-9_-]{3,15}", v.getShortName())) {
			error.rejectValue("shortName" , "" , ErrorMessages.VACCINE_SHORTNAME_INVALID);
		}
		if(v.getFullName() != null && v.getFullName().length() > 0){
			if (!DataValidation.validate("[a-zA-Z0-9\\s_-]{3,30}", v.getFullName())) {
				error.rejectValue("fullName" , "" , ErrorMessages.VACCINE_FULLNAME_INVALID);
			}
		}
		
		if (isNew) {
			String q = "select count(*) from vaccine where name = '" +  v.getName() + "' ";
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				error.rejectValue("name", "", "A vaccine with given name already exists");
			}
		}
		
	}
}
