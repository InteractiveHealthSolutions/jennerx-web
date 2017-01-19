package org.ird.unfepi.rest.helper;

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

public class MetadataServiceHelper2 {
	

	public static String getUsersMetadata(JSONObject jsonObject){

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		try {
			JSONArray usersId = jsonObject.getJSONArray(RequestElements.METADATA_USERS+RequestElements.METADATA_IDS);
			
			String lastEditDateStr = jsonObject.getString(RequestElements.LAST_SYNC_TIME);
			Date lastEditDate = WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.parse(lastEditDateStr);

			fillUsers(lastEditDate, usersId, response);

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
	

	public static String getVaccineMetadata(JSONObject jsonObject){

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		try {
			JSONArray vaccinesId = jsonObject.getJSONArray(RequestElements.METADATA_VACCINE+RequestElements.METADATA_IDS);

			String lastEditDateStr = jsonObject.getString(RequestElements.LAST_SYNC_TIME);
			Date lastEditDate = WebGlobals.GLOBAL_JAVA_DATETIME_FORMAT.parse(lastEditDateStr);

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

	public static String getLocationMetadata(JSONObject jsonObject){

		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		try {
			JSONArray centrId = jsonObject.getJSONArray(RequestElements.METADATA_VACCINATION_CENTRES+RequestElements.METADATA_IDS);

			String lastEditDateStr = jsonObject.getString(RequestElements.LAST_SYNC_TIME);
			Date lastEditDate = WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.parse(lastEditDateStr);

			fillVaccinationCentres(lastEditDate, centrId, response);
			fillLocation(jsonObject, response);
			fillLocationType(jsonObject, response);

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
			fetchMetaData(RequestElements.METADATA_VACCINE, columns, table,  WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.format(lastEditDate), response);
			findDeletedIds(RequestElements.METADATA_VACCINE, columns, table, ids, RequestElements.METADATA_FIELD_VACCINE_ID, response);
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
				RequestElements.METADATA_FIELD_VACCINEGAP_VALUE };
		String table = "vaccinegap";

		fetchAndCompareMetaData(RequestElements.METADATA_VACCINEGAP, columns, table, response, json);
	}

	private static void fillVaccineGapType(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINEGAP_VACCINEGAPTYPEID,
				RequestElements.METADATA_FIELD_VACCINEGAPTYPE_NAME };
		String table = "vaccinegaptype";

		fetchAndCompareMetaData(RequestElements.METADATA_VACCINEGAPTYPE, columns, table, response, json);
	}

	private static void fillVaccinePrerequisite(JSONObject json, org.json.simple.JSONObject response) {

		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINE_ID,
				RequestElements.METADATA_FIELD_VACCINEPREREQUISITE_ID,
				RequestElements.METADATA_FIELD_VACCINEPREREQUISITE_MANDATORY };
		String table = "vaccineprerequisite";

		fetchAndCompareMetaData(RequestElements.METADATA_VACCINEPREREQUISITE, columns, table, response, json);
	}

	private static void fillVaccinationCentres(Date lastEditDate, JSONArray ids, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] {
				RequestElements.METADATA_FIELD_VACCINATION_CENTRE_ID,
				RequestElements.METADATA_FIELD_VACCINATION_CENTRE_NAME };
		String table = "vaccinationcenter";
		try {
			fetchMetaData(RequestElements.METADATA_VACCINATION_CENTRES, columns, table,  WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.format(lastEditDate), response);
			findDeletedIds(RequestElements.METADATA_VACCINATION_CENTRES, columns, table, ids, RequestElements.METADATA_FIELD_VACCINATION_CENTRE_ID, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void fillLocation(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] { RequestElements.METADATA_FIELD_LOCATION_ID,
				RequestElements.METADATA_FIELD_LOCATION_NAME,
				RequestElements.METADATA_FIELD_LOCATION_PARENT,
		"locationType" };
		String table = "location";

		fetchAndCompareMetaData(RequestElements.METADATA_LOCATION, columns, table, response, json);
	}

	private static void fillLocationType(JSONObject json, org.json.simple.JSONObject response) {
		String[] columns = new String[] { 
				RequestElements.METADATA_FIELD_LOCATION_TYPE_ID,
				RequestElements.METADATA_FIELD_LOCATION_TYPE_NAME };
		String table = "locationtype";

		fetchAndCompareMetaData(RequestElements.METADATA_LOCATION_TYPE, columns, table, response, json);
	}
	
	private static void fillHealthProgram(JSONObject json, org.json.simple.JSONObject response)
	{
		String[] columns = new String[] { 
				RequestElements.METADATA_FIELD_HEALTHPROGRAM_ID,
				RequestElements.METADATA_FIELD_HEALTHPROGRAM_NAME };
		String table = "healthprogram";

		fetchAndCompareMetaData(RequestElements.METADATA_HEALTHPROGRAM, columns, table, response, json);
	}
	
	public static void fillUsers(Date lastEditDate_c, JSONArray identifiers, org.json.simple.JSONObject response)
	{
		ServiceContext sc = Context.getServices();
//		org.json.simple.JSONObject response = new org.json.simple.JSONObject();

		String[] columns = new String[] {RequestElements.METADATA_USER_USERNAME, RequestElements.METADATA_USER_PASSWORD, RequestElements.METADATA_USER_IDENTIFIER,
				RequestElements.METADATA_USER_STATUS, RequestElements.METADATA_USER_CREATEDDATE, RequestElements.METADATA_USER_LASTEDITDATE };

		String query1 = "SELECT identifier.identifier FROM unfepi.identifier "
				+ "inner join unfepi.user on unfepi.identifier.mappedId=unfepi.user.mappedId  "
				+ "inner join vaccinator on unfepi.user.mappedId=vaccinator.mappedId "
				+ "where identifier.identifier in ("+identifiers.toString().replaceAll("\\[|\\]", "")+");";

		String query2 = "SELECT user.username , user.password, identifier.identifier, user.status, user.createdDate, user.lastEditedDate FROM unfepi.identifier "
				+ "inner join unfepi.user on unfepi.identifier.mappedId=unfepi.user.mappedId  "
				+ "inner join vaccinator on unfepi.user.mappedId=vaccinator.mappedId "
				+ "where (user.createdDate > '" + WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.format(lastEditDate_c) 
				+ "' OR user.lastEditedDate > '"+ WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.format(lastEditDate_c) +"') ;";

		List<String> records1 = sc.getCustomQueryService().getDataBySQL(query1);
		List records2 = sc.getCustomQueryService().getDataBySQL(query2);
		try{			
			
			for (int i = 0; i < identifiers.length(); i++) {
				int index = -1;
				for (String str : records1) {
					if (identifiers.getString(i).equals(str)) {
						index = i;
						break;
					}
				}
				if (index != -1) {
					identifiers.remove(index);
					i--;
				}
			}

			ResponseBuilder.buildMetadataResponse(response, RequestElements.METADATA_USERS, columns, records2);
			response.put(RequestElements.METADATA_USERS+"_deleted", identifiers);

		} catch(Exception e){
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
//		return response.toString();
	}

	//	public static String fillVaccinePrerequisite(JSONArray jsonArray)
	//	{
	//		JSONObject response = new JSONObject();
	//
	//		ServiceContext sc = Context.getServices();
	//		GsonBuilder gsonBuilder = new GsonBuilder();
	//		Gson gson = gsonBuilder.registerTypeAdapter(VaccinePrerequisite.class, new VaccinePrerequisiteAdapter()).create();
	//
	//		List<VaccinePrerequisite> records = sc.getCustomQueryService().getDataByHQL("from VaccinePrerequisite");
	//
	//		try {
	//			for (Iterator<VaccinePrerequisite> iterator= records.iterator(); iterator.hasNext();) {
	//				VaccinePrerequisite vp_server = iterator.next();
	//				int index = -1;
	//				for(int i= 0 ; i < jsonArray.length(); i++){
	//					VaccinePrerequisite vp_client = gson.fromJson(jsonArray.getJSONObject(i).toString(), VaccinePrerequisite.class);
	//					if(vp_server.equals(vp_client)){
	//						iterator.remove();
	//						index = i ;
	//						break;
	//					}
	//				}
	//				if(index != -1){
	//					jsonArray.remove(index);
	//				}
	//			}
	//
	//			JSONArray newData = new JSONArray();
	//			for (VaccinePrerequisite vp : records) {
	//				newData.put(new JSONObject(gson.toJson(vp)));
	//			}
	//
	//			response.put("new", newData);
	//			response.put("deleted", jsonArray);
	//		} catch (JSONException e) {
	//			e.printStackTrace();
	//		}
	//		return response.toString();
	//	}

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
		} finally {
			sc.closeSession();
		}
	}

	private static void findDeletedIds(String dataType, String[] columns, String table, JSONArray ids, String column, org.json.simple.JSONObject container)
 {
		ServiceContext sc = Context.getServices();
		try {
			if (container == null)
				container = new org.json.simple.JSONObject();

			String query = CustomQueryBuilder.queryWhereIn(table, ids.toString().replaceAll("\\[|\\]", ""), column);
			List results = sc.getCustomQueryService().getDataBySQL(query);

			for (int i = 0; i < ids.length(); i++) {
				int index = -1;
				for (Object obj : results) {
					if (ids.getString(i).equals(obj.toString())) {
						index = i;
						break;
					}
				}
				if (index != -1) {
					ids.remove(index);
					i--;
				}
			}

			container.put(dataType + "_deleted", ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
	}

	private static void fetchAndCompareMetaData(String dataType, String[] _columns, String _table, org.json.simple.JSONObject response, JSONObject requestjson){
		ServiceContext sc = Context.getServices();

		try {
			String query = CustomQueryBuilder.query(_columns, _table);
			List<Object> records = sc.getCustomQueryService().getDataBySQL(query);

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
		} finally {
			sc.closeSession();
		}
	}


}
