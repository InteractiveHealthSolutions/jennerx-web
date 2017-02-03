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
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainPageController {
	
	@RequestMapping(value="/mainpage", method=RequestMethod.GET)
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

			model.put("avgEnrollmentsPerWeek", (week==null||totalEnrollments==null)?null:new Float(totalEnrollments/(week+0.0)));
			model.put("avgSuccessfulEventsPerWeek", (week==null||totalSuccessEvnts==null)?null:new Float(totalSuccessEvnts/(week+0.0)));
			
			List<Round> roundList = sc.getCustomQueryService().getDataByHQL("from Round");
			model.put("roundList", roundList);
			
			List<VaccinationCenter> siteList = sc.getCustomQueryService().getDataByHQL("from VaccinationCenter");
			model.put("siteList", siteList);
			
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
