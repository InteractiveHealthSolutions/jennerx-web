package org.ird.unfepi.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Device;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.helper.ChildEnrollmentServiceHelper;
import org.ird.unfepi.rest.helper.DeviceServiceHelper;
import org.ird.unfepi.utils.GZipper;
import org.ird.unfepi.utils.LoggerUtils;
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
		Device device = null;
		Long deviceId;
		
		ChildEnrollmentServiceHelper childHelper=new ChildEnrollmentServiceHelper();
		JSONParser parser = new JSONParser();		
		ServiceContext sc=Context.getServices();
		StringBuilder sB=new StringBuilder();
		JSONObject jsonObject=new JSONObject();
		sB.append("{");
		try 
		{
			DeviceServiceHelper deviceServiceHelper=new DeviceServiceHelper();
			JSONObject obj1 = (JSONObject)parser.parse(json);
			String receivedJson=GZipper.decompress((String)obj1.get("compress"));
			JSONObject obj = (JSONObject)parser.parse(receivedJson);
			System.out.println(obj);
			JSONArray enrollmentArray=(JSONArray) obj.get("child");
			JSONArray eventArray=(JSONArray) obj.get("event");
			JSONArray vaccinationArray=(JSONArray) obj.get("vaccination");
			JSONArray updateArray=(JSONArray) obj.get("update");
			JSONArray itemsAray=(JSONArray) obj.get("itemsdistributed");
			JSONArray muacAray=(JSONArray) obj.get("muacmeasurements");
			
			deviceId = (Long) obj.get(RequestElements.DEVICE_DEVICEID);
			long lastCount = (Long) obj.get("lastCount");
			device= deviceServiceHelper.getDevice(deviceId);
			
			jsonObject.put("Enrollment",childHelper.addEnrollments(enrollmentArray));
			jsonObject.put("Vaccination",childHelper.addVaccinations(vaccinationArray));
			jsonObject.put("Event",childHelper.addEvent(eventArray));
			jsonObject.put("Update",childHelper.addUpdates(updateArray));
			jsonObject.put("ItemsDistributed",childHelper.addItemDistributed(itemsAray));
			jsonObject.put("MuacMeasurements",childHelper.addMuacMeasurement(muacAray));
					
			device.setLastCount((int)lastCount);
			deviceServiceHelper.updateDevice(device);
			sc.commitTransaction();
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			sc.closeSession();
		}
		
		int count= device.getLastCount()>0?device.getLastCount():0;
		jsonObject.put("lastCount", count);
		sB.append(" \"lastCount\" : "+count);
		sB.append("}");
		System.out.println(jsonObject.toJSONString());
		return jsonObject.toJSONString();
	}
}
