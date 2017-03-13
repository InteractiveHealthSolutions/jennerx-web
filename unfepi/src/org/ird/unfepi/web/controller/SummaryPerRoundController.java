package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SummaryPerRoundController {
	
	@RequestMapping(value="/summaryPerRound", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getsummaryPerArea(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ServiceContext sc = Context.getServices();
		
		String healthprogram = req.getParameter("program");
		String round = req.getParameter("round");
		int pageNumber =  req.getParameter("page") == null ?0 : Integer.parseInt(req.getParameter("page"))-1;
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");
		int pageSize = req.getParameter("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(req.getParameter("rows"));
		
		System.out.println(" healthprogram " + healthprogram + " round " + round);
		System.out.println("pageNumber "+ pageNumber + " sort " + sort + " order " +order + " pageSize " + pageSize);
		
		JSONObject response = new JSONObject();
		try {
			if(healthprogram != null  && round != null){
			Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId =" + healthprogram).get(0);
			String calenderName = (String) sc.getCustomQueryService().getDataByHQL("select shortName from VaccinationCalendar where calenderId = " + calendarId).get(0);
			String vaccines = "SELECT DISTINCT  vaccine.shortName FROM vaccine JOIN vaccinegap ON vaccine.vaccineId = vaccinegap.vaccineId "
						 + "WHERE vaccine.vaccine_entity LIKE 'CHILD_%' AND vaccinegap.vaccinationcalendarId = " + calendarId + " ORDER BY standardOrder";

			
			List<String> vaccine = sc.getCustomQueryService().getDataBySQL(vaccines);
			
			JSONArray columns = new JSONArray();
			JSONArray sub = new JSONArray();
			sub.put(new JSONObject().put("field", "area").put("title", "area").put("width", 100));
			int tablewidth = 100 ;
			
			
			String query =   " select  "
					+ " (CASE WHEN lt.typeName LIKE 'Commune' THEN l.locationId WHEN lt.typeName LIKE 'Aire de sante' THEN l.parentLocation ELSE NULL END) 'areaId' "
					+ " ,(CASE WHEN lt.typeName LIKE 'Commune' THEN l.name WHEN lt.typeName LIKE 'Aire de sante' THEN pl.name ELSE NULL END) 'area' ";
			
			for (String name : vaccine) {
				query += ",COUNT( CASE WHEN va.name LIKE '"+name+"%' THEN v.vaccineId ELSE NULL END) "+name+"  ";
				sub.put(new JSONObject().put("field", name).put("title", name).put("width", 80));
				tablewidth = tablewidth + 80;
			}
			columns.put(sub);
			
			query	+=" from vaccinationcenter vc "
					+ " LEFT JOIN vaccination v on vc.mappedId=v.vaccinationCenterId "
					+ " LEFT JOIN vaccine va ON v.vaccineId=va.vaccineId   "
					+ " LEFT JOIN identifier i ON vc.mappedId = i.mappedId "
					+ " LEFT JOIN location l ON i.locationId = l.locationId "
					+ " LEFT JOIN locationtype lt ON l.locationType = lt.locationTypeId "
					+ " LEFT JOIN location pl ON l.parentLocation = pl.locationId "
					+ " where vc.mappedId in (SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ healthprogram +" and isActive = true) "
					+ " and v.vaccinationStatus = 'VACCINATED' "
					+ " AND v.vaccineId IN (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = 1)  "
					+ " and v.roundId = "+ round 
					+ " GROUP BY area";
			
			int total = sc.getCustomQueryService().getDataBySQLMapResult(query).size();
			query += "  LIMIT " + (pageNumber*pageSize) + " , " + pageSize;
			
			List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
//					System.out.println(new JSONObject((HashMap)object).toString());
			}	
			
			response.put("rows", data);
			response.put("total", total);
			response.put("columns", columns);
			response.put("tablewidth", tablewidth);
			response.put("calender",calenderName);
			System.out.println(response.toString());
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}

}
