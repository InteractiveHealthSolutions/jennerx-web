package org.ird.unfepi.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriUtils;

import com.mysql.jdbc.StringUtils;

public class UnfepiUtils {

	public static String getStringFilter(SearchFilter filter, HttpServletRequest req){
		return (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter(filter.FILTER_NAME()))) ? null : req.getParameter(filter.FILTER_NAME());
	}
	
	public static void executeDump(String procedureName){
		Session ses = Context.getNewSession();
		try {
			executeDump(procedureName, ses);
		}
		finally{
			ses.close();
		}
	}
	
	public static void executeDump(String procedureName, Session ses){
			Transaction tx = ses.beginTransaction();
			ses.createSQLQuery("CALL "+procedureName+"();").executeUpdate();
			ses.createSQLQuery("UPDATE dmp_ SET lastDumpDate = NOW() WHERE dmpProcedureName='"+procedureName+"'").executeUpdate();
			tx.commit();
			ses.flush();
	}
	
	public static void executeDump(String procedureName, Integer calenderId){
		Session ses = Context.getNewSession();
		try {
			executeDump(procedureName, calenderId, ses);
		}
		finally{
			ses.close();
		}
	}
	
	public static void executeDump(String procedureName,Integer calenderId, Session ses){
		Transaction tx = ses.beginTransaction();
		ses.createSQLQuery("CALL "+procedureName+"("+ calenderId +");").executeUpdate();
		ses.createSQLQuery("UPDATE dmp_ SET lastDumpDate = NOW() WHERE dmpProcedureName='"+procedureName+"'").executeUpdate();
		tx.commit();
		ses.flush();
}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static Enum getEnumFilter(SearchFilter filter, Class enumType, HttpServletRequest req){
		String filterVal = getStringFilter(filter, req);
		if(filterVal != null){
			return Enum.valueOf(enumType, filterVal);
		}
		return null;
	}
	
	public static Integer getIntegerFilter(SearchFilter filter, HttpServletRequest req){
		String strval = getStringFilter(filter, req);
		return strval==null?null:Integer.parseInt(strval);
	}
	
	public static Float getFloatFilter(SearchFilter filter, HttpServletRequest req){
		String strval = getStringFilter(filter, req);
		return strval==null?null:Float.parseFloat(strval);
	}
	
	public static Date getDateFilter(SearchFilter filter, HttpServletRequest req) throws ParseException{
		String strval = getStringFilter(filter, req);
		return strval==null?null:WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(strval);	
	}
	
	public static String setDateFilter(Date date) throws ParseException{
		return date==null?null:WebGlobals.GLOBAL_JAVA_DATE_FORMAT.format(date);	
	}
	
	public static JSONArray getCSVToJson(Scanner path) throws JSONException {
    	String[] hr = getHeaderRowNum(path);
	    // Start constructing JSON.
	    JSONArray jarr = new JSONArray();

	    while (true) {
			List<String> rc = getNextRowContent(path);
			if(rc.isEmpty()){
				break;
			}
			JSONObject row = new JSONObject();
			for (int j = 0; j < hr.length; j++) {
				row.put(hr[j], rc.get(j));
			}
			jarr.put(row);
		}

    	return jarr;
	}

	private static String[] getHeaderRowNum(Scanner scanner) {
		while (scanner.hasNextLine()) {
			String[] r = scanner.nextLine().replace("\"", "").split(",");
			for (String c : r) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(c)){
					return r;
				}
			}
		}
		return null;
	}
	private static List<String> getNextRowContent(Scanner scanner) {
		List<String> hc = new ArrayList<String>();
		if(scanner.hasNextLine())
		for (String c : scanner.nextLine().replace("\"", "").split(",", -1)) {
			hc.add(c);
		}
		return hc;
	}
	
	public static RedirectView redirectView(String url, String payload) throws UnsupportedEncodingException {
		return new RedirectView(url+"?"+UriUtils.encodeQuery(payload, "UTF-8"));
	}
}
