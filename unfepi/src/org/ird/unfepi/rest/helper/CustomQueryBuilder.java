package org.ird.unfepi.rest.helper;

public class CustomQueryBuilder
{
	
	private static final String SELECT ="SELECT";
	private static final String FROM ="FROM";
	private static final String WHERE ="WHERE";
	private static final String createdDate ="createdDate";
	private static final String lastEditedDate ="lastEditedDate";
	private static final String OR ="OR";
	private static final String IN ="IN";
	
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
	
	public static String queryCreatedUpdated(String[] columns, String table, String date) throws Exception
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
		query.append(" ");
		query.append(WHERE);
		query.append(" ");
		query.append("(");
		query.append(createdDate);
		query.append(">");
		query.append("'");
		query.append(date);
		query.append("'");
		query.append(" ");
		query.append(OR);
		query.append(" ");
		query.append(lastEditedDate);
		query.append(">");
		query.append("'");
		query.append(date);
		query.append("'");
		query.append(")");
				
		return query.toString();
	}
	
	public static String queryWhereIn(String table, String inList, String column) throws Exception
	{
		if(column==null || "".equals(column.trim()) || table==null || "".equals(table.trim()))
			throw new Exception("Please provide at least 1 column and 1 table");
		
		StringBuilder query=new StringBuilder();
		query.append(SELECT);
		query.append(" ");
		query.append(column);
		query.append(" ");
		query.append(FROM);
		query.append(" ");
		query.append(table);
		query.append(" ");
		query.append(WHERE);
		query.append(" ");
		query.append(column);
		query.append(" ");
		query.append(IN);
		query.append("(");
		query.append(inList);
		query.append(")");		
				
		return query.toString();
	}
}
