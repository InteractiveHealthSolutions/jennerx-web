package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseStatus;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.dao.DAOCommunicationNote;
import org.ird.unfepi.model.dao.DAOResponse;
import org.ird.unfepi.service.CommunicationService;

public class CommunicationServiceImpl implements CommunicationService{

	DAOResponse daoresp;
	DAOCommunicationNote daocomm;
	
	public CommunicationServiceImpl(DAOResponse daoresp, DAOCommunicationNote daocomm){
		this.daoresp=daoresp;
		this.daocomm = daocomm;
	}

	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Response.class){
			return daoresp.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		if(clazz == CommunicationNote.class){
			return daocomm.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		return null;
	}
	
	@Override
	public List<Response> getAllResponse(int firstResult,int fetchSize,boolean isreadonly, String[] mappingsToJoin) {
		List<Response> resplst=daoresp.getAll(firstResult, fetchSize,isreadonly,mappingsToJoin);
		return resplst;
	}

	@Override
	public List<Response> getResponseByCriteria(Integer mappedId, Short[] roleId, boolean notWithRole, Date begindate, Date enddate, 
			String[] originator, String[] recipient, ResponseType[] responseType, ResponseStatus[] responseStatus, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter) {
		List<Response> resplst=daoresp.findByCriteria(mappedId, roleId, notWithRole, begindate, enddate, originator, recipient, responseType, 
				responseStatus, referenceNumber, eventClass, eventId, bodyMatches, firstResult, fetchsize, isreadonly, mappingsToJoin, sqlFilter);
		return resplst;
	}

	@Override
	public Response getResponse(int responseId,boolean isreadonly, String[] mappingsToJoin) {
		Response obj=daoresp.findById(responseId);
		return obj;
	}
	
	@Override
	public Serializable saveResponse(Response response) {
		return daoresp.save(response);
	}
	@Override
	public void updateResponse(Response response) {
		daoresp.update(response);
	}

	@Override
	public CommunicationNote findCommunicationNoteById(int id) {
		return daocomm.findById(id);
	}
	
	@Override
	public List<CommunicationNote> getCommunicationNoteByGroupId(String groupId, CommunicationEventType[] type, CommunicationCategory[] category, boolean isreadonly, String[] mappingsToJoin) {
		return daocomm.getByGroupId(groupId, type, category, isreadonly, mappingsToJoin);
	}

	@Override
	public List<CommunicationNote> findCommunicationNoteByCriteria(
			Date begindate, Date enddate, String[] source, String[] receiver,
			CommunicationEventType[] type, CommunicationCategory[] category,
			Class probeClass, Integer probeId, int firstResult, int fetchsize,
			boolean isreadonly, String[] mappingsToJoin, String sqlFilter) {
		return daocomm.findByCriteria(begindate, enddate, source, receiver, type, category, probeClass, probeId, firstResult, fetchsize, isreadonly, mappingsToJoin, sqlFilter);
	}

	@Override
	public List<CommunicationNote> getAllCommunicationNote(int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) {
		return daocomm.getAll(firstResult, fetchsize, isreadonly, mappingsToJoin);
	}
}
