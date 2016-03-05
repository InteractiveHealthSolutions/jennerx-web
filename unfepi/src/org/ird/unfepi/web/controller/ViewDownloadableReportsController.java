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
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewDownloadableReportsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<DownloadableReport> list =new ArrayList<DownloadableReport>();
			
			String downloadablename = UnfepiUtils.getStringFilter(SearchFilter.NAME_PART, req);
			String typedo = UnfepiUtils.getStringFilter(SearchFilter.TYPE, req);
			Date createdDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date createdDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(action == null && pagerOffset == null){//page is accessed first time
				createdDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				createdDateto = new Date();
			}
			else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			list = sc.getReportService().findDownloadableReportByCritria(downloadablename, null, typedo, null, null, createdDatefrom, createdDateto, true, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, null);
			
			if(sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(DownloadableReport.class)!=null){
				totalRows = sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(DownloadableReport.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), downloadablename);
			addModelAttribute(model, SearchFilter.TYPE.FILTER_NAME(), typedo);
			addModelAttribute(model, SearchFilter.DATE1_FROM.FILTER_NAME(), UnfepiUtils.setDateFilter(createdDatefrom));
			addModelAttribute(model, SearchFilter.DATE1_TO.FILTER_NAME(), UnfepiUtils.setDateFilter(createdDateto));

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
