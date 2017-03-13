package org.ird.unfepi.web.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.management.InstanceAlreadyExistsException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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

@Controller
@RequestMapping("/location")
public class LocationModalController {

	@RequestMapping(value = "/newlocation", method = RequestMethod.POST)
	public @ResponseBody String addlocation(@RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pId")String parent, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("type")String type, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Integer id = null;
			Location location = sc.getLocationService().findLocationByName(name, true, null);
			if(location == null) {
				location = new Location();
				location.setName(name);
				location.setShortName(shortName);
				location.setFullName(fullName);
				if(parent.equals("")) { location.setParentLocation(null); }			
				else { Location parentLocation = sc.getLocationService().findLocationById(strToInt(parent), true, null); location.setParentLocation(parentLocation); }
				location.setDescription(description);
				location.setOtherIdentifier(otherIdentifier);
	    		LocationType locationType = sc.getLocationService().findLocationTypeById(strToInt(type), true, null); 
	    		if(locationType == null) { locationType = new LocationType(); locationType.setLocationTypeId(strToInt(type)); sc.getLocationService().addLocationType(locationType); location.setLocationType(locationType); }
	    		else { location.setLocationType(locationType); }
	    		location.setLatitude(latitude);
	    		location.setLongitude(longitude);
	    		Serializable locationIdd = sc.getLocationService().addLocation(location); 
	    		id = Integer.parseInt(locationIdd.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION '" + location.getName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = location.getLocationId(); 
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION '" + location.getName() + "' ALREADY EXISTS<br/>";
			}		
			sc.commitTransaction();
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/newlocationType", method = RequestMethod.POST)
//	public @ResponseBody String addlocationType(HttpServletRequest request, HttpServletResponse response) throws JSONException, InstanceAlreadyExistsException {
	public @ResponseBody String addlocationType(@RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
//			String name = request.getParameter("addlocationTypeName");
//			String level = request.getParameter("addlocationTypeLevel");
//			String description = request.getParameter("addlocationTypeDescription");
			Integer id = null;
			LocationType locationType = sc.getLocationService().findLocationTypeByName(name, true, null);
			if(locationType == null) {
				locationType = new LocationType();
				locationType.setTypeName(name);
				locationType.setLevel(strToInt(level));
				locationType.setDescription(description);
				Serializable locationTypeId = sc.getLocationService().addLocationType(locationType); 
	    		id = Integer.parseInt(locationTypeId.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = locationType.getLocationTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION TYPE '" + locationType.getTypeName() + "' ALREADY EXISTS<br/>";
			}
			sc.commitTransaction();
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/newlocationattributetype", method = RequestMethod.POST)
//	public @ResponseBody String addlocationattributetype(HttpServletRequest request, HttpServletResponse response) throws JSONException, InstanceAlreadyExistsException {
	public @ResponseBody String addlocationattributetype(@RequestParam("name")String locationAttributeTypeName, @RequestParam("displayName")String locationAttributeTypeDisplayName, @RequestParam("description")String locationAttributeTypeDescription, @RequestParam("category")String locationAttributeTypeCategory) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
//			String locationAttributeTypeName = request.getParameter("addlocationAttributeTypeName");
//			String locationAttributeTypeDescription = request.getParameter("addlocationAttributeTypeDescription");
//			String locationAttributeTypeDisplayName = request.getParameter("addlocationAttributeTypeDisplayName");
//			String locationAttributeTypeCategory = request.getParameter("addlocationAttributeTypeCategory");
			Integer id = null;
			LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(locationAttributeTypeName, true, null);
			if(locationAttributeType == null) {
				locationAttributeType = new LocationAttributeType();
				locationAttributeType.setAttributeName(locationAttributeTypeName);
				locationAttributeType.setDisplayName(locationAttributeTypeDisplayName);
				locationAttributeType.setDescription(locationAttributeTypeDescription);
				locationAttributeType.setCategory(locationAttributeTypeCategory);
				Serializable locationAttributeTypeId = sc.getLocationService().addLocationAttributeType(locationAttributeType); 
	    		id = Integer.parseInt(locationAttributeTypeId.toString());
	    		status = "success";
	    		message = "SUCCESS!";
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' CREATED SUCCESSFULLY<br/>";
			}
			else {
				id = locationAttributeType.getLocationAttributeTypeId();
	    		status = "info";
	    		message = "INFO:";
	    		returnStr = "LOCATION ATTRIBUTE TYPE '" + locationAttributeType.getAttributeName() + "' ALREADY EXISTS<br/>";
			}
			sc.commitTransaction();
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/newlocationattribute", method = RequestMethod.POST)
	public @ResponseBody String addlocationattribute(@RequestParam("parent")String locationParent, @RequestParam("array")String attributes) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			ArrayList<Integer> ids = new ArrayList<Integer>();
//			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);
//			ArrayList<ArrayList<String>> list = new ArrayList<>();
			ArrayList<LocationAttribute> locationAttributes = new ArrayList<LocationAttribute>();
			JSONArray arr = new JSONArray(attributes);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jsonArr = new JSONObject(arr.get(i).toString());
				LocationAttribute locationAttribute = new LocationAttribute();
				locationAttribute.setLocationId(strToInt(locationParent));
				locationAttribute.setLocationAttributeTypeId(strToInt(jsonArr.get("locationAttributeTypeId").toString()));
				locationAttribute.setValue(jsonArr.get("locationAttributeTypeValue").toString());
				locationAttribute.setTypeName(jsonArr.get("type").toString());
				if(jsonArr.get("type").toString().equals("None")) { }
				else if(jsonArr.get("type").toString().equals("Time_Bound")) {
					locationAttribute.setTypeValue1(jsonArr.get("value1").toString());
					locationAttribute.setTypeValue2(jsonArr.get("value2").toString());
				}
				else {
					locationAttribute.setTypeValue1(jsonArr.get("value1").toString());
					locationAttribute.setTypeValue2(jsonArr.get("value1").toString());
				}
				locationAttributes.add(locationAttribute);
			}
			for(int i=0; i<locationAttributes.size(); i++) {
				ids.add((Integer) sc.getLocationService().addLocationAttribute(locationAttributes.get(i)));
			}
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
			returnStr =  "New Attribute Created";
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", ids);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/newlocationupdate", method = RequestMethod.POST)
	public @ResponseBody String updatelocation(@RequestParam("id")String id, @RequestParam("name")String name, @RequestParam("shortName")String shortName, @RequestParam("fullName")String fullName, @RequestParam("pId")String pId, @RequestParam("pName")String pName, @RequestParam("description")String description, @RequestParam("otherIdentifier")String otherIdentifier, @RequestParam("type")String type, @RequestParam("typeName")String typeName, @RequestParam("latitude")String latitude, @RequestParam("longitude")String longitude) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			Location location = new Location();
			Location parentLocation = new Location();
			LocationType locationType = new LocationType();

			location.setLocationId(Integer.parseInt(id));
			location.setName(name);
			location.setShortName(shortName);
			location.setFullName(fullName);
	
			if(parentLocation.equals("")) {
				location.setParentLocation(null);
			}			
			else {
				parentLocation.setLocationId(strToInt(pId));
				location.setParentLocation(parentLocation);				
			}

			location.setDescription(description);
			location.setOtherIdentifier(otherIdentifier);

			locationType.setLocationTypeId(Integer.parseInt(type));
			location.setLocationType(locationType);	
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			
			sc.getLocationService().updateLocation(location);
			sc.commitTransaction();
    		status = "success";
    		message = "SUCCESS!";
    		returnStr = "Location Updated.";

			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", id);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}

	@RequestMapping(value = "/updatelocationType", method = RequestMethod.POST)
	public @ResponseBody String updatelocationType(@RequestParam("id")String locationTypeId, @RequestParam("name")String name, @RequestParam("level")String level, @RequestParam("description")String description) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			LocationType locationType = new LocationType();
			locationType.setLocationTypeId(strToInt(locationTypeId));
			locationType.setTypeName(name);
			locationType.setLevel(strToInt(level));
			locationType.setDescription(description);
			sc.getLocationService().updateLocationType(locationType);
			sc.commitTransaction();
			status = "success";
			message = "SUCCESS!";
			returnStr = "Location Type Updated.";
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", locationTypeId);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	

	@RequestMapping(value = "/updatelocationattributetype", method = RequestMethod.POST)
//	public @ResponseBody String addlocationType(HttpServletRequest request, HttpServletResponse response) throws JSONException, InstanceAlreadyExistsException {
	public @ResponseBody String updatelocationAttributeType(@RequestParam("id")String locationAttributeTypeId, @RequestParam("name")String name, @RequestParam("displayName")String displayName, @RequestParam("description")String description, @RequestParam("category")String category) throws InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
		JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
		try {
			LocationAttributeType locationAttributeType = new LocationAttributeType();
			locationAttributeType.setLocationAttributeTypeId(strToInt(locationAttributeTypeId));
			locationAttributeType.setAttributeName(name);
			locationAttributeType.setDisplayName(displayName);
			locationAttributeType.setDescription(description);
			locationAttributeType.setCategory(category);
			sc.getLocationService().updateLocationAttributeType(locationAttributeType); 
			sc.commitTransaction();
			status = "success";
			message = "SUCCESS!";
			returnStr = "Location Attribute Type Updated.";
			result.append("status", status);
			result.append("message", message);
			result.append("returnStr", returnStr);
			result.append("id", locationAttributeTypeId);
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}	
	
	@RequestMapping(value = "/newCsv", method = RequestMethod.POST)
	public @ResponseBody String updateCsv(@RequestParam("csvData")String csvData) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
        String s1 = ""; 
        String s2 = ""; 
		String status = "";
		String message = "";
		String returnStr = "";
	    ArrayList<String> locationParent = new ArrayList<String>();
	    ArrayList<String> locationParentFound = new ArrayList<String>();
	    ArrayList<String> locationParentNotFound = new ArrayList<String>();			
//	    System.out.println(csvData);
	    JSONObject result = new JSONObject();
		try {
			// PARSE CSV DATA TO LOCATION OBJECTS			
			ArrayList<Location> csvLocations = new ArrayList<Location>();
			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
			JSONArray arr = new JSONArray(csvData);
		    for(int i = 0 ; i < arr.length(); i++){
		    	ArrayList<String> array = new ArrayList<String>();
		        JSONArray arrr = arr.getJSONArray(i);
		        for(int j = 0; j < arrr.length(); j++){ array.add(arrr.get(j).toString()); }
		        list.add(array);
		    }
		    ArrayList<String> head = list.get(0);
		    for (int i = 1; i < list.size(); i++) {
		    	ArrayList<String> val = list.get(i);
				if(head.size()==val.size()) {
		            Location l = new Location();
					for (int j = 0; j < head.size(); j++) {
			        	if(head.get(j).trim().toLowerCase().equals("name")) { l.setName(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("short_name")) { l.setShortName(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("full_name")) { l.setFullName(val.get(j)); }
			        	//else if(head.get(j).trim().toLowerCase().equals("parent")) { if(!val.get(j).equals("NULL")) { Location parent = new Location(); parent.setName(val.get(j)); l.setParentLocation(parent); } }	
			        	else if(head.get(j).trim().toLowerCase().equals("parent")) {
			        		if(val.get(j).equals("") || val.get(j).equals("NULL")) { } 
			        		else {
			        			Location parent = new Location(); 
			        			parent.setName(val.get(j)); 
			        			l.setParentLocation(parent);
			        		}
		        		}
			        	else if(head.get(j).trim().toLowerCase().equals("type")) { 
			        		LocationType lt = sc.getLocationService().findLocationTypeByName(val.get(j), true, null); 
			        		if(lt == null) { 
			        			lt = new LocationType();
			        			lt.setTypeName(val.get(j));
			        			sc.getLocationService().addLocationType(lt); 
			        			l.setLocationType(lt);
			        			s1 += "LOCATION TYPE: '" + l.getLocationType().getTypeName() + "' CREATED SUCCESSFULLY<br/>"; 
			        		}
			        		else {
			        			l.setLocationType(lt);
			        			s1 += "LOCATION TYPE '" + lt.getTypeName() + "' ALREADY EXISTS<br/>"; 
		        			}
		        		}
			        	else if(head.get(j).trim().toLowerCase().equals("identifier")) { l.setOtherIdentifier(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("description")) { l.setDescription(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("latitude")) { l.setLatitude(val.get(j)); }
			        	else if(head.get(j).trim().toLowerCase().equals("longitude")) { l.setLongitude(val.get(j)); }
					}
					csvLocations.add(l);
				}
			}
			
			// CHECK PARENT IF NULL OR NOT
		    ArrayList<String> locationName = new ArrayList<String>();
			for (Location l : csvLocations) {
	        	locationName.add(l.getName());
	        	if(l.getParentLocation() != null) { locationParent.add(l.getParentLocation().getName()); }
	        	else { locationParent.add("NULL"); }
			}
	        for (String locationParentName : locationParent) {
		        if(locationName.contains(locationParentName) || locationParentName.equals("NULL")) { locationParentFound.add(locationParentName); }
		        else {
		        	Location l = sc.getLocationService().findLocationByName(locationParentName, true, null);
					if(l == null) { locationParentNotFound.add(locationParentName); }
		        	else { locationParentFound.add(locationParentName); }
	        	}
			}
	        
		    // SAVE LOCATION, LOCATIONTYPE TO DATABASE
	        if(locationParent.size() == locationParentFound.size()) {
	        	for (Location l : csvLocations) {
	        		Location ll = sc.getLocationService().findLocationByName(l.getName(), true, null); 
	        		if(ll == null) { 
	        			ll = l;
	        			if(ll.getParentLocation() == null) { }
	        			else { 
	        				Location lp = sc.getLocationService().findLocationByName(ll.getParentLocation().getName(), true, null);
	        				if(lp != null) { ll.setParentLocation(lp); }
	        				else { System.out.println("PARENT LOCATION NULL ERROR"); }
	        			}
	        			sc.getLocationService().addLocation(ll);  
	        			s2 += "LOCATION '" + ll.getName() + "' CREATED SUCCESSFULLY<br/>"; 
	        		}
	        		else { s2 += "LOCATION '" + ll.getName() + "' ALREADY EXISTS<br/>"; }
				}
        		if(s2.equals("")) { 
        			returnStr += "LOCATIONS CREATED"; 
        		}
	        	else { 
	        		returnStr += s2; 
        		}
	        	
        		if(s1.equals("")) {
        			returnStr += "LOCATION TYPES CREATED"; 
    			}
	        	else { 
	        		returnStr += s1; 
        		}

	        	// GENERATE TREE FROM DATABASE
    			JSONArray locationNodes = new JSONArray();
    			JSONArray locationTypeNodes = new JSONArray();
    			JSONArray locationAttributeNodes = new JSONArray();
    			JSONArray locationAttributeTypeNodes = new JSONArray();
    			ArrayList<Location> locations = (ArrayList<Location>) sc.getLocationService().getAllLocation(true, null);
    			ArrayList<LocationType> locationTypes = (ArrayList<LocationType>) sc.getLocationService().getAllLocationType(true, null);		
    			ArrayList<LocationAttribute> locationAttributes = (ArrayList<LocationAttribute>) sc.getLocationService().getAllLocationAttribute(true, null);
    			ArrayList<LocationAttributeType> locationAttributeTypes = (ArrayList<LocationAttributeType>) sc.getLocationService().getAllLocationAttributeType(true, null);

    			if(locations == null) {}
    			else {
    				for(int i=0; i<locations.size(); i++) {
    					if(locations.get(i) == null) {}
    					else {
    						if(locations.get(i).getLocationId() == 66 || locations.get(i).getLocationId() == 77 || locations.get(i).getLocationId() == 88 || locations.get(i).getLocationId() == 99) { }
    						else {
    							JSONObject locationNode = new JSONObject();
    							locationNode.append("id", locations.get(i).getLocationId());
    							locationNode.append("name", locations.get(i).getName());
    							locationNode.append("shortName", locations.get(i).getShortName());
    							locationNode.append("fullName", locations.get(i).getFullName());
    							if(locations.get(i).getParentLocation() == null) { locationNode.append("pId", 0); locationNode.append("pName", ""); }
    							else { locationNode.append("pId", locations.get(i).getParentLocation().getLocationId()); locationNode.append("pName", locations.get(i).getParentLocation().getName()); }
    							locationNode.append("description", locations.get(i).getDescription());
    							locationNode.append("otherIdentifier", locations.get(i).getOtherIdentifier());
    							if(locations.get(i).getLocationType() == null) { locationNode.append("type", 0); locationNode.append("typeName", ""); }
    							else { for(int j=0; j<locationTypes.size(); j++) { if(locationTypes.get(j).getLocationTypeId() == locations.get(i).getLocationType().getLocationTypeId()) { locationNode.append("type", locations.get(i).getLocationType().getLocationTypeId()); locationNode.append("typeName", locationTypes.get(j).getTypeName()); } } }
    							locationNode.append("latitude", locations.get(i).getLatitude());
    							locationNode.append("longitude", locations.get(i).getLongitude());
    							locationNode.append("open", true);
    							locationNodes.put(locationNode);
    						} 
    					}
    				}
    			}

    			if(locationTypes == null) {}
    			else {
    				for(int i=0; i<locationTypes.size(); i++) {
    					if(locationTypes.get(i) == null) {}
    					else {
    						JSONObject locationTypeNode = new JSONObject();
    						locationTypeNode.append("id", locationTypes.get(i).getLocationTypeId());
    						locationTypeNode.append("name", locationTypes.get(i).getTypeName());
    						if(locationTypes.get(i).getLevel() == null) { locationTypeNode.append("level", 0); }
    						else { locationTypeNode.append("level", locationTypes.get(i).getLevel()); }
    						locationTypeNode.append("description", locationTypes.get(i).getDescription());
    						locationTypeNodes.put(locationTypeNode);
    					}
    				}
    			}
    			
    			if(locationAttributes == null) {}
    			else {
    				for(int i=0; i<locationAttributes.size(); i++) {
    					if(locationAttributes.get(i) == null) {}
    					else {
    						JSONObject locationAttributeNode = new JSONObject();
    						locationAttributeNode.append("id", locationAttributes.get(i).getLocationAttributeId());
    						locationAttributeNode.append("typeId", locationAttributes.get(i).getLocationAttributeTypeId());
    						locationAttributeNode.append("locationId", locationAttributes.get(i).getLocationId());
    						locationAttributeNode.append("value", locationAttributes.get(i).getValue());
    						locationAttributeNode.append("typeName", locationAttributes.get(i).getTypeName());
    						locationAttributeNode.append("typeValue1", locationAttributes.get(i).getTypeValue1());
    						locationAttributeNode.append("typeValue2", locationAttributes.get(i).getTypeValue2());
    						locationAttributeNodes.put(locationAttributeNode);
    					}
    				}
    			}

    			if(locationAttributeTypes == null) {}
    			else {
    				for(int i=0; i<locationAttributeTypes.size(); i++) {
    					if(locationAttributeTypes.get(i) == null) {}
    					else {
    						JSONObject locationAttributeTypeNode = new JSONObject();
    						locationAttributeTypeNode.append("id", locationAttributeTypes.get(i).getLocationAttributeTypeId());
    						locationAttributeTypeNode.append("name", locationAttributeTypes.get(i).getAttributeName());
    						locationAttributeTypeNode.append("displayName", locationAttributeTypes.get(i).getDisplayName());
    						locationAttributeTypeNode.append("description", locationAttributeTypes.get(i).getDescription());
    						locationAttributeTypeNode.append("category", locationAttributeTypes.get(i).getCategory());
    						locationAttributeTypeNodes.put(locationAttributeTypeNode);
    					} 
    				}
    			}
	        	result.append("locationNodes", locationNodes);
				result.append("locationTypeNodes", locationTypeNodes);
				result.append("locationAttributeNodes", locationAttributeNodes);
				result.append("locationAttributeTypeNodes", locationAttributeTypeNodes);
	        }
	        else {
	        	returnStr = "PARENT LOCATION DOESN'T EXIST FOR:-";
		        for (int i = 0; i < locationParentNotFound.size(); i++) {
			        returnStr +=  "<br/>" + locationParentNotFound.get(i);
				}
				result.append("locationNodes", new JSONArray());
				result.append("locationTypeNodes", new JSONArray());
				result.append("locationAttributeTypeNodes", new JSONArray());
	        }
	        status = "info";
	        message = "INFO:";
	        result.append("status", status);
	        result.append("message", message);
			result.append("returnStr", returnStr);
			sc.commitTransaction();
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
	
	@RequestMapping(value = "/newCsvForLocAttrs", method = RequestMethod.POST)
	public @ResponseBody String updateCsvForLocAttrs(@RequestParam("csvData")String csvData) throws JSONException, InstanceAlreadyExistsException {
		ServiceContext sc = Context.getServices();
	    JSONObject result = new JSONObject();
		String status = "";
		String message = "";
		String returnStr = "";
	    try {
	    	// PARSE CSV DATA TO LOCATION ATTRIBUTE OBJECTS
	    	ArrayList<LocationAttribute> csvLocationAttributes = new ArrayList<LocationAttribute>();
			ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
			JSONArray arr = new JSONArray(csvData);
		    for(int i = 0 ; i < arr.length(); i++){
		    	ArrayList<String> array = new ArrayList<String>();
		        JSONArray arrr = arr.getJSONArray(i);
		        for(int j = 0; j < arrr.length(); j++){ array.add(arrr.get(j).toString()); }
		        list.add(array);
		    }
		    ArrayList<String> head = list.get(0);
		    for (int i = 1; i < list.size(); i++) {
			    ArrayList<String> val = list.get(i);
				if(head.size()==val.size()) {
					LocationAttribute locationAttribute = new LocationAttribute();
					for (int j = 0; j < head.size(); j++) {
						if(head.get(j).trim().toLowerCase().equals("parent")) { 
							Location location = sc.getLocationService().findLocationByName(val.get(j), true, null);
							if(location == null) { returnStr += "LOCATION '"+val.get(j)+"' DOES NOT EXIST\n"; } 
							else { locationAttribute.setLocationId(location.getLocationId()); }
						}
						else if(head.get(j).trim().toLowerCase().equals("attribute_name")) { 
							String locationAttributeTypeName = "";
							String typeName = "";
							if(val.get(j).toUpperCase().contains("_ANUALLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_ANUALLY", "").length()).replaceAll("_", " "); typeName = "Anually"; }
							else if(val.get(j).toUpperCase().contains("_ANUAL")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_ANUAL", "").length()).replaceAll("_", " "); typeName = "Anually"; }
							else if(val.get(j).toUpperCase().contains("_MONTHLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_MONTHLY", "").length()).replaceAll("_", " "); typeName = "Monthly"; }
							else if(val.get(j).toUpperCase().contains("_DAILY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_DAILY", "").length()).replaceAll("_", " "); typeName = "Daily"; }
							else if(val.get(j).toUpperCase().contains("_ANUALLY")) { locationAttributeTypeName = val.get(j).substring(0, val.get(j).toUpperCase().replace("_TIME_BOUND", "").length()).replaceAll("_", " "); typeName = "Time_Bound"; }
							else { locationAttributeTypeName = val.get(j).replaceAll("_", " "); typeName = "None"; }
							LocationAttributeType locationAttributeType = sc.getLocationService().findLocationAttributeTypeByName(locationAttributeTypeName, true, null);
							if(locationAttributeType == null) { returnStr += "ATTRIBUTE TYPE NAME '"+locationAttributeTypeName+"' DOES NOT EXIST\n"; } 
							else { locationAttribute.setLocationAttributeTypeId(locationAttributeType.getLocationAttributeTypeId()); locationAttribute.setTypeName(typeName); }
						}
						else if(head.get(j).trim().toLowerCase().equals("value")) { locationAttribute.setValue(val.get(j)); }
						else if(head.get(j).trim().toLowerCase().equals("type_value")) { 
							String typeValue1 = "";
							String typeValue2 = "";
							if(val.get(j).contains(":")) { String[] parts = val.get(j).split(":"); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else if(val.get(j).contains("_")) { String[] parts = val.get(j).split("_"); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else if(val.get(j).contains(" to ")) { String[] parts = val.get(j).split(" to "); typeValue1 = parts[0]; typeValue2 = parts[1]; }
							else { typeValue1 = val.get(j); typeValue2 = val.get(j); }
							locationAttribute.setTypeValue1(typeValue1); locationAttribute.setTypeValue2(typeValue2);
						}
					} csvLocationAttributes.add(locationAttribute);
				}
		    }
		    if(returnStr == "") {
			    for (int i = 0; i < csvLocationAttributes.size(); i++) {
			    	if(csvLocationAttributes.get(i) != null) { sc.getLocationService().addLocationAttribute(csvLocationAttributes.get(i)); returnStr += "LOCATION ATTRIBUTE CREATED SUCCESSFULLY<br/>"; }
			    	else { returnStr += "LOCATION ATTRIBUTE ALREADY EXISTS<br/>"; }
			    }
		    }
		    else {
		    	returnStr += "\nERROR WHILE CREATING LOCATION ATTRIBUTES<br/>" ;
		    }
	        status = "info";
	        message = "INFO:";
		    result.append("status", status);
		    result.append("message", message);
			result.append("returnStr", returnStr);
			sc.commitTransaction();
		} catch(Exception e) { e.printStackTrace(); sc.rollbackTransaction();
		} finally { sc.closeSession(); 
		} return result.toString();
	}
		
	private Integer strToInt(String str) {
	    if(str.equals("")) return null;
	    else return Integer.parseInt(str);
	}
}
