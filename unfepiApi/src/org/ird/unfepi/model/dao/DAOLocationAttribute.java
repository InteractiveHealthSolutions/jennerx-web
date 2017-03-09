/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.LocationAttribute;

public interface DAOLocationAttribute extends DAO {

	LocationAttribute findById(int id, boolean isreadonly, String[] mappingsToJoin);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	LocationAttribute findByValue(String value, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeName(String typeName, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeValue1(String typeValue, boolean isreadonly, String[] mappingsToJoin);

	LocationAttribute findByTypeValue2(String typeValue, boolean isreadonly, String[] mappingsToJoin);

	List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin);
}
