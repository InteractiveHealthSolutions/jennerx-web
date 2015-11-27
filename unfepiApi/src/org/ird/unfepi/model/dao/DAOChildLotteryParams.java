package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildLotteryParams;

public interface DAOChildLotteryParams extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	ChildLotteryParams findById(short childLotteryParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<ChildLotteryParams> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<ChildLotteryParams> findByCriteria(Short enrollmentVaccine, Short receivedVaccine,
			Date createdDateLower, Date createdDateUpper, Float criteriaRangeLower,
			Float criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

}
