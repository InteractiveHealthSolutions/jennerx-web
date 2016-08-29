package org.ird.unfepi.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEntryForm;
import org.ird.unfepi.DataEntryFormController;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.constants.FormType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
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
@RequestMapping(value="/addhealth_program")
public class AddHealthProgramController extends DataEntryFormController{
	
	private static final FormType formType = FormType.HEALTHPROGRAM_ADD;
	
	AddHealthProgramController(){
		super(new DataEntryForm("health_program", "Health Program (New)", SystemPermissions.ADD_HEALTH_PROGRAM));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addHealthProgramView(HttpServletRequest request, ModelAndView modelAndView){
		modelAndView.addObject("command", new HealthProgram());
		return showForm(modelAndView, "dataForm");		
	}
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")HealthProgram hp, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
//		System.out.println(hp.getName() + " " + hp.getDescription() + " " + hp.getEnrollmentLimit());
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		String[] centersId = request.getParameterValues("vaccinationCenters");
		
		new HealthProgramValidator().validateHealthProgramVaccinationCenters(hp, centersId, results, true);
		if(results.hasErrors()){	
			return showForm(modelAndView, "dataForm");	
		}
		
		try {
			ControllerUIHelper.doHealthProgramRegistration(hp, centersId, user.getUser(), sc);
			sc.commitTransaction();
			
			return new ModelAndView(new RedirectView("viewHealthProgram.htm"));
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
			
		} finally {
			sc.closeSession();
		}
	}
	
	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception 
	{
		ServiceContext sc = Context.getServices();
		try {
			List<VaccinationCenter> centeres = sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"});
			model.addAttribute("vaccinationCenters", centeres);

		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());

		} finally {
			sc.closeSession();
		}	
	}

}
