package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.OtherResponse;
import org.ird.unfepi.model.OtherResponse.OtherResponseType;
import org.ird.unfepi.model.dao.DAOOtherResponse;

public class DAOOtherResponseImpl extends DAOHibernateImpl implements DAOOtherResponse{
	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOOtherResponseImpl(Session session) {
		super(session);
		this.session=session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOChild#LAST_QUERY_TOTAL_ROW_COUNT()
	 */
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public OtherResponse findById(int id) {
		OtherResponse sms = (OtherResponse) session.get(OtherResponse.class, id);
		setLAST_QUERY_TOTAL_ROW_COUNT(sms == null ? 0 : 1);
		return sms;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OtherResponse> findByCriteria(Integer mappedId, Date begindate,
			Date enddate, String cellNumber, OtherResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(OtherResponse.class);
		if (begindate != null && enddate != null) {
			cri.add(Restrictions.between("recievedDate", begindate, enddate));
		}
		if (cellNumber != null) {
			cri.add(Restrictions.like("originatorCellNumber", cellNumber, MatchMode.END));
		}
		if (mappedId != null) {
			cri.add(Restrictions.eq("mappedId", mappedId));
		}
		if (responseType != null) {
			cri.add(Restrictions.eq("responseType", responseType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<OtherResponse> list = cri.addOrder(Order.desc("recievedDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OtherResponse> findByCriteria(String programId, Date begindate,
			Date enddate, String cellNumber, OtherResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(OtherResponse.class);
		if (begindate != null && enddate != null) {
			cri.add(Restrictions.between("recievedDate", begindate, enddate));
		}
		if (cellNumber != null) {
			cri.add(Restrictions.like("originatorCellNumber", cellNumber, MatchMode.END));
		}
		if (programId != null) {
			cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
		}
		if (responseType != null) {
			cri.add(Restrictions.eq("responseType", responseType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<OtherResponse> list = cri.addOrder(Order.desc("recievedDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OtherResponse> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin){
		Criteria cri= session.createCriteria(OtherResponse.class)
				.setReadOnly(isreadonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<OtherResponse> list = cri.addOrder(Order.desc("recievedDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
