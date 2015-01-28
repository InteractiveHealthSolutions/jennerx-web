/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Storekeeper;

/**
 * The Interface DAOStorekeeper.
 */
public interface DAOStorekeeper extends DAO{

	/**
	 * Find by id.
	 *
	 * @param Id the id
	 * @return the storekeeper
	 */
	Storekeeper findById(int Id);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	Storekeeper findById(int mappedId, boolean readonly, String[] mappingsToJoin);

	Storekeeper findById(String programId, boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> getAll(boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> findByCriteria(String partOfName, String storeName, Integer closestVaccinationCenterId, String nic,
			Gender gender, int firstResult,	int fetchsize, boolean readonly, String[] mappingsToJoin);
}
