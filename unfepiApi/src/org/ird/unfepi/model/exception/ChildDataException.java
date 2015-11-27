/*
 * 
 */
package org.ird.unfepi.model.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class ChildDataException.
 */
public class ChildDataException extends Exception{
	
	/** The Constant CHILD_DOESNOT_EXIST. */
	public static final String CHILD_DOESNOT_EXIST="Given child does not exist";
	
	/** The Constant CHILD_ID_MISSING_OR_NULL. */
	public static final String CHILD_ID_MISSING_OR_NULL="No child id is specified";
	
	/** The Constant CHILD_CURRENT_CELL_MISSING. */
	public static final String CHILD_CURRENT_CELL_MISSING="No currnet cell number for child is specified";
	
	/** The Constant CHILD_CELL_NUMBER_INVALID_DIGIT_RANGE. */
	public static final String CHILD_CELL_NUMBER_INVALID_DIGIT_RANGE="Current cell number not matches exact digit range";
	
	/** The Constant INVALID_CELL_NUMBER. */
	public static final String INVALID_CELL_NUMBER="Current cell number contains invalid characters";
	
	/** The Constant CHILD_NAME_EMPTY. */
	public static final String CHILD_NAME_EMPTY="Child must have a first name";
	
	/** The Constant CHILD_EXISTS. */
	public static final String CHILD_EXISTS="Given Child already exists";
	
	/** The Constant CELL_NUM_OCCUPIED. */
	public static final String CELL_NUM_OCCUPIED="Given cell number already occupied by another child";
	
	/** The Constant CHILD_NOT_FOUND. */
	public static final String CHILD_NOT_FOUND="No child found";
	
	/** The Constant GIVEN_CHILD_ID_ASSIGNED_TO_MULTIPLE_CHILDREN. */
	public static final String GIVEN_CHILD_ID_ASSIGNED_TO_MULTIPLE_CHILDREN="Data inconsistent. Given child id has been found on more than one child records.";
	
	/** The Constant GIVEN_CELL_NUMBER_ASSIGNED_TO_MULTIPLE_CHILDREN. */
	public static final String GIVEN_CELL_NUMBER_ASSIGNED_TO_MULTIPLE_CHILDREN="Data inconsistent. Given child cell number has been found on more than one child records.";
	
	/** The Constant INVALID_CRITERIA_VALUE_SPECIFIED. */
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";
	
	/** The Constant EPI_NUMBER_EXISTS. */
	public static final String EPI_NUMBER_EXISTS="Epi registration number exists.";

	//public static final String 

	/** The ERRO r_ code. */
	public String ERROR_CODE;

		/** The error message. */
		private String errorMessage;
		
		/**
		 * Instantiates a new child data exception.
		 *
		 * @param errorcode the errorcode
		 */
		public ChildDataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		
		/**
		 * Instantiates a new child data exception.
		 *
		 * @param errorcode the errorcode
		 * @param message the message
		 */
		public ChildDataException(String errorcode,String message){
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
