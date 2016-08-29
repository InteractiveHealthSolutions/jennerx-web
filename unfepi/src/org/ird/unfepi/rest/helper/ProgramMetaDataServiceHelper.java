package org.ird.unfepi.rest.helper;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.json.JSONArray;
import org.json.JSONObject;


public class ProgramMetaDataServiceHelper {
	
	public static String getVaccineMetadata(JSONObject jsonObject){

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		try {
			JSONArray vaccinesId = jsonObject.optJSONArray(RequestElements.METADATA_VACCINE+RequestElements.METADATA_IDS);

			String lastEditDateStr = jsonObject.optString(RequestElements.LAST_SYNC_TIME);
			Date lastEditDate = (!lastEditDateStr.trim().isEmpty()) ? WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.parse(lastEditDateStr) : null ;
			
			if(jsonObject.isNull("calendarId")){
				throw new Exception("calendarId is not provided or null") ;
			}
			
			fillVaccine(lastEditDate, vaccinesId, response);
			fillVaccineGap(jsonObject, response);
			fillVaccineGapType(jsonObject, response);
			fillVaccinePrerequisite(jsonObject, response);

			HashMap<String, Object> resp = new HashMap<String, Object>();
			resp.put("METADATA", response);

			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, resp);

		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.equals(e);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("error", "Error in getting metadata");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, map);
		}
	}
	
	public static String getHealthProgramMetadata(JSONObject jsonObject){
		
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		
		try {
			
			fillHealthProgram(jsonObject, response);
			
			HashMap<String, Object> resp = new HashMap<String, Object>();
			resp.put("METADATA", response);

			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, resp);
			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.equals(e);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("error", "Error in getting metadata");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, map);
		}
	}

	private static void fillVaccine(Date lastEditDate, JSONArray ids, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINE_ID,
				RequestElements.METADATA_FIELD_VACCINE_NAME,
				RequestElements.METADATA_FIELD_VACCINE_ISSUPPLEMENTARY,
				RequestElements.METADATA_FIELD_VACCINE_ENTITY,
				RequestElements.METADATA_FIELD_VACCINE_FULL_NAME };
		String table = "vaccine";
		try {
			if(lastEditDate != null && ids != null){
				fetchMetaData(RequestElements.METADATA_VACCINE, columns, table,  WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.format(lastEditDate), response);
				findDeletedIds(RequestElements.METADATA_VACCINE, columns, table, ids, RequestElements.METADATA_FIELD_VACCINE_ID, response);
			}
			else if(lastEditDate == null && ids == null){
				fetchMetaData(RequestElements.METADATA_VACCINE, columns, table, response);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void fillVaccineGap(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINEGAP_VACCINEGAPTYPEID,	
				RequestElements.METADATA_FIELD_VACCINE_ID,
				RequestElements.METADATA_FIELD_VACCINEGAP_GAPTIMEUNIT,
				RequestElements.METADATA_FIELD_VACCINEGAP_VALUE,
				RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID};
		String table = "vaccinegap";

		Integer calendarId = json.optInt("calendarId");
		
		String query = "SELECT " + Arrays.toString(columns).replaceAll("\\[|\\]", "") + " FROM " + table
				+ " WHERE " + RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID + " = " + calendarId;
		
		if(json.has(RequestElements.METADATA_VACCINEGAP)){
			fetchAndCompareMetaData(RequestElements.METADATA_VACCINEGAP, columns, table, query, response, json);
		}
		else{
			fetchMetaDataByCustomQuery(RequestElements.METADATA_VACCINEGAP, query, columns, response);
		}
	}

	private static void fillVaccineGapType(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINEGAP_VACCINEGAPTYPEID,
				RequestElements.METADATA_FIELD_VACCINEGAPTYPE_NAME,
				RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID};
		String table = "vaccinegaptype";
		
		Integer calendarId = json.optInt("calendarId");
		
		String query = "SELECT " + Arrays.toString(columns).replaceAll("\\[|\\]", "") + " FROM " + table
				+ " WHERE " + RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID + " = " + calendarId;
		
		if(json.has(RequestElements.METADATA_VACCINEGAPTYPE)){
			fetchAndCompareMetaData(RequestElements.METADATA_VACCINEGAPTYPE, columns, table, query, response, json);
		}
		else{
			fetchMetaDataByCustomQuery(RequestElements.METADATA_VACCINEGAPTYPE, query, columns, response);
		}
	}

	private static void fillVaccinePrerequisite(JSONObject json, org.json.simple.JSONObject response) {

		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINE_ID,
				RequestElements.METADATA_FIELD_VACCINEPREREQUISITE_ID,
				RequestElements.METADATA_FIELD_VACCINEPREREQUISITE_MANDATORY,
				RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID};
		String table = "vaccineprerequisite";
		
		Integer calendarId = json.optInt("calendarId");
		
		String query = "SELECT " + Arrays.toString(columns).replaceAll("\\[|\\]", "") + " FROM " + table
				+ " WHERE " + RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID + " = " + calendarId;
		
		if(json.has(RequestElements.METADATA_VACCINEPREREQUISITE)){
			fetchAndCompareMetaData(RequestElements.METADATA_VACCINEPREREQUISITE, columns, table, query, response, json);
		}
		else{
			fetchMetaDataByCustomQuery(RequestElements.METADATA_VACCINEPREREQUISITE, query, columns, response);
		}
	}
	
	private static void fillHealthProgram(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] { 
				RequestElements.METADATA_FIELD_HEALTHPROGRAM_ID,
				RequestElements.METADATA_FIELD_HEALTHPROGRAM_NAME,
				RequestElements.METADATA_FIELD_VACCINATIONCALENDAR_ID};
		String table = "healthprogram";
		
