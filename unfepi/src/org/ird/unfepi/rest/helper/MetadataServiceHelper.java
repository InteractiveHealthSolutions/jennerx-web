package org.ird.unfepi.rest.helper;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.rest.elements.RequestElements;
import org.ird.unfepi.rest.elements.ResponseElements;
import org.ird.unfepi.rest.elements.ResponseStatus;
import org.ird.unfepi.service.impl.CustomQueryServiceImpl;
import org.json.simple.JSONObject;

public class MetadataServiceHelper
{	
	ServiceContext sc = Context.getServices();
	
	public static String getMetadata()
	{
		try
		{
			JSONObject mainResponse=new JSONObject();
			
			fillLocation(mainResponse);
			fillLocationType(mainResponse);
			fillVaccinationCentres(mainResponse);
			
			HashMap<String, Object> resp = new HashMap<String, Object>();
			resp.put("METADATA", mainResponse);
			
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_SUCCESS, resp);
		}
		catch (Exception e)
		{
			GlobalParams.MOBILELOGGER.equals(e);
			e.printStackTrace();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("error", "Error in getting metadata");
			return ResponseBuilder.buildResponse(ResponseStatus.STATUS_INTERNAL_ERROR, map);
		}
	}
	
	public String getMetadata(String dataType)
	{	
		if (dataType==null)
			return null;
		
		JSONObject mainResponse=new JSONObject();		
		
		if(dataType.equalsIgnoreCase(RequestElements.METADATA_LOCATION))
		{			
			fillLocation(mainResponse);			
		}
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_LOCATION_TYPE))
		{
			fillLocationType(mainResponse);
		}
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_VACCINE))
		{
			fillVaccine(mainResponse);
		}
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_VACCINATION_CENTRES))
		{
			fillVaccinationCentres(mainResponse);
		}
		
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_VACCINE_SCHEDULE))
		{
			fillVaccine(mainResponse);
		}
		
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_USER))
		{
			fillUser(mainResponse);
		}
		else if(dataType.equalsIgnoreCase(RequestElements.METADATA_ALL))
		{
			fillLocation(mainResponse);	
			fillLocationType(mainResponse);
			fillVaccine(mainResponse);
			fillVaccinationCentres(mainResponse);
			fillVaccine(mainResponse);
			fillUser(mainResponse);
		}			
		
		if(mainResponse != null && mainResponse.values().size() >1)
			return mainResponse.toJSONString();
		else 
			return null;
	}
	
	private static void fillLocation(JSONObject mainResponse)
	{
		String[] columns = new String[]{RequestElements.METADATA_FIELD_LOCATION_ID,
										RequestElements.METADATA_FIELD_LOCATION_NAME,
										RequestElements.METADATA_FIELD_LOCATION_PARENT,
										"locationType"};
		String table = "location";
		fetchMetaData(/*"location"*/RequestElements.METADATA_LOCATION, columns, table, mainResponse);
	}
	
	private static void fillLocationType(JSONObject mainResponse)
	{
		String[] columns = new String[]{RequestElements.METADATA_FIELD_LOCATION_TYPE_ID,
										RequestElements.METADATA_FIELD_LOCATION_TYPE_NAME };
		String table = "locationtype";
		fetchMetaData(/*"locationtype"*/RequestElements.METADATA_LOCATION_TYPE, columns, table, mainResponse);
	}
	
	private static void fillVaccine(JSONObject mainResponse)
	{
		//TODO: after discussion with Maimoona. Vaccine table needs restructuring
		//TODO: add logic for returning the vaccine data here
	}
	
	private static void fillUser(JSONObject mainResponse)
	{
		//TODO: add logic for returning the User data here
	}
	
	private static void fillVaccinationCentres(JSONObject mainResponse)
	{
		
		String[] columns = new String[]{RequestElements.METADATA_FIELD_VACCINATION_CENTRE_ID,
				RequestElements.METADATA_FIELD_VACCINATION_CENTRE_NAME };
		String table = "vaccinationcenter";
		fetchMetaData(RequestElements.METADATA_VACCINATION_CENTRES, columns, table, mainResponse);
		
}
	
	
	
	private static void fetchMetaData(String dataType, String[] columns, String table, JSONObject container )
	{
		try
		{
			if(container== null)
				container = new JSONObject();
			String query = CustomQueryBuilder.query(columns, table);			
			ServiceContext sc = Context.getServices();
			List results =sc.getCustomQueryService().getDataBySQL(query);
			ResponseBuilder.buildMetadataResponse(container, dataType, columns, results);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public File zipData(File tempFile)
	{	
		//get the temporary file first
		if(tempFile.exists())
		{
			try
			{
				FileInputStream fis = new FileInputStream(tempFile);
				File tempZipFile = File.createTempFile("raw", "cmprsd");
				if(tempZipFile.exists())
				{
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempZipFile));
					ZipEntry entry = new ZipEntry(tempFile.getName());
					zos.putNextEntry(entry);
					byte[] data = new byte[1024];
					while(fis.read(data)>0)
					{
						zos.write(data, 0, data.length);
					}
					zos.flush();
					zos.close();
					fis.close();
					return tempZipFile;
				}
			}
			catch (IOException e)
			{
				GlobalParams.MOBILELOGGER.error("Error occured while creating zip file for metadata");
				GlobalParams.MOBILELOGGER.error(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		
		return null;
	}
	
	public static File createTempFile(String data)
	{
		try
		{			
			File temp = File.createTempFile("rwData", "meta");
			temp.createNewFile();
			
			if (temp.exists())
			{				
				FileOutputStream fos = new FileOutputStream(temp);
				fos.write(data.getBytes());
				fos.flush();
				fos.close();
				System.out.println("Temp file :" + temp.getAbsolutePath() + " created");
				return temp;
			}
		}
		catch (Exception e)
		{			
			GlobalParams.MOBILELOGGER.error("Error occured while creating file for metadata");
			GlobalParams.MOBILELOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		return null;
	}


}