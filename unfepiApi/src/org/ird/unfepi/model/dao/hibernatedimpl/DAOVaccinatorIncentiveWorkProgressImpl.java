package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveWorkProgress;

public class DAOVaccinatorIncentiveWorkProgressImpl extends DAOHibernateImpl implements DAOVaccinatorIncentiveWorkProgress{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinatorIncentiveWorkProgressImpl(Session session) {
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
	public VaccinatorIncentiveWorkProgress findById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveWorkProgress.class)
				.add(Restrictions.eq("serialNumber", serialNumber)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveWorkProgress> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveWorkProgress> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveWorkProgress.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("serialNumber"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveWorkProgress> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveWorkProgress> findByCriteria(Integer vaccinatorIncentiveParticipantId
			, String workType, String workValue
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveWorkProgress.class).setReadOnly(readonly);
		
		if(vaccinatorIncentiveParticipantId != null){
			cri.add(Restrictions.eq("vaccinatorIncentiveParticipantId", vaccinatorIncentiveParticipantId));
		}
		if(workType != null){
			cri.add(Restrictions.eq("workType", workType));
		}
		if(workValue != null){
			cri.add(Restrictions.eq("workValue", workValue));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<VaccinatorIncentiveWorkProgress> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
