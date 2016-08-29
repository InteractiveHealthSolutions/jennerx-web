package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Hera Rafique
 *
 */

@Entity
@Table(name = "vaccinationcalendar")
public class VaccinationCalendar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer calenderId;

	@Column(length = 50)
	private String fullName;

	@Column(length = 30, unique = true, nullable = false)
	private String shortName;

	/**
	 * @return the calenderId
	 */
	public Integer getCalenderId() {
		return calenderId;
	}

	/**
	 * @param calenderId the calenderId to set
	 */
	public void setCalenderId(Integer calenderId) {
		this.calenderId = calenderId;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
