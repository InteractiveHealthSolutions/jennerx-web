package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParticipant;

public class DAOVaccinatorIncentiveParticipantImpl extends DAOHibernateImpl implements DAOVaccinatorIncentiveParticipant{
	
	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinatorIncentiveParticipantImpl(Session session) {
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
	public VaccinatorIncentiveParticipant findById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveParticipant.class)
				.add(Restrictions.eq("serialNumber", serialNumber)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveParticipant> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveParticipant> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveParticipant.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("serialNumber"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentiveParticipant> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentiveParticipant> findByCriteria(Integer vaccinatorIncentiveEventId,
			Integer vaccinatorId, Short vaccinatorIncentiveParamsId, Boolean isIncentivised
			,Integer criteriaElementValueLower, Integer criteriaElementValueUpper, Date lotteryDateLower, Date lotteryDateUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentiveParticipant.class).setReadOnly(readonly);
		
		if(vaccinatorIncentiveEventId != null){
			cri.add(Restrictions.eq("vaccinatorIncentiveEventId", vaccinatorIncentiveEventId));
		}
		if(vaccinatorId != null){
			cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
		}
		if(vaccinatorIncentiveParamsId != null){
			cri.add(Restrictions.eq("vaccinatorIncentiveParamsId", vaccinatorIncentiveParamsId));
		}
		if(isIncentivised != null){
			cri.add(Restrictions.eq("isIncentivised", isIncentivised));
		}
		if(lotteryDateLower!= null && lotteryDateUpper != null){
			cri.createAlias("vaccinatorIncentiveEvent", "event").add(Restrictions.between("event.dateOfEvent", lotteryDateLower, lotteryDateUpper));
		}
		if(criteriaElementValueLower != null && criteriaElementValueUpper != null){
			cri.add(Restrictions.between("criteriaElementValue", criteriaElementValueLower, criteriaElementValueUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<VaccinatorIncentiveParticipant> list = cri.addOrder(Order.desc("vaccinatorIncentiveEventId")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
