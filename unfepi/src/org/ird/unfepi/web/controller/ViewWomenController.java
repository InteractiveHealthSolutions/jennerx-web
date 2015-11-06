/**
 * 
 */
package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Safwan
 *
 */
public class ViewWomenController extends DataDisplayController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//Model model = null;
		Map<String, Object> model = new HashMap<String, Object>();
		String text = "WomenController";
		addModelAttribute(model, "text", text);
		return showForm(model);
	}


}
