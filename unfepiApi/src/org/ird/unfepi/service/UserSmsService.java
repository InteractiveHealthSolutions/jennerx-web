package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms;

public interface UserSmsService
{
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	UserSms findUserSmsById(int id);
	
	List<UserSms> getAllUserSms(int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);

	List<UserSms> findUserSmsByCriteria(Date duedatesmaller,Date duedategreater,Date sentdatesmaller,
			Date sentdategreater,SmsStatus smsStatus, boolean putNotWithSmsStatus, Integer recipientId, Short recipientRole,  
			int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	void updateUserSms(UserSms usersms);

	Serializable saveUserSms(UserSms userSms);
}
