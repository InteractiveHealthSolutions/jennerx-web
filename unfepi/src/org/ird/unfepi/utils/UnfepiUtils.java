package org.ird.unfepi.utils;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;

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
}
