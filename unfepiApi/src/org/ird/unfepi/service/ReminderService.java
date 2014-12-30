package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;

public interface ReminderService {

	List<Reminder> getAllReminders(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	Reminder getReminder(String reminderName,boolean isreadonly, String[] mappingsToJoin);

	List<Reminder> findRemindersByName(String reminderName, ReminderType reminderType, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);
	
	Reminder getReminderById(short reminderid, boolean isreadonly, String[] mappingsToJoin);

	Serializable addReminder(Reminder reminder);

	void updateReminder(Reminder reminder);

	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	List<ReminderSms> findReminderSmsRecordByCriteria(Integer mappedId,Short[] reminder, Short[] vaccine, ReminderType[] reminderType, 
			String recipient, Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	List<ReminderSms> getAllReminderSmsRecord(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);

	Serializable addReminderSmsRecord(ReminderSms reminderSms);

	void updateReminderSmsRecord(ReminderSms reminderSms);

	List<ReminderSms> findByCriteria(String vaccineName ,REMINDER_STATUS reminderStatus
			,boolean putNotWithReminderStatus,Short dayNumber,Integer vaccinationRecordNum
			,boolean isreadonly, String[] mappingsToJoin);

	ReminderSms getReminderSmsRecord(int id,boolean isreadonly, String[] mappingsToJoin);
}
