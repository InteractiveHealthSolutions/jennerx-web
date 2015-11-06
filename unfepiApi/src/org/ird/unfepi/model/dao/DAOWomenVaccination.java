/**
 * 
 */
package org.ird.unfepi.model.dao;

import org.ird.unfepi.model.Vaccination;

/**
 * @author Safwan
 *
 */
public interface DAOWomenVaccination extends DAO {
	
	Vaccination findById(int id,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

}
