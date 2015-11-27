/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class CalendarDay.
 */
@Entity
@Table(name="calendarday")
public class CalendarDay {

	/** The day number. */
	@Id
	@Column(length = 1)
	private short dayNumber;
	
	/** The day full name. */
	@Column(length = 10, unique = true)
	private String dayFullName;
	
	/** The day short name. */
	@Column(length = 4, unique = true)
	private String dayShortName;

	public CalendarDay() {
		
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

	/**
	 * Gets the day full name.
	 *
	 * @return the day full name
	 */
	public String getDayFullName() {
		return dayFullName;
	}

	/**
	 * Sets the day full name.
	 *
	 * @param dayFullName the new day full name
	 */
	public void setDayFullName(String dayFullName) {
		this.dayFullName = dayFullName;
	}

	/**
	 * Gets the day short name.
	 *
	 * @return the day short name
	 */
	public String getDayShortName() {
		return dayShortName;
	}

	/**
	 * Sets the day short name.
	 *
	 * @param dayShortName the new day short name
	 */
	public void setDayShortName(String dayShortName) {
		this.dayShortName = dayShortName;
	}
}
