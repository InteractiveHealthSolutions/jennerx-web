package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.helper.MetadataServiceHelper2;
import org.ird.unfepi.rest.helper.ProgramMetaDataServiceHelper;
import org.json.JSONObject;

@Path("/prog/metadata")
public class ProgramMetaDataService {
	
	@GET
	@Path("/vaccine")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccine(String jsonString, @QueryParam("calendarId") String calendarId){
		String response = "";
		try {
			
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;	
			jsonObject.put("calendarId", calendarId);
			
			response = ProgramMetaDataServiceHelper.getVaccineMetadata(jsonObject); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response ;
	}
	
	@GET
	@Path("/healthprogram")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataHealthProgram(String jsonString){
		String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			response = ProgramMetaDataServiceHelper.getHealthProgramMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
