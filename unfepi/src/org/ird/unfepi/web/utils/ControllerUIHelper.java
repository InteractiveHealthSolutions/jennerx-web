package org.ird.unfepi.web.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.ird.unfepi.ChildIncentivization;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.EnrollmentWrapperWomen;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.IdentifierType;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Model.TimeIntervalUnit;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
import org.ird.unfepi.model.exception.VaccinationDataException;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.utils.IncentiveUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.ird.unfepi.web.validator.VaccinationCenterValidator;
import org.ird.unfepi.web.validator.ValidatorUtils;

import com.mysql.jdbc.StringUtils;

public class ControllerUIHelper {

	/**
	 * @param currentVaccination
	 *            : The vaccination object whose previous vaccination is
	 *            required
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED
	 *            afterwards
	 * @return The last vaccination that had been given just before
	 *         currentVaccination.
	 */
	@SuppressWarnings("unchecked")
	public static Vaccination getPreviousVaccination(int childid, ServiceContext sc) {
		Integer pvcnum = null;
		if (pvcnum == null) {
			String sql = "select vaccinationRecordNum from vaccination " + " where childid=" + childid + " " + " and vaccinationdate is not null "
					+ " order by vaccinationdate desc,vaccinationRecordNum asc limit 1";
			List<Integer> lis = sc.getCustomQueryService().getDataBySQL(sql);
			pvcnum = lis.size() == 0 ? null : lis.get(0);
		}

		Vaccination prevVaccination = null;
		if (pvcnum != null) {
			prevVaccination = sc.getVaccinationService().getVaccinationRecord(pvcnum, true, new String[]{"vaccine"}, null);
		}

		return prevVaccination;
	}

	/**
	 * @param currentVaccination
	 *            : The vaccination object whose previous vaccination is
	 *            required
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be CLOSED
	 *            afterwards
	 * @return The last vaccination that had been given just before
	 *         currentVaccination.
	 */
	@SuppressWarnings({"unchecked", "null"})
	public static WomenVaccination getPreviousWomenVaccination(int womenId, ServiceContext sc) {
		Integer pvcnum = null;

		if (pvcnum == null) {
			String sql = "select vaccinationRecordNum from womenvaccination " + " where womenId=" + womenId + " " + " and vaccinationdate is not null"
					+ " order by vaccinationdate desc,vaccinationRecordNum asc limit 1";
			List<Integer> lis = sc.getCustomQueryService().getDataBySQL(sql);
			pvcnum = lis.size() == 0 ? null : lis.get(0);
		}

		WomenVaccination prevVaccination = null;
		if (pvcnum != null) {
			prevVaccination = sc.getWomenVaccinationService().getVaccinationRecord(pvcnum, true, new String[]{"vaccine"}, null);
		}

		return prevVaccination;
	}

	/**
	 * Calculates the vaccine that child is actually eligible for; based on
	 * birthdateVaccineGap schedule.
	 * 
	 * @param vaccineGiven
	 * @param birthdate
	 * @param vaccinationDate
	 * @param sc
	 * @param visitDate
	 * @return
	 */
	public static Vaccine getEnrollmentVaccine(List<VaccineSchedule> vaccineSchedule, Date birthdate, ServiceContext sc, Date visitDate) {
		int closestgap = 999999999;
		List<Vaccine> enrVacc = new ArrayList<Vaccine>();
		for (VaccineSchedule vsh : vaccineSchedule) {
			if (vsh.getVaccination_date() != null && vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.toString()) && IMRUtils.getBirthdateGap(vsh.getVaccine().getVaccineId()) != null) {
				// what should have been the date of vaccination wrt schedule
				// chart
				Calendar cal = Calendar.getInstance();
				cal.setTime(birthdate);
				short gaptovvac = IMRUtils.getBirthdateGap(vsh.getVaccine().getVaccineId()).getValue();
				TimeIntervalUnit unit = IMRUtils.getBirthdateGap(vsh.getVaccine().getVaccineId()).getGapTimeUnit();
				if (unit.equals(TimeIntervalUnit.DAY)) {
					cal.add(Calendar.DATE, gaptovvac);

				} else if (unit.equals(TimeIntervalUnit.WEEK)) {
					cal.add(Calendar.DATE, gaptovvac * 7);

				} else if (unit.equals(TimeIntervalUnit.MONTH)) {
					cal.add(Calendar.MONTH, gaptovvac);

				} else if (unit.equals(TimeIntervalUnit.YEAR)) {
					cal.add(Calendar.YEAR, gaptovvac);
				}

				// How close vaccinationDate is with schedule chart.
				int gap = Math.abs(DateUtils.differenceBetweenIntervals(visitDate, cal.getTime(), TIME_INTERVAL.DATE));

				// if it is closest one mark it as enrollment vaccine
				if (closestgap >= gap - 20) {
					closestgap = gap;
					enrVacc.add(vsh.getVaccine());
				}
			}
		}

