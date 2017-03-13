package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.HealthProgram;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DailyReportController {

	@RequestMapping(value="/dailyreportperdose", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getreport_perdose(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ServiceContext sc = Context.getServices();
		
		String healthProgramId = req.getParameter("program");
		String roundId = req.getParameter("round");
		String siteId = req.getParameter("site");
		String date = req.getParameter("date");
		
		if(date != null && !date.equals("")){
			date = WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date));
		}
		
//		System.out.println(healthProgramId +" "+ roundId +" "+ siteId +" "+ date );
		
		HashMap map = new HashMap();
		JSONObject response = new JSONObject();
		
		try {
			if(healthProgramId != null && healthProgramId.length() >0){
			
			HealthProgram hp = (HealthProgram) sc.getCustomQueryService().getDataByHQL("from HealthProgram where programId = "+healthProgramId).get(0);
			
			String query = "SELECT vc.name vaccine,  "
					+"SUM(CASE e.ageGroup WHEN '0-11' THEN 1 ELSE 0 END) '0-11', "
					+"SUM(CASE e.ageGroup WHEN '12-23' THEN 1 ELSE 0 END) '12-23' , "
					+"SUM(CASE e.ageGroup WHEN '24-59' THEN 1 ELSE 0 END) '24-59' , "
					+"COUNT(e.vaccineId) total "
					+"FROM vaccine vc "
					+"LEFT JOIN vaccinegap vg ON vg.vaccineId=vc.vaccineId AND vg.vaccineGapTypeId=1 and vg.vaccinationcalendarId = "+hp.getVaccinationcalendarId() +" "
					+"LEFT JOIN ( "
					+"        SELECT ag.name ageGroup, c.mappedId,v.* FROM vaccination v "
					+"        JOIN child c ON c.mappedId=v.childId "
					+"        JOIN (SELECT 0 start, 365 end, '0-11' name UNION "
					+"                 SELECT 366 start, 730 end, '12-23' name UNION "
					+"                 SELECT 731 start, 1830 end, '24-59' name "
					+ ") ag ON TIMESTAMPDIFF(DAY,c.birthdate,v.vaccinationDate) BETWEEN ag.start AND ag.end "
					+"        WHERE vaccinationStatus='VACCINATED' and voided = false ";
					
					if(roundId != null && roundId.length() > 0){
						query += "  AND roundId = "+roundId;
					} else {
						query += "  AND roundId IN (SELECT roundId FROM round where healthProgramId = "+healthProgramId+")"; 
					}
					
					if(siteId != null && siteId.length() > 0){
						query += "  AND vaccinationCenterId = "+siteId;
					}
					
					if(date != null && !date.equals("")){
						query += " AND vaccinationDate like '"+date+"%'";
					}
					
					query += ") e ON e.vaccineId=vc.vaccineId "
						  +"where vc.vaccineId in (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = "+hp.getVaccinationcalendarId() +") "
					      +"GROUP BY vc.name ORDER BY vc.standardOrder ";

			JSONArray rows =  new JSONArray();
			rows.put(new JSONObject().put("name", "vaccine"));
			rows.put(new JSONObject().put("name", "0-11"));
			rows.put(new JSONObject().put("name", "12-23"));
			rows.put(new JSONObject().put("name", "24-59"));
			rows.put(new JSONObject().put("name", "total"));
			response.put("row", rows);
			
			List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
//					System.out.println(new JSONObject((HashMap)object).toString());
			}
			response.put("data", data);
			response.put("calender", hp.getVaccinationCalendar().getShortName());
			
			}
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}
	
	@RequestMapping(value="/dailyreportalldose", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getreport_alldose(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		String healthProgramId = req.getParameter("program");
		String roundId = req.getParameter("round");
		String siteId = req.getParameter("site");
		String date = req.getParameter("date");
		
		if(date != null && !date.equals("")){
			date = WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date));
		}
		
