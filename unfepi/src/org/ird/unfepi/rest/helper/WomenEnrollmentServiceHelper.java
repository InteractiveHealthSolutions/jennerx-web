/**
 * 
 */
package org.ird.unfepi.rest.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.Women.WOMENSTATUS;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.json.simple.JSONObject;

/**
 * @author Safwan
 *
 */
public class WomenEnrollmentServiceHelper {

	@SuppressWarnings({"unused", "rawtypes", "unchecked"})
	public static String createEnrollment(JSONObject objectToParse) throws ParseException {
		// demographics
		String projectId;
		String womenFirstName;
		// String womenLastName;
		String fatherFirstName;
		// String faterLastName;
		String husbandFirstName;
		String dob;
		String maritalStatus;
		String epiNo;
		boolean isEstimated;
		String days;
		String weeks;
		String months;
		String years;
		String modeDateOfBirth = null;
		Date dateFormStart = new Date();

		// Address
		String address;
		String uc;
		String landmark;
		String city;
		String town;

		// Program details
		String currentVaccine;
		String currentVaccineDueDate;
		boolean sameCenter;
		String strSameCenter = null;
		String mobileNoString;
		String landlineNoString;
		// JSONArray vaccinesGiven;
		// JSONArray vaccinations;
		JSONObject demographics;
		JSONObject vaccination;
		JSONObject tt1 = null;
		JSONObject tt2 = null;
		JSONObject tt3 = null;
		JSONObject tt4 = null;
		JSONObject tt5 = null;
		WOMEN_VACCINATION_STATUS tt1Status, tt2Status, tt3Status, tt4Status, tt5Status;
		String enrollmentVaccine = null;
		Date enrollmentDate = null;
		ServiceContext sc = Context.getServices();

		WomenVaccinationCenterVisit centerVisit = new WomenVaccinationCenterVisit();
		List<WomenVaccination> vaccines = new ArrayList<WomenVaccination>();

		// List<String>currentVaccines=new ArrayList<String>();
		// List<VaccineSchedule> schedule = new ArrayList<VaccineSchedule>();

		// User infomation
		Integer userId;
		Integer centreId;
		String userName;

		// //////////////////////////////////////////////////////////////////////////////
		// //////////////////////////FIELDS FROM
		// MOBILE//////////////////////////////////
		// //////////////////////////////////////////////////////////////////////////////
		// metadata
		// mobileDateFormat =
		// (String)objectToParse.get(RequestElements.REQUEST_DATE_FORMAT);

		demographics = (JSONObject) objectToParse.get(RequestElements.WOMEN_DEMOGRAPHICS);
		vaccination = (JSONObject) objectToParse.get(RequestElements.WOMEN_VACCINATION);

		// user centric fields
		userId = Integer.valueOf(demographics.get(RequestElements.LG_USERID).toString());
		centreId = Integer.valueOf(demographics.get(RequestElements.ENROLLEMNT_CENTRE).toString());

		// demographics
		projectId = (String) demographics.get(RequestElements.WOMEN_PROJECT_ID);
		womenFirstName = (String) demographics.get(RequestElements.WOMEN_FIRST_NAME);
		fatherFirstName = (String) demographics.get(RequestElements.FATHER_FIRST_NAME);
		husbandFirstName = (String) demographics.get(RequestElements.HUSBAND_FIRST_NAME);
		dob = (String) demographics.get(RequestElements.DOB);
		days = (String) demographics.get(RequestElements.DOB_DAY);
		weeks = (String) demographics.get(RequestElements.DOB_WEEK);
		months = (String) demographics.get(RequestElements.DOB_MONTH);
		years = (String) demographics.get(RequestElements.DOB_YEAR);
		maritalStatus = (String) demographics.get(RequestElements.WOMEN_MARITAL_STATUS);

		Date dateOfBirth = null;
		dateOfBirth = RestUtils.stringToDate(dob);

		// epiNo = (String) objectToParse.get(RequestElements.EPI_NO);
		// isEstimated = RestUtils.setBoolean((String)
		// objectToParse.get(RequestElements.IS_BIRHTDATE_ESTIMATED));

		// address
		address = (String) demographics.get(RequestElements.WOMEN_ADDRESS);
		uc = (String) demographics.get(RequestElements.ADD_UC);
		landmark = (String) demographics.get(RequestElements.ADD_LANDMARK);
		town = (String) demographics.get(RequestElements.ADD_TOWN);
		city = (String) demographics.get(RequestElements.ADD_CITY).toString();

		// program details
		if (vaccination.get(RequestElements.TT1) != null) {
			tt1 = (JSONObject) vaccination.get(RequestElements.TT1);
			tt1Status = WOMEN_VACCINATION_STATUS.findEnum(tt1.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
			centerVisit.getTt1().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt1.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
			if (tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString().equals(""))
				centerVisit.getTt1().setVaccinationDate(null);
			else
				centerVisit.getTt1().setVaccinationDate(stringToDate(tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));

			centerVisit.getTt1().setVaccineId(sc.getVaccinationService().getByName("TT1").getVaccineId());
			vaccines.add(centerVisit.getTt1());
			if (tt1Status.equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
				enrollmentVaccine = "tt1";
				enrollmentDate = centerVisit.getTt1().getVaccinationDate();
			}
		}
		if (vaccination.get(RequestElements.TT2) != null) {
			tt2 = (JSONObject) vaccination.get(RequestElements.TT2);
			tt2Status = WOMEN_VACCINATION_STATUS.findEnum(tt2.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
			centerVisit.getTt2().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt2.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
			if (tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString().equals(""))
				centerVisit.getTt2().setVaccinationDate(null);
			else
				centerVisit.getTt2().setVaccinationDate(stringToDate(tt2.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));

			centerVisit.getTt2().setVaccineId(sc.getVaccinationService().getByName("TT2").getVaccineId());
			vaccines.add(centerVisit.getTt2());
			if (tt2Status.equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
				enrollmentVaccine = "tt2";
				enrollmentDate = centerVisit.getTt1().getVaccinationDate();
			}
		}
		if (vaccination.get(RequestElements.TT3) != null) {
			tt3 = (JSONObject) vaccination.get(RequestElements.TT3);
			tt3Status = WOMEN_VACCINATION_STATUS.findEnum(tt3.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
			centerVisit.getTt3().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt3.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
			if (tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString().equals(""))
				centerVisit.getTt3().setVaccinationDate(null);
			else
				centerVisit.getTt3().setVaccinationDate(stringToDate(tt3.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));

			centerVisit.getTt3().setVaccineId(sc.getVaccinationService().getByName("TT3").getVaccineId());
			vaccines.add(centerVisit.getTt3());
			if (tt3Status.equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
				enrollmentVaccine = "tt3";
				enrollmentDate = centerVisit.getTt1().getVaccinationDate();
			}
		}
		if (vaccination.get(RequestElements.TT4) != null) {
			tt4 = (JSONObject) vaccination.get(RequestElements.TT4);
			tt4Status = WOMEN_VACCINATION_STATUS.findEnum(tt4.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
			centerVisit.getTt4().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt4.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
			if (tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString().equals(""))
				centerVisit.getTt4().setVaccinationDate(null);
			else
				centerVisit.getTt4().setVaccinationDate(stringToDate(tt4.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			centerVisit.getTt4().setVaccineId(sc.getVaccinationService().getByName("TT4").getVaccineId());
			vaccines.add(centerVisit.getTt4());
			if (tt4Status.equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
				enrollmentVaccine = "tt4";
				enrollmentDate = centerVisit.getTt1().getVaccinationDate();
			}
		}
		if (vaccination.get(RequestElements.TT5) != null) {
			tt5 = (JSONObject) vaccination.get(RequestElements.TT5);
			tt5Status = WOMEN_VACCINATION_STATUS.findEnum(tt5.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
			centerVisit.getTt5().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt5.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
			if (tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString().equals(""))
				centerVisit.getTt5().setVaccinationDate(null);
			else
				centerVisit.getTt5().setVaccinationDate(stringToDate(tt5.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			centerVisit.getTt5().setVaccineId(sc.getVaccinationService().getByName("TT5").getVaccineId());
			vaccines.add(centerVisit.getTt5());
			if (tt5Status.equals(WOMEN_VACCINATION_STATUS.VACCINATED)) {
				enrollmentVaccine = "tt5";
				enrollmentDate = centerVisit.getTt1().getVaccinationDate();
			}
		}

		// vaccinations = (JSONArray)
		// objectToParse.get(RequestElements.VACCINATION_SCHEDULE);
		sameCenter = RestUtils.setBoolean((String) objectToParse.get(RequestElements.SAME_CENTER));
		mobileNoString = (String) demographics.get(RequestElements.WOMEN_PRIMARY_CONTACT);
		landlineNoString = (String) demographics.get(RequestElements.WOMEN_SECONDARY_CONTACT);

		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// ///////////////////////////PREPARE OBJECTS FOR API
		// CALLS//////////////////////////////////////////////
		// ///////////////////////////////////////////////////////////////////////////////////////////////

		// submitter.setMappedId(userId);

		Women women = new Women();
		women.setBirthdate(dateOfBirth);
		women.setFirstName(womenFirstName);
		women.setFatherFirstName(fatherFirstName);
		women.setBirthdate(dateOfBirth);
		women.setMaritalStatus(maritalStatus);
		women.setStatus(WOMENSTATUS.ENROLLMENT);
		User user = sc.getUserService().findUser(userId);

		// ///////////Address fields ///////////////////////
		Address add = new Address();
		add.setAddress1(address);
		add.setLandmark(landmark);
		add.setCityId(Integer.valueOf(city));
		// set town id
		add.setTown(town);
		// set uc id
		add.setUc(uc);
		// hardcoating the id for testing
		centerVisit.setVaccinationCenterId(centreId);

		centerVisit.setVaccinatorId(userId);
		centerVisit.setContactPrimary(mobileNoString);
		centerVisit.setContactSecondary(landlineNoString);

		try {
			ControllerUIHelper.doWomenEnrollment(DataEntrySource.MOBILE, projectId, women, dob, years, months, weeks, days, add, centerVisit, dateFormStart, user, enrollmentVaccine, enrollmentDate,
					vaccines, sc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HashMap<String, String> mobileErrors = new HashMap<String, String>();

		try {

			StringBuilder builder = new StringBuilder();

			for (String key : mobileErrors.keySet()) {
				builder.append(mobileErrors.get(key) + "\r\n");
			}

			// proceed to do enrollment if no errors found
			if (mobileErrors.size() == 0) {
				sc.commitTransaction();
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
			} else {
				// Log the errors for now
				GlobalParams.MOBILELOGGER.debug("Error(s) found in validation follows \n");
				GlobalParams.MOBILELOGGER.error(builder.toString());
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, (Map) mobileErrors);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
			sc.rollbackTransaction();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, null);
		} finally {
			sc.closeSession();
		}
	}

	public static Date stringToDate(String text) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		Date date = format.parse(text);
		return date;
	}

}
