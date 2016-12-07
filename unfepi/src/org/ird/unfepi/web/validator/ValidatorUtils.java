package org.ird.unfepi.web.validator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.DataField;
import org.ird.unfepi.constants.ErrorMessages;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenter.CenterType;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.ird.unfepi.utils.validation.DataValidation;
import org.ird.unfepi.utils.validation.REG_EX;
import org.ird.unfepi.web.utils.IMRUtils;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.ird.unfepi.web.validator.ValidatorOutput.ValidatorStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.mysql.jdbc.StringUtils;

public class ValidatorUtils {

	// private static final String CHILD_PRF = "child.";
	// private static final String IDMAPPER_PRF = "idMapper.";
	// private static final String CURRENT_VACCINATION_PRF = "centerVisit.";
	// private static final String ADDRESS_PRF = "address.";
	// private static final String PREFERENCE_PRF = "centerVisit.preference.";
	// private static final String CONTACT_GEN_PRF = "contactNumber.";
	// private static final String STOREKEEPER_PRF = "storekeeper.";
	/*
	 * private static final String VACCINATOR_PRF = "vaccinator."; private
	 * static final String USER_GEN_PRF = "user.";
	 */

	public static final boolean IGNORE_NEXT_VACCINE_FOLLOWUP_FORM = false;
	public static final boolean IGNORE_NEXT_VACCINE_FOLLOWUP_PRIVILEGED_FORM = true;

	static final int MAX_AGE_YEARS_MEASLES2 = 3;
	static final int MAX_AGE_YEARS_OTHER_VACCINES = 2;
	static final int MAX_AGE_YEARS = 3;
	static final int MAX_AGE_MONTHS = 36;
	static final int MAX_AGE_WEEKS = 52;
	static final int MAX_AGE_DAYS = 365;

	/**
	 * @param dataEntrySource
	 *            MUST be specified
	 * @param errorMsg
	 *            The actual message text that should be displayed to user if
	 *            value doesnot comply with validity rules
	 * @param mobileErrors
	 *            The list that would encapsulte/appended with given
	 *            {@code errorMsg} IFF {@code dataEntrySource} is MOBILE
	 * @param webErrors
	 *            The spring {@link Errors} object of corresponding validator of
	 *            respective entity e.g. {@link ChildValidator} that called the
	 *            function and would encapsulate specified {@code errorMsg} if
	 *            value is rejected for {@code dataEntrySource} WEB
	 * @param field
	 *            Only for {@code dataEntrySource} WEB. The complete field or
	 *            property name that would be rejected for spring bound tags ().
	 *            if dataEntrySource is MOBILE and no field is specified then
	 *            errorMessage is appended with field
	 *            {@linkplain DataField#OTHER}
	 */
	private static void putError(DataEntrySource dataEntrySource, String errorMsg, HashMap<String, String> mobileErrors, Errors webErrors, String field, boolean useFieldPrefix) {
		if (dataEntrySource.equals(DataEntrySource.MOBILE)) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(field)) {
				mobileErrors.put(field, errorMsg);
			} else {
				String prevError = mobileErrors.get(DataField.OTHER) == null ? "" : (mobileErrors.get(DataField.OTHER) + "\n");
				mobileErrors.put(DataField.OTHER, prevError + errorMsg);
			}
		} else {
			if (!StringUtils.isEmptyOrWhitespaceOnly(field)) {
				if (!useFieldPrefix) {
					field = field.substring(field.lastIndexOf(".") + 1);
				}

				webErrors.rejectValue(field, "nocode", null, errorMsg);
			} else {
				webErrors.reject("nocode", null, errorMsg);
			}
		}
	}

	/**
	 * @param programId
	 *            : Program ID assigned to child. ID should conform to regex
	 *            {@linkplain GlobalParams#CHILD_PROGRAMID_REGEX}
	 * @param isNew
	 *            : If ID is newly assigned and should be rejected if already
	 *            exists in DB
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @return {@linkplain ValidatorOutput}
	 */
	public static ValidatorOutput validateChildProgramId(String programId, boolean isNew, ServiceContext sc) {
		if (StringUtils.isEmptyOrWhitespaceOnly(programId) || !DataValidation.validate(GlobalParams.CHILD_PROGRAMID_REGEX, programId)) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.CHILD_ID_INVALID);
		}

		if (isNew) {
			String q = "select count(*) from identifier i where identifier = '" + programId + "' ";
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.CHILD_ALREADY_EXISTS);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	public static ValidatorOutput validateNIC(Integer childId, String cnic, boolean unique, boolean isNew, ServiceContext sc) {
		if (!StringUtils.isEmptyOrWhitespaceOnly(cnic) && cnic.length() != 13) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.NIC_INVALID);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(cnic) && unique) {
			String q = "select count(*) from child where nic = '" + cnic + "' " + (isNew ? "" : " and mappedId <> " + childId);
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.NIC_ALREADY_EXISTS);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	/**
	 * @param programId
	 *            : Program ID assigned to child. ID should conform to regex
	 *            {@linkplain GlobalParams#CHILD_PROGRAMID_REGEX}
	 * @param isNew
	 *            : If ID is newly assigned and should be rejected if already
	 *            exists in DB
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @return {@linkplain ValidatorOutput}
	 */
	public static ValidatorOutput validateTeamUserProgramId(String programId, boolean isNew, ServiceContext sc) {
		if (StringUtils.isEmptyOrWhitespaceOnly(programId) || !DataValidation.validate(GlobalParams.TEAM_USER_PROGRAMID_REGEX, programId)) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.TEAM_USER_ID_INVALID);
		}

		if (isNew) {
			String q = "select count(*) from idmapper i join role r on i.roleId = r.roleId where programId = '" + programId + "' and r.roleName NOT IN ('" + GlobalParams.CHILD_ROLE_NAME + "', '"
					+ GlobalParams.STOREKEEPER_ROLE_NAME + "', '" + GlobalParams.VACCINATOR_ROLE_NAME + "')";
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.USERID_ALREADY_EXISTS);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	/**
	 * Validates Enrollment (child.dateEnrolled,
	 * currentVaccination.vaccinationDuedate, currentVaccination.vaccinationDate
	 * MUST be equal and for mobile entries these MUST be equal to current date)
	 * (fields not mentioned in following parameters` list would automatically
	 * get populated during data saving)
	 * 
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param idMapper
	 *            :
	 *            <ul>
	 *            <li>programId- see
	 *            {@linkplain ValidatorUtils#validateChildProgramId}
	 *            </ul>
	 * @param childNamed
	 *            - if child has been named or not
	 * @param child
	 *            :
	 *            <ul>
	 *            <li>dateEnrolled- MUST be Non-null past date. For MOBILE
	 *            entries, it MUST be equal to current date
	 *            <li>Other validations would be same as for child param of : @see
	 *            {@linkplain ValidatorUtils#validateBiographics}
	 *            </ul>
	 *            [{
	 * @param birthdateOrAge
	 * @param ageYears
	 * @param ageMonths
	 * @param ageWeeks
	 * @param ageDays
	 *            see {@linkplain ValidatorUtils#validateBiographics} <br>
	 *            }]
	 * @param address
	 *            :
	 *            <ul>
	 *            <li>@see {@linkplain ValidatorUtils#validateAddress}
	 *            </ul>
	 * @param completeCourseFromCenter
	 * @param centerVisit
	 *            : @see
	 *            {@linkplain ValidatorUtils#validateEnrollmentContactInfo} AND @see
	 *            {@linkplain ValidatorUtils#validateVaccination} AND @see
	 *            {@linkplain ValidatorUtils#validateNextVaccine}
	 * @param vaccineSchedule
	 *            : @see {@link ValidatorUtils#validateVaccinationSchedule}
	 * @param mobileErrors
	 *            : List object that would be appended with error messages for
	 *            MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors
	 *            The spring {@link Errors} object that would encapsulate the
	 *            error messages for WEB (Must be provided if dataEntrySource is
	 *            WEB)
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * 
	 *            All errors occurred during validation would be populated into
	 *            {@code mobileErrors} OR {@code webErrors} w.r.t to
	 *            dataEntrySource provided
	 * */
	public static void validateEnrollmentForm(DataEntrySource dataEntrySource, String projectId, Boolean childNamed, Child child, String birthdateOrAge, String ageYears, String ageMonths,
			String ageWeeks, String ageDays, Address address, String completeCourseFromCenter, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, 
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = true; // We know for enrollment we have
										// encapsulated entities
		
		ValidatorOutput vidop = validateChildProgramId(projectId, true, sc);
		if (!vidop.STATUS().equals(ValidatorStatus.OK)) {
			putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, DataField.CHILD_IDENTIFIER, useFieldPrefix);
		}

		if (child.getDateEnrolled() == null || DateUtils.afterTodaysDate(child.getDateEnrolled())) {
			Date d = child.getDateEnrolled();
//			System.out.println(d);
			putError(dataEntrySource, ErrorMessages.CHILD_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		}
		
		

//		if (StringUtils.isEmptyOrWhitespaceOnly(completeCourseFromCenter)) {
//			putError(dataEntrySource, ErrorMessages.COMPLETE_COURSE_FROM_CENTER_MISSING, mobileErrors, webErrors, DataField.CHILD_COMPLETE_COURSE_FROM_CENTER, useFieldPrefix);
//		}

//		validateChildNIC(dataEntrySource, child.getMappedId(), child.getNic(), true, mobileErrors, webErrors, useFieldPrefix, sc);

		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, child.getDateEnrolled());

		validateBiographics(dataEntrySource, childNamed, child, measles2Given, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, mobileErrors, webErrors, sc, useFieldPrefix);

		validateVaccinationSchedule(dataEntrySource, child, true, centerVisit, vaccineSchedule, mobileErrors, webErrors, sc, useFieldPrefix);

		validateAddress(dataEntrySource, address, mobileErrors, webErrors, useFieldPrefix);

