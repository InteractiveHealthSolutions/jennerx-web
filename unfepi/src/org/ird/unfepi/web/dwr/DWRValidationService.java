package org.ird.unfepi.web.dwr;

import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.web.validator.ValidatorOutput;
import org.ird.unfepi.web.validator.ValidatorOutput.ValidatorStatus;
import org.ird.unfepi.web.validator.ValidatorUtils;

public class DWRValidationService {
	public String validateMobileNumber(String number){
		ValidatorOutput op = ValidatorUtils.validateContactNumber(number, ContactTeleLineType.MOBILE);
		return op.STATUS().equals(ValidatorStatus.OK)?"OK":op.MESSAGE();
	}
	
	/*public String validateEpiNumber(String epiNumber, int vaccinationCenter, String programId){
		
	}*/
}
