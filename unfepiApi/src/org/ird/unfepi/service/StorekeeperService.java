package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Storekeeper;

public interface StorekeeperService {
	
	Storekeeper findById(int Id);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	Storekeeper findStorekeeperById(int mappedId, boolean readonly, String[] mappingsToJoin);

	Storekeeper findStorekeeperById(String programId, boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> getAllStorekeeper(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> getAllStorekeeper(boolean readonly, String[] mappingsToJoin);

	List<Storekeeper> findStorekeeperByCriteria(String partOfName, String storeName, Integer closestVaccinationCenterId, String nic,
			Gender gender, int firstResult,	int fetchsize, boolean readonly, String[] mappingsToJoin);

	Serializable saveStorekeeper(Storekeeper storekeeper);

	void updateStorekeeper(Storekeeper storekeeper);

	Storekeeper mergeUpdateStorekeeper(Storekeeper storekeeper);

}
