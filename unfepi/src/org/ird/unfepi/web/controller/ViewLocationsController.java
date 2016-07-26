package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewLocationsController extends DataDisplayController {
	
	ViewLocationsController(){
		super("dataForm",  new  DataSearchForm("location", "Locations", SystemPermissions.VIEW_LOCATIONS, true));
	}
	
	@RequestMapping(value="/viewLocations", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
		int totalRows=0;
		Map<String, Object> model = new HashMap<String, Object>();
		req.setAttribute("editOrUpdateMessage", req.getParameter("editOrUpdateMessage"));
		ServiceContext sc = Context.getServices();
		try{
			List<Location> list =new ArrayList<Location>();
			
			Integer locationid = UnfepiUtils.getIntegerFilter(SearchFilter.PROGRAM_ID, req);
			String namelike = UnfepiUtils.getStringFilter(SearchFilter.NAME_PART, req);
			Integer locationType = UnfepiUtils.getIntegerFilter(SearchFilter.TYPE, req);	

			String pagerOffset = req.getParameter("pager.offset");
			
			int startRecord = 0;
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(pagerOffset)){//request is to navigate pages
				startRecord = Integer.parseInt(req.getParameter("pager.offset"));
			}
			
			if(locationid != null){
				List o = sc.getCustomQueryService().getDataByHQL("FROM Location l LEFT JOIN FETCH l.parentLocation p WHERE l.locationId="+locationid);
				if(o.size() > 0) list.add((Location) o.get(0));
			}
			else {
				String hqlWFilter = "FROM Location l "
						+ " LEFT JOIN FETCH l.parentLocation p "
						+ " WHERE 1=1 "
						+ (StringUtils.isEmptyOrWhitespaceOnly(namelike)?"":(" AND l.name like '"+namelike+"%'"))
						+ (locationType==null?"":(" AND l.locationType="+locationType));
				
				String hql = hqlWFilter // + " LIMIT "+startRecord+"," + WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS
						+ " ORDER BY l.name";
				
				Session ss = Context.getNewSession();
				try{
					totalRows=Integer.parseInt(ss.createQuery("SELECT count(*) "+hqlWFilter.replaceAll(" FETCH ", " ")).list().get(0).toString());
					
					Query q = ss.createQuery(hql);
					list = q.setReadOnly(true).setFirstResult(startRecord).setMaxResults(WebGlobals.DEFAULT_PAGING_MAX_PAGE_ITEMS).list();
				}
				finally{
					ss.close();
				}
			}

			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), locationid);
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), namelike);
			addModelAttribute(model, SearchFilter.TYPE.FILTER_NAME(), locationType);

			addModelAttribute(model, "locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType ORDER BY level"));
			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			return showForm(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			req.getSession().setAttribute("exceptionTrace",e);
			return new ModelAndView("exception");
		}
		finally{
			sc.closeSession();
		}
	}

}
