package org.ird.unfepi.web.validator;

import java.util.Date;

import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.model.Screening;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class ScreeningValidator implements Validator{

	public boolean supports(Class clazz) {
		return Screening.class.equals(clazz);
	}

	public void validate(Object command, Errors error) {
		
		Screening scr = (Screening) command;
		
		EntityValidation ev = new EntityValidation();
/*		if(scr.getIsChildHealthy() && !ev.validateMrNumber(scr.getEpiId(), scr.getScreeningDate())){
			error.rejectValue("epiId", "", ev.ERROR_MESSAGE);
		}
		else if(scr.getEpiId() != null){
			ServiceContext sc = Context.getServices();
			List<Screening> scl = sc.getChildService().findScreeningByCriteria(null, scr.getVaccinationCenterId(), scr.getEpiId(), null, null, true, 0, 12, new String[]{"idMapper"});
			if(scl.size() > 0 && (scr.getScreeningId() != scl.get(0).getScreeningId())){
				error.rejectValue("epiId", "", "Epi number occupied");
			}
			else{
				List<Vaccination> vcl = null;
				try {
					vcl = sc.getVaccinationService().findVaccinationRecordByCriteria(null, null, scr.getEpiId(), null, null, null, null, null, true, 0, 10, true, null);
				} catch (VaccinationDataException e) {
					e.printStackTrace();
				}
				if(vcl.size() > 0 && (scr.getMappedId() == null || scr.getMappedId()!=vcl.get(0).getChildId())){
					error.rejectValue("epiId", "", "Epi number occupied");
				}
			}
		}*/
		
		if(scr.getScreeningDate() == null){
			error.rejectValue("screeningDate", "", ErrorMessages.SCREENING_SCREENING_DATE_MISSING);
		}
		else if(scr.getScreeningDate().after(new Date())){
			error.rejectValue("screeningDate", "", ErrorMessages.SCREENING_SCREENING_DATE_IN_FUTURE);
		}
		
		if(scr.getVaccinationCenterId() == null){
			error.rejectValue("vaccinationCenterId" , "" , ErrorMessages.VACCINATION_CENTER_MISSING);
    	}
    	
    	if(scr.getVaccinatorId() == null){
			error.rejectValue("vaccinatorId" , "" , ErrorMessages.VACCINATOR_MISSING);
    	}
    	
//		if(scr.getIsChildHealthy() == null || (!scr.getIsChildHealthy() && scr.getHealthProblem().trim().equals(""))){
//			error.rejectValue("healthProblem", "", ErrorMessages.SCREENING_CHILD_HEALTH_PROBLEM_MISSING);
//		}
		if(scr.getBroughtByRelationshipId() == null){
			error.rejectValue("broughtByRelationshipId" , "" , ErrorMessages.SCREENING_BROUGHTBY_MISSING);
		}
		else if(scr.getBroughtByRelationshipId() == WebGlobals.OTHER_OPTION_ID_IN_DB
				&& StringUtils.isEmptyOrWhitespaceOnly(scr.getOtherBroughtByRelationship())){
			error.rejectValue("otherBroughtByRelationship" , "" , ErrorMessages.SCREENING_OTHER_BROUGHTBY_MISSING);
		}
/*		else if(scr.getIsChildHealthy() && (scr.getHasApprovedLottery() == null || (!scr.getHasApprovedLottery() && scr.getReasonLotteryRejection().trim().equals("")))){
			error.rejectValue("reasonLotteryRejection", "", ErrorMessages.SCREENING_LOTTERY_REJECTION_REASON_MISSING);
		}
		else if(scr.getIsChildHealthy() && (scr.getHasApprovedReminders() == null || (!scr.getHasApprovedReminders() && scr.getReasonRemindersRejection().trim().equals("")))){
			error.rejectValue("reasonRemindersRejection", "", ErrorMessages.SCREENING_REMINDERS_REJECTION_REASON_MISSING);
		}
		else if(scr.getIsChildHealthy() && scr.getHasCellPhoneAccess() == null){
			error.rejectValue("hasCellPhoneAccess", "", ErrorMessages.SCREENING_CELL_PHONE_ACCESS_MISSING);
		}
		
		if(scr.getWillReturnToCurrentCenter() == null ||(!scr.getWillReturnToCurrentCenter() && scr.getReferredVaccinationCenterId() == null)){
			error.rejectValue("referredVaccinationCenterId", "", ErrorMessages.SCREENING_REFERRED_CENTER_MISSING);
		}*/
		/*if(scr.getHasCellPhoneAccess() != null && scr.getHasCellPhoneAccess()){
			if(!DataValidation.validate(REG_EX.CELL_NUMBER, scr.getCellNumber())){
				error.rejectValue("cellNumber", "", ErrorMessages.SCREENING_CELL_NUMBER_MISSING);
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(scr.getCellNumber()) && !DataValidation.validate(REG_EX.NAME_CHARACTERS, scr.getCellNumberOwnerFirstName())){
			error.rejectValue("cellNumberOwnerFirstName", "", ErrorMessages.SCREENING_CELL_NUMBER_OWNER_MISSING);
		}*/
	}
}
