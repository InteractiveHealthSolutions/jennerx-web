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
		
		String programId = req.getParameter("programId");
		try{
			if(!StringUtils.isEmptyOrWhitespaceOnly(programId)){
				
				List<CenterProgram> centerProgramL = sc.getCustomQueryService().getDataByHQL("from CenterProgram where healthProgramId = "+Integer.parseInt(programId) + " and isActive = true order by vaccinationCenter.name");
				List<Round> roundL = sc.getCustomQueryService().getDataByHQL("from Round where healthProgramId = " + programId );
				
				addModelAttribute(model, "centerPrograms", centerProgramL );
				addModelAttribute(model, "rounds", roundL);
				addModelAttribute(model, "programId", programId);
			}
			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");		
		}
		finally{
			sc.closeSession();
		}
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			List<HealthProgram> programs = sc.getCustomQueryService().getDataByHQL("from HealthProgram order by name");
			model.addAttribute("healthPrograms", programs);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());

		} finally {
			sc.closeSession();
		}	
	}
}
