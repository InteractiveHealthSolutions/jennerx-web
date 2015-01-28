package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;

public interface CommunicationService {
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	List<Response> getAllResponse(int firstResult,int fetchSize,boolean isreadonly, String[] mappingsToJoin);
	
	List<Response> getResponseByCriteria(Integer mappedId, Short[] roleId, boolean notWithRole, Date begindate, Date enddate, 
			String[] originator, String[] recipient, ResponseType[] responseType, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter);
	
	Response getResponse(int responseId, boolean isreadonly, String[] mappingsToJoin);

	void updateResponse(Response response);
	
	Serializable saveResponse(Response response);
}
