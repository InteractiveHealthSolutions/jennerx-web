package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.VaccinationCenter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewRoundVaccineController {
	
	@RequestMapping(value="/viewRoundVaccineStatus", method=RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		Map<String, Object> model=new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		try{
			Map<HealthProgram, Map<Round,List<String>>> data =new LinkedHashMap<HealthProgram, Map<Round,List<String>>>();
			List<HealthProgram> healthProgramList = sc.getCustomQueryService().getDataByHQL("from HealthProgram order by programId");
			for (HealthProgram hp : healthProgramList) {
				
				Map<Round,List<String>> roundVaccine =new LinkedHashMap<Round,List<String>>();  
				List<Round> roundList = sc.getCustomQueryService().getDataByHQL("from Round where healthProgramId = "+ hp.getProgramId() + " order by roundId , healthProgramId ");
				
				for (Round rd : roundList) {
					String query = " SELECT  name FROM vaccine v "
							 +"LEFT JOIN (SELECT vaccineId, roundId, status FROM roundvaccine   WHERE roundId = "+ rd.getRoundId() +") as t1   "
							 +"ON t1.vaccineId = v.vaccineId  "
							 +"where v.vaccineId in (SELECT distinct(vaccineId) FROM vaccinegap WHERE vaccinationcalendarId = "+ hp.getVaccinationcalendarId() +" )   "
							 +"and vaccine_entity like '%child%' and (t1.status is null or t1.status = 1) ";
					
					List<String> vaccineList = sc.getCustomQueryService().getDataBySQL(query);
					roundVaccine.put(rd, vaccineList);
					
					System.out.println(rd.getRoundId()  + " " + hp.getVaccinationcalendarId() + " " + vaccineList.toString().replaceAll("\\[|\\]", ""));
				}
				data.put(hp, roundVaccine);
			};
			
			model.put("data", data);
			
			List<String> vaccinesL = sc.getCustomQueryService().getDataBySQL("SELECT name FROM vaccine where vaccine_entity like 'CHILD%' OR vaccine_entity is null ORDER BY vaccineId ");   /*where vaccine_entity like 'CHILD%'*/
			model.put("vaccinesList", vaccinesL);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
		return new ModelAndView("round_vaccine", "model", model);
	}

}
