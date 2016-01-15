/**
 * 
 */
package org.ird.unfepi.service;

import org.ird.unfepi.model.Location;

/**
 * @author Safwan
 *
 */
public interface LocationService {
	
	Location getCityById(int cityId, boolean isreadonly, String[] mappingsToJoin);

}
