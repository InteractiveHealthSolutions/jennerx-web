package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinationCalendar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewVaccinationCalendarController extends DataDisplayController {
	
	ViewVaccinationCalendarController(){
		super("dataForm", new  DataViewForm("vaccination_calendar", "Vaccination Calendar", SystemPermissions.VIEW_HEALTH_ROUND, false));
	}
	
	@RequestMapping(value="/viewVaccinationCalendar.htm", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new LinkedHashMap<String, Object>();
		
		String calendarId = req.getParameter("calendarId");
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(calendarId)){
			
			List<LinkedHashMap> vaccinesL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine ORDER BY vaccineId");   /*where vaccine_entity like 'CHILD%'*/
			List<LinkedHashMap> vaccineGapsL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vg.vaccineId , group_concat(concat(vgt.name ,'(',vg.value,' ',vg.gapTimeUnit, ')') ORDER BY vg.vaccineGapTypeId SEPARATOR ',') AS gaps FROM vaccinegap vg  JOIN vaccinegaptype vgt ON vg.vaccineGapTypeId = vgt.vaccineGapTypeId WHERE vg.vaccinationcalendarId = "+ calendarId +" GROUP BY vg.vaccineId ORDER BY vg.vaccineId;"); 
			List<LinkedHashMap> prerequisiteL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vp.vaccineId, group_concat(v.name) AS prerequisites FROM vaccineprerequisite vp JOIN vaccine v on vp.vaccineprerequisiteId = v.vaccineId WHERE vaccinationcalendarId = "+ calendarId +" GROUP BY vaccineId ");
			
			Map<Map, Map<String, String>> data = new LinkedHashMap<Map, Map<String,String>>();
			
			for (HashMap vaccine : vaccinesL) {
				for (HashMap gap : vaccineGapsL) {
					Map<String, String> vaccineGaps = new LinkedHashMap<String, String>();
					if (vaccine.get("vaccineId") == gap.get("vaccineId")) {
						String gapString = (String) gap.get("gaps");
						Pattern pattern = Pattern.compile("([A-Za-z\\s]+Gap)\\((\\d+\\s[A-Z]+)\\)");
						Matcher matcher = pattern.matcher(gapString);
						while (matcher.find()) {
							vaccineGaps.put(matcher.group(1).trim(), matcher.group(2).trim());
						}
						data.put(vaccine, vaccineGaps);
					}
				}
			}
			model.put("data", data);
			model.put("prerequisites", prerequisiteL);
			addModelAttribute(model, "calenderId", calendarId);
		}
		return showForm(model);
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			List<VaccinationCalendar> vaccinationCalendars = sc.getCustomQueryService().getDataByHQL("from VaccinationCalendar");
			model.addAttribute("vaccinationCalendars", vaccinationCalendars);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
	
}
