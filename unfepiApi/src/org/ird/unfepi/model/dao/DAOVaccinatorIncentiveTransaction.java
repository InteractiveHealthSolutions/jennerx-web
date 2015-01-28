package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction.TranscationStatus;

public interface DAOVaccinatorIncentiveTransaction extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	VaccinatorIncentiveTransaction findById(int VaccinatorIncentiveTransactionId,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveTransaction> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveTransaction> findByCriteria(
			Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);

}