//		validateReminderAndContactInfo(dataEntrySource, centerVisit.getPreference(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), mobileErrors, webErrors, sc, useFieldPrefix);
	}

	public static void validateWomenEnrollmentForm(DataEntrySource dataEntrySource, String projectId, Women women, String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks,
			String ageDays, Address address, WomenVaccinationCenterVisit centerVisit, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = true; // We know for enrollment we have
										// encapsulated entities

		ValidatorOutput vidop = validateWomenProgramId(projectId, true, sc);
		if (!vidop.STATUS().equals(ValidatorStatus.OK)) {
			putError(dataEntrySource, vidop.MESSAGE(), mobileErrors, webErrors, DataField.PROGRAM_ID, useFieldPrefix);
		}

		if (women.getDateEnrolled() == null || DateUtils.afterTodaysDate(women.getDateEnrolled())) {
			putError(dataEntrySource, ErrorMessages.CHILD_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.WOMEN_DATE_ENROLLED, useFieldPrefix);
		}// for mobile entries enrollment/vaccination date should be equal to
			// current date time
		else if (dataEntrySource.equals(DataEntrySource.MOBILE) && !DateUtils.datesEqual(women.getDateEnrolled(), new Date())) {
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_MOBILE_BACKDATED_ENTRY, mobileErrors, null, null, useFieldPrefix);
		}

		// boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule,
		// child.getDateEnrolled());

		validateWomenBiographics(dataEntrySource, women, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, mobileErrors, webErrors, sc, useFieldPrefix);

		validateWomenVaccinationSchedule(dataEntrySource, women, true, centerVisit, false, mobileErrors, webErrors, sc, useFieldPrefix);

		validateAddress(dataEntrySource, address, mobileErrors, webErrors, useFieldPrefix);

		// validateReminderAndContactInfo(dataEntrySource,
		// centerVisit.getPreference(), centerVisit.getContactPrimary(),
		// centerVisit.getContactSecondary(), mobileErrors, webErrors, sc,
		// useFieldPrefix);
	}

	public static ValidatorOutput validateWomenProgramId(String programId, boolean isNew, ServiceContext sc) {
		/*
		 * if(StringUtils.isEmptyOrWhitespaceOnly(programId) ||
		 * !DataValidation.validate(GlobalParams.WOMEN_PROGRAMID_REGEX,
		 * programId)){ return new ValidatorOutput(ValidatorStatus.ERROR,
		 * ErrorMessages.CHILD_ID_INVALID); }
		 */

		if (isNew) {
			String q = "select count(*) from identifier i where identifier = '" + programId + "' ";
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ErrorMessages.WOMEN_ALREADY_EXISTS);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param preferences
	 *            :
	 *            <ul>
	 *            <li>hasApprovedReminder- MUST be provided for all vaccines but
	 *            SHOULD be NULL if Measles2
	 *            </ul>
	 * @param contactPrimary
	 *            : MUST be non-null valid MOBILE number IF
	 *            <b>hasApprovedReminder</b> is true, optional otherwise
	 * @param contactSecondary
	 *            : If not null should valid MOBILE/LANDLINE number
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param webErrors
	 *            : {@link Errors} object of spring (web) {@link ChildValidator}
	 *            that called the function (Must be provided if dataEntrySource
	 *            is Web)<br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link ChildValidator} ,
	 *            ignored otherwise
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 * */
	public static void validateReminderAndContactInfo(DataEntrySource dataEntrySource, LotterySms preferences, String contactPrimary, String contactSecondary, HashMap<String, String> mobileErrors,
			Errors webErrors, ServiceContext sc, boolean useFieldPrefix) {
		// If Next Vaccine scheduled, A preference MUST be specified
		// if(nextVaccines.size() > 0){
		// Preference Approved reminder MUST be specified
//		if (preferences == null || preferences.getHasApprovedReminders() == null) {
//			putError(dataEntrySource, ErrorMessages.APPROVAL_REMINDERS_MISSING, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY, useFieldPrefix);
//		}// If reminders are approved then a contact number MUST be present
//		else if (preferences.getHasApprovedReminders() && (contactPrimary == null || StringUtils.isEmptyOrWhitespaceOnly(contactPrimary))) {
//			putError(dataEntrySource, ErrorMessages.CONTACT1_NUMBER_INVALID, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY, useFieldPrefix);
//		}
		// }// If No Next Vaccine, A preference SHOULD NOT be specified
		// else if(nextVaccines.size() == 0
		// && preferences != null && preferences.getHasApprovedReminders() !=
		// null){
		// putError(dataEntrySource,
		// ErrorMessages.APPROVAL_REMINDERS_NOT_APPLICABLE, mobileErrors,
		// webErrors, DataField.CENTER_VISIT_HAS_APPROVED_REMINDERS,
		// useFieldPrefix);
		// }

		if (!StringUtils.isEmptyOrWhitespaceOnly(contactPrimary)) {
			ValidatorOutput vconp = validateContactNumber(contactPrimary, ContactTeleLineType.MOBILE);
			if (!vconp.STATUS().equals(ValidatorStatus.OK)) {
				putError(dataEntrySource, vconp.MESSAGE(), mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY, useFieldPrefix);
			}
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(contactSecondary)) {
			ValidatorOutput vconsec = validateContactNumber(contactSecondary, ContactTeleLineType.MOBILE);
			if (!vconsec.STATUS().equals(ValidatorStatus.OK)) {
				vconsec = validateContactNumber(contactSecondary, ContactTeleLineType.LANDLINE);
				if (!vconsec.STATUS().equals(ValidatorStatus.OK)) {
					putError(dataEntrySource, "Secondary Contact should be a valid mobile or landline number", mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_SECONDARY, useFieldPrefix);
				}
			}
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(contactPrimary) && !StringUtils.isEmptyOrWhitespaceOnly(contactSecondary) && contactPrimary.equalsIgnoreCase(contactSecondary)) {
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_ALREADY_ASSIGNED, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_SECONDARY, useFieldPrefix);
		}

