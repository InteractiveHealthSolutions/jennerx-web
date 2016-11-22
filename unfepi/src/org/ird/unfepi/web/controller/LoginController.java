package org.ird.unfepi.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.beans.Credentials;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.reporting.LoggerUtil;
import org.ird.unfepi.web.validator.LoginValidator;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@RequestMapping(value="/login" , method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request, ModelAndView modelView){
		modelView.setViewName("login");
		return modelView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	protected String onSubmit(@ModelAttribute("credentials") Credentials credentials, BindingResult results, 
							  HttpServletRequest request, HttpServletResponse response, Model model) {
		
		new LoginValidator().validate(credentials, results);
		if (results.hasErrors()) {
			return "login";
		}
		try{
			if(!UserSessionUtils.isUserSessionActive(request))
			{
				UserSessionUtils.login(credentials.getUsername(), Context.getAuthenticatedUser(credentials.getUsername(), credentials.getPassword()), request, response);
				LoggerUtil.logIt(LoggerUtil.getJVMInfo());//TODO try moving it to some other event
			}
		} catch (SchedulerException e) {
			
			System.out.println("SchedulerException");
			e.printStackTrace();
			
		} catch (UserServiceException e) {
			
			System.out.println("UserServiceException");
			e.printStackTrace();
			GlobalParams.FILELOGGER.error("Login Failure ", e);
			request.getSession().setAttribute("logmessage", e.getMessage());
			return "login";
			
		} catch (Exception e) {
			
			System.out.println("Exception");
			e.printStackTrace();
			GlobalParams.FILELOGGER.error("Login Failure ", e);
			request.getSession().setAttribute("exceptionTrace", e);
			return "exception";
			
		}
		
		GlobalParams.DBLOGGER.info("User "+credentials.getUsername()+" logged in", LoggerUtils.getLoggerParams(LogType.LOGIN, null,null));
		return "redirect:/mainpage.htm";
	}
	
	@ModelAttribute("credentials")
	protected Credentials backingObject() {
		return new Credentials();
	}
	
//	@ModelAttribute
	protected void referenceData(HttpServletRequest request, Model model) throws Exception {
		model.addAttribute("logmessage", request.getSession().getAttribute("logmessage"));
		if (request.getParameter("logmessage") != null) {
			model.addAttribute("logmessage", request.getParameter("logmessage"));
		}
		try {
			request.getSession().removeAttribute("logmessage");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
