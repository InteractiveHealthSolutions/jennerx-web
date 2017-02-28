package org.ird.unfepi.web.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.MuacMeasurement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;


@Controller
public class ViewMuacMeasurements extends DataDisplayController{
	
	ViewMuacMeasurements(){
		super("dataForm", new  DataSearchForm("muac_measurements", "Muac Measurements", SystemPermissions.VIEW_ITEM, false));
	}
	
	@RequestMapping(value="/viewmuacmeasurements", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		ServiceContext sc = Context.getServices();
		Map<String, Object> model = new LinkedHashMap<String, Object>();
		
		try {
			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			String query = "SELECT m.mappedId , m.circumference, m.colorrange, m.measureDate , i.identifier FROM muacmeasurement m left join identifier i on m.mappedId = i.mappedId ";
			
			totalRows = sc.getCustomQueryService().getDataBySQLMapResult(query).size();	
			
			query += " limit "+startRecord +", 20";
		
		List<HashMap> records = sc.getCustomQueryService().getDataBySQLMapResult(query);
		
//		System.out.println(records.toString());
	
		addModelAttribute(model,"muacmeasurements", records);
		addModelAttribute(model, "totalRows", totalRows);
		
		return showForm(model);
			
		} catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");
		} finally {
			sc.closeSession();
		}
		
	}

}
