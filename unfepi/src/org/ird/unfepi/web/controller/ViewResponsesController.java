package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewResponsesController extends DataDisplayController {
	
	ViewResponsesController(){
		super("dataForm", new  DataSearchForm("response", "Responses", SystemPermissions.VIEW_REMINDERSMS, false));
	}
	
	public enum TabRole {
		CHILD("child", "caregiver"), 
		STOREKEEPER("storekeeper", "storekeeper"), 
		VACCINATOR("vaccinator", "vaccinator"), OTHER("other", "other"), 
		CALLS("calls", "calls");

		private String role;
		private String title;

		public String ROLE() {
			return role;
		}

		public String TITLE() {
			return title;
		}

		private TabRole(String roleName, String tabTitle) {
			role = roleName;
			title = tabTitle;
		}

		public static String findRoleFromTitle(String title) {
			for (TabRole tr : TabRole.values()) {
				if (tr.TITLE().equalsIgnoreCase(title)) {
					return tr.ROLE();
				}
			}
			return null;
		}
	}
	
	@RequestMapping(value="/viewResponses", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List list =new ArrayList();
			
			String idName = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String originator = UnfepiUtils.getStringFilter(SearchFilter.ORIGINATOR, req);
			String recipient = UnfepiUtils.getStringFilter(SearchFilter.RECIPIENT, req);
			Date receiveDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date receiveDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			
			String entityrole = req.getParameter("rolename");
			if(StringUtils.isEmptyOrWhitespaceOnly(entityrole)){
				entityrole = UnfepiUtils.getStringFilter(SearchFilter.ROLE_NAME, req);
			}
			if(StringUtils.isEmptyOrWhitespaceOnly(entityrole)){
				entityrole = TabRole.CHILD.TITLE();
			}
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			if(action == null && pagerOffset == null){//page is accessed first time
				receiveDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				receiveDateto = new Date();
			}
			else {
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
			}
			IdMapper idm = null;
			if(!StringUtils.isEmptyOrWhitespaceOnly(idName)){
				idm = sc.getIdMapperService().findIdMapper(idName);
			}
						
			String sqlFilter = null;

			Short[] roleId = null;
			ResponseType[] respType = new ResponseType[]{ResponseType.SMS};
			if(!entityrole.equalsIgnoreCase(TabRole.OTHER.TITLE())
					&& !entityrole.equalsIgnoreCase(TabRole.CALLS.TITLE())){
				roleId = new Short[]{sc.getUserService().getRole(TabRole.findRoleFromTitle(entityrole), true, null).getRoleId()};
			}
			//if other role
			else if(entityrole.equalsIgnoreCase(TabRole.OTHER.TITLE())){
				Short[] rl = (Short[]) sc.getCustomQueryService().getDataBySQL("SELECT roleId FROM role WHERE rolename IN ('"+GlobalParams.CHILD_ROLE_NAME+"','"+GlobalParams.STOREKEEPER_ROLE_NAME+"','"+GlobalParams.VACCINATOR_ROLE_NAME+"')").toArray(new Short[]{});
				sqlFilter = " (roleId NOT IN ("+IRUtils.getListAsString(Arrays.asList(rl), ",")+") OR roleId IS NULL) ";
			}
			else if(entityrole.equalsIgnoreCase(TabRole.CALLS.TITLE())){
				respType = new ResponseType[]{ResponseType.M_CALL, ResponseType.R_CALL};
			}
			
			String[] org = originator==null?null:new String[]{originator};
			String[] recp = recipient==null?null:new String[]{recipient};
			
			list = sc.getCommunicationService().getResponseByCriteria((idm == null?null:idm.getMappedId()), roleId, 
					entityrole.equalsIgnoreCase(GlobalParams.OTHER_ROLE_NAME), receiveDatefrom, receiveDateto, org, recp, 
					respType, null, null, null, null, null, 
					startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"idMapper"}, sqlFilter );
			
			Number r = sc.getCommunicationService().LAST_QUERY_TOTAL_ROW_COUNT(Response.class);
			totalRows = r==null?list.size():r.intValue();
			
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), idName);
			addModelAttribute(model, SearchFilter.ORIGINATOR.FILTER_NAME(), originator);
			addModelAttribute(model, SearchFilter.RECIPIENT.FILTER_NAME(), recipient);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(receiveDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(receiveDateto));
			addModelAttribute(model, SearchFilter.ROLE_NAME.FILTER_NAME(), entityrole);
			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView("exception");
		}
		finally{
			req.setAttribute("sc"	, sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}
	
}
