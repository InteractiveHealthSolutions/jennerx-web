/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.OtherResponse;
import org.ird.unfepi.model.OtherResponse.OtherResponseType;

/**
 * The Interface DAOOtherResponse.
 */
public interface DAOOtherResponse extends DAO{
	
	OtherResponse findById(int id);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<OtherResponse> findByCriteria(Integer mappedId, Date begindate,
			Date enddate, String cellNumber, OtherResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin);
	
	List<OtherResponse> findByCriteria(String programId, Date begindate,
			Date enddate, String cellNumber, OtherResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin);
	
	List<OtherResponse> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);
}
