package org.ird.unfepi.rest.helper;

public class CustomQueryBuilder
{
	
	private static final String SELECT ="SELECT";
	private static final String FROM ="FROM";
	
	public static String query(String[] columns, String table) throws Exception
	{
		if(columns.length<1 || table==null || "".equals(table.trim()))
			throw new Exception("Please provide at least 1 column and 1 table");
		
		StringBuilder query=new StringBuilder();
		query.append(SELECT);
		query.append(" ");
		
		if(columns.length ==1)
		{
			query.append(columns[0]);
		}
		//iterate through columns to add to query
		else
		{
			for(int i =0; i <columns.length; i ++)
			{
				//add ',' for all columns except after the last column
				query.append(columns[i]);
				if(i != (columns.length-1))
				{
					query.append(", ");
				}
			}
		}
		query.append(" ");
		query.append(FROM);
		query.append(" ");
		query.append(table);		
				
		return query.toString();
	}
}
