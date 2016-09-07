package org.ird.unfepi.web.controller;

import java.util.Date;
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
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.validator.RoundValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/addprogramround")
public class AddRound extends DataEntryFormController {
	private static final FormType formType = FormType.HEALTHPROGRAMROUND_ADD;

	AddRound() {
		super(new DataEntryForm("program_round", "Program Round (New)", SystemPermissions.ADD_HEALTH_ROUND));
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView addHealthProgramView(HttpServletRequest request, ModelAndView modelAndView){
		
		modelAndView.addObject("command", new Round());
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Round round, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		
		new RoundValidator().validate(round, results);
		if(results.hasErrors()){	
			return showForm(modelAndView, "dataForm");	
		}
		
		String programId = request.getParameter("programId");
		
		try {
			if ( round.getIsActive() == true){
				List<Round> active_rounds = sc.getCustomQueryService().getDataByHQL("from Round where isActive = true and healthProgramId = " + programId);

				for (Round r : active_rounds) {
					r.setIsActive(false);
					sc.getCustomQueryService().update(r);
				}
			}

			//Integer cpId = cpL.get(0).getCenterProgramId();
			//round.setCenterProgramId(cpId);
			round.setCreatedDate(new Date());
			round.setCreatedByUserId(user.getUser());
			sc.getCustomQueryService().save(round);
			sc.commitTransaction();

			return new ModelAndView(new RedirectView("viewHealthProgramRounds.htm?programId="+programId));
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
			String programId = request.getParameter("programId");

			List<HealthProgram> records = sc.getCustomQueryService().getDataByHQL("from HealthProgram where programId = " + programId);
			if(records != null && records.size() > 0){
				model.addAttribute("healthprogram", records.get(0));
			} else {
				throw new Exception("healthprogram not found for programId " + programId);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error(formType.name(), e);
			request.setAttribute("errorMessagev", "An error occurred while retrieving reference data list. Error message is:"+e.getMessage());
		} finally {
			sc.closeSession();
		}	
	}
}
