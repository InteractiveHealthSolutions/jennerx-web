package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ViewImmunizationRecordDetailsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			List<Map<String,Object>> dmp = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM dmp_master_epidata WHERE ChildId='"+programId+"'");
			addModelAttribute(model, "datalist", dmp);
			
			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));		
		}
		finally{
			sc.closeSession();
		}
	}
}
