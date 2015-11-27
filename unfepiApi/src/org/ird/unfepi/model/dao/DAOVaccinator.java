/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Vaccinator;

/**
 * The Interface DAOVaccinator.
 */
public interface DAOVaccinator extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the vaccinator
	 */
	Vaccinator findById(int id);

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<Vaccinator> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	Vaccinator findById(int mappedId, boolean readonly, String[] mappingsToJoin);

	Vaccinator findById(String programId, boolean readonly,	String[] mappingsToJoin);

	List<Vaccinator> findByCriteria(String partOfName, String nic, Gender gender, Integer vaccinationCenterId
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
}
