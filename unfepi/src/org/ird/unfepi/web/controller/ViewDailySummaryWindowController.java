package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

public class ViewDailySummaryWindowController implements Controller{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String dsId = request.getParameter("dsId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			DailySummary dsum = sc.getReportService().findDailySummaryBySerialId(Integer.parseInt(dsId), true, new String[]{"createdByUserId", "lastEditedByUserId"});
			model.put("dsumar", dsum);
			model.put("vcenter", sc.getVaccinationService().findVaccinationCenterById(dsum.getVaccinationCenterId(), true, new String[]{"idMapper"}));
			model.put("vaccinator", sc.getVaccinationService().findVaccinatorById(dsum.getVaccinatorId(), true, new String[]{"idMapper"}));

			List<DailySummaryVaccineGiven> vcgvnlis = sc.getReportService().findDailySummaryVaccineGivenByCriteria(null, null, dsum.getSerialNumber(), null, true, 0, Integer.MAX_VALUE, null);
			for (DailySummaryVaccineGiven vcgvn : vcgvnlis) {
				model.put(vcgvn.getVaccineName().toUpperCase(), vcgvn.getQuantityGiven());
			}
			
			return new ModelAndView("viewDailySummarywindow","model",model);	
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
