package org.ird.unfepi.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/location")
public class LocationModalController {

	@RequestMapping(value = "/newlocation", method = RequestMethod.POST)
	public @ResponseBody String addlocation(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException {
//		Context.instantiate(null);
		ServiceContext sc = Context.getServices();	
//		String id = request.getParameter("addId");
		String name = request.getParameter("addName");
		String shortName = request.getParameter("addShortName");
		String fullName = request.getParameter("addFullName");
		String parent = request.getParameter("addParent");
		String description = request.getParameter("addDescription");
		String otherIdentifier = request.getParameter("addOtherIdentifier");
		String type = request.getParameter("addType");
		Location location = new Location();
		Location parentLocation = new Location();
		LocationType locationType = new LocationType();
//		location.setLocationId(Utility.strToInt(id));
		location.setName(name);
		location.setShortName(shortName);
		location.setFullName(fullName);

		parentLocation.setLocationId(strToInt(parent));
		location.setParentLocation(parentLocation);
		
		location.setDescription(description);
		location.setOtherIdentifier(otherIdentifier);
		
		locationType.setLocationTypeId(strToInt(type));
		location.setLocationType(locationType);
		Integer id = (Integer) sc.getLocationService().addLocation(location);
		sc.commitTransaction();
		return Integer.toString(id);
//		return "New Location Added";
	}

	@RequestMapping(value = "/newlocationType", method = RequestMethod.POST)
	public @ResponseBody String addlocationType(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException {
//		Context.instantiate(null);
		ServiceContext sc = Context.getServices();	
//		String id = request.getParameter("addlocationTypeId");
		String name = request.getParameter("addlocationTypeName");
		String level = request.getParameter("addlocationTypeLevel");
		LocationType locationType = new LocationType();
//		locationType.setLocationTypeId(Utility.strToInt(id));
		locationType.setTypeName(name);
		locationType.setLevel(strToInt(level));
		Integer id = (Integer) sc.getLocationService().addLocationType(locationType);
		sc.commitTransaction();
		return Integer.toString(id);
//		return "New Location Type Added";
	}	

	@RequestMapping(value = "/newlocationattributetype", method = RequestMethod.POST)
	public @ResponseBody String addlocationattributetype(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException {
//		String locationAttributeTypeId = request.getParameter("addlocationAttributeTypeId");
		String locationAttributeTypeName = request.getParameter("addlocationAttributeTypeName");
		String locationAttributeTypeDescription = request.getParameter("addlocationAttributeTypeDescription");
		String locationAttributeTypeCategory = request.getParameter("addlocationAttributeTypeCategory");
		LocationAttributeType locationAttributeType = new LocationAttributeType();
//		locationAttributeType.setLocationAttributeTypeId(Utility.strToInt(locationAttributeTypeId));
		locationAttributeType.setAttributeName(locationAttributeTypeName);
		locationAttributeType.setDescription(locationAttributeTypeDescription);
		locationAttributeType.setCategory(locationAttributeTypeCategory);
//		Context.instantiate(null);
		ServiceContext sc = Context.getServices();	
		Integer id = (Integer) sc.getLocationService().addLocationAttributeType(locationAttributeType);
		sc.commitTransaction();
		return Integer.toString(id);
//		return "New Attribute Type Created";
	}

	@RequestMapping(value = "/newlocationattribute", method = RequestMethod.POST)
	public @ResponseBody String addlocationattribute(HttpServletRequest request, HttpServletResponse response) throws InstanceAlreadyExistsException {

		ServiceContext sc = Context.getServices();
		String locationParent = request.getParameter("locationParent");
		ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
		ArrayList<LocationAttribute> locationAttributes = new ArrayList<LocationAttribute>();
		
		for(int i=0; i<locationAttributeTypes.size(); i++) {
			String value = request.getParameter("locationAttribute_" + Integer.toString(locationAttributeTypes.get(i).getLocationAttributeTypeId()));
			if(value != null && !StringUtils.isEmptyOrWhitespaceOnly(value)){
				LocationAttribute locationAttribute;
				List<LocationAttribute> records = sc.getCustomQueryService().getDataByHQL("from LocationAttribute where "
						+ "locationAttributeTypeId = " +locationAttributeTypes.get(i).getLocationAttributeTypeId() 
						+ " and locationId = " + strToInt(locationParent));
				if(records != null && records.size() > 0){
					locationAttribute = records.get(0);
				} else {
					locationAttribute = new LocationAttribute();
				}
//				LocationAttribute locationAttribute = new LocationAttribute();
				locationAttribute.setLocationId(strToInt(locationParent));
				locationAttribute.setLocationAttributeTypeId(locationAttributeTypes.get(i).getLocationAttributeTypeId());
				locationAttribute.setValue(value);
				locationAttributes.add(locationAttribute);
			}
		}
		for(int i=0; i<locationAttributes.size(); i++) {
			sc.getCustomQueryService().saveOrUpdate(locationAttributes.get(i));
//			ids.add((Integer) sc.getLocationService().addLocationAttribute(locationAttributes.get(i)));
		}
		sc.commitTransaction();
		return "LocationAttribute(s) added/updated for locationId " + locationParent;
	}

	@RequestMapping(value = "/newlocationupdate", method = RequestMethod.POST)
	public @ResponseBody String updatelocation(@RequestParam("id")String id, @RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pId")String pId, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("type")String type, @RequestParam("typeName")String typeName) {
		Location location = new Location();
		location.setLocationId(Integer.parseInt(id));
		location.setName(name);
		location.setShortName(shortName);
		location.setFullName(fullName);

		Location parentLocation = new Location();
		parentLocation.setLocationId(Integer.parseInt(pId));
		location.setParentLocation(parentLocation);
		
		location.setDescription(description);
		location.setOtherIdentifier(otherIdentifier);
		
		LocationType locationType = new LocationType();
		locationType.setLocationTypeId(Integer.parseInt(type));
		location.setLocationType(locationType);
		
		ServiceContext sc = Context.getServices();	
		sc.getLocationService().updateLocation(location);
		sc.commitTransaction();
		return "Location Updated";
	}

	private Integer strToInt(String str) {
        if(str.equals("")) return null;
        else return Integer.parseInt(str);
    }
}
