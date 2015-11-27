/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseStatus;
import org.ird.unfepi.model.Response.ResponseType;

public interface DAOResponse extends DAO {
	
	Response findById(int id);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	
	List<Response> findByCriteria(Integer mappedId, Short[] roleId, boolean notWithRole, Date begindate, Date enddate, 
			String[] originator, String[] recipient, ResponseType[] responseType, ResponseStatus[] responseStatus, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter);
	
	List<Response> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);
}
