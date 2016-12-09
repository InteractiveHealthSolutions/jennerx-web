package org.ird.unfepi.rest.resources;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.MetadataServiceHelper;
import org.ird.unfepi.rest.helper.MetadataServiceHelper2;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.json.JSONObject;

@Path("/metadata")
public class MetadataService
{	
	public String getMetadata(String data_request[])
	{
		String data=null;
		return data;		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public String downloadAll(@QueryParam("programId") int programId)
	{	
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			String metadata = helper.getMetadata(programId);
			return metadata;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			HashMap<String, Object>resp = new HashMap<String, Object>();
			resp.put("error", "Error occured in fetching data");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE,resp);
		}
		
	}
	
//	@POST
//	@Path("/vaccinePrerequisite")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_JSON)
//	public String metadataVaccinePrerequisite(String jsonString){
//		String response = "";
//		try {
//			JSONObject jsonObject = new JSONObject(jsonString);
//			JSONArray jsonArray = jsonObject.getJSONArray(RequestElements.METADATA_VACCINEPREREQUISITE);
//			response = MetadataServiceHelper2.fillVaccinePrerequisite(jsonArray);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
	
	@GET
	@Path("/metadata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccine(String jsonString, @QueryParam("programId") int programId){
		/*String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);			
			response = MetadataServiceHelper2.getVaccineMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;*/
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			String metadata = helper.getMetadata(RequestElements.METADATA_VACCINE, programId);
			return metadata;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			HashMap<String, Object>resp = new HashMap<String, Object>();
			resp.put("error", "Error occured in fetching data");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE,resp);
		}
		
	}
	
	@GET
	@Path("/location")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccinationCentres(String jsonString){
		/*String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			response = MetadataServiceHelper2.getLocationMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;*/
		
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			String metadata = helper.getMetadata(RequestElements.METADATA_LOCATION, 0);
			return metadata;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			HashMap<String, Object>resp = new HashMap<String, Object>();
			resp.put("error", "Error occured in fetching data");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE,resp);
		}
	}
	
	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataUser(@QueryParam("users") String jsonString) {
		String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
//			JSONArray usersId = jsonObject.getJSONArray(RequestElements.METADATA_USERS+RequestElements.METADATA_IDS);
//			
//			String lastEditDateStr = jsonObject.getString(RequestElements.LAST_SYNC_TIME);
//			Date lastEditDate = WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.parse(lastEditDateStr);
						
//			response = MetadataServiceHelper2.fillUsers(lastEditDate, usersId);
			response = MetadataServiceHelper2.getUsersMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GET
	@Path("/healthprogram")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataHealthProgram(String jsonString){
		String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			response = MetadataServiceHelper2.getHealthProgramMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
