package org.ird.unfepi.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.ChildLotteryRunner;
import org.ird.unfepi.GlobalParams.LotteryType;
import org.ird.unfepi.constants.EncounterType;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterId;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.EncounterResultsId;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;

import com.mysql.jdbc.StringUtils;

public class EncounterUtil {
	public enum ElementGeneral {
		PROJECT_ID,
		FIRST_NAME,
		LAST_NAME,
		FATHER_FIRST_NAME,
		FATHER_LAST_NAME,
		GENDER,
		AGE,
		BIRTHDATE,
		BIRTHDATE_ESTIMATED,
		AGE_YEARS,
		AGE_MONTHS,
		AGE_WEEKS,
		AGE_DAYS,
		ADDITIONAL_NOTE,
		BIRTHDATE_OR_AGE,
		QUALIFICATION,
		
		JUSTIFICATION,
		REQUESTED_BY,
		FORM_STATUS
	}
	public enum ElementChild {
		CHILD_NAMED,
		FOLLOWUP_STATUS,
		REMINDERS_APPROVED
	}
	
	public enum ElementWomen {
		FOLLOWUP_STATUS
	}
	
	public enum ElementStorekeeper {
		CLOSEST_VACCINATION_CENTER,
		STORE_NAME,
	}

	public enum ElementUser {
		USERNAME,
		USER_ID,
		USER_ROLE
	}
	
	public enum ElementVaccinator {
		CLOSEST_VACCINATION_CENTER,
	}
	
	public enum ElementVaccinationCenter {
		CENTER_NAME,
		CENTER_FULLNAME,
		CENTER_TYPE
	}
	
	public enum ElementContact {
		PRIMARY_CONTACT_NUMBER,
		SECONDARY_CONTACT_NUMBER,
		CONTACT_NUMBER,
		TELELINE_TYPE,
		NUMBER_TYPE,
	}
	
	public enum ElementIncentive {
		INCENTIVE_DATE_START,
		INCENTIVE_DATE_END, LOTTERY_STATUS, LOTTERY_ID, WON, WON_AMOUNT, VERIFICATION_CODE, PROBABILITY, CRITERIA_VALUE,
		TRANSACTION_DATE,
		CONSUMPTION_DATE,
		LOTTERY_TYPE,
	}
	
	public enum ElementVaccination{
		VACCINE_ID,
		VACCINE_NAME,
		VACCINATION_RECORD_NUM, 
		EPI_NUMBER, 
		VACCINATION_CENTER, 
		VACCINATION_CENTER_ID, 
		LOTTERY_APPROVED, 
		VACCINATION_DATE, 
		VISIT_DATE, 
		VACCINATION_STATUS, VACCINATION_DUE_DATE, SYSTEM_CALCULATED_DATE, VACCINATION_TYPE, 
	}
	
	public enum ElementAddress{
		HOUSE_NUMBER,
		STREET,
		SECTOR,
		COLONY,
		TOWN,
		TOWN_ID,
		TOWN_NAME,
		UC,
		UC_ID,
		UC_NAME,
		LANDMARK,
		CITY,
		CITY_ID,
		CITY_NAME,
		CITY_OTHER,
	}
	public static String getElementValueFromEncResult(String element, List<EncounterResults> encreslist){
		for (EncounterResults encounterResults : encreslist) {
			if(encounterResults.getId().getElement().equalsIgnoreCase(element)){
				return encounterResults.getValue();
			}
		}
		return null;
	}
	