//		Integer calendarId = json.optInt("calendarId");
		
		String query = "SELECT " + Arrays.toString(columns).replaceAll("\\[|\\]", "") + " FROM " + table;
		
		if(json.has(RequestElements.METADATA_HEALTHPROGRAM)){
			fetchAndCompareMetaData(RequestElements.METADATA_HEALTHPROGRAM, columns, table, query, response, json);
		}
		else{
			fetchMetaDataByCustomQuery(RequestElements.METADATA_HEALTHPROGRAM, query, columns, response);
		}

//		fetchAndCompareMetaData(RequestElements.METADATA_HEALTHPROGRAM, columns, table, response, json);
	}
	
	private static void fetchMetaDataByCustomQuery(String dataType , String query, String columns[], org.json.simple.JSONObject container){

		try
		{
			if (container == null)
				container = new org.json.simple.JSONObject();

			ServiceContext sc = Context.getServices();
			List results = sc.getCustomQueryService().getDataBySQL(query);
			ResponseBuilder.buildMetadataResponse(container, dataType, columns, results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	private static void fetchMetaData(String dataType, String[] columns, String table, org.json.simple.JSONObject container)
	{
		ServiceContext sc = Context.getServices();
		try
		{
			if (container == null)
				container = new org.json.simple.JSONObject();
			String query = CustomQueryBuilder.query(columns, table);
		
			List results = sc.getCustomQueryService().getDataBySQL(query);
			ResponseBuilder.buildMetadataResponse(container, dataType, columns, results);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{sc.closeSession();}
	}
	
	private static void fetchMetaData(String dataType, String[] columns, String table, String date, org.json.simple.JSONObject container)
	{
		ServiceContext sc = Context.getServices();
		try
		{
			if (container == null)
				container = new org.json.simple.JSONObject();

			String query = CustomQueryBuilder.queryCreatedUpdated(columns, table, date);

			List results = sc.getCustomQueryService().getDataBySQL(query);
			ResponseBuilder.buildMetadataResponse(container, dataType, columns, results);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{sc.closeSession();}
	}

	private static void findDeletedIds(String dataType, String[] columns, String table, JSONArray ids, String column, org.json.simple.JSONObject container)
	{
		ServiceContext sc = Context.getServices();
		try
		{
			if (container == null)
				container = new org.json.simple.JSONObject();

			String query = CustomQueryBuilder.queryWhereIn(table, ids.toString().replaceAll("\\[|\\]", ""), column);
			List results = sc.getCustomQueryService().getDataBySQL(query);

			for (Object obj : results){
				int index = -1;				
				for(int i = 0 ;  i < ids.length() ; i++) {
					if(ids.getString(i).equals(obj.toString())){
						index = i;
						break;
					}
				}
				if(index != -1){
					ids.remove(index);
				}
			}

			container.put(dataType+"_deleted", ids);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{sc.closeSession();}
	}

	private static void fetchAndCompareMetaData(String dataType, String[] _columns, String _table, String query, org.json.simple.JSONObject response, JSONObject requestjson){

		ServiceContext sc = Context.getServices();

		try {
//			String query = CustomQueryBuilder.query(_columns, _table);
			List<Object> records = sc.getCustomQueryService().getDataBySQL(query);
			
			if(records == null || records.isEmpty()){
				throw new Exception("no record found -> \'" + query + "\'");
			}

			JSONObject table = requestjson.getJSONObject(dataType);
			JSONArray cloumns = table.getJSONArray("columns");
			JSONArray values = table.getJSONArray("values");		

			for (Iterator<Object> iterator= records.iterator(); iterator.hasNext();) {

				Object[] nextRecord = (Object[])iterator.next();
				boolean match = false;
				int index = -1;

				for (int j = 0; j < cloumns.length(); j++) {

					Object nextField;
					if (nextRecord[j] instanceof Short) {
						nextField = new Integer(nextRecord[j].toString());
					} else {
						nextField = nextRecord[j];
					}

					if(j == 0){
						for (int i = 0; i < values.length(); i++) {

							JSONArray nextRow = values.getJSONArray(i);
							JSONObject nextColumn = nextRow.getJSONObject(j);

//							System.out.print(nextRecord[j].getClass() + " " + j + " " + nextRecord[j] + " " + nextColumn.get(cloumns.getString(j)).getClass() + " "+ nextColumn.get(cloumns.getString(j)));

							if(nextColumn.get(cloumns.getString(j)).equals(nextField)){
//								System.out.print("\t\t"+nextColumn.get(cloumns.getString(j)) + " " + nextField + " " + Arrays.toString(nextRecord)+ " "+ i);
								match = true;
								index = i;
								break;
							}
						}
					}
					if(j > 0 && match == true && index != -1){
						if(values.getJSONArray(index).getJSONObject(j).get(cloumns.getString(j)).equals(nextField)){
//							System.out.print("\t\t"+values.getJSONArray(index).getJSONObject(j).get(cloumns.getString(j)) + " " + nextField + " " + Arrays.toString(nextRecord)+ " "+ index);
							match = true;
						} else {
							match = false;
						}
					}
					if(match == false){
						break;
					}

				}
				if(match == true){
					iterator.remove();
					values.remove(index);
				}

//				System.out.println("\n" + match);
			}

//			System.out.println(records.size() + "  " + values.length());

			if (records.size() > 0) {
				ResponseBuilder.buildMetadataResponse(response, dataType, _columns, records);
			}
			if (values.length() > 0) {
				response.put(dataType + "_deleted", values);
			}


		} catch (Exception e) {
			e.printStackTrace();
			response.put(dataType+"_error", "no record found");
		}
	}


}
