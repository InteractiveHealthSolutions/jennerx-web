package org.ird.unfepi.rest.helper;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ResponseBuilder
{
	private static final String RESPONSE_STATUS="status";
	private static final String PARAMS="params";
	public static final String LOTTERY_RESULTS="lottery_results";
	
	public static String buildResponse(int status, Map<String, Object>params)
	{
		JSONObject responseHolder = new JSONObject();
		
		responseHolder.put(RESPONSE_STATUS, status);
		
		//CHECK DATA OTHER THAN RESULT OF THE REQUEST NEEDS TO BE SENT BACK 
		if (params != null && params.size() >0)
		{
			//STORE ALL THE PATAMS IN AN ARRAY
			JSONArray arrayParams = new JSONArray();
			
			//OBJECT TO EXTRACT EACH KEY-VALUE PAIR
			JSONObject tempObject;
			
			//ITERATE THROUGH THE HASHMAP
			for (String s : params.keySet())
			{
				tempObject = new JSONObject();
				tempObject.put(s, params.get(s));
				arrayParams.add(tempObject);
			}
			
			//RETURN RESPONSE IN A FORMAT THAT CAN BE SENT BACK TO MOBILE DIRECTLY
			responseHolder.put(PARAMS, arrayParams);
		}
		return responseHolder.toJSONString();
	}

	/**
	 * Method to create JSON representing a single metadata entity
	 * Structure of Holder JSONObject:
		 * |-Holder
		 * |
		 * |-Category Metadata 1
		 *   |----Data Category 1
		 * |
		 * |
		 * |-Category Metadata 2
		 * 	 |----Data Category 2
		 * |
		 * |
		 * |-Category Metadata N
		 *   |----Data Category N 
		 
	 * @param parent
	 * @param tag
	 * @param columns
	 * @param rows
	 * @throws MalformedMetadataException
	 * @throws ResponseFormatException 
	 */
	public static void buildMetadataResponse(JSONObject parent, String tag, String[] columns, List<Object> rows) throws MalformedMetadataException, ResponseFormatException
	{
		try
		{
			//1.Add columns
			JSONObject metadata = new JSONObject();		
			JSONObject field=null;
			JSONArray columnsArray = new JSONArray();			
			for(String col: columns)
			{
				columnsArray.add(col);
			}
			metadata.put("columns", columnsArray);
			
			//2.Add values of each row in JSONArray
			JSONArray allRows = new JSONArray();
			JSONArray rowJson;
			
			//Add values in all the rows for metadata entity
			for(Object o: rows)
			{
				Object[] row;				
				//each element in the array is also an array
				row  = (Object[])o;
				
				rowJson = new JSONArray();
				
				//add values in a row
				for(int i =0; i < row.length; i ++)
				{
					//inner loop on all fields in the row, add json objects in the row
					for(int j =0; j<columns.length; j ++)
					{
						field = new JSONObject();
//						if(row[j] == null)
//							field.put(columns[j], -1);
//						else
							field.put(columns[j], row[j]);
						rowJson.add(field);
					}
				}
				//values were added or not
				if(rowJson.size()>0)
				{
					//add to the JSONArray object holding all the values
					allRows.add(rowJson);
				}
			}
			
			//3.place in the parent container
			metadata.put("values", allRows);			
			parent.put(tag, metadata);
		}
		catch (Exception e)
		{
			throw new ResponseFormatException ("Unexpected data for building resopnse");
		}	
	}
	
	public static class MalformedMetadataException extends Exception
	{
		public MalformedMetadataException (String message)
		{
			super(message);
		}
	}
	
	public static class ResponseFormatException extends Exception
	{
		public ResponseFormatException (String message)
		{
			super(message);
		}
	}
}
