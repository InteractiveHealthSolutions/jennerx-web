package org.ird.unfepi;

import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IncentiveParams;
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinatorIncentive;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.web.utils.IMRUtils;

import com.mysql.jdbc.StringUtils;

public class ChildIncentivization 
{
	public Boolean HAS_WON = null;
	public Float AMOUNT = null;
	public IncentiveParams INCENTIVE_PARAMS = null;
	public String VERIFICATION_CODE = null;
	public String INCENTIVE_STATUS_ERRORS = null;
	public Short VACCINE_ID = null;
	public String VACCINE_NAME = null;
	public Integer INCENTIVE_RECORD_NUMBER = null;
	public Integer VACCINATION_RECORD_NUMBER = null;

	/**
	 * @param verificationcode
	 * @param hasWon
	 * @param amount
	 * @param chlparams
	 * @param incentiveStatusErrors
	 * @param vaccineId
	 * @param vaccineName
	 * @param incentiveRecordNumber
	 * @param vaccinationRecordNum
	 */
	private ChildIncentivization(String verificationcode, Boolean hasWon, Float amount, IncentiveParams chlparams, String incentiveStatusErrors, Short vaccineId, String vaccineName, Integer incentiveRecordNumber, Integer vaccinationRecordNum){
		HAS_WON = hasWon;
		AMOUNT = amount;
		VERIFICATION_CODE = verificationcode;
		INCENTIVE_PARAMS = chlparams;
		INCENTIVE_STATUS_ERRORS = incentiveStatusErrors;
		VACCINE_ID = vaccineId;
		VACCINE_NAME = vaccineName;
		INCENTIVE_RECORD_NUMBER = incentiveRecordNumber;
		VACCINATION_RECORD_NUMBER = vaccinationRecordNum;
	}
	
	private static ChildIncentivization createIncentives(DataEntrySource dataEntrySource, boolean hasNic, int armId, Vaccination incentiveVaccination, 
			Vaccine incentiveVaccine, User user, ServiceContext sc) {
		Float amount = null;
		IncentiveParams chincentiveParams = null;
		VaccinatorIncentive vaccinatorIncentive = new VaccinatorIncentive();
		Integer incentiveRecordNumber = null;
		
		List<IncentiveParams> incentiveParamsForChildlist = sc.getIncentiveService().findIncentiveParamsByCriteria(incentiveVaccine.getVaccineId(), armId, sc.getUserService().getRole(GlobalParams.CHILD_ROLE_NAME, true, null).getRoleId(), null, null, null, null, 0, 1, true, null);
		List<IncentiveParams> incentiveParamsForVaccinatorlist = sc.getIncentiveService().findIncentiveParamsByCriteria(incentiveVaccine.getVaccineId(), armId, sc.getUserService().getRole(GlobalParams.VACCINATOR_ROLE_NAME, true, null).getRoleId(), null, null, null, null, 0, 1, true, null);
		
		String incentiveStatusErrors = incentiveEligibilityErrors(incentiveVaccination, incentiveVaccine, incentiveParamsForChildlist, sc);
				
		if(StringUtils.isEmptyOrWhitespaceOnly(incentiveStatusErrors))
		{
			ChildIncentive childIncentive = new ChildIncentive();
			chincentiveParams = incentiveParamsForChildlist.get(0);
			
			amount = chincentiveParams.getAmount();
			
			childIncentive.setArmId(armId);
			childIncentive.setCreator(user);
			//TODO 
			childIncentive.setHasWonIncentive(true);
			childIncentive.setIncentiveDate(incentiveVaccination.getVaccinationDate());
			childIncentive.setVaccinationRecordNum(incentiveVaccination.getVaccinationRecordNum());
//				childIncentive.setDescription("");
			childIncentive.setAmount(amount);
			childIncentive.setIncentiveStatus(IncentiveStatus.AVAILABLE);
			childIncentive.setIncentiveParamId(chincentiveParams.getIncentiveParamsId());
				
			incentiveRecordNumber = Integer.parseInt(sc.getIncentiveService().saveChildIncentive(childIncentive).toString());
				
			// create incentive smses if won via web form
			List<ReminderSms> smsl = IMRUtils.createLotteryWonReminderSms(hasNic?"LOTTERY_WON_REMINDER_WITH_NIC":"LOTTERY_WON_REMINDER_WITHOUT_NIC", childIncentive.getVaccinationRecordNum(), childIncentive.getIncentiveDate(), childIncentive.getCreatedByUserId(), null, sc);
			for (ReminderSms reminderSms : smsl) {
				sc.getReminderService().addReminderSmsRecord(reminderSms);
			}
		}
			
		if(StringUtils.isEmptyOrWhitespaceOnly(incentiveStatusErrors) && incentiveParamsForVaccinatorlist.size()>0)
		{
			IncentiveParams vincentiveParams = incentiveParamsForVaccinatorlist.get(0);
			vaccinatorIncentive.setArmId(armId);
			vaccinatorIncentive.setIncentiveParamId(vincentiveParams.getIncentiveParamsId());
			vaccinatorIncentive.setAmount(vincentiveParams.getAmount());
			vaccinatorIncentive.setCreator(user);
			vaccinatorIncentive.setIsIncentivized(true);
			vaccinatorIncentive.setIncentiveStatus(IncentiveStatus.AVAILABLE);
			vaccinatorIncentive.setIncentiveDate(incentiveVaccination.getVaccinationDate());
			vaccinatorIncentive.setVaccinationRecordNum(incentiveVaccination.getVaccinationRecordNum());
			vaccinatorIncentive.setVaccinatorId(incentiveVaccination.getVaccinatorId());
			vaccinatorIncentive.setCreator(user);
		//	vaccinationIncentive.setDescription("");
			
			sc.getIncentiveService().saveVaccinatorIncentive(vaccinatorIncentive);
		}
		
		return new ChildIncentivization(null, StringUtils.isEmptyOrWhitespaceOnly(incentiveStatusErrors), amount, 
				chincentiveParams, incentiveStatusErrors, incentiveVaccine.getVaccineId(), incentiveVaccine.getName(), 
				incentiveRecordNumber, incentiveVaccination.getVaccinationRecordNum());
	}
	
