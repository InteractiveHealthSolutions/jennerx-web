package org.ird.unfepi.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class MainPageController implements Controller{

	@SuppressWarnings("rawtypes")
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		String centersFilter = req.getParameter("centersFilter");
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			List startDatel = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_START_DATE);
			Date startDate = startDatel.size()>0?new SimpleDateFormat("yyyy-MM-dd").parse(startDatel.get(0).toString()):null;
			model.put("projectStartDate", startDate);
			
			Integer week = startDate == null? null : DateUtils.differenceBetweenIntervals(new Date(), startDate, TIME_INTERVAL.WEEK)+1;
			model.put("projectCurrentWeek", week);
			
			List targetEnrl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_TARGET_ENROLLMENTS);
			String targetEnr = targetEnrl.size()>0?targetEnrl.get(0).toString(): null;
			model.put("targetEnrollments", targetEnr);
			
			List targetEvntl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_TARGET_EVENTS);
			String targetEvnt = targetEvntl.size()>0?targetEvntl.get(0).toString(): null;
			model.put("targetEvents", targetEvnt);
			
			List totalEnrollmentsl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_TOTAL_ENROLLMENTS);
			Integer totalEnrollments = Integer.parseInt(totalEnrollmentsl.get(0).toString());
			
			model.put("totalEnrollments", totalEnrollments);
			
			List totalSuccessEvntsl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_VACCINATIONS_RECEIVED);
			Integer totalSuccessEvnts = Integer.parseInt(totalSuccessEvntsl.get(0).toString());
			
			model.put("successfulEvents", totalSuccessEvnts);
			//model.put("unsuccessfulEvents", arg1);
			model.put("avgEnrollmentsPerWeek", (week==null||totalEnrollments==null)?null:new Float(totalEnrollments/(week+0.0)));
			model.put("avgSuccessfulEventsPerWeek", (week==null||totalSuccessEvnts==null)?null:new Float(totalSuccessEvnts/(week+0.0)));
			//model.put("avgUnsuccessfulEventsPerWeek", arg1);
			
			/*List rscohortbygender = sc.getCustomQueryService().getDataBySQL("CALL SummaryEnrByGenderCohort2()");
			model.put("rscohortbygender", rscohortbygender);
			
			List rssummaryFupByCohorttotal = sc.getCustomQueryService().getDataBySQL("CALL SummaryFupByCohort2()");
			model.put("rssummaryFupByCohorttotal", rssummaryFupByCohorttotal);*/
			
			/*List centersummary = sc.getCustomQueryService().getDataBySQL("CALL SummaryEnrByCenterCohort2()");
			model.put("centersummary", centersummary);*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView("mainpage", "model", model);
	}
}