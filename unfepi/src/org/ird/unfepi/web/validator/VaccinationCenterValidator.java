package org.ird.unfepi.web.validator;

import java.util.Date;
import java.util.Map;

import org.ird.unfepi.beans.VCenterRegistrationWrapper;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class VaccinationCenterValidator implements Validator{

	@Override
	public boolean supports(Class arg0) {
		return VCenterRegistrationWrapper.class.equals(arg0);
	}

	@Override
	public void validate(Object command, Errors error) {
		VCenterRegistrationWrapper vaccw = (VCenterRegistrationWrapper) command;
		
		if(vaccw.getVaccinationCenter().getDateRegistered() == null){
			error.rejectValue("vaccinationCenter.dateRegistered", "", ErrorMessages.REGISTRATION_DATE_MISSING);
		}
		else if(vaccw.getVaccinationCenter().getDateRegistered().after(new Date())){
			error.rejectValue("vaccinationCenter.dateRegistered", "", ErrorMessages.REGISTRATION_DATE_IN_FUTURE);
		}
    	
		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, vaccw.getVaccinationCenter().getName())){
			error.rejectValue("vaccinationCenter.name" , "" , ErrorMessages.CENTER_NAME_INVALID);
		}
		else {
			ServiceContext sc = Context.getServices();
			try {
				VaccinationCenter evc = sc.getVaccinationService().findVaccinationCenterByName(vaccw.getVaccinationCenter().getName().trim(), true, null);
				if(evc != null && evc.getMappedId() != vaccw.getVaccinationCenter().getMappedId()){
					error.rejectValue("vaccinationCenter.name" , "" , "Center with given name already exists");
				}
			} catch (Exception e) {
				error.rejectValue("vaccinationCenter.name" , "" , e.getMessage());
			}
			finally{
				sc.closeSession();
			}
		}

		if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, vaccw.getVaccinationCenter().getFullName())){
			error.rejectValue("vaccinationCenter.fullName" , "" , ErrorMessages.CENTER_FULLNAME_INVALID);
		}
		
//		int i = 0;
//		for (Map<String, Object> vdml : vaccw.getVaccineDayMapList()) {
//			String[] strarr = (String[]) vdml.get("daylist");
//			boolean anydayspecified = false;
//			for (String string : strarr) {
//				if(!StringUtils.isEmptyOrWhitespaceOnly(string)){
//					anydayspecified = true;
//					break;
//				}
//			}
//			
//			if(!anydayspecified){
//				error.reject("nocode", null, "\nNo day specified for vaccine "+((Vaccine)vdml.get("vaccine")).getName());
//			}
//			
//			i++;
//		}
	}
}
