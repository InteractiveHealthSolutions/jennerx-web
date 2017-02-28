package org.ird.unfepi.rest.resources;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.model.Device;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.helper.ChildServiceHelper;
import org.ird.unfepi.rest.helper.DeviceServiceHelper;
import org.ird.unfepi.utils.GZipper;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@Path("/update")
public class DownloadLatestDataService {


	private Date lastSynced;

	@Path("/children")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUpdatedChildren(String json){
		lastSynced=new Date();
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		try {
			JSONParser parser = new JSONParser();	

			JSONObject obj = (JSONObject)parser.parse(json);

			String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;

			org.json.JSONObject j=new org.json.JSONObject();
			j.put("allchildren",childServiceHelper.getUpdatedChildren(lastEditDate));

			return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Path("/vaccinations")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getUpdatedVaccinations(String json){
		DeviceServiceHelper deviceServiceHelper=new DeviceServiceHelper();
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		try {
			JSONParser parser = new JSONParser();	

			JSONObject obj = (JSONObject)parser.parse(json);

			String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;
			Long programId =(Long) obj.get(RequestElements.METADATA_FIELD_HEALTHPROGRAM_ID);

			org.json.JSONObject j=new org.json.JSONObject();
			j.put("allvaccinations",childServiceHelper.getUpdatedVaccinations(lastEditDate, programId));
			if(lastSynced==null){
				lastSynced=new Date();
			}
			Long deviceId = (Long) obj.get(RequestElements.DEVICE_DEVICEID);
			Device device= deviceServiceHelper.getDevice(deviceId);
			device.setLastSyncDate(lastSynced);
			deviceServiceHelper.updateDevice(device);

			return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Path("/encounters")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getNewEncounters(String json) throws IOException, JSONException{
		lastSynced=new Date();
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		try {
			JSONParser parser = new JSONParser();	
			JSONObject obj = (JSONObject)parser.parse(json);
			String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;
			Long programId =(Long) obj.get(RequestElements.METADATA_FIELD_HEALTHPROGRAM_ID);

			org.json.JSONObject j=new org.json.JSONObject();
			j.put("allencounters",childServiceHelper.getNewEucounters(lastEditDate, programId));
			
			return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Path("/itemsdistributed")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getItemsDistributed(String json) throws IOException, JSONException{
		lastSynced=new Date();
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		try {
			JSONParser parser = new JSONParser();	
			JSONObject obj = (JSONObject)parser.parse(json);
			String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;

			org.json.JSONObject j=new org.json.JSONObject();
			j.put("allitemsdistributed",childServiceHelper.getNewItemsDistributed(lastEditDate));
			
			return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Path("/muacmeasurements")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getMuacMeasurements(String json) throws IOException, JSONException{
		lastSynced=new Date();
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		try {
			JSONParser parser = new JSONParser();	
			JSONObject obj = (JSONObject)parser.parse(json);
			String lastEditDate=(String)obj.get(RequestElements.LAST_SYNC_TIME)	;

			org.json.JSONObject j=new org.json.JSONObject();
			j.put("allmuacmeasurements",childServiceHelper.getNewMuacMeasurements(lastEditDate));
			
			return GZipper.compress(j.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