		Vaccine minIdVaccine = null;
		for (Vaccine vaccine : enrVacc) {
			if (minIdVaccine == null || minIdVaccine.getVaccineId() > vaccine.getVaccineId()) {
				minIdVaccine = vaccine;
			}
		}
		return minIdVaccine;
	}

	/**
	 * Manipulates (populates required default info) and saves data for entities
	 * participating in enrollment. All values MUST have been validated via
	 * {@link ValidatorUtils#validateEnrollmentForm} before calling the method.
	 * Add ID, Child and vaccinations, creates default Measles1 if needed,
	 * handles next vaccination if required.
	 * 
	 * @see {@linkplain ControllerUIHelper#handleEnrollmentPreference} AND
	 *      {@linkplain ControllerUIHelper#handleEnrollmentContactInfo}
	 * @param idMapper
	 *            MUST have been validated using Enrollment Validator i.e.
	 *            {@linkplain ValidatorUtils#validateEnrollmentForm}
	 * @param child
	 *            MUST have been validated using Enrollment Validator i.e.
	 *            {@linkplain ValidatorUtils#validateEnrollmentForm}
	 * @param enrollmentVaccination
	 *            MUST have been validated using Enrollment Validator i.e.
	 *            {@linkplain ValidatorUtils#validateEnrollmentForm}
	 * @param currentVaccine
	 *            NonNull instance of vaccine existing in Database on which
	 *            child is enrolled
	 * @param nextVaccine
	 *            NonNull instance of vaccine existing in Database that should
	 *            be scheduled next for child. Should be null iff current
	 *            vaccine is Measles2
	 * @param nextVaccinationObject
	 *            The object that would hold the vaccination scheduled next
	 * @param preference
	 *            can be ignored iff current vaccine is Mealses2
	 * @param primContact
	 *            MUST provide if reminders has been approved
	 * @param secContact
	 *            nullable
	 * @param address
	 *            MUST be provided
	 * @param vaccineSchedule
	 * @param user
	 *            Authenticated user who had done dataEntry
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be COMMITED and
	 *            CLOSED after data is saved into session
	 * @throws ChildDataInconsistencyException
	 */
	public static List<ChildIncentivization> doEnrollment(DataEntrySource dataEntrySource, String projectId, Boolean childNamed, Child child, String birthdateOrAge, String ageYears, String ageMonths,
			String ageWeeks, String ageDays, Address address, VaccinationCenterVisit centerVisit, String completeCourseFromCenter, List<VaccineSchedule> vaccineSchedule, Date formStartDate,
			User user, ServiceContext sc) throws ChildDataInconsistencyException {
		handleEnrollmentChild(projectId, childNamed, child, getEnrollmentVaccine(vaccineSchedule, child.getBirthdate(), sc, centerVisit.getVisitDate()), centerVisit.getVaccinationCenterId(), user, sc);

		List<ChildIncentivization> incentivesList = handleEnrollmentVaccinations(dataEntrySource, centerVisit, vaccineSchedule, centerVisit.getPreference().getHasApprovedReminders(), centerVisit
				.getPreference().getHasApprovedLottery(), child, user, sc);

		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, child.getDateEnrolled());
		handlePreference(centerVisit.getPreference(), measles2Given, child, user, sc);

		handleEnrollmentContactInfo(centerVisit.getPreference().getHasApprovedReminders(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), address, child, user, sc);

		EncounterUtil.createEnrollmentEncounter(dataEntrySource, projectId, childNamed, child, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, address, centerVisit, vaccineSchedule,
				completeCourseFromCenter, incentivesList, formStartDate, user, sc);
		return incentivesList;
	}

	public static void doWomenEnrollment(DataEntrySource dataEntrySource, String projectId, Women women, String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays,
			Address address, WomenVaccinationCenterVisit centerVisit, Date formStartDate, User user, String enrollmentVaccine, Date enrollmentDate, List<WomenVaccination> vaccines, ServiceContext sc)
			throws ChildDataInconsistencyException {
		IdMapper womenId = handleEnrollmentWomen(projectId, women, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, enrollmentVaccine, centerVisit.getVaccinationCenterId(), user, sc);

		handleWomenEnrollmentContactInfo(centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), address, women, user, sc);

		handleWomenVaccination(sc, women, centerVisit, user, enrollmentVaccine, vaccines);
		
		EncounterUtil.createWomenEnrollmentEncounter(womenId, dataEntrySource, projectId, women, birthdateOrAge, ageYears, ageMonths, ageWeeks, ageDays, address, centerVisit, formStartDate, user, sc);

	}

	public static void handleWomenVaccination(ServiceContext sc, Women women, WomenVaccinationCenterVisit centerVisit, User user, String enrollmentVaccine, List<WomenVaccination> vaccines) {

		for (int i = 0; i < vaccines.size(); i++) {
			if (!vaccines.get(i).getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				WomenVaccination wv = new WomenVaccination();
				wv.setWomenId(women.getMappedId());
				wv.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
				wv.setCreator(user);
				wv.setVaccinatorId(centerVisit.getVaccinatorId());
				if (!vaccines.get(i).getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED))
					wv.setVaccinationDate(vaccines.get(i).getVaccinationDate());
				
				if (vaccines.get(i).getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					wv.setVaccinationDuedate(vaccines.get(i).getVaccinationDate());
				} else {
					wv.setVaccinationDuedate(null);
				}
				// For now the logic is that the first vaccination from the
				// centre is kept as first vaccination
				if (vaccines.get(i).getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
					wv.setIsFirstVaccination(true);
					// wv.setVaccineId(sc.getVaccinationService().getByName(enrollmentVaccine).getVaccineId());
					wv.setVaccineId(vaccines.get(i).getVaccineId());
				} else {
					wv.setVaccineId(vaccines.get(i).getVaccineId());
					wv.setIsFirstVaccination(false);
				}

				wv.setVaccinationDate(vaccines.get(i).getVaccinationDate());
				wv.setVaccinationStatus(vaccines.get(i).getVaccinationStatus());
				sc.getWomenVaccinationService().save(wv);
			}
		}
	}

	public static List<ChildIncentivization> doFollowup(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule, Date formStartDate, User user,
			ServiceContext sc) throws VaccinationDataException, ChildDataInconsistencyException {
		Child ch = sc.getChildService().findChildById(centerVisit.getChildId(), true, null);

		List<ChildIncentivization> incentiveResults = handleFollowupVaccinations(dataEntrySource, centerVisit, vaccineSchedule, centerVisit.getPreference().getHasApprovedReminders(), centerVisit
				.getPreference().getHasApprovedLottery(), user, sc);

		boolean measles2Given = IMRUtils.isMeasles2Given(vaccineSchedule, centerVisit.getVisitDate());
		handlePreference(centerVisit.getPreference(), measles2Given, ch, user, sc);

		if (centerVisit.getPreference() != null && centerVisit.getPreference().getHasApprovedReminders() != null) {
			handleNonEnrollmentContactInfo(centerVisit.getPreference(), centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), user, sc);
		}

		EncounterUtil.createFollowupEncounter(centerVisit, vaccineSchedule, incentiveResults, dataEntrySource, formStartDate, user, sc);
		return incentiveResults;
	}

	public static void doWomenFollowup(DataEntrySource dataEntrySource, WomenVaccinationCenterVisit centerVisit, List<WomenVaccination> vaccines, Date formStartDate, Women women, Address address,
			User user, ServiceContext sc) throws VaccinationDataException, ChildDataInconsistencyException {
		
		handleWomenDemographics(women, sc);

		handleWomenFollowupContactInfo(centerVisit.getContactPrimary(), centerVisit.getContactSecondary(), address, women, user, sc);

		handleFollowupWomenVaccinations(dataEntrySource, centerVisit, vaccines, user, sc);

		EncounterUtil.createWomenFollowupEncounter(centerVisit, dataEntrySource, formStartDate, user, sc);

	}
	
	private static void handleWomenDemographics(Women women, ServiceContext sc){
		sc.getWomenService().update(women);
	}

	private static void handleWomenFollowupContactInfo(String primaryContact, String secondaryContact, Address address, Women women, User user, ServiceContext sc)
			throws ChildDataInconsistencyException {

		List<ContactNumber> number = sc.getDemographicDetailsService().getContactNumber(women.getMappedId(), false, new String[]{"idMapper"});

		for (ContactNumber contact : number) {
			// if else because for now there are only two types of contact
			// numbers
			if (contact.getNumberType().equals(ContactType.PRIMARY)) {
				contact.setCreator(user);
				contact.setMappedId(women.getMappedId());
				contact.setNumberType(ContactType.PRIMARY);
				contact.setTelelineType(ContactTeleLineType.UNKNOWN);
				contact.setNumber(primaryContact);

				sc.getDemographicDetailsService().updateContactNumber(contact);
			} else {
				contact.setCreator(user);
				contact.setMappedId(women.getMappedId());
				contact.setNumberType(ContactType.SECONDARY);
				contact.setTelelineType(ContactTeleLineType.UNKNOWN);
				contact.setNumber(secondaryContact);

				sc.getDemographicDetailsService().updateContactNumber(contact);
			}
		}

		address.setAddressType(ContactType.PRIMARY);
		address.setCreator(user);
		address.setMappedId(women.getMappedId());
		sc.getDemographicDetailsService().updateAddress(address);
	}

	public static void doChangePreference(LotterySms preference, User user, ServiceContext sc) throws ChildDataInconsistencyException {
		preference.setCreator(user);
		sc.getChildService().saveLotterySms(preference);

		if (preference.getHasApprovedReminders()) {
			List<Vaccination> vaccinationslist = sc.getVaccinationService().findVaccinationRecordByCriteria(preference.getMappedId(), null, null, null, null,
					new Date(preference.getDatePreferenceChanged().getTime()), new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 1000L)), null, null, null, VACCINATION_STATUS.SCHEDULED,
					false, 0, 30, true, null, null);

			for (Vaccination vacc : vaccinationslist) {
				if (sc.getReminderService().findByCriteria(null, null, false, null, vacc.getVaccinationRecordNum(), true/* readonly */, null).size() == 0) {
					List<ReminderSms> remindersmses = IMRUtils.createNextVaccineReminderSms(vacc, null, sc);

					for (ReminderSms reminderSms : remindersmses) {
						if (reminderSms.getDueDate().before(DateUtils.roundoffDatetoDate(new Date(preference.getDatePreferenceChanged().getTime())))) {
							reminderSms.setReminderStatus(REMINDER_STATUS.NA);
						}
						sc.getReminderService().addReminderSmsRecord(reminderSms);
					}
				}
			}
		}
	}
	public static void doStorekeeperRegistration(String projectId, Storekeeper storekeeper, String usernameGiven, String passwordGiven, Boolean ignoreLoginCredentials, User user, ServiceContext sc)
			throws UserServiceException, Exception {
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole(GlobalParams.STOREKEEPER_ROLE_NAME, false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(projectId);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.STOREKEEPER_PROJECT_ID + "'").get(0));
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		sc.getCustomQueryService().save(ident);

		storekeeper.setMappedId(idMapper.getMappedId());
		storekeeper.setCreator(user);
		sc.getStorekeeperService().saveStorekeeper(storekeeper);

		if (!StringUtils.isEmptyOrWhitespaceOnly(usernameGiven) || !StringUtils.isEmptyOrWhitespaceOnly(passwordGiven) || !ignoreLoginCredentials) {
			User storkuser = new User(storekeeper.getMappedId(), usernameGiven, passwordGiven);
			storkuser.setFirstName(storekeeper.getFirstName());
			storkuser.setMiddleName(storekeeper.getMiddleName());
			storkuser.setLastName(storekeeper.getLastName());
			storkuser.setCreator(user);
			storkuser.setStatus(UserStatus.ACTIVE);

			sc.getUserService().createUser(storkuser);
		}
	}

	/**
	 * Manipulates (populates required default info) and saves data for entities
	 * participating in vaccinator registration. All values MUST have been
	 * validated via {@link ValidatorUtils#validateVaccinatorRegistrationForm}
	 * AND {@link ValidatorUtils#validateLoginCredentials} before calling the
	 * method.
	 * 
	 * @param idMapper
	 *            MUST have been validated using
	 *            {@link ValidatorUtils#validateVaccinatorRegistrationForm}
	 * @param vaccinator
	 *            MUST have been validated using
	 *            {@link ValidatorUtils#validateVaccinatorRegistrationForm}
	 * @param usernameGiven
	 *            MUST have been validated using
	 *            {@link ValidatorUtils#validateLoginCredentials}
	 * @param passwordGiven
	 *            MUST have been validated using
	 *            {@link ValidatorUtils#validateLoginCredentials}
	 * @param ignoreLoginCredentials
	 *            If for any reason Vaccinator`s username and password should
	 *            not be created
	 * @param user
	 *            Authenticated user who had done dataEntry
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be COMMITED and
	 *            CLOSED after data is saved into session
	 * @throws UserServiceException
	 * @throws Exception
	 */
	public static void doVaccinatorRegistration(String projectId, Vaccinator vaccinator, String usernameGiven, String passwordGiven, Boolean ignoreLoginCredentials, User user, ServiceContext sc)
			throws UserServiceException, Exception {
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole(GlobalParams.VACCINATOR_ROLE_NAME, false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(projectId);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.VACCINATOR_PROJECT_ID + "'").get(0));
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		sc.getCustomQueryService().save(ident);

		vaccinator.setMappedId(idMapper.getMappedId());
		vaccinator.setCreator(user);
		sc.getVaccinationService().saveVaccinator(vaccinator);

		if (!StringUtils.isEmptyOrWhitespaceOnly(usernameGiven) || !StringUtils.isEmptyOrWhitespaceOnly(passwordGiven) || !ignoreLoginCredentials) {
			User vaccuser = new User(vaccinator.getMappedId(), usernameGiven, passwordGiven);
			vaccuser.setFirstName(vaccinator.getFirstName());
			vaccuser.setMiddleName(vaccinator.getMiddleName());
			vaccuser.setLastName(vaccinator.getLastName());
			vaccuser.setCreator(user);
			vaccuser.setStatus(UserStatus.ACTIVE);

			sc.getUserService().createUser(vaccuser);
		}
	}

	/**
	 * Helper method to extract day with given name from given list of days
	 */
	private static CalendarDay getDaySelected(String dayName, List<CalendarDay> calendarDays) {
		for (CalendarDay calendarDay : calendarDays) {
			if (calendarDay.getDayFullName().equalsIgnoreCase(dayName) || calendarDay.getDayShortName().equalsIgnoreCase(dayName)) {
				return calendarDay;
			}
		}
		return null;
	}

	/**
	 * Manipulates (populates required default info) and saves data for entities
	 * participating in vaccination center registration. All values MUST have
	 * been validated via {@link VaccinationCenterValidator} before calling the
	 * method.
	 * 
	 * @param idMapper
	 * @param center
	 * @param vaccinationDayMapList
	 * @param calendarDays
	 * @param user
	 *            Authenticated user who had done dataEntry
	 * @param sc
	 *            {@linkplain ServiceContext} Object that SHOULD be COMMITED and
	 *            CLOSED after data is saved into session
	 */
	// TODO
	public static void doVaccinationCenterRegistration(String projectId, Integer entityLocation, VaccinationCenter center, List<Map<String, Object>> vaccinationDayMapList,
			List<CalendarDay> calendarDays, User user, ServiceContext sc) {
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole(GlobalParams.VACCINATION_CENTER_ROLE_NAME, false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(projectId);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.CENTER_PROJECT_ID + "'").get(0));
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		ident.setLocationId(entityLocation);
		sc.getCustomQueryService().save(ident);

		center.setMappedId(idMapper.getMappedId());
		center.setCreator(user);
		sc.getVaccinationService().saveVaccinationCenter(center);

