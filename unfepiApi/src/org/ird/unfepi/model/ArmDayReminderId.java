/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Embeddable;

/**
 * The Class ArmDayReminderId.
 */
@Embeddable
public class ArmDayReminderId implements java.io.Serializable {

	/** The arm id. */
	private short armId;
	
	/** The reminder id. */
	private short reminderId;
	
	/** The vaccine id. */
	private short vaccineId;
	
	/** The day number. */
	private short dayNumber;

	/**
	 * Instantiates a new arm day reminder id.
	 */
	public ArmDayReminderId() {
	}

	/**
	 * Instantiates a new arm day reminder id.
	 *
	 * @param armId the arm id
	 * @param reminderId the reminder id
	 * @param vaccineId the vaccine id
	 * @param dayNumber the day number
	 */
	public ArmDayReminderId(short armId, short reminderId, short vaccineId,short dayNumber) {
		this.armId = armId;
		this.reminderId = reminderId;
		this.vaccineId = vaccineId;
		this.dayNumber=dayNumber;
	}


	/**
	 * Gets the arm id.
	 *
	 * @return the arm id
	 */
	public short getArmId() {
		return armId;
	}

	/**
	 * Sets the arm id.
	 *
	 * @param armId the new arm id
	 */
	public void setArmId(short armId) {
		this.armId = armId;
	}

	/**
	 * Gets the reminder id.
	 *
	 * @return the reminder id
	 */
	public short getReminderId() {
		return reminderId;
	}

	/**
	 * Sets the reminder id.
	 *
	 * @param reminderId the new reminder id
	 */
	public void setReminderId(short reminderId) {
		this.reminderId = reminderId;
	}

	/**
	 * Gets the vaccine id.
	 *
	 * @return the vaccine id
	 */
	public short getVaccineId() {
		return vaccineId;
	}

	/**
	 * Sets the vaccine id.
	 *
	 * @param vaccineId the new vaccine id
	 */
	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	/**
	 * Gets the day number.
	 *
	 * @return the day number
	 */
	public short getDayNumber() {
		return dayNumber;
	}

	/**
	 * Sets the day number.
	 *
	 * @param dayNumber the new day number
	 */
	public void setDayNumber(short dayNumber) {
		this.dayNumber = dayNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ArmDayReminderId))
			return false;
		ArmDayReminderId castOther = (ArmDayReminderId) other;

		return (this.getArmId() == castOther.getArmId())
				&& (this.getReminderId() == castOther.getReminderId())
				&& (this.getVaccineId() == castOther.getVaccineId())
				&& (this.getDayNumber() == castOther.getDayNumber());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getArmId();
		result = 37 * result + this.getReminderId();
		result = 37 * result + this.getVaccineId();
		result = 37 * result + this.getDayNumber();
		return result;
	}
}
