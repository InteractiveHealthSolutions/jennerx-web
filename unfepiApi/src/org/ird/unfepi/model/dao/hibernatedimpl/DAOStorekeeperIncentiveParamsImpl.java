package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.StorekeeperIncentiveParams;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParams;

public class DAOStorekeeperIncentiveParamsImpl extends DAOHibernateImpl implements DAOStorekeeperIncentiveParams{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOStorekeeperIncentiveParamsImpl(Session session) {
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
	public StorekeeperIncentiveParams findById(short storekeeperIncentiveParamsId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParams.class)
				.add(Restrictions.eq("storekeeperIncentiveParamsId", storekeeperIncentiveParamsId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveParams> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveParams> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParams.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("createdDate"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<StorekeeperIncentiveParams> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StorekeeperIncentiveParams> findByCriteria(Date createdDateLower, Date createdDateUpper
			, Integer criteriaRangeLower, Integer criteriaRangeUpper
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(StorekeeperIncentiveParams.class).setReadOnly(readonly);
		
		if(createdDateLower != null && createdDateUpper != null){
			cri.add(Restrictions.between("createdDate", createdDateLower, createdDateUpper));
		}
		
		if(criteriaRangeLower != null && criteriaRangeUpper != null){
			cri.add(Restrictions.le("criteriaRangeMin", criteriaRangeLower))
			.add(Restrictions.ge("criteriaRangeMax", criteriaRangeUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<StorekeeperIncentiveParams> list = cri.addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
