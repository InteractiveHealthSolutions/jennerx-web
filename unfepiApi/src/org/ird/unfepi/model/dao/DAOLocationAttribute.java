/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationAttribute;

public interface DAOLocationAttribute extends DAO {

	LocationAttribute findById(int id, boolean isreadonly, String[] mappingsToJoin);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin);
}
