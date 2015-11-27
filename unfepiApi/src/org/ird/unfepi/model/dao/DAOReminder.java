/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOReminder.
 */
public interface DAOReminder extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the reminder
	 */
	Reminder findById(short id);

	/**
	 * Gets the reminder by name.
	 *
	 * @param reminderName the reminder name
	 * @param isreadonly the isreadonly
	 * @param remTextFetchmode the rem text fetchmode
	 * @return the reminder by name
	 */
	Reminder getReminderByName(String reminderName,boolean isreadonly, String[] mappingsToJoin);

	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	/**
	 * Find reminder.
	 *
	 * @param reminderName the reminder name
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @param isreadonly the isreadonly
	 * @param remTextFetchmode the rem text fetchmode
	 * @return the list
	 */
	List<Reminder> findReminder(String reminderName, ReminderType reminderType, int firstResult,
			int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	/**
	 * Gets the all.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @param isreadonly the isreadonly
	 * @param remTextFetchmode the rem text fetchmode
	 * @return the all
	 */
	List<Reminder> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	Reminder getReminderById(short reminderid, boolean isreadonly, String[] mappingsToJoin);

}
