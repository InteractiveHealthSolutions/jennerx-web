package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewUsersController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<User> list =new ArrayList<User>();
			
			String userID = UnfepiUtils.getStringFilter(SearchFilter.USERNAME, req);
			String partOfName = UnfepiUtils.getStringFilter(SearchFilter.NAME_PART, req);
			String programId = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			UserStatus userstatus = (UserStatus) UnfepiUtils.getEnumFilter(SearchFilter.USER_STATUS, UserStatus.class, req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			if(action == null && pagerOffset == null){//page is accessed first time
				list = sc.getUserService().findUserByCriteria(programId, null, partOfName, userstatus, false, 0, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, true, true, false, new String[]{"createdByUserId", "lastEditedByUserId"});
			}
			else {
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
				
				if(userID != null){
					User obj = sc.getUserService().findUserByUsername(userID, true, true, true, false, new String[]{"createdByUserId", "lastEditedByUserId"});
					if(obj != null) list.add(obj);
				}
				else {
					list = sc.getUserService().findUserByCriteria(programId, null, partOfName, userstatus, false, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, true, true, false, new String[]{"createdByUserId", "lastEditedByUserId"});
				}
			}
			
			if(sc.getUserService().LAST_QUERY_TOTAL_ROW_COUNT(User.class)!=null){
			totalRows = sc.getUserService().LAST_QUERY_TOTAL_ROW_COUNT(User.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			
			addModelAttribute(model, SearchFilter.USERNAME.FILTER_NAME(), userID);
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), partOfName);
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), programId);
			addModelAttribute(model, SearchFilter.USER_STATUS.FILTER_NAME(), userstatus);

			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
}
