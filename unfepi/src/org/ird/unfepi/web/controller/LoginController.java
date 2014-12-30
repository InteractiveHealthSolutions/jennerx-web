package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.quartz.SchedulerException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

public class LoginController extends SimpleFormController {
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		Credentials cr=(Credentials)command;
		try{
			if(!UserSessionUtils.isUserSessionActive(request))
			{
				UserSessionUtils.login(cr.getUsername(), Context.getAuthenticatedUser(cr.getUsername(), cr.getPassword()), request, response);
				LoggerUtil.logIt(LoggerUtil.getJVMInfo());//TODO try moving it to some other event
			}
		}catch (SchedulerException e) {
			e.printStackTrace();
		} 
		catch (UserServiceException e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error("Login Failure ", e);
			request.getSession().setAttribute("logmessage", e.getMessage());
			return new ModelAndView(new RedirectView("login.htm"));
		}catch (Exception e) {
			e.printStackTrace();
			GlobalParams.FILELOGGER.error("Login Failure ", e);
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
			
		GlobalParams.DBLOGGER.info("User "+cr.getUsername()+" logged in", LoggerUtils.getLoggerParams(LogType.LOGIN, null,null));
		
		return new ModelAndView(new RedirectView("mainpage.htm"));
	}
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map< String, Object> model=new HashMap<String, Object>();
		model.put("logmessage", request.getSession().getAttribute("logmessage"));
		if(request.getParameter("logmessage")!=null){
			model.put("logmessage", request.getParameter("logmessage"));
		}
		try{
			request.getSession().removeAttribute("logmessage");
		}catch (Exception e) {e.printStackTrace();}
		
		return model;
	}
}
