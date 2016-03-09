package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.rest.helper.ChildEnrollmentServiceHelper;
import org.ird.unfepi.utils.GZipper;
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
		StringBuilder sB=new StringBuilder();
		sB.append("{");
		try 
		{
			JSONObject obj1 = (JSONObject)parser.parse(json);
			String receivedJson=GZipper.decompress((String)obj1.get("compress"));
			JSONObject obj = (JSONObject)parser.parse(receivedJson);
			System.out.println(obj);
			JSONArray enrollmentArray=(JSONArray) obj.get("child");
			JSONArray eventArray=(JSONArray) obj.get("event");
			JSONArray vaccinationArray=(JSONArray) obj.get("vaccination");
			JSONArray updateArray=(JSONArray) obj.get("update");
			
			sB.append(childHelper.addEnrollments(enrollmentArray));
			sB.append(childHelper.addVaccinations(vaccinationArray));
			sB.append(childHelper.addEvent(eventArray));
			sB.append(childHelper.addUpdates(updateArray));
		}catch(Exception e){
			e.printStackTrace();
		}
		sB.append("}");
		return sB.toString();
	}
}
