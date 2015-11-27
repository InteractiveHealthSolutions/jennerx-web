package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.VaccinatorIncentiveParams;

public interface DAOVaccinatorIncentiveParams extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	VaccinatorIncentiveParams findById(short vaccinatorIncentiveParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParams> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParams> findByCriteria(Date createdDateLower,
			Date createdDateUpper, Integer criteriaRangeLower,
			Integer criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

}
