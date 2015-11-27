package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.model.dao.DAODailySummaryVaccineGiven;

public class DAODailySummaryVaccineGivenImpl extends DAOHibernateImpl implements DAODailySummaryVaccineGiven 
{
	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAODailySummaryVaccineGivenImpl(Session session) {
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
	public DailySummaryVaccineGiven findBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(DailySummaryVaccineGiven.class).setReadOnly(readonly)
				.add(Restrictions.eq("summaryVaccineId", serialNumber));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<DailySummaryVaccineGiven> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DailySummaryVaccineGiven> findByCriteria(Integer vaccineId,
			String vaccineName, Integer dailySummaryId, Boolean vaccineExists,
			boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(DailySummaryVaccineGiven.class).setReadOnly(readonly);
		
		if(vaccineId != null){
			cri.add(Restrictions.eq("vaccineId", vaccineId));
		}
		if(vaccineName != null){
			cri.add(Restrictions.eq("vaccineName", vaccineName));
		}
		if(dailySummaryId != null){
			cri.add(Restrictions.eq("dailySummaryId", dailySummaryId));
		}
		if(vaccineExists != null){
			cri.add(Restrictions.eq("vaccineExists", vaccineExists));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<DailySummaryVaccineGiven> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).addOrder(Order.asc("dailySummaryId")).list();
		return list;
	}
}
