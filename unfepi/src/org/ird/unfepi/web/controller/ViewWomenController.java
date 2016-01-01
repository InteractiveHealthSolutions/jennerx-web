/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Safwan
 *
 */
public class ViewWomenController extends DataDisplayController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		ServiceContext sc = Context.getServices();
		List<Women> women = sc.getWomenService().getAllWomen(true, 0, Integer.MAX_VALUE, new String[]{"idMapper"});
		List<WomenVaccination> vaccine = sc.getWomenVaccinationService().getAllVaccinationRecord(0, Integer.MAX_VALUE, true, new String[]{"idMapper"}, null);
		addModelAttribute(model, "women", women);
		addModelAttribute(model, "vaccine", vaccine);
		return showForm(model);
	}


}
