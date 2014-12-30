package org.ird.unfepi.utils;

import org.ird.unfepi.constants.FormType;

public class LoggerUtils 
{
	public enum LogType{
		TRANSACTION_UPDATE,
		TRANSACTION_DELETE,
		TRANSACTION_INSERT,
		LOGIN,
		LOGOUT,
		FAILURES,
		DAILY_LOGS,
		DATA_EXPORT
	}
	
	public static Object[] getLoggerParams(LogType logType, FormType formType, String username){
		return new Object[]{logType, formType, username};
	}
}
