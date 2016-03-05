package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.rest.helper.ChildEnrollmentServiceHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("/childenrollment")
public class ChildEnrollmentService {

	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String enroll(String json)
	{
		ChildEnrollmentServiceHelper childHelper=new ChildEnrollmentServiceHelper();
		JSONParser parser = new JSONParser();		
		ServiceContext sc=Context.getServices();
		try 
		{
			
			JSONObject obj = (JSONObject)parser.parse(json);
			JSONArray enrollmentArray=(JSONArray) obj.get("enrollment");
			JSONArray eventArray=(JSONArray) obj.get("event");
			JSONArray vaccinationArray=(JSONArray) obj.get("vaccination");
			JSONArray updateArray=(JSONArray) obj.get("update");
			
			childHelper.addEnrollments(enrollmentArray,sc);
			childHelper.addEvent(eventArray, sc);
			childHelper.addVaccinations(vaccinationArray, sc);
			childHelper.addUpdates(updateArray, sc);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			sc.closeSession();
		}
		
		return "";
	}
}
