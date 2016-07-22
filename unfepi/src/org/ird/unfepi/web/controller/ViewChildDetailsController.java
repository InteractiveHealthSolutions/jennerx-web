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
import org.ird.unfepi.model.Child;
import org.ird.unfepi.utils.IRUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewChildDetailsController extends DataDisplayController{
	
	ViewChildDetailsController() {
		super("popupForm", new  DataViewForm("child_details", "Enrollment Details", SystemPermissions.VIEW_CHILDREN_DATA, false));
	}

	@RequestMapping(value="/childDetails", method={RequestMethod.GET})
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ServiceContext sc = Context.getServices();
		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			Child child = sc.getChildService().findChildById(programId, true, new String[]{"idMapper", "nicOwnerRelation", "createdByUserId", "lastEditedByUserId", "enrollmentVaccine"});
			addModelAttribute(model, "programId", programId);
			addModelAttribute(model, "child", child);
			String epiNumber = request.getParameter("epiNumber");
			if(epiNumber == null){
				List epiNumberl = sc.getCustomQueryService().getDataBySQL("select epiNumber from vaccination where childId="+child.getMappedId()+" and vaccinationStatus IN ('VACCINATED','LATE_VACCINATED') order by vaccinationDate DESC");
				epiNumber = (String) (epiNumberl.size()==0?"":epiNumberl.get(0));
			}
			addModelAttribute(model, "epiNumber", epiNumber);
			addModelAttribute(model, "vaccinations", IRUtils.addRecord(child));
			addModelAttribute(model, "contacts", sc.getDemographicDetailsService().getContactNumber(child.getMappedId(), true, new String[]{"ownerRelation"}));
			addModelAttribute(model, "addresses", sc.getDemographicDetailsService().getAddress(child.getMappedId(), true, new String[]{"city"}));
			addModelAttribute(model, "preferences", sc.getChildService().findLotterySmsByChild(child.getMappedId(), true, 0, 50, null));
			addModelAttribute(model, "incentives", sc.getIncentiveService().findChildIncentiveByCriteria(child.getMappedId(), null, null, null, null, null, null, null, null, null, 0, 100, true, null));
			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");		
		}
		finally{
			request.setAttribute("sc", sc);
			/*sc.closeSession();*///will close after page have been loaded
		}
	}

}
