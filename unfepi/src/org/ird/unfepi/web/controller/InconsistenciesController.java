package org.ird.unfepi.web.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InconsistenciesController extends DataDisplayController {
	
	InconsistenciesController(){
		super("dataForm", new  DataViewForm("inconsistency", "System/Data Inconsistencies", SystemPermissions.VIEW_INCONSISTENCIES, false));
	}
	
	@RequestMapping(value="/inconsistencies", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return showForm(new HashMap<String, Object>());
	}
}
