package org.ird.unfepi.rest.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import org.ird.unfepi.model.Vaccination;
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

import com.sun.org.apache.xerces.internal.util.Status;

public class ChildEnrollmentServiceHelper {

	
	public static String addEnrollments(JSONArray jsonArray){
		
		StringBuilder string=new StringBuilder();
		string.append("\"Enrollment\" :[ ");
		
		for(int i=0;i<jsonArray.size();i++)
		{
			try {
				string.append(createEnrollment((JSONObject)jsonArray.get(i)));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		string.setCharAt(string.length()-1,' '  );
		string.append("],");
		return string.toString();
	}
	
	
	public static String createEnrollment(JSONObject objectToParse) throws ChildDataInconsistencyException
	{
		
		ServiceContext sc=Context.getServices();
		String childIdentifier="";
		try{
		
			sc.beginTransaction();
		//ServiceContext sc = Context.getServices();
		childIdentifier = String.valueOf(objectToParse
				.get(RequestElements.CHILD_IDENTIFIER));
		String firstName = (String) objectToParse
				.get(RequestElements.FIRST_NAME);
		String lastName = (String) objectToParse.get(RequestElements.LAST_NAME);
		String gender = (String) objectToParse.get(RequestElements.GENDER);
		String birthDate = (String) objectToParse
				.get(RequestElements.BIRTH_DATE);
		String motherFirstName = (String) objectToParse
				.get(RequestElements.MOTHER_FIRST_NAME);
		String contactNumber1 = (String) objectToParse
				.get(RequestElements.CONTACT_NUMBER);
		String address1 = (String) objectToParse.get(RequestElements.ADDRESS1);
		String address2 = (String) objectToParse.get(RequestElements.ADDRESS2);
		int userId = Integer.valueOf(objectToParse
				.get(RequestElements.LG_USERID) != null ? objectToParse.get(
				RequestElements.LG_USERID).toString() : "00");
		String creator = (String) objectToParse.get(RequestElements.CREATOR);
		String lastEditor = (String) objectToParse
				.get(RequestElements.LASTEDITOR);

		String createdDate = (String) objectToParse
				.get(RequestElements.CREATED_DATE);
		String lastEditedDate = (String) objectToParse
				.get(RequestElements.LAST_EDITED_DATE);
		String enrolledDate = (String) objectToParse
				.get(RequestElements.ENROLLED_DATE);
		
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
				User user=userService.findUser(creator);
				
				//User user= new User(Integer.parseInt(creator));
				submitter.setMappedId(user.getMappedId());

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
				ch.setStatus(STATUS.FOLLOW_UP);
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
				if(contactNumber1!=null){
				ContactNumber contactMob = new ContactNumber();
				contactMob.setNumber(contactNumber1);
				contactMob.setTelelineType(ContactTeleLineType.MOBILE);
				contactMob.setNumberType(ContactType.PRIMARY);
				contactMob.setMappedId(ch.getMappedId());
				sc.getDemographicDetailsService().saveContactNumber(contactMob);
				}
				sc.commitTransaction();
		}catch(Exception e)
		{
			e.printStackTrace();
			return "{ \"id\":" +childIdentifier+", \"message\":\""+e.getMessage().replace("\""," ")+"\"},";		}finally{
			sc.closeSession();
		}
				
				return "";
	}
	
	public String addEvent(JSONArray array ){
		
		StringBuilder string=new StringBuilder();
		string.append("\"Event\" :[ ");
		for(int i=0;i<array.size();i++)
		{
			try {
			
				string.append(addEncounter((JSONObject)array.get(i)));
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		string.setCharAt(string.length()-1,' '  );
		string.append("],");
		return string.toString();
	}
	
	public String addEncounter(JSONObject json){
		ServiceContext sc=Context.getServices();
		
		String childIdentifier=(String)json.get("patientId");
		try{
		//	sc.beginTransaction();
		IdMapper mappId=sc.getIdMapperService().findIdMapper((String)json.get("patientId"));
		int patientId=mappId.getMappedId(); 
		Date encounterDate= RestUtils.stringToDate((String)json.get("encounterDate")); 
		String encounterType=(String)json.get("encounterType");  
		long location = (Long)json.get("locationId");
		int locationId =(int) location;
		long user=(Long)json.get("userId");
		User userMapped=sc.getUserService().findUser(String.valueOf(user));
		int userId =userMapped.getMappedId();
		EncounterType type=null;
		if(encounterType.equalsIgnoreCase("enrollment"))
		{
			type=EncounterType.ENROLLMENT;
		}else if(encounterType.equalsIgnoreCase("update")){
			type=EncounterType.FOLLOWUP_ADMIN;
		}else if(encounterType.equalsIgnoreCase("followup")){
			type=EncounterType.FOLLOWUP;
		}
		Encounter encounter = EncounterUtil.saveEncounter(patientId, userId,locationId, encounterDate,encounterDate,null,userId,type,DataEntrySource.MOBILE,sc);
	
		
		}catch(Exception e){
			e.printStackTrace();
			sc.rollbackTransaction();
			return "{ \"id\":" +childIdentifier+", \"message\":\""+e.getMessage().replace("\""," ")+"\"},";		}finally{
			sc.closeSession();
		}
		
	return "";
	}
	
	public String addVaccination(JSONObject json ){
		ServiceContext sc=Context.getServices();
		Long childIdentifier = null ;
		try{
		
			
			
		//TODO add vaccinations 
		String vaccinationStatus =(String) json.get(RequestElements.VACCINATION_STATUS);
		String vaccinationDueDate =(String) json.get(RequestElements.VACCINATION_DUEDATE);
		String lastEditDate = (String) json.get(RequestElements.LAST_EDITED_DATE);
		String creator =(String) json.get(RequestElements.CREATOR);
		String createdDate = (String) json.get(RequestElements.CREATED_DATE);
		String lastEditor = (String) json.get(RequestElements.LASTEDITOR);
		String vaccinatorId = (String) json.get(RequestElements.VACCINATOR);
		String vaccinationDate=(String) json.get(RequestElements.VACCINATION_DATE);
	//	JSONObject vaccineObject=(JSONObject) json.get("vaccine");
		//JSONObject centreObject=(JSONObject) json.get("centre");
		Long centreId = (Long) json.get(RequestElements.VACCINATION_CENTER);
		Long vaccineId =(Long) json.get(RequestElements.VACCINEID);
		//JSONObject childObject=(JSONObject) json.get("child");
		childIdentifier = (Long) json.get(RequestElements.CHILD_IDENTIFIER) ;
		
		//ServiceContext sc=Context.getServices();;
		IdMapper mappId=sc.getIdMapperService().findIdMapper(childIdentifier.toString());
		User creatorUser=sc.getUserService().findUser(creator);
		User lastEditorUser=sc.getUserService().findUser(lastEditor);
		
		//org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED;
		List<Vaccination> vaccinatedList = sc.getVaccinationService().findByCriteria(mappId.getMappedId(), vaccineId.shortValue(),org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED, 1, 15, true,  new String[] {"idMapper"});
	
		
		if(vaccinatedList.size()!=0){
			return "";
		}
		//mappId.getMappedId();
		sc.beginTransaction();
		org.ird.unfepi.model.Vaccination v=new org.ird.unfepi.model.Vaccination();
		v.setChildId(mappId.getMappedId());
		//v.setCreatedByUserId(creatorUser);
		//v.setCreator(creatorUser);
		//SimpleDateFormat sdf=new SimpleDateFormat(RestUtils.);
		
		
		v.setLastEditedByUserId(lastEditorUser);
		
		v.setLastEditedDate(RestUtils.stringToDate(lastEditDate));
		v.setVaccinationCenterId(Integer.parseInt(centreId.toString()));
		v.setVaccinationDate(RestUtils.stringToDate(vaccinationDate));
		v.setVaccinationDuedate(RestUtils.stringToDate(vaccinationDate));
		
		//this is done because of limit possibilities in jennerX app
		org.ird.unfepi.model.Vaccination.VACCINATION_STATUS status=null;
		if(vaccinationStatus.equalsIgnoreCase("vaccinated")){
			status =org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED;
		}else{
			status=org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.NOT_VACCINATED;
		}
		
		v.setVaccinationStatus(status);
		if(vaccinatorId!=null)
		{
		v.setVaccinatorId(Integer.valueOf(vaccinatorId));
		}else {
			v.setVaccinatorId(null);
			
		}
		v.setVaccineId(Short.valueOf(vaccineId.toString()));
		sc.getVaccinationService().addVaccinationRecord(v);

		sc.commitTransaction();
		}catch(Exception e){
			e.printStackTrace();
			sc.rollbackTransaction();
			return "{ \"id\":" +childIdentifier+", \"message\":\""+e.getMessage().replace("\""," ")+"\"},";		}finally{
			sc.closeSession();
		}
		
		return "";
		
	}	
	public String addVaccinations(JSONArray array){
		StringBuilder string=new StringBuilder();
		string.append("\"Vaccination\" :[ ");
		
		for(int i=0;i<array.size();i++)
		{
			try {
				string.append(addVaccination((JSONObject)array.get(i)));
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		string.setCharAt(string.length()-1,' '  );
		string.append("],");
		return string.toString();
		
	}
	
	public String addUpdates(JSONArray array){
		StringBuilder string=new StringBuilder();
		string.append("\"Update\" :[ ");
		
		for(int i=0;i<array.size();i++)
		{
			try {
				string.append(update((JSONObject)array.get(i)));
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		string.setCharAt(string.length()-1,' '  );
		string.append("]");
		
		return string.toString();
		
	}


	private String update(JSONObject jsonObject) {
		ServiceContext sc=Context.getServices();
		Long childIdentifier=(Long)jsonObject.get("childIdentifier");
		try {
			sc.beginTransaction();
		String village=(String)jsonObject.get("address1");
		String healthArea=(String)jsonObject.get("address2");

		String phoneNumber=(String)jsonObject.get("contactNumber1");
		
		IdMapper mappId=sc.getIdMapperService().findIdMapper(String.valueOf(childIdentifier));
		
		Address add=sc.getDemographicDetailsService().getAddress(mappId.getMappedId(), true, new String[] { "idMapper" }).get(0);
		 
		add.setAddress1(village);
		add.setAddress2(healthArea);
		sc.getDemographicDetailsService().updateAddress(add);
		//setting contact informations
		if(phoneNumber!=null){
		ContactNumber contactMob =sc.getDemographicDetailsService().getContactNumberById(Integer.parseInt(phoneNumber), true, new String[] { "idMapper" });
		contactMob.setNumber(phoneNumber);
		contactMob.setTelelineType(ContactTeleLineType.MOBILE);
		contactMob.setNumberType(ContactType.PRIMARY);
		sc.getDemographicDetailsService().updateContactNumber(contactMob);
		}
			sc.commitTransaction();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sc.rollbackTransaction();
			return "{ \"id\":" +childIdentifier+", \"message\":\""+e.getMessage().replace("\""," ")+"\"},";
		}finally{
			sc.closeSession();
		}
		
		return "";
	}

	
	
}
