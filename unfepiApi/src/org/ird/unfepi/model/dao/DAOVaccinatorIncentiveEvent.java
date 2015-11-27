package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.VaccinatorIncentiveEvent;

public interface DAOVaccinatorIncentiveEvent extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<VaccinatorIncentiveEvent> findByCriteria(Date dateOfEventLower,
			Date dateOfEventUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveEvent> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	VaccinatorIncentiveEvent findById(int vaccinatorIncentiveEventId,
			boolean readonly, String[] mappingsToJoin);

}
