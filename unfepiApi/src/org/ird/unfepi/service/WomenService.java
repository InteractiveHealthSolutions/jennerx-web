/**
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Women;

/**
 * @author Safwan
 *
 */
public interface WomenService {
	
	Women findById (int mappedId);
	
	List<Women> getAllWomen (boolean readOnly);
	
	List<Women> getWomen (String partialName, String nic);
	
	Serializable save(Women women);

}
