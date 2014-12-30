package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.StorekeeperIncentiveParams;

public interface DAOStorekeeperIncentiveParams extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	StorekeeperIncentiveParams findById(short storekeeperIncentiveParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParams> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParams> findByCriteria(Date createdDateLower,
			Date createdDateUpper, Integer criteriaRangeLower,
			Integer criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

}
