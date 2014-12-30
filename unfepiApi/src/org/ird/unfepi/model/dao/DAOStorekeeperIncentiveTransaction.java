package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus;

public interface DAOStorekeeperIncentiveTransaction extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	StorekeeperIncentiveTransaction findById(int StorekeeperIncentiveTransactionId,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveTransaction> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveTransaction> findByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
