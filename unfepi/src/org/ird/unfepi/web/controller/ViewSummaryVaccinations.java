package org.ird.unfepi.web.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewSummaryVaccinations extends DataDisplayController{
	
	ViewSummaryVaccinations(){
		super("dataForm", new DataSearchForm("summary_vaccinations", "Summary Vaccinations", SystemPermissions.VIEW_SUMMARY_VACCINATIONS, false));
	}
	
	@RequestMapping(value="/siteList/{programId}" , method=RequestMethod.GET)
	public @ResponseBody String getSiteList(@PathVariable Integer programId){
		ServiceContext sc = Context.getServices();
		JSONArray jarray = new JSONArray();
		List<HashMap> sites = sc.getCustomQueryService().getDataBySQLMapResult("select * from vaccinationcenter where mappedId in "
						+ "(SELECT vaccinationCenterId FROM centerprogram WHERE healthProgramId = "+ programId + "  and isActive = true)"
						+ " order by fullName");
		for (HashMap hashMap : sites) {
			JSONObject jobj = new JSONObject(hashMap);
			jarray.put(jobj);
		}
		return jarray.toString();
	}
	
	@RequestMapping(value="/viewSummaryVaccinations", method={RequestMethod.GET,RequestMethod.POST})
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
			
			if(healthProgramId != null && siteMappedId !=null){
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}
				
				Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId =" + healthProgramId).get(0);
				String query = "SELECT DISTINCT vaccine.vaccineId , vaccine.name FROM vaccine INNER JOIN vaccinegap ON vaccine.vaccineId = vaccinegap.vaccineId "
							 + "WHERE vaccine.vaccine_entity LIKE 'CHILD_%' AND vaccinegap.vaccinationcalendarId = " + calendarId;
				
				List<HashMap> vaccine = sc.getCustomQueryService().getDataBySQLMapResult(query);
				String mainQuery = "SELECT TIMESTAMPDIFF(DAY,rnd.startDate,vac.vaccinationDate) 'day', DATE(vac.vaccinationDate) 'date', vct.name 'site' , vac.roundId, vac.vaccinationStatus,";
				for (HashMap hashMap : vaccine) {	
					mainQuery += " SUM(IF(vaccineId= "+ hashMap.get("vaccineId") +" ,1,0)) '"+ hashMap.get("name") +"',";
				}
				mainQuery += "  count(vac.vaccinationDate) total "
						+ " FROM vaccination vac "
						+ " LEFT JOIN vaccinationcenter vct on (vac.vaccinationCenterId = vct.mappedId)"
						+ " LEFT JOIN round rnd on (vac.roundId = rnd.roundId)"
						+ " WHERE vac.vaccinationStatus LIKE 'VACCINATED' AND vac.roundId IN("
						+ " SELECT rd.roundId FROM round rd where healthprogramId = "+ healthProgramId + ") "
						+ " AND vac.vaccinationCenterId = " + siteMappedId
						+ " GROUP BY vac.vaccinationDate, vac.vaccinationCenterId, vac.roundId "
						+ " ORDER BY vac.vaccinationCenterId , vac.roundId, vac.vaccinationDate";
//						+ " limit "+startRecord +", 20";
				
//				System.out.println(mainQuery);
				
				totalRows = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery).size();			
				
				mainQuery += " limit "+startRecord +", 20";
				List<HashMap> report = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery);
				
				addModelAttribute(model, "vaccines", vaccine);
				addModelAttribute(model, "report", report);
				
				for (HashMap hashMap : vaccine) {	
					String querytotal = "SELECT count(vaccineId) "
							+ "FROM vaccination "
							+ "WHERE  vaccineId = "+hashMap.get("vaccineId")
							+ " AND vaccinationStatus = 'vaccinated' "
							+ " AND vaccinationCenterId = " + siteMappedId		
							+ " AND roundId IN(SELECT rd.roundId FROM round rd where healthprogramId ="+ healthProgramId +") ;";
					
					addModelAttribute(model, hashMap.get("name").toString(), sc.getCustomQueryService().getDataBySQL(querytotal).get(0));
//					System.out.println(hashMap.get("name").toString() + "   " + sc.getCustomQueryService().getDataBySQL(querytotal).toString());
				}
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
