package org.ird.unfepi;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ChildLottery.CodeStatus;
import org.ird.unfepi.model.ChildLotteryParams;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.IncentiveUtils;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.web.utils.IMRUtils;

import com.mysql.jdbc.StringUtils;

public class ChildLotteryRunner 
{
	public Boolean HAS_WON = null;
	public Float AMOUNT = null;
	public ChildLotteryParams LOTTERY_PARAMS = null;
	public String VERIFICATION_CODE = null;
	public String LOTTERY_STATUS_ERRORS = null;
	public Short VACCINE_ID = null;
	public String VACCINE_NAME = null;
	public Integer LOTTERY_RECORD_NUMBER = null;
	public Integer VACCINATION_RECORD_NUMBER = null;

	private ChildLotteryRunner(String verificationcode, Boolean hasWon, Float amount, ChildLotteryParams chlparams, String lotteryStatusErrors, Short vaccineId, String vaccineName, Integer lotteryRecordNumber, Integer vaccinationRecordNum){
		HAS_WON = hasWon;
		AMOUNT = amount;
		VERIFICATION_CODE = verificationcode;
		LOTTERY_PARAMS = chlparams;
		LOTTERY_STATUS_ERRORS = lotteryStatusErrors;
		VACCINE_ID = vaccineId;
		VACCINE_NAME = vaccineName;
		LOTTERY_RECORD_NUMBER = lotteryRecordNumber;
		VACCINATION_RECORD_NUMBER = vaccinationRecordNum;
	}
	
	/**
	 * Runs lottery and return object with parameters (HAS_WON, AMOUNT, LOTTERY_PARAMS, VERIFICATION_CODE, LOTTERY_STATUS_ERRORS)
	 * @param child the validated and successfully saved child object
	 * @param lotteryVaccination the validated successfully saved vaccination object for which lottery is being performed
	 * @param lotteryVaccine NonNull instance of vaccine existing in Database for which lottery is being performed
	 * @param enrollmentVaccineId Id of vaccine on which child is/was enrolled
	 * @param lotteryCriteria the criteria that drives the amount worth i.e. timeliness. for enrollment timeliness would be 0 
	 * @param user Authenticated user who had done dataEntry
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be COMMITED after data is saved into session 
	 * @return instance of the class with lottery results those having following parameters
	 * <ul><li>HAS_WON- null if lottery was inapplicable, result otherwise
	 * <li>AMOUNT- claimable amount that child had won
	 * <li>LOTTERY_PARAMS- {@linkplain ChildLotteryParams} object that was applied to drive the lottery process (denomination, probability etc)
	 * <li>VERIFICATION_CODE- code that child should provide to claim lottery amount
	 * <li>LOTTERY_STATUS_ERRORS- errors that prevented lottery from being run, these errors include:
	 *		<ul><li>LOTTERY ALREADY EXISTS - if any lottery has previously been done for given vaccination
	 *			<li>VACCINATION NOT FOUND  - if no vaccination is found for the child for given vaccine
	 *			<li>CHILD DID NOT GET VACCINE - if vaccination status is not in (VACCINATED,UNFILLED)
	 *			<li>LOTTERY SEEMS NOT APPLICABLE - if vaccination has lotteryApproval NULL
	 *			<li>LOTTERY NOT APPROVED - if vaccination`s lotteryApproval is FALSE
	 *			<li>LOTTERY CRITERIA NOT SATISFIED - if there was no lottery params found for given lotteryCriteria.
	 *			<li>ENROLLMENT DONE ON MEASLES2 - if enrollment is on Measles2
	 *		</ul>
	 * </ul>
	 */
	
