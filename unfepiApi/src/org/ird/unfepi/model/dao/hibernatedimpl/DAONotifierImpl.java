package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;
import org.ird.unfepi.model.dao.DAONotifier;

public class DAONotifierImpl extends DAOHibernateImpl implements DAONotifier{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAONotifierImpl(Session session) {
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
	public Notifier getById(int notifierId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Notifier.class)
				.add(Restrictions.eq("notifierId", notifierId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Notifier> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notifier> findByCritria(String notifierName,
			NotifierType notifierType, NotifierStatus notifierStatus,
			boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Notifier.class);
		
		if(notifierName != null){
			cri.add(Restrictions.like("notifierName", notifierName, MatchMode.ANYWHERE));
		}
		
		if(notifierType != null){
			cri.add(Restrictions.eq("notifierType", notifierType));
		}
		
		if(notifierStatus != null){
			cri.add(Restrictions.eq("notifierStatus", notifierStatus));
		}
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		return cri.addOrder(Order.asc("notifierName"))
				.setReadOnly(readonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
	}

}
