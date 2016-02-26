package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/childenrollment")
public class ChildEnrollmentService {

	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String enroll(String json)
	{
		JSONParser parser = new JSONParser();		
		try 
		{
			JSONObject obj = (JSONObject)parser.parse(json);
		
		}catch(Exception e){
			
		}
		return "";
	}
}
