package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccine;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ViewVaccinationCenterDetailsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			VaccinationCenter vaccinationCenter = sc.getVaccinationService().findVaccinationCenterById(programId, true, new String[]{"idMapper", "createdByUserId", "lastEditedByUserId"});
			addModelAttribute(model, "programId", programId);
			addModelAttribute(model, "vaccinationCenter", vaccinationCenter);
			addModelAttribute(model, "contacts", sc.getDemographicDetailsService().getContactNumber(vaccinationCenter.getMappedId(), true, null));
			addModelAttribute(model, "addresses", sc.getDemographicDetailsService().getAddress(vaccinationCenter.getMappedId(), true, new String[]{"city"}));

			List<Vaccine> vcl = sc.getVaccinationService().getAll(true, null, "vaccineId");
			ArrayList<Map<String, Object>> vaccineDayMapList = new ArrayList<Map<String,Object>>();

			for (Vaccine vaccine : vcl) 
			{
				Map<String,Object> vdmap = new HashMap<String, Object>();
				vdmap.put("vaccine", vaccine);
				List<VaccinationCenterVaccineDay> vcd = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccinationCenter.getMappedId(), vaccine.getVaccineId(), null, true);
				String days = "";
				for (VaccinationCenterVaccineDay vaccinationCenterVaccineDay : vcd) {
					days += sc.getVaccinationService().getByDayNumber(vaccinationCenterVaccineDay.getId().getDayNumber()).getDayShortName()+",";
				}
				vdmap.put("daylist", days);
				vaccineDayMapList.add(vdmap);
			}
			addModelAttribute(model, "vaccineDayMapList", vaccineDayMapList);
			
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
