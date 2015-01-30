package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ReminderSms;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewReminderSmsWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception 
	{
		String rsid=request.getParameter("rsId");
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			ReminderSms rsms=sc.getReminderService().getReminderSmsRecord(Integer.parseInt(rsid),true,null);
			model.put("remsms", rsms);
			return new ModelAndView("viewReminderSmswindow","model",model);	
		}catch (Exception e) {
			request.getSession().setAttribute("exceptionTrace",e);
			sc.closeSession();//incase of error close session
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			request.setAttribute("sc"	, sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}
}
