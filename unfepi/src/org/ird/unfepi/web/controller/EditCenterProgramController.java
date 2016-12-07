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
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editcenterprogram")
public class EditCenterProgramController extends DataEditFormController{
	
	public EditCenterProgramController() {
		super(new DataEditForm("center_program", "Center Program (Edit)", SystemPermissions.EDIT_HEALTH_CENTER_PROGRAM));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView editCenterProgramView(HttpServletRequest request, ModelAndView modelAndView) {
		modelAndView.addObject("command", formBackingObject(request));
		return showForm(modelAndView, "dataForm");
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView onSubmit(@ModelAttribute("command")CenterProgram cp, BindingResult results,
			HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws Exception {
		
		ServiceContext sc = Context.getServices();
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		
		try {
//			cp.setLastEditedByUserId(user.getUser());
//			cp.setLastEditedDate(new Date());
			sc.getCustomQueryService().update(cp);
			sc.commitTransaction();
			
//			System.out.println(cp);
			
			return new ModelAndView(new RedirectView("viewHealthProgramRounds.htm?centerId="+cp.getVaccinationCenterId()+"&programId="+cp.getHealthProgramId()));

		} catch (Exception e) {
			sc.rollbackTransaction();
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace", e);
			return new ModelAndView("exception");
			
		} finally {
			sc.closeSession();
		}
	}
	
	protected CenterProgram formBackingObject(HttpServletRequest request) {
		String centerprogramId = request.getParameter("centerprogramId");
		LoggedInUser user = UserSessionUtils.getActiveUser(request);
		CenterProgram cp = null;
		if (user == null) {
			boolean session_expired = true;
			request.setAttribute("session_expired", session_expired);
		} else {
			ServiceContext sc = Context.getServices();
			try {
				List<CenterProgram> cpL = sc.getCustomQueryService().getDataByHQL("from CenterProgram where centerProgramId =" + centerprogramId);
				if (cpL == null || cpL.size() == 0) {
					request.setAttribute("errorMessage", "Oops .. Some error occurred. HealthProgram was not found.");
				} else {
					cp = cpL.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Oops .. Some exception was thrown. Error message is:"+e.getMessage());
			} finally {
				sc.closeSession();
			}
		}
		return cp;
	}
}