	public static EncounterResults getEncResult(String element, List<EncounterResults> encreslist){
		for (EncounterResults encounterResults : encreslist) {
			if(encounterResults.getId().getElement().equalsIgnoreCase(element)){
				return encounterResults;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private static EncounterResults createEncounterResult (Encounter e, Enum element, Object value, String displayName, String group)
	{
		return createEncounterResult(e, element.name(), value, displayName, group);
	}
	
	private static EncounterResults createEncounterResult (Encounter e, String element, Object value, String displayName, String group)
	{
		String val = value==null?null:value.toString();
		
		EncounterResultsId erId = new EncounterResultsId (e.getId ().getEncounterId (), e.getId ().getP1id(), e.getId ().getP2id(), element);
		EncounterResults er = new EncounterResults (erId, val);
		er.setDisplayName(displayName);
		er.setGroupName(group);

		return er;
	}
	
	private static Encounter saveEncounter(int p1Id, int p2Id, Integer locationId, Date encounterDate, Date formStartDate, String note, int dataEntryUserId, 
			EncounterType encounterType, DataEntrySource dataEntrySource, ServiceContext sc)
	{
		List<Encounter> listenc = sc.getEncounterService().findEncounter(p1Id, p2Id, null);
		EncounterId id = new EncounterId(listenc.size()+1, p1Id, p2Id);
		Encounter e = new Encounter(id , encounterType.name(), dataEntrySource, locationId == null ? null : locationId.toString(), encounterDate, formStartDate, new Date(), note);
		e.setCreatedByUserId(dataEntryUserId);
		sc.getEncounterService().saveEncounter(e);
		return e;
	} 
	
	private static void saveEncounterResults(List<EncounterResults> encr, ServiceContext sc){
		int i = 0;
		for (EncounterResults encounterres : encr) {
			encounterres.setOrderAs(i+=3);
			sc.getEncounterService().saveEncounterResult(encounterres);
		}
	}
	
	public static void createEnrollmentEncounter(DataEntrySource dataEntrySource, String projectId, Boolean childNamed, Child child, 
			String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays, Address address, 
			VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, String completeCourseFromCenter, /*List<ChildLotteryRunner> lotteryRes,*/ 
			Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(child.getMappedId(), centerVisit.getVaccinatorId(), centerVisit.getVaccinationCenterId(), child.getDateEnrolled(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.ENROLLMENT, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, projectId, null, null));
		
		encr.add(createEncounterResult(e, ElementChild.CHILD_NAMED, childNamed, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FIRST_NAME, child.getFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.LAST_NAME, child.getLastName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FATHER_FIRST_NAME, child.getFatherFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FATHER_LAST_NAME, child.getFatherLastName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.GENDER, child.getGender(), null, null));
		encr.add(createEncounterResult(e, ElementChild.FOLLOWUP_STATUS, child.getStatus(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_OR_AGE, birthdateOrAge, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(child.getBirthdate()), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_ESTIMATED, child.getEstimatedBirthdate(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_YEARS, ageYears, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_MONTHS, ageMonths, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_WEEKS, ageWeeks, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_DAYS, ageDays, null, null));

		populateAddressEncounters(e, encr, address, sc);
		
		encr.add(createEncounterResult(e, ElementChild.REMINDERS_APPROVED, centerVisit.getPreference().getHasApprovedReminders(), null, null));
		encr.add(createEncounterResult(e, ElementContact.PRIMARY_CONTACT_NUMBER, centerVisit.getContactPrimary(), null, null));
		encr.add(createEncounterResult(e, ElementContact.SECONDARY_CONTACT_NUMBER, centerVisit.getContactSecondary(), null, null));
		
		populateVaccinationEncounter(e, encr, centerVisit, vaccineSchedule, sc);
		
	//	populateLotteryEncounter(e, encr, lotteryRes, null);
	
		saveEncounterResults(encr, sc);
	}
	
	public static void createWomenEnrollmentEncounter(IdMapper womenId, DataEntrySource dataEntrySource, String projectId,  Women women, 
			String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays, Address address, 
			WomenVaccinationCenterVisit centerVisit,	Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(womenId.getMappedId(), centerVisit.getVaccinatorId(), centerVisit.getVaccinationCenterId(), women.getDateEnrolled(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.ENROLLMENT, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, projectId, null, null));
		
		encr.add(createEncounterResult(e, ElementGeneral.FIRST_NAME, women.getFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.LAST_NAME, women.getLastName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FATHER_FIRST_NAME, women.getFatherFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FATHER_LAST_NAME, women.getFatherLastName(), null, null));
		encr.add(createEncounterResult(e, ElementChild.FOLLOWUP_STATUS, women.getStatus(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_OR_AGE, birthdateOrAge, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(women.getBirthdate()), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_ESTIMATED, women.getEstimatedBirthdate(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_YEARS, ageYears, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_MONTHS, ageMonths, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_WEEKS, ageWeeks, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.AGE_DAYS, ageDays, null, null));

		populateAddressEncounters(e, encr, address, sc);
		
		encr.add(createEncounterResult(e, ElementContact.PRIMARY_CONTACT_NUMBER, centerVisit.getContactPrimary(), null, null));
		encr.add(createEncounterResult(e, ElementContact.SECONDARY_CONTACT_NUMBER, centerVisit.getContactSecondary(), null, null));
		
		populateWomenVaccinationEncounter(e, encr, centerVisit, sc);
		
	//	populateLotteryEncounter(e, encr, lotteryRes, null);
	
		saveEncounterResults(encr, sc);
	}
	
	public static void createFollowupEncounter(VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, 
			/*List<ChildLotteryRunner> lotteryResults,*/ Object lotteryCriteriaValue, DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(centerVisit.getChildId(), centerVisit.getVaccinatorId(), centerVisit.getVaccinationCenterId(), centerVisit.getVisitDate(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.FOLLOWUP, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		encr.add(createEncounterResult(e, ElementChild.REMINDERS_APPROVED, centerVisit.getPreference().getHasApprovedReminders(), null, null));
		encr.add(createEncounterResult(e, ElementContact.PRIMARY_CONTACT_NUMBER, centerVisit.getContactPrimary(), null, null));
		encr.add(createEncounterResult(e, ElementContact.SECONDARY_CONTACT_NUMBER, centerVisit.getContactSecondary(), null, null));
		
		populateVaccinationEncounter(e, encr, centerVisit, vaccineSchedule, sc);
		
		// populateLotteryEncounter(e, encr, lotteryResults, lotteryCriteriaValue);
	
		saveEncounterResults(encr, sc);
	}
	
	public static void createFollowupPrivilegedEncounter(Vaccination currentVaccination, 
			Vaccine currentVaccine, String nextVaccineName, String nextVaccineSystemCalculatedDate,
			Boolean reminderApproved, String primaryMobileNumber, ChildLotteryRunner lotteryRes, Object lotteryCriteriaVal, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		/*Encounter e = saveEncounter(currentVaccination.getChildId(), currentVaccination.getVaccinatorId(), currentVaccination.getVaccinationCenterId(), currentVaccination.getVaccinationDate(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.FOLLOWUP_ADMIN, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		encr.add(createEncounterResult(e, "REMINDERS_APPROVED", reminderApproved));
		encr.add(createEncounterResult(e, "PRIMARY_MOBILE_NUMBER", primaryMobileNumber));

		populateVaccinationEncounter(e, encr, currentVaccination, currentVaccine, nextVaccineName, nextVaccineSystemCalculatedDate, sc);
		
		populateLotteryEncounter(e, encr, currentVaccination.getHasApprovedLottery(), lotteryRes.LOTTERY_STATUS_ERRORS, (lotteryRes.HAS_WON != null && lotteryRes.HAS_WON ), lotteryRes.AMOUNT, lotteryRes.VERIFICATION_CODE, lotteryRes.LOTTERY_PARAMS == null ? null : lotteryRes.LOTTERY_PARAMS.getProbability(), lotteryCriteriaVal);
		
		saveEncounterResults(encr, sc);*/
	}
	
	public static void createContactNumberEncounter(IdMapper programId, ContactNumber contact, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(programId.getMappedId(), dataEntryUser.getMappedId(), null, new Date(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.ADD_CONTACT_NUM, dataEntrySource, sc);
		
		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, programId.getIdentifiers().get(0).getIdentifier(), null, null));
		encr.add(createEncounterResult(e, ElementContact.NUMBER_TYPE, contact.getNumberType().name(), null, null));
		encr.add(createEncounterResult(e, ElementContact.CONTACT_NUMBER, contact.getNumber(), null, null));
		encr.add(createEncounterResult(e, ElementContact.TELELINE_TYPE, contact.getTelelineType(), null, null));
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createChangePreferenceEncounter(LotterySms preference, String reminderCellNumber, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(preference.getMappedId(), dataEntryUser.getMappedId(), null, preference.getDatePreferenceChanged(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.CHANGE_PREFERENCE, dataEntrySource, sc);
		
		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementChild.REMINDERS_APPROVED, preference.getHasApprovedReminders(), null, null));
		encr.add(createEncounterResult(e, ElementContact.PRIMARY_CONTACT_NUMBER, reminderCellNumber, null, null));
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createStorekeeperRegistrationEncounter(String programId, Storekeeper storekeeper, String usernameGiven,
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(storekeeper.getMappedId(), dataEntryUser.getMappedId(), null, storekeeper.getDateRegistered(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.STOREKEEPER_REG, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementStorekeeper.CLOSEST_VACCINATION_CENTER, sc.getVaccinationService().findVaccinationCenterById(storekeeper.getClosestVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, programId, null, null));
		encr.add(createEncounterResult(e, ElementUser.USERNAME, usernameGiven, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FIRST_NAME, storekeeper.getFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.LAST_NAME, storekeeper.getLastName(), null, null));
		encr.add(createEncounterResult(e, ElementStorekeeper.STORE_NAME, storekeeper.getStoreName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.GENDER, storekeeper.getGender(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(storekeeper.getBirthdate()), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_ESTIMATED, storekeeper.getEstimatedBirthdate(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.QUALIFICATION, storekeeper.getQualification(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.ADDITIONAL_NOTE, storekeeper.getDescription(), null, null));
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createVaccinatorRegistrationEncounter(String programId, Vaccinator vaccinator, String usernameGiven,
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(vaccinator.getMappedId(), dataEntryUser.getMappedId(), null, vaccinator.getDateRegistered(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.VACCINATOR_REG, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementVaccinator.CLOSEST_VACCINATION_CENTER, sc.getVaccinationService().findVaccinationCenterById(vaccinator.getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, programId, null, null));
		encr.add(createEncounterResult(e, ElementUser.USERNAME, usernameGiven, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.FIRST_NAME, vaccinator.getFirstName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.LAST_NAME, vaccinator.getLastName(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.GENDER, vaccinator.getGender(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vaccinator.getBirthdate()), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_ESTIMATED, vaccinator.getEstimatedBirthdate(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.QUALIFICATION, vaccinator.getQualification(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.ADDITIONAL_NOTE, vaccinator.getDescription(), null, null));
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createVaccinationCenterRegistrationEncounter(short cityId, String programId, VaccinationCenter center, 
			List<Map<String, Object>> vaccinationDayMapList, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(center.getMappedId(), dataEntryUser.getMappedId(), null, center.getDateRegistered(), formStartDate, null, dataEntryUser.getMappedId(), EncounterType.VACC_CENTER_REG, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementAddress.CITY, cityId, null, null));
		encr.add(createEncounterResult(e, ElementAddress.CITY_NAME, sc.getCustomQueryService().getDataByHQL("select name from Location where locationId="+cityId).get(0).toString(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, programId, null, null));
		encr.add(createEncounterResult(e, ElementVaccinationCenter.CENTER_NAME, center.getName(), null, null));
		encr.add(createEncounterResult(e, ElementVaccinationCenter.CENTER_FULLNAME, center.getFullName(), null, null));
		encr.add(createEncounterResult(e, ElementVaccinationCenter.CENTER_TYPE, center.getCenterType(), null, null));
		for (Map<String, Object> vdml : vaccinationDayMapList) {
			String[] strarr = (String[]) vdml.get("daylist");
			Vaccine vaccine = (Vaccine)vdml.get("vaccine");
			
			String daysSelected = "";
			
			for (String dayname : strarr) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(dayname)){
					daysSelected += dayname + ",";
				}
			}
			
			if(daysSelected.endsWith(",")){
				daysSelected = daysSelected.substring(0, daysSelected.lastIndexOf(","));
			}
			
			encr.add(createEncounterResult(e, vaccine.getName().toUpperCase(), daysSelected, null, null));
		}
		encr.add(createEncounterResult(e, ElementGeneral.ADDITIONAL_NOTE, center.getDescription(), null, null));
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createStorekeeperIncentiveEncounter(Date incentivizationDate, Date incentiveDateRangeFrom, Date incentiveDateRangeTo, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(dataEntryUser.getMappedId(), dataEntryUser.getMappedId(), null, incentivizationDate, formStartDate, null, dataEntryUser.getMappedId(), EncounterType.STK_INCENTIVIZE, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		populateIncentiveEncounter(e, encr, dataEntryUser, incentiveDateRangeFrom, incentiveDateRangeTo);
		
		saveEncounterResults(encr, sc);
	}
	
	public static void createVaccinatorIncentiveEncounter(Date incentivizationDate, Date incentiveDateRangeFrom, Date incentiveDateRangeTo, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(dataEntryUser.getMappedId(), dataEntryUser.getMappedId(), null, incentivizationDate, formStartDate, null, dataEntryUser.getMappedId(), EncounterType.VACC_INCENTIVIZE, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();
		
		populateIncentiveEncounter(e, encr, dataEntryUser, incentiveDateRangeFrom, incentiveDateRangeTo);
		
		saveEncounterResults(encr, sc);
	}
	
	public static Map<String, Object> createLotteryGeneratorEncounter(LotteryType lotteryType, String childProgramId, Child child, 
			Vaccination curVactn, Vaccine vaccine, String justification, String requestedBy, String addNote, 
			Date birthdate, Boolean isBirthdateEstimated, ChildLotteryRunner lotteryRes, Object criteriaValueTimeliness, 
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(child.getMappedId(), curVactn.getVaccinatorId(), curVactn.getVaccinationCenterId(), new Date(), formStartDate, (lotteryType.equals(LotteryType.EXISTING)?"NA":"PENDING"), dataEntryUser.getMappedId(), EncounterType.LOTTERY_GEN, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementGeneral.PROJECT_ID, childProgramId, null, null));
		encr.add(createEncounterResult(e, ElementChild.FOLLOWUP_STATUS, child.getStatus(), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(child.getBirthdate()), null, null));
		encr.add(createEncounterResult(e, ElementGeneral.BIRTHDATE_ESTIMATED, child.getEstimatedBirthdate(), null, null));
		
		encr.add(createEncounterResult(e, ElementIncentive.LOTTERY_TYPE, lotteryType, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.JUSTIFICATION, justification, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.REQUESTED_BY, requestedBy, null, null));
		encr.add(createEncounterResult(e, ElementGeneral.ADDITIONAL_NOTE, addNote, null, null));

		List<ChildLotteryRunner> lotteryResults = new ArrayList<ChildLotteryRunner>();
		lotteryResults.add(lotteryRes);
		
		//populateLotteryEncounter(e, encr, lotteryResults , criteriaValueTimeliness);

		encr.add(createEncounterResult(e, ElementGeneral.FORM_STATUS, lotteryType.equals(LotteryType.EXISTING)?"NA":"PENDING", null, null));

		saveEncounterResults(encr, sc);
		
		Map<String, Object> encountermap = new HashMap<String, Object>();
		encountermap.put("encounter", e);
		encountermap.put("encounterresults", encr);
		
		return encountermap;
	}
	
	public static void createLotteryConsumerEncounter(Child child, ChildLottery chlott,  
			DataEntrySource dataEntrySource, Date formStartDate, User dataEntryUser, ServiceContext sc)
	{
		Encounter e = saveEncounter(child.getMappedId(), chlott.getStorekeeperId(), null, chlott.getTransactionDate(), formStartDate, "", dataEntryUser.getMappedId(), EncounterType.LOTTERY_CONSUMP, dataEntrySource, sc);

		List<EncounterResults> encr = new ArrayList<EncounterResults>();

		encr.add(createEncounterResult(e, ElementVaccination.VACCINE_ID, chlott.getVaccination().getVaccineId(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINE_NAME, sc.getVaccinationService().findVaccineById(chlott.getVaccination().getVaccineId()).getName(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_STATUS, chlott.getVaccination().getVaccinationStatus(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_RECORD_NUM, chlott.getVaccination().getVaccinationRecordNum(), null, null));
		
		encr.add(createEncounterResult(e, ElementIncentive.WON_AMOUNT, chlott.getAmount(), null, null));
		encr.add(createEncounterResult(e, ElementIncentive.VERIFICATION_CODE, chlott.getCode(), null, null));
		
		encr.add(createEncounterResult(e, ElementIncentive.TRANSACTION_DATE, new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA).format(chlott.getTransactionDate()), null, null));
		encr.add(createEncounterResult(e, ElementIncentive.CONSUMPTION_DATE, new SimpleDateFormat(WebGlobals.GLOBAL_DATETIME_FORMAT_JAVA).format(chlott.getConsumptionDate()), null, null));

		saveEncounterResults(encr, sc);
	}
	
	private static void populateIncentiveEncounter(Encounter e, List<EncounterResults> encr,User dataEntryUser, 
			Date incentiveDateRangeFrom, Date incentiveDateRangeTo)
	{
		encr.add(createEncounterResult(e, ElementUser.USERNAME, dataEntryUser.getUsername(), null, null));
		encr.add(createEncounterResult(e, ElementUser.USER_ID, dataEntryUser.getIdMapper().getIdentifiers().get(0).getIdentifier(), null, null));
		encr.add(createEncounterResult(e, ElementUser.USER_ROLE, dataEntryUser.getIdMapper().getRole().getRolename(), null, null));

		encr.add(createEncounterResult(e, ElementIncentive.INCENTIVE_DATE_START, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(incentiveDateRangeFrom), null, null));
		encr.add(createEncounterResult(e, ElementIncentive.INCENTIVE_DATE_END, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(incentiveDateRangeTo), null, null));

	}
	
	/*private static void populateLotteryEncounter(Encounter e , List<EncounterResults> encr, List<ChildLotteryRunner> lotteryResults, Object lotteryCriteriaValue)
	{
		for (ChildLotteryRunner childLotteryRunner : lotteryResults) {
			String lotteryVariableIDPrefix = getLotteryEncounterPrefix(childLotteryRunner.VACCINE_NAME, childLotteryRunner.VACCINE_ID);
			String group = "LOTTERY_"+childLotteryRunner.VACCINE_NAME;
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementVaccination.VACCINE_ID, childLotteryRunner.VACCINE_ID, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementVaccination.VACCINE_NAME, childLotteryRunner.VACCINE_NAME, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementVaccination.VACCINATION_RECORD_NUM, childLotteryRunner.VACCINATION_RECORD_NUMBER, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.LOTTERY_ID, childLotteryRunner.LOTTERY_RECORD_NUMBER, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.LOTTERY_STATUS, (StringUtils.isEmptyOrWhitespaceOnly(childLotteryRunner.LOTTERY_STATUS_ERRORS)?"OK":childLotteryRunner.LOTTERY_STATUS_ERRORS), group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.WON, childLotteryRunner.HAS_WON, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.WON_AMOUNT, childLotteryRunner.AMOUNT, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.VERIFICATION_CODE, childLotteryRunner.VERIFICATION_CODE, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.PROBABILITY, childLotteryRunner.LOTTERY_PARAMS!=null?childLotteryRunner.LOTTERY_PARAMS.getProbability():null, group));
			encr.add(createRepeatedGroupEncounterResult(e, lotteryVariableIDPrefix, ElementIncentive.CRITERIA_VALUE, lotteryCriteriaValue, group));
		}
	}*/
	
	public static String getLotteryEncounterPrefix(String vaccineName, short vaccineId){
		String lotteryVariableIDPrefix = "LOTTERY_"+vaccineName+"_"+vaccineId;
		return lotteryVariableIDPrefix;
	}
	
	private static void populateVaccinationEncounter(Encounter e , List<EncounterResults> encr, VaccinationCenterVisit centerVisit, 
			List<VaccineSchedule> vaccineSchedule, ServiceContext sc)
	{
		encr.add(createEncounterResult(e, ElementVaccination.EPI_NUMBER, centerVisit.getEpiNumber(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VISIT_DATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(centerVisit.getVisitDate()), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_CENTER, centerVisit.getVaccinationCenterId(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_CENTER_ID, sc.getVaccinationService().findVaccinationCenterById(centerVisit.getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.LOTTERY_APPROVED, centerVisit.getHasApprovedLottery(), null, null));

		for (VaccineSchedule vsh : vaccineSchedule) {
			String vaccineVariablePrefix = null;
			String group = null;
			// if vaccine was not saved or updated in this event 
			// And has been received or updated in some previous followup and was shown on current schedule
			if(vsh.getVaccinationObjCurrentVisit() == null && !(vsh.getAssigned_duedate() == null || vsh.getVaccination_date() == null || vsh.getStatus() == null)){
				vaccineVariablePrefix = "VACCINE_DISPLAY_"+vsh.getVaccine().getName().toUpperCase()+"_"+vsh.getVaccine().getVaccineId();
				group = "VACCINE_DISPLAY_"+vsh.getVaccine().getName().toUpperCase();
				//TODO 
			}
			// if vaccine IS saved or updated in this event 
			else if(vsh.getVaccinationObjCurrentVisit() != null){
				if(vsh.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO.name()) || vsh.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name())){
					vaccineVariablePrefix = "VACCINE_RETRO_"+vsh.getVaccine().getName().toUpperCase()+"_"+vsh.getVaccine().getVaccineId();
					group = "VACCINE_RETRO_"+vsh.getVaccine().getName().toUpperCase();
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_ID, vsh.getVaccinationObjCurrentVisit().getVaccineId(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_NAME, vsh.getVaccine().getName(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_TYPE, vsh.getStatus(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_RECORD_NUM, vsh.getVaccinationObjCurrentVisit().getVaccinationRecordNum(), group));
					
					if(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_DUE_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()), group));
					
					if(vsh.getVaccinationObjCurrentVisit().getVaccinationDate()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getVaccinationObjCurrentVisit().getVaccinationDate()), group));
					
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_CENTER, vsh.getVaccinationObjCurrentVisit().getVaccinationCenterId(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_CENTER_ID,  sc.getVaccinationService().findVaccinationCenterById(vsh.getVaccinationObjCurrentVisit().getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_STATUS, vsh.getVaccinationObjCurrentVisit().getVaccinationStatus(), group));
				}
				else if(vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())){
					vaccineVariablePrefix = "VACCINE_CURRENT_"+vsh.getVaccine().getName().toUpperCase()+"_"+vsh.getVaccine().getVaccineId();
					group = "VACCINE_CURRENT_"+vsh.getVaccine().getName().toUpperCase();
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_ID, vsh.getVaccinationObjCurrentVisit().getVaccineId(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_NAME, vsh.getVaccine().getName(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_TYPE, vsh.getStatus(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_RECORD_NUM, vsh.getVaccinationObjCurrentVisit().getVaccinationRecordNum(), group));
					
					if(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_DUE_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()), group));
					
					if(vsh.getVaccinationObjCurrentVisit().getVaccinationDate()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getVaccinationObjCurrentVisit().getVaccinationDate()), group));
					
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_CENTER, vsh.getVaccinationObjCurrentVisit().getVaccinationCenterId(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_CENTER_ID,  Utils.initializeAndUnproxy(sc.getVaccinationService().findVaccinationCenterById(vsh.getVaccinationObjCurrentVisit().getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier()), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_STATUS, vsh.getVaccinationObjCurrentVisit().getVaccinationStatus(), group));
				}
				else if(vsh.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())){
					vaccineVariablePrefix = "VACCINE_SCHEDULED_"+vsh.getVaccine().getName().toUpperCase()+"_"+vsh.getVaccine().getVaccineId();
					group = "VACCINE_SCHEDULED_"+vsh.getVaccine().getName().toUpperCase();
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_ID, vsh.getVaccinationObjCurrentVisit().getVaccineId(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINE_NAME, vsh.getVaccine().getName(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_TYPE, vsh.getStatus(), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_RECORD_NUM, vsh.getVaccinationObjCurrentVisit().getVaccinationRecordNum(), group));
					
					if(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_DUE_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getVaccinationObjCurrentVisit().getVaccinationDuedate()), group));
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.VACCINATION_STATUS, vsh.getVaccinationObjCurrentVisit().getVaccinationStatus(), group));
					
					if(vsh.getAuto_calculated_date()!= null)
					encr.add(createRepeatedGroupEncounterResult(e, vaccineVariablePrefix, ElementVaccination.SYSTEM_CALCULATED_DATE,  WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(vsh.getAuto_calculated_date()), group));
				}
			}
		}
	}
	
	private static void populateWomenVaccinationEncounter(Encounter e , List<EncounterResults> encr, WomenVaccinationCenterVisit centerVisit,  ServiceContext sc)
	{
		encr.add(createEncounterResult(e, ElementVaccination.EPI_NUMBER, centerVisit.getEpiNumber(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VISIT_DATE, WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(centerVisit.getVisitDate()), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_CENTER, centerVisit.getVaccinationCenterId(), null, null));
		encr.add(createEncounterResult(e, ElementVaccination.VACCINATION_CENTER_ID, sc.getVaccinationService().findVaccinationCenterById(centerVisit.getVaccinationCenterId(), true, new String[]{"idMapper"}).getIdMapper().getIdentifiers().get(0).getIdentifier(), null, null));
	}
	
	public static String getEncounterElementName(String variablePrefix, Enum elementName){
		return variablePrefix+"_"+elementName;
	}
	private static EncounterResults createRepeatedGroupEncounterResult(Encounter e, String variablePrefix, Enum elementName, Object value, String group){
		return createEncounterResult(e, getEncounterElementName(variablePrefix, elementName), value, elementName.name(), group);
	}
	private static void populateAddressEncounters(Encounter e , List<EncounterResults> encr, Address address, ServiceContext sc)
	{
		List c = sc.getCustomQueryService().getDataBySQL("select otherIdentifier, name from location where locationId="+address.getCityId());
		List t = sc.getCustomQueryService().getDataBySQL("select otherIdentifier, name from location where locationId="+address.getAddtown());
		List u = sc.getCustomQueryService().getDataBySQL("select otherIdentifier, name from location where locationId="+address.getAddUc());
		
		encr.add(createEncounterResult(e, ElementAddress.HOUSE_NUMBER, address.getAddHouseNumber(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.STREET, address.getAddStreet(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.SECTOR, address.getAddSector(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.COLONY, address.getAddColony(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.TOWN, address.getAddtown(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.TOWN_ID, ((Object[])t.get(0))[0], null, null));
		encr.add(createEncounterResult(e, ElementAddress.TOWN_NAME, ((Object[])t.get(0))[1], null, null));
		encr.add(createEncounterResult(e, ElementAddress.UC, address.getAddUc(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.UC_ID, ((Object[])u.get(0))[0], null, null));
		encr.add(createEncounterResult(e, ElementAddress.UC_NAME, ((Object[])u.get(0))[1], null, null));
		encr.add(createEncounterResult(e, ElementAddress.LANDMARK, address.getAddLandmark(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.CITY, address.getCityId(), null, null));
		encr.add(createEncounterResult(e, ElementAddress.CITY_ID, ((Object[])c.get(0))[0], null, null));
		encr.add(createEncounterResult(e, ElementAddress.CITY_NAME, ((Object[])c.get(0))[1], null, null));
		encr.add(createEncounterResult(e, ElementAddress.CITY_OTHER, address.getCityName(), null, null));
	}
}
