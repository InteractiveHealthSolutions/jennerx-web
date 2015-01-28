package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.dao.DAODailySummary;

public class DAODailySummaryImpl extends DAOHibernateImpl implements DAODailySummary 
{
	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAODailySummaryImpl(Session session) {
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
	public DailySummary findBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(DailySummary.class).setReadOnly(readonly)
				.add(Restrictions.eq("serialNumber", serialNumber));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<DailySummary> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DailySummary> findByCriteria(Integer vaccinationCenterId,
			Integer vaccinatorId, Date datelower, Date dateUpper, boolean readonly
			, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(DailySummary.class).setReadOnly(readonly);
		
		if(vaccinationCenterId != null){
			cri.add(Restrictions.eq("vaccinationCenterId", vaccinationCenterId));
		}
		if(vaccinatorId != null){
			cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
		}
		if(datelower!=null && dateUpper!=null){
			cri.add(Restrictions.between("summaryDate", datelower, dateUpper));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<DailySummary> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).addOrder(Order.desc("summaryDate")).list();
		return list;
	}
}
