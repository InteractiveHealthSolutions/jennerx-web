package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.ird.unfepi.DataDisplayController;
import org.ird.unfepi.DataSearchForm;
import org.ird.unfepi.DataViewForm;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.StringUtils;

@Controller
public class ViewLocationsController extends DataDisplayController {
	
	ViewLocationsController(){
		super("dataForm",  new  DataViewForm("location", "Locations", SystemPermissions.VIEW_LOCATIONS, false));
	}
	
	@RequestMapping(value="/viewLocations", method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView handleRequest1(HttpServletRequest req,	HttpServletResponse resp) throws Exception {
		
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
			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(true, null);
			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);
			String locationNodes = "[ ";
			for(int i=0; i<locations.size(); i++) {
				if(locations.get(i).getLocationId() == 66 || locations.get(i).getLocationId() == 77 || locations.get(i).getLocationId() == 88 || locations.get(i).getLocationId() == 99) { }
				else {
					locationNodes += "{ id: " + Integer.toString(locations.get(i).getLocationId());
					locationNodes += ", name: \"" + locations.get(i).getName() + "\"" ;
					locationNodes += ", shortName: \"" + locations.get(i).getShortName() + "\"" ;
					locationNodes += ", fullName: \"" + locations.get(i).getFullName() + "\"" ;
					if(locations.get(i).getParentLocation() == null) { locationNodes += ", pId: " + Integer.toString(0) + ""; }
					else { locationNodes += ", pId: " + Integer.toString(locations.get(i).getParentLocation().getLocationId()) + ""; }
					locationNodes += ", description: \"" + locations.get(i).getDescription() + "\"" ;
					locationNodes += ", otherIdentifier: \"" + locations.get(i).getOtherIdentifier() + "\"" ;
					if(locations.get(i).getLocationType() == null) {
						locationNodes += ", type: " + Integer.toString(0) + ""; 
						locationNodes += ", typeName: \"" + "\"" + ""; 
					}
					else {
						for(int j=0; j<locationTypes.size(); j++) {
							if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) {
								locationNodes += ", type: " + locations.get(i).getLocationType().getLocationTypeId() + ""; 
								locationNodes += ", typeName: \"" + locationTypes.get(j).getTypeName() + "\"";
							}
						}
					}
					locationNodes += ", open:true }";
					if(i == locations.size()-1) { }
					else { locationNodes += ", "; }
				}
			}
			locationNodes+= " ]";
			
//			ArrayList<LocationType> locationTypes = LocationTypeDAO.readLocationTypes("from LocationType");
			String locationTypeNodes = "[ ";
			for(int i=0; i<locationTypes.size(); i++) {
				locationTypeNodes += "{ id: " + Integer.toString(locationTypes.get(i).getLocationTypeId());
				locationTypeNodes += ", name: \"" + locationTypes.get(i).getTypeName() + "\"" ;
				// TODO
				if(locationTypes.get(i).getLevel() == null) { locationTypeNodes += ", level: " + Integer.toString(0) + "}"; }
				else { locationTypeNodes += ", level: " + Integer.toString(locationTypes.get(i).getLevel()) + "}"; }
				if(i == locationTypes.size()-1) { }
				else { locationTypeNodes += ", "; }
			}
			locationTypeNodes+= " ]";

			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
			String locationAttributeTypeNodes = "[ ";
			for(int i=0; i<locationAttributeTypes.size(); i++) {
				locationAttributeTypeNodes += "{ id: " + Integer.toString(locationAttributeTypes.get(i).getLocationAttributeTypeId());
				locationAttributeTypeNodes += ", name: \"" + locationAttributeTypes.get(i).getAttributeName() + "\"";
				locationAttributeTypeNodes += ", description: \"" + locationAttributeTypes.get(i).getDescription() + "\"";
				locationAttributeTypeNodes += ", category: \"" + locationAttributeTypes.get(i).getCategory() + "\"" + "}";
				if(i == locationAttributeTypes.size()-1) { }
				else { locationAttributeTypeNodes += ", "; }
			}
			locationAttributeTypeNodes+= " ]";
			
			addModelAttribute(model, SearchFilter.PROGRAM_ID.FILTER_NAME(), locationid);
			addModelAttribute(model, SearchFilter.NAME_PART.FILTER_NAME(), namelike);
			addModelAttribute(model, SearchFilter.TYPE.FILTER_NAME(), locationType);

			addModelAttribute(model, "locationTypes", sc.getCustomQueryService().getDataByHQL("FROM LocationType ORDER BY level"));
			addModelAttribute(model, "datalist", list);
			addModelAttribute(model, "totalRows", totalRows);
			
			addModelAttribute(model, "locationNodes", locationNodes);
			addModelAttribute(model, "locationTypeNodes", locationTypeNodes);
			addModelAttribute(model, "locationAttributeTypeNodes", locationAttributeTypeNodes);
			addModelAttribute(model, "hello", "hello");
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
