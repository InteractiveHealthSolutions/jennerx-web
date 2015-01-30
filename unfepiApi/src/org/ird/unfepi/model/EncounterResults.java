/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class EncounterResults.
 */
@Entity
@Table(name = "encounterresults")
public class EncounterResults implements java.io.Serializable
{
	
	/** The id. */
	@Id
	private EncounterResultsId	id;
	
	private Integer orderAs;
	
	/** The value. */
	private String				value;
	
	private String				groupName;
	
	private String				displayName;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "p1id", insertable = false, updatable = false)
	@ForeignKey(name = "encounterresults_p1id_idmapper_mappedId_FK")
	private IdMapper p1;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "p2id", insertable = false, updatable = false)
	@ForeignKey(name = "encounterresults_p2id_idmapper_mappedId_FK")
	private IdMapper p2;
	/**
	 * Instantiates a new encounter results.
	 */
	public EncounterResults ()
	{
		
	}

	/**
	 * Instantiates a new encounter results.
	 *
	 * @param id the id
	 * @param value the value
	 */
	public EncounterResults (EncounterResultsId id, String value)
	{
		this.id = id;
		this.value = value;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public EncounterResultsId getId ()
	{
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId (EncounterResultsId id)
	{
		this.id = id;
	}
	
	public Integer getOrderAs() {
		return orderAs;
	}

	public void setOrderAs(Integer orderAs) {
		this.orderAs = orderAs;
	}

	public IdMapper getP1() {
		return p1;
	}

	void setP1(IdMapper p1) {
		this.p1 = p1;
	}
	
	public IdMapper getP2() {
		return p2;
	}

	void setP2(IdMapper p2) {
		this.p2 = p2;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue ()
	{
		return this.value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue (String value)
	{
		this.value = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