//		for (Map<String, Object> vdml : vaccinationDayMapList) {
//			String[] strarr = (String[]) vdml.get("daylist");
//			Vaccine vaccine = (Vaccine) vdml.get("vaccine");
//
//			Set<VaccinationCenterVaccineDay> av = new HashSet<VaccinationCenterVaccineDay>();
//
//			for (String dayname : strarr) {
//				if (!StringUtils.isEmptyOrWhitespaceOnly(dayname)) {
//					VaccinationCenterVaccineDay vcd = new VaccinationCenterVaccineDay();
//					vcd.setId(new VaccinationCenterVaccineDayId(idMapper.getMappedId(), vaccine.getVaccineId(), getDaySelected(dayname, calendarDays).getDayNumber()));
//
//					av.add(vcd);
//				}
//			}
//
//			for (VaccinationCenterVaccineDay vcdli : av) {
//				sc.getVaccinationService().saveVaccinationCenterVaccineDay(vcdli);
//			}
//		}
	}

	public static void doLocationRegistration(Location location, User user, ServiceContext sc) {
		location.setCreatedByUserId(user);
		location.setCreatedDate(new Date());
		sc.getCustomQueryService().save(location);
	}

	/**
	 * Manipulates and save currentVaccination. Based on current vaccine
	 * received and nextVaccination scheduled it manipulates or create next
	 * vaccination and saves it.
	 * 
	 * @return
	 * @see {@linkplain ControllerUIHelper#handleNewNextVaccination} AND
	 *      {@linkplain ControllerUIHelper#handleExistingNextVaccination}
	 */
	private static List<ChildIncentivization> handleFollowupVaccinations(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule,
			Boolean hasApprovedReminders, Boolean hasApprovedLottery, User user, ServiceContext sc) throws VaccinationDataException {
		hasApprovedReminders = hasApprovedReminders == null ? false : hasApprovedReminders;

		List<ChildIncentivization> incentives = new ArrayList<ChildIncentivization>();

		Integer armId = null;

		String sql = "SELECT armId FROM childincentive c JOIN vaccination v on c.vaccinationRecordNum = v.vaccinationRecordNum where v.childId = " + vaccineSchedule.get(0).getChildId();
		List arms = sc.getCustomQueryService().getDataBySQL(sql);

		if (arms.size() > 0) {
			armId = Integer.parseInt(arms.get(0).toString());
		}

		for (VaccineSchedule vsh : vaccineSchedule) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(vsh.getStatus()) && !vsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())
					&& !vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())
					&& (vsh.getAssigned_duedate() != null || vsh.getCenter() != null || vsh.getVaccination_date() != null)) {
//				List<Vaccination> vtnl = sc.getVaccinationService().findByCriteria(centerVisit.getChildId(), vsh.getVaccine().getVaccineId(), null, 0, 10, false, null);
				List<Vaccination> vtnl = sc.getCustomQueryService().getDataByHQL("FROM Vaccination WHERE childId = "+centerVisit.getChildId()+" AND vaccineId = "+vsh.getVaccine().getVaccineId()+" AND vaccinationStatus NOT LIKE 'NOT_VACCINATED'");
				
				if (vtnl != null && vtnl.size() > 1) {
					throw new IllegalStateException(vtnl.size() + " vaccination records found for " + vsh.getVaccine().getName() + " for child " + centerVisit.getChildId());
				}
				Vaccination vtn = null;
				if (vtnl.size() > 0) {
					vtn = vtnl.get(0);
				}// ONLY if vaccination is given. otherwise next vaccine handler
					// code part would decide to create obj
				else if (!vsh.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())) {
					vtn = new Vaccination();
					vtn.setChildId(centerVisit.getChildId());
					vtn.setCreator(user);
					vtn.setVaccineId(vsh.getVaccine().getVaccineId());
					vtn.setIsFirstVaccination(false);
				}

				// if today vaccinated on current center
				if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())) {
//					vtn.setEpiNumber(centerVisit.getEpiNumber().toUpperCase());
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					if (vtn.getVaccinationDuedate() == null) {
						vtn.setVaccinationDuedate(vsh.getAssigned_duedate() == null ? vsh.getSchedule_duedate() : vsh.getAssigned_duedate());
					}
					vtn.setVaccinationStatus(VACCINATION_STATUS.VACCINATED);
					vtn.setVaccinatorId(centerVisit.getVaccinatorId().intValue());
					vtn.setHasApprovedLottery(hasApprovedLottery);

					// vaccination must be saved before running lottery
					if (vtnl.size() > 0 ) {
						sc.getVaccinationService().updateVaccinationRecord(vtn);
					} else {
						vtn.setVaccinationRecordNum(Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString()));
					}

					// Run Lottery with lottery criteria 0 as enrollment doesnt
					// bother about timeliness
