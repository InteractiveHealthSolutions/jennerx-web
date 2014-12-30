package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UserSessionUtils;

public class DWRVaccinationCenterService
{
	public List<Map<String, Object>> getAllVaccinationCenters(Map<String, String> params) {
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
		
		try{
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>(); 
		
		List centers = sc.getCustomQueryService().getDataBySQL("SELECT i.identifier, CONCAT(i.identifier,':',v.name) FROM vaccinationcenter v JOIN idmapper id ON v.mappedId=id.mappedId JOIN identifier i ON id.mappedId=i.mappedId AND i.preferred=TRUE ORDER BY i.identifier");
        for (Object object : centers) {
			Object[] objarr = (Object[]) object;
			Map<String,Object> item = new HashMap<String,Object>();
			
			item.put("id", objarr[0]==null?"":objarr[0]);
			item.put("text", objarr[1]==null?"":objarr[1]);
			items.add(item);
		}
        
       /* Map<String,Object> defaultItem = new HashMap<String,Object>();
		
        defaultItem.put("id", "");
        defaultItem.put("text", "Select Center (optional)");

        items.add(0, defaultItem);*/
        return items;  
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return null;
	}
	public int getVaccinationCentersCountInCity(String cityId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try{
			List res = sc.getCustomQueryService().getDataBySQL("select count(*) from vaccinationcenter join identifier using(mappedId) where identifier like '"+cityId+"%'");
			return res.size()==0?0:Integer.parseInt(res.get(0).toString());
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return -1;
	}
	
	public boolean isIdExists(String programId){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			//return "Session expired. Logout from application and login again";
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ServiceContext sc = Context.getServices();
		try {
			List resl = sc.getCustomQueryService().getDataBySQL("select count(*) from identifier where identifier='"+programId+"'");
			return resl.size() == 0 ? false: Integer.parseInt(resl.get(0).toString())>0;
		}
		finally {
			sc.closeSession();
		}
	}
}
