package org.ird.unfepi.web.validator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VaccinationValidator implements Validator {

    public boolean supports(Class clazz) {
        return VaccinationCenterVisit.class.equals(clazz);
    }

    public void validate(Object obj, Errors errors) {
    	
    }
    
    public void validateVaccinationForm(VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, Errors errors, HttpServletRequest request){
    	ServiceContext sc = Context.getServices();
		try{
			ValidatorUtils.validateFollowupForm(DataEntrySource.WEB, vaccineSchedule, centerVisit, null, errors, sc);
		}
		finally{
			sc.closeSession();
		}
    }
}