//		System.out.println("dailyreportalldose  "+healthProgramId +" "+ roundId +" "+ siteId +" "+ date );
		
		ServiceContext sc = Context.getServices();
		HashMap map = new HashMap();
		JSONObject response = new JSONObject();
		
		try {
			if(healthProgramId != null && healthProgramId.length() >0){
				
			HealthProgram hp = (HealthProgram) sc.getCustomQueryService().getDataByHQL("from HealthProgram where programId = "+healthProgramId).get(0);
				
			String query = "SELECT vc.shortName vaccine,  "
					+"SUM(CASE e.ageGroup WHEN '0-11' THEN 1 ELSE 0 END) '0-11', "
					+"SUM(CASE e.ageGroup WHEN '12-23' THEN 1 ELSE 0 END) '12-23' , "
					+"SUM(CASE e.ageGroup WHEN '24-59' THEN 1 ELSE 0 END) '24-59' , "
					+"COUNT(e.vaccineId) total "
					+"FROM vaccine vc "
					+"LEFT JOIN vaccinegap vg ON vg.vaccineId=vc.vaccineId AND vg.vaccineGapTypeId=1 and vg.vaccinationcalendarId = "+hp.getVaccinationcalendarId() +" "
					+"LEFT JOIN ( "
					+"        SELECT ag.name ageGroup, c.mappedId,v.* FROM vaccination v "
					+"        JOIN child c ON c.mappedId=v.childId "
					+"        JOIN (SELECT 0 start, 365 end, '0-11' name UNION "
					+"                 SELECT 366 start, 730 end, '12-23' name UNION "
					+"                 SELECT 731 start, 1830 end, '24-59' name "
					+ ") ag ON TIMESTAMPDIFF(DAY,c.birthdate,v.vaccinationDate) BETWEEN ag.start AND ag.end "
					+"        WHERE vaccinationStatus='VACCINATED' and voided = false ";
			
			
			if(roundId != null && roundId.length() > 0){
				query += "  AND roundId = " + roundId;
			}else {
				query += "  AND roundId IN (SELECT roundId FROM round where healthProgramId = "+healthProgramId+")"; 
			}
			if(siteId != null && siteId.length() > 0){
				query += "  AND vaccinationCenterId = " + siteId;
			}
			if(date != null && !date.equals("")){
				query += " AND vaccinationDate like '"+date+"%'";
			}
			
			query += ") e ON e.vaccineId=vc.vaccineId "
					+"where vc.vaccineId in (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId = "+hp.getVaccinationcalendarId() +") "
					+"GROUP BY vc.shortName ORDER BY vc.standardOrder ";

			JSONArray rows =  new JSONArray();
			rows.put(new JSONObject().put("name", "vaccine"));
			rows.put(new JSONObject().put("name", "0-11"));
			rows.put(new JSONObject().put("name", "12-23"));
			rows.put(new JSONObject().put("name", "24-59"));
			rows.put(new JSONObject().put("name", "total"));
			response.put("row", rows);
			
			List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
			
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
//					System.out.println(new JSONObject((HashMap)object).toString());
			}
			response.put("data", data);
			response.put("calender", hp.getVaccinationCalendar().getShortName());
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}
	
	@RequestMapping(value="/dailyreportcounts", method={RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody String getreport_counts(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		String healthProgramId = req.getParameter("program");
		String roundId = req.getParameter("round");
		String siteId = req.getParameter("site");
		String date = req.getParameter("date");
		
		if(date != null && !date.equals("")){
			date = WebGlobals.GLOBAL_SQL_DATE_FORMAT.format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date));
		}
		
//		System.out.println("dailyreportalldose  "+healthProgramId +" "+ roundId +" "+ siteId +" "+ date );
		
		ServiceContext sc = Context.getServices();
		HashMap map = new HashMap();
		JSONObject response = new JSONObject();
		
		try {
			if(healthProgramId != null && healthProgramId.length() >0){
				
			HealthProgram hp = (HealthProgram) sc.getCustomQueryService().getDataByHQL("from HealthProgram where programId = "+healthProgramId).get(0);
				
			String query =   "SELECT  "
					 +"temp.date "
					 +", count(temp.date) 'total'"
					 +", sum(if(temp.encounterType = 'ENROLLMENT',1,0)) 'ENROLLMENT' "
					 +", sum(if(temp.encounterType = 'FOLLOWUP',1,0)) 'FOLLOWUP' "
					 +", '' ELIGIBLE "
					 +", sum(if(temp.VACCINATED >0 ,1,0)) 'VACCINATED'  "
					 +", sum(if(temp.NOT_VACCINATED >0 ,1,0)) 'CONTRAINDICATIONS' "
					 +"FROM "
					 +"(SELECT DATE(vaccinationDate) 'date', childId , e.encounterType "
					 +", sum(if(vaccinationStatus = 'VACCINATED',1,0)) 'VACCINATED' "
					 +", sum(if(vaccinationStatus = 'NOT_VACCINATED',1,0)) 'NOT_VACCINATED' "
					 +"FROM unfepi.vaccination v "
					 +"LEFT JOIN encounter e  "
					 +"ON ( DATE(vaccinationDate) = DATE(dateEncounterEntered) and v.childId = e.p1id and v.vaccinationCenterId = e.locationId) "
					 +"WHERE vaccineId in (select vaccineId from vaccinegap where vaccineGapTypeId=1 and vaccinationcalendarId ="+hp.getVaccinationcalendarId() +") "
					 +" and vaccinationStatus in ( 'VACCINATED', 'NOT_VACCINATED') and voided = false ";
			
			if(roundId != null && roundId.length() > 0){
				query += "  and roundId = " + roundId;
			}else {
				query += "  and roundId IN (SELECT roundId FROM round where healthProgramId = "+healthProgramId+")"; 
			}
			if(siteId != null && siteId.length() > 0){
				query += "  AND vaccinationCenterId = " + siteId;
			}
			if(date != null && !date.equals("")){
				query += " AND vaccinationDate like '"+date+"%'";
			}
			
			query += " group by childId , e.encounterId )temp "
					 +"group by temp.date ";
			
//			+"and roundId = 1 "
//			 +"and vaccinationCenterId = 48022 "
//			 +"and vaccinationStatus in ( 'VACCINATED', 'NOT_VACCINATED') "
//			 +"and DATE(vaccinationDate) like '2016-12-02%' "
//			 +"group by childId , e.encounterId )temp "
//			 +"group by temp.date ";
//			

			List records = sc.getCustomQueryService().getDataBySQLMapResult(query);
			
			JSONArray data =  new JSONArray();
			for (Object object : records) {
				data.put(new JSONObject((HashMap)object));
//					System.out.println(new JSONObject((HashMap)object).toString());
			}
			response.put("data", data);
			response.put("calender", hp.getVaccinationCalendar().getShortName());
			}
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "error ";
		} finally {
			sc.closeSession();
		}
	}

	@RequestMapping(value="/search/sites/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getSiteList(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
		List<String> centers = sc.getCustomQueryService().getDataBySQL("select mappedId from vaccinationcenter where mappedId in (SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ programId + " )");
		sc.closeSession();
		return centers.toString();
	}
	
	@RequestMapping(value="/search/rounds/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getRoundList(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
		List<String> rounds = sc.getCustomQueryService().getDataBySQL("select roundId from round where healthProgramId = " + programId);
		sc.closeSession();
		return rounds.toString();
	}

}
