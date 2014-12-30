package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.utils.UserSessionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewEncounterWindowController  implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		LoggedInUser user=UserSessionUtils.getActiveUser(request);
		if(user==null){
			request.setAttribute("lmessage", "Your Session has expired . please login again.");
		}
		
		ServiceContext sc = Context.getServices();

		String encounterId = request.getParameter("encid");
		String p1id = request.getParameter("p1");
		String p2id = request.getParameter("p2");

		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Encounter enc = sc.getEncounterService().findEncounterByCriteria(Integer.parseInt(encounterId), p1id, p2id, null, null, null, null, true, 0, 10, new String[]{"createdByUser"}).get(0);
			List listres = sc.getCustomQueryService().getDataBySQL("select element,value from encounterresults where encounterId="+enc.getId().getEncounterId()+" and p1id ='"+enc.getId().getP1id()+"' and p2id ='"+enc.getId().getP2id()+"' order by orderAs ASC");
			model.put("encounter", enc);
			model.put("encounterresults", listres);

			return new ModelAndView("popdv_encounter","model",model);	
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
