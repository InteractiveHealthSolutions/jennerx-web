package org.ird.unfepi.rest.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.validator.ValidatorUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class EnrollmentServiceHelper
{
	public static String createEnrollment(JSONObject objectToParse)
	{
		// demographics
		String projectId;
		boolean isChildNamed;
		String childFirstName;
		String childLastName;
		String gender;
		String fatherFirstName;
		String fatherNIC;
		String faterLastName;
		String dob;
		String epiNo;
		boolean isEstimated;
		String days;
		String weeks;
		String months;
		String years;
		String modeDateOfBirth = null;

		// Address
		String houseNo;
		String street;
		String sector;
		String colony;
		String uc;
		String landmark;
		String city;
		String town;

		// Program details
		String currentVaccine;
		String currentVaccineDueDate;
		boolean sameCenter;
		String strSameCenter = null;
		boolean smsReminder;
		boolean hasApprovedLottery;
		String mobileNoString;
		String landlineNoString;
		// JSONArray vaccinesGiven;
		JSONArray vaccinations;
		JSONArray supplementary;

		// List<String>currentVaccines=new ArrayList<String>();
		List<VaccineSchedule> schedule = new ArrayList<VaccineSchedule>();

		// User infomation
		Integer userId;
		Integer centreId;
		String userName;


		// //////////////////////////////////////////////////////////////////////////////
		// //////////////////////////FIELDS FROM MOBILE//////////////////////////////////
		// //////////////////////////////////////////////////////////////////////////////
		// metadata
		// mobileDateFormat = (String)objectToParse.get(RequestElements.REQUEST_DATE_FORMAT);

		// user centric fields
		userId = Integer.valueOf(objectToParse.get(RequestElements.LG_USERID).toString());
		centreId = Integer.valueOf(objectToParse.get(RequestElements.ENROLLEMNT_CENTRE).toString());

		// demographics
		projectId = (String) objectToParse.get(RequestElements.CHILD_PROG_ID);
		isChildNamed = RestUtils.setBoolean((String) objectToParse.get(RequestElements.CHILD_NAMED));
		childFirstName = (String) objectToParse.get(RequestElements.CHILD_FIRST_NAME);
		childLastName = (String) objectToParse.get(RequestElements.CHILD_LAST_NAME);
		gender = (String) objectToParse.get(RequestElements.CHILD_GENDER);
		fatherFirstName = (String) objectToParse.get(RequestElements.FATHER_FIRST_NAME);
		fatherNIC = (String) objectToParse.get(RequestElements.FATHER_NIC);
		faterLastName = (String) objectToParse.get(RequestElements.FATHER_LAST_NAME);
		dob = (String) objectToParse.get(RequestElements.DOB);
		days = (String) objectToParse.get(RequestElements.DOB_DAY);
		weeks = (String) objectToParse.get(RequestElements.DOB_WEEK);
		months = (String) objectToParse.get(RequestElements.DOB_MONTH);
		years = (String) objectToParse.get(RequestElements.DOB_YEAR);

		Date dateOfBirth = null;
		dateOfBirth = RestUtils.stringToDate(dob);

		epiNo = (String) objectToParse.get(RequestElements.EPI_NO);
		isEstimated = RestUtils.setBoolean((String) objectToParse.get(RequestElements.IS_BIRHTDATE_ESTIMATED));

		// address
		houseNo = (String) objectToParse.get(RequestElements.ADD_HOUSENO);
		street = (String) objectToParse.get(RequestElements.ADD_STREET);
		sector = (String) objectToParse.get(RequestElements.ADD_SECTOR);
		colony = (String) objectToParse.get(RequestElements.ADD_COLONY);
		uc = (String) objectToParse.get(RequestElements.ADD_UC);
		landmark = (String) objectToParse.get(RequestElements.ADD_LANDMARK);
		town = (String) objectToParse.get(RequestElements.ADD_TOWN);
		city = (String) objectToParse.get(RequestElements.ADD_CITY);

		// program details
		vaccinations = (JSONArray) objectToParse.get(RequestElements.VACCINATION_SCHEDULE);
		supplementary = (JSONArray) objectToParse.get(RequestElements.VACCINATION_SUPPLEMENTARY);
		sameCenter = RestUtils.setBoolean((String) objectToParse.get(RequestElements.SAME_CENTER));
		smsReminder = Boolean.parseBoolean(objectToParse.get(RequestElements.SMS_REMINDER_APP).toString());
		hasApprovedLottery = Boolean.parseBoolean(objectToParse.get(RequestElements.LOTTERY_APPROVAL).toString());
		mobileNoString = (String) objectToParse.get(RequestElements.PRIMARY_NUMBER);
		landlineNoString = (String) objectToParse.get(RequestElements.SECONDARY_NUMBER);

		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// ///////////////////////////PREPARE OBJECTS FOR API CALLS//////////////////////////////////////////////
		// ///////////////////////////////////////////////////////////////////////////////////////////////
		ServiceContext sc = Context.getServices();
		User submitter = new User();
		UserService userService = sc.getUserService();

		submitter.setMappedId(userId);

		Child ch = new Child();
		ch.setBirthdate(dateOfBirth);
		if ("M".equals(gender))
		{
			ch.setGender(Gender.MALE);
		}
		else if ("F".equals(gender))
		{
			ch.setGender(Gender.FEMALE);
		}
		else
		{
			ch.setGender(Gender.UNKNOWN);
		}

		ch.setFirstName(childFirstName);
		ch.setLastName(childLastName);
		ch.setFatherFirstName(fatherFirstName);
		ch.setNic(fatherNIC);
		ch.setFatherLastName(faterLastName);
		ch.setBirthdate(dateOfBirth);
		ch.setEstimatedBirthdate(isEstimated);
		ch.setDateEnrolled(new Date());

		// String representing the mode of recording the date of birth of the child
		modeDateOfBirth = (isEstimated) ? GlobalParams.MODE_BIRTHDATE_AGE : GlobalParams.MODE_BIRTHDATE_DOB;

		// ///////////Address fields ///////////////////////
		Address add = new Address();
		add.setAddHouseNumber(houseNo);
		add.setAddSector(sector);
		add.setAddColony(colony);
		add.setAddLandmark(landmark);
		add.setAddStreet(street);
		add.setCityId(Integer.valueOf(city));
		// set town id
		add.setAddtown(town);
		// set uc id
		add.setAddUc(uc);


		ContactNumber contactMob = new ContactNumber();
		contactMob.setNumber(mobileNoString);
		contactMob.setTelelineType(ContactTeleLineType.MOBILE);

		ContactNumber contactLnd = new ContactNumber();
		contactLnd.setNumber(landlineNoString);
		contactLnd.setTelelineType(ContactTeleLineType.LANDLINE);

		// //////////Program Details////////////////////

		// 2.Next vaccines:
		// Main VaccinationCenterVisit object used for doing the enrollment
		// VaccinationCenterVisit vcv = new VaccinationCenterVisit();

		// Temporary JSONObject to get the vaccine composite currently being looked up in the loop
		JSONObject obj;

		// Fields to retrieve from the objects in the array
		Date vaccinationDate = null;
		Date dueDate = null;
		String vaccineName = null;
		Vaccine vaccine = null;

		// Iterate to loop through all the next vaccines scheduled.
		Iterator i = vaccinations.iterator();
		VaccineSchedule row;
		// go through all the next vaccines to be given
		while (i.hasNext())
		{
			row = new VaccineSchedule();
			JSONObject o = (JSONObject) i.next();

			// set vaccine for vaccineschedule row
			vaccineName = (String) o.get(RequestElements.VACCINENAME);
			vaccine = sc.getVaccinationService().findVaccine(vaccineName, true).get(0);
			row.setVaccine(vaccine);

			// set status for for vaccineschedule row
			String status = (String) o.get(RequestElements.VACCINATION_STATUS);
			row.setStatus(status);

			// set centre

			Integer vaccineCentreId = Integer.valueOf(o.get(RequestElements.VACCINATION_CENTER).toString());
			if (vaccineCentreId == 0)// sending 0 from mobile if status is VACCINATED
				row.setCenter(centreId);
			else
				row.setCenter(vaccineCentreId);

			// set dates for vaccineschedule row
			String vaccinationDateString = (String) o.get(RequestElements.DATE_OF_VACCINATION);
			String dueDateString = (String) o.get(RequestElements.NEXT_ALLOTTED_DATE);
			try
			{
				vaccinationDate = RestUtils.stringToDate(vaccinationDateString);
				dueDate = RestUtils.stringToDate(dueDateString);
				row.setAssigned_duedate(dueDate);
				row.setVaccination_date(vaccinationDate);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			schedule.add(row);
		}

		// 3.Lottery and SMs Preferences
		LotterySms lotterysms = new LotterySms();
		lotterysms.setHasApprovedReminders(smsReminder);
		lotterysms.setHasApprovedLottery(hasApprovedLottery);

		// 4.Same center
		if (sameCenter)
		{
			strSameCenter = GlobalParams.APPLICATION_YES;
		}
		else
		{
			strSameCenter = GlobalParams.APPLICATION_NO;
		}

		VaccinationCenterVisit vacCentrVist = new VaccinationCenterVisit(null, new Date(), Integer.valueOf(submitter.getMappedId()), Integer.valueOf(centreId), epiNo, null, mobileNoString,
				landlineNoString, lotterysms);
		

		HashMap<String, String> mobileErrors = new HashMap<String, String>();

		try
		{
			ValidatorUtils.validateEnrollmentForm(DataEntrySource.MOBILE, projectId, isChildNamed, ch, modeDateOfBirth, years, months, weeks, days, add, strSameCenter, vacCentrVist,
					schedule, mobileErrors, null, sc);

			StringBuilder builder = new StringBuilder();

			for (String key : mobileErrors.keySet())
			{
				builder.append(mobileErrors.get(key) + "\r\n");
			}

			// proceed to do enrollment if no errors found
			if (mobileErrors.size() == 0)
			{
				ControllerUIHelper.doEnrollment(DataEntrySource.MOBILE, projectId, isChildNamed, ch, dob, null, null, null, null, add, vacCentrVist, null, schedule, new Date(), submitter,
						sc);
				sc.commitTransaction();
				if (supplementary.size() > 0)
				{

					SupplementaryVaccinesHepler.save(ch.getMappedId(), epiNo, supplementary, vacCentrVist.getVaccinatorId(), userId);
				}
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
			}
			else
			{
				// Log the errors for now
				GlobalParams.MOBILELOGGER.debug("Error(s) found in validation follows \n");
				GlobalParams.MOBILELOGGER.error(builder.toString());
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, (Map) mobileErrors);

				// Map<String,Object> hashResponse = new HashMap<String, Object>();
				// for(String str :mobileErrors.keySet())
				// {
				// hashResponse.put(str, mobileErrors.get(str));
				// }
				// return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, hashResponse);
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			sc.rollbackTransaction();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, null);
		}
		finally
		{
			sc.closeSession();
		}
	}
}
