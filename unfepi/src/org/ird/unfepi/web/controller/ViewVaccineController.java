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
import org.ird.unfepi.model.Vaccine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewVaccineController extends DataDisplayController {

	ViewVaccineController() {
		super("dataForm", new  DataViewForm("vaccine", "Vaccines", SystemPermissions.VIEW_VACCINES, false));
	}
	
	@RequestMapping(value="/viewVaccine.htm", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<HashMap> vaccinesL = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM vaccine where vaccine_entity like 'CHILD%' OR vaccine_entity is null ORDER BY vaccineId ");   /*where vaccine_entity like 'CHILD%'*/
		
		model.put("vaccinesList", vaccinesL);
		
		System.out.println(vaccinesL.toString());
		
		return showForm(model);
	}
	
	
}
