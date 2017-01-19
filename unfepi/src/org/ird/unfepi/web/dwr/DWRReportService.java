package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UserSessionUtils;

import com.mysql.jdbc.StringUtils;

public class DWRReportService {

	public Map<String, Object> getSummaryEnrollmentByCenter(Map<String, String> params) throws ParseException {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());
		String date1from = params.get(DWRParamsGeneral.date1from.name());
		String date1to = params.get(DWRParamsGeneral.date1to.name());
		
		String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
		String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
			String sort = params.get("sort");
			String order = params.get("order");

			int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));

			items = sc.getCustomQueryService().getDataBySQLMapResult("CALL SummaryEnrByCenterCohort('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , "+(pageNumber*pageSize)+", "+pageSize+", '"+(sort==null?"":sort)+"', '"+(order==null?"":order)+"')");
		
			int totalRows = StringUtils.isEmptyOrWhitespaceOnly(center)?sc.getVaccinationService().getAllVaccinationCenter(true, null).size():center.split(",").length;
			map.put("rows", items);
		    map.put("total", totalRows);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
	    //System.out.println(map);
		return map;
	}
	
	public Map<String, Object> getSummaryFollowupAgeAppropriate(Map<String, String> params) throws ParseException {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());
		String date1from = params.get(DWRParamsGeneral.date1from.name());
		String date1to = params.get(DWRParamsGeneral.date1to.name());
		
		String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
		String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
			int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));
			String sort = params.get("sort");
			String order = params.get("order");
			
//			items = sc.getCustomQueryService().getDataBySQLMapResult("CALL SummaryFollowupAgeAppropriate('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , "+(pageNumber*pageSize)+", "+pageSize+", '"+(sort==null?"":sort)+"', '"+(order==null?"":order)+"')");
		
			int totalRows = items.size();//StringUtils.isEmptyOrWhitespaceOnly(center)?sc.getVaccinationService().getAllVaccinationCenter(true, null).size():center.split(",").length;
			map.put("rows", items);
		    map.put("total", totalRows);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
//	    System.out.println(map);
		return map;
	}
	
	public Map<String, Object> getSummaryFollowupAgeAppropriateWRetro(Map<String, String> params) throws ParseException {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());
		String date1from = params.get(DWRParamsGeneral.date1from.name());
		String date1to = params.get(DWRParamsGeneral.date1to.name());
		
		String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
		String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
			int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));
			String sort = params.get("sort");
			String order = params.get("order");
			
//			items = sc.getCustomQueryService().getDataBySQLMapResult("CALL SummaryFollowupAgeAppropriateWRetro('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , "+(pageNumber*pageSize)+", "+pageSize+", '"+(sort==null?"":sort)+"', '"+(order==null?"":order)+"')");
		
			int totalRows = items.size();//StringUtils.isEmptyOrWhitespaceOnly(center)?sc.getVaccinationService().getAllVaccinationCenter(true, null).size():center.split(",").length;
			map.put("rows", items);
		    map.put("total", totalRows);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
//	    System.out.println(map);
		return map;
	}
	
	public Map<String, Object> getSummaryFupByVaccinator(Map<String, String> params) throws ParseException {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());
		String date1from = params.get(DWRParamsGeneral.date1from.name());
		String date1to = params.get(DWRParamsGeneral.date1to.name());
		
		String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
		String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
			String sort = params.get("sort");
			String order = params.get("order");

			int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));

			items = sc.getCustomQueryService().getDataBySQLMapResult("CALL SummaryImmunizationByVaccinator('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , "+(pageNumber*pageSize)+", "+pageSize+", '"+(sort==null?"":sort)+"', '"+(order==null?"":order)+"')");
		
			int totalRows = StringUtils.isEmptyOrWhitespaceOnly(center)?sc.getVaccinationService().getAllVaccinator(0, 1000000000, true, null).size():center.split(",").length;
			map.put("rows", items);
		    map.put("total", totalRows);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
	    //System.out.println(map);
		return map;
	}
	
	public Map<String, Object> getSummaryFupByCenter(Map<String, String> params) throws ParseException {
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());
		String date1from = params.get(DWRParamsGeneral.date1from.name());
		String date1to = params.get(DWRParamsGeneral.date1to.name());
		
		String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
		String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		try{
			int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
			String sort = params.get("sort");
			String order = params.get("order");

			int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));

			items = sc.getCustomQueryService().getDataBySQLMapResult("CALL SummaryImmunizationByCenter('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , "+(pageNumber*pageSize)+", "+pageSize+", '"+(sort==null?"":sort)+"', '"+(order==null?"":order)+"')");
		
			int totalRows = StringUtils.isEmptyOrWhitespaceOnly(center)?sc.getVaccinationService().getAllVaccinationCenter(0, 1000000000, true, null).size():center.split(",").length;
			map.put("rows", items);
		    map.put("total", totalRows);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		
	    //System.out.println(map);
		return map;
	}
}
