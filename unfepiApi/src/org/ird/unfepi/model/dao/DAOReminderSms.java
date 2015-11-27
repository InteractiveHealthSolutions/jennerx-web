/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.exception.ReminderDataException;

/**
 * The Interface DAOReminderSms.
 */
public interface DAOReminderSms extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @param isreadonly the isreadonly
	 * @param collectionFetchMode the collection fetch mode
	 * @return the reminder sms
	 */
	ReminderSms findById(int id,boolean isreadonly, String[] mappingsToJoin);

	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	/**
	 * Find by criteria.
	 *
	 * @param childId the child id
	 * @param reminderName the reminder name
	 * @param vaccineName the vaccine name
	 * @param reminderDuedatesmaller the reminder duedatesmaller
	 * @param reminderDuedategreater the reminder duedategreater
	 * @param reminderSentdatesmaller the reminder sentdatesmaller
	 * @param reminderSentdategreater the reminder sentdategreater
	 * @param reminderStatus the reminder status
	 * @param putNotWithReminderStatus the put not with reminder status
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @param isreadonly the isreadonly
	 * @param collectionFetchMode the collection fetch mode
	 * @return the list
	 * @throws ReminderDataException the reminder data exception
	 */
	List<ReminderSms> findByCriteria(Integer mappedId,Short[] reminder, Short[] vaccine, ReminderType[] reminderType, 
			String recipient, Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	/**
	 * Gets the all.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @param isreadonly the isreadonly
	 * @param collectionFetchMode the collection fetch mode
	 * @return the all
	 */
	List<ReminderSms> getAll(int firstResult, int fetchsize,boolean isreadonly
			, String[] mappingsToJoin);


	/**
	 * Find by criteria.
	 *
	 * @param childId the child id
	 * @param reminderName the reminder name
	 * @param vaccineName the vaccine name
	 * @param reminderDuedatesmaller the reminder duedatesmaller
	 * @param reminderDuedategreater the reminder duedategreater
	 * @param reminderStatus the reminder status
	 * @param putNotWithReminderStatus the put not with reminder status
	 * @param dayNumber the day number
	 * @param vaccinationRecordNum the vaccination record num
	 * @param isreadonly the isreadonly
	 * @param collectionFetchMode the collection fetch mode
	 * @return the list
	 * @throws ReminderDataException the reminder data exception
	 */
	List<ReminderSms> findByCriteria(String vaccineName ,REMINDER_STATUS reminderStatus
			,boolean putNotWithReminderStatus,Short dayNumber,Integer vaccinationRecordNum
			,boolean isreadonly, String[] mappingsToJoin);

}
