package org.ird.unfepi.rest.resources;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.directwebremoting.annotations.Param;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.EnrollmentServiceHelper;
import org.ird.unfepi.rest.helper.FollowupHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.ird.unfepi.rest.helper.StorekeeperHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestParam;


@Path("/storekeeper")
public class StorekeeperService
{
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getLotteriesWon(@QueryParam(RequestElements.CHILD_ID) String childId)
	{
		try
		{
			return StorekeeperHelper.findLotteryWinnings(childId);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_FAILURE, null);
		}
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String consumeLottery(String json)
	{
		JSONParser parser = new JSONParser();		
		try 
		{
			JSONObject form = (JSONObject)parser.parse(json);
			String response = StorekeeperHelper.consumeLottery(form);
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
}
