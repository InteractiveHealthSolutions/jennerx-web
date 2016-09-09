package org.ird.unfepi.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.ControllerUIHelper;
import org.ird.unfepi.web.validator.HealthProgramValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/edithealthprogram")
public class EditHealthProgramController extends DataEditFormController {
	
	public EditHealthProgramController() {
		super(new DataEditForm("health_program", "Health Program (Edit)", SystemPermissions.EDIT_HEALTH_PROGRAM));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editChildView(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")HealthProgram hp, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		String[] centersId = request.getParameterValues("vaccinationCenters");
		
		for (String string : centersId) {
			System.out.println(string + " \t");
		}
		
		new HealthProgramValidator().validateHealthProgramVaccinationCenters(hp, centersId, results, true);
		if(results.hasErrors()){	
			return showForm(modelAndView.addObject("centers_selected", Arrays.asList(centersId)), "dataForm");	
		}
		
		try {
			ControllerUIHelper.doHealthProgramUpdate(hp, centersId, user.getUser(), sc);
			sc.commitTransaction();
			
			return new ModelAndView(new RedirectView("viewHealthProgram.htm"));
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
			
		} finally {
			sc.closeSession();
		}
	}
	
	protected HealthProgram formBackingObject(HttpServletRequest request) {
		String programId = request.getParameter("programId");
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		HealthProgram hp = null;
		if (user == null) {
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		
		} else {
			ServiceContext sc = Context.getServices();
			try {
				List<HealthProgram> hpL = sc.getCustomQueryService().getDataByHQL("from HealthProgram where programId =" + programId);
				if (hpL == null || hpL.size() == 0) {
					request.setAttribute("errorMessage", "Oops .. Some error occurred. HealthProgram was not found.");
				} else {
					hp = hpL.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			} finally {
				sc.closeSession();
			}
		}
		return hp;
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		String programId = request.getParameter("programId");
		
		try {
			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"});
			model.addAttribute("vaccinationCenters", centeres);
			
			List<CenterProgram> cpList = sc.getCustomQueryService().getDataByHQL("from CenterProgram where healthProgramId = " + programId);
			model.addAttribute("centerProgram", cpList);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
	
}
