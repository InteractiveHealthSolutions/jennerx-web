package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.StorekeeperIncentiveParticipant;

public interface DAOStorekeeperIncentiveParticipant extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	StorekeeperIncentiveParticipant findById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParticipant> getAll(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParticipant> findByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			Short storekeeperIncentiveParamsId, Boolean isIncentivised,
			Float criteriaElementValueLower, Float criteriaElementValueUpper, Date incentiveDateLower, Date incentiveDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
