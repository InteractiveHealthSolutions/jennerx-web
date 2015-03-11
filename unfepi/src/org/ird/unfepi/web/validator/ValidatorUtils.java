package org.ird.unfepi.web.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.DataField;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenter.CenterType;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.ird.unfepi.web.utils.IMRUtils;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.validator.ValidatorOutput.ValidatorStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class ValidatorUtils {

	//private static final String CHILD_PRF = "child.";
	//private static final String IDMAPPER_PRF = "idMapper.";
	//private static final String CURRENT_VACCINATION_PRF = "centerVisit.";
	//private static final String ADDRESS_PRF = "address.";
	//private static final String PREFERENCE_PRF = "centerVisit.preference.";
	//private static final String CONTACT_GEN_PRF = "contactNumber.";
	//private static final String STOREKEEPER_PRF = "storekeeper.";
	/*private static final String VACCINATOR_PRF = "vaccinator.";
	private static final String USER_GEN_PRF = "user.";*/
	
	public static final boolean IGNORE_NEXT_VACCINE_FOLLOWUP_FORM = false;
	public static final boolean IGNORE_NEXT_VACCINE_FOLLOWUP_PRIVILEGED_FORM = true;

	static final int MAX_AGE_YEARS_MEASLES2 = 5;
	static final int MAX_AGE_YEARS = 3;
	static final int MAX_AGE_MONTHS = 36;
	static final int MAX_AGE_WEEKS = 52;
	static final int MAX_AGE_DAYS = 365;

	/** Compares if two object have same value by comparing their string value. Returns true if both objects are null or equal. Works well for Integers, Longs, Short, Boolean, String. Donot use it for comparing Dates.
	 * @param obj1 object 1
	 * @param obj2 object 2
	 * @param ignoreStringCase if case should be ignored while comparing string values of specified onjects.
	 * @return true if both object are equal or null
	 */
	private static boolean isValueEqual(Object obj1, Object obj2, boolean ignoreStringCase){
		if(obj1 == null && obj2 == null){
			return true;
		}
		
		if((obj1 == null && obj2 != null)
				|| ((obj1 != null && obj2 == null))){
			return false;
		}
		
		if(ignoreStringCase){
			if(obj1.toString().equalsIgnoreCase(obj2.toString())){
				return true;
			}
		}
		else if(obj1.toString().equals(obj2.toString())){
			return true;
		}

		return false;
	}
	
	/**
	 * @param dataEntrySource MUST be specified
	 * @param errorMsg The actual message text that should be displayed to user if value doesnot comply with validity rules
	 * @param mobileErrors The list that would encapsulte/appended with given {@code errorMsg} IFF {@code dataEntrySource} is MOBILE 
	 * @param webErrors The spring {@link Errors} object of corresponding validator of respective entity e.g. {@link ChildValidator} that called the function and would encapsulate specified {@code errorMsg} if value is rejected for {@code dataEntrySource} WEB
	 * @param field Only for {@code dataEntrySource} WEB. The complete field or property name that would be rejected for spring bound tags ().
	 * if dataEntrySource is MOBILE and no field is specified then errorMessage is appended with field {@linkplain DataField#OTHER}
	 */
	private static void putError(DataEntrySource dataEntrySource, String errorMsg, HashMap<String, String> mobileErrors, Errors webErrors, String field, boolean useFieldPrefix){
		if(dataEntrySource.equals(DataEntrySource.MOBILE))
		{
			if(!StringUtils.isEmptyOrWhitespaceOnly(field)){
				mobileErrors.put(field, errorMsg);
			}
			else {
				String prevError = mobileErrors.get(DataField.OTHER)==null?"":(mobileErrors.get(DataField.OTHER)+"\n");
				mobileErrors.put(DataField.OTHER, prevError+errorMsg);
			}
		}
		else{
			if (!StringUtils.isEmptyOrWhitespaceOnly(field)) {
				if(!useFieldPrefix){
					field = field.substring(field.lastIndexOf(".")+1);
				}
				
				webErrors.rejectValue(field, "nocode", null, errorMsg);
			}
			else {
				webErrors.reject("nocode", null, errorMsg);
			}
		}
	}
	
	/**
	 * @param programId : Program ID assigned to child. ID should conform to regex {@linkplain GlobalParams#CHILD_PROGRAMID_REGEX} 
	 * @param isNew : If ID is newly assigned and should be rejected if already exists in DB 
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @return {@linkplain ValidatorOutput} 
	 */
	public static ValidatorOutput validateChildProgramId(String programId, boolean isNew, ServiceContext sc){
		if(StringUtils.isEmptyOrWhitespaceOnly(programId)
				|| !DataValidation.validate(GlobalParams.CHILD_PROGRAMID_REGEX, programId)){
			return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.CHILD_ID_INVALID);
		}
		
//		if(isNew){
//			String q = "select count(*) from idmapper i join role r on i.roleId = r.roleId where programId = '"+programId+"' and r.roleName='"+GlobalParams.CHILD_ROLE_NAME+"'";
//			if(Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0){
//				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.CHILD_ALREADY_EXISTS);
//			}
//		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}
	
	/**
	 * @param programId : Program ID assigned to child. ID should conform to regex {@linkplain GlobalParams#CHILD_PROGRAMID_REGEX} 
	 * @param isNew : If ID is newly assigned and should be rejected if already exists in DB 
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @return {@linkplain ValidatorOutput} 
	 */
	public static ValidatorOutput validateTeamUserProgramId(String programId, boolean isNew, ServiceContext sc){
		if(StringUtils.isEmptyOrWhitespaceOnly(programId)
				|| !DataValidation.validate(GlobalParams.TEAM_USER_PROGRAMID_REGEX, programId)){
			return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.TEAM_USER_ID_INVALID);
		}
		
		if(isNew){
			String q = "select count(*) from idmapper i join role r on i.roleId = r.roleId where programId = '"+programId+"' and r.roleName NOT IN ('"+GlobalParams.CHILD_ROLE_NAME+"', '"+GlobalParams.STOREKEEPER_ROLE_NAME+"', '"+GlobalParams.VACCINATOR_ROLE_NAME+"')";
			if(Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0){
				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.USERID_ALREADY_EXISTS);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}
	
	/** Validates Enrollment (child.dateEnrolled, currentVaccination.vaccinationDuedate, currentVaccination.vaccinationDate MUST be equal and for mobile entries these MUST be equal to current date)
	 * (fields not mentioned in following parameters` list would automatically get populated during data saving)
	 * @param dataEntrySource : MUST be specified
	 * @param idMapper : 
	 * <ul><li>programId- see {@linkplain ValidatorUtils#validateChildProgramId}</ul>
	 * @param childNamed- if child has been named or not
	 * @param child : 
	 * <ul><li>dateEnrolled-  MUST be Non-null past date. For MOBILE entries, it MUST be equal to current date
	 * <li>Other validations would be same as for child param of : @see {@linkplain ValidatorUtils#validateBiographics}
	 * </ul>
	 * [{
	 * @param birthdateOrAge 
	 * @param ageYears 
	 * @param ageMonths 
	 * @param ageWeeks 
	 * @param ageDays see {@linkplain ValidatorUtils#validateBiographics} <br>
	 * }]
	 * @param address : 
	 * <ul><li> @see {@linkplain ValidatorUtils#validateAddress}
	 * </ul>
	 * @param completeCourseFromCenter
	 * @param centerVisit : @see {@linkplain ValidatorUtils#validateEnrollmentContactInfo} AND @see {@linkplain ValidatorUtils#validateVaccination} AND @see {@linkplain ValidatorUtils#validateNextVaccine}
	 * @param vaccineSchedule : @see {@link ValidatorUtils#validateVaccinationSchedule}
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 * */
	public static void validateEnrollmentForm(DataEntrySource dataEntrySource, String projectId, Boolean childNamed, Child child, 
			String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays, Address address, 
			String completeCourseFromCenter, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = true; // We know for enrollment we have encapsulated entities
		
		ValidatorOutput vidop = validateChildProgramId(projectId, true, sc);
		if(!vidop.STATUS().equals(ValidatorStatus.OK)){
			putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, DataField.PROGRAM_ID, useFieldPrefix);
		}

		if(child.getDateEnrolled() == null || DateUtils.afterTodaysDate(child.getDateEnrolled())){
			putError(dataEntrySource, ErrorMessages.CHILD_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		}// for mobile entries enrollment/vaccination date should be equal to current date time
		else if(dataEntrySource.equals(DataEntrySource.MOBILE) && !DateUtils.datesEqual(child.getDateEnrolled(), new Date())){
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_MOBILE_BACKDATED_ENTRY, mobileErrors, null, null, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(completeCourseFromCenter)){
			putError(dataEntrySource, ErrorMessages.COMPLETE_COURSE_FROM_CENTER_MISSING, mobileErrors, webErrors, DataField.CHILD_COMPLETE_COURSE_FROM_CENTER, useFieldPrefix);
		}
		
		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, child.getDateEnrolled());

		validateBiographics(dataEntrySource, childNamed, child, measles2Given, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, mobileErrors, webErrors, sc, useFieldPrefix);
		
		validateVaccinationSchedule(dataEntrySource, child, true, centerVisit, vaccineSchedule, false, mobileErrors, webErrors, sc, useFieldPrefix);

		validateAddress(dataEntrySource, address, mobileErrors, webErrors, useFieldPrefix);
		
		validateReminderAndContactInfo(dataEntrySource, centerVisit.getPreference(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), mobileErrors, webErrors, sc, useFieldPrefix);
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param preferences : 
	 * <ul><li>hasApprovedReminder- MUST be provided for all vaccines but SHOULD be NULL if Measles2
	 * </ul>
	 * @param contactPrimary : MUST be non-null valid MOBILE number IF <b>hasApprovedReminder</b> is true, optional otherwise
	 * @param contactSecondary : If not null should valid MOBILE/LANDLINE number
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param webErrors : {@link Errors} object of spring (web) {@link ChildValidator} that called the function (Must be provided if dataEntrySource is Web)<br>
 	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link ChildValidator} , ignored otherwise
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 * */
	public static void validateReminderAndContactInfo(DataEntrySource dataEntrySource,  
			LotterySms preferences, String contactPrimary, String contactSecondary,
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix)
	{
		// If Next Vaccine scheduled, A preference MUST be specified
		//if(nextVaccines.size() > 0){
			//Preference Approved reminder MUST be specified
			if(preferences == null || preferences.getHasApprovedReminders() == null){
				putError(dataEntrySource, ErrorMessages.APPROVAL_REMINDERS_MISSING, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY , useFieldPrefix);
			}//If reminders are approved then a contact number MUST be present
			else if(preferences.getHasApprovedReminders() 
					&& (contactPrimary == null || StringUtils.isEmptyOrWhitespaceOnly(contactPrimary))){
				putError(dataEntrySource, ErrorMessages.CONTACT1_NUMBER_INVALID, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY , useFieldPrefix);
			}
		//}// If No Next Vaccine, A preference SHOULD NOT be specified
//		else if(nextVaccines.size() ==  0
//				&& preferences != null && preferences.getHasApprovedReminders() != null){
//			putError(dataEntrySource, ErrorMessages.APPROVAL_REMINDERS_NOT_APPLICABLE, mobileErrors, webErrors, DataField.CENTER_VISIT_HAS_APPROVED_REMINDERS, useFieldPrefix);
//		}

		if(!StringUtils.isEmptyOrWhitespaceOnly(contactPrimary)){
			ValidatorOutput vconp = validateContactNumber(contactPrimary, ContactTeleLineType.MOBILE);
			if(!vconp.STATUS().equals(ValidatorStatus.OK)){
				putError(dataEntrySource, vconp.MESSAGE(), mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY, useFieldPrefix);
			}
		}
				
		if(!StringUtils.isEmptyOrWhitespaceOnly(contactSecondary)){
			ValidatorOutput vconsec = validateContactNumber(contactSecondary, ContactTeleLineType.MOBILE);
			if(!vconsec.STATUS().equals(ValidatorStatus.OK)){
				vconsec = validateContactNumber(contactSecondary, ContactTeleLineType.LANDLINE);
				if(!vconsec.STATUS().equals(ValidatorStatus.OK)){
					putError(dataEntrySource, "Secondary Contact should be a valid mobile or landline number", mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_SECONDARY, useFieldPrefix);
				}
			}
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(contactPrimary) && !StringUtils.isEmptyOrWhitespaceOnly(contactSecondary)
				&& contactPrimary.equalsIgnoreCase(contactSecondary)){
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_ALREADY_ASSIGNED, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_SECONDARY, useFieldPrefix);
		}
	}
	
	/** Validates the Enrollment form filled for lottery generated without any recorded enrollment i.e. Lottery Generator with option MISSING_ENROLLMENT
	 * @param dataEntrySource : MUST be specified. For MOBILE entries this service is not applicable
	 * @param child : Child instance that exists in database. Note that a child should exist in DB at this point with status UNENROLLED and enrollmentVaccination UNFILLED
	 * <ul><li>dateEnrolled-  MUST be specified and should be same as one specified in form when lottery was generated
	 * <li>birthdate- MUST be specified and should be same as one specified while generating lottery
	 * <li>estimatedBirthdate- MUST be specified and must be same as one specified while generating lottery
	 * <li>Other validations would be same as for child param of : @see {@linkplain ValidatorUtils#validateBiographics}
	 * </ul>
	 * @param currentVaccination : Enrollment vaccination instance that was created at time of lottery generation. 
	 * For validations details @see {@linkplain ValidatorUtils#validateVaccinationFillLotteryGenForm}
	 * @param nextVaccine : Vaccine that would be scheduled next for child. Should only be null IFF currentVaccine is Measles2
	 * For details @see {@linkplain ValidatorUtils#validateVaccinationFillLotteryGenForm} OR @see {@linkplain ValidatorUtils#validateNextVaccine}
	 * @param address : 
	 * <ul><li> @see {@linkplain ValidatorUtils#validateAddress}
	 * </ul>
	 * @param preferences @see corresponding param of {@linkplain ValidatorUtils#validateEnrollmentContactInfo}
	 * @param contactPrimary @see corresponding param of {@linkplain ValidatorUtils#validateEnrollmentContactInfo}
	 * @param contactSecondary @see corresponding param of {@linkplain ValidatorUtils#validateEnrollmentContactInfo}
	 * 
	 * @param encounter : Encounter object of lottery generation process
	 * @param encreslist : EncounterResults of lottery generation process of above encounter
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 * */
	public static void validateEnrollmentFillLotteryGenForm(DataEntrySource dataEntrySource, Child child, Vaccination currentVaccination, 
			Vaccine nextVaccine, Address address, LotterySms preferences, ContactNumber contactPrimary,
			ContactNumber contactSecondary, Encounter encounter, List<EncounterResults> encreslist, 
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = true; // We know for enrollment we have encapsulated entities

		/*if(dataEntrySource.equals(DataEntrySource.MOBILE)){
			putError(dataEntrySource, ErrorMessages.DATA_ENTRY_SOURCE_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
			return;
		}
		
		if(sc.getChildService().findChildById(child.getMappedId(), true, null) == null){
			putError(dataEntrySource, ErrorMessages.CHILD_DOESNOT_EXISTS, mobileErrors, webErrors, null, useFieldPrefix);
		}

		if(child.getDateEnrolled() == null
				|| !DateUtils.datesEqual(child.getDateEnrolled(), encounter.getDateEncounterEntered())){
			putError(dataEntrySource, "DateEnrolled:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, webErrors, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		}

		try {
			if(!isValueEqual(child.getBirthdate(), WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.parse(EncounterUtil.getElementValueFromEncResult("DATE_OF_BIRTH", encreslist)), true)){
				putError(dataEntrySource, "Birthdate:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
			putError(dataEntrySource, "Birthdate: Error validating birthdate "+e.getMessage(), mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}
    	
    	if(!isValueEqual(child.getEstimatedBirthdate(), EncounterUtil.getElementValueFromEncResult("IS_DATE_OF_BIRTHDATE_ESTIMATED", encreslist), true)){
			putError(dataEntrySource, "IsBirthdateEstimated:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}	
    	
		validateBiographics(dataEntrySource, child, currentVaccination, mobileErrors, webErrors, sc, useFieldPrefix);
		
		validateVaccinationFillLotteryGenForm(dataEntrySource, child, true, currentVaccination, nextVaccine, false, encounter, encreslist, mobileErrors, webErrors, sc, useFieldPrefix);

		validateAddress(dataEntrySource, address, mobileErrors, webErrors, useFieldPrefix);
		
		validateEnrollmentContactInfo(dataEntrySource, currentVaccination, preferences, contactPrimary, contactSecondary, mobileErrors, webErrors, sc, useFieldPrefix);
*/	}
	
	/**
	 * @param dataEntrySource : MUST be specified
****************************************************	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	public static void validateFollowupForm(DataEntrySource dataEntrySource, List<VaccineSchedule> vaccineSchedule, VaccinationCenterVisit centerVisit,  
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false; // We know that for followup form, only single command object i.e. vaccinationCentertVisit is bound

		validateVaccinationSchedule(dataEntrySource, sc.getChildService().findChildById(centerVisit.getChildId(), true, null), false, centerVisit, vaccineSchedule, false, mobileErrors, webErrors, sc, useFieldPrefix);
		
		validateReminderAndContactInfo(dataEntrySource, centerVisit.getPreference(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), mobileErrors, webErrors, sc, useFieldPrefix);
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param currentVaccination: MUST be an existing vaccination object that had been scheduled on any of previous visits
	 * <ul><li>vaccinationStatus- MUST be specified and should NOT be PENDING
	 * <li>vaccineId - MUST be specified from vaccines existing in DB
	 * <li>vaccinationCenterId- MUST be specified from centers existing in DB 
	 * <li>vaccinatorId- MUST be specified from vaccinators existing in DB 
	 * <li>vaccinationDuedate- MUST be a Non-null valid date.
	 * <li>vaccinationDate- MUST be a valid, past date and ideally should not be 4 or more days before vaccination due date
	 * <br>For Vaccine (NOT M2 i.e. BCG, P1, P2, P3, M1) - It should not be more than 2 years after birthdate
	 * <br>For Vaccine M2 - It should not be more than 3 years after birthdate 
	 * <li>polioVaccineGiven, pcvGiven, hasApprovedLottery- MUST be specified for MOBILE entries 
	 * <li>nextAssignedDate- MUST be specified for mobile entries for all vaccines and SHOULD be NULL if {@code nextVaccine} is NULL
	 * <li>epiNumber- MUST be specified and SHOULD be same as epiNumber of previous vaccination on given center if any exists 
	 * <li>For more details @see {@linkplain ValidatorUtils#validateVaccination}
	 * </ul>
	 * @param nextVaccine : NonNull instance of vaccine existing in Database that would be scheduled next for child (@see {@linkplain ValidatorUtils#validateNextVaccine})
	 * This param can only be null IFF  currentVaccine is [Measles2 OR (P3 and M1 vaccinated)]. 
	 * 
	 * @param hasApprovedReminders : @see corresponding param of {@linkplain ValidatorUtils#validateFollowupReminderPreference}
	 * @param reminderContactNumber : @see corresponding param of {@linkplain ValidatorUtils#validateFollowupReminderPreference}
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	public static void validateFollowupPrivilegedForm(DataEntrySource dataEntrySource, Vaccination currentVaccination, Vaccine nextVaccine, 
			Boolean hasApprovedReminders, String reminderContactNumber, ArrayList<String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false; // We know that for followup form, only single command object i.e. vaccination is bound

		/*validateVaccination(dataEntrySource, sc.getChildService().findChildById(currentVaccination.getChildId(), true, null), false, currentVaccination, nextVaccine, IGNORE_NEXT_VACCINE_FOLLOWUP_PRIVILEGED_FORM, mobileErrors, webErrors, sc, useFieldPrefix);
		
		validateFollowupReminderPreference(dataEntrySource, hasApprovedReminders, reminderContactNumber, mobileErrors, webErrors);
*/	}
	
	/** 
	 * @param hasApprovedReminders : MUST be specified
	 * @param reminderContactNumber : Must be a valid MOBILE number if reminders has been approved, COULD be null otherwise
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	private static void validateFollowupReminderPreference(DataEntrySource dataEntrySource, Boolean hasApprovedReminders, 
			String reminderContactNumber, ArrayList<String> mobileErrors, Errors webErrors){
		ValidatorOutput vopc = null;
		/*if(hasApprovedReminders == null){
			putError(dataEntrySource, ErrorMessages.PREFERENCE_REMINDER_MISSING, mobileErrors, webErrors, null);
		}
		else if(hasApprovedReminders
				&& StringUtils.isEmptyOrWhitespaceOnly(reminderContactNumber)){
			putError(dataEntrySource, ErrorMessages.CONTACT1_NUMBER_INVALID, mobileErrors, webErrors, null);
		}

		if(!StringUtils.isEmptyOrWhitespaceOnly(reminderContactNumber)
				&& !(vopc = validateContactNumber(reminderContactNumber, ContactTeleLineType.MOBILE)).STATUS().equals(ValidatorStatus.OK)){
			putError(dataEntrySource, vopc.MESSAGE(), mobileErrors, webErrors, null);
		}*/
	}
	
	/** Validates the Followup form filled for lottery generated without any recorded followup i.e. Lottery Generator with option PENDING_FOLLOWUP
	 * @param dataEntrySource : MUST be specified. For MOBILE entries this service is not applicable
	 * @param currentVaccination : Followup vaccination instance that was created at time of lottery generation. 
	 * For validations details @see {@linkplain ValidatorUtils#validateVaccinationFillLotteryGenForm}	 
	 * @param nextVaccine : NonNull instance of vaccine existing in Database that would be scheduled next for child (@see {@linkplain ValidatorUtils#validateVaccinationFillLotteryGenForm})
	 * This param can only be null IFF  currentVaccine is [Measles2 OR (P3 and M1 vaccinated)]. 
	 * 
	 * @param hasApprovedReminders : @see corresponding param of {@linkplain ValidatorUtils#validateFollowupReminderPreference}
	 * @param reminderContactNumber : @see corresponding param of {@linkplain ValidatorUtils#validateFollowupReminderPreference}
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	public static void validateFollowupFillLotteryGenForm(DataEntrySource dataEntrySource, Vaccination currentVaccination,  
			Vaccine nextVaccine, Boolean hasApprovedReminders, String reminderContactNumber, Encounter encounter, List<EncounterResults> encreslist, 
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false; // We know that for followup form, only single command object i.e. vaccination is bound

		/*if(dataEntrySource.equals(DataEntrySource.MOBILE)){
			putError(dataEntrySource, ErrorMessages.DATA_ENTRY_SOURCE_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
		}
		
		validateVaccinationFillLotteryGenForm(dataEntrySource, sc.getChildService().findChildById(currentVaccination.getChildId(), true, null), false, currentVaccination, nextVaccine, true, encounter, encreslist, mobileErrors, webErrors, sc, useFieldPrefix);
		
		validateFollowupReminderPreference(dataEntrySource, hasApprovedReminders, reminderContactNumber, mobileErrors, webErrors);
*/	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param contact : 
	 * <ul>
	 * <li>numberType : MUST be specified and should not be UNKNOWN. Should be SECONDARY if ID has already been assigned a PRIMARY number before
	 * <li>mappedId : MUST be specified to whome number is being assigned to
	 * <li>number- Should be a non-null and valid number according to given {@code telelineType} . Also same number should not already exists for the given ID
	 * <li>telelineType- MUST be provided and should be in (MOBILE/LANDLINE)
	 * </ul>
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	public static void validateContactNumberForm(DataEntrySource dataEntrySource, ContactNumber contact, 
			HashMap<String,String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false; // We know that for contact number form, only single command object i.e. contact number is bound

		if(contact.getNumberType() == null
				|| contact.getNumberType().equals(ContactType.UNKNOWN)){
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_NUMBER_TYPE_INVALID, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		}
		else if(contact.getNumberType().equals(ContactType.PRIMARY)
				&& sc.getCustomQueryService().getDataBySQL(
						"SELECT mappedId FROM contactnumber " +
						" WHERE mappedId="+contact.getMappedId()+" " +
								" AND contactNumberId <>"+contact.getContactNumberId() +
								" AND numberType='"+ContactType.PRIMARY+"'").size() > 0){
			putError(dataEntrySource, ErrorMessages.CONTACT_PRIMARY_NUMBER_EXISTS, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		}
		
		if(contact.getMappedId() == null || contact.getMappedId() <= 0){
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_MAPPEDID_MISSING, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		}
		
		if(contact.getTelelineType() == null 
				|| contact.getTelelineType().equals(ContactTeleLineType.UNKNOWN)){
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_TELELINE_TYPE_INVALID, mobileErrors, webErrors, DataField.CONTACT_NUMBER_TELELINE_TYPE, useFieldPrefix);
		}
		else {
			ValidatorOutput vconsec = validateContactNumber(contact.getNumber(), contact.getTelelineType());
			if(!vconsec.STATUS().equals(ValidatorStatus.OK)){
				putError(dataEntrySource, vconsec.MESSAGE(), mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER, useFieldPrefix);
			}
			else if(contact.getMappedId() != null 
					&& sc.getCustomQueryService().getDataBySQL(
					"SELECT mappedId FROM contactnumber " +
					" WHERE number='"+contact.getNumber()+"' " +
							" AND mappedId="+contact.getMappedId()+" " +
									" AND contactNumberId <>"+contact.getContactNumberId()).size() > 0){
				putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_ALREADY_ASSIGNED, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
			}
		}
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param programId : see programId param of @see {@linkplain ValidatorUtils#validateUser}. 
	 * ProgramId would be validated as a newly assigned id i.e. with param isNew TRUE 
	 * @param confirmPwd : The password provided in Re-Enter password field
	 * @param userUnderEdit : 
	 * field username (loginIdGiven), clearTextPassword (passwordGiven), and confirmPwd would be validated according to {@linkplain ValidatorUtils#validateLoginCredentials}. 
	 * <b>All others would be validated according to @see {@linkplain ValidatorUtils#validateUser}</b>
	 */
	public static void validateUserRegistration(DataEntrySource dataEntrySource, String programId, String confirmPwd, 
			User userUnderEdit, Role userUnderEditRole, User editorUser, Role editorRole, 
			ArrayList<String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false; // We know that for user registration form, only single command object i.e. user is bound

		/*validateLoginCredentials(dataEntrySource, userUnderEdit.getUsername(), userUnderEdit.getClearTextPassword(), confirmPwd, mobileErrors, webErrors, sc);
		
		validateUser(dataEntrySource, true, programId, userUnderEdit, userUnderEditRole, editorUser, editorRole, mobileErrors, webErrors, useFieldPrefix, sc);
*/	}
	
	/** @see {@linkplain ValidatorUtils#validateUser}
	 */
	public static void validateUserEdit(DataEntrySource dataEntrySource, String programId, User userUnderEdit, Role userUnderEditRole, 
			User editorUser, Role editorRole, 
			ArrayList<String> mobileErrors, Errors webErrors, ServiceContext sc){
		boolean useFieldPrefix = false; // We know that for user edit form, only single command object i.e. user is bound

		validateUser(dataEntrySource, false, programId, userUnderEdit, userUnderEditRole, editorUser, editorRole, mobileErrors, webErrors, useFieldPrefix, sc);
	}
	
	/**
	 * @param role: The role whose roleName is being searched in given roles` list
	 * @param roles : list of roles that would be searched for given role
	 * 
	 * @return true if role`s name matches any of the roles in given list
	 */
	private static boolean isRoleInList(Role role, List<Role> roles){
		for (Role rol : roles) {
			if(rol.getRolename().equalsIgnoreCase(role.getRolename())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param isNew : If userUnderEdit is being rolled in i.e. is going to be inserted first time in DB
	 * @param programId : depending on the role programId would be validated according to any of the validations given below
	 * <br>{@linkplain ValidatorUtils#validateChildProgramId} OR
	 * <br>{@linkplain ValidatorUtils#validateVaccinatorProgramId} OR
 	 * <br>{@linkplain ValidatorUtils#validateStorekeeperProgramId} OR
 	 * <br>{@linkplain ValidatorUtils#validateTeamUserProgramId}
	 * @param userUnderEdit : The user being edited MUST be provided
	 * <ul><li>username- MUST be a Non-null, alphanumeric, non-whitespace sequence of 5-20 characters.
	 * <li>firstName- MUST be specified with valid name characters
	 * <li>lastName- (optional) but if specified, should have valid name characters
	 * <li>email- MUST be a valid email address
	 * <li>status- MUST be specified
	 * </ul>
	 * @param userUnderEditRole : The instance of existing role that is assigned to user MUST be specified
	 * @param editorUser : MUST be Non-Null and existing user that is manipulating the userUnderEdit
	 * @param editorRole : The role of {@code editorUser} to assess operation`s authentication and privileges. MUST be provided
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateUser(DataEntrySource dataEntrySource, boolean isNew, String programId, User userUnderEdit, Role userUnderEditRole, 
			User editorUser, Role editorRole, 
			ArrayList<String> mobileErrors, Errors webErrors, boolean useFieldPrefix, ServiceContext sc) 
	{
		// it should only be checked for alphanumeric and allowable range of characters. As type, regex and uniqueness would be calculated at the time of creation
		/*if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getUsername())
				|| !DataValidation.validate(REG_EX.ALPHA_NUMERIC, userUnderEdit.getUsername(), 5, 20)) {
			putError(dataEntrySource, ErrorMessages.USERNAME_INVALID, mobileErrors, webErrors, getFieldName(USER_GEN_PRF, "username", useFieldPrefix));
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getFirstName())
				|| !DataValidation.validate(REG_EX.ALPHA, userUnderEdit.getFirstName())) {
			putError(dataEntrySource, ErrorMessages.FIRSTNAME_INVALID, mobileErrors, webErrors, getFieldName(USER_GEN_PRF, "firstName", useFieldPrefix));
		}
		
		if (!StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getLastName())
				&& !DataValidation.validate(REG_EX.ALPHA, userUnderEdit.getLastName())) {
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, getFieldName(USER_GEN_PRF, "lastName", useFieldPrefix));
		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getEmail()) 
				|| !DataValidation.validate(REG_EX.EMAIL, userUnderEdit.getEmail())) {
			putError(dataEntrySource, ErrorMessages.EMAIL_INVALID, mobileErrors, webErrors, getFieldName(USER_GEN_PRF, "email", useFieldPrefix));
		}
		
		if (userUnderEdit.getStatus() == null) {
			putError(dataEntrySource, ErrorMessages.USER_STATUS_MISSING, mobileErrors, webErrors, null);
		}
		
		if (userUnderEditRole != null)
		{
			ValidatorOutput vidop = null;
			if(StringUtils.isEmptyOrWhitespaceOnly(programId)) {
				putError(dataEntrySource, ErrorMessages.ID_INVALID, mobileErrors, webErrors, null);
			}
			else if (
					(userUnderEditRole.getRolename().equalsIgnoreCase("child")
							&& !(vidop=validateChildProgramId(programId, isNew, sc)).STATUS().equals(ValidatorStatus.OK))
					||(userUnderEditRole.getRolename().equalsIgnoreCase("vaccinator")
							&& !(vidop=validateVaccinatorProgramId(programId, isNew, sc)).STATUS().equals(ValidatorStatus.OK))
					||(userUnderEditRole.getRolename().equalsIgnoreCase("storekeeper")
							&& !(vidop=validateStorekeeperProgramId(programId, isNew, sc)).STATUS().equals(ValidatorStatus.OK))
					||(!userUnderEditRole.getRolename().matches("(?i:storekeeper|vaccinator|child)")
							&& !(vidop=validateTeamUserProgramId(programId, isNew, sc)).STATUS().equals(ValidatorStatus.OK))
					)
			{
				putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, null);
			}
			
			if(userUnderEdit.getStatus() != null
					&& User.isUserDefaultAdministrator(userUnderEdit.getUsername(), userUnderEditRole.getRolename())
					&& !userUnderEdit.getStatus().equals(UserStatus.ACTIVE)){
				putError(dataEntrySource, ErrorMessages.USER_STATUS_ADMIN_INVALID, mobileErrors, webErrors, null);
			}
			
			Map<String, ArrayList<Role>> rolemap = UserSessionUtils.getRolesDistinction(editorUser, editorRole, sc);
			List<Role> allowedRoles = rolemap.get("ALLOWED_ROLES");
			List<Role> notAllowedRoles = rolemap.get("NOT_ALLOWED_ROLES");
			
			if(User.isUserDefaultAdministrator(userUnderEdit.getUsername(), userUnderEditRole.getRolename())
					&& !User.isUserDefaultAdministrator(editorUser.getUsername(), editorRole.getRolename())){
				putError(dataEntrySource, ErrorMessages.USER_ADMIN_EDITOR_NOT_AUTHORIZED, mobileErrors, webErrors, null);
			} 
			// If underEditRole should be in allowed rolesList and should NOT be in restricted/unallowed rolesList
			else if(!isRoleInList(userUnderEditRole, allowedRoles) 
					&& isRoleInList(userUnderEditRole, notAllowedRoles)){
				putError(dataEntrySource, ErrorMessages.USER_ROLE_EDIT_NOT_ALLOWED, mobileErrors, webErrors, null);
			}
		}
		else {
			putError(dataEntrySource, ErrorMessages.USER_ROLE_MISSING, mobileErrors, webErrors, null);
		}*/
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param loginIdGiven : The username or id that would be used to login. Must be a UNIQUE, alphanumeric, 
	 * non-whitespace sequence of 5-20 characters. Must not be admin or administrator
	 * @param passwordGiven : Must be alphanumeric, non-whitespace sequence of 5-20 characters.
	 * @param passwordConfirm : Must be exactly same as {@code passwordGiven}
	 * 
	 * @param mobileErrors : List object that would be appended with error messages for MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors The spring {@link Errors} object that would encapsulate the error messages for WEB (Must be provided if dataEntrySource is WEB)
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * 
	 * All errors occurred during validation would be populated into {@code mobileErrors} OR {@code webErrors} w.r.t to dataEntrySource provided
	 */
	public static void validateLoginCredentials(DataEntrySource dataEntrySource, String loginIdGiven, String passwordGiven, String passwordConfirm, 
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc)
	{
		boolean useFieldPrefix = false;
		
		if(StringUtils.isEmptyOrWhitespaceOnly(loginIdGiven)
				|| !DataValidation.validate(REG_EX.ALPHA_NUMERIC, loginIdGiven, 5, 20)
				|| loginIdGiven.equalsIgnoreCase("admin")
				|| loginIdGiven.equalsIgnoreCase("administrator")){
			putError(dataEntrySource, ErrorMessages.USERNAME_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
		}
		else if(sc.getUserService().findUserByUsername(loginIdGiven) != null){
			putError(dataEntrySource, ErrorMessages.USERNAME_OCCUPIED, mobileErrors, webErrors, null, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(passwordGiven)
				|| !DataValidation.validate(REG_EX.ALPHA_NUMERIC, passwordGiven, 5, 20)){
			putError(dataEntrySource, ErrorMessages.PASSWORD_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
		}
		else if(passwordConfirm != null && !passwordConfirm.equals(passwordGiven)){
			putError(dataEntrySource, ErrorMessages.PASSWORDS_DONOT_MATCH, mobileErrors, webErrors, null, useFieldPrefix);
		}
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param programId : see {@linkplain ValidatorUtils#validateStorekeeperProgramId}
	 * @param storekeeper : 
	 * <ul><li>dateRegistered-  MUST be Non-null past date.
	 * <li>closestVaccinationCenterId- MUST be specified from centers existing in DB
	 * <li>nic- optional but if specified, should be 13 digit numeric sequence
	 * <li>epAccountNumber- optional but if specified, should be 12 digit numeric sequence 
	 * <li>firstName- MUST be specified with valid name characters
	 * <li>lastName- (optional) but if specified, should have valid name characters
	 * <li>storeName- MUST be specified with no special characters
	 * <li>birthdate- MUST be specified and should be a past date
	 * <li>estimatedBirthdate- MUST be specified
	 * <li>gender- MUST be either MALE/FEMALE
	 * <li>qualification- MUST be provided, if it contains Other: then it must be followed by a valid qualification name <br>
	 * </ul>
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 * @param birthdateOrAge 
	 * @param ageYears 
	 */
	public static void validateStorekeeperRegistrationForm(DataEntrySource dataEntrySource, String programId, Storekeeper storekeeper, 
			String birthdateOrAge, String ageYears, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix)
	{
		/*ValidatorOutput vidop = validateStorekeeperProgramId(programId, true, sc);
		if(!vidop.STATUS().equals(ValidatorStatus.OK)){
			putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, null, useFieldPrefix);
		}*/
		
		if(storekeeper.getDateRegistered() == null){
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_DATE_REGISTERED, useFieldPrefix);
		}
		else if(storekeeper.getDateRegistered().after(new Date())){
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_IN_FUTURE, mobileErrors, webErrors, DataField.STOREKEEPER_DATE_REGISTERED, useFieldPrefix);
		}
		
		if(storekeeper.getClosestVaccinationCenterId() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_CLOSEST_VACCINATION_CENTER_ID, useFieldPrefix);
    	}
    	
		if(!StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getNic())
				&& !DataValidation.validate(REG_EX.NUMERIC, storekeeper.getNic(), 13, 13)){
			putError(dataEntrySource, ErrorMessages.NIC_INVALID, mobileErrors, webErrors, DataField.STOREKEEPER_NIC, useFieldPrefix);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getEpAccountNumber())
				&& !DataValidation.validate(REG_EX.NUMERIC, storekeeper.getEpAccountNumber(), 12, 12)){
			putError(dataEntrySource, ErrorMessages.EP_WALLET_INVALID, mobileErrors, webErrors, DataField.STOREKEEPER_EP_ACCOUNT_NUMBER, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, storekeeper.getFirstName())){
			putError(dataEntrySource, ErrorMessages.FIRSTNAME_INVALID, mobileErrors, webErrors, DataField.STOREKEEPER_FIRST_NAME, useFieldPrefix);
		}

		if(!StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getLastName())
				&& !DataValidation.validate(REG_EX.NAME_CHARACTERS, storekeeper.getLastName())) {
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, DataField.STOREKEEPER_LAST_NAME, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getStoreName()) || !DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, storekeeper.getStoreName())){
			putError(dataEntrySource, ErrorMessages.STORENAME_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_STORE_NAME, useFieldPrefix);
		}
		
		if(storekeeper.getEstimatedBirthdate() == null){
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_BIRTHDATE, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge)){//TODO remove this. it is redundant.
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(!birthdateOrAge.equalsIgnoreCase("age") && !birthdateOrAge.equalsIgnoreCase("birthdate")){
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors,  DataField.STOREKEEPER_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(birthdateOrAge.toLowerCase().contains("age")){
			if(StringUtils.isEmptyOrWhitespaceOnly(ageYears) || !Utils.isNumberBetween(ageYears, 0, MAX_AGE_YEARS)){
				putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, DataField.STOREKEEPER_STOREKEEPER_AGE_YEARS, useFieldPrefix);
			}
		}
		
		if(storekeeper.getGender() == null || storekeeper.getGender().equals(Gender.UNKNOWN)){
			putError(dataEntrySource, ErrorMessages.GENDER_INVALID, mobileErrors, webErrors, DataField.STOREKEEPER_GENDER, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(storekeeper.getQualification())
				|| (storekeeper.getQualification().equals("Other") || storekeeper.getQualification().trim().endsWith(":"))){
			putError(dataEntrySource, ErrorMessages.QUALIFICATION_MISSING, mobileErrors, webErrors, DataField.STOREKEEPER_QUALIFICATION, useFieldPrefix);
		}
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param programId : see {@linkplain ValidatorUtils#validateVaccinatorProgramId}
	 * @param vaccinator : 
	 * <ul><li>dateRegistered-  MUST be Non-null past date.
	 * <li>vaccinationCenterId- MUST be specified from centers existing in DB
	 * <li>nic- optional but if specified, should be 13 digit numeric sequence
	 * <li>epAccountNumber- optional but if specified, should be 12 digit numeric sequence 
	 * <li>firstName- MUST be specified with valid name characters
	 * <li>lastName- (optional) but if specified, should have valid name characters
	 * <li>birthdate- MUST be specified and should be a past date
	 * <li>estimatedBirthdate- MUST be specified
	 * <li>gender- MUST be either MALE/FEMALE
	 * <li>qualification- MUST be provided, if it contains Other: then it must be followed by a valid qualification name <br>
	 * </ul>
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateVaccinatorRegistrationForm(DataEntrySource dataEntrySource, String programId, Vaccinator vaccinator, 
			String birthdateOrAge, String ageYears, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix)
	{
		/*ValidatorOutput vidop = validateVaccinatorProgramId(programId, true, sc);
		if(!vidop.STATUS().equals(ValidatorStatus.OK)){
			putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, null);
		}*/
		
		if(vaccinator.getDateRegistered() == null){
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_DATE_REGISTERED, useFieldPrefix);
		}
		else if(vaccinator.getDateRegistered().after(new Date())){
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_IN_FUTURE, mobileErrors, webErrors, DataField.VACCINATOR_DATE_REGISTERED, useFieldPrefix);
		}
		
		if(vaccinator.getVaccinationCenterId() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_VACCINATION_CENTER_ID, useFieldPrefix);
    	}
    	
		if(!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getNic())
				&& !DataValidation.validate(REG_EX.NUMERIC, vaccinator.getNic(), 13, 13)){
			putError(dataEntrySource, ErrorMessages.NIC_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_NIC, useFieldPrefix);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getEpAccountNumber())
				&& !DataValidation.validate(REG_EX.NUMERIC, vaccinator.getEpAccountNumber(), 12, 12)){
			putError(dataEntrySource, ErrorMessages.EP_WALLET_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_EP_ACCOUNT_NUMBER, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, vaccinator.getFirstName())){
			putError(dataEntrySource, ErrorMessages.FIRSTNAME_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_FIRST_NAME, useFieldPrefix);
		}

		if(!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getLastName())
				&& !DataValidation.validate(REG_EX.NAME_CHARACTERS, vaccinator.getLastName())) {
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_LAST_NAME, useFieldPrefix);
		}
		
		if(vaccinator.getEstimatedBirthdate() == null){
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_BIRTHDATE, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge)){//TODO remove this. it is redundant.
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(!birthdateOrAge.equalsIgnoreCase("age") && !birthdateOrAge.equalsIgnoreCase("birthdate")){
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors,  DataField.VACCINATOR_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(birthdateOrAge.toLowerCase().contains("age") && StringUtils.isEmptyOrWhitespaceOnly(ageYears)){
			putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, null, useFieldPrefix);
		}
		
		if(vaccinator.getGender() == null || vaccinator.getGender().equals(Gender.UNKNOWN)){
			putError(dataEntrySource, ErrorMessages.GENDER_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_GENDER, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getQualification())
				|| (vaccinator.getQualification().equals("Other") || vaccinator.getQualification().trim().endsWith(":"))){
			putError(dataEntrySource, ErrorMessages.QUALIFICATION_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_QUALIFICATION, useFieldPrefix);
		}
	}
	
	/**
	 * @param number : a non-null and non-whitespace number validated against the respective telelineType
	 * @param telelineType : type of validation to be used for the number provided, should be in (MOBILE, LANDLINE)
	 * @return {@linkplain ValidatorOutput} 
	 */
	public static ValidatorOutput validateContactNumber(String number, ContactTeleLineType telelineType){
		if(StringUtils.isEmptyOrWhitespaceOnly(number)){
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_EMPTY);
		}
		
		if(telelineType.equals(ContactTeleLineType.MOBILE)){
			if(!DataValidation.validate(REG_EX.CELL_NUMBER, number)){
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_NOT_MOBILE);
			}
			else {
				return new ValidatorOutput(ValidatorStatus.OK, "");
			}
		}
		
		if(telelineType.equals(ContactTeleLineType.LANDLINE)){
			if(!DataValidation.validate("(\\+92|92|0)?[0-9]{7,10}", number)){
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_NOT_LANDLINE);
			}
			else {
				return new ValidatorOutput(ValidatorStatus.OK, "");
			}
		}
		
		return new ValidatorOutput(ValidatorStatus.UNKNOWN, ValidatorOutput.UNKNOWN_STATUS);
	}
	
	/**
	 * @param epiNumber : a non-null and non-whitespace numeric sequence of 8 characters starting with 201
	 * @return
	 */
	private static ValidatorOutput validateEpiNumberString(String epiNumber){
		if(StringUtils.isEmptyOrWhitespaceOnly(epiNumber)){
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_EMPTY);
		}
		
		if(!epiNumber.matches("201.{5}")){
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_INVALID);
		}
		else if(Integer.parseInt(epiNumber.substring(0, 4)) > Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))){
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_YEAR_INVALID);
		}
		
		return new ValidatorOutput(ValidatorStatus.OK, "OK");
	}
	
	/** Validates the EPI number
	 * @param epiNumber : a non-null and non-whitespace numeric sequence of 8 characters starting with 201
	 * @param vaccinationCenterId : ID of a vaccinationCenter existing in DB on which EPI number is assigned. MUST be valid to ensure Uniqueness
	 * @param childId : ID of a child existing in DB to whom EPI number is assigned. MUST be valid to ensure previous
	 * @param ensurePrevious : Flag to ensure if EPI number assigned to specified child was same on last visit on specified center
	 * @param ensureUnique : Whether EPI number should be validated for uniqueness on specified vaccination center
	 * @return {@linkplain ValidatorOutput} 
	 */
	public static ValidatorOutput validateEpiNumber(String epiNumber, int vaccinationCenterId, int childId, boolean ensurePrevious, boolean ensureUnique){
		ValidatorOutput vepi = validateEpiNumberString(epiNumber);
		if(!vepi.STATUS().equals(ValidatorStatus.OK)){
			return vepi;
		}
		
		ServiceContext sc = Context.getServices();
		try{
			if(ensureUnique){
				String q = "select count(*) from vaccination where vaccinationCenterId="+vaccinationCenterId+" and epiNumber='"+epiNumber+"' and childId <> "+childId;
				if(Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0){
					return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_OCCUPIED);
				}
			}
			
			if(ensurePrevious){
				String qp = "select epiNumber from vaccination where length(epiNumber) <> 0 and vaccinationCenterId="+vaccinationCenterId+" and childId = "+childId;
				@SuppressWarnings("rawtypes")
				List qpl = sc.getCustomQueryService().getDataBySQL(qp);
				if(qpl.size() > 0 && !qpl.get(0).toString().equalsIgnoreCase(epiNumber)){
					return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_NOT_MATCHED+" (Previous:New):('"+qpl.get(0).toString()+"':'"+epiNumber+"')");
				}
			}
		}
		finally{
			sc.closeSession();
		}
		
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}
	
	/** Validates the EPI number
	 * @param epiNumber : a non-null and non-whitespace numeric sequence of 8 characters starting with 201
	 * @param vaccinationCenterId : ID of a vaccinationCenter existing in DB on which EPI number is assigned. MUST be valid to ensure Uniqueness
	 * @param ensureUnique : Whether EPI number should be validated for uniqueness on specified vaccination center
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @return {@linkplain ValidatorOutput} 
	 */
	public static ValidatorOutput validateNewEpiNumber(String epiNumber, int vaccinationCenterId, boolean ensureUnique, ServiceContext sc){
		ValidatorOutput vepi = validateEpiNumberString(epiNumber);
		if(!vepi.STATUS().equals(ValidatorStatus.OK)){
			return vepi;
		}
		
		if(ensureUnique){
			String q = "select count(*) from vaccination v join vaccinationcenter vc on v.vaccinationCenterId=vc.mappedId where vc.centerType not like '"+CenterType.UNREGISTERED+"' and vaccinationCenterId="+vaccinationCenterId+" and epiNumber='"+epiNumber+"'";
			if(Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0){
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_OCCUPIED);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}
	
	public static void validateChildStatus(DataEntrySource dataEntrySource, Child child, 	
			HashMap<String, String> mobileErrors, Errors webErrors, boolean useFieldPrefix)
	{
		if(child.getStatus() == null){
			putError(dataEntrySource, ErrorMessages.CHILD_STATUS_MISSING, mobileErrors, webErrors, DataField.CHILD_STATUS, useFieldPrefix);
		}
		else if(child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name())
				&& (child.getTerminationDate() == null || child.getTerminationDate().after(new Date()))){
			putError(dataEntrySource, ErrorMessages.CHILD_TERMINATION_DATE_INVALID, mobileErrors, webErrors, DataField.CHILD_TERMINATION_DATE, useFieldPrefix);
		}
		else if(child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name())
				&& StringUtils.isEmptyOrWhitespaceOnly(child.getTerminationReason())){
			putError(dataEntrySource, ErrorMessages.CHILD_TERMINATION_REASON_MISSING, mobileErrors, webErrors, DataField.CHILD_TERMINATION_REASON, useFieldPrefix);
		}
	}
	
	/**
	 * @param dataEntrySource : MUST be specified
	 * @param childNamed : MUST Be specified
	 * @param child : 
	 * <ul><li>dateEnrolled-  MUST be Non-null PAST date.
	 * <li>firstName- If <code>childNamed</code> is true, it MUST be specified with valid name characters
	 * <li>fatherFirstName- MUST be specified with valid name characters
	 * <li>birthdate- MUST be a Non-null PAST date. Birthdate should be BEFORE(LESS THAN) dateEnrolled
	 * <br>For EnrollmentVaccine (NOT M2 i.e. BCG, P1, P2, P3, M1) - It should not be more than 2 years at enrollment date
	 * <br>For EnrollmentVaccine M2 - It should not be more than 3 years at enrollment date.
	 * <li>estimatedBirthdate- MUST be specified
	 * <li>gender- MUST be either MALE/FEMALE
	 * </ul>	
	 * @param measles2Given : if measles2 was given on enrollment
	 * @param birthdateOrAge: MUST be specified and should only be AGE or BIRTHDATE
	 * @param ageYears : MUST be a valid number between 0 - {@linkplain ValidatorUtils#MAX_AGE_YEARS} , optional otherwise
	 * @param ageMonths : MUST be a valid number between 0 - {@linkplain ValidatorUtils#MAX_AGE_MONTHS} , optional otherwise
	 * @param ageWeeks : MUST be a valid number between 0 - {@linkplain ValidatorUtils#MAX_AGE_WEEKS} , optional otherwise
	 * @param ageDays : MUST be a valid number between 0 - {@linkplain ValidatorUtils#MAX_AGE_DAYS} , optional otherwise
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param webErrors : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateBiographics(DataEntrySource dataEntrySource, Boolean childNamed, Child child, boolean measles2Given, 
			String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays,	
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix)
	{
		if(child.getDateEnrolled() == null || DateUtils.afterTodaysDate(child.getDateEnrolled())){
			putError(dataEntrySource, ErrorMessages.CHILD_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		}
		else if(child.getBirthdate() == null || DateUtils.afterTodaysDate(child.getBirthdate())){
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}
		else if(DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.DATE) < 0){
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}
		else if((!measles2Given && DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS) 
				|| (measles2Given && DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS_MEASLES2) 
				){
			putError(dataEntrySource, ErrorMessages.CHILD_AGE_LIMIT_EXCEEDED, mobileErrors, webErrors, null, measles2Given);
		}
		
		if(child.getEstimatedBirthdate() == null){
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge)){//TODO remove this. it is redundant.
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(!birthdateOrAge.equalsIgnoreCase("age") && !birthdateOrAge.equalsIgnoreCase("birthdate")){
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors,  DataField.CHILD_BIRTHDATE_OR_AGE, useFieldPrefix);
		}
		else if(birthdateOrAge.toLowerCase().contains("age")){
			if(StringUtils.isEmptyOrWhitespaceOnly(ageYears) || !Utils.isNumberBetween(ageYears, 0, MAX_AGE_YEARS)){
				putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_YEARS, useFieldPrefix);
			}
			if(StringUtils.isEmptyOrWhitespaceOnly(ageMonths) || !Utils.isNumberBetween(ageMonths, 0, MAX_AGE_MONTHS)){
				putError(dataEntrySource, ErrorMessages.INVALID_MONTHS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_MONTHS, useFieldPrefix);
			}
			if(StringUtils.isEmptyOrWhitespaceOnly(ageWeeks) || !Utils.isNumberBetween(ageWeeks, 0, MAX_AGE_WEEKS)){
				putError(dataEntrySource, ErrorMessages.INVALID_WEEKS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_WEEKS, useFieldPrefix);
			}
			if(StringUtils.isEmptyOrWhitespaceOnly(ageDays) || !Utils.isNumberBetween(ageDays, 0, MAX_AGE_DAYS)){
				putError(dataEntrySource, ErrorMessages.INVALID_DAYS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_DAYS, useFieldPrefix);
			}
		}
		
		if(childNamed == null){
			putError(dataEntrySource, ErrorMessages.NAME_AVAILABLE, mobileErrors, webErrors, DataField.CHILD_NAMED, useFieldPrefix);
		}
		else if(childNamed && (StringUtils.isEmptyOrWhitespaceOnly(child.getFirstName()) 
				|| !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getFirstName()))){
			putError(dataEntrySource, "Child`s "+ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_FIRST_NAME, useFieldPrefix);
		}
	
		/*if(!StringUtils.isEmptyOrWhitespaceOnly(child.getLastName())
				&& !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getLastName())){
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, DataField.CHILD_LAST_NAME, useFieldPrefix);
		}*/
		
		if(StringUtils.isEmptyOrWhitespaceOnly(child.getFatherFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getFatherFirstName())){
			putError(dataEntrySource, "Father`s " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_FATHER_FIRST_NAME, useFieldPrefix);
		}
	
		/*if(!StringUtils.isEmptyOrWhitespaceOnly(child.getFatherLastName())
				&& !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getFatherLastName())){
			putError(dataEntrySource, "Father`s " + ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, DataField.CHILD_FATHER_LAST_NAME, useFieldPrefix);
		}*/
		
		if(child.getGender() == null || child.getGender().equals(Gender.UNKNOWN)){
			putError(dataEntrySource, ErrorMessages.GENDER_INVALID, mobileErrors, webErrors,  DataField.CHILD_GENDER, useFieldPrefix);
		}
	}
	
	/**
	 * 
	 * @param dataEntrySource : MUST be specified
	 * @param address : 
	 * <ul><li>addHouseNumber, addStreet, addSector, addColony, addLandmark- optional but if provided must not have special characters other than( |/()_,-.(space)` )
	 * <li>addTown- MUST be provided
	 * <li>AddUc- MUST be provided
	 * <li>cityId- MUST be provided
	 * <li>cityName- If cityId provided is referring to Other option then this field must have a value 
	 * </ul>
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile)
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise
	 */
	public static void validateAddress(DataEntrySource dataEntrySource, Address address, HashMap<String, String> mobileErrors, Errors error, boolean useFieldPrefix)
	{
		if(StringUtils.isEmptyOrWhitespaceOnly( address.getAddHouseNumber())){
		}
		else if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getAddHouseNumber())){
			putError(dataEntrySource, ErrorMessages.ADDRESS_HOUSE_NUMBER_INVALID, mobileErrors, error, DataField.ADDRESS_HOUSE_NUMBER, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddStreet())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getAddStreet())){
				putError(dataEntrySource, ErrorMessages.ADDRESS_STREET_INVALID, mobileErrors, error, DataField.ADDRESS_STREET, useFieldPrefix);
			}
		}

		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddSector())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getAddSector())){
				putError(dataEntrySource, ErrorMessages.ADDRESS_SECTOR_INVALID, mobileErrors, error, DataField.ADDRESS_SECTOR, useFieldPrefix);
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddColony())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getAddColony())){
				putError(dataEntrySource, ErrorMessages.ADDRESS_COLONY_INVALID, mobileErrors, error, DataField.ADDRESS_COLONY, useFieldPrefix);
			}
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddtown())){
			putError(dataEntrySource, ErrorMessages.ADDRESS_TOWN_MISSING, mobileErrors, error, DataField.ADDRESS_TOWN, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddUc())){
			putError(dataEntrySource, ErrorMessages.ADDRESS_UC_MISSING, mobileErrors, error, DataField.ADDRESS_UC, useFieldPrefix);
		}
		
		if(StringUtils.isEmptyOrWhitespaceOnly(address.getAddLandmark())){
		}
		else{
			if(!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getAddLandmark())){
				putError(dataEntrySource, ErrorMessages.ADDRESS_LANDMARK_INVALID, mobileErrors, error, DataField.ADDRESS_LANDMARK, useFieldPrefix);
			}
		}
		
		if(address.getCityId() == null){
			putError(dataEntrySource, ErrorMessages.ADDRESS_CITY_MISSING, mobileErrors, error, DataField.ADDRESS_CITY_ID, useFieldPrefix);
		}
		else if(address.getCityId() == WebGlobals.OTHER_OPTION_ID_IN_DB
				&& StringUtils.isEmptyOrWhitespaceOnly(address.getCityName())){
			putError(dataEntrySource, ErrorMessages.ADDRESS_OTHER_CITY_MISSING, mobileErrors, error, DataField.ADDRESS_CITY_NAME, useFieldPrefix);
		}
	}
	
	/** Validates vaccination that is associated on form filled after Lottery has been run using LOttery Generator.
	 * @param dataEntrySource : MUST be specified
	 * @param child : child object for which vaccination is being validated. 
	 * Note: this method doesnot validate child object, but just based on some properties of child, do validation of vaccination.  
 	 * @param isNewEnrollment : if vaccination is for Enrollment
	 * @param currentVaccination : Vaccination instance that was created at time of lottery generation. 
	 * For Enrollment vaccination vaccinationDate, vaccinationDueDate and child`s enrollmentDate should be EQUAL
	 * <ul>
	 * <li>vaccineId - MUST be specified and should be equal to the one specified in Lottery generation process
	 * <li>vaccinationStatus- MUST be VACCINATED
	 * <li>vaccinationCenterId- MUST be specified and should be equal to the one specified in Lottery generation process
	 * <li>vaccinatorId- MUST be specified and should be equal to the one specified in Lottery generation process
	 * <li>vaccinationDuedate- MUST be a Non-null valid date (if IS NEW ENROLLMENT it must be equal to vaccinationDate) 
	 * <li>vaccinationDate- MUST be Non-null and equal to the lottery generation date
	 * <li>polioVaccineGiven, pcvGiven- MUST be specified for MOBILE entries
	 * <li>hasApprovedLottery- MUST be TRUE
	 * <li>nextAssignedDate- MUST be specified for mobile entries for all vaccines and SHOULD be NULL if {@code nextVaccine} is NULL
	 * <li>epiNumber- MUST be Non-null and equal to the one specified in Lottery generation process
	 * </ul>	 
	 * @param nextVaccine : NonNull instance of vaccine existing in Database that would be scheduled next for child (@see {@linkplain ValidatorUtils#validateNextVaccine})
	 * This param can only be null IFF  currentVaccine is [Measles2 OR (P3 and M1 vaccinated)]. However, only for WEB entries it could be ignored IFF @param ignoreNextVaccineIfNull is TRUE
	 * @param ignoreNextVaccineIfNull This param MUST be specified if {@code nextVaccine} should be ignored for NULLs(only for WEB entries)
	 * 
	 * @param encounter : Encounter object of lottery generation process
	 * @param encreslist : EncounterResults of lottery generation process of above encounter
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	private static void validateVaccinationFillLotteryGenForm(DataEntrySource dataEntrySource, Child child, boolean isNewEnrollment, Vaccination currentVaccination, 
			Vaccine nextVaccine, Boolean ignoreNextVaccineIfNull, Encounter encounter, List<EncounterResults> encreslist, 
			ArrayList<String> mobileErrors, Errors error, ServiceContext sc, boolean useFieldPrefix)
	{
		/*if(sc.getCustomQueryService().getDataBySQL("select count(*) from vaccination where vaccinationRecordNum="+currentVaccination.getVaccinationRecordNum()).size() == 0){
			putError(dataEntrySource, "VaccinationRecordNum:"+ErrorMessages.SPECIFIED_VACCINE_NOT_RECEIVED_FOR_CHILD, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccineId", useFieldPrefix));
		}
		else if(!isValueEqual(currentVaccination.getVaccinationCenterId(), EncounterUtil.getElementValueFromEncResult("VACCINATION_RECORD_NUM", encreslist), true)){
			putError(dataEntrySource, "VaccinationRecordNum:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccineId", useFieldPrefix));
    	}
		
		if(!isValueEqual(currentVaccination.getVaccinationCenterId(), EncounterUtil.getElementValueFromEncResult("VACCINATION_CENTER", encreslist), true)){
			putError(dataEntrySource, "VaccinationCenter:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccinationCenterId", useFieldPrefix));
    	}
    	
    	if(!isValueEqual(currentVaccination.getVaccinatorId(), encounter.getId().getP2id(), true)){
			putError(dataEntrySource, "Vaccinator:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccinatorId", useFieldPrefix));
    	}
    	
    	if(!isValueEqual(currentVaccination.getEpiNumber(), EncounterUtil.getElementValueFromEncResult("EPI_NUMBER", encreslist), true)){
			putError(dataEntrySource, "EpiNumber:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "epiNumber", useFieldPrefix));
		}	
		
    	if(!isValueEqual(currentVaccination.getVaccineId(), EncounterUtil.getElementValueFromEncResult("VACCINE_ID_RECEIVED", encreslist), true)){
			putError(dataEntrySource, "Vaccine:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccineId", useFieldPrefix));
		}
    	
    	if(currentVaccination.getVaccinationDate() == null
				|| !DateUtils.datesEqual(currentVaccination.getVaccinationDate(), encounter.getDateEncounterEntered())){
			putError(dataEntrySource, "VaccinationDate:"+ErrorMessages.NOT_EQUAL_TO_LAST_FILLED_VALUE, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccinationDate", useFieldPrefix));
		}   	
    	else if(currentVaccination.getVaccinationDate().after(new Date())){
			putError(dataEntrySource, ErrorMessages.VACCINATION_VACCINATION_DATE_INVALID, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccinationDate", useFieldPrefix));
		}
    	else if(currentVaccination.getVaccinationDuedate() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_DUEDATE_INVALID, mobileErrors, error, getFieldName(CURRENT_VACCINATION_PRF, "vaccinationDuedate", useFieldPrefix));
		}
    	// Program assumes that dateEnrolled, vacinationDate, vaccinationDuedate should be equal for enrollment 
		// as all fields are for event information of same datetime
    	else if(isNewEnrollment && child.getDateEnrolled() != null 
				&& !DateUtils.datesEqual(child.getDateEnrolled(), currentVaccination.getVaccinationDate()) 
				|| !DateUtils.datesEqual(currentVaccination.getVaccinationDate(), currentVaccination.getVaccinationDuedate())){
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_EQUAL_DATES_REQUIRED, mobileErrors, error, getFieldName(CHILD_PRF, "dateEnrolled", useFieldPrefix));
		}
    	
		if(dataEntrySource.equals(DataEntrySource.MOBILE) && currentVaccination.getPolioVaccineGiven() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_POLIO_VACCINE_MISSING, mobileErrors, null, null);
		}
		
		if(dataEntrySource.equals(DataEntrySource.MOBILE) && currentVaccination.getPcvGiven() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_PCV_MISSING, mobileErrors, null, null);
		}
		
		if(currentVaccination.getHasApprovedLottery() == null
				|| !currentVaccination.getHasApprovedLottery()){
			putError(dataEntrySource, "Lottery approval should be YES, as lottery has taken place", mobileErrors, null, null);
		}
		
		if(currentVaccination.getVaccinationStatus() == null
				|| !currentVaccination.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED)){
			putError(dataEntrySource, "VaccinationStatus should only be Vaccinated", mobileErrors, error, null);
			return;
		}
		
		validateNextVaccine(dataEntrySource, currentVaccination, nextVaccine, ignoreNextVaccineIfNull, mobileErrors, error, sc, isNewEnrollment, useFieldPrefix);*/
	}
	
	private static void validateVaccinationSchedule(DataEntrySource dataEntrySource, Child child, boolean isNewEnrollment, VaccinationCenterVisit centerVisit, 
			List<VaccineSchedule> vaccineSchedule, Boolean ignoreNextVaccineIfNull, HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean useFieldPrefix)
	{
		if(!isNewEnrollment && centerVisit.getChildId() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_CHILD_ID_MISSING, mobileErrors, error, DataField.CENTER_VISIT_CHILD_ID, useFieldPrefix);
			return;
		}
		
		if(centerVisit.getVaccinationCenterId() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATION_CENTER_ID, useFieldPrefix);
			return;
		}
    	
		if(child.getBirthdate() == null){
			putError(dataEntrySource, "Can not proceed without child`s birthdate", mobileErrors, error, null, useFieldPrefix);
			return;
		}
		
		
		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, child.getDateEnrolled());
		if(centerVisit.getVisitDate() == null 
				|| centerVisit.getVisitDate().after(new Date())){
			putError(dataEntrySource, ErrorMessages.VACCINATION_VISIT_DATE_INVALID, mobileErrors, error, DataField.CENTER_VISIT_VISIT_DATE, useFieldPrefix);
			return;
		}
    	// check if user nullable dates are provided
    	// Program assumes that dateEnrolled, vacinationDate, vaccinationDuedate should be equal for enrollment 
		// as all fields are for event information of same datetime
    	else if(isNewEnrollment && child.getDateEnrolled() != null 
				&& (!DateUtils.datesEqual(child.getDateEnrolled(), centerVisit.getVisitDate()))){
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_EQUAL_DATES_REQUIRED, mobileErrors, error, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
			return;
    	}
    	else if(!isNewEnrollment 
    			&& (!measles2Given  && DateUtils.differenceBetweenIntervals(centerVisit.getVisitDate(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS) 
				&& (measles2Given && DateUtils.differenceBetweenIntervals(centerVisit.getVisitDate(), child.getBirthdate(), TIME_INTERVAL.YEAR) > 3) 
				){
			putError(dataEntrySource, ErrorMessages.CHILD_AGE_LIMIT_EXCEEDED, mobileErrors, error, null, useFieldPrefix);
			return;
    	}
		
		ArrayList<VaccineSchedule> defSch = VaccineSchedule.generateDefaultSchedule(child.getBirthdate(), centerVisit.getVisitDate(), centerVisit.getChildId(), centerVisit.getVaccinationCenterId(), true);
		
		for (VaccineSchedule dfvsh : defSch) {
			VaccineSchedule vsobj = null;
			for (VaccineSchedule vs : vaccineSchedule) {
				if(dfvsh.getVaccine().getName().equalsIgnoreCase(vs.getVaccine().getName())){
					vsobj = vs;
					break;
				}
				
			}
			
			if(dfvsh.getStatus() == null || !dfvsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())){
				// vaccine was retro or should have been vaccinated yet its status must be provided.
				// or if vaccine has to be scheduled then a next date must be provided. it cant be left blank
				if(dfvsh.getSchedule_duedate() != null  
						&& (dfvsh.getIs_retro_suspect() || dfvsh.getIs_current_suspect()) 
					&& IMRUtils.passVaccinePrerequisiteCheck(vsobj==null?dfvsh:vsobj, vaccineSchedule) 
					&& (dfvsh.getExpiry_date() != null && dfvsh.getExpiry_date().after(new Date()))
					&& (vsobj == null || StringUtils.isEmptyOrWhitespaceOnly(vsobj.getStatus())) 
					&& !dfvsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())){
					putError(dataEntrySource, dfvsh.getVaccine().getName()+" info and status must be provided. It cannot be left blank", mobileErrors, error, null, useFieldPrefix);
				}
				else if(vsobj != null){
					if(!StringUtils.isEmptyOrWhitespaceOnly(vsobj.getStatus())
							&& !dfvsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())){
						if(!vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())
								&& vsobj.getCenter() == null){
							putError(dataEntrySource, dfvsh.getVaccine().getName()+" vaccination center must be provided", mobileErrors, error, null, useFieldPrefix);
						}
						else if(!(vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name()) 
								|| vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name()))
								&& vsobj.getVaccination_date() == null){
							putError(dataEntrySource, dfvsh.getVaccine().getName()+" vaccination date must be provided", mobileErrors, error, null, useFieldPrefix);
						}
						else if(vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())
								&& vsobj.getAssigned_duedate() == null){
							putError(dataEntrySource, dfvsh.getVaccine().getName()+" assigned duedate must be specified", mobileErrors, error, null, useFieldPrefix);
						}
						else if(vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())
								&& (vsobj.getVaccination_date() == null || !DateUtils.datesEqual(vsobj.getVaccination_date(),centerVisit.getVisitDate()))){
							putError(dataEntrySource, dfvsh.getVaccine().getName()+" vaccination date must be equal to center visit date", mobileErrors, error, null, useFieldPrefix);
						}
						else if(vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())
								&& !isNewEnrollment && sc.getVaccinationService().findByCriteria(centerVisit.getChildId().intValue(), vsobj.getVaccine().getVaccineId(), VACCINATION_STATUS.VACCINATED, 0, 2, true, null).size()>0){
							putError(dataEntrySource, dfvsh.getVaccine().getName()+" already has been given. Can not duplicate records", mobileErrors, error, null, useFieldPrefix);
						}
					}
					
					if(vsobj.getVaccination_date() != null 
							&& (vsobj.getVaccination_date().before(child.getBirthdate())
									|| DateUtils.differenceBetweenIntervals(vsobj.getVaccination_date(), centerVisit.getVisitDate(), TIME_INTERVAL.DATE) >= 1)){
						putError(dataEntrySource, dfvsh.getVaccine().getName()+" vaccination date can not be before birthdate or after enrollment date", mobileErrors, error, null, useFieldPrefix);
					}
					
					
				}
			}
		}
    	
    	if(centerVisit.getVaccinatorId() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATOR_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATOR_ID, useFieldPrefix);
    	}

		// for M2 some params should be null
		/*if(measles2Given)
		{
			if(isNewEnrollment && centerVisit.getHasApprovedLottery() != null){
				putError(dataEntrySource, ErrorMessages.VACCINATION_LOTTERY_APPROVAL_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, DataField.CENTER_VISIT_HAS_APPROVED_LOTTERY, useFieldPrefix);
			}
		}
		else if(dataEntrySource.equals(DataEntrySource.MOBILE) 
    				&& centerVisit.getHasApprovedLottery() == null){
			putError(dataEntrySource, ErrorMessages.VACCINATION_LOTTERY_MISSING, mobileErrors, error, DataField.CENTER_VISIT_HAS_APPROVED_LOTTERY, useFieldPrefix);
		}*/
		
		if(centerVisit.getVaccinationCenterId() != null){
			ValidatorOutput vepi = null;
			if(isNewEnrollment)
			{
				vepi = ValidatorUtils.validateNewEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), false, sc);
			}
			else if(!isNewEnrollment && centerVisit.getChildId() != null){
				vepi = ValidatorUtils.validateEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), centerVisit.getChildId(), false, false/*dataEntrySource.equals(DataEntrySource.MOBILE)*/);
			}
			
			if(!vepi.STATUS().equals(ValidatorStatus.OK)){
				putError(dataEntrySource, vepi.MESSAGE(), mobileErrors, error, DataField.CENTER_VISIT_EPI_NUMBER, useFieldPrefix);
			}
		}
	}
