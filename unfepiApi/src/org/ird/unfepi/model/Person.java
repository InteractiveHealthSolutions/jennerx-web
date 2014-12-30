package org.ird.unfepi.model;

/*
 * 
 
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Model.MaritalStatus;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;

*//**
 * The Class Person.
 *//*
@Entity
@Table(name = "person")
public class Person {
	
	*//** The person id. *//*
	@Id
	private int mappedId;
	
	*//** The first name of the user. *//*

	@Column(length = 20)
	private String firstName;

	*//** The middle name of the user. *//*

	@Column(length = 20)
	private String middleName;

	*//** The last name of the user. *//*

	@Column(length = 20)
	private String lastName;
	
	*//** The birthdate. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date	birthdate;
	
	*//** The age. *//*
	@Transient
	private Integer	age;
	
	*//** The estimated birthdate. *//*
	private Boolean	estimatedBirthdate;
	
	*//** The guardian first name. *//*
	@Column(length = 20)
	private String guardianFirstName;
	
	*//** The guardian middle name. *//*
	@Column(length = 20)
	private String guardianMiddleName;
	
	*//** The guardian last name. *//*
	@Column(length = 20)
	private String guardianLastName;
	
	*//** The guardian relation. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guardianRelation")
	@ForeignKey(name = "person_guardianRelation_relationship_relationshipId_FK")
	private Relationship guardianRelation;

	@Column(length = 30)
	private String otherGuardianRelation;
	
	*//** The father first name. *//*
	@Column(length = 20)
	private String fatherFirstName;
	
	*//** The father middle name. *//*
	@Column(length = 20)
	private String fatherMiddleName;
	
	*//** The father last name. *//*
	@Column(length = 20)
	private String fatherLastName;
	
	*//** The father first name. *//*
	@Column(length = 20)
	private String motherFirstName;
	
	*//** The father middle name. *//*
	@Column(length = 20)
	private String motherMiddleName;
	
	*//** The father last name. *//*
	@Column(length = 20)
	private String motherLastName;
	
	*//** The spouse first name. *//*
	@Column(length = 30)
	private String spouseFirstName;
	
	*//** The spouse middle name. *//*
	@Column(length = 30)
	private String spouseMiddleName;
	
	*//** The spouse last name. *//*
	@Column(length = 30)
	private String spouseLastName;
	
	*//** The NIC. *//*
	@Column(length = 20)
	private String nic;
	
	*//** The NIC owner name. *//*
	@Column(length = 30)
	private String nicOwnerFirstName;
	
	*//** The NIC owner name. *//*
	@Column(length = 30)
	private String nicOwnerLastName;
	
	*//** The NIC owner relation. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NICOwnerRelation")
	@ForeignKey(name = "person_NICOwnerRelation_relationship_relationshipId_FK")
	private Relationship nicOwnerRelation;
	
	@Column(length = 30)
	private String otherNicOwnerRelation;
	
	*//** The domicile. *//*
	@Column(length = 30)
	private String domicile;
	
	*//** The gender. *//*
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Gender gender;
	
	*//** The marital status. *//*
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MaritalStatus maritalStatus;
	
	*//** The religion. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "religion")
	@ForeignKey(name = "person_religion_religion_religionId_FK")
	private Religion religion;
	
	@Column(length = 30)
	private String otherReligion;
	
	*//** The ethnicity. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ethnicity")
	@ForeignKey(name = "person_ethnicity_ethnicity_ethnicityId_FK")
	private Ethnicity ethnicity;

	@Column(length = 30)
	private String otherEthnicity;
	
	@Column(length = 50)
	private String qualification;

	@Column(length = 30)
	private String designation;
	
	public Person() {
		
	}
	public Person(int mappedId) {
		this.mappedId = mappedId;
	}
	
	public int getMappedId() {
		return mappedId;
	}

	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}

	*//**
	 * @return the firstName
	 *//*
	public String getFirstName() {
		return firstName;
	}
	*//**
	 * @param firstName the firstName to set
	 *//*
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	*//**
	 * @return the middleName
	 *//*
	public String getMiddleName() {
		return middleName;
	}
	*//**
	 * @param middleName the middleName to set
	 *//*
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	*//**
	 * @return the lastName
	 *//*
	public String getLastName() {
		return lastName;
	}
	*//**
	 * @param lastName the lastName to set
	 *//*
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	*//**
	 * Gets the full name.
	 *
	 * @return the full name
	 *//*
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
	
	*//**
	 * Gets the birthdate.
	 *
	 * @return the birthdate
	 *//*
	public Date getBirthdate() {
		return birthdate;
	}
	
	*//**
	 * Sets the birthdate.
	 *
	 * @param birthdate the new birthdate
	 *//*
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	*//**
	 * Sets the age.
	 *
	 * @param ageInWeeks the new age
	 *//*
	public void setAge(Integer ageInWeeks) {
		age=ageInWeeks;
	}
	
	*//**
	 * Gets the age.
	 *
	 * @return the age
	 *//*
	public Integer getAge() {
		if(getBirthdate()!=null){
			return age= DateUtils.differenceBetweenIntervals(new Date(), getBirthdate() , TIME_INTERVAL.WEEK);
		}
		return age;
	}
	
	*//**
	 * Gets the estimated birthdate.
	 *
	 * @return the estimated birthdate
	 *//*
	public Boolean getEstimatedBirthdate() {
		return estimatedBirthdate;
	}
	
	*//**
	 * Sets the estimated birthdate.
	 *
	 * @param estimatedBirthdate the new estimated birthdate
	 *//*
	public void setEstimatedBirthdate(Boolean estimatedBirthdate) {
		this.estimatedBirthdate = estimatedBirthdate;
	}
	*//**
	 * Gets the guardian relation.
	 *
	 * @return the guardian relation
	 *//*
	public Relationship getGuardianRelation() {
		return guardianRelation;
	}

	*//**
	 * Sets the guardian relation.
	 *
	 * @param guardianRelation the new guardian relation
	 *//*
	public void setGuardianRelation(Relationship guardianRelation) {
		this.guardianRelation = guardianRelation;
	}

	public String getOtherGuardianRelation() {
		return otherGuardianRelation;
	}
	public void setOtherGuardianRelation(String otherGuardianRelation) {
		this.otherGuardianRelation = otherGuardianRelation;
	}
	*//**
	 * Gets the domicile.
	 *
	 * @return the domicile
	 *//*
	public String getDomicile() {
		return domicile;
	}

	*//**
	 * Sets the domicile.
	 *
	 * @param domicile the new domicile
	 *//*
	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	*//**
	 * Gets the gender.
	 *
	 * @return the gender
	 *//*
	public Gender getGender() {
		return gender;
	}

	*//**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 *//*
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	*//**
	 * Gets the marital status.
	 *
	 * @return the marital status
	 *//*
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	*//**
	 * Sets the marital status.
	 *
	 * @param maritalStatus the new marital status
	 *//*
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	*//**
	 * Gets the religion.
	 *
	 * @return the religion
	 *//*
	public Religion getReligion() {
		return religion;
	}

	*//**
	 * Sets the religion.
	 *
	 * @param religion the new religion
	 *//*
	public void setReligion(Religion religion) {
		this.religion = religion;
	}

	public String getOtherReligion() {
		return otherReligion;
	}
	public void setOtherReligion(String otherReligion) {
		this.otherReligion = otherReligion;
	}
	*//**
	 * Gets the ethnicity.
	 *
	 * @return the ethnicity
	 *//*
	public Ethnicity getEthnicity() {
		return ethnicity;
	}

	*//**
	 * Sets the ethnicity.
	 *
	 * @param ethnicity the new ethnicity
	 *//*
	public void setEthnicity(Ethnicity ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getOtherEthnicity() {
		return otherEthnicity;
	}
	public void setOtherEthnicity(String otherEthnicity) {
		this.otherEthnicity = otherEthnicity;
	}
	
	*//**
	 * Gets the guardian first name.
	 *
	 * @return the guardian first name
	 *//*
	public String getGuardianFirstName() {
		return guardianFirstName;
	}

	*//**
	 * Sets the guardian first name.
	 *
	 * @param guardianFirstName the new guardian first name
	 *//*
	public void setGuardianFirstName(String guardianFirstName) {
		this.guardianFirstName = guardianFirstName;
	}

	*//**
	 * Gets the guardian middle name.
	 *
	 * @return the guardian middle name
	 *//*
	public String getGuardianMiddleName() {
		return guardianMiddleName;
	}

	*//**
	 * Sets the guardian middle name.
	 *
	 * @param guardianMiddleName the new guardian middle name
	 *//*
	public void setGuardianMiddleName(String guardianMiddleName) {
		this.guardianMiddleName = guardianMiddleName;
	}

	*//**
	 * Gets the guardian last name.
	 *
	 * @return the guardian last name
	 *//*
	public String getGuardianLastName() {
		return guardianLastName;
	}

	*//**
	 * Sets the guardian last name.
	 *
	 * @param guardianLastName the new guardian last name
	 *//*
	public void setGuardianLastName(String guardianLastName) {
		this.guardianLastName = guardianLastName;
	}

	*//**
	 * Gets the father first name.
	 *
	 * @return the father first name
	 *//*
	public String getFatherFirstName() {
		return fatherFirstName;
	}

	*//**
	 * Sets the father first name.
	 *
	 * @param fatherFirstName the new father first name
	 *//*
	public void setFatherFirstName(String fatherFirstName) {
		this.fatherFirstName = fatherFirstName;
	}

	*//**
	 * Gets the father middle name.
	 *
	 * @return the father middle name
	 *//*
	public String getFatherMiddleName() {
		return fatherMiddleName;
	}

	*//**
	 * Sets the father middle name.
	 *
	 * @param fatherMiddleName the new father middle name
	 *//*
	public void setFatherMiddleName(String fatherMiddleName) {
		this.fatherMiddleName = fatherMiddleName;
	}

	*//**
	 * Gets the father last name.
	 *
	 * @return the father last name
	 *//*
	public String getFatherLastName() {
		return fatherLastName;
	}

	*//**
	 * Sets the father last name.
	 *
	 * @param fatherLastName the new father last name
	 *//*
	public void setFatherLastName(String fatherLastName) {
		this.fatherLastName = fatherLastName;
	}

	*//**
	 * @return the motherFirstName
	 *//*
	public String getMotherFirstName() {
		return motherFirstName;
	}
	*//**
	 * @param motherFirstName the motherFirstName to set
	 *//*
	public void setMotherFirstName(String motherFirstName) {
		this.motherFirstName = motherFirstName;
	}
	*//**
	 * @return the motherMiddleName
	 *//*
	public String getMotherMiddleName() {
		return motherMiddleName;
	}
	*//**
	 * @param motherMiddleName the motherMiddleName to set
	 *//*
	public void setMotherMiddleName(String motherMiddleName) {
		this.motherMiddleName = motherMiddleName;
	}
	*//**
	 * @return the motherLastName
	 *//*
	public String getMotherLastName() {
		return motherLastName;
	}
	*//**
	 * @param motherLastName the motherLastName to set
	 *//*
	public void setMotherLastName(String motherLastName) {
		this.motherLastName = motherLastName;
	}
	*//**
	 * Gets the spouse first name.
	 *
	 * @return the spouse first name
	 *//*
	public String getSpouseFirstName() {
		return spouseFirstName;
	}

	*//**
	 * Sets the spouse first name.
	 *
	 * @param spouseFirstName the new spouse first name
	 *//*
	public void setSpouseFirstName(String spouseFirstName) {
		this.spouseFirstName = spouseFirstName;
	}

	*//**
	 * Gets the spouse middle name.
	 *
	 * @return the spouse middle name
	 *//*
	public String getSpouseMiddleName() {
		return spouseMiddleName;
	}

	*//**
	 * Sets the spouse middle name.
	 *
	 * @param spouseMiddleName the new spouse middle name
	 *//*
	public void setSpouseMiddleName(String spouseMiddleName) {
		this.spouseMiddleName = spouseMiddleName;
	}

	*//**
	 * Gets the spouse last name.
	 *
	 * @return the spouse last name
	 *//*
	public String getSpouseLastName() {
		return spouseLastName;
	}

	*//**
	 * Sets the spouse last name.
	 *
	 * @param spouseLastName the new spouse last name
	 *//*
	public void setSpouseLastName(String spouseLastName) {
		this.spouseLastName = spouseLastName;
	}
	*//**
	 * @return the nic
	 *//*
	public String getNic() {
		return nic;
	}
	*//**
	 * @param nic the nic to set
	 *//*
	public void setNic(String nic) {
		this.nic = nic;
	}
	*//**
	 * @return the nicOwnerFirstName
	 *//*
	public String getNicOwnerFirstName() {
		return nicOwnerFirstName;
	}
	*//**
	 * @param nicOwnerFirstName the nicOwnerFirstName to set
	 *//*
	public void setNicOwnerFirstName(String nicOwnerFirstName) {
		this.nicOwnerFirstName = nicOwnerFirstName;
	}
	*//**
	 * @return the nicOwnerLastName
	 *//*
	public String getNicOwnerLastName() {
		return nicOwnerLastName;
	}
	*//**
	 * @param nicOwnerLastName the nicOwnerLastName to set
	 *//*
	public void setNicOwnerLastName(String nicOwnerLastName) {
		this.nicOwnerLastName = nicOwnerLastName;
	}
	*//**
	 * @return the nicOwnerRelation
	 *//*
	public Relationship getNicOwnerRelation() {
		return nicOwnerRelation;
	}
	*//**
	 * @param nicOwnerRelation the nicOwnerRelation to set
	 *//*
	public void setNicOwnerRelation(Relationship nicOwnerRelation) {
		this.nicOwnerRelation = nicOwnerRelation;
	}
	*//**
	 * @return the otherNicOwnerRelation
	 *//*
	public String getOtherNicOwnerRelation() {
		return otherNicOwnerRelation;
	}
	*//**
	 * @param otherNicOwnerRelation the otherNicOwnerRelation to set
	 *//*
	public void setOtherNicOwnerRelation(String otherNicOwnerRelation) {
		this.otherNicOwnerRelation = otherNicOwnerRelation;
	}
}
*/