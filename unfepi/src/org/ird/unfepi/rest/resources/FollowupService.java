package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.FollowupHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("/followup")
public class FollowupService {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addFollowup(String json)
	{
		JSONParser parser = new JSONParser();		
		try 
		{
			JSONObject obj = (JSONObject)parser.parse(json);
			String response = FollowupHelper.saveForm(obj);
			return response;
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, null);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getChildInformation(@QueryParam(RequestElements.CHILD_ID) String childId)
	{
		try
		{
			return FollowupHelper.getData(childId);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, null);
		}
	}
	
	@Path("/identifier")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String switchIdentifier(@QueryParam(RequestElements.CHILD_ID) String childId,
			@QueryParam(RequestElements.CHILD_NFC_ID) String oldId)
	{
		try
		{
			return FollowupHelper.switchIdentifier(oldId,childId);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			GlobalParams.MOBILELOGGER.equals(ex);
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, null);
		}		
	}
}
