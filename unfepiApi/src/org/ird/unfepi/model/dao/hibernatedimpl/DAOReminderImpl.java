/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.dao.DAOReminder;

/**
 * The Class DAOReminderImpl.
 */
public class DAOReminderImpl extends DAOHibernateImpl implements DAOReminder{
	
	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO reminder impl.
	 *
	 * @param session the session
	 */
	public DAOReminderImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOReminder#findById(int)
	 */
	@Override
	public Reminder findById(short id) {
		Reminder rem= (Reminder) session.get(Reminder.class,id);
		setLAST_QUERY_TOTAL_ROW_COUNT(rem==null?0:1);
		return rem;
	}
	
	/**
	 * Sets the lAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW_COUNT the new lAS t_ quer y_ tota l_ ro w__ count
	 */
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOReminder#LAST_QUERY_TOTAL_ROW_COUNT()
	 */
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Reminder.class)
					.addOrder(Order.asc("remindername"))
					.setReadOnly(isreadonly);
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Reminder> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOReminder#getReminderByName(java.lang.String, boolean, org.hibernate.FetchMode)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Reminder getReminderByName(String reminderName,boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri =session.createCriteria(Reminder.class)
							.add(Restrictions.like("remindername", reminderName,MatchMode.EXACT))
							.setReadOnly(isreadonly);
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Reminder> r = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(r.size());
		if(r.size()>0){
			return r.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Reminder getReminderById(short reminderid, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri =session.createCriteria(Reminder.class)
							.add(Restrictions.eq("reminderId", reminderid))
							.setReadOnly(isreadonly);
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Reminder> r = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(r.size());
		if(r.size()>0){
			return r.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> findReminder(String reminderName, ReminderType reminderType, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri=session.createCriteria(Reminder.class).setReadOnly(isreadonly);
				
		if(reminderName != null){
			cri.add(Restrictions.like("remindername", reminderName,MatchMode.ANYWHERE));
		}
		
		if(reminderType != null){
			cri.add(Restrictions.eq("reminderType", reminderType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Reminder> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
