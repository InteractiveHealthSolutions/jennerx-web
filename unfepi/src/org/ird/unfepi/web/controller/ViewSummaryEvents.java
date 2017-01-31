package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.utils.UnfepiUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewSummaryEvents extends DataDisplayController{
	
	ViewSummaryEvents(){
		super("dataForm", new DataSearchForm("summary_events", "Summary Events", SystemPermissions.VIEW_SUMMARY_EVENTS, false));
	}
	
	@RequestMapping(value="/viewSummaryEvents", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int totalRows = 0;
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new HashMap<String, Object>();
		
		try {
			String healthProgramId = UnfepiUtils.getStringFilter(SearchFilter.HEALTH_PROGRAM, req);
			String siteMappedId = UnfepiUtils.getStringFilter(SearchFilter.SITE, req);
			
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");		
			
//			System.out.println(healthProgramId + "  " + siteMappedId );
			
			if(healthProgramId != null && siteMappedId != null){
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
//				List<Integer> rounds = sc.getCustomQueryService().getDataByHQL("select roundId from Round where healthProgramId = " + healthProgramId);
//				if(rounds.size() > 0){
					
					String mainQuery =  "SELECT "
							+" vct.name 'siteName'"
							+", TIMESTAMPDIFF(DAY,r.startDate, dateEncounterEntered) 'day'"
							+", DATE(dateEncounterEntered) 'dateEncounterEntered'"
							+", count(dateEncounterEntered) 'count'"
							+", GROUP_CONCAT(p1id) 'p1id'"
							+", GROUP_CONCAT(encounterType) 'encounterType' "
							+", group_concat(TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered)) 'age'"
							+", result.roundId"
							+", result.site";
					
					String query = ""
							+", SUM(IF(encounterType='ENROLLMENT' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 0 AND 11),1,0)) 'ENROLLMENT_0_11'"
							+", SUM(IF(encounterType='ENROLLMENT' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 12 AND 23),1,0)) 'ENROLLMENT_12_23'"
							+", SUM(IF(encounterType='ENROLLMENT' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 34 AND 59),1,0)) 'ENROLLMENT_24_59'"
							+", SUM(IF(encounterType='FOLLOWUP' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 0 AND 11),1,0)) 'FOLLOWUP_0_11'"
							+", SUM(IF(encounterType='FOLLOWUP' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 12 AND 23),1,0)) 'FOLLOWUP_12_23'"
							+", SUM(IF(encounterType='FOLLOWUP' AND (TIMESTAMPDIFF(MONTH, c.birthdate, e.dateEncounterEntered) BETWEEN 24 AND 59),1,0)) 'FOLLOWUP_24_59'"
							+" FROM encounter e"
							+" LEFT JOIN child c ON e.p1id = c.mappedId"
							+" LEFT JOIN ("
							+" SELECT  encounterId , p1id , p2id "
							+" , group_concat(distinct(if(element like '%ROUND%' ,value,null)))'roundId' "
							+" , group_concat(distinct(if(element like 'VISIT_DATE' ,value,null)))'visitdate' "
							+" , group_concat(if(element like 'SITE_MAPPED_ID',value,null))'site' "
							+" FROM encounterresults group by encounterId , p1id , p2id"
							+" ) result USING ( encounterId , p1id , p2id )"
							+" LEFT JOIN round r ON (result.roundId = r.roundId)"
							+" LEFT JOIN vaccinationcenter vct on (result.site = vct.mappedId)"
							+" where encounterType IN( 'ENROLLMENT' , 'FOLLOWUP')"
							+" AND result.roundId IN (select roundId from round where healthProgramId = " + healthProgramId + ") "
							+ "AND  site = "+ siteMappedId ;
							
					mainQuery += query +" group by dateEncounterEntered, result.site, result.roundId "
							+ " order by result.site, result.roundId, dateEncounterEntered ";
					
//					System.out.println(mainQuery);
					
					totalRows = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery).size();			
					
					mainQuery += " limit "+startRecord +", 20";
					List<HashMap> report = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery);
					
					String querytotal = "SELECT"
							+ " SUM(IF(encounterType='ENROLLMENT',1,0)) 'ENROLLMENT' "
							+ ",SUM(IF(encounterType='FOLLOWUP',1,0)) 'FOLLOWUP' "
							+ query;
					
//					System.out.println(querytotal);
					
//					List<HashMap> recordsum = sc.getCustomQueryService().getDataBySQLMapResult(querytotal);
//					System.out.println(recordsum);
					
					addModelAttribute(model, "recordsum", sc.getCustomQueryService().getDataBySQLMapResult(querytotal));
					addModelAttribute(model, "report", report);
//				}	
			}	
			
//			JSONArray jarray = new JSONArray();
//			List<HashMap> sites = sc.getCustomQueryService().getDataBySQLMapResult("select * from VaccinationCenter");
//			for (HashMap hashMap : sites) {
//				JSONObject jobj = new JSONObject(hashMap);
//				jarray.put(jobj);
//			}
//			addModelAttribute(model, "sitesJ", jarray);
//			addModelAttribute(model, "sites", sc.getCustomQueryService().getDataBySQLMapResult("select * from VaccinationCenter"));
			addModelAttribute(model, "healthprograms", sc.getCustomQueryService().getDataByHQL("from HealthProgram order by name"));
			addModelAttribute(model, "rounds", sc.getCustomQueryService().getDataByHQL("from Round"));
			addModelAttribute(model, SearchFilter.HEALTH_PROGRAM.FILTER_NAME(), healthProgramId);
			addModelAttribute(model, SearchFilter.SITE.FILTER_NAME(), siteMappedId);
			addModelAttribute(model, "siteMappedId", siteMappedId);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
			
		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");
		} finally{
			sc.closeSession();
		}		
	}
}