//		if (preferences == null || preferences.getHasApprovedLottery() == null) {
//			putError(dataEntrySource, ErrorMessages.VACCINATION_LOTTERY_MISSING, mobileErrors, webErrors, null, useFieldPrefix);
//		} else if (preferences.getHasApprovedLottery() && (contactPrimary == null || StringUtils.isEmptyOrWhitespaceOnly(contactPrimary))) {
//			putError(dataEntrySource, ErrorMessages.CONTACT1_NUMBER_INVALID, mobileErrors, webErrors, DataField.CENTER_VISIT_CONTACT_PRIMARY, useFieldPrefix);
//		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified *
	 * @param mobileErrors
	 *            : List object that would be appended with error messages for
	 *            MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors
	 *            The spring {@link Errors} object that would encapsulate the
	 *            error messages for WEB (Must be provided if dataEntrySource is
	 *            WEB)
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * 
	 *            All errors occurred during validation would be populated into
	 *            {@code mobileErrors} OR {@code webErrors} w.r.t to
	 *            dataEntrySource provided
	 */
	public static void validateFollowupForm(DataEntrySource dataEntrySource, List<VaccineSchedule> vaccineSchedule, VaccinationCenterVisit centerVisit, HashMap<String, String> mobileErrors,
			Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = false; // We know that for followup form, only
										// single command object i.e.
										// vaccinationCentertVisit is bound

		validateVaccinationSchedule(dataEntrySource, sc.getChildService().findChildById(centerVisit.getChildId(), true, null), false, centerVisit, vaccineSchedule, mobileErrors, webErrors, sc,
				useFieldPrefix);

		validateReminderAndContactInfo(dataEntrySource, centerVisit.getPreference(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), mobileErrors, webErrors, sc, useFieldPrefix);
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param contact
	 *            :
	 *            <ul>
	 *            <li>numberType : MUST be specified and should not be UNKNOWN.
	 *            Should be SECONDARY if ID has already been assigned a PRIMARY
	 *            number before
	 *            <li>mappedId : MUST be specified to whome number is being
	 *            assigned to
	 *            <li>number- Should be a non-null and valid number according to
	 *            given {@code telelineType} . Also same number should not
	 *            already exists for the given ID
	 *            <li>telelineType- MUST be provided and should be in
	 *            (MOBILE/LANDLINE)
	 *            </ul>
	 * 
	 * @param mobileErrors
	 *            : List object that would be appended with error messages for
	 *            MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors
	 *            The spring {@link Errors} object that would encapsulate the
	 *            error messages for WEB (Must be provided if dataEntrySource is
	 *            WEB)
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * 
	 *            All errors occurred during validation would be populated into
	 *            {@code mobileErrors} OR {@code webErrors} w.r.t to
	 *            dataEntrySource provided
	 */
	public static void validateContactNumberForm(DataEntrySource dataEntrySource, ContactNumber contact, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = false; // We know that for contact number form,
										// only single command object i.e.
										// contact number is bound

		if (contact.getNumberType() == null || contact.getNumberType().equals(ContactType.UNKNOWN)) {
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_NUMBER_TYPE_INVALID, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		} else if (contact.getNumberType().equals(ContactType.PRIMARY)
				&& sc.getCustomQueryService()
						.getDataBySQL(
								"SELECT mappedId FROM contactnumber " + " WHERE mappedId=" + contact.getMappedId() + " " + " AND contactNumberId <>" + contact.getContactNumberId()
										+ " AND numberType='" + ContactType.PRIMARY + "'").size() > 0) {
			putError(dataEntrySource, ErrorMessages.CONTACT_PRIMARY_NUMBER_EXISTS, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		}

		if (contact.getMappedId() == null || contact.getMappedId() <= 0) {
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_MAPPEDID_MISSING, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
		}

		if (contact.getTelelineType() == null || contact.getTelelineType().equals(ContactTeleLineType.UNKNOWN)) {
			putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_TELELINE_TYPE_INVALID, mobileErrors, webErrors, DataField.CONTACT_NUMBER_TELELINE_TYPE, useFieldPrefix);
		} else {
			ValidatorOutput vconsec = validateContactNumber(contact.getNumber(), contact.getTelelineType());
			if (!vconsec.STATUS().equals(ValidatorStatus.OK)) {
				putError(dataEntrySource, vconsec.MESSAGE(), mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER, useFieldPrefix);
			} else if (contact.getMappedId() != null
					&& sc.getCustomQueryService()
							.getDataBySQL(
									"SELECT mappedId FROM contactnumber " + " WHERE number='" + contact.getNumber() + "' " + " AND mappedId=" + contact.getMappedId() + " " + " AND contactNumberId <>"
											+ contact.getContactNumberId()).size() > 0) {
				putError(dataEntrySource, ErrorMessages.CONTACT_NUMBER_ALREADY_ASSIGNED, mobileErrors, webErrors, DataField.CONTACT_NUMBER_NUMBER_TYPE, useFieldPrefix);
			}
		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param programId
	 *            : see programId param of @see
	 *            {@linkplain ValidatorUtils#validateUser}. ProgramId would be
	 *            validated as a newly assigned id i.e. with param isNew TRUE
	 * @param confirmPwd
	 *            : The password provided in Re-Enter password field
	 * @param userUnderEdit
	 *            : field username (loginIdGiven), clearTextPassword
	 *            (passwordGiven), and confirmPwd would be validated according
	 *            to {@linkplain ValidatorUtils#validateLoginCredentials}.
	 *            <b>All others would be validated according to @see
	 *            {@linkplain ValidatorUtils#validateUser}</b>
	 */
	public static void validateUserRegistration(DataEntrySource dataEntrySource, String programId, String confirmPwd, User userUnderEdit, Role userUnderEditRole, User editorUser, Role editorRole,
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = false; // We know that for user registration
										// form, only single command object i.e.
										// user is bound

		validateLoginCredentials(dataEntrySource, userUnderEdit.getUsername(), userUnderEdit.getClearTextPassword(), confirmPwd, mobileErrors, webErrors, sc);

		validateUser(dataEntrySource, true, programId, userUnderEdit, userUnderEditRole, editorUser, editorRole, mobileErrors, webErrors, useFieldPrefix, sc);
	}

	/**
	 * @see {@linkplain ValidatorUtils#validateUser}
	 */
	public static void validateUserEdit(DataEntrySource dataEntrySource, String programId, User userUnderEdit, Role userUnderEditRole, User editorUser, Role editorRole,
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = false; // We know that for user edit form, only
										// single command object i.e. user is
										// bound

		validateUser(dataEntrySource, false, programId, userUnderEdit, userUnderEditRole, editorUser, editorRole, mobileErrors, webErrors, useFieldPrefix, sc);
	}

	/**
	 * @param role
	 *            : The role whose roleName is being searched in given roles`
	 *            list
	 * @param roles
	 *            : list of roles that would be searched for given role
	 * 
	 * @return true if role`s name matches any of the roles in given list
	 */
	private static boolean isRoleInList(Role role, List<Role> roles) {
		for (Role rol : roles) {
			if (rol.getRolename().equalsIgnoreCase(role.getRolename())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param isNew
	 *            : If userUnderEdit is being rolled in i.e. is going to be
	 *            inserted first time in DB
	 * @param programId
	 *            : depending on the role programId would be validated according
	 *            to any of the validations given below <br>
	 *            {@linkplain ValidatorUtils#validateChildProgramId} OR <br>
	 *            {@linkplain ValidatorUtils#validateVaccinatorProgramId} OR <br>
	 *            {@linkplain ValidatorUtils#validateStorekeeperProgramId} OR <br>
	 *            {@linkplain ValidatorUtils#validateTeamUserProgramId}
	 * @param userUnderEdit
	 *            : The user being edited MUST be provided
	 *            <ul>
	 *            <li>username- MUST be a Non-null, alphanumeric, non-whitespace
	 *            sequence of 5-20 characters.
	 *            <li>firstName- MUST be specified with valid name characters
	 *            <li>lastName- (optional) but if specified, should have valid
	 *            name characters
	 *            <li>email- MUST be a valid email address
	 *            <li>status- MUST be specified
	 *            </ul>
	 * @param userUnderEditRole
	 *            : The instance of existing role that is assigned to user MUST
	 *            be specified
	 * @param editorUser
	 *            : MUST be Non-Null and existing user that is manipulating the
	 *            userUnderEdit
	 * @param editorRole
	 *            : The role of {@code editorUser} to assess operation`s
	 *            authentication and privileges. MUST be provided
	 * 
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param error
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateUser(DataEntrySource dataEntrySource, boolean isNew, String programId, User userUnderEdit, Role userUnderEditRole, User editorUser, Role editorRole,
			HashMap<String, String> mobileErrors, Errors webErrors, boolean useFieldPrefix, ServiceContext sc) {
		// it should only be checked for alphanumeric and allowable range of
		// characters. As type, regex and uniqueness would be calculated at the
		// time of creation
		if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getUsername()) || !DataValidation.validate(REG_EX.ALPHA_NUMERIC, userUnderEdit.getUsername(), 5, 20)) {
			putError(dataEntrySource, ErrorMessages.USERNAME_INVALID, mobileErrors, webErrors, "username", useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getFirstName()) || !DataValidation.validate(REG_EX.ALPHA, userUnderEdit.getFirstName())) {
			putError(dataEntrySource, ErrorMessages.FIRSTNAME_INVALID, mobileErrors, webErrors, "firstName", useFieldPrefix);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getLastName()) && !DataValidation.validate(REG_EX.ALPHA, userUnderEdit.getLastName())) {
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, "lastName", useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(userUnderEdit.getEmail()) || !DataValidation.validate(REG_EX.EMAIL, userUnderEdit.getEmail())) {
			putError(dataEntrySource, ErrorMessages.EMAIL_INVALID, mobileErrors, webErrors, "email", useFieldPrefix);
		}

		if (userUnderEdit.getStatus() == null) {
			putError(dataEntrySource, ErrorMessages.USER_STATUS_MISSING, mobileErrors, webErrors, null, useFieldPrefix);
		}

		if (userUnderEditRole != null) {
			if (StringUtils.isEmptyOrWhitespaceOnly(programId)) {
				putError(dataEntrySource, ErrorMessages.ID_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
			}

			if (userUnderEdit.getStatus() != null && User.isUserDefaultAdministrator(userUnderEdit.getUsername(), userUnderEditRole.getRolename())
					&& !userUnderEdit.getStatus().equals(UserStatus.ACTIVE)) {
				putError(dataEntrySource, ErrorMessages.USER_STATUS_ADMIN_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
			}

			Map<String, ArrayList<Role>> rolemap = UserSessionUtils.getRolesDistinction(editorUser, editorRole, sc);
			List<Role> allowedRoles = rolemap.get("ALLOWED_ROLES");
			List<Role> notAllowedRoles = rolemap.get("NOT_ALLOWED_ROLES");

			if (User.isUserDefaultAdministrator(userUnderEdit.getUsername(), userUnderEditRole.getRolename()) && !User.isUserDefaultAdministrator(editorUser.getUsername(), editorRole.getRolename())) {
				putError(dataEntrySource, ErrorMessages.USER_ADMIN_EDITOR_NOT_AUTHORIZED, mobileErrors, webErrors, null, useFieldPrefix);
			}
			// If underEditRole should be in allowed rolesList and should NOT be
			// in restricted/unallowed rolesList
			else if (!isRoleInList(userUnderEditRole, allowedRoles) && isRoleInList(userUnderEditRole, notAllowedRoles)) {
				putError(dataEntrySource, ErrorMessages.USER_ROLE_EDIT_NOT_ALLOWED, mobileErrors, webErrors, null, useFieldPrefix);
			}
		} else {
			putError(dataEntrySource, ErrorMessages.USER_ROLE_MISSING, mobileErrors, webErrors, null, useFieldPrefix);
		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param loginIdGiven
	 *            : The username or id that would be used to login. Must be a
	 *            UNIQUE, alphanumeric, non-whitespace sequence of 5-20
	 *            characters. Must not be admin or administrator
	 * @param passwordGiven
	 *            : Must be alphanumeric, non-whitespace sequence of 5-20
	 *            characters.
	 * @param passwordConfirm
	 *            : Must be exactly same as {@code passwordGiven}
	 * 
	 * @param mobileErrors
	 *            : List object that would be appended with error messages for
	 *            MOBILE (Must be provided if dataEntrySource is MOBILE)
	 * @param webErrors
	 *            The spring {@link Errors} object that would encapsulate the
	 *            error messages for WEB (Must be provided if dataEntrySource is
	 *            WEB)
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * 
	 *            All errors occurred during validation would be populated into
	 *            {@code mobileErrors} OR {@code webErrors} w.r.t to
	 *            dataEntrySource provided
	 */
	public static void validateLoginCredentials(DataEntrySource dataEntrySource, String loginIdGiven, String passwordGiven, String passwordConfirm, HashMap<String, String> mobileErrors,
			Errors webErrors, ServiceContext sc) {
		boolean useFieldPrefix = false;

		if (StringUtils.isEmptyOrWhitespaceOnly(loginIdGiven) || !DataValidation.validate(REG_EX.ALPHA_NUMERIC, loginIdGiven, 5, 20) || loginIdGiven.equalsIgnoreCase("admin")
				|| loginIdGiven.equalsIgnoreCase("administrator")) {
			putError(dataEntrySource, ErrorMessages.USERNAME_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
		} else if (sc.getUserService().findUserByUsername(loginIdGiven) != null) {
			putError(dataEntrySource, ErrorMessages.USERNAME_OCCUPIED, mobileErrors, webErrors, null, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(passwordGiven) || !DataValidation.validate(REG_EX.ALPHA_NUMERIC, passwordGiven, 5, 20)) {
			putError(dataEntrySource, ErrorMessages.PASSWORD_INVALID, mobileErrors, webErrors, null, useFieldPrefix);
		} else if (passwordConfirm != null && !passwordConfirm.equals(passwordGiven)) {
			putError(dataEntrySource, ErrorMessages.PASSWORDS_DONOT_MATCH, mobileErrors, webErrors, null, useFieldPrefix);
		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param programId
	 *            : see {@linkplain ValidatorUtils#validateVaccinatorProgramId}
	 * @param vaccinator
	 *            :
	 *            <ul>
	 *            <li>dateRegistered- MUST be Non-null past date.
	 *            <li>vaccinationCenterId- MUST be specified from centers
	 *            existing in DB
	 *            <li>nic- optional but if specified, should be 13 digit numeric
	 *            sequence
	 *            <li>epAccountNumber- optional but if specified, should be 12
	 *            digit numeric sequence
	 *            <li>firstName- MUST be specified with valid name characters
	 *            <li>lastName- (optional) but if specified, should have valid
	 *            name characters
	 *            <li>birthdate- MUST be specified and should be a past date
	 *            <li>estimatedBirthdate- MUST be specified
	 *            <li>gender- MUST be either MALE/FEMALE
	 *            <li>qualification- MUST be provided, if it contains Other:
	 *            then it must be followed by a valid qualification name <br>
	 *            </ul>
	 * 
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param error
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateVaccinatorRegistrationForm(DataEntrySource dataEntrySource, String programId, Vaccinator vaccinator, String birthdateOrAge, String ageYears,
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix) {
		if (vaccinator.getDateRegistered() == null) {
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_DATE_REGISTERED, useFieldPrefix);
		} else if (vaccinator.getDateRegistered().after(new Date())) {
			putError(dataEntrySource, ErrorMessages.REGISTRATION_DATE_IN_FUTURE, mobileErrors, webErrors, DataField.VACCINATOR_DATE_REGISTERED, useFieldPrefix);
		}

		if (vaccinator.getVaccinationCenterId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_VACCINATION_CENTER_ID, useFieldPrefix);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getNic()) && !DataValidation.validate(REG_EX.NUMERIC, vaccinator.getNic(), 13, 13)) {
			putError(dataEntrySource, ErrorMessages.NIC_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_NIC, useFieldPrefix);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getEpAccountNumber()) && !DataValidation.validate(REG_EX.NUMERIC, vaccinator.getEpAccountNumber(), 12, 12)) {
			putError(dataEntrySource, ErrorMessages.EP_WALLET_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_EP_ACCOUNT_NUMBER, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, vaccinator.getFirstName())) {
			putError(dataEntrySource, ErrorMessages.FIRSTNAME_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_FIRST_NAME, useFieldPrefix);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getLastName()) && !DataValidation.validate(REG_EX.NAME_CHARACTERS, vaccinator.getLastName())) {
			putError(dataEntrySource, ErrorMessages.LASTNAME_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_LAST_NAME, useFieldPrefix);
		}

		if (vaccinator.getEstimatedBirthdate() == null) {
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_BIRTHDATE, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge) || (!birthdateOrAge.equalsIgnoreCase("age") && !birthdateOrAge.equalsIgnoreCase("birthdate"))) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors, null, useFieldPrefix);
		} else if (birthdateOrAge.toLowerCase().contains("age") && StringUtils.isEmptyOrWhitespaceOnly(ageYears)) {
			putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, null, useFieldPrefix);
		}

		if (vaccinator.getGender() == null || vaccinator.getGender().equals(Gender.UNKNOWN)) {
			putError(dataEntrySource, ErrorMessages.GENDER_INVALID, mobileErrors, webErrors, DataField.VACCINATOR_GENDER, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(vaccinator.getQualification()) || (vaccinator.getQualification().equals("Other") || vaccinator.getQualification().trim().endsWith(":"))) {
			putError(dataEntrySource, ErrorMessages.QUALIFICATION_MISSING, mobileErrors, webErrors, DataField.VACCINATOR_QUALIFICATION, useFieldPrefix);
		}
	}

	/**
	 * @param number
	 *            : a non-null and non-whitespace number validated against the
	 *            respective telelineType
	 * @param telelineType
	 *            : type of validation to be used for the number provided,
	 *            should be in (MOBILE, LANDLINE)
	 * @return {@linkplain ValidatorOutput}
	 */
	public static ValidatorOutput validateContactNumber(String number, ContactTeleLineType telelineType) {
		if (StringUtils.isEmptyOrWhitespaceOnly(number)) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_EMPTY);
		}

		if (telelineType.equals(ContactTeleLineType.MOBILE)) {
			if (!DataValidation.validate(REG_EX.CELL_NUMBER, number)) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_NOT_MOBILE);
			} else {
				return new ValidatorOutput(ValidatorStatus.OK, "");
			}
		}

		if (telelineType.equals(ContactTeleLineType.LANDLINE)) {
			if (!DataValidation.validate("(\\+92|92|0)?[0-9]{7,10}", number)) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.CONTACT_NUMBER_NOT_LANDLINE);
			} else {
				return new ValidatorOutput(ValidatorStatus.OK, "");
			}
		}

		return new ValidatorOutput(ValidatorStatus.UNKNOWN, ValidatorOutput.UNKNOWN_STATUS);
	}

	/**
	 * @param epiNumber
	 *            : a non-null and non-whitespace numeric sequence of 8
	 *            characters starting with 201
	 * @return
	 */
	private static ValidatorOutput validateEpiNumberString(String epiNumber) {
		if (StringUtils.isEmptyOrWhitespaceOnly(epiNumber)) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_EMPTY);
		}

		if (!epiNumber.matches("201.{5}")) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_INVALID);
		} else if (Integer.parseInt(epiNumber.substring(0, 4)) > Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()))) {
			return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_YEAR_INVALID);
		}

		return new ValidatorOutput(ValidatorStatus.OK, "OK");
	}

	/**
	 * Validates the EPI number
	 * 
	 * @param epiNumber
	 *            : a non-null and non-whitespace numeric sequence of 8
	 *            characters starting with 201
	 * @param vaccinationCenterId
	 *            : ID of a vaccinationCenter existing in DB on which EPI number
	 *            is assigned. MUST be valid to ensure Uniqueness
	 * @param childId
	 *            : ID of a child existing in DB to whom EPI number is assigned.
	 *            MUST be valid to ensure previous
	 * @param ensurePrevious
	 *            : Flag to ensure if EPI number assigned to specified child was
	 *            same on last visit on specified center
	 * @param ensureUnique
	 *            : Whether EPI number should be validated for uniqueness on
	 *            specified vaccination center
	 * @return {@linkplain ValidatorOutput}
	 */
	public static ValidatorOutput validateEpiNumber(String epiNumber, int vaccinationCenterId, int childId, boolean ensurePrevious, boolean ensureUnique) {
		ValidatorOutput vepi = validateEpiNumberString(epiNumber);
		if (!vepi.STATUS().equals(ValidatorStatus.OK)) {
			return vepi;
		}

		ServiceContext sc = Context.getServices();
		try {
			if (ensureUnique) {
				String q = "select count(*) from vaccination where vaccinationCenterId=" + vaccinationCenterId + " and epiNumber='" + epiNumber + "' and childId <> " + childId;
				if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
					return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_OCCUPIED);
				}
			}

			if (ensurePrevious) {
				String qp = "select epiNumber from vaccination where length(epiNumber) <> 0 and vaccinationCenterId=" + vaccinationCenterId + " and childId = " + childId;
				@SuppressWarnings("rawtypes")
				List qpl = sc.getCustomQueryService().getDataBySQL(qp);
				if (qpl.size() > 0 && !qpl.get(0).toString().equalsIgnoreCase(epiNumber)) {
					return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_NOT_MATCHED + " (Previous:New):('" + qpl.get(0).toString() + "':'" + epiNumber + "')");
				}
			}
		} finally {
			sc.closeSession();
		}

		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	/**
	 * Validates the EPI number
	 * 
	 * @param epiNumber
	 *            : a non-null and non-whitespace numeric sequence of 8
	 *            characters starting with 201
	 * @param vaccinationCenterId
	 *            : ID of a vaccinationCenter existing in DB on which EPI number
	 *            is assigned. MUST be valid to ensure Uniqueness
	 * @param ensureUnique
	 *            : Whether EPI number should be validated for uniqueness on
	 *            specified vaccination center
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @return {@linkplain ValidatorOutput}
	 */
	public static ValidatorOutput validateNewEpiNumber(String epiNumber, int vaccinationCenterId, boolean ensureUnique, ServiceContext sc) {
		ValidatorOutput vepi = validateEpiNumberString(epiNumber);
		if (!vepi.STATUS().equals(ValidatorStatus.OK)) {
			return vepi;
		}

		if (ensureUnique) {
			String q = "select count(*) from vaccination v join vaccinationcenter vc on v.vaccinationCenterId=vc.mappedId where vc.centerType not like '" + CenterType.UNREGISTERED
					+ "' and vaccinationCenterId=" + vaccinationCenterId + " and epiNumber='" + epiNumber + "'";
			if (Integer.parseInt(sc.getCustomQueryService().getDataBySQL(q).get(0).toString()) > 0) {
				return new ValidatorOutput(ValidatorStatus.ERROR, ValidatorOutput.EPI_NUMBER_OCCUPIED);
			}
		}
		return new ValidatorOutput(ValidatorStatus.OK, "");
	}

	public static void validateChildNIC(DataEntrySource dataEntrySource, Integer childId, String cnic, boolean isNew, HashMap<String, String> mobileErrors, Errors webErrors, boolean useFieldPrefix,
			ServiceContext sc) {
		ValidatorOutput vnicop = validateNIC(childId, cnic, true, isNew, sc);
		if (!vnicop.STATUS().equals(ValidatorStatus.OK)) {
			putError(dataEntrySource, vnicop.MESSAGE(), mobileErrors, webErrors, null, false);
		}
	}

	public static void validateChildStatus(DataEntrySource dataEntrySource, Child child, HashMap<String, String> mobileErrors, Errors webErrors, boolean useFieldPrefix) {
		if (child.getStatus() == null) {
			putError(dataEntrySource, ErrorMessages.CHILD_STATUS_MISSING, mobileErrors, webErrors, DataField.CHILD_STATUS, useFieldPrefix);
		} else if (child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name()) && (child.getTerminationDate() == null || child.getTerminationDate().after(new Date()))) {
			putError(dataEntrySource, ErrorMessages.CHILD_TERMINATION_DATE_INVALID, mobileErrors, webErrors, DataField.CHILD_TERMINATION_DATE, useFieldPrefix);
		} else if (child.getStatus().name().equalsIgnoreCase(STATUS.TERMINATED.name()) && StringUtils.isEmptyOrWhitespaceOnly(child.getTerminationReason())) {
			putError(dataEntrySource, ErrorMessages.CHILD_TERMINATION_REASON_MISSING, mobileErrors, webErrors, DataField.CHILD_TERMINATION_REASON, useFieldPrefix);
		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param childNamed
	 *            : MUST Be specified
	 * @param child
	 *            :
	 *            <ul>
	 *            <li>dateEnrolled- MUST be Non-null PAST date.
	 *            <li>firstName- If <code>childNamed</code> is true, it MUST be
	 *            specified with valid name characters
	 *            <li>fatherFirstName- MUST be specified with valid name
	 *            characters
	 *            <li>birthdate- MUST be a Non-null PAST date. Birthdate should
	 *            be BEFORE(LESS THAN) dateEnrolled <br>
	 *            For EnrollmentVaccine (NOT M2 i.e. BCG, P1, P2, P3, M1) - It
	 *            should not be more than 2 years at enrollment date <br>
	 *            For EnrollmentVaccine M2 - It should not be more than 3 years
	 *            at enrollment date.
	 *            <li>estimatedBirthdate- MUST be specified
	 *            <li>gender- MUST be either MALE/FEMALE
	 *            </ul>
	 * @param measles2Given
	 *            : if measles2 was given on enrollment
	 * @param birthdateOrAge
	 *            : MUST be specified and should only be AGE or BIRTHDATE
	 * @param ageYears
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_YEARS} , optional otherwise
	 * @param ageMonths
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_MONTHS} , optional
	 *            otherwise
	 * @param ageWeeks
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_WEEKS} , optional otherwise
	 * @param ageDays
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_DAYS} , optional otherwise
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param webErrors
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateBiographics(DataEntrySource dataEntrySource, Boolean childNamed, Child child, boolean measles2Given, String birthdateOrAge, String ageYears, String ageMonths,
			String ageWeeks, String ageDays, HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix) {
		if (child.getDateEnrolled() == null || DateUtils.afterTodaysDate(child.getDateEnrolled())) {
			putError(dataEntrySource, ErrorMessages.CHILD_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		} else if (child.getBirthdate() == null || DateUtils.afterTodaysDate(child.getBirthdate())) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		} else if (DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.DATE) < 0) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		} else if ((!measles2Given && DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS)
				|| (measles2Given && DateUtils.differenceBetweenIntervals(child.getDateEnrolled(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS_MEASLES2)) {
			putError(dataEntrySource, ErrorMessages.CHILD_AGE_LIMIT_EXCEEDED, mobileErrors, webErrors, null, measles2Given);
		}

		if (child.getEstimatedBirthdate() == null) {
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge) || (!birthdateOrAge.equalsIgnoreCase("age") && !birthdateOrAge.equalsIgnoreCase("birthdate"))) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors, webErrors, DataField.CHILD_BIRTHDATE_OR_AGE, useFieldPrefix);
		} else if (birthdateOrAge.toLowerCase().contains("age")) {
			if (StringUtils.isEmptyOrWhitespaceOnly(ageYears) || !Utils.isNumberBetween(ageYears, 0, MAX_AGE_YEARS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_YEARS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageMonths) || !Utils.isNumberBetween(ageMonths, 0, MAX_AGE_MONTHS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_MONTHS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_MONTHS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageWeeks) || !Utils.isNumberBetween(ageWeeks, 0, MAX_AGE_WEEKS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_WEEKS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_WEEKS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageDays) || !Utils.isNumberBetween(ageDays, 0, MAX_AGE_DAYS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_DAYS_OF_AGE, mobileErrors, webErrors, DataField.CHILD_CHILD_AGE_DAYS, useFieldPrefix);
			}
		}

//		if (childNamed == null) {
//			putError(dataEntrySource, ErrorMessages.NAME_AVAILABLE, mobileErrors, webErrors, DataField.CHILD_NAMED, useFieldPrefix);
//		} else if (childNamed && (StringUtils.isEmptyOrWhitespaceOnly(child.getFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getFirstName(), 3, 40))) {
//			putError(dataEntrySource, "Child`s " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_FIRST_NAME, useFieldPrefix);
//		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(child.getFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getFirstName(), 2, 30)) {
			putError(dataEntrySource, "Child`s first " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_FIRST_NAME, useFieldPrefix);
		}
		
//		if(child.getFirstName().split("\\s").length > 2){
//			putError(dataEntrySource, "Child`s first " + "Name must have only one space", mobileErrors, webErrors, DataField.CHILD_FIRST_NAME, useFieldPrefix);
//		}
		
		if (StringUtils.isEmptyOrWhitespaceOnly(child.getLastName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getLastName(), 2, 30)) {
			putError(dataEntrySource, "Child`s last " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_LAST_NAME, useFieldPrefix);
		}
		
		if(child.getMotherFirstName() != null && child.getMotherFirstName().length() > 0){
			if (!DataValidation.validate(REG_EX.NAME_CHARACTERS, child.getMotherFirstName(), 2, 30)) {
				putError(dataEntrySource, "Mother`s " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.CHILD_MOTHER_FIRST_NAME, useFieldPrefix);
			}
		}

		if (child.getGender() != null && child.getGender().equals(Gender.UNKNOWN)) {
			putError(dataEntrySource, ErrorMessages.GENDER_INVALID, mobileErrors, webErrors, DataField.CHILD_GENDER, useFieldPrefix);
		}
	}

	/**
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param women
	 *            :
	 *            <ul>
	 *            <li>dateEnrolled- MUST be Non-null PAST date.
	 *            <li>firstName- If <code>childNamed</code> is true, it MUST be
	 *            specified with valid name characters
	 *            <li>fatherFirstName- MUST be specified with valid name
	 *            characters
	 *            <li>birthdate- MUST be a Non-null PAST date. Birthdate should
	 *            be BEFORE(LESS THAN) dateEnrolled
	 *            <li>estimatedBirthdate- MUST be specified
	 *            </ul>
	 * @param birthdateOrAge
	 *            : MUST be specified and should only be AGE or BIRTHDATE
	 * @param ageYears
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_YEARS} , optional otherwise
	 * @param ageMonths
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_MONTHS} , optional
	 *            otherwise
	 * @param ageWeeks
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_WEEKS} , optional otherwise
	 * @param ageDays
	 *            : MUST be a valid number between 0 -
	 *            {@linkplain ValidatorUtils#MAX_AGE_DAYS} , optional otherwise
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param webErrors
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateWomenBiographics(DataEntrySource dataEntrySource, Women women, String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays,
			HashMap<String, String> mobileErrors, Errors webErrors, ServiceContext sc, boolean useFieldPrefix) {
		if (women.getDateEnrolled() == null || DateUtils.afterTodaysDate(women.getDateEnrolled())) {
			putError(dataEntrySource, ErrorMessages.WOMEN_DATE_ENROLLED_INVALID, mobileErrors, webErrors, DataField.WOMEN_DATE_ENROLLED, useFieldPrefix);
		} else if (women.getBirthdate() == null || DateUtils.afterTodaysDate(women.getBirthdate())) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.WOMEN_BIRTHDATE, useFieldPrefix);
		} else if (DateUtils.differenceBetweenIntervals(women.getDateEnrolled(), women.getBirthdate(), TIME_INTERVAL.DATE) < 0) {
			putError(dataEntrySource, ErrorMessages.BIRTHDATE_INVALID, mobileErrors, webErrors, DataField.WOMEN_BIRTHDATE, useFieldPrefix);
		}

		/*if (women.getEstimatedBirthdate() == null) {
			putError(dataEntrySource, ErrorMessages.IS_ESTIMATED_BIRTHDATE_MISSING, mobileErrors, webErrors, DataField.WOMEN_BIRTHDATE, useFieldPrefix);
		}*/
		
		if (women.getBirthdate() == null) {
			putError(dataEntrySource, "Can not proceed without women`s birthdate", mobileErrors, webErrors, DataField.WOMEN_BIRTHDATE, useFieldPrefix);
		}

		/*
		 * if(StringUtils.isEmptyOrWhitespaceOnly(birthdateOrAge)){//TODO remove
		 * this. it is redundant. putError(dataEntrySource,
		 * ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors,
		 * webErrors, DataField.WOMEN_BIRTHDATE_OR_AGE, useFieldPrefix); } else
		 * if(!birthdateOrAge.equalsIgnoreCase("age") &&
		 * !birthdateOrAge.equalsIgnoreCase("birthdate")){
		 * putError(dataEntrySource,
		 * ErrorMessages.BIRTHDATE_OR_AGE_PARAM_MISSING, mobileErrors,
		 * webErrors, DataField.WOMEN_BIRTHDATE_OR_AGE, useFieldPrefix); }
		 */
		else if (birthdateOrAge.toLowerCase().contains("age")) {
			if (StringUtils.isEmptyOrWhitespaceOnly(ageYears) || !Utils.isNumberBetween(ageYears, 0, MAX_AGE_YEARS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_YEARS_OF_AGE, mobileErrors, webErrors, DataField.WOMEN_WOMEN_AGE_YEARS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageMonths) || !Utils.isNumberBetween(ageMonths, 0, MAX_AGE_MONTHS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_MONTHS_OF_AGE, mobileErrors, webErrors, DataField.WOMEN_WOMEN_AGE_MONTHS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageWeeks) || !Utils.isNumberBetween(ageWeeks, 0, MAX_AGE_WEEKS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_WEEKS_OF_AGE, mobileErrors, webErrors, DataField.WOMEN_WOMEN_AGE_WEEKS, useFieldPrefix);
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(ageDays) || !Utils.isNumberBetween(ageDays, 0, MAX_AGE_DAYS)) {
				putError(dataEntrySource, ErrorMessages.INVALID_DAYS_OF_AGE, mobileErrors, webErrors, DataField.WOMEN_WOMEN_AGE_DAYS, useFieldPrefix);
			}
		}

		if (women.getFirstName().length() < 3) {
			putError(dataEntrySource, ErrorMessages.WOMEN_NAME_LENGTH, mobileErrors, webErrors, DataField.WOMEN_FIRST_NAME, useFieldPrefix);
		}

		if (women.getFatherFirstName().length() < 3) {
			putError(dataEntrySource, ErrorMessages.WOMEN_NAME_LENGTH, mobileErrors, webErrors, DataField.WOMEN_FATHER_FIRST_NAME, useFieldPrefix);
		}

		if ((women.getMaritalStatus().equals("Married") || women.getMaritalStatus().equals("Widowed")) && women.getHusbandFirstName().length() < 3) {
			putError(dataEntrySource, ErrorMessages.WOMEN_NAME_LENGTH, mobileErrors, webErrors, DataField.WOMEN_HUSBAND_FIRST_NAME, useFieldPrefix);
		}

		if (StringUtils.isEmptyOrWhitespaceOnly(women.getFatherFirstName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, women.getFatherFirstName())) {
			putError(dataEntrySource, "Father`s " + ErrorMessages.NAME_INVALID, mobileErrors, webErrors, DataField.WOMEN_FATHER_FIRST_NAME, useFieldPrefix);
		}

	}

	/**
	 * 
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param address
	 *            :
	 *            <ul>
	 *            <li>address1, landmark- optional but if provided must not have
	 *            special characters other than( |/()_,-.(space)` )
	 *            <li>town- MUST be provided
	 *            <li>uc- MUST be provided
	 *            <li>cityId- MUST be provided
	 *            <li>cityName- If cityId provided is referring to Other option
	 *            then this field must have a value
	 *            </ul>
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile)
	 * @param error
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise
	 */
	public static void validateAddress(DataEntrySource dataEntrySource, Address address, HashMap<String, String> mobileErrors, Errors error, boolean useFieldPrefix) {
		
		if (StringUtils.isEmptyOrWhitespaceOnly(address.getAddress1())) {
			putError(dataEntrySource, "village must be specified", mobileErrors, error, DataField.ADDRESS_ADDRESS1, useFieldPrefix);
		}
		if (StringUtils.isEmptyOrWhitespaceOnly(address.getAddress2())) {
			putError(dataEntrySource, "location must be specified", mobileErrors, error, DataField.ADDRESS_ADDRESS2, useFieldPrefix);
		}
		
		if (address.getAddress1().length() > 30 || address.getAddress1().length() < 3 ) {
			putError(dataEntrySource, "invalid length, should have 3 to 30 characters", mobileErrors, error, DataField.ADDRESS_ADDRESS1, useFieldPrefix);
		}
		
//		if (StringUtils.isEmptyOrWhitespaceOnly(address.getTown())) {
//			putError(dataEntrySource, ErrorMessages.ADDRESS_TOWN_MISSING, mobileErrors, error, DataField.ADDRESS_TOWN, useFieldPrefix);
//		}
//		if (StringUtils.isEmptyOrWhitespaceOnly(address.getUc())) {
//			putError(dataEntrySource, ErrorMessages.ADDRESS_UC_MISSING, mobileErrors, error, DataField.ADDRESS_UC, useFieldPrefix);
//		}
//		if (StringUtils.isEmptyOrWhitespaceOnly(address.getLandmark())) {
//		} else {
//			if (!DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, address.getLandmark())) {
//				putError(dataEntrySource, ErrorMessages.ADDRESS_LANDMARK_INVALID, mobileErrors, error, DataField.ADDRESS_LANDMARK, useFieldPrefix);
//			}
//		}
//		if (address.getCityId() == null) {
//			putError(dataEntrySource, ErrorMessages.ADDRESS_CITY_MISSING, mobileErrors, error, DataField.ADDRESS_CITY_ID, useFieldPrefix);
//		} else if (address.getCityId() == WebGlobals.OTHER_OPTION_ID_IN_DB && StringUtils.isEmptyOrWhitespaceOnly(address.getCityName())) {
//			putError(dataEntrySource, ErrorMessages.ADDRESS_OTHER_CITY_MISSING, mobileErrors, error, DataField.ADDRESS_CITY_NAME, useFieldPrefix);
//		}
	}

	private static void validateVaccinationSchedule(DataEntrySource dataEntrySource, Child child, boolean isNewEnrollment, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule,
			HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean useFieldPrefix) {
		if (!isNewEnrollment && centerVisit.getChildId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_CHILD_ID_MISSING, mobileErrors, error, DataField.CENTER_VISIT_CHILD_ID, useFieldPrefix);
			return;
		}

		if (centerVisit.getVaccinationCenterId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATION_CENTER_ID, useFieldPrefix);
			return;
		}

		if (child.getBirthdate() == null) {
			putError(dataEntrySource, "Can not proceed without child`s birthdate", mobileErrors, error, null, useFieldPrefix);
			return;
		}

		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, child.getDateEnrolled());
		//TODO -<>-
		if (centerVisit.getVisitDate() == null || centerVisit.getVisitDate().after(new Date())) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_VISIT_DATE_INVALID, mobileErrors, error, DataField.CENTER_VISIT_VISIT_DATE, useFieldPrefix);
			return;
		}
		// check if user nullable dates are provided
		// Program assumes that dateEnrolled, vacinationDate, vaccinationDuedate
		// should be equal for enrollment
		// as all fields are for event information of same datetime
		else if (isNewEnrollment && child.getDateEnrolled() != null && (!DateUtils.datesEqual(child.getDateEnrolled(), centerVisit.getVisitDate()))) {
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_EQUAL_DATES_REQUIRED, mobileErrors, error, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
			return;
		} else if (isNewEnrollment && (!measles2Given && DateUtils.differenceBetweenIntervals(centerVisit.getVisitDate(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS_OTHER_VACCINES)
				&& (measles2Given && DateUtils.differenceBetweenIntervals(centerVisit.getVisitDate(), child.getBirthdate(), TIME_INTERVAL.YEAR) > MAX_AGE_YEARS_MEASLES2)) {
			putError(dataEntrySource, ErrorMessages.CHILD_AGE_LIMIT_EXCEEDED, mobileErrors, error, null, useFieldPrefix);
			return;
		}

		ArrayList<VaccineSchedule> defSch = VaccineSchedule.generateDefaultSchedule( child.getBirthdate(), centerVisit.getVisitDate(), centerVisit.getChildId(), centerVisit.getVaccinationCenterId(), true, null, centerVisit.getHealthProgramId());
		
//		System.out.println("\n\ndefSch = VaccineSchedule.generateDefaultSchedule\n\n");
//		for (VaccineSchedule defSchvs : defSch) {
//			defSchvs.printVaccineSchedule();
//		}
		
		Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId = " + centerVisit.getHealthProgramId()).get(0);
		
		boolean anyScheduleVaccineRecceivedToday = false;
		boolean contraindication = false;
		for (VaccineSchedule dfvsh : defSch) {
			VaccineSchedule vsobj = null;
			for (VaccineSchedule vs : vaccineSchedule) {
				if (dfvsh.getVaccine().getName().equalsIgnoreCase(vs.getVaccine().getName()) && 
						(!vs.getStatus().equalsIgnoreCase(VaccineStatusType.INVALID_DOSE.name()) || !vs.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_GIVEN.name()))
						) {
					vsobj = vs;
					break;
				}
			}

			if (dfvsh.getStatus() != null && (dfvsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name()))) {
				continue;// if vaccinated earlier or invalid dose skip any validation
			}

			boolean prereqpassed = IMRUtils.passVaccinePrerequisiteCheck(dfvsh, vaccineSchedule, calendarId);

			// vaccine was retro or should have been vaccinated yet its status
			// must be provided.
			// or if vaccine has to be scheduled then a next date must be
			// provided. it cant be left blank
//			TODO
//			if (dfvsh.getSchedule_duedate() != null && 
//			   (dfvsh.getIs_retro_suspect() || dfvsh.getIs_current_suspect()) && prereqpassed && !dfvsh.getExpired() && (vsobj == null || StringUtils.isEmptyOrWhitespaceOnly(vsobj.getStatus()))) {
//				putError(dataEntrySource, dfvsh.getVaccine().getName() + " info and status must be provided. It cannot be left blank", mobileErrors, error, null, useFieldPrefix);
//			}
			if (vsobj != null) {
				if (vsobj.getVaccination_date() != null
						&& (vsobj.getVaccination_date().before(child.getBirthdate()) || DateUtils.differenceBetweenIntervals(vsobj.getVaccination_date(), centerVisit.getVisitDate(),
								TIME_INTERVAL.DATE) >= 1)) {
					putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination date should not be before birthdate or a future date", mobileErrors, error, null, useFieldPrefix);
				} else if (StringUtils.isEmptyOrWhitespaceOnly(vsobj.getStatus())) {// expired
																					// or
																					// out
																					// of
																					// schedule
																					// vaccines
					if (dfvsh.getSchedule_duedate() != null && !dfvsh.getExpired()) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination status found empty which is not possible. Contact program vendor!", mobileErrors, error, null,
								useFieldPrefix);
					}

					if (vsobj.getVaccination_date() != null || vsobj.getCenter() != null) {
						putError(dataEntrySource, dfvsh.getVaccine().getName()
								+ " vaccination status found empty but vaccination date and/or center was specified which is not possible. Contact program vendor!", mobileErrors, error, null,
								useFieldPrefix);
					}
				} else if (vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())) {
					if (vsobj.getAssigned_duedate() == null) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " due date found empty for SCHEDULED vaccine which is not possible here. Contact program vendor!", mobileErrors,
								error, null, useFieldPrefix);
					} /*else if (vsobj.getAssigned_duedate().before(centerVisit.getVisitDate())) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " due date should be after center visit date", mobileErrors, error, null, useFieldPrefix);
					}*/

					if (vsobj.getVaccination_date() != null || (!dataEntrySource.equals(DataEntrySource.MOBILE) && vsobj.getCenter() != null)) {
						putError(dataEntrySource, dfvsh.getVaccine().getName()
								+ " vaccination status found SCHEDULED but vaccination date and/or center was specified which is not possible. Contact program vendor!", mobileErrors, error, null,
								useFieldPrefix);
					}

					if (!prereqpassed) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " prerequisite vaccine not given for SCHEDULED vaccine which is not possible. Contact program vendor!", mobileErrors,
								error, null, useFieldPrefix);
					}
				} else if (vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name()) || vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_VACCINATED.name())) {
					anyScheduleVaccineRecceivedToday = true;
					if (!prereqpassed) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " prerequisite vaccine not given for VACCINATED vaccine which is not possible. Contact program vendor!", mobileErrors,
								error, null, useFieldPrefix);
					}

					if ((vsobj.getVaccination_date() == null || !DateUtils.datesEqual(vsobj.getVaccination_date(), centerVisit.getVisitDate()))) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination date must be equal to center visit date", mobileErrors, error, null, useFieldPrefix);
					}
					
					if (vsobj.getCenter() == null || (vsobj.getCenter().intValue() != centerVisit.getVaccinationCenterId().intValue())) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination center must be equal to current center", mobileErrors, error, null, useFieldPrefix);
					}
				} else if (vsobj.getStatus().toUpperCase().contains(VaccineStatusType.RETRO.name())) {
					if (!prereqpassed) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " prerequisite vaccine not given for RETRO vaccine which is not possible. Contact program vendor!", mobileErrors,
								error, null, useFieldPrefix);
					}

