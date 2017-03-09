/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationType;

public interface DAOLocationType extends DAO{

	LocationType findById(int locationTypeId, boolean isreadonly, String[] mappingsToJoin);
	
	LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin);

	LocationType findByLevel(int level, boolean isreadonly, String[] mappingsToJoin);

	LocationType findByDescription(String description, boolean isreadonly, String[] mappingsToJoin);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin);
}
