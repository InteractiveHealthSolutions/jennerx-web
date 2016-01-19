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
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Women.WOMENSTATUS;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.DemographicDetailsService;
import org.ird.unfepi.service.WomenService;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;
import org.json.simple.JSONObject;

/**
 * @author Safwan
 *
 */
public class WomenFollowupHelper {

	public static final String DEMOGRAPHICS = "Demographics";
	public static final String PROGRAM_DETAILS = "progDet";
	public static final String VACCINATION = "Vaccination";
	static ServiceContext sc = Context.getServices();

	public static String getData(String womenId) throws ParseException {

		Women women = new Women();

		WomenService womenService = sc.getWomenService();

		women = womenService.findWomenByIdentifier(womenId, false, new String[]{"idMapper"});
		// a valid Women was provided
		if (women != null) {
			Map<String, Object> responseParams = new HashMap<String, Object>();
			responseParams.put(VACCINATION, getVaccines(women));
			responseParams.put(DEMOGRAPHICS, getDemographics(women));
			//
			// responseParams.put(IMMUNIZATOINS, getImmunization(women));
			// responseParams.put(PROGRAM_DETAILS, getProgramDetails(women));
			responseParams.put(RequestElements.REQ_TYPE, RequestElements.REQ_TYPE_FETCH);
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, responseParams);
		} else {
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_ID_WOMEN_NOT_EXIST, null);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes", "unused"})
	private static JSONObject getDemographics(Women women) {

		String fatherFirstName;
		String womenFirstName;
		String husbandFirstName;
		String dob;
		Date dateOfBirth;
		String epiNo;
		String maritalStatus;

		Address address = new Address();

		fatherFirstName = women.getFatherFirstName();
		womenFirstName = women.getFirstName();
		husbandFirstName = women.getHusbandFirstName();
		dateOfBirth = women.getBirthdate();
		maritalStatus = women.getMaritalStatus();

		List epiNumbersList = sc.getCustomQueryService().getDataBySQL(
				"select epiNumber from womenvaccination" + " where womenId=" + women.getMappedId() + " and vaccinationStatus IN ('VACCINATED') order by vaccinationDate DESC");
		epiNo = (String) (epiNumbersList.size() == 0 ? "" : epiNumbersList.get(0));

		DemographicDetailsService demoService;
		demoService = sc.getDemographicDetailsService();
		List<ContactNumber> numbers;
		String primaryContact = "";
		String secondaryContact = "";

		try {
			// Get Contact Numbers
			numbers = demoService.getContactNumber(women.getMappedId(), true, new String[]{"idMapper"});
			for (ContactNumber number : numbers) {
				if (ContactType.PRIMARY == number.getNumberType()) {
					primaryContact = number.getNumber();
				} else if (ContactType.SECONDARY == number.getNumberType()) {
					secondaryContact = number.getNumber();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		address = demoService.getPrimaryAddress(women.getMappedId(), false, new String[]{"idMapper"}).get(0);

		JSONObject demoJson = new JSONObject();

		try {
			demoJson.put(RequestElements.WOMEN_FIRST_NAME, womenFirstName);
			demoJson.put(RequestElements.FATHER_FIRST_NAME, fatherFirstName);
			demoJson.put(RequestElements.HUSBAND_FIRST_NAME, husbandFirstName);
			demoJson.put(RequestElements.DOB, RestUtils.dateToString(dateOfBirth, null));
			demoJson.put(RequestElements.WOMEN_MARITAL_STATUS, maritalStatus);
			demoJson.put(RequestElements.EPI_NO, epiNo);
			demoJson.put(RequestElements.WOMEN_PRIMARY_CONTACT, primaryContact);
			demoJson.put(RequestElements.WOMEN_SECONDARY_CONTACT, secondaryContact);
			demoJson.put(RequestElements.WOMEN_ADDRESS, address.getAddress1());
			demoJson.put(RequestElements.ADD_CITY, address.getCity());
			// demoJson.put(RequestElements.WOMEN_ADDRESS_OTHER_CITY,
			// address.getCity().getFullName());
			demoJson.put(RequestElements.ADD_TOWN, address.getTown());
			demoJson.put(RequestElements.ADD_UC, address.getUc());
			demoJson.put(RequestElements.ADD_LANDMARK, address.getLandmark());
		} catch (Exception e) {
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return demoJson;
	}

	@SuppressWarnings("unchecked")
	private static JSONObject getVaccines(Women women) throws ParseException {
		List<WomenVaccination> vaccineList = sc.getWomenVaccinationService().findByWomenId(women.getMappedId());
		JSONObject womenVaccine = new JSONObject();

		for (int i = 0; i < vaccineList.size(); i++) {
			if (i == 0) {
				JSONObject vaccine = new JSONObject();
				if (vaccineList.get(0).getVaccinationDate() != null)
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, dateTimeToDate(vaccineList.get(0).getVaccinationDate().toString()));
				else
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, "");
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(0).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT1, vaccine);
			}

			if (i == 1) {
				JSONObject vaccine = new JSONObject();
				if (vaccineList.get(1).getVaccinationDate() != null)
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, dateTimeToDate(vaccineList.get(1).getVaccinationDate().toString()));
				else
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, "");
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(1).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT2, vaccine);
			}

