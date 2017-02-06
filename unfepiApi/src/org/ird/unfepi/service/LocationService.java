package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;

public interface LocationService {
	
	Location findLocationById(int cityId, boolean isreadonly, String[] mappingsToJoin);
	
	Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<Location> getAllLocation(boolean isreadonly, String[] mappingsToJoin);

	LocationType findLocationTypeById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationType findLocationTypeByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<LocationType> getAllLocationType(boolean isreadonly, String[] mappingsToJoin);
	
	LocationAttributeType findLocationAttributeTypeById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationAttributeType findLocationAttributeTypeByName(String name, boolean isreadonly, String[] mappingsToJoin);

	LocationAttributeType findLocationAttributeTypeByCategory(String category, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttributeType> getAllLocationAttributeType(boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findLocationAttributeById(int id, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findLocationAttributeByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	List<LocationAttribute> getAllLocationAttribute(boolean isreadonly, String[] mappingsToJoin);

	Serializable addLocation(Location location);

	Serializable addLocationType(LocationType locationType);

	Serializable addLocationAttribute(LocationAttribute locationAttribute);

	Serializable addLocationAttributeType(LocationAttributeType locationAttributeType);

	void updateLocation(Location location);

	void updateLocationType(LocationType locationType);

	void updateLocationAttribute(LocationAttribute locationAttribute);

	void updateLocationAttributeType(LocationAttributeType locationAttributeType);

	void deleteLocation(Location location);

	void deleteLocationType(LocationType locationType);

	void deleteLocationAttribute(LocationAttribute locationAttribute);

	void deleteLocationAttributeType(LocationAttributeType locationAttributeType);

}
