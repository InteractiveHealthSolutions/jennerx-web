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
	
	Women findById (int mappedId);
	
	List<Women> getAllWomen (boolean readOnly);
	
	List<Women> getWomen (String partialName, String nic);
}
