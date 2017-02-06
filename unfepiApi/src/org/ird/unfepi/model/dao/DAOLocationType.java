/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationType;

public interface DAOLocationType extends DAO{

	LocationType findById(int id, boolean isreadonly, String[] mappingsToJoin);

	LocationType findByName(String roleName, boolean isreadonly, String[] mappingsToJoin);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin);
}
