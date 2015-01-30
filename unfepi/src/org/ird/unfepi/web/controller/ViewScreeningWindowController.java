package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Screening;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewScreeningWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		
		int screeningid = Integer.parseInt(request.getParameter("screening_id"));
		ServiceContext sc = Context.getServices();
		try{
			Map<String, Object> model=new HashMap<String, Object>();
			Screening screening=sc.getChildService().findScreeningById(screeningid, true, new String[]{"idMapper", "broughtByRelationship", "", "lastEditedByUserId", "createdByUserId"});
			model.put("vcenter", sc.getVaccinationService().findVaccinationCenterById(screening.getVaccinationCenterId(), true, new String[]{"idMapper"}));
			model.put("vaccinator", sc.getVaccinationService().findVaccinatorById(screening.getVaccinatorId(), true, new String[]{"idMapper"}));
			model.put("screening", screening);
			
			return new ModelAndView("viewScreeningwindow","model",model);	
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
