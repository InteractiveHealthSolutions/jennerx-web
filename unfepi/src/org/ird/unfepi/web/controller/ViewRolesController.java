package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewRolesController extends DataDisplayController {
	
	ViewRolesController(){
		super("dataForm", new  DataSearchForm("role", "Roles", SystemPermissions.VIEW_ROLES, true));
	}
	
	@RequestMapping(value="/viewRoles", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<Role> list =new ArrayList<Role>();
			
			String roleName = UnfepiUtils.getStringFilter(SearchFilter.ROLE_NAME, req);;

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			if(action == null && pagerOffset == null){//page is accessed first time
				list = sc.getUserService().getAllRoles(true, new String[]{"createdByUserId", "lastEditedByUserId", "permissions"});
			}
			else {
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
				
				list = sc.getUserService().getRolesByName(roleName, true, new String[]{"createdByUserId", "lastEditedByUserId", "permissions"});
			}
			
			totalRows=list.size();
				
			addModelAttribute(model, SearchFilter.ROLE_NAME.FILTER_NAME(), roleName);

			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");
		}
		finally{
			sc.closeSession();
		}
	}

}
