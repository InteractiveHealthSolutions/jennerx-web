/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.dao.DAOReminderSms;

/**
 * The Class DAOReminderSmsImpl.
 *
 * @author maimoonak
 */
public class DAOReminderSmsImpl extends DAOHibernateImpl implements DAOReminderSms{

	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	/**
	 * Instantiates a new dAO reminder sms impl.
	 *
	 * @param session the session
	 */
	public DAOReminderSmsImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ReminderSms findById(int id, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ReminderSms.class)
								.add(Restrictions.eq("rsmsRecordNum", id)).setReadOnly(isreadonly);
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ReminderSms> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list.size()==0?null:list.get(0);
	}
	
	/**
	 * Sets the lAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW_COUNT the new lAS t_ quer y_ tota l_ ro w__ count
	 */
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ReminderSms.class).setReadOnly(isreadonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ReminderSms> list =  cri.addOrder(Order.desc("dueDate"))
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.setReadOnly(isreadonly)
				.list();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> findByCriteria(Integer mappedId,Short[] reminder, Short[] vaccine, ReminderType[] reminderType, 
			String recipient, Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin)
	{
		Criteria cri=session.createCriteria(ReminderSms.class);
		
		if(mappedId != null || vaccine!=null){
			cri.createAlias("vaccination", "vacc");
			
			if(mappedId != null){
				cri.add(Restrictions.eq("vacc.childId", mappedId));
			}
			if(vaccine != null && vaccine.length > 0){
				cri.add(Restrictions.in("vacc.vaccineId", vaccine));
			}
		}
		if(reminder != null || reminderType != null){
			cri.createAlias("reminder", "rem");
			if(reminder!=null &&  reminder.length > 0){
				cri.add(Restrictions.in("rem.reminderId", reminder));
			}
			if(reminderType != null && reminderType.length > 0){
				cri.add(Restrictions.in("rem.reminderType", reminderType));
			}
		}
		
		if(reminderDuedatesmaller!=null && reminderDuedategreater!=null){
			cri.add(Restrictions.between("dueDate", reminderDuedatesmaller, reminderDuedategreater));
		}
		if(reminderSentdatesmaller!=null && reminderSentdategreater !=null){
			cri.add(Restrictions.between("sentDate", reminderSentdatesmaller, reminderSentdategreater));
		}
		
		if(recipient!=null){
			cri.add(Restrictions.like("recipient", recipient,MatchMode.END));
		}
		
		if(reminderStatus!=null){
			if(putNotWithReminderStatus){
					cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
				}else{
					cri.add(Restrictions.eq("reminderStatus", reminderStatus));
				}
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ReminderSms> list =  cri.addOrder(Order.desc("dueDate"))
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.setReadOnly(isreadonly)
				.list();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderSms> findByCriteria(String vaccineName ,REMINDER_STATUS reminderStatus
			,boolean putNotWithReminderStatus,Short dayNumber,Integer vaccinationRecordNum
			,boolean isreadonly, String[] mappingsToJoin)
	{
		Criteria cri=session.createCriteria(ReminderSms.class);
		if(vaccineName!=null){
			cri.createAlias("vaccination", "vacc").createAlias("vacc.vaccine", "vaccv")
				.add(Restrictions.eq("vaccv.name",vaccineName));
		}
		if(reminderStatus!=null){
			if(putNotWithReminderStatus){
				cri.add(Restrictions.not(Restrictions.eq("reminderStatus", reminderStatus)));
			}else{
				cri.add(Restrictions.eq("reminderStatus", reminderStatus));
			}
		}
		if(dayNumber!=null){
			cri.add(Restrictions.eq("dayNumber", dayNumber));
		}
		if(vaccinationRecordNum!=null){
			cri.add(Restrictions.eq("vaccinationRecordNum", vaccinationRecordNum));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		List<ReminderSms> list =  cri.addOrder(Order.desc("dueDate"))
				.setReadOnly(isreadonly)
				.list();
		return list;
	}
}
