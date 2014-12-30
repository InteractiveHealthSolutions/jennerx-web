/*
 * 
 */
package org.ird.unfepi.model;

/**
 * The Class Model.
 */
public class Model {
	
	/**
	 * The Enum Gender.
	 */
	public enum Gender {
		
		MALE ,
		FEMALE,
		UNKNOWN;
	}
	
	/**
	 * The Enum MaritalStatus.
	 */
	public enum MaritalStatus{
		
		/** The MARRIED. */
		MARRIED,
		
		/** The UNMARRIED. */
		UNMARRIED,
		
		/** The NIKAH. */
		NIKAH,
		
		/** The DIVORCED. */
		DIVORCED,
		
		/** The SEPARATED. */
		SEPARATED,
		
		/** The WIDOW. */
		WIDOW,
		
		/** The REFUSED. */
		REFUSED;
	}
	
	/**
	 * The Enum ContactType.
	 */
	public enum ContactType{
		
		//PRIMARY_UNIQUE,
		//SECONDARY_UNIQUE,
		/** The PRIMARY. */
		PRIMARY,
		
		/** The SECONDARY. */
		SECONDARY,
		UNKNOWN
	}
	
	public enum ContactTeleLineType {
		MOBILE,
		LANDLINE,
		WIRELESS,
		UNKNOWN
	}
	
	public enum SmsStatus {

		/** Pending. */
		PENDING("PND"),
		/** Sent. */
		SENT("SNT"),
		/** Failed. */
		FAILED("FLD"),
		/** Missed. */
		MISSED("MIS"),
		/** Cancelled. */
		CANCELLED("CNCL");

		/** The REPRESENTATION. */
		private String REPRESENTATION;

		public String getREPRESENTATION() {
			return REPRESENTATION;
		}

		private SmsStatus(String representation) {
			this.REPRESENTATION = representation;
		}

		public static SmsStatus findEnum(String representationString) {
			for (SmsStatus en : SmsStatus.values()) {
				if (en.REPRESENTATION.equalsIgnoreCase(representationString)) {
					return en;
				}
			}
			return null;
		}
	}
	
	/**
	 * The Enum UNIT_GAP.
	 */
	public enum TimeIntervalUnit{
		
		/** The DAY. */
		DAY,
		
		/** The WEEK. */
		WEEK,
		
		/** The MONTH. */
		MONTH,
		
		/** The YEAR. */
		YEAR;
	}
}
