/*
 * 
 */
package org.ird.unfepi.service.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class UserServiceException.
 */
public class UserServiceException extends Exception{

	/** The Constant USER_NOT_EXISTS. */
	public static final String USER_NOT_EXISTS="Given username doesnot exists";
	
	/** The Constant USER_ALREADY_LOGGED_IN. */
	public static final String USER_ALREADY_LOGGED_IN="Given username is already logged in";
	
	/** The Constant PASSWORDS_NOT_MATCH. */
	public static final String PASSWORDS_NOT_MATCH="Both passwords donot matches";
	
	/** The Constant WRONG_PASSWORD. */
	public static final String WRONG_PASSWORD="Wrong password.";
	
	/** The Constant ROLE_EXISTS. */
	public static final String ROLE_EXISTS="A role already exists in database with the same name";
	
	/** The Constant PERMISSION_EXISTS. */
	public static final String PERMISSION_EXISTS="A permission already exists in database with the same name";
	
	/** The Constant USER_EXISTS. */
	public static final String USER_EXISTS="A user already exists in database with the same username";
	
	/** The Constant INVILD_USER_NAME. */
	public static final String INVILD_USER_NAME="Username is invalid";
	
	/** The Constant USERNAME_EMPTY. */
	public static final String USERNAME_EMPTY="User name is not specified";
	
	/** The Constant USER_FIRST_NAME_MISSING. */
	public static final String USER_FIRST_NAME_MISSING="User first name is not specified";
	
	/** The Constant USER_PASSWORD_MISSING. */
	public static final String USER_PASSWORD_MISSING="User password is not provided";
	
	/** The Constant INVALID_PASSWORD_CHARACTERS. */
	public static final String INVALID_PASSWORD_CHARACTERS="Password contains invalid characters. Only allowed charcters are a-zA-Z0-9@#%*()_[]{}|;.";
	
	/** The Constant USER_MUST_HAVE_ROLE. */
	public static final String USER_MUST_HAVE_ROLE="User must have atleast one role";
	
	/** The Constant AUTHENTICATION_EXCEPTION. */
	public static final String AUTHENTICATION_EXCEPTION="Invalid username or password. Is caps lock on?";
	
	/** The Constant ACCOUNT_DISABLED. */
	public static final String ACCOUNT_DISABLED="Your account is disabled by the admin";
	
	/** The Constant SESSION_EXPIRED. */
	public static final String SESSION_EXPIRED="Your current session has expired . please login again";
	
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
	public UserServiceException(String errorcode){
		this.errorMessage=errorcode;
		this.ERROR_CODE=errorcode;
	}
	
	/**
	 * Instantiates a new user service exception.
	 *
	 * @param message the message
	 * @param errorcode the errorcode
	 */
	public UserServiceException(String message,String errorcode){
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
