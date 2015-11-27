package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveTransaction;

public class DAOStorekeeperIncentiveTransactionImpl extends DAOHibernateImpl implements DAOStorekeeperIncentiveTransaction{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOStorekeeperIncentiveTransactionImpl(Session session) {
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
	public StorekeeperIncentiveTransaction findById(int StorekeeperIncentiveTransactionId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveTransaction.class)
				.add(Restrictions.eq("storekeeperIncentiveTransactionId", StorekeeperIncentiveTransactionId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveTransaction> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveTransaction> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveTransaction.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("createdDate"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveTransaction> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveTransaction> findByCriteria(Integer storekeeperIncentiveEventId,
			Integer storekeeperId, TranscationStatus transactionStatus
			,Float amountDueLower, Float amountDueUpper, Date createdDateLower, Date createdDateUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveTransaction.class).setReadOnly(readonly);
		
		if(storekeeperIncentiveEventId != null){
			cri.add(Restrictions.eq("storekeeperIncentiveEventId", storekeeperIncentiveEventId));
		}
		if(storekeeperId != null){
			cri.add(Restrictions.eq("storekeeperId", storekeeperId));
		}
		if(transactionStatus != null){
			cri.add(Restrictions.eq("transactionStatus", transactionStatus));
		}
				
		if(amountDueLower != null && amountDueUpper != null){
			cri.add(Restrictions.between("amountDue", amountDueLower, amountDueUpper));
		}
	
		if(createdDateLower != null && createdDateUpper != null){
			cri.add(Restrictions.between("createdDate", createdDateLower, createdDateUpper));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<StorekeeperIncentiveTransaction> list = cri.addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
