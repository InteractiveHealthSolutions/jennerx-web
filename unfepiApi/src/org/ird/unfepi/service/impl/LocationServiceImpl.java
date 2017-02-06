/**
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.dao.DAOLocation;
import org.ird.unfepi.model.dao.DAOLocationAttribute;
import org.ird.unfepi.model.dao.DAOLocationAttributeType;
import org.ird.unfepi.model.dao.DAOLocationType;
import org.ird.unfepi.service.LocationService;

/**
 * @author Safwan
 *
 */
public class LocationServiceImpl implements LocationService {
	
	private ServiceContext sc;

	private DAOLocation daoLocation;

	private DAOLocationType daoLocationType;

	private DAOLocationAttribute daoLocationAttribute;

	private DAOLocationAttributeType daoLocationAttributeType;
	
	public LocationServiceImpl(ServiceContext sc, DAOLocation daoLocation, DAOLocationType daoLocationType, DAOLocationAttribute daoLocationAttribute, DAOLocationAttributeType daoLocationAttributeType){
		this.sc = sc;
		this.daoLocation = daoLocation;
		this.daoLocationType = daoLocationType;
		this.daoLocationAttribute = daoLocationAttribute;
		this.daoLocationAttributeType = daoLocationAttributeType;
	}

	@Override
	public Location findLocationById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findById(cityId, isreadonly, mappingsToJoin);
	}
	
	@Override
	public Location findLocationByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.findByName(name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<Location> getAllLocation(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocation.getAll(isreadonly, mappingsToJoin);
	}
	
	@Override
	public LocationType findLocationTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findById(id, isreadonly, mappingsToJoin);
	}

	@Override
	public LocationType findLocationTypeByName(String Name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.findByName(Name, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationType> getAllLocationType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationType.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public LocationAttribute findLocationAttributeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.findById(id, isreadonly, mappingsToJoin);
	}
	
	@Override
	public List<LocationAttribute> findLocationAttributeByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		return daoLocationAttribute.findByCriteria(locationAttributeId, value, locationId, locationAttributeTypeId, firstResult, fetchsize, isreadonly, mappingsToJoin, sqlFilter);
	}
	
	@Override
	public List<LocationAttribute> getAllLocationAttribute(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttribute.getAll(isreadonly, mappingsToJoin);
	}
	
	@Override
	public LocationAttributeType findLocationAttributeTypeById(int id, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findById(id, isreadonly, mappingsToJoin);
	}

	@Override
	public LocationAttributeType findLocationAttributeTypeByName(String Name, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByName(Name, isreadonly, mappingsToJoin);
	}

	@Override
	public LocationAttributeType findLocationAttributeTypeByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.findByCategory(category, isreadonly, mappingsToJoin);
	}

	@Override
	public List<LocationAttributeType> getAllLocationAttributeType(boolean isreadonly, String[] mappingsToJoin) {
		return daoLocationAttributeType.getAll(isreadonly, mappingsToJoin);
	}

	@Override
	public Serializable addLocation(Location location) {
		return daoLocation.save(location);
	}

	@Override
	public Serializable addLocationType(LocationType locationType) {
		return daoLocationType.save(locationType);
	}

	@Override
	public Serializable addLocationAttribute(LocationAttribute locationAttribute) {
		return daoLocationAttribute.save(locationAttribute);
	}

	@Override
	public Serializable addLocationAttributeType(LocationAttributeType locationAttributeType) {
		return daoLocationAttributeType.save(locationAttributeType);
	}

	@Override
	public void updateLocation(Location location) {
		daoLocation.update(location);
	}

	@Override
	public void updateLocationType(LocationType locationType) {
		daoLocationType.update(locationType);
	}

	@Override
	public void updateLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.update(locationAttribute);
	}

	@Override
	public void updateLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.update(locationAttributeType);
	}

	@Override
	public void deleteLocation(Location location) {
		daoLocation.delete(location);
	}

	@Override
	public void deleteLocationType(LocationType locationType) {
		daoLocationType.delete(locationType);
	}

	@Override
	public void deleteLocationAttribute(LocationAttribute locationAttribute) {
		daoLocationAttribute.delete(locationAttribute);
	}

	@Override
	public void deleteLocationAttributeType(LocationAttributeType locationAttributeType) {
		daoLocationAttributeType.delete(locationAttributeType);
	}
}
