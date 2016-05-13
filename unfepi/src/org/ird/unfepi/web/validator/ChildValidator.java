package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.beans.EnrollmentWrapper;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ChildValidator implements Validator{
	
	public boolean supports(Class cls) {
		return EnrollmentWrapper.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{}
	
	public void validateEnrollment(EnrollmentWrapper ew, List<VaccineSchedule> vaccineSchedule, Errors error){
		ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateEnrollmentForm(DataEntrySource.WEB, ew.getChildIdentifier(), ew.getChildNamed(), ew.getChild(), 
					ew.getBirthdateOrAge(), ew.getChildagey(), ew.getChildagem(), ew.getChildagew(), ew.getChildaged(), ew.getAddress(), 
					ew.getCompleteCourseFromCenter(), ew.getCenterVisit(), vaccineSchedule, null, error, sc);
		}
		finally{
			sc.closeSession();
		}
	}
}
