/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.ArmDayReminderId;
import org.ird.unfepi.model.dao.DAOArmDayReminder;

/**
 * The Class DAOArmDayReminderImpl.
 */
public class DAOArmDayReminderImpl extends DAOHibernateImpl implements DAOArmDayReminder {

	/** The session. */
	private Session session;

	/**
	 * Instantiates a new dAO arm day reminder impl.
	 *
	 * @param session the session
	 */
	public DAOArmDayReminderImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOArmDayReminder#findById(org.ird.unfepi.model.ArmDayReminderId)
	 */
	@Override
	public ArmDayReminder findById(ArmDayReminderId armdayReminderID) {
			return (ArmDayReminder) session.get("ArmDayReminder",armdayReminderID);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOArmDayReminder#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArmDayReminder> getAll(){
		return session.createQuery("from ArmDayReminder").list();
	}
	
	/**
	 * find records by specified criteria, keep arguments null you want to exclude from criteria,
	 * invalid values will automatically excluded from search criteria.
	 *
	 * @param armId the arm id
	 * @param vaccineId the vaccine id
	 * @param reminderId the reminder id
	 * @param dayNum the day num
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ArmDayReminder> findByIdsCriteria(Short armId,Short vaccineId,Short reminderId,Short dayNum) {
		Criteria cri=session.createCriteria(ArmDayReminder.class);
		if(armId!=null){
			cri.add(Restrictions.eq("id.armId",armId));
		}
		if(vaccineId!=null){
			cri.add(Restrictions.eq("id.vaccineId",vaccineId));
		}
		if(reminderId!=null){
			cri.add(Restrictions.eq("id.reminderId",reminderId));
		}
		if(dayNum!=null){
			cri.add(Restrictions.eq("id.dayNumber",dayNum));
		}
		return cri.list();
	}
}
