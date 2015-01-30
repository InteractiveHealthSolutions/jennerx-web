package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.dao.DAOReminder;
import org.ird.unfepi.model.dao.DAOReminderSms;
import org.ird.unfepi.service.ReminderService;
import org.ird.unfepi.utils.date.DateUtils;

public class ReminderServiceImpl implements ReminderService{

	private ServiceContext sc;
	private DAOReminder rdao;
	
	private DAOReminderSms rsmsdao;
	
	public ReminderServiceImpl(ServiceContext sc, DAOReminder rem,DAOReminderSms rsmsdao){
		this.sc = sc;
		this.rdao=rem;
		this.rsmsdao=rsmsdao;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Reminder.class){
			return rdao.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == ReminderSms.class){
			return rsmsdao.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}
	
	@Override
	public Serializable addReminder(Reminder reminder) {
		return rdao.save(reminder);
	}
	
	@Override
	public List<Reminder> getAllReminders(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		List<Reminder> rem=rdao.getAll(firstResult, fetchsize,isreadonly,mappingsToJoin);
		return rem;
	}
	
	@Override
	public Reminder getReminder(String reminderName,boolean isreadonly, String[] mappingsToJoin) {
		Reminder rem=rdao.getReminderByName(reminderName,isreadonly,mappingsToJoin);
		return rem;
	}
	
	@Override
	public List<Reminder> findRemindersByName(String reminderName, ReminderType reminderType, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		List<Reminder> reml= rdao.findReminder(reminderName, reminderType, firstResult, fetchsize,isreadonly,mappingsToJoin);
		return reml;
	}

	@Override
	public Reminder getReminderById(short reminderid, boolean isreadonly, String[] mappingsToJoin) {
		return rdao.getReminderById(reminderid, isreadonly, mappingsToJoin);
	}

	@Override
	public void updateReminder(Reminder reminder) {
		rdao.update(reminder);
	}

	@Override
	public List<ReminderSms> findReminderSmsRecordByCriteria(Integer mappedId,Short[] reminder, Short[] vaccine, ReminderType[] reminderType, 
			String recipient, Date reminderDuedatesmaller,Date reminderDuedategreater,Date reminderSentdatesmaller
			,Date reminderSentdategreater,REMINDER_STATUS reminderStatus,boolean putNotWithReminderStatus
			, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		if(reminderDuedatesmaller!=null){
			reminderDuedatesmaller=DateUtils.truncateDatetoDate(reminderDuedatesmaller);
		}
		if(reminderDuedategreater!=null){
			reminderDuedategreater=DateUtils.roundoffDatetoDate(reminderDuedategreater);
		}
		if(reminderSentdatesmaller!=null){
			reminderSentdatesmaller=DateUtils.truncateDatetoDate(reminderSentdatesmaller);
		}
		if(reminderSentdategreater!=null){
			reminderSentdategreater=DateUtils.roundoffDatetoDate(reminderSentdategreater);
		}
		List<ReminderSms> rsms=rsmsdao.findByCriteria(mappedId, reminder, vaccine
				, reminderType, recipient, reminderDuedatesmaller, reminderDuedategreater, reminderSentdatesmaller
				, reminderSentdategreater, reminderStatus, putNotWithReminderStatus, firstResult
				, fetchsize,isreadonly, mappingsToJoin);
		return rsms;
	}

	@Override
	public List<ReminderSms> getAllReminderSmsRecord(int firstResult,int fetchsize,boolean isreadonly, String[] mappingsToJoin) {
		List<ReminderSms> rsmsl=rsmsdao.getAll(firstResult, fetchsize,isreadonly, mappingsToJoin);
		return  rsmsl;
	}

	@Override
	public void updateReminderSmsRecord(ReminderSms reminderSms) {
		rsmsdao.update(reminderSms);
	}

	@Override
	public Serializable addReminderSmsRecord(ReminderSms reminderSms) {
		return rsmsdao.save(reminderSms);
	}

	@Override
	public List<ReminderSms> findByCriteria(String vaccineName ,REMINDER_STATUS reminderStatus
			,boolean putNotWithReminderStatus,Short dayNumber,Integer vaccinationRecordNum
			,boolean isreadonly, String[] mappingsToJoin) {
		
		List<ReminderSms> rsmsl= rsmsdao.findByCriteria(vaccineName, reminderStatus, putNotWithReminderStatus
				,dayNumber,vaccinationRecordNum,isreadonly, mappingsToJoin);
		return  rsmsl;
	}

@Override
	public ReminderSms getReminderSmsRecord(int id,boolean isreadonly, String[] mappingsToJoin) {
		ReminderSms r=rsmsdao.findById(id,isreadonly, mappingsToJoin);
		return r;
	}
}
