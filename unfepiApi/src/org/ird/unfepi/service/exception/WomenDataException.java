/**
 * 
 */
package org.ird.unfepi.service.exception;

/**
 * @author Safwan
 *
 */
public class WomenDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321642290884005171L;
	
	
	public static final String WOMEN_EXISTS = "Women with given Id already exists";
	
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
	public WomenDataException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	
	/**
	 * Instantiates a new user service exception.
	 *
	 * @param message the message
	 * @param errorcode the errorcode
	 */
	public WomenDataException(String message,String errorcode){
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
