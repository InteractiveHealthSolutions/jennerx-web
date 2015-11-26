package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.IncentiveParams;

public interface DAOIncentiveParams extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	IncentiveParams findById(short incentiveParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<IncentiveParams> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<IncentiveParams> findByCriteria(Short vaccineId, Integer armId, Short roleId,
			Date createdDateLower, Date createdDateUpper, Float criteriaRangeLower,
			Float criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
}
