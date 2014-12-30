/*
 * 
 */
package org.ird.unfepi.model.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class VaccinationDataException.
 */
public class VaccinationDataException extends Exception{
	
	/** The Constant INVALID_CRITERIA_VALUE_SPECIFIED. */
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";
	
	/** The Constant VACCINE_ALREADY_EXISTS. */
	public static final String VACCINE_ALREADY_EXISTS="A vaccine already exists with given name in database";
	
	/** The Constant VACCINE_FORM_NUMBER_ALREADY_EXISTS. */
	public static final String VACCINE_FORM_NUMBER_ALREADY_EXISTS="A vaccine already exists with given form number in database";
	
	/** The Constant VACCINE_FORM_NAME_ALREADY_EXISTS. */
	public static final String VACCINE_FORM_NAME_ALREADY_EXISTS="A vaccine already exists with given form name in database";

	public static final String CHILD_HAVE_NOT_RECIEVED_PREVIOUS_VACCINATION="Child seems to have previous vaccination Pending";

	public static final String MULITPLE_VACCINE_RECORDS_FOUND_PENDING="Multiple vaccination records found to be Pending for given vaccine";

	public static final String MULITPLE_VACCINES_WITH_SAME_STATUS_FOR_CHILD="Multiple vaccination records found for given vaccine with same vaccination status for child specified";

	//public static final String 

	/** The ERRO r_ code. */
	public String ERROR_CODE;

		/** The error message. */
		private String errorMessage;
		
		/**
		 * Instantiates a new vaccination data exception.
		 *
		 * @param errorcode the errorcode
		 */
		public VaccinationDataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		
		/**
		 * Instantiates a new vaccination data exception.
		 *
		 * @param errorcode the errorcode
		 * @param message the message
		 */
		public VaccinationDataException(String errorcode,String message){
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