	//TODO changing it private temporarily so that it could be re added in future
	private static ChildLotteryRunner runLottery(DataEntrySource dataEntrySource, Vaccination lotteryVaccination, Vaccine lotteryVaccine, 
			float lotteryCriteria, User user, ServiceContext sc)
	{
		String verificationcode = null;
		Boolean hasWon = null;
		Float amount = null;
		ChildLotteryParams chlparams = null;
		Integer lotteryRecordNumber = null;
		List<ChildLotteryParams> chlparamsl = sc.getIncentiveService().findChildLotteryParamsByCriteria(null, lotteryVaccine.getVaccineId(), null, null, null, null, 0, 1, true, null);

		String lotteryStatusErrors = lotteryEligibilityErrors(lotteryVaccination, lotteryVaccine, chlparamsl, sc);
				
		if(StringUtils.isEmptyOrWhitespaceOnly(lotteryStatusErrors))
		{
			ChildLottery chlott = new ChildLottery();

			if(chlparamsl.size()>0)
			{
				chlparams = chlparamsl.get(0);
				
				hasWon = IncentiveUtils.determineLotteryWon(chlparams.getProbability().intValue());

				chlott.setCreator(user);
				chlott.setHasWonLottery(hasWon);
				chlott.setLotteryDate(new Date());
				chlott.setVaccinationRecordNum(lotteryVaccination.getVaccinationRecordNum());
				
				if(hasWon){
					verificationcode = IncentiveUtils.determineVerificationCode();
						
					amount = chlparams.getDenomination();
					
					chlott.setAmount(amount);
					chlott.setCode(verificationcode);
					chlott.setCodeStatus(CodeStatus.AVAILABLE);
				}
				else {
					chlott.setCodeStatus(CodeStatus.LOTTERY_LOST);
				}
				
				lotteryRecordNumber = Integer.parseInt(sc.getIncentiveService().saveChildLottery(chlott).toString());
					
				// create lottery smses if won via web form
				if(hasWon && dataEntrySource.equals(DataEntrySource.WEB)){
					List<ReminderSms> smsl = IMRUtils.createLotteryWonReminderSms(chlott.getVaccinationRecordNum(), chlott.getLotteryDate(), chlott.getCreatedByUserId(), null, sc);
					for (ReminderSms reminderSms : smsl) {
						sc.getReminderService().addReminderSmsRecord(reminderSms);
					}
				}
			}
		}
		
		return new ChildLotteryRunner(verificationcode, hasWon, amount, chlparams, lotteryStatusErrors, lotteryVaccine.getVaccineId(), lotteryVaccine.getName(), lotteryRecordNumber, lotteryVaccination.getVaccinationRecordNum());
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean lotteryPreviouslyDone(int childId, short vaccineId, ServiceContext sc){
		List<ChildLottery> lotts = sc.getIncentiveService().findChildLotteryByCriteria(null, childId, vaccineId, null, null, null, null, null, null, null, null, null, null, null, null, 0, 10, true, null);
		return lotts.size() > 0;
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean vaccinationExists(int vaccinationRecordNum, ServiceContext sc){
		List vaccncnt = sc.getCustomQueryService().getDataBySQL("select count(*) from vaccination where vaccinationRecordNum="+vaccinationRecordNum);
		
		if(Integer.parseInt(vaccncnt.get(0).toString())>0){
			return true;
		}
		return false;
	}
	
	private static String lotteryEligibilityErrors(Vaccination lotteryVaccination, Vaccine lotteryVaccine, 
			List<ChildLotteryParams> chlparamsl, ServiceContext sc){
		String lotteryStatus = "";
		
		////Test if lottery already has been performed.
		boolean lotteryEverDone = lotteryPreviouslyDone(lotteryVaccination.getChildId(), lotteryVaccination.getVaccineId(), sc);
		boolean vaccinationExists = vaccinationExists(lotteryVaccination.getVaccinationRecordNum(), sc);
		
		if(lotteryEverDone){
			lotteryStatus  += "LOTTERY ALREADY EXISTS;";
		}
		
		if(!vaccinationExists){
			lotteryStatus  += "VACCINATION NOT FOUND;";
		}
		
		if(!lotteryVaccination.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED)
				&& !lotteryVaccination.getVaccinationStatus().equals(VACCINATION_STATUS.UNFILLED)){
			lotteryStatus += "CHILD DID NOT GET VACCINE;";
		}
		
		if(lotteryVaccination.getHasApprovedLottery() == null){
			lotteryStatus += "LOTTERY SEEMS NOT APPLICABLE;";
		}
		else if(!lotteryVaccination.getHasApprovedLottery()){
			lotteryStatus += "LOTTERY NOT APPROVED;";
		}
		
		if(chlparamsl.size() == 0){
			lotteryStatus += "LOTTERY CRITERIA NOT SATISFIED;";
		}
		else {
			//find lotteries for vaccines having same group as current vaccine has
			List<ChildLotteryParams> chpr = sc.getIncentiveService().findChildLotteryParamsByCriteria(null, null, null, null, chlparamsl.get(0).getCriteriaRangeMin(), chlparamsl.get(0).getCriteriaRangeMin(), 0, 10, true, null);
			for (ChildLotteryParams chp : chpr) {
				// if criteria is not for current vaccine
				if(chp.getChildLotteryParamsId() != chlparamsl.get(0).getChildLotteryParamsId()){
					// if any lottery exists for any ofthe vaccines of same group show error
					if(lotteryPreviouslyDone(lotteryVaccination.getChildId(), chp.getRecievedVaccineId(), sc)){
						Vaccine vacobj = Utils.initializeAndUnproxy(sc.getVaccinationService().findVaccineById(chp.getRecievedVaccineId()));
						lotteryStatus += "LOTTERY ALREADY DONE FOR VACCINE ("+chp.getRecievedVaccineId() + ":" +vacobj.getName()+") OF SAME GROUP;";
						break;
					}
				}
			}
		}
		
		if(lotteryVaccine.getName().toLowerCase().contains("measles2") && lotteryVaccination.getIsFirstVaccination()){
			lotteryStatus += "MEASLES2 GIVEN ON ENROLLMENT;";
		}
		
		return lotteryStatus;
	}
}
