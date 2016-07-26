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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewImmunizationRecordDetailsController extends DataDisplayController{

	ViewImmunizationRecordDetailsController(){
		super("popupForm", new  DataViewForm("immunization_details", "Immunization Details", SystemPermissions.VIEW_CHILDREN_DATA, false));
	}
	
	@RequestMapping(value="/immunizationDetails", method={RequestMethod.GET})
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		
		ServiceContext sc = Context.getServices();

		String programId = request.getParameter("programId");
		Map<String, Object> model = new HashMap<String, Object>();
		try{
			String dmpTable = "dmp_master_epidata";
			List<String> colNames = sc.getCustomQueryService().getDataBySQL("SELECT column_name FROM information_schema.columns WHERE table_name='"+dmpTable +"' ORDER BY ORDINAL_POSITION");

			List<Map<String,Object>> dmp = sc.getCustomQueryService().getDataBySQLMapResult("SELECT * FROM "+dmpTable+" WHERE ChildId='"+programId+"'");
			addModelAttribute(model, "datalist", dmp);
			addModelAttribute(model, "columnNames", colNames);

			return showForm(model);
		}catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");		
		}finally{
			sc.closeSession();
		}
	}
}
