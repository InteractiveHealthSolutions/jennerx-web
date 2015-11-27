package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.DailySummary;

public interface DAODailySummary extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	DailySummary findBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin);
	
	List<DailySummary> findByCriteria(Integer vaccinationCenterId, Integer vaccinatorId, Date datelower, Date dateUpper, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);
}
