package org.ird.unfepi.rest.resources;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Device;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.RoundVaccine;
import org.ird.unfepi.model.VialCount;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.MetadataServiceHelper;
import org.ird.unfepi.rest.helper.MetadataServiceHelper2;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.ird.unfepi.rest.helper.RestUtils;
import org.ird.unfepi.utils.GZipper;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
	public String downloadAll(@QueryParam("programId") int programId, @QueryParam("deviceId") int deviceId)
	{	
		MetadataServiceHelper helper = new MetadataServiceHelper();
		
		try
		{
			ServiceContext sc = Context.getServices();
			Device device = (Device) sc.getCustomQueryService().getDataByHQL("from Device where deviceId = " + deviceId).get(0) ;
			device.setHealthProgramId(programId);
			sc.getCustomQueryService().saveOrUpdate(device);
			sc.commitTransaction();
			
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
	
	@POST
	@Path("/save/vialcount")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveVialInfo(String json)
	{
		JSONParser parser = new JSONParser();		
		ServiceContext sc=Context.getServices();
		try {
			org.json.simple.JSONObject obj1 = (org.json.simple.JSONObject)parser.parse(json);
//			String receivedJson=GZipper.decompress((String)obj1.get("compress"));
//			org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject)parser.parse(receivedJson);
			
			org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject)parser.parse(json);
			JSONArray vialArray=(JSONArray) obj2.get("vialCount");
			for (int i = 0; i < vialArray.size(); i++) {
				try{
					org.json.simple.JSONObject objectToParse = (org.json.simple.JSONObject) vialArray.get(i);
					String date = (String) objectToParse.get(RequestElements.METADATA_FIELD_VIAL_DATE);
					Integer startCount = ((Long)objectToParse.get(RequestElements.METADATA_FIELD_VIAL_STARTCOUNT)).intValue();
					Integer endCount = ((Long) objectToParse.get(RequestElements.METADATA_FIELD_VIAL_ENDCOUNT)).intValue();
					Integer centreId = ((Long) objectToParse.get(RequestElements.METADATA_FIELD_VIAL_CENTREID)).intValue();
					Integer roundId = ((Long) objectToParse.get(RequestElements.METADATA_FIELD_VIAL_ROUNDID)).intValue();
//					boolean isBeginning = Boolean.parseBoolean(((Long)objectToParse.get(RequestElements.METADATA_FIELD_VIAL_ISBEGINNING)).toString());
//					boolean isBeginning = (Long)objectToParse.get(RequestElements.METADATA_FIELD_VIAL_ISBEGINNING) == 1;
					short vaccineId = ((Long)objectToParse.get(RequestElements.METADATA_FIELD_VIAL_VACCINEID)).shortValue();
					
					VialCount vialCount = new VialCount();
					vialCount.setDate(WebGlobals.GLOBAL_SQL_DATE_FORMAT.parse(date));
					vialCount.setStartCount(startCount);
					vialCount.setEndCount(endCount);
					vialCount.setCentreId(centreId);
					vialCount.setRoundId(roundId);
					vialCount.setVaccineId(vaccineId);
					
					sc.getCustomQueryService().save(vialCount);
				}catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			sc.commitTransaction();
			
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
			
		} catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, null);
		} finally{
			sc.closeSession();
		}
	}
	
	@POST
	@Path("/save/roundvaccine")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String saveRoundVaccine(String json)
	{
		JSONParser parser = new JSONParser();		
		ServiceContext sc=Context.getServices();
		try {
			org.json.simple.JSONObject obj1 = (org.json.simple.JSONObject)parser.parse(json);
			String receivedJson=GZipper.decompress((String)obj1.get("compress"));
			org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject)parser.parse(receivedJson);
			
//			org.json.simple.JSONObject obj2 = (org.json.simple.JSONObject)parser.parse(json);
			JSONArray vialArray=(JSONArray) obj2.get("VaccineStatus");
			for (int i = 0; i < vialArray.size(); i++) {
				try{
					org.json.simple.JSONObject objectToParse = (org.json.simple.JSONObject) vialArray.get(i);
					Integer roundId = ((Long) objectToParse.get(RequestElements.METADATA_FIELD_ROUNDVACCINE_ROUNDID)).intValue();
					//boolean status = (Long)objectToParse.get(RequestElements.METADATA_FIELD_ROUNDVACCINE_STATUS) == 1;
					boolean status = (Boolean) objectToParse.get(RequestElements.METADATA_FIELD_ROUNDVACCINE_STATUS) ;
					short vaccineId = ((Long)objectToParse.get(RequestElements.METADATA_FIELD_ROUNDVACCINE_VACCINEID)).shortValue();
					RoundVaccine roundVaccine;
					List<RoundVaccine> records = sc.getCustomQueryService().getDataByHQL("from RoundVaccine where vaccineId = " +vaccineId + " and roundId = "+ roundId);
					if(records != null && records.size() > 0){
						roundVaccine = records.get(0);
					} else {
						roundVaccine =  new RoundVaccine();
						roundVaccine.setRoundId(roundId);
						roundVaccine.setVaccineId(vaccineId);
					}
					roundVaccine.setStatus(status);
					sc.getCustomQueryService().saveOrUpdate(roundVaccine);
				}catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			sc.commitTransaction();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			sc.rollbackTransaction();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, null);
		} finally{
			sc.closeSession();
		}
	}

	@GET
	@Path("/devicehealthprogram")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deviceHealthProgram(@QueryParam("device") String jsonString){
		String response = "";
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			System.out.println(jsonString);
			
			Integer healthprogramId = (Integer) jsonObject.get("programId");
			Integer deviceId = (Integer) jsonObject.get("deviceId");
			
			ServiceContext sc = Context.getServices();
			
			Device device = (Device) sc.getCustomQueryService().getDataByHQL("from Device where deviceId = " + deviceId).get(0) ;
			device.setHealthProgramId(healthprogramId);
			sc.getCustomQueryService().saveOrUpdate(device);
			sc.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	@GET
	@Path("/metadata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String metadataVaccine(String jsonString, @QueryParam("programId") int programId){
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
