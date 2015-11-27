/*
 * 
 */
package org.ird.unfepi.model;

import java.io.Serializable;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;

/**
 * The Class Storekeeper.
 */
@Entity
@Table(name = "storekeeper")
public class Storekeeper implements Serializable{

	/** The storekeeper id. */
	@Id
	private int mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class, optional = false)
	@PrimaryKeyJoinColumn(name = "mappedId")
	@ForeignKey(name = "storekeeper_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	@Column(length = 50, unique = true)
	private String storeName;
	
	@Column(length = 30)
	private String firstName;

	/** The middle name of the user. */

	@Column(length = 30)
	private String middleName;

	/** The last name of the user. */

	@Column(length = 30)
	private String lastName;
	
	private Integer closestVaccinationCenterId;
	
	/** The vaccination center id. */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = VaccinationCenter.class)
	@JoinColumn(name = "closestVaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "storekeeper_vaccinationCenterId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter closestVaccinationCenter;
	
	/** The birthdate. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date	birthdate;
	
	/** The age. */
	@Transient
	private Integer	age;
	
	/** The estimated birthdate. */
	private Boolean	estimatedBirthdate;
	
	/** The NIC. */
	@Column(length = 20)
	private String nic;
	
	@Column(length = 30)
	private String epAccountNumber;
	
	/** The NIC owner name. */
	@Column(length = 30)
	private String nicOwnerFirstName;
	
	/** The NIC owner name. */
	@Column(length = 30)
	private String nicOwnerLastName;
	
	private Short nicOwnerRelationId;
	
	/** The NIC owner relation. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nicOwnerRelationId", insertable = false, updatable = false)
	@ForeignKey(name = "storekeeper_NICOwnerRelation_relationship_relationshipId_FK")
	private Relationship nicOwnerRelation;
	
	@Column(length = 30)
	private String otherNicOwnerRelation;
	
	/** The domicile. */
	@Column(length = 30)
	private String domicile;
	
	/** The gender. */
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Gender gender;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRegistered;
	
	@Column(length = 50)
	private String qualification;

	@Column(length = 50)
	private String designation;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "storekeeper_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "storekeeper_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public Storekeeper() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the mappedId
	 */
	public int getMappedId() {
		return mappedId;
	}

	/**
	 * @param mappedId the mappedId to set
	 */
	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}

	public IdMapper getIdMapper() {
		return idMapper;
	}

	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Gets the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		String fullName = "";
		if (firstName != null)
			fullName += firstName + " ";
		if (middleName != null)
			fullName += middleName + " ";
		if (lastName != null)
			fullName += lastName;
		return fullName;
	}
	
	public Integer getClosestVaccinationCenterId() {
		return closestVaccinationCenterId;
	}
	public void setClosestVaccinationCenterId(Integer closestVaccinationCenterId) {
		this.closestVaccinationCenterId = closestVaccinationCenterId;
	}
	public VaccinationCenter getClosestVaccinationCenter() {
		return closestVaccinationCenter;
	}
	void setClosestVaccinationCenter(VaccinationCenter closestVaccinationCenter) {
		this.closestVaccinationCenter = closestVaccinationCenter;
	}
	/**
	 * Gets the birthdate.
	 *
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	
	/**
	 * Sets the birthdate.
	 *
	 * @param birthdate the new birthdate
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	/**
	 * Sets the age.
	 *
	 * @param ageInWeeks the new age
	 */
	public void setAge(Integer ageInWeeks) {
		age=ageInWeeks;
	}
	
	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public Integer getAge() {
		if(getBirthdate()!=null){
			return age= DateUtils.differenceBetweenIntervals(new Date(), getBirthdate() , TIME_INTERVAL.WEEK);
		}
		return age;
	}
	
	/**
	 * Gets the estimated birthdate.
	 *
	 * @return the estimated birthdate
	 */
	public Boolean getEstimatedBirthdate() {
		return estimatedBirthdate;
	}
	
	/**
	 * Sets the estimated birthdate.
	 *
	 * @param estimatedBirthdate the new estimated birthdate
	 */
	public void setEstimatedBirthdate(Boolean estimatedBirthdate) {
		this.estimatedBirthdate = estimatedBirthdate;
	}
	/**
	 * @return the nic
	 */
	public String getNic() {
		return nic;
	}
	/**
	 * @param nic the nic to set
	 */
	public void setNic(String nic) {
		this.nic = nic;
	}
	public String getEpAccountNumber() {
		return epAccountNumber;
	}
	public void setEpAccountNumber(String epAccountNumber) {
		this.epAccountNumber = epAccountNumber;
	}
	/**
	 * @return the nicOwnerFirstName
	 */
	public String getNicOwnerFirstName() {
		return nicOwnerFirstName;
	}
	/**
	 * @param nicOwnerFirstName the nicOwnerFirstName to set
	 */
	public void setNicOwnerFirstName(String nicOwnerFirstName) {
		this.nicOwnerFirstName = nicOwnerFirstName;
	}
	/**
	 * @return the nicOwnerLastName
	 */
	public String getNicOwnerLastName() {
		return nicOwnerLastName;
	}
	/**
	 * @param nicOwnerLastName the nicOwnerLastName to set
	 */
	public void setNicOwnerLastName(String nicOwnerLastName) {
		this.nicOwnerLastName = nicOwnerLastName;
	}
	public Short getNicOwnerRelationId() {
		return nicOwnerRelationId;
	}
	public void setNicOwnerRelationId(Short nicOwnerRelationId) {
		this.nicOwnerRelationId = nicOwnerRelationId;
	}
	/**
	 * @return the nicOwnerRelation
	 */
	public Relationship getNicOwnerRelation() {
		return nicOwnerRelation;
	}
	/**
	 * @param nicOwnerRelation the nicOwnerRelation to set
	 */
	void setNicOwnerRelation(Relationship nicOwnerRelation) {
		this.nicOwnerRelation = nicOwnerRelation;
	}
	/**
	 * @return the otherNicOwnerRelation
	 */
	public String getOtherNicOwnerRelation() {
		return otherNicOwnerRelation;
	}
	/**
	 * @param otherNicOwnerRelation the otherNicOwnerRelation to set
	 */
	public void setOtherNicOwnerRelation(String otherNicOwnerRelation) {
		this.otherNicOwnerRelation = otherNicOwnerRelation;
	}
	
	/**
	 * Gets the domicile.
	 *
	 * @return the domicile
	 */
	public String getDomicile() {
		return domicile;
	}

	/**
	 * Sets the domicile.
	 *
	 * @param domicile the new domicile
	 */
	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Date getDateRegistered() {
		return dateRegistered;
	}
	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
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
