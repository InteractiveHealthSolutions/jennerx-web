package org.ird.unfepi.rest.resources;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.MetadataServiceHelper;
import org.ird.unfepi.rest.helper.MetadataServiceHelper2;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.json.JSONArray;
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
	public String downloadAll()
	{	
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			String metadata = helper.getMetadata();
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
	@Path("/vaccine")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccine(String jsonString){
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
			String metadata = helper.getMetadata(RequestElements.METADATA_VACCINE);
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
			String metadata = helper.getMetadata(RequestElements.METADATA_LOCATION);
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
	public String metadataUser(HttpServletRequest req) {
		String jsonString = req.getParameter(RequestElements.METADATA_USERS);
		String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			JSONArray usersId = jsonObject.getJSONArray(RequestElements.METADATA_USERS+RequestElements.METADATA_IDS);
			
			String lastEditDateStr = jsonObject.getString(RequestElements.LAST_SYNC_TIME);
			Date lastEditDate = WebGlobals.GLOBAL_SQL_DATETIME_FORMAT.parse(lastEditDateStr);
						
			response = MetadataServiceHelper2.fillUsers(lastEditDate, usersId);
			
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
