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
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.utils.UserSessionUtils;
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
//		modelAndView.addObject("center", request.getParameter("center"));
//		modelAndView.addObject("program", request.getParameter("program"));
		return showForm(modelAndView, "dataForm");		
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Round round, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		
		String centerId = request.getParameter("centerId");
		String programId = request.getParameter("programId");
		
		try {
			List<CenterProgram> cpL = sc.getCustomQueryService().getDataByHQL("from CenterProgram where vaccinationCenterId = " + centerId + " and healthProgramId = " + programId);			
			if(cpL == null || cpL.size() == 0){
				throw new Exception("record not found for health program Id"+ programId + " in vaccination center Id" + centerId + " or vice versa \n");
			} else {
				
				if ( round.getIsActive() == true){
					List<Round> active_rounds = sc.getCustomQueryService().getDataByHQL("from Round where isActive = true");
					
					for (Round r : active_rounds) {
						r.setIsActive(false);
						sc.getCustomQueryService().update(r);
					}
				}
				
				Integer cpId = cpL.get(0).getCenterProgramId();
				round.setCenterProgramId(cpId);
				round.setCreatedDate(new Date());
				round.setCreatedByUserId(user.getUser());
				sc.getCustomQueryService().save(round);
				sc.commitTransaction();
			}
			
			return new ModelAndView(new RedirectView("viewHealthProgramRounds.htm?centerId="+centerId+"&programId="+programId));
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
			String centerId = request.getParameter("centerId");
			String programId = request.getParameter("programId");
			
			List<CenterProgram> cpL = sc.getCustomQueryService().getDataByHQL("from CenterProgram where vaccinationCenterId = " + centerId + " and healthProgramId = " + programId);			
			if(cpL == null || cpL.size() == 0){
				throw new Exception("record not found for health program Id"+ programId + " in vaccination center Id" + centerId + " or vice versa \n");
			} else {
				model.addAttribute("centerprogram", cpL.get(0));
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
