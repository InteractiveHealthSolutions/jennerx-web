package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.StorekeeperIncentiveEvent;

public interface DAOStorekeeperIncentiveEvent extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<StorekeeperIncentiveEvent> findByCriteria(Date dateOfEventLower,
			Date dateOfEventUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveEvent> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	StorekeeperIncentiveEvent findById(int storekeeperIncentiveEventId,
			boolean readonly, String[] mappingsToJoin);

}
