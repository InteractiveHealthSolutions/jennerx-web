package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.VaccinatorIncentiveParticipant;

public interface DAOVaccinatorIncentiveParticipant extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	VaccinatorIncentiveParticipant findById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParticipant> getAll(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParticipant> findByCriteria(
			Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			Short vaccinatorIncentiveParamsId, Boolean isIncentivised,
			Integer criteriaElementValueLower, Integer criteriaElementValueUpper, Date lotteryDateLower, Date lotteryDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
