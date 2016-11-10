package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveTransaction;

public class DAOVaccinatorIncentiveTransactionImpl extends DAOHibernateImpl implements DAOVaccinatorIncentiveTransaction{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinatorIncentiveTransactionImpl(Session session) {
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
	public VaccinatorIncentiveTransaction findById(int VaccinatorIncentiveTransactionId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveTransaction.class)
				.add(Restrictions.eq("vaccinatorIncentiveTransactionId", VaccinatorIncentiveTransactionId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveTransaction> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveTransaction> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveTransaction.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("createdDate"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveTransaction> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveTransaction> findByCriteria(Integer vaccinatorIncentiveEventId,
			Integer vaccinatorId, TranscationStatus transactionStatus
			,Float amountDueLower, Float amountDueUpper, Date createdDateLower, Date createdDateUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveTransaction.class).setReadOnly(readonly);
		
		if(vaccinatorIncentiveEventId != null){
			cri.add(Restrictions.eq("vaccinatorIncentiveEventId", vaccinatorIncentiveEventId));
		}
		if(vaccinatorId != null){
			cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
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
		
		List<VaccinatorIncentiveTransaction> list = cri.addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
