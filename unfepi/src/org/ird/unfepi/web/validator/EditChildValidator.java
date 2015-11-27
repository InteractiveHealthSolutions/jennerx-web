package org.ird.unfepi.web.validator;

import java.util.List;

import org.ird.unfepi.beans.ChildDataBean;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EditChildValidator implements Validator{
	
	public boolean supports(Class cls) {
		return ChildDataBean.class.equals(cls);
	}
	
	public void validate(Object command, Errors error) 
	{
		ChildDataBean ew = (ChildDataBean)command;
		
		ServiceContext sc = Context.getServices();
		try{
			//List<Vaccination> enrVacc = sc.getVaccinationService().findByCriteria(ew.getChild().getMappedId(), ew.getChild().getEnrollmentVaccineId(), VACCINATION_STATUS.VACCINATED, 0, 2, true, null);
//TODO			ValidatorUtils.validateBiographics(DataEntrySource.WEB, ew.getChild(), enrVacc.get(0), null, error, sc, true);
		}
		finally{
			sc.closeSession();
		}
	}
}
