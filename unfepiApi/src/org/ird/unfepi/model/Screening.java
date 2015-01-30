/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class Screening.
 */
@Entity
@Table(name = "screening")
public class Screening {

	/** The screening id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int screeningId;
	
	private Integer mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	private Integer vaccinationCenterId;
	
	/** The vaccination center id. */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = VaccinationCenter.class)
	@JoinColumn(name = "vaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_vaccinationCenterId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter vaccinationCenter;
	
	private Integer vaccinatorId;
	
	/** The vaccinator id. */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Vaccinator.class)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator vaccinator;
	
	/** The epi id. *//*
	@Column(length = 20)
	private String epiId;*/
	
	/*private Short cityId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Ctu.class)
	@JoinColumn(name = "cityId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_cityId_ctu_id_FK")
	private Ctu city;
	
	*//** The epi id. *//*
	@Column(length = 30)
	private String cityName;
	
	private Short townId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Ctu.class)
	@JoinColumn(name = "townId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_townId_ctu_id_FK")
	private Ctu town;
	
	@Column(length = 30)
	private String townName;*/
	
	/** The living duration. *//*
	private Byte visitDuration;
	
	private TimeIntervalUnit unitVisitDuration;*/
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date screeningDate;
	
	/** The is child healthy. */
	private Boolean isChildHealthy;
	
	/** The health problem. */
	private String healthProblem;
	
	private String healthProblemOther;

	/** The has approved lottery. *//*
	private Boolean hasApprovedLottery;
	
	*//** The reason lottery rejection. *//*
	private String reasonLotteryRejection;
	
	*//** The has approved lottery. *//*
	private Boolean hasApprovedReminders;
	
	*//** The reason lottery rejection. *//*
	private String reasonRemindersRejection;*/
	
	/*private Boolean willReturnToCurrentCenter;
	
	private Integer referredVaccinationCenterId;
	
	*//** The referred vaccination center. *//*
	*//** The vaccinator id. *//*
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = VaccinationCenter.class)
	@JoinColumn(name = "referredVaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_referredVaccinationCenter_vaccinationcenter_mappedId_FK")
	private VaccinationCenter referredVaccinationCenter;*/
	
	private Short broughtByRelationshipId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broughtByRelationshipId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_broughtByRelationship_relationship_relationshipId_FK")
	private Relationship broughtByRelationship;
	
	@Column(length = 30)
	private String otherBroughtByRelationship;
	
	/** The has cell phone access. *//*
	private Boolean hasCellPhoneAccess;*/
	
 	/** The cell number owner. *//*
	@Column(length = 30)
	private String cellNumberOwnerFirstName;
	
	@Column(length = 30)
	private String cellNumberOwnerLastName;
	
	private Integer cellNumberOwnerRelationShipId;
	
	*//** The cell number owner relation ship. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cellNumberOwnerRelationShipId", insertable = false, updatable = false)
	@ForeignKey(name = "screening_cellNumberOwnerRelationShip_relationship_relationshipId_FK")
	private Relationship cellNumberOwnerRelationShip;

	private String otherCellNumberOwnerRelationShip;
	*//** The cell number. *//*
	@Column(length = 20)
	private String cellNumber;*/

	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "screening_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "screening_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public Screening() {
		
	}
	/**
	 * @return the screeningId
	 */
	public int getScreeningId() {
		return screeningId;
	}

	/**
	 * @param screeningId the screeningId to set
	 */
	public void setScreeningId(int screeningId) {
		this.screeningId = screeningId;
	}

	/**
	 * @return the mappedId
	 */
	public Integer getMappedId() {
		return mappedId;
	}

	/**
	 * @param mappedId the mappedId to set
	 */
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
	 * @return the vaccinationCenter
	 */
	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	/**
	 * @param vaccinationCenter the vaccinationCenter to set
	 */
	void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	/**
	 * @return the vaccinator
	 */
	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	/**
	 * @param vaccinator the vaccinator to set
	 */
	void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
	}

	/**
	 * Gets the epi id.
	 *
	 * @return the epi id
	 *//*
	public String getEpiId() {
		return epiId;
	}

	*//**
	 * Sets the epi id.
	 *
	 * @param epiId the new epi id
	 *//*
	public void setEpiId(String epiId) {
		this.epiId = epiId;
	}

	public Ctu getCity() {
		return city;
	}
	void setCity(Ctu city) {
		this.city = city;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Ctu getTown() {
		return town;
	}
	void setTown(Ctu town) {
		this.town = town;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public Byte getVisitDuration() {
		return visitDuration;
	}
	public void setVisitDuration(Byte visitDuration) {
		this.visitDuration = visitDuration;
	}
	public TimeIntervalUnit getUnitVisitDuration() {
		return unitVisitDuration;
	}
	public void setUnitVisitDuration(TimeIntervalUnit unitVisitDuration) {
		this.unitVisitDuration = unitVisitDuration;
	}*/
	public Date getScreeningDate() {
		return screeningDate;
	}

	public void setScreeningDate(Date screeningDate) {
		this.screeningDate = screeningDate;
	}

	/**
	 * Gets the checks if is child healthy.
	 *
	 * @return the checks if is child healthy
	 */
	public Boolean getIsChildHealthy() {
		return isChildHealthy;
	}

	/**
	 * Sets the checks if is child healthy.
	 *
	 * @param isChildHealthy the new checks if is child healthy
	 */
	public void setIsChildHealthy(Boolean isChildHealthy) {
		this.isChildHealthy = isChildHealthy;
	}

	/**
	 * Gets the health problem.
	 *
	 * @return the health problem
	 */
	public String getHealthProblem() {
		return healthProblem;
	}

	/**
	 * Sets the health problem.
	 *
	 * @param healthProblem the new health problem
	 */
	public void setHealthProblem(String healthProblem) {
		this.healthProblem = healthProblem;
	}

	public String getHealthProblemOther() {
		return healthProblemOther;
	}
	public void setHealthProblemOther(String healthProblemOther) {
		this.healthProblemOther = healthProblemOther;
	}
	/**
	 * Gets the checks for approved lottery.
	 *
	 * @return the checks for approved lottery
	 *//*
	public Boolean getHasApprovedLottery() {
		return hasApprovedLottery;
	}

	*//**
	 * Sets the checks for approved lottery.
	 *
	 * @param hasApprovedLottery the new checks for approved lottery
	 *//*
	public void setHasApprovedLottery(Boolean hasApprovedLottery) {
		this.hasApprovedLottery = hasApprovedLottery;
	}

	*//**
	 * Gets the reason lottery rejection.
	 *
	 * @return the reason lottery rejection
	 *//*
	public String getReasonLotteryRejection() {
		return reasonLotteryRejection;
	}

	*//**
	 * Sets the reason lottery rejection.
	 *
	 * @param reasonLotteryRejection the new reason lottery rejection
	 *//*
	public void setReasonLotteryRejection(String reasonLotteryRejection) {
		this.reasonLotteryRejection = reasonLotteryRejection;
	}

	public Boolean getHasApprovedReminders() {
		return hasApprovedReminders;
	}
	public void setHasApprovedReminders(Boolean hasApprovedReminders) {
		this.hasApprovedReminders = hasApprovedReminders;
	}
	public String getReasonRemindersRejection() {
		return reasonRemindersRejection;
	}
	public void setReasonRemindersRejection(String reasonRemindersRejection) {
		this.reasonRemindersRejection = reasonRemindersRejection;
	}*/
	/*public Boolean getWillReturnToCurrentCenter() {
		return willReturnToCurrentCenter;
	}
	public void setWillReturnToCurrentCenter(Boolean willReturnToCurrentCenter) {
		this.willReturnToCurrentCenter = willReturnToCurrentCenter;
	}
	public VaccinationCenter getReferredVaccinationCenter() {
		return referredVaccinationCenter;
	}
	void setReferredVaccinationCenter(VaccinationCenter referredVaccinationCenter) {
		this.referredVaccinationCenter = referredVaccinationCenter;
	}*/
	public Relationship getBroughtByRelationship() {
		return broughtByRelationship;
	}
	void setBroughtByRelationship(Relationship broughtByRelationship) {
		this.broughtByRelationship = broughtByRelationship;
	}
	public String getOtherBroughtByRelationship() {
		return otherBroughtByRelationship;
	}
	public void setOtherBroughtByRelationship(String otherBroughtByRelationship) {
		this.otherBroughtByRelationship = otherBroughtByRelationship;
	}
	/**
	 * Gets the checks for cell phone access.
	 *
	 * @return the checks for cell phone access
	 *//*
	public Boolean getHasCellPhoneAccess() {
		return hasCellPhoneAccess;
	}

	*//**
	 * Sets the checks for cell phone access.
	 *
	 * @param hasCellPhoneAccess the new checks for cell phone access
	 *//*
	public void setHasCellPhoneAccess(Boolean hasCellPhoneAccess) {
		this.hasCellPhoneAccess = hasCellPhoneAccess;
	}*/

	/*public String getCellNumberOwnerFirstName() {
		return cellNumberOwnerFirstName;
	}

	public void setCellNumberOwnerFirstName(String cellNumberOwnerFirstName) {
		this.cellNumberOwnerFirstName = cellNumberOwnerFirstName;
	}

	public String getCellNumberOwnerLastName() {
		return cellNumberOwnerLastName;
	}

	public void setCellNumberOwnerLastName(String cellNumberOwnerLastName) {
		this.cellNumberOwnerLastName = cellNumberOwnerLastName;
	}

	*//**
	 * Gets the cell number owner relation ship.
	 *
	 * @return the cell number owner relation ship
	 *//*
	public Relationship getCellNumberOwnerRelationShip() {
		return cellNumberOwnerRelationShip;
	}

	*//**
	 * Sets the cell number owner relation ship.
	 *
	 * @param cellNumberOwnerRelationShip the new cell number owner relation ship
	 *//*
	void setCellNumberOwnerRelationShip(Relationship cellNumberOwnerRelationShip) {
		this.cellNumberOwnerRelationShip = cellNumberOwnerRelationShip;
	}

	public String getOtherCellNumberOwnerRelationShip() {
		return otherCellNumberOwnerRelationShip;
	}
	public void setOtherCellNumberOwnerRelationShip(
			String otherCellNumberOwnerRelationShip) {
		this.otherCellNumberOwnerRelationShip = otherCellNumberOwnerRelationShip;
	}
	*//**
	 * Gets the cell number.
	 *
	 * @return the cell number
	 *//*
	public String getCellNumber() {
		return cellNumber;
	}

	*//**
	 * Sets the cell number.
	 *
	 * @param cellNumber the new cell number
	 *//*
	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
	}*/
	
	/**
	 * @return the vaccinationCenterId
	 */
	public Integer getVaccinationCenterId() {
		return vaccinationCenterId;
	}
	/**
	 * @param vaccinationCenterId the vaccinationCenterId to set
	 */
	public void setVaccinationCenterId(Integer vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}
	/**
	 * @return the vaccinatorId
	 */
	public Integer getVaccinatorId() {
		return vaccinatorId;
	}
	/**
	 * @param vaccinatorId the vaccinatorId to set
	 */
	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}
	/**
	 * @return the cityId
	 *//*
	public Short getCityId() {
		return cityId;
	}
	*//**
	 * @param cityId the cityId to set
	 *//*
	public void setCityId(Short cityId) {
		this.cityId = cityId;
	}
	*//**
	 * @return the townId
	 *//*
	public Short getTownId() {
		return townId;
	}
	*//**
	 * @param townId the townId to set
	 *//*
	public void setTownId(Short townId) {
		this.townId = townId;
	}
	*//**
	 * @return the referredVaccinationCenterId
	 *//*
	public Integer getReferredVaccinationCenterId() {
		return referredVaccinationCenterId;
	}
	*//**
	 * @param referredVaccinationCenterId the referredVaccinationCenterId to set
	 *//*
	public void setReferredVaccinationCenterId(Integer referredVaccinationCenterId) {
		this.referredVaccinationCenterId = referredVaccinationCenterId;
	}*/
	/**
	 * @return the broughtByRelationshipId
	 */
	public Short getBroughtByRelationshipId() {
		return broughtByRelationshipId;
	}
	/**
	 * @param broughtByRelationshipId the broughtByRelationshipId to set
	 */
	public void setBroughtByRelationshipId(Short broughtByRelationshipId) {
		this.broughtByRelationshipId = broughtByRelationshipId;
	}
	/**
	 * @return the cellNumberOwnerRelationShipId
	 *//*
	public Integer getCellNumberOwnerRelationShipId() {
		return cellNumberOwnerRelationShipId;
	}
	*//**
	 * @param cellNumberOwnerRelationShipId the cellNumberOwnerRelationShipId to set
	 *//*
	public void setCellNumberOwnerRelationShipId(
			Integer cellNumberOwnerRelationShipId) {
		this.cellNumberOwnerRelationShipId = cellNumberOwnerRelationShipId;
	}*/
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
