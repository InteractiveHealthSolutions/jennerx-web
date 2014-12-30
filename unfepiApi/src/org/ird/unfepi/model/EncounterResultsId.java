/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * The Class EncounterResultsId.
 */
@Embeddable
public class EncounterResultsId implements java.io.Serializable
{
	/** The encounter id. */
	private int encounterId;

	private Integer p1id;
	
	private Integer p2id;
	
	/** The element. */
	private String element;

	/**
	 * Instantiates a new encounter results id.
	 */
	public EncounterResultsId ()
	{
	}

	/**
	 * Instantiates a new encounter results id.
	 *
	 * @param encounterId the encounter id
	 * @param pid1 the pid1
	 * @param pid2 the pid2
	 * @param element the element
	 */
	public EncounterResultsId (int encounterId, int pid1, int pid2, String element)
	{
		this.encounterId = encounterId;
		this.p1id = pid1;
		this.p2id = pid2;
		this.element = element;
	}

	/**
	 * Gets the encounter id.
	 *
	 * @return the encounter id
	 */
	public int getEncounterId ()
	{
		return this.encounterId;
	}

	/**
	 * Sets the encounter id.
	 *
	 * @param encounterId the new encounter id
	 */
	public void setEncounterId (int encounterId)
	{
		this.encounterId = encounterId;
	}

	public Integer getP1id() {
		return p1id;
	}

	public void setP1id(Integer p1id) {
		this.p1id = p1id;
	}

	public Integer getP2id() {
		return p2id;
	}

	public void setP2id(Integer p2id) {
		this.p2id = p2id;
	}

	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	public String getElement ()
	{
		return this.element;
	}

	/**
	 * Sets the element.
	 *
	 * @param element the new element
	 */
	public void setElement (String element)
	{
		this.element = element;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + encounterId;
		result = prime * result + (p1id ^ (p1id >>> 32));
		result = prime * result + (p2id ^ (p2id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EncounterResultsId other = (EncounterResultsId) obj;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (encounterId != other.encounterId)
			return false;
		if (p1id != other.p1id)
			return false;
		if (p2id != other.p2id)
			return false;
		return true;
	}

}
