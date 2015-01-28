/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class VaccinationCenter.
 */
@Entity
@Table(name = "vaccinationcenter")
public class VaccinationCenter {

	public enum CenterType{
		PRIVATE,
		GOVT,
		UNREGISTERED
	}
	/** The vaccination center id. */
	@Id
	private int mappedId;
	
	/** The name. */
	@Column(length = 30, unique = true)
	private String name;
	
	/** The full name. */
	@Column(length = 50)
	private String fullName;
	
	/** The short name. */
	@Column(length = 30)
	private String shortName;
	
	/** The center type. */
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private CenterType centerType;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class, optional = false)
	@PrimaryKeyJoinColumn(name = "mappedId")
	@ForeignKey(name = "vaccinationcenter_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = VaccinationCenterVaccineDay.class)
	@JoinColumn(name = "vaccinationCenterId")
	@ForeignKey(name = "vaccinationcenter_mappedId_vaccinationcentervaccineday_vaccinationCenterId_FK")
	private Set<VaccinationCenterVaccineDay> allowedVaccineDays;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRegistered;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vaccinationcenter_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "vaccinationcenter_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public VaccinationCenter() {
		// TODO Auto-generated constructor stub
	}

	public VaccinationCenter(int mappedId) {
		this.mappedId = mappedId;
	}

	public int getMappedId() {
		return mappedId;
	}

	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name.
	 *
	 * @param fullName the new full name
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Gets the center type.
	 *
	 * @return the center type
	 */
	public CenterType getCenterType() {
		return centerType;
	}

	/**
	 * Sets the center type.
	 *
	 * @param centerType the new center type
	 */
	public void setCenterType(CenterType centerType) {
		this.centerType = centerType;
	}

	public IdMapper getIdMapper() {
		return idMapper;
	}

	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 */
	public User getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 */
	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 */
	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	/**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 */
	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	/**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 */
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	/**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 */
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}

	/**
	 * Gets the short name.
	 *
	 * @return the short name
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Sets the short name.
	 *
	 * @param shortName the new short name
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	/**
	 * Sets the editor.
	 *
	 * @param editor the new editor
	 */
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
	
	public Set<VaccinationCenterVaccineDay> getAllowedVaccineDays() {
		return allowedVaccineDays;
	}

	public void setAllowedVaccineDays(Set<VaccinationCenterVaccineDay> allowedVaccineDays) {
		this.allowedVaccineDays = allowedVaccineDays;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
}
