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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;

@Entity
@Table (name = "child")
public class Child implements java.io.Serializable {

	public enum STATUS {
		
		/** Follow Up (Under Follow ups). */
		FOLLOW_UP("FUP"), 
		
		/** Terminated. */
		TERMINATED("TRM"),
		
		/** Completed. */
		COMPLETED("CMP"),
		
		/** Unresponsive. */
		UNRESPONSIVE("UNRES"),
		
		UNENROLLED("UNENR");
		
		/** The REPRESENTATION. */
		private String REPRESENTATION;
		
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}
		
		private STATUS(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static STATUS findEnum(String representationString){
			for (STATUS en : STATUS.values()) {
				if(en.REPRESENTATION.equalsIgnoreCase(representationString)){
					return en;
				}
			}
			return null;
		}
	}

	@Id
	private int	mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class, optional = false)
	@PrimaryKeyJoinColumn(name = "mappedId")
	@ForeignKey(name = "child_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	@Column(length = 30)
	private String firstName;

	@Column(length = 30)
	private String middleName;

	@Column(length = 30)
	private String lastName;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	birthdate;
	
	@Transient
	private Integer	age;
	
	private Boolean	estimatedBirthdate;
	
	@Column(length = 30)
	private String guardianFirstName;
	
	@Column(length = 30)
	private String guardianMiddleName;
	
	@Column(length = 20)
	private String guardianLastName;
	
	private Short guardianRelationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guardianRelationId", insertable = false, updatable = false)
	@ForeignKey(name = "child_guardianRelation_relationship_relationshipId_FK")
	private Relationship guardianRelation;

	@Column(length = 30)
	private String otherGuardianRelation;
	
	@Column(length = 30)
	private String fatherFirstName;
	
	@Column(length = 30)
	private String fatherMiddleName;
	
	@Column(length = 30)
	private String fatherLastName;
	
	@Column(length = 30)
	private String motherFirstName;
	
	@Column(length = 30)
	private String motherMiddleName;
	
	@Column(length = 30)
	private String motherLastName;
	
	@Column(length = 30)
	private String nic;
	
	@Column(length = 30)
	private String nicOwnerFirstName;
	
	@Column(length = 30)
	private String nicOwnerLastName;
	
	private Short nicOwnerRelationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nicOwnerRelationId", insertable = false, updatable = false)
	@ForeignKey(name = "child_nicOwnerRelation_relationship_relationshipId_FK")
	private Relationship nicOwnerRelation;
	
	@Column(length = 30)
	private String otherNicOwnerRelation;
	
	@Column(length = 30)
	private String domicile;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Gender gender;
	
	private Short religionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "religionId", insertable = false, updatable = false)
	@ForeignKey(name = "child_religion_religion_religionId_FK")
	private Religion religion;
	
	@Column(length = 30)
	private String otherReligion;
	
	private Short ethnicityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ethnicityId", insertable = false, updatable = false)
	@ForeignKey(name = "child_ethnicity_ethnicity_ethnicityId_FK")
	private Ethnicity ethnicity;

	@Column(length = 30)
	private String otherEthnicity;
	
	private Short languageId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "languageId", insertable = false, updatable = false)
	@ForeignKey(name = "child_language_language_languageId_FK")
	private Language language;

	@Column(length = 30)
	private String otherLanguage;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	dateEnrolled;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	dateOfCompletion;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	terminationDate;
	
	private String	terminationReason;
	
	private String terminationReasonOther;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private STATUS	status;
	
	private Short enrollmentVaccineId;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "enrollmentVaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "child_enrVaccId_vaccine_vaccId")
	private Vaccine	enrollmentVaccine;

	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "child_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "child_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public Child() {
	}

	public Child(int mappedId) {
		this.setMappedId(mappedId);
	}

	public int getMappedId() {
		return mappedId;
	}

	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}
	
	public IdMapper getIdMapper() {
		return idMapper;
	}

	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
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
	 * Gets the guardian relation.
	 *
	 * @return the guardian relation
	 */
	public Relationship getGuardianRelation() {
		return guardianRelation;
	}

	/**
	 * Sets the guardian relation.
	 *
	 * @param guardianRelation the new guardian relation
	 */
	void setGuardianRelation(Relationship guardianRelation) {
		this.guardianRelation = guardianRelation;
	}

	public String getOtherGuardianRelation() {
		return otherGuardianRelation;
	}
	public void setOtherGuardianRelation(String otherGuardianRelation) {
		this.otherGuardianRelation = otherGuardianRelation;
	}
	public Short getGuardianRelationId() {
		return guardianRelationId;
	}

	public void setGuardianRelationId(Short guardianRelationId) {
		this.guardianRelationId = guardianRelationId;
	}

	public Short getNicOwnerRelationId() {
		return nicOwnerRelationId;
	}

	public void setNicOwnerRelationId(Short nicOwnerRelationId) {
		this.nicOwnerRelationId = nicOwnerRelationId;
	}

	public Short getReligionId() {
		return religionId;
	}

	public void setReligionId(Short religionId) {
		this.religionId = religionId;
	}

	public Short getEthnicityId() {
		return ethnicityId;
	}

	public void setEthnicityId(Short ethnicityId) {
		this.ethnicityId = ethnicityId;
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

	/**
	 * Gets the religion.
	 *
	 * @return the religion
	 */
	public Religion getReligion() {
		return religion;
	}

	/**
	 * Sets the religion.
	 *
	 * @param religion the new religion
	 */
	void setReligion(Religion religion) {
		this.religion = religion;
	}

	public String getOtherReligion() {
		return otherReligion;
	}
	public void setOtherReligion(String otherReligion) {
		this.otherReligion = otherReligion;
	}
	/**
	 * Gets the ethnicity.
	 *
	 * @return the ethnicity
	 */
	public Ethnicity getEthnicity() {
		return ethnicity;
	}

	/**
	 * Sets the ethnicity.
	 *
	 * @param ethnicity the new ethnicity
	 */
	void setEthnicity(Ethnicity ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getOtherEthnicity() {
		return otherEthnicity;
	}
	public void setOtherEthnicity(String otherEthnicity) {
		this.otherEthnicity = otherEthnicity;
	}
	
	public Short getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Short languageId) {
		this.languageId = languageId;
	}

	public Language getLanguage() {
		return language;
	}

	void setLanguage(Language language) {
		this.language = language;
	}

	public String getOtherLanguage() {
		return otherLanguage;
	}

	public void setOtherLanguage(String otherLanguage) {
		this.otherLanguage = otherLanguage;
	}

	/**
	 * Gets the guardian first name.
	 *
	 * @return the guardian first name
	 */
	public String getGuardianFirstName() {
		return guardianFirstName;
	}

	/**
	 * Sets the guardian first name.
	 *
	 * @param guardianFirstName the new guardian first name
	 */
	public void setGuardianFirstName(String guardianFirstName) {
		this.guardianFirstName = guardianFirstName;
	}

	/**
	 * Gets the guardian middle name.
	 *
	 * @return the guardian middle name
	 */
	public String getGuardianMiddleName() {
		return guardianMiddleName;
	}

	/**
	 * Sets the guardian middle name.
	 *
	 * @param guardianMiddleName the new guardian middle name
	 */
	public void setGuardianMiddleName(String guardianMiddleName) {
		this.guardianMiddleName = guardianMiddleName;
	}

	/**
	 * Gets the guardian last name.
	 *
	 * @return the guardian last name
	 */
	public String getGuardianLastName() {
		return guardianLastName;
	}

	/**
	 * Sets the guardian last name.
	 *
	 * @param guardianLastName the new guardian last name
	 */
	public void setGuardianLastName(String guardianLastName) {
		this.guardianLastName = guardianLastName;
	}

	/**
	 * Gets the father first name.
	 *
	 * @return the father first name
	 */
	public String getFatherFirstName() {
		return fatherFirstName;
	}

	/**
	 * Sets the father first name.
	 *
	 * @param fatherFirstName the new father first name
	 */
	public void setFatherFirstName(String fatherFirstName) {
		this.fatherFirstName = fatherFirstName;
	}

	/**
	 * Gets the father middle name.
	 *
	 * @return the father middle name
	 */
	public String getFatherMiddleName() {
		return fatherMiddleName;
	}

	/**
	 * Sets the father middle name.
	 *
	 * @param fatherMiddleName the new father middle name
	 */
	public void setFatherMiddleName(String fatherMiddleName) {
		this.fatherMiddleName = fatherMiddleName;
	}

	/**
	 * Gets the father last name.
	 *
	 * @return the father last name
	 */
	public String getFatherLastName() {
		return fatherLastName;
	}

	/**
	 * Sets the father last name.
	 *
	 * @param fatherLastName the new father last name
	 */
	public void setFatherLastName(String fatherLastName) {
		this.fatherLastName = fatherLastName;
	}

	/**
	 * @return the motherFirstName
	 */
	public String getMotherFirstName() {
		return motherFirstName;
	}
	/**
	 * @param motherFirstName the motherFirstName to set
	 */
	public void setMotherFirstName(String motherFirstName) {
		this.motherFirstName = motherFirstName;
	}
	/**
	 * @return the motherMiddleName
	 */
	public String getMotherMiddleName() {
		return motherMiddleName;
	}
	/**
	 * @param motherMiddleName the motherMiddleName to set
	 */
	public void setMotherMiddleName(String motherMiddleName) {
		this.motherMiddleName = motherMiddleName;
	}
	/**
	 * @return the motherLastName
	 */
	public String getMotherLastName() {
		return motherLastName;
	}
	/**
	 * @param motherLastName the motherLastName to set
	 */
	public void setMotherLastName(String motherLastName) {
		this.motherLastName = motherLastName;
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
	 * Gets the date enrolled.
	 *
	 * @return the date enrolled
	 */
	public Date getDateEnrolled() {
		return dateEnrolled;
	}
	
	/**
	 * Sets the date enrolled.
	 *
	 * @param dateEnrolled the new date enrolled
	 */
	public void setDateEnrolled(Date dateEnrolled) {
		this.dateEnrolled = dateEnrolled;
	}
	
	/**
	 * Gets the date of completion.
	 *
	 * @return the date of completion
	 */
	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}
	
	/**
	 * Sets the date of completion.
	 *
	 * @param dateOfCompletion the new date of completion
	 */
	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}
	
	/**
	 * Gets the termination date.
	 *
	 * @return the termination date
	 */
	public Date getTerminationDate() {
		return terminationDate;
	}
	
	/**
	 * Sets the termination date.
	 *
	 * @param terminationDate the new termination date
	 */
	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}
	
	/**
	 * Gets the termination reason.
	 *
	 * @return the termination reason
	 */
	public String getTerminationReason() {
		return terminationReason;
	}
	
	/**
	 * Sets the termination reason.
	 *
	 * @param terminationReason the new termination reason
	 */
	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}
	
	public String getTerminationReasonOther() {
		return terminationReasonOther;
	}

	public void setTerminationReasonOther(String terminationReasonOther) {
		this.terminationReasonOther = terminationReasonOther;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public STATUS getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(STATUS status) {
		this.status = status;
	}
	
/*	public Boolean getIsMotherNursingChild() {
		return isMotherNursingChild;
	}

	public void setIsMotherNursingChild(Boolean isMotherNursingChild) {
		this.isMotherNursingChild = isMotherNursingChild;
	}

	public String getWhyNotNursing() {
		return whyNotNursing;
	}

	public void setWhyNotNursing(String whyNotNursing) {
		this.whyNotNursing = whyNotNursing;
	}

	public String getNursingDetails() {
		return nursingDetails;
	}

	public void setNursingDetails(String nursingDetails) {
		this.nursingDetails = nursingDetails;
	}*/

	public Short getEnrollmentVaccineId() {
		return enrollmentVaccineId;
	}

	public void setEnrollmentVaccineId(Short enrollmentVaccineId) {
		this.enrollmentVaccineId = enrollmentVaccineId;
	}

	public Vaccine getEnrollmentVaccine() {
		return enrollmentVaccine;
	}

	void setEnrollmentVaccine(Vaccine enrollmentVaccine) {
		this.enrollmentVaccine = enrollmentVaccine;
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