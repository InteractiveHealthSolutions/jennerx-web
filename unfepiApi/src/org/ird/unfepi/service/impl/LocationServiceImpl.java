/**
 * 
 */
package org.ird.unfepi.service.impl;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.dao.DAOLocation;
import org.ird.unfepi.service.LocationService;

/**
 * @author Safwan
 *
 */
public class LocationServiceImpl implements LocationService {
	
	private ServiceContext sc;

	private DAOLocation locDao;
	
	public LocationServiceImpl(ServiceContext sc, DAOLocation loc ){
		this.sc = sc;
		this.locDao = loc;

	}

	@Override
	public Location getCityById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		Location l = locDao.getCityById(cityId, isreadonly, mappingsToJoin);
		return l;
	}

}
