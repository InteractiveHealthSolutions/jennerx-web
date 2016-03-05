package org.ird.unfepi.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.utils.reporting.ExceptionUtil;
import org.ird.unfepi.utils.reporting.LoggerUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ExceptionController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse arg1) throws Exception {
		  Exception ex=(Exception) (req.getSession().getAttribute("exceptionTrace"));
		  StringBuilder trctstr = new StringBuilder(ExceptionUtil.getStackTrace(ex));
		  LoggerUtil.logIt(trctstr.toString());
			  req.setAttribute("exceptionTrace", "A trace of exception is logged in log file. Exception message was \n"+ex.getMessage()+"\n\nPlease contact your system administrator.\nFull trace is : \n"+trctstr);
			try{
				req.getSession().removeAttribute("exceptionTrace");
			}catch (Exception e) {
				e.printStackTrace();
			}
			return new ModelAndView("exception");
	}

}
