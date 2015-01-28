package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.WebGlobals.DGInconsistenciesFieldNames;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UserSessionUtils;

public class DWRAdminTaskService {
	/*public String updateIrSetting(String settingName,String newValue) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Your session has expired . Please login again.";
		}
	
		IRSettingValidator valid=new IRSettingValidator();
		if(valid.validateSettingValue(settingName, newValue)){
			boolean isupdated=Context.updateSetting(settingName, newValue,user);		
			if(!isupdated){
				return "setting was not updated due to some reason. check log for details.";
			}
			return "setting was updated successfully.";
		}else{
			return valid.ERROR_MESSAGE;
		}
	}*/
	
	public Map<String, Object> getSystemInconsistencies(Map<String, String> params) {
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
		
		 Map<String,Object> map = new HashMap<String,Object>();
			List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			try{
				List cons = sc.getCustomQueryService().getDataBySQL(GlobalParams.SQL_inconsistenciesList);
		        for (Object object : cons) {
					Object[] objarr = (Object[]) object;
					Map<String,Object> item = new HashMap<String,Object>();
					
					item.put(DGInconsistenciesFieldNames.uniqueName.name(), objarr[0]==null?"":objarr[0]);
					
					String rowCount = objarr[1]==null?"":sc.getCustomQueryService().getDataBySQL(objarr[1].toString()).get(0).toString();
					item.put(DGInconsistenciesFieldNames.rowCount.name(), rowCount);
					
					item.put(DGInconsistenciesFieldNames.description.name(), objarr[2]==null?"":objarr[2]);

					items.add(item);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				sc.closeSession();
			}
			map.put("rows", items);
		    //System.out.println(map);
			return map;
	}
}
