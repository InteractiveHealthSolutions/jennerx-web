/**
 * 
 */
package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.beans.EnrollmentWrapperWomen;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Safwan
 *
 */
public class AddWomenValidator implements Validator {

	@Override
	public boolean supports(Class cls) {
		return EnrollmentWrapperWomen.class.equals(cls);
	}

	@Override
	public void validate(Object command, Errors errors) {
		
	}
	
	public void validateEnrollment(EnrollmentWrapperWomen ewr, Errors errors) {
		ServiceContext sc = Context.getServices();
		try {
			ValidatorUtils.validateWomenEnrollmentForm(DataEntrySource.WEB, ewr.getProjectId(), ewr.getWomen(), ewr.getBirthdateOrAge(), 
					ewr.getwomenagey(), ewr.getwomenagem(), ewr.getwomenagew(), ewr.getwomenaged(), ewr.getAddress(), ewr.getCenterVisit(), 
					 null, errors, sc);
		} finally {
			sc.closeSession();
		}
	}

}
