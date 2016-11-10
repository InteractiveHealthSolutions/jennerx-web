package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms.SmsType;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.model.dao.DAOUserSms;
import org.ird.unfepi.service.UserSmsService;

public class UserSmsServiceImpl implements UserSmsService{

	DAOUserSms usms;
	
	public UserSmsServiceImpl(DAOUserSms user){
		usms=user;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == UserSms.class){
			return usms.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}
	
	@Override
	public UserSms findUserSmsById(int id) {
		UserSms obj = usms.findById(id);
		return obj;
	}

	@Override
	public List<UserSms> getAllUserSms(int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) {
		List<UserSms> objl = usms.getAll(firstResult, fetchsize, isreadonly);
		return objl;
	}

	@Override
	public List<UserSms> findUserSmsByCriteria(Date duedatesmaller, Date duedategreater, Date sentdatesmaller, Date sentdategreater,
			SmsStatus smsStatus, boolean putNotWithSmsStatus, SmsType smsType, String recipient, Integer recipientId,
			Short recipientRole, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<UserSms> objl = usms.findByCriteria(duedatesmaller, duedategreater, sentdatesmaller, sentdategreater, smsStatus, putNotWithSmsStatus, smsType, recipient, recipientId, recipientRole, firstResult, fetchsize, readonly, mappingsToJoin);
		return objl;
	}

	@Override
	public Serializable saveUserSms(UserSms userSms) {
		return usms.save(userSms);
	}

	@Override
	public void updateUserSms(UserSms usersms) {
		usms.update(usersms);
	}
}
