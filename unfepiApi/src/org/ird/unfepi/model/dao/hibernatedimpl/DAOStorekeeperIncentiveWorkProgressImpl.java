package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveWorkProgress;

public class DAOStorekeeperIncentiveWorkProgressImpl extends DAOHibernateImpl implements DAOStorekeeperIncentiveWorkProgress{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOStorekeeperIncentiveWorkProgressImpl(Session session) {
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
	public StorekeeperIncentiveWorkProgress findById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveWorkProgress.class)
				.add(Restrictions.eq("serialNumber", serialNumber)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveWorkProgress> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveWorkProgress> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveWorkProgress.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("serialNumber"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveWorkProgress> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveWorkProgress> findByCriteria(Integer StorekeeperIncentiveParticipantId
			, Integer transactionsLower, Integer transactionsUpper, Float transactionsAmountLower, Float transactionsAmountUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveWorkProgress.class).setReadOnly(readonly);
		
		if(StorekeeperIncentiveParticipantId != null){
			cri.add(Restrictions.eq("storekeeperIncentiveParticipantId", StorekeeperIncentiveParticipantId));
		}
		if(transactionsLower != null && transactionsUpper != null){
			cri.add(Restrictions.between("transactions", transactionsLower, transactionsUpper));
		}
		if(transactionsLower != null && transactionsUpper != null){
			cri.add(Restrictions.between("totalTransactionsAmount", transactionsAmountLower, transactionsAmountUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<StorekeeperIncentiveWorkProgress> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
