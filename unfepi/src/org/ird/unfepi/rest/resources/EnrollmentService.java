package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.EnrollmentServiceHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



@Path("/enrollment")
public class EnrollmentService 
{
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String enroll(String json)
	{
		JSONParser parser = new JSONParser();		
		try 
		{
			JSONObject obj = (JSONObject)parser.parse(json);
			String response = EnrollmentServiceHelper.createEnrollment(obj);
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
	@Consumes(MediaType.TEXT_PLAIN)
	public JSONObject getEnrolledChildrenByDate(@QueryParam("from") String dateFrom, @QueryParam("to")String dateTo)
	{
		JSONObject parentObj = new JSONObject();
		
		JSONObject object;
		JSONObject[] array = new JSONObject[2];
		
		object = new JSONObject();
		object.put("name", "Omar Ahmed");
		object.put("sex", "male");
		
		array[0] = object;
		
		object = new JSONObject();
		object.put("name", "Maimoona Kausar");
		object.put("sex", "female");
		
		array[1] = object;
		
		parentObj.put("children", array);
		parentObj.put("FromDate", dateFrom);
		parentObj.put("ToDate", dateTo);
		return parentObj;		
	}
	
}
