package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.VaccinationCenter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ViewStorekeeperDetailsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Storekeeper storekeeper = sc.getStorekeeperService().findStorekeeperById(programId, true, new String[]{"idMapper", "createdByUserId", "lastEditedByUserId"});
			addModelAttribute(model, "programId", programId);
			addModelAttribute(model, "storekeeper", storekeeper);
			addModelAttribute(model, "contacts", sc.getDemographicDetailsService().getContactNumber(storekeeper.getMappedId(), true, null));
			addModelAttribute(model, "addresses", sc.getDemographicDetailsService().getAddress(storekeeper.getMappedId(), true, new String[]{"city"}));

			VaccinationCenter vc = null;
			if(storekeeper.getClosestVaccinationCenterId() != null){
				vc = sc.getVaccinationService().findVaccinationCenterById(storekeeper.getClosestVaccinationCenterId(), true, new String[]{"idMapper"});
			}
			addModelAttribute(model, "vcenter", vc);
			
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
