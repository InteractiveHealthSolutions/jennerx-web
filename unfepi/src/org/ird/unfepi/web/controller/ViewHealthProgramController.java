package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewHealthProgramController extends DataDisplayController  {
	
	ViewHealthProgramController(){
		super("dataForm", new  DataSearchForm("health_program", "Health Program", SystemPermissions.VIEW_HEALTH_PROGRAM, true));
	}
	
	@RequestMapping(value="/viewHealthProgram.htm", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new HashMap<String, Object>();
		
		Map<HealthProgram, List<CenterProgram>> programMap = new LinkedHashMap<HealthProgram, List<CenterProgram>>();
		
		List<HealthProgram> hpRecords = sc.getCustomQueryService().getDataByHQL("from HealthProgram ");
		
		for (HealthProgram hp : hpRecords) {
			List<CenterProgram> cpRecords = sc.getCustomQueryService().getDataByHQL(
					"from CenterProgram where healthProgramId = "+ hp.getProgramId() + " order by vaccinationCenter.name");
			
			programMap.put(hp, cpRecords);
		}
		
		totalRows=hpRecords.size();
		addModelAttribute(model, "healthprograms", programMap);
		addModelAttribute(model, "totalRows", totalRows);
		
		return showForm(model);
	}
}
