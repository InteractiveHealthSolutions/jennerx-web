package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewEncountersController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<Encounter> list =new ArrayList<Encounter>();
			
			String peronid = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String encounterType = UnfepiUtils.getStringFilter(SearchFilter.ENCOUNTER_TYPE, req);
			Date enteredDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date enteredDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			DataEntrySource dataentrysource = (DataEntrySource) UnfepiUtils.getEnumFilter(SearchFilter.DATA_ENTRY_SOURCE, DataEntrySource.class, req);	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(action == null && pagerOffset == null){//page is accessed first time
				enteredDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				enteredDateto = new Date();
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			list = sc.getEncounterService().findEncounterByCriteria(null, peronid, null, encounterType, dataentrysource, enteredDatefrom, enteredDateto, true, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, new String[]{"p1", "p2", "createdByUser"});
			
			if(sc.getEncounterService().LAST_QUERY_TOTAL_ROW_COUNT(Encounter.class)!=null){
			totalRows = sc.getEncounterService().LAST_QUERY_TOTAL_ROW_COUNT(Encounter.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), peronid);
			addModelAttribute(model, SearchFilter.ENCOUNTER_TYPE.FILTER_NAME(), encounterType);
			addModelAttribute(model, SearchFilter.DATA_ENTRY_SOURCE.FILTER_NAME(), dataentrysource);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(enteredDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(enteredDateto));

			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}

}
