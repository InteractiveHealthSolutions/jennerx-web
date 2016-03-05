package org.ird.unfepi.web.validator;

import com.mysql.jdbc.StringUtils;

/**
 * Represents the Validation Result 
 * <ul><li>{@link ValidatorOutput#STATUS()} return status of validator: {@link ValidatorStatus#OK} for successful validation
 * <li>{@link ValidatorOutput#MESSAGE()} OR {@link ValidatorOutput#MESSAGE(String)} return error messages if any
 * </ul>
 */
public class ValidatorOutput {

	public static final String CONTACT_NUMBER_EMPTY = "Contact number found to be empty or whitespace only";
	public static final String CONTACT_NUMBER_NOT_MOBILE = "Contact number was not a valid mobile number. Remove whitespaces, if any";
	public static final String CONTACT_NUMBER_NOT_LANDLINE = "Contact number was not a valid landline number. Remove whitespaces, if any";
	//public static final String CONTACT_NUMBER_EMPTY = "";
	public static final String PROGRAM_ID_EMPTY = "Program ID found to be empty or whitespace only";

	public static final String EPI_NUMBER_EMPTY = "EPI number found to be empty or whitespace only";
	public static final String EPI_NUMBER_YEAR_INVALID = "EPI number year part should be less than or equal to current year";
	public static final String EPI_NUMBER_INVALID = "Epi number should be a numeric sequence of 8 digits starting with 201. Remove whitespaces, if any";
	public static final String EPI_NUMBER_OCCUPIED = "EPI number found to be occupied by another child on same center";
	public static final String EPI_NUMBER_NOT_MATCHED = "EPI number found to be different for child on previous visits.";

	public static final String UNKNOWN_STATUS = "Validations not performed for every case";
	
	public enum ValidatorStatus{
		ERROR,
		WARNING,
		OK,
		UNKNOWN
	}
	
	
	private String[] MESSAGE;
	private ValidatorStatus STATUS;
	
	public String MESSAGE(String arraySeparator) {
		String msg = "";
		if(MESSAGE == null || MESSAGE.length == 0){
			return msg;
		}
		
		for (String m : MESSAGE) {
			msg += m + (StringUtils.isEmptyOrWhitespaceOnly(arraySeparator)?"":arraySeparator );
		}
		return msg;
	}

	public String MESSAGE() {
		return MESSAGE(" ");
	}
	
	public ValidatorOutput(ValidatorStatus status, String message[]) {
		this.STATUS = status;
		this.MESSAGE = message;
	}

	public ValidatorOutput(ValidatorStatus status, String message) {
		this.STATUS = status;
		this.MESSAGE = new String[]{message};
	}
	
	public ValidatorStatus STATUS() {
		return STATUS;
	}
}