	 public static ChildIncentivization runIncentive(DataEntrySource dataEntrySource, boolean hasNic, int armId, Vaccination incentiveVaccination, Vaccine incentiveVaccine, 
				User user, ServiceContext sc)
	{
		return createIncentives(dataEntrySource, hasNic, armId, incentiveVaccination, incentiveVaccine, user, sc);
	}
	
	public static boolean vaccinationIncentiveExists(int childId, short vaccineId, ServiceContext sc){
		
		List<ChildIncentive> listChildIncentive = sc.getIncentiveService().findChildIncentiveByCriteria(childId, vaccineId, null, null, null, null, null, null, null, null, 0, 10, true, null);
		return listChildIncentive.size() > 0;
	}
	
	public static boolean vaccinationExists(int vaccinationRecordNum, ServiceContext sc){
		Vaccination vaccncnt = sc.getVaccinationService().getVaccinationRecord(vaccinationRecordNum, true, null, null);
		
		if(vaccncnt != null){
			return true;
		}
		return false;
	}
	
	private static String incentiveEligibilityErrors(Vaccination incentiveVaccination, Vaccine incentiveVaccine, 
			List<IncentiveParams> chlparamsl, ServiceContext sc){
		String incentiveStatus = "";
		
		////Test if incentive already has been performed.
		boolean incentiveEverDone = vaccinationIncentiveExists(incentiveVaccination.getChildId(), incentiveVaccination.getVaccineId(), sc);
		boolean vaccinationExists = vaccinationExists(incentiveVaccination.getVaccinationRecordNum(), sc);
		
		if(incentiveEverDone){
			incentiveStatus  += "INCENTIVE ALREADY EXISTS;";
		}
		
		if(!vaccinationExists){
			incentiveStatus  += "VACCINATION NOT FOUND;";
		}
		
		if(!incentiveVaccination.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED)
				&& !incentiveVaccination.getVaccinationStatus().equals(VACCINATION_STATUS.UNFILLED)){
			incentiveStatus += "CHILD DID NOT GET VACCINE TODAY;";
		}
		
		if(incentiveVaccination.getHasApprovedLottery() == null || !incentiveVaccination.getHasApprovedLottery()){
			incentiveStatus += "INCENTIVE NOT APPROVED;";
		}
		
		if(chlparamsl.size() == 0){
			incentiveStatus += "INCENTIVE CRITERIA NOT SATISFIED;";
		}
		
		/*if(incentiveVaccine.getName().toLowerCase().contains("measles2") && incentiveVaccination.getIsFirstVaccination()){
			incentiveStatus += "MEASLES2 GIVEN ON ENROLLMENT;";
		}*/
		
		return incentiveStatus;
	}
}
