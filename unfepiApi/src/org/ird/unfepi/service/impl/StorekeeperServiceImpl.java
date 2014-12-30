package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Storekeeper;
import org.ird.unfepi.model.dao.DAOStorekeeper;
import org.ird.unfepi.service.StorekeeperService;

public class StorekeeperServiceImpl implements StorekeeperService{

	private ServiceContext sc;
	private DAOStorekeeper daostorkeep;
	
	public StorekeeperServiceImpl(ServiceContext sc, DAOStorekeeper daostorkeep) {
		this.sc = sc;
		this.daostorkeep = daostorkeep;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Storekeeper.class){
			return daostorkeep.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}

	@Override
	public Storekeeper findById(int Id) {
		Storekeeper obj = daostorkeep.findById(Id);
		return obj;
	}

	@Override
	public Serializable saveStorekeeper(Storekeeper storekeeper) {
		return daostorkeep.save(storekeeper);
	}

	@Override
	public void updateStorekeeper(Storekeeper storekeeper) {
		daostorkeep.update(storekeeper);
	}

	@Override
	public Storekeeper mergeUpdateStorekeeper(Storekeeper storekeeper) {
		return (Storekeeper) daostorkeep.merge(storekeeper);
	}

	@Override
	public Storekeeper findStorekeeperById(int mappedId, boolean readonly,	String[] mappingsToJoin) {
		Storekeeper obj = daostorkeep.findById(mappedId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public Storekeeper findStorekeeperById(String programId, boolean readonly,	String[] mappingsToJoin) {
		Storekeeper obj = daostorkeep.findById(programId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<Storekeeper> getAllStorekeeper(int firstResult, int fetchsize,	boolean readonly, String[] mappingsToJoin) {
		List<Storekeeper> list = daostorkeep.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<Storekeeper> getAllStorekeeper(boolean readonly, String[] mappingsToJoin) {
		List<Storekeeper> list = daostorkeep.getAll(readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<Storekeeper> findStorekeeperByCriteria(String partOfName, String storeName
			, Integer closestVaccinationCenterId, String nic, Gender gender
			, int firstResult, int fetchsize,	boolean readonly, String[] mappingsToJoin) {
		List<Storekeeper> list = daostorkeep.findByCriteria(partOfName, storeName, closestVaccinationCenterId, nic, gender, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

}
