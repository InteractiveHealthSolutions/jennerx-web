package org.ird.unfepi.service.exception;


public class ChildDataInconsistencyException extends Exception{
	public static final String CHILD_EXISTS = "Child with given Id already exists";
	//public static final String CHILD_ID_LEN_INVALID = "Child/Screening Id must be numeric of length "+ Globals.CHILD_ID_LEN;
	public static final String CHILD_BIRTHDATE_INVALID = "Child must have a valid birthdate defined";
	public static final String CHILD_CREATOR_INFO_MISSING = "Child must have creator information";
	public static final String CHILD_EDITOR_INFO_MISSING = "Child must have editor information";
	public static final String CHILD_ENROLLED_DATE_MISSING = "Child must have date enrolled specified";
	public static final String CHILD_STATUS_SHOULD_FOLLOWUP_ON_ENROLLMENT = "Child must have status only FOLLOWUP on enrollment";
	public static final String CHILD_ARM_NOT_SPECIFIED = "Child must be mapped with an ARM. If applicable choose Default form Globals";
	public static final String CHILD_HAVING_MULTIPLE_PRIMARY_CONTACT_NUMBERS = "Child was found to be having multiple primary contact numbers. Proram cannot have multiple primary contacts for any entity";
	public static final String CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBER = "Child was found to be having a primary contact numbers. Proram cannot have multiple primary contacts for any entity";
	public static final String CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED = "Given number is already occupied by another entity.";

	/** The Constant OTHER. */
	public static final String OTHER="Other";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 714368144228840189L;
	
	/** The error message. */
	private String errorMessage;
	
	/** The ERRO r_ code. */
	public String ERROR_CODE;
	
	/** The throwing class. */
	private String throwingClass;

	/**
	 * Instantiates a new user service exception.
	 *
	 * @param errorcode the errorcode
	 */
	public ChildDataInconsistencyException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	
	/**
	 * Instantiates a new user service exception.
	 *
	 * @param message the message
	 * @param errorcode the errorcode
	 */
	public ChildDataInconsistencyException(String message,String errorcode){
		this.errorMessage=message;
		this.ERROR_CODE=errorcode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
	}
}