//					if (armId != null) {
//						ChildIncentivization ir = ChildIncentivization.runIncentive(dataEntrySource, false, armId, vtn, vsh.getVaccine(), user, sc);
//						incentives.add(ir);
//					}
				}
				
				else if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_VACCINATED.name())) {
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					if (vtn.getVaccinationDuedate() == null) {
						vtn.setVaccinationDuedate(vsh.getAssigned_duedate() == null ? vsh.getSchedule_duedate() : vsh.getAssigned_duedate());
					}
					vtn.setVaccinationStatus(VACCINATION_STATUS.NOT_VACCINATED);
					vtn.setVaccinatorId(centerVisit.getVaccinatorId().intValue());

					if (vtnl.size() > 0 ) {
						sc.getVaccinationService().updateVaccinationRecord(vtn);
					} else {
						vtn.setVaccinationRecordNum(Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString()));
					}
				}
				
				// if retro
				else if (vsh.getStatus().toLowerCase().contains("retro")) {
					boolean datemissing = vsh.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name());
					vtn.setIsFirstVaccination(false);
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					Date duedate = null;
					if (vtn.getVaccinationDuedate() != null) {// retain old
																// duedate
						duedate = vtn.getVaccinationDuedate();
					}
					if (vsh.getVaccine().getVaccineGaps().isEmpty()) {// if out
																		// of
																		// schedule
																		// make
																		// =
																		// vaccine
																		// date
						duedate = vsh.getVaccination_date();
					}
					if (duedate == null) {// if still null or for others make =
											// assigned date
						duedate = vsh.getAssigned_duedate();
					}
					if (duedate == null) {// still null make = schedule date
						duedate = vsh.getSchedule_duedate();
					}
					if (duedate == null) {// still null make = vaccine date for
											// all other vaccines too
						duedate = vsh.getVaccination_date();
					}
					if (duedate == null) {// still null make = visitdate
						duedate = centerVisit.getVisitDate();
					}
					vtn.setVaccinationDuedate(duedate);
					vtn.setVaccinationStatus(datemissing ? VACCINATION_STATUS.RETRO_DATE_MISSING : VACCINATION_STATUS.RETRO);

					if (vtnl.size() > 0) {
						sc.getVaccinationService().updateVaccinationRecord(vtn);
					} else {
						vtn.setVaccinationRecordNum(Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString()));
					}
				}
				// else if scheduled for next
				else if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())) {
					// if vaccine was scheduled and next date has been changed
					if (vtn != null && !DateUtils.datesEqual(vsh.getAssigned_duedate(), vtn.getVaccinationDuedate())) {
						handleExistingNextVaccination(vtn.getVaccinationDuedate(), vtn, hasApprovedReminders, user, sc);
					} else if (vtn == null) {
						vtn = handleNewNextVaccination(centerVisit.getChildId(), centerVisit.getVisitDate(), vsh.getAssigned_duedate(), vsh.getVaccine(), hasApprovedReminders, user, sc);
					}
				}

				vsh.setVaccinationObjCurrentVisit(vtn);
			}
		}

		return incentives;
	}

	private static void handleFollowupWomenVaccinations(DataEntrySource dataEntrySource, WomenVaccinationCenterVisit centerVisit, List<WomenVaccination> vaccines, User user, ServiceContext sc)
			throws VaccinationDataException {

		WomenVaccination tt1, tt2, tt3, tt4, tt5;
		tt1 = centerVisit.getTt1();
		tt2 = centerVisit.getTt2();
		tt3 = centerVisit.getTt3();
		tt4 = centerVisit.getTt4();
		tt5 = centerVisit.getTt5();

		// if it wasn't send in json then leave it
		if (tt1.getVaccinationStatus() != null) {
			// if it's not vaccinated then it's not changed.
			if (!tt1.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				// record number != 0 means record exists, so update it.
				if (tt1.getVaccinationRecordNum() != 0 && tt1.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					sc.getWomenVaccinationService().updateVaccinationRecord(tt1);
				} 
				// if the list queried from db is empty then create/save this record
				else if (vaccines.size() == 0) {
					tt1.setVaccineId(sc.getVaccinationService().getByName("TT1").getVaccineId());
					tt1.setWomenId(centerVisit.getWomenId());
					tt1.setCreator(user);
					tt1.setCreatedDate(new Date());
					tt1.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
					tt1.setVaccinatorId(centerVisit.getVaccinatorId());
					tt1.setIsFirstVaccination(false);
					sc.getWomenVaccinationService().save(tt1);
				}
			}
		}

		if (tt2.getVaccinationStatus() != null) {
			if (!tt2.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				if (tt2.getVaccinationRecordNum() != 0 && tt2.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					sc.getWomenVaccinationService().updateVaccinationRecord(tt2);
				} 
				// if the size is exactly one then it means this is new record.
				else if (vaccines.size() == 1) {
					tt2.setVaccineId(sc.getVaccinationService().getByName("TT2").getVaccineId());
					tt2.setWomenId(centerVisit.getWomenId());
					tt2.setCreator(user);
					tt2.setCreatedDate(new Date());
					tt2.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
					tt2.setVaccinatorId(centerVisit.getVaccinatorId());
					tt2.setIsFirstVaccination(false);
					sc.getWomenVaccinationService().save(tt2);
				}

			}
		}

		if (tt3.getVaccinationStatus() != null) {
			if (!tt3.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				if (tt3.getVaccinationRecordNum() != 0 && tt3.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					sc.getWomenVaccinationService().updateVaccinationRecord(tt3);
				} else if (vaccines.size() == 2) {
					tt3.setVaccineId(sc.getVaccinationService().getByName("TT3").getVaccineId());
					tt3.setWomenId(centerVisit.getWomenId());
					tt3.setCreator(user);
					tt3.setCreatedDate(new Date());
					tt3.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
					tt3.setVaccinatorId(centerVisit.getVaccinatorId());
					tt3.setIsFirstVaccination(false);
					sc.getWomenVaccinationService().save(tt3);
				}

			}
		}

		if (tt4.getVaccinationStatus() != null) {
			if (!tt4.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				if (tt4.getVaccinationRecordNum() != 0 && tt4.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					sc.getWomenVaccinationService().updateVaccinationRecord(tt4);
				} else if (vaccines.size() == 3) {
					tt4.setVaccineId(sc.getVaccinationService().getByName("TT4").getVaccineId());
					tt4.setWomenId(centerVisit.getWomenId());
					tt4.setCreator(user);
					tt4.setCreatedDate(new Date());
					tt4.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
					tt4.setVaccinatorId(centerVisit.getVaccinatorId());
					tt4.setIsFirstVaccination(false);
					sc.getWomenVaccinationService().save(tt4);
				}
			}
		}

		if (tt5.getVaccinationStatus() != null) {
			if (!tt5.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.NOT_VACCINATED)) {
				if (tt5.getVaccinationRecordNum() != 0 && tt5.getVaccinationStatus().equals(WOMEN_VACCINATION_STATUS.SCHEDULED)) {
					sc.getWomenVaccinationService().updateVaccinationRecord(tt5);
				} else if (vaccines.size() == 4) {
					tt5.setVaccineId(sc.getVaccinationService().getByName("TT5").getVaccineId());
					tt5.setWomenId(centerVisit.getWomenId());
					tt5.setCreator(user);
					tt5.setCreatedDate(new Date());
					tt5.setVaccinationCenterId(centerVisit.getVaccinationCenterId());
					tt5.setVaccinatorId(centerVisit.getVaccinatorId());
					tt5.setIsFirstVaccination(false);
					sc.getWomenVaccinationService().save(tt5);
				}
			}
		}
	}
	/**
	 * Manipulate and save IdMapper for role CHILD and save child for ID
	 * assigned by IdMapper
	 */
	private static void handleEnrollmentChild(String projectId, Boolean childNamed, Child child, Vaccine enrollmentVaccine, Integer enrollmentCenter, User user, ServiceContext sc) {
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole("child", false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(projectId);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.CHILD_PROJECT_ID + "'").get(0));
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		sc.getCustomQueryService().save(ident);

