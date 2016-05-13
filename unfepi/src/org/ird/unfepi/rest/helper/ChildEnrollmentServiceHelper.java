package org.ird.unfepi.rest.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import org.ird.unfepi.model.Device;
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
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;
import org.ird.unfepi.utils.EncounterUtil;
import org.ird.unfepi.web.utils.IMRUtils;
import org.joda.time.DateTimeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.org.apache.xerces.internal.util.Status;

public class ChildEnrollmentServiceHelper {
	public static int lastCount = 0;

	public static JSONArray addEnrollments(JSONArray jsonArray) {
		ServiceContext sc = Context.getServices();
		JSONArray mArray = new JSONArray();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = createEnrollment((JSONObject) jsonArray.get(i));
				if (obj != null) {
					mArray.add(obj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		return mArray;
	}

	public static JSONObject createEnrollment(JSONObject objectToParse)
			throws ChildDataInconsistencyException {

		ServiceContext sc = Context.getServices();
		String childIdentifier = "";
		try {
			childIdentifier = String.valueOf(objectToParse
					.get(RequestElements.CHILD_IDENTIFIER));
			String firstName = (String) objectToParse
					.get(RequestElements.FIRST_NAME);
			String lastName = (String) objectToParse
					.get(RequestElements.LAST_NAME);
			String gender = (String) objectToParse.get(RequestElements.GENDER);
			String birthDate = (String) objectToParse
					.get(RequestElements.BIRTH_DATE);
			String motherFirstName = (String) objectToParse
					.get(RequestElements.MOTHER_FIRST_NAME);
			String contactNumber1 = (String) objectToParse
					.get(RequestElements.CONTACT_NUMBER);
			String address1 = (String) objectToParse
					.get(RequestElements.ADDRESS1);
			String address2 = (String) objectToParse
					.get(RequestElements.ADDRESS2);
			int userId = Integer.valueOf(objectToParse
					.get(RequestElements.LG_USERID) != null ? objectToParse
					.get(RequestElements.LG_USERID).toString() : "00");
			String creator = (String) objectToParse
					.get(RequestElements.CREATOR);
			String lastEditor = (String) objectToParse
					.get(RequestElements.LASTEDITOR);

			String createdDate = (String) objectToParse
					.get(RequestElements.CREATED_DATE);
			String lastEditedDate = (String) objectToParse
					.get(RequestElements.LAST_EDITED_DATE);
			String enrolledDate = (String) objectToParse
					.get(RequestElements.ENROLLED_DATE);
			// ///////////////////////////////////////////////////////////////////////////////////////////////
			// ///////////////////////////PREPARE OBJECTS FOR API
			// CALLS//////////////////////////////////////////////
			// ///////////////////////////////////////////////////////////////////////////////////////////////

			IdMapper idMapper = new IdMapper();
			idMapper.setRoleId(sc.getUserService()
					.getRole("child", false, null).getRoleId());
			idMapper.setMappedId(Integer.parseInt(sc.getIdMapperService()
					.saveIdMapper(idMapper).toString()));

			Identifier ident = new Identifier();
			ident.setIdentifier(childIdentifier);
			ident.setIdentifierType((IdentifierType) sc
					.getCustomQueryService()
					.getDataByHQL(
							"FROM IdentifierType WHERE name ='"
									+ GlobalParams.IdentifierType.CHILD_PROJECT_ID
									+ "'").get(0));
			ident.setPreferred(true);
			ident.setIdMapper(idMapper);
			sc.getCustomQueryService().save(ident);

			// sc.commitTransaction();
			User submitter = new User();
			UserService userService = sc.getUserService();
			User user = userService.findUser(creator);

			// User user= new User(Integer.parseInt(creator));
			submitter.setMappedId(user.getMappedId());

			Child ch = new Child();
			ch.setBirthdate(RestUtils.stringToDate(birthDate));
			if ("M".equalsIgnoreCase(gender) || "MALE".equalsIgnoreCase(gender)) {
				ch.setGender(Gender.MALE);
			} else if ("F".equalsIgnoreCase(gender)
					|| "FEMALE".equalsIgnoreCase(gender)) {
				ch.setGender(Gender.FEMALE);
			} else {
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

			ChildService childService = sc.getChildService();
			childService.saveChild(ch);
			// ///////////Address fields ///////////////////////
			Address add = new Address();
			add.setAddress1(address1);
			add.setAddress2(address2);
			add.setMappedId(idMapper.getMappedId());

			add.setAddressType(ContactType.PRIMARY);
			add.setCreator(user);
			add.setMappedId(ch.getMappedId());
			sc.getDemographicDetailsService().saveAddress(add);

			// setting contact informations
			if ((contactNumber1 != null) &&  (!contactNumber1.isEmpty())) {
				ContactNumber contactMob = new ContactNumber();
				contactMob.setNumber(contactNumber1);
				contactMob.setTelelineType(ContactTeleLineType.MOBILE);
				contactMob.setNumberType(ContactType.PRIMARY);
				contactMob.setMappedId(ch.getMappedId());
				sc.getDemographicDetailsService().saveContactNumber(contactMob);
			}
			sc.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject errorJson = new JSONObject();
			errorJson.put("id", childIdentifier);
			errorJson.put("message", e.getMessage());
			return errorJson;
		} finally {
			sc.closeSession();
		}
		return null;
	}

	public JSONArray addEvent(JSONArray array) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			try {
				JSONObject obj = addEncounter((JSONObject) array.get(i));
				if (obj != null) {
					jsonArray.add(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	public JSONObject addEncounter(JSONObject json) {
		ServiceContext sc = Context.getServices();
		String childIdentifier = (String) json.get("patientId");
		try {
			IdMapper mappId = sc.getIdMapperService().findIdMapper(
					(String) json.get("patientId"));
			int patientId = mappId.getMappedId();
			Date encounterDate = RestUtils.stringToDate((String) json
					.get("encounterDate"));
			String encounterType = (String) json.get("encounterType");
			long location = (Long) json.get("locationId");
			int locationId = (int) location;
			long user = (Long) json.get("userId");		
			User userMapped = sc.getUserService()
					.findUser(String.valueOf(user));
			int userId = userMapped.getMappedId();
			EncounterType type = null;
			if (encounterType.equalsIgnoreCase("enrollment")) {
				type = EncounterType.ENROLLMENT;
			} else if (encounterType.equalsIgnoreCase("update")) {
				type = EncounterType.FOLLOWUP_ADMIN;
			} else if (encounterType.equalsIgnoreCase("followup")) {
				type = EncounterType.FOLLOWUP;
			}
			Encounter encounter = EncounterUtil.saveEncounter(patientId,
					userId, locationId, encounterDate, encounterDate, null,
					userId, type, DataEntrySource.MOBILE, sc);

			sc.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
			JSONObject errorJson = new JSONObject();
			errorJson.put("id", childIdentifier);
			errorJson.put("message", e.getMessage());
			return errorJson;
		} finally {
			sc.closeSession();
		}
		return null;
	}

	public JSONObject addVaccination(JSONObject json) {
		
		ServiceContext sc = Context.getServices();
		Long childIdentifier = null;
		try {
			// #TODO: do role based criteria
			Vaccination dbVaccination = null;
			
			String vaccinationStatus = (String) json
					.get(RequestElements.VACCINATION_VACCINATION_STATUS);
			String vaccinationDueDate = (String) json
					.get(RequestElements.VACCINATION_DUEDATE);
		
			String creator = (String) json.get(RequestElements.CREATOR);
			String createdDate = (String) json
					.get(RequestElements.CREATED_DATE);
			String lastEditDate = (String) json
					.get(RequestElements.LAST_EDITED_DATE);
			String lastEditor = (String) json.get(RequestElements.LASTEDITOR);
			String vaccinatorId = (String) json.get(RequestElements.VACCINATOR);
			String vaccinationDate = (String) json
					.get(RequestElements.VACCINATION_DATE);
			Long centreId = (Long) json.get(RequestElements.VACCINATION_CENTER);
			Long vaccineId = (Long) json.get(RequestElements.VACCINEID);
			String role = (String) json.get(RequestElements.USER_ROLE);
			childIdentifier = (Long) json.get(RequestElements.CHILD_IDENTIFIER);

			IdMapper mappId = sc.getIdMapperService().findIdMapper(
					childIdentifier.toString());
			Child child = sc.getChildService().findChildById(
					mappId.getMappedId(), true, new String[] {});
			User creatorUser = sc.getUserService().findUser(creator);
			User lastEditorUser = sc.getUserService().findUser(lastEditor);
			Date currentVaccinationDate = RestUtils.stringToDate(vaccinationDate);
			List<Vaccination> vaccinatedList = sc
					.getVaccinationService()
					.findByCriteria(
							mappId.getMappedId(),
							vaccineId.shortValue(),
							org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED,
							0, 1500, false, new String[] { "idMapper" });
			org.ird.unfepi.model.Vaccination currentVaccination = new org.ird.unfepi.model.Vaccination();
			// setting received data vaccine id
			currentVaccination.setVaccineId(Short.valueOf(vaccineId.toString()));
			//sc.beginTransaction();
			if (vaccinatedList != null) {
				if (vaccinatedList.size() > 0) {
					// #TODO: do role based criteria
				 dbVaccination = null;
					for (Vaccination va : vaccinatedList) {
						if (!va.isVoided()) {
							dbVaccination = va;
							break;
						}
					}
					short it = vaccinatedList.get(0).getVaccine()
							.getVaccineId();// getPrerequisites().iterator();
					List list = sc.getCustomQueryService()
							.getDataBySQLMapResult(
									"select * from vaccineprerequisite  where vaccinePrerequisiteId="
											+ it);
					HashMap map = (HashMap) list.get(0);

					if (map != null) {
						//sc.beginTransaction();
						// get and set next vaccine id
						short nextVaccineId = (Short) map.get("vaccineId");
						// System.out.println(map.get("vaccineId")+"----");
						Vaccine nextVaccine = sc.getVaccinationService()
								.findVaccineById(nextVaccineId);
						boolean birthGap = IMRUtils.validateBirthGap(
								child.getBirthdate(), currentVaccinationDate,
								nextVaccine.getVaccineId());
						boolean birthMax = IMRUtils.validateMaxBirthGap(
								nextVaccine.getVaccineId(), child.getBirthdate(),
								currentVaccinationDate);
						boolean preReqGap = IMRUtils.validatePreRequisiteGap(
								nextVaccine.getVaccineId(), child.getBirthdate(),
								vaccinatedList.get(0).getVaccinationDate(),
								currentVaccinationDate);
						if (birthGap && birthMax & preReqGap) {
							currentVaccination.setVaccineId(nextVaccineId);
						} else {
							if (dbVaccination != null) {
								if (role.equalsIgnoreCase("Entrance")) {
									
									boolean preReq = IMRUtils
											.validatePreRequisiteGap(
													nextVaccineId,
													child.getBirthdate(),
													currentVaccinationDate,
													dbVaccination.getVaccinationDate());
									if (preReq) {
										if(dbVaccination.getVaccinationDate().getTime()>currentVaccinationDate.getTime()) {
											dbVaccination.setVaccineId(nextVaccineId);
											dbVaccination.setDescription("vaccination updated!");
										} else if(dbVaccination.getVaccinationDate().getTime()<currentVaccinationDate.getTime()){
											currentVaccination.setVaccineId(nextVaccineId);
											currentVaccination.setDescription("vaccination updated!");
										}else {
											currentVaccination.setVoided(true);
											currentVaccination.setVoidedReason("Voided because same row already exists");
										}	
									} else {
										if(dbVaccination.getRole().equalsIgnoreCase("Entrance")) {
											currentVaccination.setVoided(true);
											currentVaccination.setVoidedReason("Voided because old data is also from Entrance Role.");
										} else {
											dbVaccination.setVoided(true);
											dbVaccination.setVoidedReason("Voided because new data is from Entrance Role .Entrance Entry is preferred.");
										}
									}
									sc.getCustomQueryService().update(dbVaccination);//TODO
								} else if (role.equalsIgnoreCase("Exit")) {
									boolean preReq = IMRUtils
											.validatePreRequisiteGap(
													nextVaccineId,
													child.getBirthdate(),
													currentVaccinationDate,
													dbVaccination.getVaccinationDate());
									if (preReq) {
										if(dbVaccination.getVaccinationDate().getTime()>currentVaccinationDate.getTime()) {
											dbVaccination.setVaccineId(nextVaccineId);
											dbVaccination.setDescription("vaccination updated!");
										} else if(dbVaccination.getVaccinationDate().getTime()<currentVaccinationDate.getTime()){
											currentVaccination.setVaccineId(nextVaccineId);
											currentVaccination.setDescription("vaccination updated!");
										}else {
											currentVaccination.setVoided(true);
											currentVaccination.setVoidedReason("Voided because same row already exists");
										}	
									} else {
										if(dbVaccination.getRole().equalsIgnoreCase("Entrance")) {
											currentVaccination.setVoided(true);
											currentVaccination.setVoidedReason("Voided because old data is from Entrance Role.");
										} else {
											dbVaccination.setVoided(true);
											dbVaccination.setVoidedReason("Voided because new data is from also from Exit.");
										}
									}
								}
								sc.getVaccinationService().updateVaccinationRecord(dbVaccination);
							}
						}
					}else {
						return null;
					}
				}
			}
			currentVaccination.setChildId(mappId.getMappedId());
			//added vaccinatorId
			currentVaccination.setVaccinatorId(creatorUser.getMappedId());
			// handling users
			currentVaccination.setCreatedByUserId(creatorUser);
			currentVaccination.setLastEditedByUserId(lastEditorUser);

			// handling modification date
			currentVaccination.setCreatedDate(RestUtils.stringToDate(createdDate));
			currentVaccination.setLastEditedDate(RestUtils.stringToDate(lastEditDate));

			currentVaccination.setVaccinationCenterId(Integer.parseInt(centreId.toString()));

			currentVaccination.setRole(role);
			currentVaccination.setVaccinationDate(currentVaccinationDate);
			currentVaccination.setVaccinationDuedate(currentVaccinationDate);

			// this is done because of limit possibilities in jennerX app
			org.ird.unfepi.model.Vaccination.VACCINATION_STATUS status = null;
			if (vaccinationStatus.equalsIgnoreCase("vaccinated")) {
				status = org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.VACCINATED;
			} else if(vaccinationStatus.equalsIgnoreCase("retro")){
				status=org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.RETRO;
			} else {
				status = org.ird.unfepi.model.Vaccination.VACCINATION_STATUS.NOT_VACCINATED;
			}
			currentVaccination.setVaccinationStatus(status);
			if (vaccinatorId != null) {
				currentVaccination.setVaccinatorId(Integer.valueOf(vaccinatorId));
			} else {
				currentVaccination.setVaccinatorId(null);
			}
			if(dbVaccination != null){
				dbVaccination.setDescription("updated desc!");
				sc.getVaccinationService().updateVaccinationRecord(dbVaccination);//TODO
			}
			sc.getVaccinationService().addVaccinationRecord(currentVaccination);
			sc.commitTransaction();
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject errorjson = new JSONObject();
			errorjson.put("id", childIdentifier);
			errorjson.put("message", e.getMessage() != null ? e.getMessage()
					: "");
			return errorjson;
			}finally{
				sc.closeSession();
			}
		return null;
	}

	public JSONArray addVaccinations(JSONArray array) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			try {
				JSONObject obj = addVaccination((JSONObject) array.get(i));
				if (obj != null)
					jsonArray.add(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	public JSONArray addUpdates(JSONArray array) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			try {
				JSONObject obj = update((JSONObject) array.get(i));
				if (obj != null)
					jsonArray.add(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	private JSONObject update(JSONObject jsonObject) {
		ServiceContext sc = Context.getServices();
		Long childIdentifier = (Long) jsonObject.get("childIdentifier");
		try {
			String village = (String) jsonObject.get("address1");
			String healthArea = (String) jsonObject.get("address2");

			String phoneNumber = (String) jsonObject.get("contactNumber1");
			String lastEditDate = (String) jsonObject
					.get(RequestElements.LAST_EDITED_DATE);
			String lastEditor = (String) jsonObject.get(RequestElements.LASTEDITOR);
			UserService userService = sc.getUserService();
			User user = userService.findUser(lastEditor);
			IdMapper mappId = sc.getIdMapperService().findIdMapper(
					String.valueOf(childIdentifier));

			Address add = sc
					.getDemographicDetailsService()
					.getAddress(mappId.getMappedId(), true,
							new String[] { "idMapper" }).get(0);

			add.setAddress1(village);
			add.setAddress2(healthArea);
			add.setLastEditedByUserId(user);
			add.setLastEditedDate(RestUtils.stringToDate(lastEditDate));
			sc.getDemographicDetailsService().updateAddress(add);
			// setting contact informations
			if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
				ContactNumber contactMob = sc.getDemographicDetailsService()
						.getContactNumberById(Integer.parseInt(phoneNumber),
								true, new String[] { "idMapper" });
				contactMob.setNumber(phoneNumber);
				contactMob.setTelelineType(ContactTeleLineType.MOBILE);
				contactMob.setNumberType(ContactType.PRIMARY);
				sc.getDemographicDetailsService().updateContactNumber(
						contactMob);
				contactMob.setLastEditedByUserId(user);
				contactMob.setLastEditedDate(RestUtils.stringToDate(lastEditDate));
			
			}
			sc.commitTransaction();

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("id", childIdentifier);
			json.put("message", e.getMessage());
			return json;
		} finally {
			sc.closeSession();
		}
		return null;
	}

}
