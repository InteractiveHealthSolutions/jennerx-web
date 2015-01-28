/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.ArmDayReminderId;

/**
 * The Interface DAOArmDayReminder.
 */
public interface DAOArmDayReminder extends DAO{
	
	/**
	 * Find by id.
	 *
	 * @param armdayReminderID the armday reminder id
	 * @return the arm day reminder
	 */
	ArmDayReminder findById(ArmDayReminderId armdayReminderID);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<ArmDayReminder> getAll();
	
	/**
	 * Find by ids criteria.
	 *
	 * @param armId the arm id
	 * @param vaccineId the vaccine id
	 * @param reminderId the reminder id
	 * @param dayNum the day num
	 * @return the list
	 */
	List<ArmDayReminder> findByIdsCriteria(Short armId, Short vaccineId, Short reminderId, Short dayNum);
}