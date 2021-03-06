package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Encounter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewEncounterDetailsController extends DataDisplayController{
	
	ViewEncounterDetailsController (){
		super("popupForm", new  DataViewForm("encounter_details", "Encounter Details", SystemPermissions.VIEW_ENCOUNTERS, false));
	}
	
	@RequestMapping(value="/encounterDetails", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		
		ServiceContext sc = Context.getServices();
		String encounterId = request.getParameter("encid");
		String p1id = request.getParameter("p1");
		String p2id = request.getParameter("p2");

		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Encounter enc = sc.getEncounterService().findEncounterByCriteria(Integer.parseInt(encounterId), p1id, p2id, null, null, null, null, true, 0, 10, new String[]{"createdByUser"}).get(0);
			List listres = sc.getCustomQueryService().getDataBySQL("select element,value, groupName, displayName from encounterresults where encounterId="+enc.getId().getEncounterId()+" and p1id ='"+enc.getId().getP1id()+"' and p2id ='"+enc.getId().getP2id()+"' order by orderAs ASC");
			addModelAttribute(model, "encounter", enc);
			addModelAttribute(model, "encounterresults", listres);

			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");		
		}
		finally{
			sc.closeSession();
		}
	}
}
