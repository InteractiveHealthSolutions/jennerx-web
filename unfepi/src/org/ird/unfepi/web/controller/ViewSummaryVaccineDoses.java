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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewSummaryVaccineDoses extends DataDisplayController {

	ViewSummaryVaccineDoses(){
		super("dataForm", new  DataSearchForm("summary_vaccine_doses", "Summary Vaccine Doses", SystemPermissions.VIEW_SUMMARY_VACCINE_DOSES, false));
	}

	@RequestMapping(value="/viewSummaryVaccineDoses", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		int totalRows = 0;
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new HashMap<String, Object>();

		try {
			String healthProgramId = UnfepiUtils.getStringFilter(SearchFilter.HEALTH_PROGRAM, req);
			String siteMappedId = UnfepiUtils.getStringFilter(SearchFilter.SITE, req);
			String vaccineName = UnfepiUtils.getStringFilter(SearchFilter.VACCINE, req);

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");		

//			System.out.println(healthProgramId + "  " + siteMappedId +"  "+ vaccineName);

			if(healthProgramId != null && vaccineName != null && siteMappedId != null){
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
				}

				List<HashMap> vaccineL = sc.getCustomQueryService().getDataBySQLMapResult("select * from vaccine where shortName like '"+ vaccineName +"%' and vaccine_entity like 'CHILD_COMPULSORY'");
//				System.out.println("vaccineL size " + vaccineL.size());

				String mainQuery = "SELECT "
						+"DATE(vaccinationDate) 'date'"
						+", count(vaccinationDate) 'count'"
						+", group_concat(vaccineId) "
						+", group_concat(vaccinationRecordNum)  "
						+", group_concat(childId)  "
						+", group_concat(vaccinationStatus) "
						+", group_concat(distinct(v.roundId)) 'roundId' "
						+", group_concat(distinct(vc.name)) 'site' "
						+", TIMESTAMPDIFF(DAY, r.startDate, v.vaccinationDate) 'day'"
						+", group_concat(TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate)) 'age' ";

				String query = "";
				String vaccineId = "";
				for (HashMap hashMap : vaccineL) {	
					query += ""
						+", sum(if(vaccineId= "+ hashMap.get("vaccineId") +" and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 0 AND 11),1,0)) '"+ hashMap.get("name") +"_0_11'"
						+", sum(if(vaccineId= "+ hashMap.get("vaccineId") +" and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 12 AND 23),1,0)) '"+ hashMap.get("name") +"_12_23'"
						+", sum(if(vaccineId= "+ hashMap.get("vaccineId") +" and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 24 AND 59),1,0)) '"+ hashMap.get("name") +"_24_59'";
					
					vaccineId += hashMap.get("vaccineId") + ", " ;
				}
				query +=" FROM vaccination v "
						+"LEFT JOIN child c ON (v.childId = c.mappedId) "
						+"LEFT JOIN round r ON (v.roundId = r.roundId) "
						+"LEFT JOIN vaccinationcenter vc ON (v.vaccinationCenterId = vc.mappedId) "
						+"WHERE vaccinationStatus = 'VACCINATED' AND v.vaccineId IN (SELECT vaccineId FROM vaccine where shortName like '" + vaccineName + "') "
						+"AND v.roundId IN(SELECT roundId FROM round where healthprogramId = "+ healthProgramId + ") "
						+"AND v.vaccinationCenterId = " + siteMappedId;

				mainQuery += query +" GROUP BY DATE(v.vaccinationDate), v.vaccinationCenterId ,v.roundId  "
						+"ORDER BY v.vaccinationCenterId ,v.roundId ,DATE(v.vaccinationDate) ";

				totalRows = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery).size();
//				System.out.println(vaccineL.toString());
//				System.out.println(mainQuery);

				mainQuery += " limit "+startRecord +", 20";
				List<HashMap> report = sc.getCustomQueryService().getDataBySQLMapResult(mainQuery);

//				System.out.println(report);
				if(vaccineId != null && vaccineId.length() >0){
					vaccineId = vaccineId.substring(0, (vaccineId.length()-2));
				}
				
				String querytotal = "SELECT "
						+"  sum(if(vaccineId IN ("+vaccineId+") and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 0 AND 11),1,0)) 'total_0_11' "
						+", sum(if(vaccineId IN ("+vaccineId+") and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 12 AND 23),1,0)) 'total_12_23' "
						+", sum(if(vaccineId IN ("+vaccineId+") and (TIMESTAMPDIFF(MONTH, c.birthdate, v.vaccinationDate) BETWEEN 24 AND 59),1,0)) 'total_24_59' "
						+ query;
				
				System.out.println(querytotal);
						
				List<HashMap> recordsum = sc.getCustomQueryService().getDataBySQLMapResult(querytotal);
//				System.out.println(recordsum);

				addModelAttribute(model, "recordsum", sc.getCustomQueryService().getDataBySQLMapResult(querytotal));
				addModelAttribute(model, "vaccineL", vaccineL);
				addModelAttribute(model, "vaccineN", vaccineName);
				addModelAttribute(model, "doses", vaccineL.size());
				addModelAttribute(model, "report", report);

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
			addModelAttribute(model, "vaccines", sc.getCustomQueryService().getDataBySQL("SELECT distinct(shortName) FROM vaccine where vaccine_entity like 'CHILD_COMPULSORY' order by shortName"));

			addModelAttribute(model, SearchFilter.VACCINE.FILTER_NAME(), vaccineName);
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
