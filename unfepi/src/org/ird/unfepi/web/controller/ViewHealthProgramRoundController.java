package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.VaccinationCenter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewHealthProgramRoundController extends DataDisplayController{

	ViewHealthProgramRoundController(){
		super("dataForm", new  DataViewForm("program_round", "Program Rounds", SystemPermissions.VIEW_HEALTH_ROUND, false));
	}
	
	@RequestMapping(value="/viewHealthProgramRounds.htm", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new HashMap<String, Object>();
		
		String centerId = req.getParameter("centerId");
		System.out.println(centerId);
		
		String programId = req.getParameter("programId");
		System.out.println(programId);
		
		
		System.out.println(!StringUtils.isEmptyOrWhitespaceOnly(programId) +"  "+ !StringUtils.isEmptyOrWhitespaceOnly(centerId));
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(programId) && !StringUtils.isEmptyOrWhitespaceOnly(centerId)){
			
			System.out.println("from CenterProgram where vaccinationCenterId = "+Integer.parseInt(centerId) +" and healthProgramId = "+Integer.parseInt(programId));
			List<CenterProgram> centerProgramL = sc.getCustomQueryService().getDataByHQL("from CenterProgram where vaccinationCenterId = "+Integer.parseInt(centerId) +" and healthProgramId = "+Integer.parseInt(programId));
			List<Round> roundL = null;
			
			if(centerProgramL != null && centerProgramL.size() > 0 ){
				Integer centerProgramId = centerProgramL.get(0).getCenterProgramId();
				roundL = sc.getCustomQueryService().getDataByHQL("from Round where centerProgramId = " + centerProgramId );
			}
			
			addModelAttribute(model, "centerPrograms", centerProgramL);
			addModelAttribute(model, "rounds", roundL);
			addModelAttribute(model, "centerId", centerId);
			addModelAttribute(model, "programId", programId);
			
			for (CenterProgram cp : centerProgramL) {
				System.out.println(cp.getVaccinationCenter().getName() + " " + cp.getHealthProgram().getName());
			}
		
		}
		
		return showForm(model);
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"});
			model.addAttribute("vaccinationCenters", centeres);
			
			List<HealthProgram> programs = sc.getCustomQueryService().getDataByHQL("from HealthProgram");
			model.addAttribute("healthPrograms", programs);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());

		} finally {
			sc.closeSession();
		}	
	}
}
