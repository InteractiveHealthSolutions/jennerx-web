package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;

public interface DAOStorekeeperIncentiveWorkProgress extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	StorekeeperIncentiveWorkProgress findById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveWorkProgress> getAll(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveWorkProgress> findByCriteria(
			Integer StorekeeperIncentiveParticipantId,
			Integer transactionsLower, Integer transactionsUpper
			, Float transactionsAmountLower, Float transactionsAmountUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