//		if (!childNamed) {
//			child.setFirstName("NO NAME");
//		}
		child.setCreator(user);
		child.setStatus(STATUS.FOLLOW_UP);
		child.setMappedId(idMapper.getMappedId());
		if(enrollmentVaccine != null){child.setEnrollmentVaccineId(enrollmentVaccine.getVaccineId());}
		sc.getChildService().saveChild(child);
	}

	/**
	 * Manipulate and save IdMapper for role WOMEN and save women for ID
	 * assigned by IdMapper
	 */
	private static IdMapper handleEnrollmentWomen(String projectId, Women women, String birthdateOrAge, String ageYears, String ageMonths, String ageWeeks, String ageDays, String enrollmentVaccine,
			Integer enrollmentCenter, User user, ServiceContext sc) {
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole("WOMEN", false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(projectId);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.WOMEN_PROJECT_ID + "'").get(0));
		// ident.setLocationId(1);
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		sc.getCustomQueryService().save(ident);

		women.setCreator(user);
		women.setMappedId(idMapper.getMappedId());
		women.setStatus(Women.WOMENSTATUS.ENROLLMENT);
		// calculateDate function should be used in the future for date
		// calculation
		women.setEnrollmentVaccineId(sc.getVaccinationService().getByName(enrollmentVaccine).getVaccineId());
		sc.getWomenService().save(women);
		return idMapper;
	}

	/**
	 * Manipulate and save primaryContact if approved reminders is TRUE OR
	 * primaryContact is not null. Manipulate and save secondaryContact if not
	 * null. Manipulate and save address.
	 */
	private static void handleEnrollmentContactInfo(Boolean approvedReminders, String primaryContact, String secondaryContact, Address address, Child child, User user, ServiceContext sc)
			throws ChildDataInconsistencyException {
		if ((approvedReminders != null && approvedReminders) || !StringUtils.isEmptyOrWhitespaceOnly(primaryContact)) {
			ContactNumber primaryCont = new ContactNumber();
			primaryCont.setCreator(user);
			primaryCont.setMappedId(child.getMappedId());
			primaryCont.setNumberType(ContactType.PRIMARY);
			primaryCont.setTelelineType(ContactTeleLineType.MOBILE);
			primaryCont.setNumber(primaryContact);

			sc.getDemographicDetailsService().saveContactNumber(primaryCont);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(secondaryContact)) {
			ContactNumber secondaryCont = new ContactNumber();
			secondaryCont.setCreator(user);
			secondaryCont.setMappedId(child.getMappedId());
			secondaryCont.setNumberType(ContactType.SECONDARY);
			secondaryCont.setTelelineType(ContactTeleLineType.UNKNOWN);
			secondaryCont.setNumber(secondaryContact);

			sc.getDemographicDetailsService().saveContactNumber(secondaryCont);
		}

		address.setAddressType(ContactType.PRIMARY);
		address.setCreator(user);
		address.setMappedId(child.getMappedId());
		sc.getDemographicDetailsService().saveAddress(address);
	}

	/**
	 * Manipulate and save primaryContact if approved reminders is TRUE OR
	 * primaryContact is not null. Manipulate and save secondaryContact if not
	 * null. Manipulate and save address.
	 */
	private static void handleWomenEnrollmentContactInfo(String primaryContact, String secondaryContact, Address address, Women women, User user, ServiceContext sc)
			throws ChildDataInconsistencyException {

		if (!StringUtils.isEmptyOrWhitespaceOnly(primaryContact)) {
			ContactNumber primaryCont = new ContactNumber();
			primaryCont.setCreator(user);
			primaryCont.setMappedId(women.getMappedId());
			primaryCont.setNumberType(ContactType.PRIMARY);
			primaryCont.setTelelineType(ContactTeleLineType.UNKNOWN);
			primaryCont.setNumber(primaryContact);

			sc.getDemographicDetailsService().saveContactNumber(primaryCont);
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(secondaryContact)) {
			ContactNumber secondaryCont = new ContactNumber();
			secondaryCont.setCreator(user);
			secondaryCont.setMappedId(women.getMappedId());
			secondaryCont.setNumberType(ContactType.SECONDARY);
			secondaryCont.setTelelineType(ContactTeleLineType.UNKNOWN);
			secondaryCont.setNumber(secondaryContact);

			sc.getDemographicDetailsService().saveContactNumber(secondaryCont);
		}

		address.setAddressType(ContactType.PRIMARY);
		address.setCreator(user);
		address.setMappedId(women.getMappedId());
		sc.getDemographicDetailsService().saveAddress(address);
	}

	/**
	 * Manipulate and save Preference for child on given enrollment date, IFF
	 * current vaccine is not Measles2
	 */
	private static void handlePreference(LotterySms preference, boolean measles2Given, Child child, User user, ServiceContext sc) {
		if (!measles2Given) {
			preference.setCreator(user);
			preference.setDatePreferenceChanged(child.getDateEnrolled());
			preference.setMappedId(child.getMappedId());

			sc.getChildService().saveLotterySms(preference);
		}
	}

	/**
	 * Manipulate and Save enrollmentVaccination, Create and save Measles1 and
	 * its reminders(IFF reminderApproval is TRUE) IFF neither current nor next
	 * vaccine is for Measles1/Measles2. Manipulate and Save Next vaccination
	 * i.e. nextVaccinationObject
	 * {@linkplain ControllerUIHelper#handleNewNextVaccination} if current
	 * vaccine is not Measles2
	 * 
	 * @param dataEntrySource
	 */
	private static List<ChildIncentivization> handleEnrollmentVaccinations(DataEntrySource dataEntrySource, VaccinationCenterVisit centerVisit, List<VaccineSchedule> vaccineSchedule,
			Boolean hasApprovedReminders, Boolean hasApprovedLottery, Child child, User user, ServiceContext sc) {
		List<ChildIncentivization> incentivesResult = new ArrayList<ChildIncentivization>();

//		Integer armId = null;
//		if (hasApprovedLottery != null && hasApprovedLottery) {
//			armId = IncentiveUtils.determineIncentiveScheme();
//		}

		for (VaccineSchedule vsh : vaccineSchedule) {
			if (!StringUtils.isEmptyOrWhitespaceOnly(vsh.getStatus()) && !vsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())
					&& !vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())
					&& (vsh.getAssigned_duedate() != null || vsh.getCenter() != null || vsh.getVaccination_date() != null)) {
				Vaccination vtn = new Vaccination();
				vtn.setChildId(child.getMappedId());
				vtn.setCreator(user);
				vtn.setVaccineId(vsh.getVaccine().getVaccineId());

				// if today vaccinated on current center
				if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())) {
//					vtn.setEpiNumber(centerVisit.getEpiNumber().toUpperCase());
					vtn.setIsFirstVaccination(true);
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					vtn.setVaccinationDuedate(vsh.getAssigned_duedate() == null ? vsh.getSchedule_duedate() : vsh.getAssigned_duedate());
					vtn.setVaccinationStatus(VACCINATION_STATUS.VACCINATED);
					vtn.setVaccinatorId(centerVisit.getVaccinatorId().intValue());
//					vtn.setHasApprovedLottery(hasApprovedLottery);

					// vaccination must be saved before running lottery
					Integer vaccNum = Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString());
					vtn.setVaccinationRecordNum(vaccNum);

