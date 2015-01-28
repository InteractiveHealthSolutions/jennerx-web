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
import org.ird.unfepi.model.Screening;
import org.ird.unfepi.utils.date.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewScreeningsController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			List<Screening> list =new ArrayList<Screening>();
			
			String childIdName = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childidname"))) ? null : req.getParameter("childidname");
			//String epiNum = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("epinumber"))) ? null : req.getParameter("epinumber");		
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	
			Date screeningDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("screeningDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("screeningDatefrom"));	
			Date screeningDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("screeningDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("screeningDateto"));	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			if(action == null && pagerOffset == null){//page is accessed first time
				screeningDatefrom = new Date(DateUtils.truncateDatetoDate(new Date()).getTime()-(1000*60*60*24*7L));
				screeningDateto = new Date();
				list = sc.getChildService().findScreeningByCriteria(null, null, screeningDatefrom, screeningDateto, true, 0, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, new String[]{"idMapper", "broughtByRelationship"});
				
				/*req.setAttribute("searchlog", "All Screenings");*/
			}
			else {
				int startRecord = 0;
				if(action != null && action.trim().equalsIgnoreCase("search"))
				{//new search display from 0
					/*req.setAttribute("searchlog", "child ID:"+childIdName+" , Epi Num :"+epiNum+
							" , vacciz : "+lastSearchFollowupstatusNotchked+" "+lastSearchVaccinator+
							" , clinic :"+lastSearchScreeningDateto+" , current cell :"+lastSearchVaccCenter+" , Arm :"+lastSearchScreeningDatefrom+" , MR Num :"+lastSearchMrNum);
	*/			}
				else if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
					/*req.setAttribute("searchlog", req.getParameter("searchlog"));*/
				}
				
				if(childIdName != null){
					List<Screening> obj = sc.getChildService().findScreeningByProgramId(childIdName, true, new String[]{"idMapper", "broughtByRelationship"});
					if(obj != null) list.addAll(obj);
				}
				else {
					list = sc.getChildService().findScreeningByCriteria(vaccinator, vaccCenter, screeningDatefrom, screeningDateto, true, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, new String[]{"idMapper", "broughtByRelationship"});
				}
			}
			
			req.setAttribute("lastSearchChildIdName", /*childIdName==null?"":*/childIdName);
			//req.setAttribute("lastSearchEpiNum", /*epiNum==null?"":*/epiNum);
			req.setAttribute("lastSearchVaccCenter", /*vaccCenter==null?"":*/vaccCenter);
			req.setAttribute("lastSearchVaccinator", /*vaccinator==null?"":*/vaccinator);
			req.setAttribute("lastSearchScreeningDatefrom", screeningDatefrom != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(screeningDatefrom):screeningDatefrom);
			req.setAttribute("lastSearchScreeningDateto", screeningDateto != null ? new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).format(screeningDateto):screeningDateto);

			if(sc.getChildService().LAST_QUERY_TOTAL_ROW_COUNT(Screening.class)!=null){
			totalRows = sc.getChildService().LAST_QUERY_TOTAL_ROW_COUNT(Screening.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			model.put("vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));
			model.put("vaccinators", sc.getVaccinationService().getAllVaccinator(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}));
			
			List<Map<String, Object>> scrEnrollmentMap = new ArrayList<Map<String,Object>>();
			for (Screening screening : list) {
				Map<String, Object> hm = new HashMap<String, Object>();
				hm.put("screening", screening);
				if(screening.getMappedId() != null){
					hm.put("isEnrolled", (sc.getCustomQueryService().getDataByHQL("select mappedId from Child where mappedId="+screening.getMappedId()).size()>0));
				}
				
				scrEnrollmentMap.add(hm);
			}
			model.put("scrEnrollmentMap", scrEnrollmentMap);
			req.setAttribute("totalRows", totalRows);
			
			return new ModelAndView("viewScreenings","model",model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}
