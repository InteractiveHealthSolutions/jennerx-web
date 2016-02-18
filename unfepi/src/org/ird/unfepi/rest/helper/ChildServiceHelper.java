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

	private String childId ;
	private String childIdentifier;
	private String firstName ;
	private String lastName;
	private String fatherFirstName ;
	private String fatherLastName;
	private String motherFirstName ;
	private String motherLastName;
	private String birthDate;
	private String status;
	
	private String gender;
	private String contactNumber1;
	private String contactNumber2;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String address5;
	private String address6;
	private String terminationReason;
	private String terminationDate;
	private String createdDate;
	private String lastEditDate;
	private String creator;
	private String lastEditor;
	
	
	public static List<HashMap> getAllChildren(){
		
		ServiceContext sc =Context.getServices();
		String query="Select distinct identifier.identifier as childIdentifier , child.birthdate,child.createdDate,child.createdByUserId,child.firstName,child.lastName, child.motherFirstName "+
				",child.gender, child.lastEditedByUserId,child.lastEditedDate,child.status, child.terminationDate, child.terminationReason,"+
				" contactnumber.number , address.address1 "+
				"from child inner join identifier on child.mappedId=identifier.mappedId  inner join contactnumber on "+ 
				"child.mappedId=contactnumber.mappedId inner join address on child.mappedId=address.mappedId  group by childIdentifier ASC limit 999;";
		
		try{
				List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
					return map;
				//return buildJson("childIdentifier",map);
		}catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			sc.closeSession();
		}
		return null; 
	}
	
	public static List<HashMap> getAllChidrenVaccinations(){
		ServiceContext sc =Context.getServices();
		String query="SELECT vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "+
				"v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus, i.identifier childidentifier,v.childId, "+
				"v.vaccinatorId ,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "+
				"FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "+ 
				"inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "+ 
				"inner join vaccinationcenter vc on vc.mappedid=v.vaccinationcenterid  order by identifier ASC limit 999;";

		
		
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
	
	public static List<HashMap> getUpdatedChildren(
			String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		String query = "Select  distinct "
				+ "i.identifier as childIdentifier ,c.birthdate,c.createdDate,c.createdByUserId,c.firstName,c.lastName, c.motherFirstName, "
				+ "c.gender, c.lastEditedByUserId,c.lastEditedDate,c.status, c.terminationDate, c.terminationReason, "
				+ "cn.number , a.address1	from child c inner join identifier i on c.mappedId=i.mappedId  inner join contactnumber cn on  "
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
	
	public static List<HashMap> getUpdatedVaccinations(String lastSyncedTime) {
		ServiceContext sc = Context.getServices();
		String query = "SELECT  i.identifier identifier,v.childId, vc.mappedId centreid,v.vaccineId, v.lastEditedDate ,v.createdDate, "
				+ "v.vaccinationDate,v.vaccinationDuedate,v.vaccinationStatus,  "
				+ "v.vaccinatorId ,v.epiNumber,v.createdByUserId creator, v.lastEditedByUserId lastEditor "
				+ "FROM unfepi.vaccination  v inner join child c on c.mappedId=v.childId "
				+ "inner join identifier i on v.childid=i.mappedid inner join vaccine on v.vaccineId=vaccine.vaccineId "
				+ "inner join vaccinationcenter vc on vc.mappedid=v.vaccinationcenterid where v.lastEditedDate >='"
				+ lastSyncedTime + "' order by identifier ASC limit 999;";
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
	
	
	
	public static JSONObject buildJson(String tagToSortFor , List<HashMap> listMap){
		JSONObject returnData = new JSONObject();		
		HashSet set=new HashSet<String>();
		for (int i = 0; i < listMap.size(); i++) {
			Map map=listMap.get(i);
			set.add( map.get(tagToSortFor));
			
		}
		java.util.Iterator it =set.iterator();
		while(it.hasNext()){
			String s=(String)it.next();
			JSONArray TagtoSortObject=new JSONArray();
			for(Map m : listMap){
			
			if(((String)m.get(tagToSortFor)).equalsIgnoreCase(s))
			{
				TagtoSortObject.add(m);
			//metadata.put(key, value)	
			}
		}
			returnData.put(s,TagtoSortObject);
		}
		return returnData;
	}
	
}