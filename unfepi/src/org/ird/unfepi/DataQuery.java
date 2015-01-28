package org.ird.unfepi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataQuery {

	public static final String CHILD_INCENTIVE_QUERY = getQuery("ChildIncentiveQuery.sql");
	public static final String EXPORT_CHILD_INCENTIVE_QUERY = getQuery("ExportChildIncentiveQuery.sql");
	public static final String EXPORT_VACCINE_SMS_QUERY = getQuery("ExportVaccineSmsQuery.sql");
	public static final String EXPORT_LOTTERY_SMS_QUERY = getQuery("ExportLotterySmsQuery.sql");
	public static final String EXPORT_STOREKEEPER_QUERY = getQuery("ExportStorekeeperQuery.sql");
	public static final String EXPORT_VACCINATOR_QUERY = getQuery("ExportVaccinatorQuery.sql");

	private static String getQuery(String fileName){
		try{
			InputStream in = DataQuery.class.getResourceAsStream("/org/ird/unfepi/queries/"+fileName);
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			StringBuilder stringJson = new StringBuilder();
	
			int chunksize = 1024;
			char[] charBuffer = new char[chunksize];
		    int count = 0;
	
		    do {
		    	count = r.read(charBuffer, 0, chunksize);
	
		    	if (count >= 0) {
		    		stringJson.append(charBuffer, 0, count);
		    	}
		    } while (count>0);
		    
		    r.close();
		        
			return stringJson.toString();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
