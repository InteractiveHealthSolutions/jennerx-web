/*
 * 
 */
package org.ird.unfepi.model.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class ReminderDataException.
 */
public class ReminderDataException  extends Exception{
	
	/** The Constant INVALID_CRITERIA_VALUE_SPECIFIED. */
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";

	//public static final String 

	/** The ERRO r_ code. */
	public String ERROR_CODE;

		/** The error message. */
		private String errorMessage;
		
		/**
		 * Instantiates a new reminder data exception.
		 *
		 * @param errorcode the errorcode
		 */
		public ReminderDataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		
		/**
		 * Instantiates a new reminder data exception.
		 *
		 * @param errorcode the errorcode
		 * @param message the message
		 */
		public ReminderDataException(String errorcode,String message){
			this.errorMessage=message;
			this.ERROR_CODE=errorcode.toString();
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Throwable#getMessage()
		 */
		public String getMessage(){
			return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
		}
		
		
		
	}
