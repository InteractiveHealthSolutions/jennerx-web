package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewDailySummaryController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<DailySummary> list =new ArrayList<DailySummary>();
			
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	
			Date summaryDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("summaryDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("summaryDatefrom"));	
			Date summaryDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("summaryDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("summaryDateto"));	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			int startRecord = 0;

			if(action == null && pagerOffset == null){//page is accessed first time
				summaryDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				summaryDateto = new Date();
				/*req.setAttribute("searchlog", "All DailySummarys");*/
			}
			else {
				if(action != null && action.trim().equalsIgnoreCase("search"))
				{//new search display from 0
					/*req.setAttribute("searchlog", "child ID:"+childIdName+" , Epi Num :"+epiNum+
							" , vacciz : "+lastSearchFollowupstatusNotchked+" "+lastSearchVaccinator+
							" , clinic :"+lastSearchDailySummaryDateto+" , current cell :"+lastSearchVaccCenter+" , Arm :"+lastSearchDailySummaryDatefrom+" , MR Num :"+lastSearchMrNum);
	*/			}
				else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
					/*req.setAttribute("searchlog", req.getParameter("searchlog"));*/
				}
			}
			
			list = sc.getReportService().findDailySummaryByCriteria(vaccCenter, vaccinator, summaryDatefrom, summaryDateto, true, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, null);

			req.setAttribute("lastSearchVaccCenter", /*vaccCenter==null?"":*/vaccCenter);
			req.setAttribute("lastSearchVaccinator", /*vaccinator==null?"":*/vaccinator);
			req.setAttribute("lastSearchDailySummaryDatefrom", summaryDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(summaryDatefrom):summaryDatefrom);
			req.setAttribute("lastSearchDailySummaryDateto", summaryDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(summaryDateto):summaryDateto);

			if(sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(DailySummary.class)!=null){
			totalRows = sc.getReportService().LAST_QUERY_TOTAL_ROW_COUNT(DailySummary.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			
			List<Map<String, Object>> dsumMap = new ArrayList<Map<String,Object>>();
			for (DailySummary dsum : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("dailysummary", dsum);
				List<DailySummaryVaccineGiven> vcgvnlis = sc.getReportService().findDailySummaryVaccineGivenByCriteria(null, null, dsum.getSerialNumber(), null, true, 0, Integer.MAX_VALUE, null);
				for (DailySummaryVaccineGiven vcgvn : vcgvnlis) {
					map.put(vcgvn.getVaccineName().toUpperCase(), vcgvn.getQuantityGiven());
				}
				
				dsumMap.add(map);
			}
			
			model.put("dailysummap", dsumMap);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewDailySummaries","model",model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}

}
