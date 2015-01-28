package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.EnrollmentServiceHelper;
import org.ird.unfepi.rest.helper.UserServiceHelper;
import org.ird.unfepi.service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Path("/login")
public class LoginService
{
	
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)	
	public String login(String json)
	{		
		JSONParser parser = new JSONParser();		
		try
		{
			JSONObject obj = (JSONObject)parser.parse(json);
		    return String.valueOf(UserServiceHelper.parseUser(obj));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			return String.valueOf(ResponseStatus.STATUS_INTERNAL_ERROR);
		}
		
	}

}
