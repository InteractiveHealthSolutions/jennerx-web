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

	
	public List<HashMap> getAllChildren(){
		
		ServiceContext sc =Context.getServices();
		String query="Select distinct i.identifier as childIdentifier , c.birthdate,c.createdDate,c.createdByUserId creator,c.firstName,c.lastName, c.motherFirstName "+
				",c.gender, c.lastEditedByUserId lastEditor,c.lastEditedDate,c.status, c.terminationDate, c.terminationReason,"+
				" cn.number contactnumber1, a.address1 ,  a.address2,c.dateEnrolled "+
				"from child c inner join identifier i on c.mappedId=i.mappedId  inner join contactnumber cn  on "+ 
				"c.mappedId=cn.mappedId inner join address a on c.mappedId=a.mappedId  group by childIdentifier ASC limit 999;";
		
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
	
	public  List<HashMap> getAllChidrenVaccinations(){
		ServiceContext sc =Context.getServices();
		String query="SELECT vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "+
				"v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier childidentifier,v.childId, "+
				"v.reasonVaccineNotGiven  reason  ,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor, "+
				"FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "+ 
				"inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "+ 
				" order by identifier ASC limit 99;";

		
		
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
				+ "c.gender, c.lastEditedByUserId lastEditor,c.lastEditedDate,c.status, c.terminationDate, c.terminationReason, "
				+ "cn.number , a.address1 , a.address2,c.dateEnrolled 	from child c inner join identifier i on c.mappedId=i.mappedId  inner join contactnumber cn on  "
				+ "c.mappedId=cn.mappedId inner join address a on c.mappedId=a.mappedId  "
				+ "where  c.lastEditedDate>='"+lastSyncedTime+"' group by childIdentifier ASC limit 999;";
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
		String query = "SELECT  i.identifier identifier,v.childId, vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "
				+ "v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus,  "
				+ "v.vaccinatorId ,v.reasonVaccineNotGiven  reason,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "
				+ "FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "
				+ "inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "
				+ "where v.lastEditedDate >='"
				+ lastSyncedTime+"' order by identifier ASC limit 999;";
		try {
			List<HashMap> map = sc.getCustomQueryService()
					.getDataBySQLMapResult(query);
			
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		return null;
	}
	
	
	

	
}
