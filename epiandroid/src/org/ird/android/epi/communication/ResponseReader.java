package org.ird.android.epi.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ord.ird.android.epi.db.CentreDBHelper;
import ord.ird.android.epi.db.LocationDbHelper;

import org.ird.android.epi.communication.elements.RequestElements;
import org.ird.android.epi.model.Centre;
import org.ird.android.epi.model.Location;
import org.ird.android.epi.model.LocationType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ResponseReader
{
	private Context context;
	private LocationDbHelper locationDb;
	private CentreDBHelper centreDb;
	public static final String TAG_RESPONSE_READER="ResponseReader";
	private static final String RESPONSE_STATUS="status";
	private static final String PARAMS="params";

	
	public ResponseReader(Context cxt)
	{
		this.context = cxt;
		locationDb = new LocationDbHelper(this.context);
		centreDb = new CentreDBHelper(this.context);
	}
	
	/***
	 * 
	 * @param response
	 * @return int representing one of the possible response codes from the ResponsStatus class.
	 * If error occurs, then a -1 is returned denoting error in reading the response from the server
	 */
	public  int readStatus(String response)
	{
		int status = -1;
		try
		{
			JSONObject obj = new JSONObject(response);
			status =(int) obj.getInt(RESPONSE_STATUS);			
			return status;
		}
		catch (JSONException e)
		{
			Log.e(TAG_RESPONSE_READER, "Exception while reading Status of response: " + e.getMessage());
		}
		catch(Exception e)
		{
			Log.e(TAG_RESPONSE_READER, "Exception while reading Status of response: " + e.getMessage());
		}
		
		return 0;
	}
	
	public  Map<String, Object> readParams(String response)
	{
		if(response == null)
			return null;
		try
		{
			JSONObject obj = new JSONObject(response);//Main response object
			JSONArray params = obj.getJSONArray(PARAMS); //object containing all the parameters
			
			//first check that there is data other than reponse's status
			if (params!=null  && params.length()!=0 )
			{
				JSONObject temp;
				Map<String, Object>paramsMap = new HashMap<String, Object>();
				for(int i =0; i < params.length();i++)
				{
					temp = params.getJSONObject(i);
					//separate key and value from the JSONObject(param)
					String key = getKey(temp);					
					paramsMap.put(key, temp.get(key));
				}
				return paramsMap;
			}			
		}
		catch (JSONException e)
		{
			Log.e(TAG_RESPONSE_READER, "Exception while reading response params: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	private  String getKey(JSONObject o)
	{
		String key=null;
		if(o !=null)
		{
			Iterator<String> iter = o.keys();
			while(iter.hasNext())
			{
				key = iter.next();
			}
		}
		return key;
	}
	
	public  boolean saveMetadata(JSONObject metadata) throws JSONException
	{
		boolean allSaved	=	false;
		boolean locationSaved,locationTypeSaved,centreSaved;
		
		locationSaved		= saveLocation(metadata);
		locationTypeSaved	= saveLocationType(metadata);
		centreSaved			= saveCentres(metadata);
		
		
		
		if(locationSaved && locationTypeSaved && centreSaved)	
		{
			allSaved=true;
		}
		
		return allSaved;
	}
	
	
	private  String[] getColumns(JSONObject metadataType) throws JSONException
	{
		JSONArray cols;
		String[] columns=null;
		if(metadataType!=null)
		{
			cols = metadataType.getJSONArray("columns");
			if(cols!=null)
			{
				columns = new String[cols.length()];
				for(int i=0; i < cols.length(); i ++)
				{
					columns[i]=cols.getString(i);
				}
			}
		}
		return columns;
	}
	/**
	 * Method to obtain array of value rows
	 * Each row is a JSON array
	 * @param metadataType
	 * @return
	 * @throws JSONException
	 */
	private  Object[] getValues(JSONObject metadataType) throws JSONException
	{
		JSONArray rows;
		Object[] valuesArray=null;
		
		if(metadataType!=null)
		{
			rows = metadataType.getJSONArray("values");
			if(rows!=null)
			{
				valuesArray = new Object[rows.length()];
				for(int i=0; i < rows.length(); i ++)
				{
					valuesArray[i]=rows.get(i);
				}
			}
		}
		return valuesArray;
	}
	private  boolean saveLocation(JSONObject metadata) throws JSONException
	{
		
		Object[] values;
		JSONObject metadataType=null;
		boolean isSaved = false;
		List<Location> locations=new ArrayList<Location>();
		Location currentLocation;
		JSONArray row;
		//save location
		if(metadata.has(/*"location"*/RequestElements.METADATA_LOCATION))
		{
			metadataType=(JSONObject)metadata.get(/*"location"*/RequestElements.METADATA_LOCATION);
			//the type was found
			if(metadataType!=null)
			{
				/*
				 * Columns in the order of "locationId","fullName","parentLocation","locationType"
				 */
				
				values=getValues(metadataType);
				for(int i=0;i<values.length;i++)
				{
					row = (JSONArray)values[i];
					JSONObject field;
					
					currentLocation = new Location();
					//get  location id
					field = row.getJSONObject(0);
					if(field!=null)
						currentLocation.setId((Integer)field.get(RequestElements.METADATA_FIELD_LOCATION_ID)); //id
					
					//get location name
					field = row.getJSONObject(1);
					if(field!=null)
						currentLocation.setFullName(field.getString(RequestElements.METADATA_FIELD_LOCATION_NAME)); //name
					
					//get parent id
					field = row.getJSONObject(2);
					if(field!=null)
					{
						if(!field.get(RequestElements.METADATA_FIELD_LOCATION_PARENT).equals(null))
						{
							currentLocation.setParentId((Integer)field.get(RequestElements.METADATA_FIELD_LOCATION_PARENT));
						}
					}
					
					//get locationType id
					field = row.getJSONObject(3);
					if(field!=null)
						currentLocation.setLocationType((Integer)field.get("locationType")); //location type id
					
					locations.add(currentLocation);
				}
				isSaved=locationDb.saveLocations(locations);				
			}
		}
		else
		{
			isSaved=true;
		}
		
		return isSaved;
	}

	private  boolean saveLocationType(JSONObject metadata) throws JSONException
	{
		
		Object[] values;
		JSONArray row;
		JSONObject metadataType=null;
		boolean isSaved = false;
		List<LocationType> locationTypes=new ArrayList<LocationType>();
		LocationType currentType;
		//save location
		if(metadata.has(/*"locationtype"*/RequestElements.METADATA_LOCATION_TYPE))
		{
			metadataType=(JSONObject)metadata.get(/*"locationtype"*/RequestElements.METADATA_LOCATION_TYPE);
			//the type was found
			if(metadataType!=null)
			{
				/*
				 * Columns in the order of "locationTypeId","typeName"
				 */
				values=getValues(metadataType);
				for(int i=0;i<values.length;i++)
				{
					row = (JSONArray)values[i];
					JSONObject field;
					
					currentType = new LocationType();
					
					field = row.getJSONObject(0);
					currentType.setLocationTypeId((Integer)field.get(RequestElements.METADATA_FIELD_LOCATION_TYPE_ID)); //id
					
					field = row.getJSONObject(1);
					currentType.setTypeName((String)field.getString(RequestElements.METADATA_FIELD_LOCATION_TYPE_NAME)); //name
					
					locationTypes.add(currentType);
				}
				
				isSaved=locationDb.saveLocationTypes(locationTypes);
			}
		}
		else
		{
			isSaved=true;
		}
		
		return isSaved;
	}
	private  boolean saveCentres(JSONObject metadata) throws JSONException
	{
		
		Object[] values;
		JSONArray row;
		JSONObject metadataType=null;
		boolean isSaved = false;
		List<Centre> centres=new ArrayList<Centre>();
		Centre currentCentre;
		//save location
		if(metadata.has(RequestElements.METADATA_VACCINATION_CENTRES))
		{
			metadataType=(JSONObject)metadata.get(RequestElements.METADATA_VACCINATION_CENTRES);
			//the type was found
			if(metadataType!=null)
			{
				/*
				 * Columns in the order of "locationTypeId","typeName"
				 */
				values=getValues(metadataType);
				for(int i=0;i<values.length;i++)
				{
					row = (JSONArray)values[i];
					JSONObject field;
					
					currentCentre = new Centre();
					
					field = row.getJSONObject(0);
					currentCentre.setCentreId((Integer)field.get(RequestElements.METADATA_FIELD_VACCINATION_CENTRE_ID)); //id
					
					field = row.getJSONObject(1);
					currentCentre.setName((String)field.getString(RequestElements.METADATA_FIELD_VACCINATION_CENTRE_NAME)); //name
					
					centres.add(currentCentre);
				}
				
				isSaved=centreDb.saveCentres(centres);
			}
		}
		else
		{
			isSaved=true;
		}
		
		return isSaved;
	}	

}
