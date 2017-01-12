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
import org.json.JSONException;
import org.json.JSONObject;

@Path("/prog/metadata")
public class ProgramMetaDataService {
	
	@GET
	@Path("/vaccine")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccine(String jsonString, @QueryParam("programId") String programId){
		String response = "";
		try {
			System.out.println("jsonString " + jsonString);
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;	
			jsonObject.put("programId", programId);
			
			response = ProgramMetaDataServiceHelper.getVaccineMetadata(jsonObject); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response ;
	}
	
	@GET
	@Path("/location")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataLocation(String jsonString, @QueryParam("programId") String programId){
		String response = "";
		try {
			
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;	
			jsonObject.put("programId", programId);
			System.out.println("metadata - locations");
			response = ProgramMetaDataServiceHelper.getLocationMetadata(jsonObject); 
			
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
			
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;
			response = ProgramMetaDataServiceHelper.getHealthProgramMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GET
	@Path("/round")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataRound(String jsonString, @QueryParam("programId") String programId){
		String response = "";
		try {
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;
			jsonObject.put("programId", programId);
			
			response = ProgramMetaDataServiceHelper.getRoundMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GET
	@Path("/itemstock")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataItemStock(String jsonString){
		String response = "";
		try {
			JSONObject jsonObject = (jsonString.isEmpty()) ? new JSONObject() : new JSONObject(jsonString) ;
			response = ProgramMetaDataServiceHelper.getItemStockMetadata(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
}

