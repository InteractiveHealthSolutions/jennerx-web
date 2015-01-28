/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class Ethnicity.
 */
@Entity
@Table(name = "ethnicity")
public class Ethnicity {

	/** The ethnicity id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short ethnicityId;
	
	/** The ethnicity name. */
	@Column(length = 30, unique = true)
	private String ethnicityName;

	public Ethnicity() {
		// TODO Auto-generated constructor stub
	}
	public Ethnicity(short ethnicityId) {
		this.ethnicityId = ethnicityId;
	}
	/**
	 * Gets the ethnicity id.
	 *
	 * @return the ethnicity id
	 */
	public short getEthnicityId() {
		return ethnicityId;
	}

	/**
	 * Sets the ethnicity id.
	 *
	 * @param ethnicityId the new ethnicity id
	 */
	public void setEthnicityId(short ethnicityId) {
		this.ethnicityId = ethnicityId;
	}

	/**
	 * Gets the ethnicity name.
	 *
	 * @return the ethnicity name
	 */
	public String getEthnicityName() {
		return ethnicityName;
	}

	/**
	 * Sets the ethnicity name.
	 *
	 * @param ethnicityName the new ethnicity name
	 */
	public void setEthnicityName(String ethnicityName) {
		this.ethnicityName = ethnicityName;
	}
}
