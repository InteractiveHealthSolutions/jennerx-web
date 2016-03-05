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
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenter.CenterType;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.mysql.jdbc.StringUtils;

public class ViewVaccinationCentersController extends DataDisplayController{

	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<VaccinationCenter> list =new ArrayList<VaccinationCenter>();
			
			String centerid = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String centername = UnfepiUtils.getStringFilter(SearchFilter.NAME_PART, req);
			CenterType centerType = (CenterType) UnfepiUtils.getEnumFilter(SearchFilter.CENTER_TYPE, CenterType.class, req);	

			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(centerid)){
				VaccinationCenter o = sc.getVaccinationService().findVaccinationCenterById(centerid, true, new String[]{"idMapper"});
				if(o != null) list.add(o);
			}
			else {
				list = sc.getVaccinationService().findVaccinationCenterByCriteria(null, centername, centerType, startRecord, WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS, true, new String[]{"idMapper"});
			}

			if(sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(VaccinationCenter.class)!=null){
			totalRows = sc.getVaccinationService().LAST_QUERY_TOTAL_ROW_COUNT(VaccinationCenter.class).intValue();
			}
			else{
				totalRows=list.size();
			}
			
			List<Map<String, Object>> centervaccinedayMap = new ArrayList<Map<String,Object>>();
			for (VaccinationCenter center : list) {
				Map<String, Object> hm = new HashMap<String, Object>();
				hm.put("center", center);
				
				List<Vaccine> vcl = sc.getVaccinationService().getAll(true, null, "vaccineId");
				ArrayList<Map<String, Object>> vaccineDayMapList = new ArrayList<Map<String,Object>>();

				for (Vaccine vaccine : vcl) 
				{
					Map<String,Object> vdmap = new HashMap<String, Object>();
					vdmap.put("vaccine", vaccine);
					List<VaccinationCenterVaccineDay> vcd = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(center.getMappedId(), vaccine.getVaccineId(), null, true);
					String days = "";
					for (VaccinationCenterVaccineDay vaccinationCenterVaccineDay : vcd) {
						days += sc.getVaccinationService().getByDayNumber(vaccinationCenterVaccineDay.getId().getDayNumber()).getDayShortName()+",";
					}
					vdmap.put("daylist", days);
					vaccineDayMapList.add(vdmap);
				}
				hm.put("vaccdaymaplist", vaccineDayMapList);

				centervaccinedayMap.add(hm);
			}
			
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), centerid);
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), centername);
			addModelAttribute(model, SearchFilter.CENTER_TYPE.FILTER_NAME(), centerType);

			addModelAttribute(model, "datalist", centervaccinedayMap);
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
