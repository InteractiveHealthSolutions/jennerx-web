/**
 * 
 */
package org.ird.unfepi.model.dao;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Women;

/**
 * @author Safwan
 *
 */
public interface DAOWomen extends DAO {
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	Women findById (int mappedId);
	
	Women findById(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	Women findWomenByIdentifier(String programId, boolean isreadonly, String[] mappingsToJoin);
	
	List<Women> getAllWomen (boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	List<Women> getWomen (String partialName, String nic);
}
