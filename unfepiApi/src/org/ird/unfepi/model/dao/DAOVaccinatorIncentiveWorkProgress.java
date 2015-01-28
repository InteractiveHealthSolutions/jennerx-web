package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;

public interface DAOVaccinatorIncentiveWorkProgress extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	VaccinatorIncentiveWorkProgress findById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveWorkProgress> getAll(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveWorkProgress> findByCriteria(
			Integer VaccinatorIncentiveParticipantId,
			String workType, String workValue,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
