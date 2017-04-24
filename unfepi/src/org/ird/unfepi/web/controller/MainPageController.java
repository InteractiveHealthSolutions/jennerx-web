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
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Location;
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

			List<Round> roundList = sc.getCustomQueryService().getDataByHQL("from Round");
			model.put("roundList", roundList);
			
			List<VaccinationCenter> siteList = sc.getCustomQueryService().getDataByHQL("from VaccinationCenter");
			model.put("siteList", siteList);
			
			List<HealthProgram> healthProgramList = sc.getCustomQueryService().getDataByHQL("from HealthProgram");
			model.put("healthProgramList", healthProgramList);
			
//			Commune
			
			List<Location> locationList = sc.getCustomQueryService().getDataByHQL(
					"from Location where locationType in (select locationTypeId from LocationType where typeName like '%Commune%') ");
			model.put("locationList", locationList);
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
