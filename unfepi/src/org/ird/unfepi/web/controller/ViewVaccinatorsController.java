package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewVaccinatorsController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	int totalRows=0;
    	Map<String, Object> model = new HashMap<String, Object>();
		
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<Vaccinator> list=new ArrayList<Vaccinator>();

			String vaccinatorid = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String vaccinatorname = UnfepiUtils.getStringFilter(SearchFilter.NAME_PART, req);
			Integer vaccCenter = UnfepiUtils.getIntegerFilter(SearchFilter.VACCINATION_CENTER, req);
			Gender gender = (Gender) UnfepiUtils.getEnumFilter(SearchFilter.GENDER, Gender.class, req);	

			String action = req.getParameter("action");
			String pagerOffset = req.getParameter("pager.offset");
			
			if(action == null && pagerOffset == null){//page is accessed first time
				list = sc.getVaccinationService().getAllVaccinator(0, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"idMapper"});
			}
			else {
				int startRecord = 0;
				if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
					startRecord = Integer.parseInt(req.getParameter("pager.offset"));
					
				}
				if(vaccinatorid != null){
					Vaccinator obj = sc.getVaccinationService().findVaccinatorById(vaccinatorid, true, new String[]{"idMapper"});
					if(obj != null){
						list.add(obj);
					}
				}
				else{
					list = sc.getVaccinationService().findByCriteria(vaccinatorname, null, gender, vaccCenter, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"idMapper"});
				}
			}
			
			if(sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(Vaccinator.class)!= null){
				totalRows = sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(Vaccinator.class).intValue();
			}
			else{
				totalRows = list.size();
			}

			List<Map<String, Object>> vaccMap = new ArrayList<Map<String,Object>>();
			for (Vaccinator vacctor : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("vaccinator", vacctor);
				map.put("contacts", sc.getDemographicDetailsService().getContactNumber(vacctor.getMappedId(), true, null));
				//map.put("addresses", sc.getDemographicDetailsService().getAddress(vacctor.getMappedId(), true, null));
				vaccMap.add(map);
			}
			
			addModelAttribute(model, "vaccinationCenters", sc.getVaccinationService().getAllVaccinationCenter(true, new String[]{"idMapper"}));

			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), vaccinatorid);
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), vaccinatorname);
			addModelAttribute(model, SearchFilter.GENDER.FILTER_NAME(), gender);
			addModelAttribute(model, SearchFilter.VACCINATION_CENTER.FILTER_NAME(), vaccCenter);

			addModelAttribute(model, "datalist", vaccMap);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView(new RedirectView("exception.htm"));
		}
		finally{
			sc.closeSession();
		}
	}
}