//					if (vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name()) && vsobj.getVaccination_date() != null) {
//						putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination date found non null for RETRO_DATE_MISSING vaccine which is not possible. Contact program vendor!",
//								mobileErrors, error, null, useFieldPrefix);
//					} else 
					if (vsobj.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO.name())
							&& (vsobj.getVaccination_date() == null || vsobj.getVaccination_date().after(centerVisit.getVisitDate()))) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " non empty past vaccination date should be spcified for RETRO vaccine", mobileErrors, error, null, useFieldPrefix);
					}

					if (vsobj.getCenter() == null) {
						putError(dataEntrySource, dfvsh.getVaccine().getName() + " vaccination center must be specified for RETRO vaccine", mobileErrors, error, null, useFieldPrefix);
					}
				}
			}
		}
		if (!anyScheduleVaccineRecceivedToday) {
			putError(dataEntrySource, "Atleast one vaccine must be given on current visit.", mobileErrors, error, null, useFieldPrefix);
		}

		if (centerVisit.getVaccinatorId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATOR_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATOR_ID, useFieldPrefix);
		}

//		if (centerVisit.getVaccinationCenterId() != null) {
//			ValidatorOutput vepi = null;
//			if (isNewEnrollment) {
//				vepi = ValidatorUtils.validateNewEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), false, sc);
//			} else if (!isNewEnrollment && centerVisit.getChildId() != null) {
//				vepi = ValidatorUtils.validateEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), centerVisit.getChildId(), false, false/*
//																																								 * dataEntrySource
//																																								 * .
//																																								 * equals
//																																								 * (
//																																								 * DataEntrySource
//																																								 * .
//																																								 * MOBILE
//																																								 * )
//																																								 */);
//			}
//			if (!vepi.STATUS().equals(ValidatorStatus.OK)) {
//				putError(dataEntrySource, vepi.MESSAGE(), mobileErrors, error, DataField.CENTER_VISIT_EPI_NUMBER, useFieldPrefix);
//			}
//		}
	}

	private static void validateWomenVaccinationSchedule(DataEntrySource dataEntrySource, Women women, boolean isNewEnrollment, WomenVaccinationCenterVisit centerVisit,
			Boolean ignoreNextVaccineIfNull, HashMap<String, String> mobileErrors, Errors error, ServiceContext sc, boolean useFieldPrefix) {
		if (!isNewEnrollment && centerVisit.getWomenId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_WOMEN_ID_MISSING, mobileErrors, error, DataField.CENTER_VISIT_WOMEN_ID, useFieldPrefix);
		}

		if (centerVisit.getVaccinationCenterId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATION_CENTER_ID, useFieldPrefix);
		}
		
		if (centerVisit.getTt1().getVaccinationStatus() == null && centerVisit.getTt1().getVaccinationStatus() == null
				&& centerVisit.getTt1().getVaccinationStatus() == null
				&& centerVisit.getTt1().getVaccinationStatus() == null
				&& centerVisit.getTt1().getVaccinationStatus() == null) {
			putError(dataEntrySource, "Atleast one vaccine should be vaccinated", mobileErrors, error, DataField.WOMEN_VACCINE_TT1, useFieldPrefix);
		}

		/*if (centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED) && centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)
				&& centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)
				&& centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)
				&& centerVisit.getTt1().getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
			putError(dataEntrySource, "Atleast one vaccine should be vaccinated", mobileErrors, error, DataField.WOMEN_VACCINE_TT1, useFieldPrefix);
		}*/

		// check if user nullable dates are provided
		// Program assumes that dateEnrolled, vacinationDate, vaccinationDuedate
		// should be equal for enrollment
		// as all fields are for event information of same datetime
		if (isNewEnrollment && women.getDateEnrolled() != null && (!DateUtils.datesEqual(women.getDateEnrolled(), centerVisit.getVisitDate()))) {
			putError(dataEntrySource, ErrorMessages.ENROLLMENT_EQUAL_DATES_REQUIRED, mobileErrors, error, DataField.CHILD_DATE_ENROLLED, useFieldPrefix);
		}

		if (centerVisit.getVaccinatorId() == null) {
			putError(dataEntrySource, ErrorMessages.VACCINATOR_MISSING, mobileErrors, error, DataField.CENTER_VISIT_VACCINATOR_ID, useFieldPrefix);
		}

		if (centerVisit.getVaccinationCenterId() != null) {
			ValidatorOutput vepi = null;
			if (isNewEnrollment) {
				vepi = ValidatorUtils.validateNewEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), false, sc);
			} else if (!isNewEnrollment && centerVisit.getWomenId() != null) {
				vepi = ValidatorUtils.validateEpiNumber(centerVisit.getEpiNumber(), centerVisit.getVaccinationCenterId(), centerVisit.getWomenId(), false, false);
			}

			if (!vepi.STATUS().equals(ValidatorStatus.OK)) {
				putError(dataEntrySource, vepi.MESSAGE(), mobileErrors, error, DataField.CENTER_VISIT_EPI_NUMBER, useFieldPrefix);
			}
		}
	}

	/*	
	*/
	/**
	 * Validates vaccination for edits only validates missing params. No
	 * validation of schedule is done
	 * 
	 * @param dataEntrySource
	 *            : MUST be specified
	 * @param child
	 *            : child object for which vaccination is being validated. Note:
	 *            this method doesnot validate child object, but just based on
	 *            some properties of child, do validation of vaccination.
	 * @param vaccination
	 *            :
	 *            <ul>
	 *            <li>childId- MUST be specified
	 *            <li>vaccinationStatus- MUST be specified <br>
	 *            <b>For status PENDING</b> (@see vaccination of
	 *            {@linkplain ValidatorUtils#validatePendingVaccination})<br>
	 *            <li>(vaccinationCenterId, vaccinatorId, vaccinationDate SHOULD
	 *            be NULL)
	 *            <li>vaccinationDuedate must be specified <br>
	 *            <b>For status OTHER THAN PENDING</b> <br>
	 *            <li>vaccinationCenterId- MUST be specified from centers
	 *            existing in DB <br>
	 *            <li>vaccinatorId- MUST be specified from vaccinators existing
	 *            in DB <br>
	 *            <li>vaccinationDate- MUST be a valid, past date
	 *            <li>epiNumber- MUST be Non-null, SHOULD be same as epiNumber
	 *            of previous vaccination on given center if any exists
	 *            </ul>
	 *
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param error
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED after
	 *            validation
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	public static void validateVaccination(DataEntrySource dataEntrySource, Child child, Vaccination vaccination, HashMap<String, String> mobileErrors, Errors error, ServiceContext sc,
			boolean useFieldPrefix) {
		if (vaccination.getChildId() == null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_CHILD_ID_MISSING, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getVaccinationStatus() == null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_STATUS_MISSING, mobileErrors, error, null, useFieldPrefix);
			return;
		}

		// SHOULD ONLY BE IN CASE OF VACCINE EDITS
		if (vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.SCHEDULED)) {
			validatePendingVaccination(dataEntrySource, vaccination, mobileErrors, error, useFieldPrefix);
		} else {
			if (vaccination.getVaccinationCenterId() == null) {
				putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_CENTER_MISSING, mobileErrors, error, null, useFieldPrefix);
			}

			if (vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED) && vaccination.getVaccinatorId() == null) {
				putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATOR_MISSING, mobileErrors, error, null, useFieldPrefix);
			}

			if (!vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.RETRO_DATE_MISSING) && (vaccination.getVaccinationDate() == null || vaccination.getVaccinationDate().after(new Date()))) {
				putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_VISIT_DATE_INVALID, mobileErrors, error, null, useFieldPrefix);
			}

			if (vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.VACCINATED) && vaccination.getVaccinationCenterId() != null) {
				ValidatorOutput vepi = null;
				if (vaccination.getChildId() != null) {
//					vepi = ValidatorUtils.validateEpiNumber(vaccination.getEpiNumber(), vaccination.getVaccinationCenterId(), vaccination.getChildId(), false, dataEntrySource.equals(DataEntrySource.MOBILE));
				}

//				if (!vepi.STATUS().equals(ValidatorStatus.OK)) {
//					putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + vepi.MESSAGE(), mobileErrors, error, null, useFieldPrefix);
//				}
			}
		}
	}

	/**
	 * Validates PENDING vaccination. This function is needed when editing a
	 * PENDING vaccination to assign a different duedate
	 * 
	 * @param dataEntrySource
	 *            : MUST be provided, for Mobile Entries control should never
	 *            reach there.
	 * @param vaccination
	 *            : <li>vaccinationCenterId - MUST be NULL as is not applicable
	 *            <li>vaccinatorId - MUST be NULL as is not applicable <li>
	 *            vaccinationDuedate - MUST be a non-null valid date <li>
	 *            vaccinationDate - MUST be NULL as is not applicable </ul>
	 * 
	 * @param mobileErrors
	 *            : List object that would contain mobile error messages (Must
	 *            be provided if dataEntrySource is Mobile) <br>
	 * @param error
	 *            : {@link Errors} object of spring (web) {@link Validator} that
	 *            called the function <br>
	 * @param useFieldPrefix
	 *            : if would be used for spring (web) {@link Validator} ,
	 *            ignored otherwise <br>
	 * 
	 *            All errors occurred during validation would be populated into
	 *            mobileErrors or webErrors w.r.t to dataEntrySource provided
	 */
	private static void validatePendingVaccination(DataEntrySource dataEntrySource, Vaccination vaccination, HashMap<String, String> mobileErrors, Errors error, boolean useFieldPrefix) {
		if (dataEntrySource.equals(DataEntrySource.MOBILE)) {
			putError(dataEntrySource, ErrorMessages.DATA_ENTRY_SOURCE_INVALID, mobileErrors, error, null, useFieldPrefix);
			return;
		}
		if (!vaccination.getVaccinationStatus().equals(VACCINATION_STATUS.SCHEDULED)) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_STATUS_SCHEDULED_REQUIRED, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getVaccinationCenterId() != null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_CENTER_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getVaccinatorId() != null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATOR_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getVaccinationDuedate() == null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_DUEDATE_INVALID, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getVaccinationDate() != null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_VACCINATION_DATE_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}

		if (vaccination.getHasApprovedLottery() != null) {
			putError(dataEntrySource, vaccination.getVaccine().getName() + ": " + ErrorMessages.VACCINATION_LOTTERY_APPROVAL_SHOULD_NOT_BE_SPECIFIED, mobileErrors, error, null, useFieldPrefix);
		}
	}
	
	public static void validateHealthProgram(DataEntrySource dataEntrySource, HealthProgram hp, String[] centersId, Errors error, boolean isNew){
		
		if(centersId == null){
			putError(dataEntrySource, "select vaccination center(s) for the program", null, error, "", false);
		}
		else{
			ServiceContext sc = Context.getServices();
			
			Integer hpId = null;
			List<HealthProgram> hpL = null;
			if (isNew && hp.getName() != null){	
				
				if(hp.getProgramId() == null){
					hpL = sc.getCustomQueryService().getDataByHQL("from HealthProgram where name like '" + hp.getName() +"'"); 
				}
				else if(hp.getProgramId() != null){
					hpL = sc.getCustomQueryService().getDataByHQL("from HealthProgram where name like '" + hp.getName() +"' and programId != " + hp.getProgramId());
				}
				
				if (hpL != null && hpL.size() > 0){
					putError(dataEntrySource, "health program of this name already exist", null, error, "name", false);
					hpId = hpL.get(0).getProgramId();
				}
				
			}
		}
		
		if(hp.getName() == null || StringUtils.isEmptyOrWhitespaceOnly(hp.getName())){
			error.rejectValue("name", "", "invalid or empty program name");
		}
		
		if(hp.getEnrollmentLimit() != null){
			if (hp.getEnrollmentLimit() > Integer.MAX_VALUE || hp.getEnrollmentLimit() < 0) {
				error.rejectValue("enrollmentLimit", "", "invalid enrollmentLimit");
			}
		}
		if (StringUtils.isEmptyOrWhitespaceOnly(hp.getName()) || !DataValidation.validate(REG_EX.NAME_CHARACTERS, hp.getName())) {
			error.rejectValue("name", "", ErrorMessages.NAME_INVALID);
		}
		
		if(hp.getVaccinationcalendarId() == null || StringUtils.isEmptyOrWhitespaceOnly(hp.getVaccinationcalendarId().toString())){
			error.rejectValue("vaccinationcalendarId", "", "select vaccination calendar for the program");
		}
		
	}
}
