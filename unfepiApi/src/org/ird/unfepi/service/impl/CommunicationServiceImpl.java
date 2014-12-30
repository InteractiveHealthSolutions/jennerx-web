package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.dao.DAOResponse;
import org.ird.unfepi.service.CommunicationService;

public class CommunicationServiceImpl implements CommunicationService{

	DAOResponse daoresp;
	
	public CommunicationServiceImpl(DAOResponse daoresp){
		this.daoresp=daoresp;
	}

	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Response.class){
			return daoresp.LAST_QUERY_TOTAL_ROW_COUNT();
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
			String[] originator, String[] recipient, ResponseType[] responseType, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter) {
		List<Response> resplst=daoresp.findByCriteria(mappedId, roleId, notWithRole, begindate, enddate, originator, recipient, responseType, 
				referenceNumber, eventClass, eventId, bodyMatches, firstResult, fetchsize, isreadonly, mappingsToJoin, sqlFilter);
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
	
}
