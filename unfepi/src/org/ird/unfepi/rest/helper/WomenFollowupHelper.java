/**
 * 
 */
package org.ird.unfepi.rest.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.DemographicDetailsService;
import org.ird.unfepi.service.WomenService;
import org.json.simple.JSONObject;

/**
 * @author Safwan
 *
 */
public class WomenFollowupHelper {
	
	public static final String DEMOGRAPHICS = "demog";
	public static final String PROGRAM_DETAILS = "progDet";
	
	public static String getData(String womenId)
	{
		ServiceContext sc = Context.getServices();
		Women women = new Women();

		WomenService womenService = sc.getWomenService();

		women = womenService.findWomenById(Integer.parseInt(womenId), true, new String[] { "idMapper" });
		// a valid Women was provided
		if (women != null)
		{
			Map<String, Object> responseParams = new HashMap<String, Object>();
			responseParams.put(DEMOGRAPHICS, getDemographics(women));
			//responseParams.put(IMMUNIZATOINS, getImmunization(women));
			//responseParams.put(PROGRAM_DETAILS, getProgramDetails(women));
			responseParams.put(RequestElements.REQ_TYPE, RequestElements.REQ_TYPE_FETCH);
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, responseParams);
		}
		else
		{
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_ID_WOMEN_NOT_EXIST, null);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getDemographics(Women women)
	{
		ServiceContext sc = Context.getServices();

		String fatherFirstName;
		String fatherLastName;
		String womenFirstName;
		String womenLastName;
		String husbandFirstName;
		String husbandLastName;
		String dob;
		Date dateOfBirth;
		String epiNo;

		fatherFirstName = women.getFatherFirstName();
		fatherLastName = women.getFatherLastName();
		womenFirstName = women.getFirstName();
		womenLastName = women.getLastName();
		husbandFirstName = women.getHusbandFirstName();
		husbandLastName = women.getHusbandLastName();
		dateOfBirth = women.getBirthdate();
		List epiNumbersList = sc.getCustomQueryService().getDataBySQL(
				"select epiNumber from womenvaccination" + " where womenId=" + women.getMappedId() + " and vaccinationStatus IN ('VACCINATED') order by vaccinationDate DESC");
		epiNo = (String) (epiNumbersList.size() == 0 ? "" : epiNumbersList.get(0));

		JSONObject demoJson = new JSONObject();

		try
		{
			demoJson.put(RequestElements.WOMEN_FIRST_NAME, womenFirstName);
			demoJson.put(RequestElements.WOMEN_LAST_NAME, womenLastName);
			demoJson.put(RequestElements.FATHER_FIRST_NAME, fatherFirstName);
			demoJson.put(RequestElements.FATHER_LAST_NAME, fatherLastName);
			demoJson.put(RequestElements.HUSBAND_FIRST_NAME, husbandFirstName);
			demoJson.put(RequestElements.HUSBAND_LAST_NAME, husbandLastName);
			demoJson.put(RequestElements.DOB, RestUtils.dateToString(dateOfBirth, null));
			demoJson.put(RequestElements.EPI_NO, epiNo);
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return demoJson;
	}
	
	@SuppressWarnings("unchecked")
	private static JSONObject getProgramDetails(Women women)
	{
		JSONObject jsonProgram = null;

		DemographicDetailsService demoService;
		ServiceContext sc = Context.getServices();
		demoService = sc.getDemographicDetailsService();
		List<ContactNumber> numbers;
		String primaryContact = "";
		String secondaryContact = "";

		if (women == null)
		{
			return jsonProgram;
		}
		try
		{
			// Get Contact Numbers
			numbers = demoService.getContactNumber(women.getMappedId(), true, new String[] { "idMapper" });
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
			
			// package this all in a JSONObject to be sent back
			jsonProgram = new JSONObject();
			jsonProgram.put(RequestElements.PRIMARY_NUMBER, primaryContact);
			jsonProgram.put(RequestElements.SECONDARY_NUMBER, secondaryContact);
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return jsonProgram;
	}

}
