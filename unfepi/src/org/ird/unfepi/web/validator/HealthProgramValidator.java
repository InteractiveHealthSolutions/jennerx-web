package org.ird.unfepi.web.validator;

import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.HealthProgram;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class HealthProgramValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return HealthProgram.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors error) {
		HealthProgram hp = (HealthProgram)obj;
		
		if(hp.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(hp.getName())){
			error.rejectValue("name", "", "invalid or empty program name");
		}
		
		if (hp.getEnrollmentLimit() > Integer.MAX_VALUE || hp.getEnrollmentLimit() < Integer.MIN_VALUE) {
			error.rejectValue("enrollmentLimit", "", "invalid enrollmentLimit");
		}
	}
	
	public void validateHealthProgramVaccinationCenters(HealthProgram hp, String[] centersId, Errors error, boolean isNew){
		
		ValidatorUtils.validateHealthProgram(DataEntrySource.WEB, hp, centersId, error, isNew);
		
	}

}
