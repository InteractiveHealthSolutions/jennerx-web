package org.ird.unfepi.rest.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ird.unfepi.GlobalParams;

public class RestUtils 
{
	private static SimpleDateFormat sdf =  null;
	
	public static boolean setBoolean(String field)
	{
		if("false".equalsIgnoreCase(field))
		{
			return false;
		}
		else if("true".equalsIgnoreCase(field))
		{
			return true;
		}
		return false;
	}
	
	public static String dateToString(Date date, String dateFormat)
	{
		String dateString="";
		SimpleDateFormat sdfCustom =null;
		if(date==null)
			return dateString;
		
		if(dateFormat!= null)
		{
			sdfCustom = new SimpleDateFormat(dateFormat);		
		}
		else
		{
			if(sdf==null)
				sdf=new SimpleDateFormat(RestConstants.MOBILE_DATE_FORMAT);
		}
		try
		{			
			if(sdfCustom!=null)
			{
				dateString = sdfCustom.format(date);
			}
			else
			{
				dateString = sdf.format(date);
			}
		}
		catch(Exception ex)
		{
			GlobalParams.MOBILELOGGER.error(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
		
		return dateString;	
		}
	
	public static Date stringToDate(String dateString)
	{
		Date d = null;
		try
		{
			try
			{
				sdf = new SimpleDateFormat(RestConstants.MOBILE_DATE_FORMAT);
				if(dateString== null || "".equals(dateString))
					return null;
				d = sdf.parse(dateString);
			}
			catch (Exception e) //if the format from mobile client can not be used, use the default server date format
			{
				GlobalParams.MOBILELOGGER.error(e.getMessage());
				d = GlobalParams.DEFAULT_MOBILE_DATE_FORMAT.parse(dateString);
				e.printStackTrace();
			}
						
			return d;
		}
		catch(Exception ex)
		{
			//Log the Exception here
			GlobalParams.MOBILELOGGER.error(ex.getMessage());
			ex.printStackTrace();
			return null;
		}		
	}
	
	public static int getInt(String field)
	{
		try
		{
			Integer i  = Integer.parseInt(field);
			return i;
		}
		catch(Exception ex)
		{
			GlobalParams.MOBILELOGGER.error(ex.getMessage());
			ex.printStackTrace();
			return 0;
		}
		
	}

}
