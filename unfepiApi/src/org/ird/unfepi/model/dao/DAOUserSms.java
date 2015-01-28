package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms;

public interface DAOUserSms extends DAO{

	UserSms findById(int id);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<UserSms> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, SmsStatus smsStatus,
			boolean putNotWithSmsStatus, Integer recipientId,  Short recipientRole, 
			int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<UserSms> getAll(int firstResult, int fetchsize, boolean readonly);

}
