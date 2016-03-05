package org.ird.unfepi.rest.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.ird.unfepi.rest.elements.RequestElements;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SupplementaryVaccinesHepler {

	public static void save(int childId, String epiNumber, JSONArray supplementary , int vaccinatorId, int userId) {
		// Jugarr goes here ask Maimoona for actual solution for saving supplementary vaccines
		
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("unfepi.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	
	    String DB_URL = (String) properties.get("unfepi.database.connection.url");//"jdbc:mysql://localhost/unfepi3";

	   //  Database credentials
	    String USER = (String) properties.get("unfepi.database.connection.username");// "root";
	    String PASS = (String) properties.get("unfepi.database.connection.password");// "123456";
	
	    
	    
	    java.sql.Connection conn = null;
	    java.sql.Statement statement = null;
	    try{
	       //STEP 2: Register JDBC driver
	       Class.forName("com.mysql.jdbc.Driver");

	       //STEP 3: Open a connection
	       System.out.println("Connecting to database...");
	       conn = DriverManager.getConnection(DB_URL,USER,PASS);

	       //STEP 4: Execute a query
	       System.out.println("Creating statement...");
	       statement = conn.createStatement();
	       
	       String sql;
	       SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       String toDate = f.format(new Date());
	       sql = "insert into vaccination (childId, createdDate, epiNumber, vaccinationCenterId, vaccinationDate, vaccinationDuedate, vaccinationStatus, vaccinatorId, vaccineId, createdByUserId) ";
	       sql+="values";
	       for(int t=0;t<supplementary.size(); t++) {
				JSONObject j = (JSONObject) supplementary.get(t);
				String vaccinationDate = f.format(RestUtils. stringToDate(j.get(RequestElements.DATE_OF_VACCINATION).toString()));
			String v = "select vaccineId from vaccine where name = \""+j.get(RequestElements.VACCINENAME).toString()+"\"";
	    	   ResultSet r = statement.executeQuery(v);
	    	   r.next();
	    	   int i = r.getInt("vaccineId");
	    	   
	    	   if(t!=supplementary.size()-1) {
	    		   sql += "("+childId+", \""+toDate+"\", \""+epiNumber+"\", "+j.get(RequestElements.VACCINATION_CENTER).toString()+", \""+vaccinationDate+"\", \""+vaccinationDate+"\", \"VACCINATED\", "+vaccinatorId+", "+i+", "+userId+"),";
	    	   } else {
	    		   sql += "("+childId+", \""+toDate+"\", \""+epiNumber+"\", "+j.get(RequestElements.VACCINATION_CENTER).toString()+", \""+vaccinationDate+"\", \""+vaccinationDate+"\", \"VACCINATED\", "+vaccinatorId+", "+i+", "+userId+")";
	    	   }
	    	   
	    	   
	       }
	       		
	       System.out.println(sql);
	       statement.executeUpdate(sql);

	       
	       statement.close();
	       conn.close();
	    }catch(SQLException se){
	       //Handle errors for JDBC
	       se.printStackTrace();
	    }catch(Exception e){
	       //Handle errors for Class.forName
	       e.printStackTrace();
	    }finally{
	       //finally block used to close resources
	       try{
	          if(statement!=null)
	             statement.close();
	       }catch(SQLException se2){
	       }// nothing we can do
	       try{
	          if(conn!=null)
	             conn.close();
	       }catch(SQLException se){
	          se.printStackTrace();
	       }//end finally try
	    }//end try
	    System.out.println("vaccine saved!");
	// End of jugarr
	}
}
