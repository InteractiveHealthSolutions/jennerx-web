/*package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperResponse;
import org.ird.unfepi.model.StorekeeperResponse.StorekeeperResponseType;
import org.ird.unfepi.model.dao.DAOStorekeeperResponse;

public class DAOStorekeeperResponseImpl extends DAOHibernateImpl implements DAOStorekeeperResponse{

	*//** The session. *//*
	private Session session;
	
	*//** The LAS t_ quer y_ tota l_ ro w__ count. *//*
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOStorekeeperResponseImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	 (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOChild#LAST_QUERY_TOTAL_ROW_COUNT()
	 
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public StorekeeperResponse getById(long id) {
		StorekeeperResponse sms = (StorekeeperResponse) session.get(StorekeeperResponse.class, id);
		setLAST_QUERY_TOTAL_ROW_COUNT(sms == null ? 0 : 1);
		return sms;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperResponse> getAll(int firstResult, int fetchsize,
			boolean isreadonly, String[] mappingsToJoin) {

		Criteria cri = session.createCriteria(StorekeeperResponse.class)
				.addOrder(Order.desc("recievedDate"))
				.setReadOnly(isreadonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperResponse> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperResponse> findByCriteria(Integer mappedId, String skCellNumber,
			StorekeeperResponseType responseType, Date recievedDateLower,
			Date recievedDateUpper, String textContainingString,
			int firstResult, int fetchsize, boolean isreadonly,
			String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(StorekeeperResponse.class);
		
		cri.createAlias("storekeeper", "stor").createAlias("stor.idMapper", "im");

		if(mappedId != null){
			cri.add(Restrictions.eq("im.programId", mappedId));
		}
		if(skCellNumber != null){
			cri.add(Restrictions.eq("recipient", skCellNumber));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		if(recievedDateLower != null && recievedDateUpper != null){
			cri.add(Restrictions.between("recievedDate", recievedDateLower, recievedDateUpper));
		}
		if(textContainingString != null){
			cri.add(Restrictions.like("text", textContainingString, MatchMode.ANYWHERE));
		}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperResponse> list = cri.addOrder(Order.desc("recievedDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
*/