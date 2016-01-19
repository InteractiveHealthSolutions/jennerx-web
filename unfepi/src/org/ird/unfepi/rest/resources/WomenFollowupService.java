/**
 * 
 */
package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.FollowupHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.ird.unfepi.rest.helper.WomenFollowupHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Safwan
 *
 */
@Path("/womenFollowup")
public class WomenFollowupService {
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addFollowup(String json)
	{
		JSONParser parser = new JSONParser();		
		try 
		{
			JSONObject obj = (JSONObject)parser.parse(json);
			String response = WomenFollowupHelper.saveForm(obj);
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
	public String getWomenInformation(@QueryParam(RequestElements.WOMEN_ID) String womenId)
	{
		try
		{
			return WomenFollowupHelper.getData(womenId);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, null);
		}
	}

}
