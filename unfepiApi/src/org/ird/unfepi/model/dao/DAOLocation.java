/**
 * 
 */
package org.ird.unfepi.model.dao;

import org.ird.unfepi.model.Location;

/**
 * @author Safwan
 *
 */
public interface DAOLocation {
	
	Location getCityById(int cityId, boolean isreadonly, String[] mappingsToJoin);

}
