package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperIncentiveEvent;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveEvent;

public class DAOStorekeeperIncentiveEventImpl extends DAOHibernateImpl implements DAOStorekeeperIncentiveEvent{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOStorekeeperIncentiveEventImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public StorekeeperIncentiveEvent findById(int storekeeperIncentiveEventId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveEvent.class)
				.add(Restrictions.eq("storekeeperIncentiveEventId", storekeeperIncentiveEventId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveEvent> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveEvent> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveEvent.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("dateOfEvent"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveEvent> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveEvent> findByCriteria(Date dateOfEventLower, Date dateOfEventUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveEvent.class).setReadOnly(readonly);
		
		if(dateOfEventLower != null && dateOfEventUpper != null){
			cri.add(Restrictions.between("dateOfEvent", dateOfEventLower, dateOfEventUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<StorekeeperIncentiveEvent> list = cri.addOrder(Order.desc("dateOfEvent")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
