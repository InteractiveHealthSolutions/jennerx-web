package org.ird.unfepi.rest.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.service.ChildService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChildServiceHelper {

	
	public List<HashMap> getAllChildren(long lastRecord){
		ServiceContext sc =Context.getServices();
		//TODO divide in 10thoussand chunks
		String query="Select  c.mappedId, i.identifier as childIdentifier , c.birthdate,c.createdDate,c.createdByUserId creator,c.firstName,c.lastName, c.motherFirstName "+
				",c.gender, c.lastEditedByUserId lastEditor,c.lastEditedDate,c.status, c.terminationDate, c.terminationReason,"+
				" cn.number contactnumber1, a.address1 ,  a.address2,c.dateEnrolled "+
				"from child c inner join identifier i on c.mappedId=i.mappedId  AND i.preferred  left join contactnumber cn on  "+ 
				"c.mappedId=cn.mappedId AND cn.numberType='PRIMARY' left join address a on c.mappedId=a.mappedId  where c.mappedId>"+lastRecord+"  order by c.mappedId ASC limit 10000;";
			try{
				List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
					return map;
			}catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			sc.closeSession();
		}
		return null; 
	}
	
	public List<HashMap> getAllChidrenVaccinations(long lastRecord){
		ServiceContext sc =Context.getServices();
		//TODO divide in 10thoussand chunks
		String query="SELECT v.vaccinationRecordNum vId,v.vaccinationCenterId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "+
				"v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier childidentifier,v.childId, "+
				"v.reasonVaccineNotGiven  reason  , v.role role,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "+
				"FROM unfepi.vaccination  v  inner join child c on c.mappedId=v.childId "+ 
				"inner join identifier i on v.childid=i.mappedid  AND  i.preferred join vaccine on v.vaccineId=vaccine.vaccineId  "+ 
				" where v.voided=0 and v.vaccinationRecordNum>"+lastRecord+"  order by vId ASC limit 10000 ";
		try{
				List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
		
		return map;
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			sc.closeSession();
		}
		return null;
	}
	
	public List<HashMap> getallEncounters(long lastRecord){
		ServiceContext sc =Context.getServices();
		String query = "select encounterId, p1id, encounterType, locationId, dataEntrySource from encounter LIMIT " + lastRecord + ", 10000";
		try{
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			return map;
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			sc.closeSession();
		}
		return null;
	}
	
	public  List<HashMap> getUpdatedChildren(
			String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		String query = "Select  distinct "
				+ "i.identifier as childIdentifier ,c.birthdate,c.createdDate,c.createdByUserId creator,c.firstName,c.lastName, c.motherFirstName, "
				+ "c.gender, c.lastEditedByUserId lastEditor ,c.lastEditedDate,c.status, c.terminationDate, c.terminationReason, "
				+ "cn.number contactnumber1 , a.address1 , a.address2,c.dateEnrolled "
				+ "from child c inner join identifier i on c.mappedId=i.mappedId  AND i.preferred  left join contactnumber cn on  "
				+"c.mappedId=cn.mappedId AND cn.numberType='PRIMARY' left join address a on c.mappedId=a.mappedId "
				+ "where  c.lastEditedDate>='"+lastSyncedTime+"' ;";
		try {
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		return null;
	}
	
	public  List<HashMap> getUpdatedVaccinations(String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		String query = "SELECT  i.identifier childidentifier, v.vaccinationCenterId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "
				+ "v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus,  "
				+ "v.vaccinatorId ,v.role role,v.reasonVaccineNotGiven  reason,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "
				+ "FROM unfepi.vaccination  v  inner join child c on c.mappedId=v.childId "
				+ "inner join identifier i on v.childid=i.mappedid  AND  i.preferred join vaccine on v.vaccineId=vaccine.vaccineId   "
				+ "where v.voided=0 and v.lastEditedDate >='"
				+ lastSyncedTime+"' order by identifier ASC ;";
		try {
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		return null;
	}
	
	public  List<HashMap> getNewEucounters(String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		
		String query = "select encounterId, p1id, encounterType, locationId, dataEntrySource from encounter "
				+ "where  dateEncounterEntered >='"+lastSyncedTime+"' ;";
		try {
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		return null;
	}
	
	

	
}
