/**
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Women;

/**
 * @author Safwan
 *
 */
public interface WomenService {
	
	Women findById (int mappedId);
	
	Women findWomenById(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	Women findWomenByIdentifier(String programId, boolean isreadonly, String[] mappingsToJoin);
	
	List<Women> getAllWomen (boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	List<Women> getWomen (String partialName, String nic);
	
	Serializable save(Women women);

}
