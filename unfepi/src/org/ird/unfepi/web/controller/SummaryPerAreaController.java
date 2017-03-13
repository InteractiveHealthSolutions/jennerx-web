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
public class SummaryPerAreaController {
	
	@RequestMapping(value="/search/healthprograms/{locationId}" , method=RequestMethod.GET)
	public @ResponseBody String getHealthProgramList(@PathVariable Integer locationId){
		ServiceContext sc = Context.getServices();
		List<String> healthprograms = sc.getCustomQueryService().getDataBySQL("SELECT distinct(healthProgramId) FROM centerprogram "
				+ "where vaccinationCenterId in (SELECT mappedId FROM identifier where locationId =" +locationId + " OR "
				+ " locationId IN (SELECT locationId FROM location WHERE parentLocation = " +locationId + ") )");
		sc.closeSession();
		return healthprograms.toString();
	}
	
	@RequestMapping(value="/summaryPerArea", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getsummaryPerArea(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ServiceContext sc = Context.getServices();
		
		String area = req.getParameter("area");
		String healthprogram = req.getParameter("program");
		String round = req.getParameter("round");
		int pageNumber =  req.getParameter("page") == null ?0 : Integer.parseInt(req.getParameter("page"))-1;
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");
		int pageSize = req.getParameter("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(req.getParameter("rows"));
		
//		System.out.println("area "+ area + " healthprogram " + healthprogram + " round " + round);
//		System.out.println("pageNumber "+ pageNumber + " sort " + sort + " order " +order + " pageSize " + pageSize);
		
		JSONObject response = new JSONObject();
		try {
			if(area != null && healthprogram != null  && round != null){
			Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId =" + healthprogram).get(0);
			String calenderName = (String) sc.getCustomQueryService().getDataByHQL("select shortName from VaccinationCalendar where calenderId = " + calendarId).get(0);
			String vaccines = "SELECT DISTINCT  vaccine.shortName FROM vaccine JOIN vaccinegap ON vaccine.vaccineId = vaccinegap.vaccineId "
						 + "WHERE vaccine.vaccine_entity LIKE 'CHILD_%' AND vaccinegap.vaccinationcalendarId = " + calendarId + " ORDER BY standardOrder";
			
			List<String> vaccine = sc.getCustomQueryService().getDataBySQL(vaccines);
			
			String query = "SELECT "+"TIMESTAMPDIFF(DAY,r.startDate,v.vaccinationDate) 'day', ";
			
			JSONArray columns = new JSONArray();
			JSONArray sub = new JSONArray();
			sub.put(new JSONObject().put("field", "day").put("title", "day").put("width", 50));
			sub.put(new JSONObject().put("field", "site").put("title", "site").put("width", 100));
			
			int tablewidth = 50 + 100 ;
			
			for (String name : vaccine) {
				query += " COUNT( CASE WHEN vc.name LIKE '"+name+"%' THEN v.vaccineId ELSE NULL END) "+name+",  ";
				sub.put(new JSONObject().put("field", name).put("title", name).put("width", 80));
				tablewidth = tablewidth + 80;
			}
			columns.put(sub);
					
			query	+=" c.name site  "
					+"FROM round r  "
					+"JOIN vaccination v ON v.roundId=r.roundId "
					+"LEFT JOIN vaccine vc ON v.vaccineId=vc.vaccineId  "
					+"LEFT JOIN vaccinationcenter c ON c.mappedId=v.vaccinationCenterId  "
					+"WHERE v.vaccinationStatus = 'VACCINATED'   "
					+"AND v.vaccineId IN (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = "+calendarId+")    "
					+"AND v.vaccinationCenterId in (SELECT mappedId FROM identifier where locationId = "+ area + ""
							+ " OR locationId IN (SELECT locationId FROM location WHERE parentLocation = "+ area + ")"
							+ ")    "
					+"AND r.healthProgramId = " +healthprogram+" "
					+"AND r.roundId = "+ round + " "
					+"GROUP BY day, r.roundId, v.vaccinationCenterId "
					+"ORDER BY v.vaccinationCenterId , r.roundId, day";
			
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
//			System.out.println(response.toString());
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}
	
	@RequestMapping(value="/summaryPerAreaMinMaxAge", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getsummaryPerAreaMinMaxAge(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ServiceContext sc = Context.getServices();
		String area = req.getParameter("area");
		String healthprogram = req.getParameter("program");
		String round = req.getParameter("round");
		int pageNumber =  req.getParameter("page") == null ?0 : Integer.parseInt(req.getParameter("page"))-1;
		String sort = req.getParameter("sort");
		String order = req.getParameter("order");
		int pageSize = req.getParameter("rows") == null ? WebGlobals.PAGER_PAGE_SIZE : Integer.parseInt(req.getParameter("rows"));
		
//		System.out.println("area "+ area + " healthprogram " + healthprogram + " round " + round);
//		System.out.println("pageNumber "+ pageNumber + " sort " + sort + " order " +order + " pageSize " + pageSize);
		
		JSONObject response = new JSONObject();
		try {
			if(area != null && healthprogram != null  && round != null){
				Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId =" + healthprogram).get(0);
				
				String query =" select v.shortName, min(age)'minAge', min(age)'maxAge' from vaccine v  "
						+" left join (select   "
						+" vtn.vaccineId ,  "
						+" TIMESTAMPDIFF(MONTH,ch.birthdate,vtn.vaccinationDate) age  "
						+" from vaccination vtn   "
						+" left join child ch on vtn.childId = ch.mappedId  "
						+" where vaccineId in (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = "+calendarId+")  "
						+" and vaccinationCenterId in (SELECT mappedId FROM identifier where locationId = "+area+" "
						+ " OR locationId IN (SELECT locationId FROM location WHERE parentLocation = "+ area + ")"
								+ ")  "
						+" and roundId = "+round+"  "
						+" and vaccinationStatus = 'VACCINATED' ) vr   "
						+" on vr.vaccineId = v.vaccineId  "
						+" where v.vaccineId in (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = "+calendarId+")  "
						+" group by v.shortName  "
						+" order by v.standardOrder";
				
				List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
				JSONArray data =  new JSONArray();
				for (Object object : records) {
					data.put(new JSONObject((HashMap)object));
//						System.out.println(new JSONObject((HashMap)object).toString());
				}			
				response.put("rows", data);
			}
			return response.toString();
		}catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}
	
}
