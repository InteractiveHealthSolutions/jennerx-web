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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.ird.unfepi.model.Model.ContactTeleLineType;
import org.ird.unfepi.model.Model.ContactType;

/**
 * The Class ContactNumber.
 */
@Entity
@Table(name = "contactnumber")
public class ContactNumber {

	/** The contact number id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int contactNumberId;
	
	private Integer mappedId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false, nullable = false)
	@ForeignKey(name = "contactnumber_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;

	/** The number. */
	@Column(length = 20)
	private String number;
	
	/** The number type. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ContactType numberType;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ContactTeleLineType telelineType;
	
	/** The owner name. */
	@Column(length = 30)
	private String ownerFirstName;
	
	@Column(length = 30)
	private String ownerLastName;
	
	private Short ownerRelationId;
	/** The owner relation. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ownerRelationId", insertable = false, updatable = false)
	@ForeignKey(name = "contactnumber_ownerRelation_relationship_relationshipId_FK")
	private Relationship ownerRelation;
	
	@Column(length = 30)
	private String otherOwnerRelation;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "contactnumber_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "contactnumber_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public ContactNumber() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Gets the contact number id.
	 *
	 * @return the contact number id
	 */
	public int getContactNumberId() {
		return contactNumberId;
	}

	/**
	 * Sets the contact number id.
	 *
	 * @param contactNumberId the new contact number id
	 */
	public void setContactNumberId(int contactNumberId) {
		this.contactNumberId = contactNumberId;
	}

	public Integer getMappedId() {
		return mappedId;
	}
	public void setMappedId(Integer mappedId) {
		this.mappedId = mappedId;
	}
	public IdMapper getIdMapper() {
		return idMapper;
	}

	void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	/**
	 * Gets the number.
	 *
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Sets the number.
	 *
	 * @param number the new number
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * Gets the number type.
	 *
	 * @return the number type
	 */
	public ContactType getNumberType() {
		return numberType;
	}

	/**
	 * Sets the number type.
	 *
	 * @param numberType the new number type
	 */
	public void setNumberType(ContactType numberType) {
		this.numberType = numberType;
	}

	public ContactTeleLineType getTelelineType() {
		return telelineType;
	}
	public void setTelelineType(ContactTeleLineType telelineType) {
		this.telelineType = telelineType;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public Short getOwnerRelationId() {
		return ownerRelationId;
	}
	public void setOwnerRelationId(Short ownerRelationId) {
		this.ownerRelationId = ownerRelationId;
	}
	/**
	 * Gets the owner relation.
	 *
	 * @return the owner relation
	 */
	public Relationship getOwnerRelation() {
		return ownerRelation;
	}

	/**
	 * Sets the owner relation.
	 *
	 * @param ownerRelation the new owner relation
	 */
	void setOwnerRelation(Relationship ownerRelation) {
		this.ownerRelation = ownerRelation;
	}

	public String getOtherOwnerRelation() {
		return otherOwnerRelation;
	}
	public void setOtherOwnerRelation(String otherOwnerRelation) {
		this.otherOwnerRelation = otherOwnerRelation;
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
}