			if (i == 2) {
				JSONObject vaccine = new JSONObject();
				if (vaccineList.get(2).getVaccinationDate() != null)
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, dateTimeToDate(vaccineList.get(2).getVaccinationDate().toString()));
				else
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, "");
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(2).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT3, vaccine);
			}

			if (i == 3) {
				JSONObject vaccine = new JSONObject();
				if (vaccineList.get(3).getVaccinationDate() != null)
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, dateTimeToDate(vaccineList.get(3).getVaccinationDate().toString()));
				else
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, "");
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(3).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT4, vaccine);
			}

			if (i == 4) {
				JSONObject vaccine = new JSONObject();
				if (vaccineList.get(4).getVaccinationDate() != null)
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, dateTimeToDate(vaccineList.get(4).getVaccinationDate().toString()));
				else
					vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, "");
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(4).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT5, vaccine);
			}

		}
		return womenVaccine;
	}

	@SuppressWarnings({"unused", "unchecked", "rawtypes"})
	public static String saveForm(JSONObject objectToParse) throws ParseException {
		// demographics
		String projectId;
		String womenFirstName;
		String fatherFirstName;
		String husbandFirstName;
		String dob;
		String maritalStatus;
		String epiNo;
		String days;
		String weeks;
		String months;
		String years;
		Date dateFormStart = new Date();

		// Address
		String address;
		String uc;
		String landmark;
		String city;
		String town;

		// Program details
		/*
		 * String currentVaccine; String currentVaccineDueDate;
		 */
		boolean sameCenter;
		// String strSameCenter = null;
		String mobileNoString;
		String landlineNoString;
		JSONObject demographics;
		JSONObject vaccination;
		// JSONObject visitDateObject;
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

		// User infomation
		Integer userId;
		Integer centreId;
		String userName;

		// //////////////////////////////////////////////////////////////////////////////
		// //////////////////////////FIELDS FROM
		// MOBILE//////////////////////////////////
		// //////////////////////////////////////////////////////////////////////////////

		demographics = (JSONObject) objectToParse.get(RequestElements.WOMEN_DEMOGRAPHICS);
		vaccination = (JSONObject) objectToParse.get(RequestElements.WOMEN_VACCINATION);
		// visitDateObject = (JSONObject)
		// objectToParse.get(RequestElements.VACCINATION_CENTER_VISIT_DATE);

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

		String visitDate = dateTimeGMTToDate((String) objectToParse.get(RequestElements.VACCINATION_CENTER_VISIT_DATE));

		// epiNo = (String) objectToParse.get(RequestElements.EPI_NO);
		// isEstimated = RestUtils.setBoolean((String)
		// objectToParse.get(RequestElements.IS_BIRHTDATE_ESTIMATED));

		// address
		address = (String) demographics.get(RequestElements.WOMEN_ADDRESS);
		uc = (String) demographics.get(RequestElements.ADD_UC);
		landmark = (String) demographics.get(RequestElements.ADD_LANDMARK);
		town = (String) demographics.get(RequestElements.ADD_TOWN);
		city = (String) demographics.get(RequestElements.ADD_CITY).toString();

		WomenService womenService = sc.getWomenService();
		Women women = womenService.findWomenByIdentifier(projectId, false, new String[]{"idMapper"});
		women.setBirthdate(dateOfBirth);
		women.setFirstName(womenFirstName);
		women.setFatherFirstName(fatherFirstName);
		women.setBirthdate(dateOfBirth);
		women.setMaritalStatus(maritalStatus);
		women.setStatus(WOMENSTATUS.ENROLLMENT);
		User user = sc.getUserService().findUser(userId);

		vaccines = sc.getWomenVaccinationService().findByWomenId(women.getMappedId());

		// program details
		if (vaccines.size() > 0 && !vaccines.get(0).getVaccinationStatus().toString().equalsIgnoreCase("Scheduled")) {
			centerVisit.setTt1(vaccines.get(0));
		} else {
			tt1 = (JSONObject) vaccination.get(RequestElements.TT1);
			if (tt1 == null) {
				tt1Status = WOMEN_VACCINATION_STATUS.findEnum(WOMEN_VACCINATION_STATUS.NOT_VACCINATED.toString());
				centerVisit.getTt1().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum("NVAC"));
				centerVisit.getTt1().setVaccinationDate(null);
			} else {
				tt1Status = WOMEN_VACCINATION_STATUS.findEnum(tt1.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
				centerVisit.getTt1().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt1.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
				centerVisit.getTt1().setVaccinationDate(stringToDate(tt1.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			}

			centerVisit.getTt1().setVaccineId(sc.getVaccinationService().getByName("TT1").getVaccineId());
		}

		if (vaccines.size() > 1) {
			centerVisit.setTt2(vaccines.get(1));
		} else {
			tt2 = (JSONObject) vaccination.get(RequestElements.TT2);
			if (tt2 == null) {
				tt2Status = WOMEN_VACCINATION_STATUS.findEnum(WOMEN_VACCINATION_STATUS.NOT_VACCINATED.toString());
				centerVisit.getTt2().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum("NVAC"));
				centerVisit.getTt2().setVaccinationDate(null);
			} else {
				tt2Status = WOMEN_VACCINATION_STATUS.findEnum(tt2.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
				centerVisit.getTt2().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt2.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
				centerVisit.getTt2().setVaccinationDate(stringToDate(tt2.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			}
			centerVisit.getTt2().setVaccineId(sc.getVaccinationService().getByName("TT2").getVaccineId());
		}

		if (vaccines.size() > 2) {
			centerVisit.setTt3(vaccines.get(2));
		} else {
			tt3 = (JSONObject) vaccination.get(RequestElements.TT3);
			if (tt3 == null) {
				tt3Status = WOMEN_VACCINATION_STATUS.findEnum(WOMEN_VACCINATION_STATUS.NOT_VACCINATED.toString());
				centerVisit.getTt3().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum("NVAC"));
				centerVisit.getTt3().setVaccinationDate(null);
			} else {
				tt3Status = WOMEN_VACCINATION_STATUS.findEnum(tt3.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
				centerVisit.getTt3().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt3.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
				centerVisit.getTt3().setVaccinationDate(stringToDate(tt3.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			}
			centerVisit.getTt3().setVaccineId(sc.getVaccinationService().getByName("TT3").getVaccineId());
		}

		if (vaccines.size() > 3) {
			centerVisit.setTt4(vaccines.get(3));
		} else {
			tt4 = (JSONObject) vaccination.get(RequestElements.TT4);
			if (tt4 == null) {
				tt4Status = WOMEN_VACCINATION_STATUS.findEnum(WOMEN_VACCINATION_STATUS.NOT_VACCINATED.toString());
				centerVisit.getTt4().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum("NVAC"));
				centerVisit.getTt4().setVaccinationDate(null);
			} else {
				tt4Status = WOMEN_VACCINATION_STATUS.findEnum(tt4.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
				centerVisit.getTt4().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt4.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
				centerVisit.getTt4().setVaccinationDate(stringToDate(tt4.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			}
			centerVisit.getTt4().setVaccineId(sc.getVaccinationService().getByName("TT4").getVaccineId());
			vaccines.add(centerVisit.getTt4());
		}
		if (vaccines.size() > 4) {
			centerVisit.setTt5(vaccines.get(4));
		} else {
			tt5 = (JSONObject) vaccination.get(RequestElements.TT5);
			if (tt5 == null) {
				tt5Status = WOMEN_VACCINATION_STATUS.findEnum(WOMEN_VACCINATION_STATUS.NOT_VACCINATED.toString());
				centerVisit.getTt5().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum("NVAC"));
				centerVisit.getTt5().setVaccinationDate(null);
			} else {
				tt5Status = WOMEN_VACCINATION_STATUS.findEnum(tt5.get(RequestElements.WOMEN_VACCINATION_STATUS).toString());
				centerVisit.getTt5().setVaccinationStatus(WOMEN_VACCINATION_STATUS.findEnum(tt5.get(RequestElements.WOMEN_VACCINATION_STATUS).toString()));
				centerVisit.getTt5().setVaccinationDate(stringToDate(tt5.get(RequestElements.WOMEN_VACCINATION_DATE).toString()));
			}
			centerVisit.getTt5().setVaccineId(sc.getVaccinationService().getByName("TT5").getVaccineId());
			vaccines.add(centerVisit.getTt5());
		}

		sameCenter = RestUtils.setBoolean((String) objectToParse.get(RequestElements.SAME_CENTER));
		mobileNoString = (String) demographics.get(RequestElements.WOMEN_PRIMARY_CONTACT);
		landlineNoString = (String) demographics.get(RequestElements.WOMEN_SECONDARY_CONTACT);

		// ///////////////////////////////////////////////////////////////////////////////////////////////
		// ///////////////////////////PREPARE OBJECTS FOR API
		// CALLS//////////////////////////////////////////////
		// ///////////////////////////////////////////////////////////////////////////////////////////////

		// ///////////Address fields ///////////////////////
		List<Address> prevAdd = sc.getDemographicDetailsService().getAddress(women.getMappedId(), false, new String[]{"idMapper"});
		Address add = prevAdd.get(0);
		add.setAddress1(address);
		add.setLandmark(landmark);
		add.setCityId(Integer.valueOf(city));
		// set town id
		add.setTown(town);
		// set uc id
		add.setUc(uc);

		centerVisit.setVaccinationCenterId(centreId);

		centerVisit.setVaccinatorId(userId);
		centerVisit.setContactPrimary(mobileNoString);
		centerVisit.setContactSecondary(landlineNoString);
		centerVisit.setWomenId(women.getMappedId());
		centerVisit.setVisitDate(stringToDate(visitDate));

		try {
			ControllerUIHelper.doWomenFollowup(DataEntrySource.MOBILE, centerVisit, women.getDateEnrolled(), women, add, user, sc);
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

	public static String dateTimeToDate(String text) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
		String dateInString = new SimpleDateFormat("dd/MM/yyyy").format(date);
		return dateInString;
	}

	public static String dateTimeGMTToDate(String text) throws ParseException {
		Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(text);
		String dateInString = new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
		return dateInString;
	}

}
