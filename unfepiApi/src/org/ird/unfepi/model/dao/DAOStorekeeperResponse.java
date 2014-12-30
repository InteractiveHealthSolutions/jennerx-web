/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.StorekeeperResponse;
import org.ird.unfepi.model.StorekeeperResponse.StorekeeperResponseType;

/**
 * The Interface DAOStorekeeperResponse.
 */
public interface DAOStorekeeperResponse extends DAO{
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	StorekeeperResponse getById(long id);
	
	List<StorekeeperResponse> getAll(int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);
	
	List<StorekeeperResponse> findByCriteria(Integer mappedId, String skCellNumber, StorekeeperResponseType responseType
			, Date recievedDateLower, Date recievedDateUpper, String textContainingString
			,int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);

}