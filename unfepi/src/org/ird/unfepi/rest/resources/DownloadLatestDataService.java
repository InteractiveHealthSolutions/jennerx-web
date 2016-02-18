package org.ird.unfepi.rest.resources;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.helper.ChildServiceHelper;
import org.ird.unfepi.utils.GZipper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Path("/update")
public class DownloadLatestDataService {

	
	@Path("/children")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUpdatedChildren(String json){
	
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		JSONObject jsonObject=new JSONObject();
		try {
		JSONParser parser = new JSONParser();	
		
		JSONObject obj = (JSONObject)parser.parse(json);

	String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;
		
	;
	org.json.JSONObject j=new org.json.JSONObject();
	j.put("allchildren",childServiceHelper.getUpdatedChildren(lastEditDate));
	return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Path("/vaccinations")
	public String getUpdatedVaccinations(String json){
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		JSONObject jsonObject=new JSONObject();
		try {
		JSONParser parser = new JSONParser();	
		
		JSONObject obj = (JSONObject)parser.parse(json);

	String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;
	
	org.json.JSONObject j=new org.json.JSONObject();
	j.put("allchildren",childServiceHelper.getUpdatedVaccinations(lastEditDate));
	
	return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
