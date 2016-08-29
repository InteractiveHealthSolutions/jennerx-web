package org.ird.unfepi.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataEditForm;
import org.ird.unfepi.DataEditFormController;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editprogramround")
public class EditRound  extends DataEditFormController{

	EditRound() {
		super(new DataEditForm("program_round", "Program Round(Edit)", SystemPermissions.EDIT_HEALTH_ROUND));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editRoundView(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")Round round, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		if ( round.getIsActive() == true){
			activateRound();	
		}
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		String centerId = request.getParameter("vaccinationCenterId");
		String programId = request.getParameter("healthProgramId");
		Integer roundId = Integer.parseInt(request.getParameter("roundId"));
		
		try {
			
			sc.beginTransaction();
			round.setLastEditedByUserId(user.getUser());
			round.setLastEditedDate(new Date());
			sc.getCustomQueryService().update(round);
			sc.commitTransaction();
			
			return new ModelAndView(new RedirectView("viewHealthProgramRounds.htm?centerId="+centerId+"&programId="+programId));
			
		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
			
		} finally {
			sc.closeSession();
		}
	}
	
	private void activateRound(){
		ServiceContext sc = Context.getServices();
		try {
			List<Round> active_rounds = sc.getCustomQueryService().getDataByHQL("from Round where isActive = true");
			
			for (Round r : active_rounds) {
				r.setIsActive(false);
				sc.getCustomQueryService().update(r);
			}
			sc.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.closeSession();
		}
		
	}
	
	protected Round formBackingObject(HttpServletRequest request) {
		String roundId = request.getParameter("roundIdd");
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		Round round = null;
		if (user == null) {
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		} else {
			ServiceContext sc = Context.getServices();
			try {
				List<Round> roundL = sc.getCustomQueryService().getDataByHQL("from Round where roundId =" + roundId);
				if (roundL == null || roundL.size() == 0) {
					request.setAttribute("errorMessage", "Oops .. Some error occurred. HealthProgram Round was not found.");
				} else {
					round = roundL.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			} finally {
				sc.closeSession();
			}
		}
		return round;
	}
}
