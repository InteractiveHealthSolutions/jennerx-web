package org.ird.unfepi.rest.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.model.Device;
import org.ird.unfepi.model.User;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.rest.helper.ChildServiceHelper;
import org.ird.unfepi.rest.helper.DeviceServiceHelper;
import org.ird.unfepi.rest.helper.MetadataServiceHelper;
import org.ird.unfepi.rest.helper.ResponseBuilder;
import org.ird.unfepi.rest.helper.UserServiceHelper;
import org.ird.unfepi.utils.GZipper;
import org.ird.unfepi.utils.SecurityUtils;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




@Path("/firstsetup")
public class FirstSetupService {

	@Path("/authenticate")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String authenticate(String json)
	{		
		JSONObject jsonObject=new JSONObject();
		try {
		JSONParser parser = new JSONParser();	
		JSONObject obj;
		
		obj = (JSONObject)parser.parse(json);
		String userName = (String) obj.get(RequestElements.LG_USERNAME);
		String password = (String) obj.get(RequestElements.LG_PASSWORD);
		
		DeviceServiceHelper deviceServiceHelper=new DeviceServiceHelper();
		UserServiceHelper userServiceHelper=new UserServiceHelper();
		String pwd=SecurityUtils.decrypt(password, userName);
		User user=	userServiceHelper.authenticateUser(pwd, userName, RequestElements.USER_TYPE_ADMIN);
		if(user==null)
		{
			
			return String.valueOf(ResponseStatus.STATUS_INCORRECT_CREDENTIALS);
		}
		else{
		
			String androidId = (String) obj.get(RequestElements.DEVICE_ANDROIDID);
			String macId = (String) obj.get(RequestElements.DEVICE_MACID);
			String serialId = (String) obj.get(RequestElements.DEVICE_SERIALID);
			Device device= deviceServiceHelper.getDevice(androidId, serialId, macId);
			
			JSONObject deviceDetails=new JSONObject();
			deviceDetails.put(RequestElements.DEVICE_DEVICEID, device.getDeviceId());
			deviceDetails.put(RequestElements.DEVICE_LASTCOUNT, device.getLastCount());
			
			jsonObject.put("deviceDetails", deviceDetails);
			jsonObject.put("status",String.valueOf(ResponseStatus.STATUS_SUCCESS) );
			return jsonObject.toJSONString() ;
		}
		
		
		
		}catch ( ParseException e){
			e.printStackTrace();
			return String.valueOf(ResponseStatus.STATUS_INCORRECT_CREDENTIALS);

		} 
			catch (Exception e) {
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			return String.valueOf(ResponseStatus.STATUS_INTERNAL_ERROR);
		}
	}
	
	@Path("/allchildren")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllChildren(String json) throws IOException, JSONException{
		System.gc();
		JSONParser parser = new JSONParser();
		JSONObject receivedJson;
		try {
			receivedJson = (JSONObject)parser.parse(json);

			Long lastRecord=(Long) receivedJson.get(RequestElements.LASTRECORD);
			ChildServiceHelper childServiceHelper=new ChildServiceHelper();
			org.json.JSONObject j=new org.json.JSONObject();
			List<HashMap> map=childServiceHelper.getAllChildren(lastRecord);
			j.put("allchildren",map );
			int length=map.size()-1;
			int size=length>0?length:0;
			int mappedId=-2;
			if(size>0){
				mappedId=(Integer) map.get(size).get("mappedId");
			}
			j.put(RequestElements.LASTRECORD,mappedId );
			return GZipper.compress(j.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);

		}

	}
	
	
	@Path("/allvaccinations")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllVaccinations(String json) throws IOException, JSONException{
		System.gc();
		JSONParser parser = new JSONParser();
		JSONObject receivedJson;
		try {
			receivedJson = (JSONObject)parser.parse(json);
		
		Long lastRecord=(Long) receivedJson.get(RequestElements.LASTRECORD);
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		org.json.JSONObject j=new org.json.JSONObject();
		List<HashMap> map=childServiceHelper.getAllChidrenVaccinations(lastRecord);
		j.put("allvaccinations", map);
		
		int length=map.size()-1;
		int size=length>0?length:0;
		int mappedId=-2;
		if(size>0){
			mappedId=(Integer) map.get(size).get("vId");
		}
		j.put(RequestElements.LASTRECORD, mappedId);
		
		return GZipper.compress(j.toString());
		} catch (ParseException e) {
		
			e.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_VACCINATOPN_DATA_NULL, null);
		}
	}
	
	@Path("/allencounters")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllEncounters(String json) throws IOException, JSONException{
		System.gc();
		JSONParser parser = new JSONParser();
		JSONObject receivedJson;
		try {
			receivedJson = (JSONObject)parser.parse(json);

			Long lastRecord=(Long) receivedJson.get(RequestElements.LASTRECORD);
			ChildServiceHelper childServiceHelper=new ChildServiceHelper();
			org.json.JSONObject j=new org.json.JSONObject();
			List<HashMap> map=childServiceHelper.getallEncounters(lastRecord);
			j.put("allencounters",map );
			
			j.put(RequestElements.LASTRECORD,lastRecord + map.size());
			// return j.toString();
			return GZipper.compress(j.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INCORRECT_DATA_FORMAT_ERROR, null);
		}
	}
	

}
