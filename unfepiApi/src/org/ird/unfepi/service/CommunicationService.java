package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.Response.ResponseStatus;
import org.ird.unfepi.model.Response.ResponseType;

public interface CommunicationService {
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	CommunicationNote findCommunicationNoteById(int id);
	
	List<CommunicationNote> getCommunicationNoteByGroupId(String groupId, CommunicationEventType[] type, CommunicationCategory[] category, boolean isreadonly, String[] mappingsToJoin);

	List<CommunicationNote> findCommunicationNoteByCriteria(Date begindate, Date enddate, String[] source, 
			String[] receiver, CommunicationEventType[] type, CommunicationCategory[] category, 
			Class probeClass, Integer probeId, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter);
	
	List<CommunicationNote> getAllCommunicationNote(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	
	List<Response> getAllResponse(int firstResult,int fetchSize,boolean isreadonly, String[] mappingsToJoin);
	
	List<Response> getResponseByCriteria(Integer mappedId, Short[] roleId, boolean notWithRole, Date begindate, Date enddate, 
			String[] originator, String[] recipient, ResponseType[] responseType, ResponseStatus[] responseStatus, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter);
	
	Response getResponse(int responseId, boolean isreadonly, String[] mappingsToJoin);

	void updateResponse(Response response);
	
	Serializable saveResponse(Response response);
}
