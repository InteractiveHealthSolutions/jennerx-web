package org.ird.unfepi.rest.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Device;


public class DeviceServiceHelper {

	
	public Device registerDevice(String androidId, String serialId,String macId){
		ServiceContext sc =Context.getServices();
		String query="select max(deviceId) from device";
		Device device = null;
		try{
			//id must be greater then 200 . 
			List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
			int lastDeviceId=0;
			System.out.println(map);
			if(map.get(0).get("max(deviceId)")!=null){
			lastDeviceId=(Integer)map.get(0).get("max(deviceId)");
			lastDeviceId++;
			
			}
			int deviceIdd=map.get(0).get("max(deviceId)")==null?201:lastDeviceId;
			
			device=new Device(deviceIdd, androidId,serialId,macId,0,new Date());
			sc.getCustomQueryService().save(device);
			sc.commitTransaction();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally{
			sc.closeSession();
			
		}
		
		return device;
	}
	
	public Device getDevice(String androidId, String serialId,String macId){
		ServiceContext sc =Context.getServices();
		String query="Select * from device where androidId='"+androidId+"' or serialId='"+serialId+"' or macId='"+macId+"'";
		Device device=null;
		try{
		List<HashMap> map = sc.getCustomQueryService().getDataBySQLMapResult(query);
		if(map.size()>0){
			System.out.println(map.get(0).get("androidId"));
		device=new Device((Integer)map.get(0).get("deviceId"), String.valueOf(map.get(0).get("androidId")), (String)map.get(0).get("serialId"), (String)map.get(0).get("macId"), (Integer)map.get(0).get("lastCount"),(Date)map.get(0).get("lastSyncDate"));
		return device;
		}
		else {
			
			return registerDevice(androidId, serialId, macId);
		}
		}catch(Exception e){
			e.printStackTrace();
			
		}
		finally{
			sc.closeSession();
			
		}
		
		return null;
	}
	
	
}
