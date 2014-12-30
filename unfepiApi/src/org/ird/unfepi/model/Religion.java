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
 * The Class Religion.
 */
@Entity
@Table(name = "religion")
public class Religion {

	/** The religion id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short religionId;
	
	/** The religion name. */
	@Column(length = 30, unique = true)
	private String religionName;

	public Religion() {
		// TODO Auto-generated constructor stub
	}
	public Religion(short religionId) {
		this.religionId = religionId;
	}
	/**
	 * Gets the religion id.
	 *
	 * @return the religion id
	 */
	public short getReligionId() {
		return religionId;
	}

	/**
	 * Sets the religion id.
	 *
	 * @param religionId the new religion id
	 */
	public void setReligionId(short religionId) {
		this.religionId = religionId;
	}

	/**
	 * Gets the religion name.
	 *
	 * @return the religion name
	 */
	public String getReligionName() {
		return religionName;
	}

	/**
	 * Sets the religion name.
	 *
	 * @param religionName the new religion name
	 */
	public void setReligionName(String religionName) {
		this.religionName = religionName;
	}
}
