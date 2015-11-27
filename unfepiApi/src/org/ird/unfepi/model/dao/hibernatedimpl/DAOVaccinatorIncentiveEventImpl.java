package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VaccinatorIncentiveEvent;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveEvent;

public class DAOVaccinatorIncentiveEventImpl extends DAOHibernateImpl implements DAOVaccinatorIncentiveEvent{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinatorIncentiveEventImpl(Session session) {
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
	public VaccinatorIncentiveEvent findById(int vaccinatorIncentiveEventId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveEvent.class)
				.add(Restrictions.eq("vaccinatorIncentiveEventId", vaccinatorIncentiveEventId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveEvent> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveEvent> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveEvent.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("dateOfEvent"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveEvent> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveEvent> findByCriteria(Date dateOfEventLower, Date dateOfEventUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveEvent.class).setReadOnly(readonly);
		
		if(dateOfEventLower != null && dateOfEventUpper != null){
			cri.add(Restrictions.between("dateOfEvent", dateOfEventLower, dateOfEventUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<VaccinatorIncentiveEvent> list = cri.addOrder(Order.desc("dateOfEvent")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
