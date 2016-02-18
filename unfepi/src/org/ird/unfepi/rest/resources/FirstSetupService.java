package org.ird.unfepi.rest.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
	//	System.out.println(json);
		// GET PARAMETERS FROM MOBILE
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
		
		
		
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			return String.valueOf(ResponseStatus.STATUS_INTERNAL_ERROR);
		}
	}
	
	@Path("/allchildren")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllChildren() throws IOException, JSONException{
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		//return childServiceHelper.getAllChildren().toString(); its around 9.8mb
		
		
		// return compressString(childServiceHelper.getAllChildren().toString());
	//	return GZipper.compress("allChildren:"+childServiceHelper.getAllChildren().toString());
		//org.json.JSONArray json=new org.json.JSONArray(childServiceHelper.getAllChildren().toString());
		org.json.JSONObject j=new org.json.JSONObject();
		j.put("allchildren", childServiceHelper.getAllChildren());
		
	return GZipper.compress(j.toString());
	
	}
	
	
	@Path("/allvaccinations")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllVaccinations() throws IOException, JSONException{
		ChildServiceHelper childServiceHelper=new ChildServiceHelper();
		org.json.JSONObject j=new org.json.JSONObject();
		j.put("allvaccinations", childServiceHelper.getAllChidrenVaccinations());

		return GZipper.compress(j.toString());
	}
	
	public static String compressString(String str) throws IOException{
		if (str == null || str.length() == 0) {
		    return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(str.length());
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();

		byte[] compressedBytes = out.toByteArray(); 

		//Gdx.files.local("gziptest.gzip").writeBytes(compressedBytes, false);
		//out.close();

		return out.toString(); // I would return compressedBytes instead String
		}
	

}
