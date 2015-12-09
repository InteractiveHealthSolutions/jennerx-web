package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.constants.WebGlobals.DGUserSmsFieldNames;
import org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.UserSessionUtils;

import com.mysql.jdbc.StringUtils;

public class DWREntityService {
	
	public List<Map<String, String>> getLocationList (String[] locationTypes, Integer parentId) {
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
		
		String qry ="SELECT locationId, name, fullName FROM location l JOIN locationtype lt ON l.locationType=lt.locationTypeId WHERE lt.typeName IN ("+IRUtils.getArrayAsString(locationTypes, ",", "'")+", null)"+(parentId==null?"":" AND (parentLocation IS NULL || parentLocation="+parentId+")")+" ORDER BY lt.locationTypeId,l.fullname";
		System.out.println(qry);
		List list = sc.getCustomQueryService().getDataBySQL(qry);
		
		List<Map<String, String>> locations = new ArrayList<Map<String,String>>();

		for (Object object : list) {
			HashMap<String, String> loc = new HashMap<String, String>();
			Object[] oar = (Object[]) object;
			loc.put("locationId", oar[0].toString());
			loc.put("name", oar[1].toString());
			loc.put("fullname", oar[2].toString());
			locations.add(loc);
		}		
		return locations;
	}
	
	public List<Map<String, String>> getLocationListByParentName (String[] locationTypes, String parentName) {
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
		
		String qry ="SELECT l.locationId, l.name, l.fullName FROM location l "
				+ "JOIN locationtype lt ON l.locationType=lt.locationTypeId "
				+ "LEFT JOIN location pl ON pl.locationId=l.parentLocation "
				+ "WHERE lt.typeName IN ("+IRUtils.getArrayAsString(locationTypes, ",", "'")+", null)"
				+(parentName==null?"":" AND (l.parentLocation IS NULL || pl.name='"+parentName+"')")
				+" ORDER BY lt.locationTypeId,l.fullname";
		System.out.println(qry);
		List list = sc.getCustomQueryService().getDataBySQL(qry);
		
		List<Map<String, String>> locations = new ArrayList<Map<String,String>>();

		for (Object object : list) {
			HashMap<String, String> loc = new HashMap<String, String>();
			Object[] oar = (Object[]) object;
			loc.put("locationId", oar[0].toString());
			loc.put("name", oar[1].toString());
			loc.put("fullname", oar[2].toString());
			locations.add(loc);
		}		
		return locations;
	}

	public List<Map<String, Object>> getLocationHierarchy (Map<String, String> params) {
		String parentId = params.get("parentId");
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
		
		List<Location> list = sc.getCustomQueryService().getDataByHQL("FROM Location WHERE locationType.locationTypeId NOT IN (4,5,6,7) AND "+(!StringUtils.isEmptyOrWhitespaceOnly(parentId)?" locationId = "+parentId:" parentLocation IS NULL"));
	
		List<Map<String, Object>> locations = new ArrayList<Map<String,Object>>();

		for (Location object : list) {
			locations.add(getChild(object));
		}

		System.out.println(locations);
		return locations;
	}
	
	private Map<String, Object> getChild(Location location){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("id", location.getLocationId());
		item.put("text", location.getName());

		if(location.getChildLocations().size() > 0){
			List children = new ArrayList();
			for (Location chl : location.getChildLocations()) {
				children.add(getChild(chl));
			}
			item.put("children", children);
		}
		
		return item;
	}
	
	public Map<String, Object> getContactNumberList(Map<String, String> params) {
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
		String role = params.get(DWRParamsGeneral.entityRole.name());
		String center = params.get(DWRParamsGeneral.vaccinationCenter.name());

        Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
		Integer totalCount = 0;
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly(role)){
				int pageNumber = params.get("page") == null ? 0 : Integer.parseInt(params.get("page"))-1;
				int pageSize = params.get("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(params.get("rows"));
				
				String centerFilter = "";//StringUtils.isEmptyOrWhitespaceOnly(center)?"":" AND cid.identifier LIKE '"+center+"%'";
				totalCount = Integer.parseInt(sc.getCustomQueryService().getDataBySQL("SELECT count(*) FROM contactnumber c JOIN idmapper i ON c.mappedId=i.mappedId JOIN role r ON r.roleId=i.roleId WHERE roleName IN ('"+role+"') "+centerFilter+"").get(0).toString());
				String qry = "SELECT c.contactNumberId, cid.identifier, c.number, r.roleName, c.numberType,cid.mappedid FROM contactnumber c JOIN idmapper cidm ON c.mappedId=cidm.mappedId JOIN identifier cid ON cidm.mappedId = cid.mappedId AND cid.preferred = TRUE JOIN role r ON r.roleId=cidm.roleId WHERE roleName IN ('"+role+"') "+centerFilter+" ORDER BY cid.identifier DESC,c.number,c.numberType LIMIT "+(pageNumber*pageSize)+" , "+pageSize+"";
				List cons = sc.getCustomQueryService().getDataBySQL(qry);
		        for (Object object : cons) {
					Object[] objarr = (Object[]) object;
					Map<String,Object> item = new HashMap<String,Object>();
					
					item.put(DGUserSmsFieldNames.contactNumberId.name(), objarr[0]==null?"":objarr[0]);
					item.put(DGUserSmsFieldNames.programId.name(), objarr[1]==null?"":objarr[1]);
					item.put(DGUserSmsFieldNames.number.name(), objarr[2]==null?"":objarr[2]);
					item.put(DGUserSmsFieldNames.roleName.name(), objarr[3]==null?"":objarr[3]);
					item.put(DGUserSmsFieldNames.numberType.name(), objarr[4]==null?"":objarr[4]);
					item.put(DGUserSmsFieldNames.mappedId.name(), objarr[5]==null?"":objarr[5]);

					items.add(item);
				}
		        
		      
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		map.put("rows", items);
	    map.put("total", totalCount);
	    //System.out.println(map);
		return map;
	}
}
