/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class Encounter.
 */
@Entity
@Table(name = "encounter")
public class Encounter implements java.io.Serializable
{
	public enum DataEntrySource{
		MOBILE,
		WEB
	}
	
	/** The id. */
	@Id
	private EncounterId			id;
	
	/** The encounter type. */
	@Column(length = 30)
	private String				encounterType;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private DataEntrySource dataEntrySource;
	
	/** The location id. */
	@Column(length = 12)
	private String				locationId;
	
	/** The date encounter entered. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateEncounterEntered;
	
	/** The date encounter start. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateEncounterStart;
	
	/** The date encounter end. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateEncounterEnd;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "p1id", insertable = false, updatable = false)
	@ForeignKey(name = "encounter_p1id_idmapper_mappedId_FK")
	private IdMapper p1;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "p2id", insertable = false, updatable = false)
	@ForeignKey(name = "encounter_p2id_idmapper_mappedId_FK")
	private IdMapper p2;
	
	private int createdByUserId;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId", insertable = false, updatable = false)
	@ForeignKey(name = "encounter_createdByUserId_user_mappedId_FK")
	private User createdByUser;
	
	/** The detail. */
	private String				detail;

	/**
	 * Instantiates a new encounter.
	 */
	public Encounter ()
	{
	}

	/**
	 * Instantiates a new encounter.
	 *
	 * @param id the id
	 * @param encounterType the encounter type
	 */
	public Encounter (EncounterId id, String encounterType)
	{
		this.id = id;
		this.encounterType = encounterType;
	}

	/**
	 * Instantiates a new encounter.
	 *
	 * @param id the id
	 * @param encounterType the encounter type
	 * @param locationId the location id
	 * @param dateEncounterEntered the date encounter entered
	 * @param dateEncounterStart the date encounter start
	 * @param dateEncounterEnd the date encounter end
	 * @param detail the detail
	 */
	public Encounter (EncounterId id, String encounterType, DataEntrySource dataEntrySource, String locationId, Date dateEncounterEntered, Date dateEncounterStart, Date dateEncounterEnd, String detail)
	{
		this.id = id;
		this.encounterType = encounterType;
		this.dataEntrySource = dataEntrySource;
		this.locationId = locationId;
		this.dateEncounterEntered = dateEncounterEntered;
		this.dateEncounterStart = dateEncounterStart;
		this.dateEncounterEnd = dateEncounterEnd;
		this.detail = detail;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public EncounterId getId ()
	{
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId (EncounterId id)
	{
		this.id = id;
	}

	/**
	 * Gets the encounter type.
	 *
	 * @return the encounter type
	 */
	public String getEncounterType ()
	{
		return this.encounterType;
	}

	/**
	 * Sets the encounter type.
	 *
	 * @param encounterType the new encounter type
	 */
	public void setEncounterType (String encounterType)
	{
		this.encounterType = encounterType;
	}

	public DataEntrySource getDataEntrySource() {
		return dataEntrySource;
	}

	public void setDataEntrySource(DataEntrySource dataEntrySource) {
		this.dataEntrySource = dataEntrySource;
	}

	/**
	 * Gets the location id.
	 *
	 * @return the location id
	 */
	public String getLocationId ()
	{
		return this.locationId;
	}

	/**
	 * Sets the location id.
	 *
	 * @param locationId the new location id
	 */
	public void setLocationId (String locationId)
	{
		this.locationId = locationId;
	}

	/**
	 * Gets the date encounter entered.
	 *
	 * @return the date encounter entered
	 */
	public Date getDateEncounterEntered ()
	{
		return this.dateEncounterEntered;
	}

	/**
	 * Sets the date encounter entered.
	 *
	 * @param dateEncounterEntered the new date encounter entered
	 */
	public void setDateEncounterEntered (Date dateEncounterEntered)
	{
		this.dateEncounterEntered = dateEncounterEntered;
	}

	/**
	 * Gets the date encounter start.
	 *
	 * @return the date encounter start
	 */
	public Date getDateEncounterStart ()
	{
		return this.dateEncounterStart;
	}

	/**
	 * Sets the date encounter start.
	 *
	 * @param dateEncounterStart the new date encounter start
	 */
	public void setDateEncounterStart (Date dateEncounterStart)
	{
		this.dateEncounterStart = dateEncounterStart;
	}

	/**
	 * Gets the date encounter end.
	 *
	 * @return the date encounter end
	 */
	public Date getDateEncounterEnd ()
	{
		return this.dateEncounterEnd;
	}

	/**
	 * Sets the date encounter end.
	 *
	 * @param dateEncounterEnd the new date encounter end
	 */
	public void setDateEncounterEnd (Date dateEncounterEnd)
	{
		this.dateEncounterEnd = dateEncounterEnd;
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
	
	public int getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(int createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public User getCreatedByUser() {
		return createdByUser;
	}

	void setCreatedByUser(User createdByUser) {
		this.createdByUser = createdByUser;
	}

	/**
	 * Gets the detail.
	 *
	 * @return the detail
	 */
	public String getDetail ()
	{
		return this.detail;
	}

	/**
	 * Sets the detail.
	 *
	 * @param detail the new detail
	 */
	public void setDetail (String detail)
	{
		this.detail = detail;
	}

}
