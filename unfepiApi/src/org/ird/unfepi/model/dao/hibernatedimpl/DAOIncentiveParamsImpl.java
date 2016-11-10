package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.IncentiveParams;
import org.ird.unfepi.model.dao.DAOIncentiveParams;

public class DAOIncentiveParamsImpl extends DAOHibernateImpl implements DAOIncentiveParams{
	
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public void setLAST_QUERY_TOTAL_ROW_COUNT(Number lAST_QUERY_TOTAL_ROW_COUNT) {
		LAST_QUERY_TOTAL_ROW_COUNT = lAST_QUERY_TOTAL_ROW_COUNT;
	}

	public DAOIncentiveParamsImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IncentiveParams findById(short incentiveParamsId, boolean readonly,
			String[] mappingsToJoin) {
		
		Criteria cri = session.createCriteria(IncentiveParams.class)
				.add(Restrictions.eq("incentiveParamsId", incentiveParamsId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<IncentiveParams> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IncentiveParams> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		
		Criteria cri = session.createCriteria(IncentiveParams.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("createdDate"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<IncentiveParams> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<IncentiveParams> findByCriteria(Short vaccineId, Integer armId,
			Short roleId, Date createdDateLower, Date createdDateUpper,
			Float criteriaRangeLower, Float criteriaRangeUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		
		Criteria cri = session.createCriteria(IncentiveParams.class).setReadOnly(readonly);
		
		if(createdDateLower != null && createdDateUpper != null){
			cri.add(Restrictions.between("createdDate", createdDateLower, createdDateUpper));
		}
		
		if(vaccineId != null){
			cri.add(Restrictions.eq("vaccineId", vaccineId));
		}
		
		if(armId != null){
			cri.add(Restrictions.eq("armId", armId));
		}
		
		if(roleId != null){
			cri.add(Restrictions.eq("roleId", roleId));
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
		
		List<IncentiveParams> list = cri.addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	} 

}
