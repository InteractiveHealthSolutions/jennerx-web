/**
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Location;

/**
 * @author Safwan
 *
 */
public interface DAOLocation extends DAO {
	
	Location findById(int cityId, boolean isreadonly, String[] mappingsToJoin);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	Location findByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<Location> getAll(boolean isreadonly, String[] mappingsToJoin);
}
