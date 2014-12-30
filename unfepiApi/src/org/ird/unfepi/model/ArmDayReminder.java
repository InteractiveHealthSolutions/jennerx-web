/*
 * 
 */
package org.ird.unfepi.model;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class ArmDayReminder.
 */
@Entity
@Table (name = "armdayreminder")
public class ArmDayReminder implements java.io.Serializable {

	/** The id. */
	@Id
	private ArmDayReminderId id;
	
	/** The default reminder time. */
	private Time defaultReminderTime;
	
	/** The is default time editable. */
	private Boolean isDefaultTimeEditable;
	
	/**
	 * Instantiates a new arm day reminder.
	 */
	public ArmDayReminder() {
	}

	/**
	 * Instantiates a new arm day reminder.
	 *
	 * @param id the id
	 */
	public ArmDayReminder(ArmDayReminderId id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public ArmDayReminderId getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(ArmDayReminderId id) {
		this.id = id;
	}

	/**
	 * Sets the default reminder time.
	 *
	 * @param defaultReminderTime the new default reminder time
	 */
	public void setDefaultReminderTime(Time defaultReminderTime) {
		this.defaultReminderTime = defaultReminderTime;
	}

	/**
	 * Gets the default reminder time.
	 *
	 * @return the default reminder time
	 */
	public Time getDefaultReminderTime() {
		return defaultReminderTime;
	}

	/**
	 * Sets the checks if is default time editable.
	 *
	 * @param isDefaultTimeEditable the new checks if is default time editable
	 */
	public void setIsDefaultTimeEditable(Boolean isDefaultTimeEditable) {
		this.isDefaultTimeEditable = isDefaultTimeEditable;
	}

	/**
	 * Gets the checks if is default time editable.
	 *
	 * @return the checks if is default time editable
	 */
	public Boolean getIsDefaultTimeEditable() {
		return isDefaultTimeEditable;
	}
}
