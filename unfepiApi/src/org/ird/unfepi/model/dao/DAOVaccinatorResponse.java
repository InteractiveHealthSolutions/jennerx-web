/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.VaccinatorResponse;
import org.ird.unfepi.model.VaccinatorResponse.VaccinatorResponseType;

/**
 * The Interface DAOVaccinatorResponse.
 */
public interface DAOVaccinatorResponse extends DAO
{
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	VaccinatorResponse getById(int id);
	
	List<VaccinatorResponse> getAll(int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);
	
	List<VaccinatorResponse> findByCriteria(Integer mappedId, String vacCellNumber, VaccinatorResponseType responseType
			, Date recievedDateLower, Date recievedDateUpper, String textContainingString
			,int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);
}
