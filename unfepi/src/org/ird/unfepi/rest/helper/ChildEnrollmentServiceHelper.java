package org.ird.unfepi.rest.helper;

import java.util.Date;
import java.util.Iterator;

import javax.ws.rs.Path;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.EncounterType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.IdentifierType;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;
import org.ird.unfepi.utils.EncounterUtil;
import org.joda.time.DateTimeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChildEnrollmentServiceHelper {

	
	public static String addEnrollments(JSONArray jsonArray,ServiceContext sc){
		
		StringBuilder string=new StringBuilder();
		for(int i=0;i<jsonArray.size();i++)
		{
			try {
				string.append(createEnrollment((JSONObject)jsonArray.get(i),sc));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return string.toString();
	}
	
	
	public static String createEnrollment(JSONObject objectToParse,ServiceContext sc) throws ChildDataInconsistencyException
	{
		
		
		
		
		//ServiceContext sc = Context.getServices();
	String	childIdentifier=(String)objectToParse.get(RequestElements.CHILD_IDENTIFIER);
	String	firstName=(String) objectToParse.get(RequestElements.FIRST_NAME);
	String	lastName=(String) objectToParse.get(RequestElements.LAST_NAME);
	String	gender=(String) objectToParse.get(RequestElements.GENDER);
	String	birthDate=(String) objectToParse.get(RequestElements.BIRTH_DATE);
	String	motherFirstName=(String) objectToParse.get(RequestElements.MOTHER_FIRST_NAME);
	String	contactNumber1=(String) objectToParse.get(RequestElements.CONTACT_NUMBER);
	String		address1=(String)objectToParse.get(RequestElements.ADDRESS1);
	String		address2=(String)objectToParse.get(RequestElements.ADDRESS2);
	int		userId = Integer.valueOf(objectToParse.get(RequestElements.LG_USERID).toString());
	String		creator=(String) objectToParse.get(RequestElements.CREATOR);
	String		lastEditor=(String) objectToParse.get(RequestElements.LASTEDITOR);
	
	String		createdDate=(String) objectToParse.get(RequestElements.CREATED_DATE);
	String		lastEditedDate=(String) objectToParse.get(RequestElements.LAST_EDITED_DATE);
	String	enrolledDate=(String)objectToParse.get(RequestElements.ENROLLED_DATE);
		
		//vaccinations= (JSONArray)objectToParse.get("vaccinations");
		
		
				// ///////////////////////////////////////////////////////////////////////////////////////////////
				// ///////////////////////////PREPARE OBJECTS FOR API CALLS//////////////////////////////////////////////
				// ///////////////////////////////////////////////////////////////////////////////////////////////
				
		IdMapper idMapper = new IdMapper();
		idMapper.setRoleId(sc.getUserService().getRole("child", false, null).getRoleId());
		idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService().saveIdMapper(idMapper).toString()));

		Identifier ident = new Identifier();
		ident.setIdentifier(childIdentifier);
		ident.setIdentifierType((IdentifierType) sc.getCustomQueryService().getDataByHQL("FROM IdentifierType WHERE name ='" + GlobalParams.IdentifierType.CHILD_PROJECT_ID + "'").get(0));
		ident.setPreferred(true);
		ident.setIdMapper(idMapper);
		sc.getCustomQueryService().save(ident);
				
				
				User submitter = new User();
				UserService userService = sc.getUserService();
				User user= new User(Integer.parseInt(creator));
				submitter.setMappedId(userId);

				Child ch = new Child();
				ch.setBirthdate( RestUtils.stringToDate(birthDate));
				if ("M".equalsIgnoreCase(gender) || "MALE".equalsIgnoreCase(gender))
				{
					ch.setGender(Gender.MALE);
				}
				else if ("F".equalsIgnoreCase(gender) || "FEMALE".equalsIgnoreCase(gender))
				{
					ch.setGender(Gender.FEMALE);
				}
				else
				{
					ch.setGender(Gender.UNKNOWN);
				}

				ch.setFirstName(firstName);
				ch.setLastName(lastName);
				ch.setMotherFirstName(motherFirstName);
				ch.setDateEnrolled(RestUtils.stringToDate(enrolledDate));
				ch.setLastEditedByUserId(user);
				ch.setLastEditedDate(RestUtils.stringToDate(lastEditedDate));
				ch.setCreatedByUserId(user);
				ch.setCreatedDate(RestUtils.stringToDate(createdDate));
				ch.setCreator(user);
				ch.setMappedId(idMapper.getMappedId());
				sc.getChildService().saveChild(ch);
				ChildService childService=sc.getChildService();
				childService.saveChild(ch);
				// ///////////Address fields ///////////////////////
				Address add = new Address();
				add.setAddress1(address1);
				add.setAddress2(address2);
				//add.setMappedId();
				add.setMappedId(idMapper.getMappedId());
			
				add.setAddressType(ContactType.PRIMARY);
				add.setCreator(user);
				add.setMappedId(ch.getMappedId());
				sc.getDemographicDetailsService().saveAddress(add);
		

				
				//setting contact informations
				ContactNumber contactMob = new ContactNumber();
				contactMob.setNumber(contactNumber1);
				contactMob.setTelelineType(ContactTeleLineType.MOBILE);
				contactMob.setNumberType(ContactType.PRIMARY);
				contactMob.setMappedId(ch.getMappedId());
				sc.getDemographicDetailsService().saveContactNumber(contactMob);
				sc.commitTransaction();
	
				
				return "";
	}
	
	public String addEvent(JSONArray array, ServiceContext sc ){
		
		StringBuilder string=new StringBuilder();
		for(int i=0;i<array.size();i++)
		{
			try {
			
				string.append(addEncounter((JSONObject)array.get(i), sc));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		return string.toString();
	}
	
	public String addEncounter(JSONObject json,ServiceContext sc){
		IdMapper mappId=sc.getIdMapperService().findIdMapper((String)json.get("patientId"));
		int patientId=mappId.getMappedId(); 
		Date encounterDate= new Date((String)json.get("encounterDate")); 
		String encounterType=(String)json.get("encounterType");  
		int locationId = (Integer)json.get("locationId");
		int userId =(Integer)json.get("userId");
		Encounter encounter = EncounterUtil.saveEncounter(patientId, userId,locationId, encounterDate,encounterDate,null,userId,EncounterType.valueOf(encounterType),DataEntrySource.MOBILE,sc);
		
		if(encounter==null)
	{
		return ""+patientId;
	}
	return "";
	}
	
	public String addVaccination(JSONObject json, ServiceContext sc ){
		//TODO add vaccinations 
		String vaccinationStatus =(String) json.get(RequestElements.VACCINATION_STATUS);
		String vaccinationDueDate =(String) json.get(RequestElements.VACCINATION_DUEDATE);
		String lastEditDate = (String) json.get(RequestElements.LAST_EDITED_DATE);
		String creator =(String) json.get(RequestElements.CREATOR);
		String createdDate = (String) json.get(RequestElements.CREATED_DATE);
		String lastEditor = (String) json.get(RequestElements.LASTEDITOR);
		String vaccinatorId = (String) json.get(RequestElements.VACCINATOR);
		String vaccinationDate=(String) json.get(RequestElements.VACCINATION_DATE);
		JSONObject vaccineObject=(JSONObject) json.get("vaccine");
		JSONObject centreObject=(JSONObject) json.get("centre");
		String centreId = (String) centreObject.get(RequestElements.VACCINATION_CENTER);
		String vaccineId =(String) vaccineObject.get(RequestElements.VACCINEID);
		JSONObject childObject=(JSONObject) json.get("child");
		String childIdentifier = (String) childObject.get("childId") ;
		
		//ServiceContext sc=Context.getServices();;
		IdMapper mappId=sc.getIdMapperService().findIdMapper(childIdentifier);
		User creatorUser=sc.getUserService().findUser(creator);
		User lastEditorUser=sc.getUserService().findUser(lastEditor);
		
		
		mappId.getMappedId();
		
		org.ird.unfepi.model.Vaccination v=new org.ird.unfepi.model.Vaccination();
		v.setChildId(mappId.getMappedId());
		//v.setCreatedByUserId(creatorUser);
		//v.setCreator(creatorUser);
		v.setLastEditedByUserId(lastEditorUser);
		v.setLastEditedDate(new Date(lastEditDate));
		v.setVaccinationCenterId(Integer.parseInt(centreId));
		v.setVaccinationDate(new Date(vaccinationDate));
		v.setVaccinationDuedate(new Date(vaccinationDueDate));
		v.setVaccinationStatus(org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.valueOf(vaccinationStatus));
		v.setVaccinatorId(Integer.valueOf(vaccinatorId));
		v.setVaccineId(Short.valueOf(vaccineId));
		sc.getVaccinationService().addVaccinationRecord(v);

		sc.commitTransaction();
		return "";
		
	}	
	public String addVaccinations(JSONArray array, ServiceContext sc){
		StringBuilder string=new StringBuilder();
		for(int i=0;i<array.size();i++)
		{
			try {
				string.append(addVaccination((JSONObject)array.get(i), sc));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		return string.toString();
		
	}
	
	public String addUpdates(JSONArray array , ServiceContext sc){
		StringBuilder string=new StringBuilder();
		for(int i=0;i<array.size();i++)
		{
			try {
				string.append(update((JSONObject)array.get(i), sc));
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		return string.toString();
		
	}


	private String update(JSONObject jsonObject, ServiceContext sc) {
		String childIdentifier=(String)jsonObject.get("childIdentifier");
		try {
		String village=(String)jsonObject.get("address1");
		String healthArea=(String)jsonObject.get("address2");

		String phoneNumber=(String)jsonObject.get("contactNumber1");
		
		IdMapper mappId=sc.getIdMapperService().findIdMapper(childIdentifier);
		Address add = new Address();
		add.setAddress1(village);
		add.setAddress2(healthArea);
		//add.setMappedId();
		add.setMappedId(mappId.getMappedId());
		sc.getDemographicDetailsService().updateAddress(add);
		//setting contact informations
		ContactNumber contactMob = new ContactNumber();
		contactMob.setNumber(phoneNumber);
		contactMob.setTelelineType(ContactTeleLineType.MOBILE);
		contactMob.setNumberType(ContactType.PRIMARY);
		contactMob.setMappedId(mappId.getMappedId());

			sc.getDemographicDetailsService().updateContactNumber(contactMob);
			sc.commitTransaction();
			return "";
		} catch (ChildDataInconsistencyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return childIdentifier;
	}

	
	
}
