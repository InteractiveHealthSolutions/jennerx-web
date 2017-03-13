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
	
	public List<HashMap> getAllChidrenVaccinations(long lastRecord,	long programId) {
		ServiceContext sc = Context.getServices();
		try {
			// TODO divide in 10thoussand chunks
			String query = "SELECT v.vaccinationRecordNum vId,v.vaccinationCenterId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "
					+ "v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier childidentifier,v.childId, v.roundId, "
					+ "v.reasonVaccineNotGiven  reason  , v.role role,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "
					+ "FROM vaccination  v  inner join child c on c.mappedId=v.childId "
					+ "inner join identifier i on v.childid=i.mappedid  AND  i.preferred join vaccine on v.vaccineId=vaccine.vaccineId  "
					+ " inner join round r on v.roundId =  r.roundId "
					+ " where v.voided=0 and v.vaccinationRecordNum>"
					+ lastRecord
					+ " and r.healthProgramId = "
					+ programId
					+ "  order by vId ASC limit 10000 ";
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			sc.closeSession();
		}

	}
	
	public List<HashMap> getallEncounters(long lastRecord, long programId){
		ServiceContext sc =Context.getServices();
		String query = "select i.identifier, e.p2id, e.encounterType, e.locationId, e.dateEncounterEntered, result.roundId from encounter e inner join identifier i on e.p1id=i.mappedId ";
		
		query += "LEFT JOIN (  "
				+"SELECT   "
				+"encounterId , p1id , p2id  ,  "
				+"group_concat(distinct(if(element like '%ROUND%' ,value,null)))'roundId'  ,  "
				+"group_concat(distinct(if(element like 'VISIT_DATE' ,value,null)))'visitdate'  ,  "
				+"group_concat(if(element like 'SITE_MAPPED_ID',value,null))'site'   "
				+"FROM encounterresults  "
				+"group by encounterId , p1id , p2id ) result USING ( encounterId , p1id , p2id )"
				+"where encounterType IN( 'ENROLLMENT' , 'FOLLOWUP') AND result.roundId IN (select roundId from round where healthProgramId = " + programId +")  "
				+"group by dateEncounterEntered "
				+"order by result.site, result.roundId, dateEncounterEntered ";
		
		query +="LIMIT " + lastRecord + ", 10000";
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
	
	public List<HashMap> getAllItemDistributed(long lastRecord){
		ServiceContext sc =Context.getServices();
		//TODO divide in 10thoussand chunks
		
		String query = "select i.identifier, it.distributedDate, it.mappedId, it.quantity, it.itemRecordNum from itemsdistributed it inner join identifier i on it.mappedId=i.mappedId LIMIT " + lastRecord + ", 10000";
		
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
	
	public List<HashMap> getAllMuacMeasurements(long lastRecord){
		ServiceContext sc =Context.getServices();
		//TODO divide in 10thoussand chunks
		
		String query = "select i.identifier, m.mappedId, m.measureDate, m.circumference, m.colorrange from muacmeasurement m inner join identifier i on m.mappedId=i.mappedId LIMIT " + lastRecord + ", 10000";

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
	
	public List<HashMap> getAllVialCounts(long lastRecord, long programId){
		ServiceContext sc =Context.getServices();
		//TODO divide in 10thoussand chunks
		
		String query = "SELECT * FROM vialcount WHERE roundId IN (SELECT roundId FROM round where healthProgramId = "
					   + programId +") LIMIT " + lastRecord + ", 10000";

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
	
	public  List<HashMap> getUpdatedVaccinations(String lastSyncedTime, long programId) {
		ServiceContext sc = Context.getServices();
		String query = "SELECT  i.identifier childidentifier, v.vaccinationCenterId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "
				+ "v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, v.roundId, "
				+ "v.vaccinatorId ,v.role role,v.reasonVaccineNotGiven  reason,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "
				+ "FROM vaccination  v  inner join child c on c.mappedId=v.childId "
				+ "inner join identifier i on v.childid=i.mappedid  AND  i.preferred join vaccine on v.vaccineId=vaccine.vaccineId   "
				+ " inner join round r on v.roundId =  r.roundId "
				+ "where v.voided=0 and v.lastEditedDate >='"
				+ lastSyncedTime+"'"
				+ " and r.healthProgramId = "
				+ programId
				+ " order by identifier ASC ;";
		
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
	
	public  List<HashMap> getNewEucounters(String lastSyncedTime, long programId) {
		ServiceContext sc = Context.getServices();
		
		String query = "select i.identifier, e.p2id, e.encounterType, e.locationId, e.dateEncounterEntered from encounter e inner join identifier i on e.p1id=i.mappedId ";
		
		query += "LEFT JOIN (  "
				+"SELECT   "
				+"encounterId , p1id , p2id  ,  "
				+"group_concat(distinct(if(element like '%ROUND%' ,value,null)))'roundId'  ,  "
				+"group_concat(distinct(if(element like 'VISIT_DATE' ,value,null)))'visitdate'  ,  "
				+"group_concat(if(element like 'SITE_MAPPED_ID',value,null))'site'   "
				+"FROM encounterresults  "
				+"group by encounterId , p1id , p2id ) result USING ( encounterId , p1id , p2id )"
				+"where encounterType IN( 'ENROLLMENT' , 'FOLLOWUP') AND result.roundId IN (select roundId from round where healthProgramId = " + programId +")  "
				
				+ "and e.dateEncounterEntered >='"+lastSyncedTime+"' "
				
				+"group by dateEncounterEntered "
				+"order by result.site, result.roundId, dateEncounterEntered ";
		
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
	
	public  List<HashMap> getNewItemsDistributed(String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		
		String query = "select i.identifier, it.distributedDate, it.mappedId, it.quantity, it.itemRecordNum from itemsdistributed it inner join identifier i on it.mappedId=i.mappedId "
				+ "where it.distributedDate >='"+lastSyncedTime+"' ;";
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
	
	public  List<HashMap> getNewMuacMeasurements(String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		
		String query = "select i.identifier, m.mappedId, m.measureDate, m.circumference, m.colorrange from muacmeasurement m inner join identifier i on m.mappedId=i.mappedId "
				+ "where m.measureDate >='"+lastSyncedTime+"' ;";
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
	
	public  List<HashMap> getNewVialcounts(String lastSyncedTime, long programId) {
		ServiceContext sc = Context.getServices();
		
		String query = "select* from vialcount where roundId in (select roundId from round where healthProgramId = "
					   + programId + ") and date >='"+lastSyncedTime+"' ;";
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
	
	public  List<HashMap> getCurrentVaccineStatus(String lastSyncedTime, long programId) {
		ServiceContext sc = Context.getServices();
		
		String query = "select* from vialcount where roundId in (select roundId from round where healthProgramId = "
					   + programId + ") and date >='"+lastSyncedTime+"' ;";
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
	
	public  List<HashMap> getNewVaccineStatus(String lastSyncedTime, long programId) {
		ServiceContext sc = Context.getServices();
		
//		String query = "select* from unfepi.vialcount where roundId in (select roundId from round where healthProgramId = "
//					   + programId + ") and date >='"+lastSyncedTime+"' ;";
		
		Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId = "+ programId).get(0);
		
		String query =   " SELECT v.vaccineId, name, issupplementary, vaccine_entity,  "
				+ " fullName, shortName, shortNameOther, standardOrder  "
				+ " , if(t1.status is null,1, t1.status) 'status' "
				+ " FROM vaccine v "
				+ " LEFT JOIN (SELECT vaccineId, roundId, status FROM roundvaccine  "
				+ " WHERE roundId IN (SELECT roundId FROM round where healthProgramId = "+ programId +" and isActive = true) ) as t1 "
				+ " ON t1.vaccineId = v.vaccineId "
				+ " where v.vaccineId in (SELECT distinct(vaccineId) FROM vaccinegap WHERE vaccinationcalendarId = "+calendarId+" )  "
				+ " and vaccine_entity like '%child%' ";
		
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