//					if (vtn.getHasApprovedLottery() != null && vtn.getHasApprovedLottery() && armId != null) {
//						ChildIncentivization lotteryRes = ChildIncentivization.runIncentive(dataEntrySource, true, armId, vtn, vsh.getVaccine(), user, sc);
//						incentivesResult.add(lotteryRes);
//					}
				}
				// if retro
				else if (vsh.getStatus().toUpperCase().contains(VaccineStatusType.RETRO.name())) {
					boolean datemissing = vsh.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name());
					vtn.setIsFirstVaccination(false);
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					Date duedate = null;
					if (vsh.getVaccine().getVaccineGaps().isEmpty()) {// if out
																		// of
																		// schedule
																		// make
																		// =
																		// vaccine
																		// date
						duedate = vsh.getVaccination_date();
					}
					if (duedate == null) {// if still null or for others make =
											// assigned date
						duedate = vsh.getAssigned_duedate();
					}
					if (duedate == null) {// still null make = schedule date
						duedate = vsh.getSchedule_duedate();
					}
					if (duedate == null) {// still null make = vaccine date for
											// all other vaccines too
						duedate = vsh.getVaccination_date();
					}
					if (duedate == null) {// still null make = visitdate
						duedate = centerVisit.getVisitDate();
					}
					vtn.setVaccinationDuedate(duedate);
					vtn.setVaccinationStatus(datemissing ? VACCINATION_STATUS.RETRO_DATE_MISSING : VACCINATION_STATUS.RETRO);

					// save vaccination
					vtn.setVaccinationRecordNum(Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString()));
				}
				// else if scheduled for next
				else if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())) {
					vtn.setIsFirstVaccination(false);
					hasApprovedReminders = false ;
					vtn = handleNewNextVaccination(child.getMappedId(), centerVisit.getVisitDate(), vsh.getAssigned_duedate(), vsh.getVaccine(), false, user, sc);
				}
				else if (vsh.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_VACCINATED.name())) {
					vtn.setVaccinationCenterId(vsh.getCenter().intValue());
					vtn.setVaccinationDate(vsh.getVaccination_date());
					vtn.setVaccinationDuedate(vsh.getAssigned_duedate() == null ? vsh.getSchedule_duedate() : vsh.getAssigned_duedate());
					vtn.setVaccinationStatus(VACCINATION_STATUS.NOT_VACCINATED);
					vtn.setVaccinatorId(centerVisit.getVaccinatorId().intValue());

					// vaccination must be saved before running lottery
					Integer vaccNum = Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(vtn).toString());
					vtn.setVaccinationRecordNum(vaccNum);
				}
				
				
				vsh.setVaccinationObjCurrentVisit(vtn);
			}
		}

		return incentivesResult;
	}
	/**
	 * The nextVaccinationObject that would be scheduled first time is populated
	 * with respective dates, status, child, and vaccine. Next and previous
	 * vaccination trails are maintained. Reminders are created if
	 * hasApprovedReminders is TRUE
	 */
	private static Vaccination handleNewNextVaccination(int childid, Date visitDate, Date nextAssigendDate, Vaccine nextVaccine, boolean hasApprovedReminders, User user, ServiceContext sc) {
		Vaccination nextVaccinationObject = new Vaccination();
		nextVaccinationObject.setChildId(childid);
		nextVaccinationObject.setCreator(user);
		nextVaccinationObject.setVaccinationStatus(VACCINATION_STATUS.SCHEDULED);
		nextVaccinationObject.setVaccineId(nextVaccine.getVaccineId());
		nextVaccinationObject.setVaccinationDuedate(nextAssigendDate);

		Serializable nextvid = sc.getVaccinationService().addVaccinationRecord(nextVaccinationObject);
		nextVaccinationObject.setVaccinationRecordNum(Integer.parseInt(nextvid.toString()));

		if (hasApprovedReminders) {// Only case when hasApprovedReminders would
									// be null is currentVaccine is M2 and flow
									// wont reach here for this case
//			List<ReminderSms> remindersmses = IMRUtils.createNextVaccineReminderSms(nextVaccinationObject, null, sc);
//
//			for (ReminderSms reminderSms : remindersmses) {
//				if (reminderSms.getDueDate().before(DateUtils.roundoffDatetoDate(visitDate))) {
//					reminderSms.setReminderStatus(REMINDER_STATUS.NA);
//					reminderSms.setSmsCancelReason("SAME VACCINATION AND REMINDER DATE;");
//				}
//				sc.getReminderService().addReminderSmsRecord(reminderSms);
//			}
		}
		
		return nextVaccinationObject;
	}
	/**
	 * The nextVaccinationObject that is existing is DB is updated with new due
	 * dates, and previous vaccination. Current vaccination is provided the
	 * trail of next vaccination. PENDING reminders of previous vaccination are
	 * updated with new duedates. If no reminders exists for
	 * nextVaccinationObject then new ones are created if hasApprovedReminders
	 * is TRUE
	 */
	@SuppressWarnings("deprecation")
	private static void handleExistingNextVaccination(Date nextAssignedDate, Vaccination nextVaccinationObject, boolean hasApprovedReminders, User user, ServiceContext sc) {
		nextVaccinationObject.setEditor(user);
		nextVaccinationObject.setVaccinationDuedate(nextAssignedDate);

		sc.getVaccinationService().updateVaccinationRecord(nextVaccinationObject);

		List<ReminderSms> rsms = sc.getReminderService().findByCriteria(null, null, false, null, nextVaccinationObject.getVaccinationRecordNum(), false, null);

		if (rsms.size() > 0) {
			// verify rsms are equal to daynums in arm
			for (ReminderSms r : rsms) {
				if (r.getReminderStatus().equals(REMINDER_STATUS.SCHEDULED)) {
					int hour = r.getDueDate().getHours();
					int min = r.getDueDate().getMinutes();
					int sec = r.getDueDate().getSeconds();

					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date(nextVaccinationObject.getVaccinationDuedate().getTime()));
					cal.set(Calendar.HOUR_OF_DAY, hour);
					cal.set(Calendar.MINUTE, min);
					cal.set(Calendar.SECOND, sec);
					cal.add(Calendar.DATE, r.getDayNumber());

					r.setDueDate(cal.getTime());
					r.setEditor(user);
				}

				r.setDescription((r.getDescription() == null ? "" : r.getDescription()) + WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(new Date()) + "VaccinationDate Changed;");
				sc.getReminderService().updateReminderSmsRecord(r);
			}
		} else if (hasApprovedReminders) {
			List<ReminderSms> remindersmses = IMRUtils.createNextVaccineReminderSms(nextVaccinationObject, null, sc);

			for (ReminderSms reminderSms : remindersmses) {
				reminderSms.setDescription((reminderSms.getDescription() == null ? "" : reminderSms.getDescription()) + WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.format(new Date())
						+ "VaccinationDate Changed;");
				sc.getReminderService().addReminderSmsRecord(reminderSms);
			}
		}
	}

	/**
	 * If reminderCellNumber is not empty/null, saves it as a PRIMARY, MOBILE
	 * contact number. If a Primary contact exists, updates it, otherwise
	 * creates a new one for the ID of preference.
	 */
	public static void handleNonEnrollmentContactInfo(LotterySms preference, String reminderCellNumber, String secondaryContact, User user, ServiceContext sc) throws ChildDataInconsistencyException {
		ContactNumber con = sc.getDemographicDetailsService().getUniquePrimaryContactNumber(preference.getMappedId(), false, null);

		if (!StringUtils.isEmptyOrWhitespaceOnly(reminderCellNumber)) {
			if (con == null) {
				con = new ContactNumber();
				con.setCreator(user);
				con.setMappedId(preference.getMappedId());
				con.setNumber(reminderCellNumber);
				con.setNumberType(ContactType.PRIMARY);
				con.setTelelineType(ContactTeleLineType.MOBILE);

				sc.getDemographicDetailsService().saveContactNumber(con);
			} else if (!con.getNumber().equalsIgnoreCase(reminderCellNumber)) {
				con.setEditor(user);
				con.setNumber(reminderCellNumber);
				con.setEditor(user);
				sc.getDemographicDetailsService().updateContactNumber(con);
			}
		}

		if (!StringUtils.isEmptyOrWhitespaceOnly(secondaryContact)) {
			List<ContactNumber> conl = sc.getDemographicDetailsService().getContactNumber(preference.getMappedId(), false, null);
			ContactNumber con1 = null;

			for (ContactNumber contactNumber : conl) {
				if (contactNumber.getNumberType().equals(ContactType.SECONDARY)) {
					con1 = contactNumber;
				}
			}

			if (con1 == null) {
				con1 = new ContactNumber();
				con1.setCreator(user);
				con1.setMappedId(preference.getMappedId());
				con1.setNumber(secondaryContact);
				con1.setNumberType(ContactType.SECONDARY);
				con1.setTelelineType(ContactTeleLineType.UNKNOWN);

				sc.getDemographicDetailsService().saveContactNumber(con1);
			} else {
				con1.setNumber(secondaryContact);
				con.setEditor(user);
				sc.getDemographicDetailsService().updateContactNumber(con);
			}
		}
	}

	public static void addAddressReferenceData(HttpServletRequest request, Map<String, Object> model, ServiceContext sc) {
		List<Map<String, String>> cities = new ArrayList<Map<String, String>>();
		@SuppressWarnings("rawtypes")
		List cl = sc.getCustomQueryService().getDataBySQL("select locationId cityId, name as cityName from location where locationType=1 order by cityId");
		for (Object object : cl) {
			HashMap<String, String> city = new HashMap<String, String>();
			Object[] oar = (Object[]) object;
			city.put("cityId", oar[0].toString());
			city.put("cityName", oar[1].toString());
			cities.add(city);
		}

		model.put("cities", cities);
	}

	/**
	 * A helper method to set appropriate fields and params for display on Form
	 * requiring Enrollment specific information.
	 */
	public static void prepareWomenEnrollmentReferenceData(HttpServletRequest request, Map<String, Object> model, EnrollmentWrapperWomen command, ServiceContext sc) {
		String programId = request.getParameter("programId");

		if (programId == null && command.getWomen().getIdMapper() != null) {
			programId = command.getWomen().getIdMapper().getIdentifiers().get(0).getIdentifier();
		}

		addAddressReferenceData(request, model, sc);
	}

	/**
	 * A helper method to retrieve appropriate fields and params for display on
	 * Form requiring Followup specific information. Usually used in spring`s
	 * formBackingObject method
	 */
	public static void prepareFollowupDisplayObjects(HttpServletRequest request, Child child, ServiceContext sc) {
		request.getSession().setAttribute("childfollowup", sc.getChildService().findChildById(child.getMappedId(), true, new String[]{"idMapper"}));
	}

	/**
	 * A helper method to retrieve appropriate fields and params for display on
	 * Form requiring Followup specific information. Usually used in spring`s
	 * formBackingObject method
	 */
	public static void prepareWomenFollowupDisplayObjects(HttpServletRequest request, Women women, ServiceContext sc) {
		request.getSession().setAttribute("womenfollowup", sc.getWomenService().findWomenById(women.getMappedId(), true, new String[]{"idMapper"}));
	}

	/**
	 * A helper method to set appropriate fields and params for display on Form
	 * requiring Followup specific information.
	 */
	public static void prepareFollowupReferenceData(HttpServletRequest request, Map<String, Object> model, VaccinationCenterVisit command, ServiceContext sc) {
		if (command.getPreference().getHasApprovedReminders() == null) {
			LotterySms pref = sc.getChildService().findLotterySmsByChild(command.getChildId(), true, 0, 1, null).get(0);
			command.getPreference().setHasApprovedReminders(pref.getHasApprovedReminders());
		}

		List<ContactNumber> conl = sc.getDemographicDetailsService().getContactNumber(command.getChildId(), true, null);
		for (ContactNumber cn : conl) {
			if (command.getContactPrimary() == null && cn.getNumberType().name().equalsIgnoreCase(ContactType.PRIMARY.name())) {
				command.setContactPrimary(cn.getNumber());
			} else if (command.getContactSecondary() == null && cn.getNumberType().name().equalsIgnoreCase(ContactType.SECONDARY.name())) {
				command.setContactSecondary(cn.getNumber());
			}
		}
	}

	/**
	 * A helper method to set appropriate fields and params for display on Form
	 * requiring Followup specific information.
	 */
	public static void prepareWomenFollowupReferenceData(HttpServletRequest request, Map<String, Object> model, WomenVaccinationCenterVisit command, ServiceContext sc) {

		if (command.getContactPrimary() == null) {
			ContactNumber cont = sc.getDemographicDetailsService().getUniquePrimaryContactNumber(command.getWomenId(), true, null);
			if (cont != null)
				command.setContactPrimary(cont.getNumber());
		}
	}

	/**
	 * A helper method to set appropriate fields and params for display on Form
	 * requiring Vaccination information.
	 */
	public static void prepareVaccinationReferenceData(HttpServletRequest request, Map<String, Object> model, List<VaccinationCenter> centeres, List<Vaccinator> vaccinators) {
		model.put("vaccinationCenters", centeres);
		model.put("vaccinators", vaccinators);
	}
}