/*	
	*/
	/** Validates vaccination for edits only validates missing params. No validation of schedule is done
	 * @param dataEntrySource : MUST be specified
	 * @param child : child object for which vaccination is being validated. 
	 * Note: this method doesnot validate child object, but just based on some properties of child, do validation of vaccination.  
	 * @param vaccination : 
	 * <ul>
	 * <li>childId- MUST be specified 
	 * <li>vaccinationStatus- MUST be specified <br>
	 * <b>For status PENDING</b>  (@see vaccination of {@linkplain ValidatorUtils#validatePendingVaccination})<br>
	 * 	<li>(vaccinationCenterId, vaccinatorId, vaccinationDate SHOULD be NULL)
	 * 	<li>vaccinationDuedate must be specified <br>
	 * <b>For status OTHER THAN PENDING</b> <br>
	 * 	<li>vaccinationCenterId- MUST be specified from centers existing in DB <br>
	 * 	<li>vaccinatorId- MUST be specified from vaccinators existing in DB <br>
	 * 	<li>vaccinationDate- MUST be a valid, past date 
	 * 	<li>epiNumber- MUST be Non-null, SHOULD be same as epiNumber of previous vaccination on given center if any exists
	 * </ul>
	 *
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateVaccination(DataEntrySource dataEntrySource, Child child, Vaccination vaccination, 
			HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean useFieldPrefix)
	{
		if(vaccination.getChildId() == null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_CHILD_ID_MISSING, mobileErrors, error, null, useFieldPrefix);
    	}
		
		if(vaccination.getVaccinationStatus() == null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_STATUS_MISSING, mobileErrors, error, null, useFieldPrefix);
			return;
		}
		
		// SHOULD ONLY BE IN CASE OF VACCINE EDITS
		if(vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING))
    	{
	    	validatePendingVaccination(dataEntrySource, vaccination, mobileErrors, error, useFieldPrefix);
    	}
    	else
    	{
    		if(vaccination.getVaccinationCenterId() == null){
				putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATION_CENTER_ID, useFieldPrefix);
	    	}
	    	
	    	if(vaccination.getVaccinatorId() == null){
				putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATOR_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATOR_ID, useFieldPrefix);
	    	}

	    	if(vaccination.getVaccinationDate() == null 
    				|| vaccination.getVaccinationDate().after(new Date())){
				putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_VISIT_DATE_INVALID, mobileErrors, error, null, useFieldPrefix);
    		}
	    	    		
    		if(vaccination.getVaccinationCenterId() != null){
    			ValidatorOutput vepi = null;
    			if(vaccination.getChildId() != null){
    				vepi = ValidatorUtils.validateEpiNumber(vaccination.getEpiNumber(), vaccination.getVaccinationCenterId(), vaccination.getChildId(), true, dataEntrySource.equals(DataEntrySource.MOBILE));
    			}
    			
    			if(!vepi.STATUS().equals(ValidatorStatus.OK)){
    				putError(dataEntrySource, vaccination.getVaccine().getName()+": "+vepi.MESSAGE(), mobileErrors, error, null, useFieldPrefix);
    			}
    		}
    	}
	}
	
	/** Validates the Next Assigned Vaccine. Normally vaccines are given according to following schedule <br>
	 * BCG, Penta1, Penta2, Penta3, Measles1, Measles2 <br>
	 * Hence, Next assigned vaccine can only be empty in case of Measles2. <br>
	 * However, for some cases it may not be followed properly. Such vaccines should only be handled via web app. <br>
	 * Possibilities that may allow next vaccine to be empty for Mobile entries are : <br>
	 * Current vaccine is Measles2 OR Current vaccine is Penta3 AND Measles1 has been vaccinated earlier. <br>
	 * For special cases when Next vaccine from Web should be ignored, @param ignoreNextVaccineIfNull should be TRUE.<br>
	 * For VACCINATED vaccinations next vaccine should only be according to the rule, and for vaccinations NOT_VACCIANTED next vaccine should be same as cuurent vaccine so that child could get it on next visit.
	 * Next vaccine should have not been given earlier.
	 * @param dataEntrySource : MUST be provided
	 * @param centerVisit : This function doesnot validate centerVisit, but just uses some properties to validate nextVaccine. For validation of this object use any other appropriate method
	 * <li> nextVaccine : MUST be specified with non-empty map
	 * This param can only be null IFF  currentVaccine contains [Measles2 OR (P3 and M1 vaccinated)]. However, only for WEB entries it could be ignored IFF @param ignoreNextVaccineIfNull is TRUE
	 * @param ignoreNextVaccineIfNull This param MUST be specified if {@code nextVaccine} should be ignored for NULLs(only for WEB entries)
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link ChildValidator} that called the function (Must be provided if dataEntrySource is Web)<br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link ChildValidator} , ignored otherwise
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 *//*
	private static void validateCurrentVaccine(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, 
			HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean isNewEnrollment, boolean useFieldPrefix){
		boolean measles2Given = IRUtils.findStringInList(centerVisit.getVaccineGiven(), GlobalParams.MEASLES2_NAME_IN_DB, true) > -1?true:false;
		boolean penta3Given = IRUtils.findStringInList(centerVisit.getVaccineGiven(), GlobalParams.PENTA3_NAME_IN_DB, true) > -1?true:false;

		// if currentVaccine is NOT Measles2 OR (NOT P3 and M1 vaccinated) and nextVaccine is NULL and entry is from MOBILE 
		// or coder didnot explicitly mention to ignore nextVaccine
		boolean isM1Vaccinated = (!isNewEnrollment && penta3Given?// if NOT new enrollment and current vaccine is P3 check if M1 is VACCINATED else ignore
				Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT count(*) FROM vaccination v JOIN vaccine vc ON v.vaccineId=vc.vaccineId WHERE v.childId="+centerVisit.getChildId()+" AND vc.name like '%"+GlobalParams.MEASLES1_NAME_IN_DB+"%' and vaccinationStatus ='"+VACCINATION_STATUS.VACCINATED+"'").get(0).toString()) != 0
				:false);
		if(centerVisit.getVaccineGiven().size() > 0 
				&& !(measles2Given ||(penta3Given && isM1Vaccinated))
				&& centerVisit.getVaccineNext().size() == 0
				&& (dataEntrySource.equals(DataEntrySource.MOBILE)
						|| !ignoreNextVaccineIfNull)){
			putError(dataEntrySource, ErrorMessages.VACCINATION_NEXT_VACCINE_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINE_NEXT, useFieldPrefix);
		}
		
//		if(centerVisit.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED) 
//				&& centerVisit.getVaccineGiven().size() > 0  && centerVisit.getVaccineNext().size() > 0){
//			Vaccine nextV = IMRUtils.getNextVaccine(currentVaccine.getName(), sc);
//			String nn = (nextV==null?"N/A":nextV.getName());
//			if(!nn.equalsIgnoreCase(nextVaccine.getName())){
//				putError(dataEntrySource, "Next vaccine expected is "+nn+" but specified one is "+nextVaccine.getName(), mobileErrors, error, null);
//			}
//		}
		
		// same vaccine should be scheduled if child was not vaccinated
		if(centerVisit.getVaccinationStatus().equals(VACCINATION_STATUS.NOT_VACCINATED)
				&& centerVisit.getVaccineGiven().size() > 0  && centerVisit.getVaccineNext().size() > 0
				&& !currentVaccine.getName().equalsIgnoreCase(nextVaccine.getName())){
			putError(dataEntrySource, "Next vaccine expected is "+currentVaccine.getName()+" but specified one is "+nextVaccine.getName(), mobileErrors, error, null);
		}
		
		
		for (Entry<String, NextVaccineMap> entry : centerVisit.getVaccineNext().entrySet()) {
			if(IRUtils.findStringInList(centerVisit.getVaccineGiven(), entry.getKey(), true) > -1){
				putError(dataEntrySource, entry.getKey()+" has already been given. It should not be scheduled again", mobileErrors, error, null, useFieldPrefix);
			}
			if(entry.getValue().getAssignedDate() == null || entry.getValue().getAssignedDate().before(centerVisit.getVaccinationDate())){
				putError(dataEntrySource, entry.getKey()+" next assigned date must be a valid date after vaccination date", mobileErrors, error, null, useFieldPrefix);
			}
			
			if(entry.getValue().getAutoCalculatedDate() == null || entry.getValue().getAutoCalculatedDate().before(centerVisit.getVaccinationDate())){
				putError(dataEntrySource, entry.getKey()+" auto calculated date must be a valid date after vaccination date", mobileErrors, error, null, useFieldPrefix);
			}
		}
		
		if(!isNewEnrollment && centerVisit.getVaccineNext().size() > 0){
			for (Entry<String, NextVaccineMap> entry : centerVisit.getVaccineNext().entrySet()) {
				if(sc.getVaccinationService().findVaccinationRecordByCriteria(centerVisit.getChildId().intValue(), entry.getKey(), null, null, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 1, true, null, null).size() > 0){
					putError(dataEntrySource, entry.getKey()+" "+ErrorMessages.VACCINATION_NEXT_VACCINE_RECEIVED, mobileErrors, error, null, useFieldPrefix);
				}
			}
		}
	}
	
	*//** Validates the Next Assigned Vaccine. Normally vaccines are given according to following schedule <br>
	 * BCG, Penta1, Penta2, Penta3, Measles1, Measles2 <br>
	 * Hence, Next assigned vaccine can only be empty in case of Measles2. <br>
	 * However, for some cases it may not be followed properly. Such vaccines should only be handled via web app. <br>
	 * Possibilities that may allow next vaccine to be empty for Mobile entries are : <br>
	 * Current vaccine is Measles2 OR Current vaccine is Penta3 AND Measles1 has been vaccinated earlier. <br>
	 * For special cases when Next vaccine from Web should be ignored, @param ignoreNextVaccineIfNull should be TRUE.<br>
	 * For VACCINATED vaccinations next vaccine should only be according to the rule, and for vaccinations NOT_VACCIANTED next vaccine should be same as cuurent vaccine so that child could get it on next visit.
	 * Next vaccine should have not been given earlier.
	 * @param dataEntrySource : MUST be provided
	 * @param centerVisit : This function doesnot validate centerVisit, but just uses some properties to validate nextVaccine. For validation of this object use any other appropriate method
	 * <li> nextVaccine : MUST be specified with non-empty map
	 * This param can only be null IFF  currentVaccine contains [Measles2 OR (P3 and M1 vaccinated)]. However, only for WEB entries it could be ignored IFF @param ignoreNextVaccineIfNull is TRUE
	 * @param ignoreNextVaccineIfNull This param MUST be specified if {@code nextVaccine} should be ignored for NULLs(only for WEB entries)
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link ChildValidator} that called the function (Must be provided if dataEntrySource is Web)<br>
	 * @param sc {@linkplain ServiceContext} Object that SHOULD be CLOSED after validation
	 * @param useFieldPrefix : if would be used for spring (web) {@link ChildValidator} , ignored otherwise
	 * 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 *//*
	private static void validateNextVaccine(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, Boolean ignoreNextVaccineIfNull, 
			HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean isNewEnrollment, boolean useFieldPrefix){
		boolean measles2Given = IRUtils.findStringInList(centerVisit.getVaccineGiven(), GlobalParams.MEASLES2_NAME_IN_DB, true) > -1?true:false;
		boolean penta3Given = IRUtils.findStringInList(centerVisit.getVaccineGiven(), GlobalParams.PENTA3_NAME_IN_DB, true) > -1?true:false;

		// if currentVaccine is NOT Measles2 OR (NOT P3 and M1 vaccinated) and nextVaccine is NULL and entry is from MOBILE 
		// or coder didnot explicitly mention to ignore nextVaccine
		boolean isM1Vaccinated = (!isNewEnrollment && penta3Given?// if NOT new enrollment and current vaccine is P3 check if M1 is VACCINATED else ignore
				Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT count(*) FROM vaccination v JOIN vaccine vc ON v.vaccineId=vc.vaccineId WHERE v.childId="+centerVisit.getChildId()+" AND vc.name like '%"+GlobalParams.MEASLES1_NAME_IN_DB+"%' and vaccinationStatus ='"+VACCINATION_STATUS.VACCINATED+"'").get(0).toString()) != 0
				:false);
		if(centerVisit.getVaccineGiven().size() > 0 
				&& !(measles2Given ||(penta3Given && isM1Vaccinated))
				&& centerVisit.getVaccineNext().size() == 0
				&& (dataEntrySource.equals(DataEntrySource.MOBILE)
						|| !ignoreNextVaccineIfNull)){
			putError(dataEntrySource, ErrorMessages.VACCINATION_NEXT_VACCINE_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINE_NEXT, useFieldPrefix);
		}
		
//		if(centerVisit.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED) 
//				&& centerVisit.getVaccineGiven().size() > 0  && centerVisit.getVaccineNext().size() > 0){
//			Vaccine nextV = IMRUtils.getNextVaccine(currentVaccine.getName(), sc);
//			String nn = (nextV==null?"N/A":nextV.getName());
//			if(!nn.equalsIgnoreCase(nextVaccine.getName())){
//				putError(dataEntrySource, "Next vaccine expected is "+nn+" but specified one is "+nextVaccine.getName(), mobileErrors, error, null);
//			}
//		}
		
		// same vaccine should be scheduled if child was not vaccinated
		if(centerVisit.getVaccinationStatus().equals(VACCINATION_STATUS.NOT_VACCINATED)
				&& centerVisit.getVaccineGiven().size() > 0  && centerVisit.getVaccineNext().size() > 0
				&& !currentVaccine.getName().equalsIgnoreCase(nextVaccine.getName())){
			putError(dataEntrySource, "Next vaccine expected is "+currentVaccine.getName()+" but specified one is "+nextVaccine.getName(), mobileErrors, error, null);
		}
		
		
		for (Entry<String, NextVaccineMap> entry : centerVisit.getVaccineNext().entrySet()) {
			if(IRUtils.findStringInList(centerVisit.getVaccineGiven(), entry.getKey(), true) > -1){
				putError(dataEntrySource, entry.getKey()+" has already been given. It should not be scheduled again", mobileErrors, error, null, useFieldPrefix);
			}
			if(entry.getValue().getAssignedDate() == null || entry.getValue().getAssignedDate().before(centerVisit.getVaccinationDate())){
				putError(dataEntrySource, entry.getKey()+" next assigned date must be a valid date after vaccination date", mobileErrors, error, null, useFieldPrefix);
			}
			
			if(entry.getValue().getAutoCalculatedDate() == null || entry.getValue().getAutoCalculatedDate().before(centerVisit.getVaccinationDate())){
				putError(dataEntrySource, entry.getKey()+" auto calculated date must be a valid date after vaccination date", mobileErrors, error, null, useFieldPrefix);
			}
		}
		
		if(!isNewEnrollment && centerVisit.getVaccineNext().size() > 0){
			for (Entry<String, NextVaccineMap> entry : centerVisit.getVaccineNext().entrySet()) {
				if(sc.getVaccinationService().findVaccinationRecordByCriteria(centerVisit.getChildId().intValue(), entry.getKey(), null, null, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 1, true, null, null).size() > 0){
					putError(dataEntrySource, entry.getKey()+" "+ErrorMessages.VACCINATION_NEXT_VACCINE_RECEIVED, mobileErrors, error, null, useFieldPrefix);
				}
			}
		}
	}
	
	private static void validateVaccineElseWhere(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, 
			HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean isNewEnrollment, boolean useFieldPrefix){
		for (Entry<String, VaccineElseWhere> entry : centerVisit.getVaccineElseWhere().entrySet()) {
			if(IRUtils.findStringInList(centerVisit.getVaccineGiven(), entry.getKey(), true) > -1){
				putError(dataEntrySource, entry.getKey()+" has already been given. It should not be recorded twice", mobileErrors, error, null, useFieldPrefix);
			}
			if(centerVisit.getVaccineNext().containsKey(entry.getKey())){
				putError(dataEntrySource, entry.getKey()+" has been scheduled. It should not be scheduled again", mobileErrors, error, null, useFieldPrefix);
			}
			if(entry.getValue().getVaccinationDate() == null){
				putError(dataEntrySource, entry.getKey()+" vaccination date must be a valid date", mobileErrors, error, null, useFieldPrefix);
			}
			
			if(entry.getValue().getVaccinationCenterId() == null){
				putError(dataEntrySource, entry.getKey()+" vaccination center id must be specified", mobileErrors, error, null, useFieldPrefix);
			}
		}
		
		if(!isNewEnrollment && centerVisit.getVaccineNext().size() > 0){
			for (Entry<String, NextVaccineMap> entry : centerVisit.getVaccineNext().entrySet()) {
				if(sc.getVaccinationService().findVaccinationRecordByCriteria(centerVisit.getChildId().intValue(), entry.getKey(), null, null, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 1, true, null, null).size() > 0){
					putError(dataEntrySource, entry.getKey()+" "+ErrorMessages.VACCINATION_ELSE_WHERE_VACCINE_EXISTS, mobileErrors, error, null, useFieldPrefix);
				}
			}
		}
	}
	
	
	*/
	/** Validates PENDING vaccination. This function is needed when editing a PENDING vaccination to assign a different duedate
	 * @param dataEntrySource : MUST be provided, for Mobile Entries control should never reach there.
	 * @param vaccination : 
	 * <li>vaccinationCenterId - MUST be NULL as is not applicable
	 * <li>vaccinatorId - MUST be NULL as is not applicable
	 * <li>vaccinationDuedate - MUST be a non-null valid date
	 * <li>vaccinationDate - MUST be NULL as is not applicable
	 * </ul>	 
	 * 
	 * @param mobileErrors : List object that would contain mobile error messages (Must be provided if dataEntrySource is Mobile) <br>
	 * @param error : {@link Errors} object of spring (web) {@link Validator} that called the function <br>
	 * @param useFieldPrefix : if would be used for spring (web) {@link Validator} , ignored otherwise <br>
	 * 	 
	 * All errors occurred during validation would be populated into mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	private static void validatePendingVaccination(DataEntrySource dataEntrySource, Vaccination vaccination, 
			HashMap<String, String> mobileErrors, Errors error, boolean useFieldPrefix)
	{
		if(dataEntrySource.equals(DataEntrySource.MOBILE)){
			putError(dataEntrySource, ErrorMessages.DATA_ENTRY_SOURCE_INVALID, mobileErrors, error, null, useFieldPrefix);
			return;
		}
		if(!vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.PENDING))
    	{
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_STATUS_PENDING_REQUIRED, mobileErrors, error, null, useFieldPrefix);
    	}
		
    	if(vaccination.getVaccinationCenterId() != null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_CENTER_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
    	}
    	
    	if(vaccination.getVaccinatorId() != null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATOR_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
    	}
		
    	if(vaccination.getVaccinationDuedate() == null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_DUEDATE_INVALID, mobileErrors, error, null, useFieldPrefix);
		}
    	
		if(vaccination.getVaccinationDate() != null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_VACCINATION_DATE_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}
		
		if(vaccination.getHasApprovedLottery() != null){
			putError(dataEntrySource, vaccination.getVaccine().getName()+": "+ErrorMessages.VACCINATION_LOTTERY_APPROVAL_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}
	}
}
