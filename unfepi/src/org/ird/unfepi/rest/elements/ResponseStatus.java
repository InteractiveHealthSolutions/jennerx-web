package org.ird.unfepi.rest.elements;

public class ResponseStatus
{
	/**
	 * Child ID does not exist
	 */

	public static final int STATUS_ID_CHILD_NOT_EXIST = 0;
	
	public static final int STATUS_ID_WOMEN_NOT_EXIST = 0;

	/**
	 * Status for denoting the successful completion of the request
	 */
	public static final int STATUS_SUCCESS = 1;

	/**
	 * Incomplete operation. Error due to unknown reasons
	 */
	public static final int STATUS_FAILURE = 2;

	/**
	 * Error due to the data processing with the database
	 */
	public static final int STATUS_DATA_ERROR = 3;

	/**
	 * Server side error while processing the request.
	 */
	public static final int STATUS_INTERNAL_ERROR = 4;

	/**
	 * The data sent from server was not in the format expected by the server
	 */
	public static final int STATUS_INCORRECT_DATA_FORMAT_ERROR = 5;

	/**
	 * This status code represent following problems:
	 * If Pre-Release is found while it is not allowed.
	 * If Request version (major and minor version) is bigger then saved version
	 */
	public static final int STATUS_UNRECOGNISED_OR_INVALID_VERSION = 6;

	/**
	 * No record found against the credentials used to login
	 */
	public static final int STATUS_INCORRECT_CREDENTIALS = 7;

	/**
	 * If version number of requesting application is old
	 */
	public static final int STATUS_OLD_VERSION = 8;
}
