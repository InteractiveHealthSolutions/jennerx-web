/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildResponse;
import org.ird.unfepi.model.ChildResponse.ChildResponseType;

/**
 * The Interface DAOChildResponse.
 */
public interface DAOChildResponse extends DAO {
	
	ChildResponse findById(int id);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<ChildResponse> findByCriteriaIncludeName(
			String partOfFirstOrLastName, Date begindate, Date enddate,
			String cellNumber, String armName, ChildResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin);
	
	List<ChildResponse> findByCriteria(Integer childId, Date begindate,
			Date enddate, String cellNumber, ChildResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin);
	
	List<ChildResponse> findByCriteria(String programId, Date begindate,
			Date enddate, String cellNumber, ChildResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin);
	
	List<ChildResponse> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);
	
	List<ChildResponse> findByVaccinationRecord(int vacciantionRecordNum,boolean isreadonly, String[] mappingsToJoin);

}
