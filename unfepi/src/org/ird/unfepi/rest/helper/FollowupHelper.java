package org.ird.unfepi.rest.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.IdentifierType;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.exception.VaccinationDataException;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.DemographicDetailsService;
import org.ird.unfepi.service.IdMapperService;
import org.ird.unfepi.service.VaccinationService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;
import org.ird.unfepi.web.validator.ValidatorUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FollowupHelper
{
	public static final String DEMOGRAPHICS = "demog";
	public static final String PROGRAM_DETAILS = "progDet";
	public static final String IMMUNIZATOINS = "immuniz";

	public static String saveForm(JSONObject form)
	{
		ServiceContext sc = Context.getServices();

		String identifier;
		Integer mappedId = 0;
		JSONArray vaccinations;
		JSONArray supplementary;
		boolean smsApproval;
		String primaryNo;
		String secondaryNo;
		LotterySms smsPreferences = null;
		Integer userId;
		Integer centreId;
		User user;
		String newEpiNo;
		String nic;

		List<VaccineSchedule> schedule = new ArrayList<VaccineSchedule>();
		VaccinationCenterVisit centreVisit;
		

		identifier = form.get(RequestElements.CHILD_ID).toString();
		newEpiNo = form.get(RequestElements.EPI_NO).toString();
		vaccinations = (JSONArray) form.get(RequestElements.VACCINATION_SCHEDULE);
		supplementary = (JSONArray) form.get(RequestElements.VACCINATION_SUPPLEMENTARY);
		smsApproval = Boolean.parseBoolean(form.get(RequestElements.SMS_REMINDER_APP).toString());
		userId = Integer.valueOf(form.get(RequestElements.LG_USERID).toString());
		centreId = Integer.valueOf((String) form.get(RequestElements.ENROLLEMNT_CENTRE).toString());
		primaryNo = (String) form.get(RequestElements.PRIMARY_NUMBER);
		nic =  (String) form.get(RequestElements.FATHER_NIC);
		secondaryNo = (String) form.get(RequestElements.SECONDARY_NUMBER);
		String firstName = form.get(RequestElements.CHILD_FIRST_NAME).toString(); // to get the first name
		//System.out.println(firstName);

		Child child = new Child();
		
		ChildService childService = sc.getChildService();
		
		// changes made to update the name
		/*child.setFirstName(firstName);
		try {
			childService.updateChild(child);
		} catch (ChildDataInconsistencyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		child = childService.findChildById(identifier, false, new String[] { "idMapper" });
		
		// a valid child was provided
		if (child != null)
		{
			mappedId = child.getMappedId();
			child.setFirstName(firstName);
			child.setNic(nic);
			try {
				childService.updateChild(child);
				//childService.saveChild(child);
			} catch (ChildDataInconsistencyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		user = new User(userId);

		// fill schedule
		// Iterate to loop through all the next vaccines scheduled.
		Iterator i = vaccinations.iterator();
		Vaccine vaccine;
		String vaccineName;
		Date vaccinationDate;
		Date dueDate;
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

			row.setChildId(mappedId);

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

		// smsPreferences = new LotterySms();
		// smsPreferences.setHasApprovedReminders(smsApproval);
		// smsPreferences.setHasApprovedLottery(null);
		smsPreferences = sc.getChildService().findLotterySmsByChild(mappedId, true, 0, 2,  null).get(0);
		centreVisit = new VaccinationCenterVisit(mappedId, new Date(), userId, centreId, newEpiNo, null, primaryNo, secondaryNo, smsPreferences);
		
		
		try
		{
			HashMap<String, String> mobileErrors = new HashMap<String, String>();
			ValidatorUtils.validateFollowupForm(DataEntrySource.MOBILE, schedule, centreVisit, mobileErrors, null, sc);
			StringBuilder builder = new StringBuilder();
			for (String key : mobileErrors.keySet())
			{
				builder.append(mobileErrors.get(key) + "\r\n");
			}
			if (mobileErrors.size() == 0)
			{
				ControllerUIHelper.doFollowup(DataEntrySource.MOBILE, centreVisit, schedule, new Date(), user, sc);
				sc.commitTransaction();
				if (supplementary.size() > 0)
				{
					List<String> vaccineNames = new ArrayList<String>();
					for (int t = 0; t < supplementary.size(); t++)
					{
						JSONObject j = (JSONObject) supplementary.get(t);
						vaccineNames.add(j.get(RequestElements.VACCINENAME).toString());
					}
					SupplementaryVaccinesHepler.save(child.getMappedId(), newEpiNo, supplementary, centreVisit.getVaccinatorId(), userId);
				}

				// Map for all params to be returned
				Map<String, Object> userdata = new HashMap<String, Object>();
				userdata.put(RequestElements.REQ_TYPE, RequestElements.REQ_TYPE_SUBMIT);
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, userdata);
			}
			else
			{
				// Log the errors for now
				GlobalParams.MOBILELOGGER.debug("Error(s) found in validation follows \n");
				GlobalParams.MOBILELOGGER.error(builder.toString());
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, (Map) mobileErrors);
			}
		}
		catch (VaccinationDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ChildDataInconsistencyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Throwable th)
		{
			String s = th.getMessage();
		}

		return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
	}

	public static String getData(String childId)
	{
		ServiceContext sc = Context.getServices();
		Child child = new Child();

		ChildService childService = sc.getChildService();

		child = childService.findChildById(childId, true, new String[] { "idMapper" });
		// a valid child was provided
		if (child != null)
		{
			Map<String, Object> responseParams = new HashMap<String, Object>();
			responseParams.put(DEMOGRAPHICS, getDemographics(child));
			responseParams.put(IMMUNIZATOINS, getImmunization(child));
			responseParams.put(PROGRAM_DETAILS, getProgramDetails(child));
			responseParams.put(RequestElements.REQ_TYPE, RequestElements.REQ_TYPE_FETCH);
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, responseParams);
		}
		else
		{
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_ID_CHILD_NOT_EXIST, null);
		}
	}

	private static JSONObject getDemographics(Child child)
	{
		ServiceContext sc = Context.getServices();

		String fatherFirstName;
		String fatherLastName;
		String childFirstName;
		String childLastName;
		String gender;
		String dob;
		Date dateOfBirth;
		String epiNo;
		String validNIC;

		fatherFirstName = child.getFatherFirstName();
		fatherLastName = child.getFatherLastName();
		childFirstName = child.getFirstName();
		childLastName = child.getLastName();
		gender = child.getGender().name();
		dateOfBirth = child.getBirthdate();
		// GetNIC
		validNIC = child.getNic();
		
		List epiNumbersList = sc.getCustomQueryService().getDataBySQL(
				"select epiNumber from vaccination" + " where childId=" + child.getMappedId() + " and vaccinationStatus IN ('VACCINATED','LATE_VACCINATED') order by vaccinationDate DESC");
		epiNo = (String) (epiNumbersList.size() == 0 ? "" : epiNumbersList.get(0));

		JSONObject demoJson = new JSONObject();

		try
		{
			demoJson.put(RequestElements.CHILD_FIRST_NAME, childFirstName);
			demoJson.put(RequestElements.CHILD_LAST_NAME, childLastName);
			demoJson.put(RequestElements.FATHER_FIRST_NAME, fatherFirstName);
			demoJson.put(RequestElements.FATHER_LAST_NAME, fatherLastName);
			demoJson.put(RequestElements.CHILD_GENDER, gender);
			demoJson.put(RequestElements.DOB, RestUtils.dateToString(dateOfBirth, null));
			demoJson.put(RequestElements.EPI_NO, epiNo);
			demoJson.put(RequestElements.FATHER_NIC, validNIC);
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return demoJson;
	}

	private static JSONArray getImmunization(Child child)
	{
		JSONObject temp;
		JSONArray jsonVaccinations = new JSONArray();

		ServiceContext sc = Context.getServices();
		VaccinationService vc = sc.getVaccinationService();

		List<Vaccination> vaccinationsList = vc.findVaccinationRecordByCriteria(child.getMappedId(), null, null, null, null, null, null, null, null, null, null, false, 0, 111, true,
				new String[] {
						"vaccine", "createdByUserId", "lastEditedByUserId" }, null);

		for (Vaccination v : vaccinationsList)
		{
			// to avoid adding Supplementary vaccines (whose id are 14 - 16) in normal routine vaccines Json
			if (!(v.getVaccineId() >= 14 && v.getVaccineId() <= 16))
			{
				temp = new JSONObject();
				temp.put(RequestElements.DATE_OF_VACCINATION, v.getVaccinationDate() != null ? RestUtils.dateToString(v.getVaccinationDate(), null) : null);

				// Can't let a vaccination without Vaccine and a Due date pass
				// therefore no check for NLP
				temp.put(RequestElements.NEXT_ALLOTTED_DATE, RestUtils.dateToString(v.getVaccinationDuedate(), null));
				temp.put(RequestElements.VACCINENAME, v.getVaccine().getName());
				if (v.getVaccinationStatus() == VACCINATION_STATUS.VACCINATED)
				{
					temp.put(RequestElements.IS_AVAILABLE_FOR_VACCINATION, Boolean.toString(true));
				}
				else
				{
					temp.put(RequestElements.IS_AVAILABLE_FOR_VACCINATION, Boolean.toString(false));
				}
				temp.put(RequestElements.VACCINATION_STATUS, v.getVaccinationStatus() != null ? v.getVaccinationStatus().toString() : "");

				temp.put(RequestElements.VACCINATION_CENTER, v.getVaccinationCenter() != null ? v.getVaccinationCenter().getName() : "");

				jsonVaccinations.add(temp);
			}
		}
		return jsonVaccinations;
	}

	private static JSONObject getProgramDetails(Child child)
	{
		JSONObject jsonProgram = null;
		// boolean lotteryApproval = false;
		boolean smsApproval = false;

		DemographicDetailsService demoService;
		ServiceContext sc = Context.getServices();
		demoService = sc.getDemographicDetailsService();
		List<ContactNumber> numbers;
		String primaryContact = "";
		String secondaryContact = "";
		if (child == null)
		{
			return jsonProgram;
		}
		try
		{
			// Get Contact Numbers
			numbers = demoService.getContactNumber(child.getMappedId(), true, new String[] { "idMapper" });
			for (ContactNumber number : numbers)
			{
				if (ContactType.PRIMARY == number.getNumberType())
				{
					primaryContact = number.getNumber();
				}
				else if (ContactType.SECONDARY == number.getNumberType())
				{
					secondaryContact = number.getNumber();
				}
			}
			// Get SMS and Lottery answers from the last vaccination visit
			// Note:using custom query here as it would have been more complex
			// to use API to get the last answers for the preferences
			String sqlFetchLastPreferences = "SELECT vaccination.hasApprovedLottery, lotterysms.hasApprovedReminders " + "FROM vaccination "
					+ "LEFT JOIN lotterysms ON lotterysms.mappedid=vaccination.childId "
					+ "AND lotterysms.serialNumber = (SELECT serialNumber FROM lotterysms WHERE lotterysms.mappedid=vaccination.childid "
					+ "ORDER BY datepreferencechanged DESC, createdDate DESC LIMIT 1) " + "WHERE vaccination.vaccinationStatus = 'VACCINATED' AND vaccination.childId='"
					+ child.getMappedId() + "' "
					+ "ORDER BY vaccinationDate DESC LIMIT 1; ";
			List<Object> results = sc.getCustomQueryService().getDataBySQL(sqlFetchLastPreferences);
			if (results.size() == 1)
			{
				Object[] values = (Object[]) results.get(0);
				if (values.length == 2)
				{
					// // Lottery Approval is null now
					// lotteryApproval = ((Boolean) values[0]);
					smsApproval = ((Boolean) values[1]);
				}
			}
			// package this all in a JSONObject to be sent back
			jsonProgram = new JSONObject();
			jsonProgram.put(RequestElements.PRIMARY_NUMBER, primaryContact);
			jsonProgram.put(RequestElements.SECONDARY_NUMBER, secondaryContact);
			jsonProgram.put(RequestElements.SMS_REMINDER_APP, Boolean.toString(smsApproval));
			// jsonProgram.put(RequestElements.LOTTERY_APPROVAL, Boolean.toString(lotteryApproval));
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			GlobalParams.MOBILELOGGER.error("Error while fetching lottery and sms preferences for mobile");
			e.printStackTrace();
			return null;
		}
		return jsonProgram;
	}

	public static String switchIdentifier(String oldId, String newId)
	{
		ServiceContext sc = Context.getServices();
		HashMap<String, Object> resp = new HashMap<String, Object>();
		try
		{
			Child child = new Child();
			ChildService childService = sc.getChildService();
			child = childService.findChildById(oldId, false, new String[] { "idMapper" });
			// a valid child was provided
			if (child == null)
			{
				resp.put("error", "Identifier did not match any record");
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, resp);
			}

			// Check ID has not been updated before.
			// UPDATING AN ID MULTIPLE TIMES IS DISABLED
			boolean idAvailable = false;
			String sqlUpdatedBefore = "SELECT count(mappedId) FROM updated_ids" + " WHERE mappedId = " + child.getMappedId();
			List<Object> result = sc.getCustomQueryService().getDataBySQL(sqlUpdatedBefore);
			Object[] rowsReturned;
			if (result.size() != 1)
			{
				resp.put("error", "Error while verifying availability of ID");
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, resp);
			}

			try
			{
				// A RESULT OF 0 INDICATES THIS ID WAS NOT UPDATED, IF THIS
				// CHECK WORKS THEN
				// THEN THE VALUE OF RESULT WOULD ALWAYS BE '0' OR '1'
				if (((BigInteger) result.get(0)).intValue() == 0)
				{
					idAvailable = true;
				}
				else
				{
					resp.put("error", "The ID of this Child has already been updated once before, you can " + "not update it again yourself. Please contact the Zindagi Mehfooz team");
					return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, resp);
				}
			}
			catch (Exception e)
			{
				GlobalParams.MOBILELOGGER.error(e.getMessage());
				e.printStackTrace();
				resp.put("error", "Error while verifying availability of ID");
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, resp);
			}

			IdMapperService idService = sc.getIdMapperService();
			IdMapper childIdMapper = child.getIdMapper();
			List<Identifier> identifiers = childIdMapper.getIdentifiers();
			sc.beginTransaction();
			Iterator<Identifier> idIterator = identifiers.iterator();
			while (idIterator.hasNext())
			{
				// an old id was found
				Identifier oldIdentifer = idIterator.next();
				oldIdentifer.setPreferred(false);
			}

			// Create the new Identifier
			Identifier newIdentifier = new Identifier();
			newIdentifier.setIdentifier(newId);
			newIdentifier.setIdentifierType((IdentifierType) sc.getCustomQueryService()
					.getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.CHILD_PROJECT_ID + "'").get(0));
			newIdentifier.setPreferred(true);
			newIdentifier.setIdMapper(childIdMapper);

			// Add the new identifier to the list maintained by IdMapper object
			identifiers.add(newIdentifier);
			childIdMapper.setIdentifiers(identifiers);
			idService.saveIdMapper(childIdMapper);
			sc.commitTransaction();

			// Record that this identifier has been switched
			Session se = null;
			Transaction transaction = null;
			String sql = "INSERT INTO updated_ids(mappedId) VALUES(" + child.getMappedId() + ");";
			try
			{
				se = Context.getNewSession();
				transaction = se.beginTransaction();
				SQLQuery query = se.createSQLQuery(sql.toString());
				query.executeUpdate();
				transaction.commit();
			}
			catch (Exception ex)
			{
				if (transaction != null)
					transaction.rollback();
				return ResponseBuilder.buildResponse(ResponseStatus.STATUS_DATA_ERROR, null);
			}
			finally
			{
				se.close();
			}

			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, resp);
		}
		catch (Exception e)
		{
			sc.rollbackTransaction();
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.equals("Error creating new identifier");
			GlobalParams.MOBILELOGGER.equals(e);
			e.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, resp);
		}
	}
}
