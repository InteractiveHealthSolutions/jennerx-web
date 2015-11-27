package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParticipant;

public class DAOStorekeeperIncentiveParticipantImpl extends DAOHibernateImpl implements DAOStorekeeperIncentiveParticipant{
	
	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOStorekeeperIncentiveParticipantImpl(Session session) {
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
	public StorekeeperIncentiveParticipant findById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParticipant.class)
				.add(Restrictions.eq("serialNumber", serialNumber)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveParticipant> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveParticipant> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParticipant.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("serialNumber"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveParticipant> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveParticipant> findByCriteria(Integer storekeeperIncentiveEventId,
			Integer storekeeperId, Short storekeeperIncentiveParamsId, Boolean isIncentivised
			,Float criteriaElementValueLower, Float criteriaElementValueUpper, Date incentiveDateLower, Date incentiveDateUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParticipant.class).setReadOnly(readonly);
		
		if(storekeeperIncentiveEventId != null){
			cri.add(Restrictions.eq("storekeeperIncentiveEventId", storekeeperIncentiveEventId));
		}
		if(storekeeperId != null){
			cri.add(Restrictions.eq("storekeeperId", storekeeperId));
		}
		if(storekeeperIncentiveParamsId != null){
			cri.add(Restrictions.eq("storekeeperIncentiveParamsId", storekeeperIncentiveParamsId));
		}
		if(isIncentivised != null){
			cri.add(Restrictions.eq("isIncentivised", isIncentivised));
		}
				
		if(criteriaElementValueLower != null && criteriaElementValueUpper != null){
			cri.add(Restrictions.between("criteriaElementValue", criteriaElementValueLower, criteriaElementValueUpper));
		}
	
		if(incentiveDateLower!= null && incentiveDateUpper != null){
			cri.createAlias("storekeeperIncentiveEvent", "event").add(Restrictions.between("event.dateOfEvent", incentiveDateLower, incentiveDateUpper));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<StorekeeperIncentiveParticipant> list = cri.addOrder(Order.desc("storekeeperIncentiveEventId")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
