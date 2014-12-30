package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.dao.DAOLotterySms;

public class DAOLotterySmsImpl extends DAOHibernateImpl implements DAOLotterySms{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOLotterySmsImpl(Session session) {
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
	public LotterySms findBySerialNumber(int serialNumber, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(LotterySms.class)
				.add(Restrictions.eq("serialNumber", serialNumber)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LotterySms> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LotterySms> findByChild(int mappedId, boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(LotterySms.class)
				.add(Restrictions.eq("mappedId", mappedId)).setReadOnly(isreadonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<LotterySms> list = cri.addOrder(Order.desc("datePreferenceChanged")).addOrder(Order.desc("createdDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LotterySms> findByCriteria(String programId,
			Date datePreferenceChangedlower, Date datePreferenceChangedUpper,
			String preferredSmsTiming, Boolean hasApprovedLottery,
			Boolean hasApprovedReminders, Boolean hasCellPhoneAccess,
			boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(LotterySms.class);
		if (datePreferenceChangedlower != null && datePreferenceChangedUpper != null) {
			cri.add(Restrictions.between("datePreferenceChanged", datePreferenceChangedlower, datePreferenceChangedUpper));
		}
		if (hasApprovedLottery != null) {
			cri.add(Restrictions.eq("hasApprovedLottery", hasApprovedLottery));
		}
		if (programId != null) {
			cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
		}
		if (hasApprovedReminders != null) {
			cri.add(Restrictions.eq("hasApprovedReminders", hasApprovedReminders));
		}
		if (hasCellPhoneAccess != null) {
			cri.add(Restrictions.eq("hasCellPhoneAccess", hasCellPhoneAccess));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<LotterySms> list = cri.addOrder(Order.desc("datePreferenceChanged")).addOrder(Order.desc("createdDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

}
