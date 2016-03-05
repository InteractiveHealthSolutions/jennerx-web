package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ViewRemindersController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			addModelAttribute(model, "datalist", sc.getReminderService().getAllReminders(0, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"reminderText"}));
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}finally{
			sc.closeSession();
		}
	}
}