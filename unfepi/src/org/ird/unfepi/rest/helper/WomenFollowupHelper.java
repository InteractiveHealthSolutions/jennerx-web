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
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
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
	
	public static final String DEMOGRAPHICS = "Demographics";
	public static final String PROGRAM_DETAILS = "progDet";
	public static final String VACCINATION = "Vaccination";
	static ServiceContext sc = Context.getServices();
	
	public static String getData(String womenId)
	{
		
		Women women = new Women();

		WomenService womenService = sc.getWomenService();

		women = womenService.findWomenByIdentifier(womenId, false, new String[] { "idMapper" });
		// a valid Women was provided
		if (women != null)
		{
			Map<String, Object> responseParams = new HashMap<String, Object>();
			responseParams.put(VACCINATION, getVaccines(women));
			responseParams.put(DEMOGRAPHICS, getDemographics(women));
			//
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
		} catch (Exception e){
			e.printStackTrace();
		}
		
		address = demoService.getPrimaryAddress(women.getMappedId(), false, new String[] {"idMapper"}).get(0);
			
		JSONObject demoJson = new JSONObject();

		try
		{
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
			//demoJson.put(RequestElements.WOMEN_ADDRESS_OTHER_CITY, address.getCity().getFullName());
			demoJson.put(RequestElements.ADD_TOWN, address.getAddtown());
			demoJson.put(RequestElements.ADD_UC, address.getAddUc());
			demoJson.put(RequestElements.ADD_LANDMARK, address.getAddLandmark());
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return demoJson;
	}
	
	private static JSONObject getVaccines(Women women){
		List<WomenVaccination> vaccineList = sc.getWomenVaccinationService().findByWomenId(women.getMappedId());
		JSONObject womenVaccine = new JSONObject();
		
		
		for( int i = 0; i < vaccineList.size(); i++){
			if(i == 0){
				JSONObject vaccine = new JSONObject();
				vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, vaccineList.get(0).getVaccinationDate().toString());
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(0).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT1, vaccine);
			}
				
			if(i == 1){
				JSONObject vaccine = new JSONObject();
				vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, vaccineList.get(1).getVaccinationDate().toString());
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(1).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT2, vaccine);
			}

			if(i == 2){
				JSONObject vaccine = new JSONObject();
				vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, vaccineList.get(2).getVaccinationDate().toString());
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(2).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT3, vaccine);
			}
				
			if(i == 3){
				JSONObject vaccine = new JSONObject();
				vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, vaccineList.get(3).getVaccinationDate().toString());
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(3).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT4, vaccine);
			}
				
			if(i == 4){
				JSONObject vaccine = new JSONObject();
				vaccine.put(RequestElements.WOMEN_VACCINATION_DATE, vaccineList.get(4).getVaccinationDate().toString());
				vaccine.put(RequestElements.WOMEN_VACCINATION_STATUS, vaccineList.get(4).getVaccinationStatus().toString());
				womenVaccine.put(RequestElements.TT5, vaccine);
			}
				
		}
		return womenVaccine;
	}
	
	/*@SuppressWarnings("unchecked")
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
	}*/

}
