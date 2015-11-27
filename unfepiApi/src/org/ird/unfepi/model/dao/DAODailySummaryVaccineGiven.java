package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.DailySummaryVaccineGiven;

public interface DAODailySummaryVaccineGiven extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	DailySummaryVaccineGiven findBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin);
	
	List<DailySummaryVaccineGiven> findByCriteria(Integer vaccineId, String vaccineName, Integer dailySummaryId, Boolean vaccineExists, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);
}
