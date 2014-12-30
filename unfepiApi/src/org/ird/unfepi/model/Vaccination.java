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

@Entity
@Table (name = "vaccination")
public class Vaccination implements java.io.Serializable {

	private static final long serialVersionUID = 28472961069505989L;

	public enum TimelinessStatus{
		EARLY,
		TIMELY,
		LATE,
		NA,
		UNKNOWN
	}
	
	public enum VACCINATION_STATUS{
		
		PENDING("PND"),
		
		VACCINATED("VACC"),
		
		NOT_VACCINATED("NVAC"),
		
		/**
		 * Entry might be a result of unfilled form, or incomplete process that partially filled the table data
		 * to make some other process work that can not work without a vaccination in place.
		 * For e.g. Lottery Generator needs to make entries into lottery related tables but that depends on vaccination.
		 * So need is to run lottery even if vaccination data could not be filled or is not available at the moment. 
		 * To make the process work, vaccination table would be filled with necessary data 
		 * and a form would be logged as 'to be filled later' allowing lottery to take place with out followup form.
		 */
		UNFILLED("UFLD");
		
		private String REPRESENTATION;
		
		public String getREPRESENTATION() {
			return REPRESENTATION;
		}

		private VACCINATION_STATUS(String representation) {
			this.REPRESENTATION = representation;
		}
		
		public static VACCINATION_STATUS findEnum(String representationString){
			for (VACCINATION_STATUS en : VACCINATION_STATUS.values()) {
				if(en.REPRESENTATION.equalsIgnoreCase(representationString)){
					return en;
				}
			}
			return null;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int	vaccinationRecordNum;
	
	private Integer childId;
	
	/** The child. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "childId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccination_childId_child_mappedId_FK")
	private Child	child;
	
	private Short vaccineId;
	
	/** The vaccine. */
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccination_vaccineId_vaccine_vaccineId_FK")
	private Vaccine	vaccine;
	
	private Boolean polioVaccineGiven;
	
	private String reasonPolioVaccineNotGiven;

	private String reasonPolioVaccineNotGivenOther;

	private Boolean pcvGiven;

	private Boolean hasApprovedLottery;
	
	private Integer vaccinationCenterId;
	
	/** The vaccination center. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccination_vaccinationCenterId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter	vaccinationCenter;
	
	private Integer vaccinatorId;
	
	/** The vaccinator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccination_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator	vaccinator;
	
	/** The dose. */
	@Column(length = 30)
	private String	dose;

	/** The vaccination date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date	vaccinationDate;
	
	/** The vaccination duedate. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date	vaccinationDuedate;
	
	private Boolean isVaccinationCenterChanged;
	
	private Boolean isFirstVaccination;
	 
	private Short timelinessFactor;
	
	private TimelinessStatus timelinessStatus;
	
	/** The next vaccination record num. */
	private Integer	nextVaccinationRecordNum;

	/** The previous vaccination record num. */
	private Integer	previousVaccinationRecordNum;
	
	private String preferredReminderTiming;
	/** The weight. */
	private Float weight;
	
	/** The height. */
	private Float height;

	private Short broughtByRelationshipId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broughtByRelationshipId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccination_broughtByRelationship_relationship_relationshipId_FK")
	private Relationship broughtByRelationship;
	
	@Column(length = 30)
	private String otherBroughtByRelationship;
	
	/** The epi number. */
	private String	epiNumber;

	/** The vaccination status. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private VACCINATION_STATUS	vaccinationStatus;
	
	/** The reason not timely vaccination. */
	private String	reasonNotTimelyVaccination;
	
	private String	reasonNotTimelyVaccinationOther;

	/** The next assigned date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date	nextAssignedDate;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vaccination_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "vaccination_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	/**
	 * Instantiates a new vaccination.
	 */
	public Vaccination() {
	}

	/**
	 * Gets the vaccination record num.
	 *
	 * @return the vaccination record num
	 */
	public int getVaccinationRecordNum() {
		return vaccinationRecordNum;
	}

	/**
	 * Sets the vaccination record num.
	 *
	 * @param vaccinationRecordNum the new vaccination record num
	 */
	public void setVaccinationRecordNum(int vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}

	/**
	 * Gets the child.
	 *
	 * @return the child
	 */
	public Child getChild() {
		return child;
	}

	/**
	 * Sets the child.
	 *
	 * @param child the new child
	 */
	void setChild(Child child) {
		this.child = child;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public Short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(Short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public Integer getVaccinationCenterId() {
		return vaccinationCenterId;
	}

	public void setVaccinationCenterId(Integer vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}

	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}

	public Short getBroughtByRelationshipId() {
		return broughtByRelationshipId;
	}

	public void setBroughtByRelationshipId(Short broughtByRelationshipId) {
		this.broughtByRelationshipId = broughtByRelationshipId;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public Boolean getPolioVaccineGiven() {
		return polioVaccineGiven;
	}

	public void setPolioVaccineGiven(Boolean polioVaccineGiven) {
		this.polioVaccineGiven = polioVaccineGiven;
	}

	public String getReasonPolioVaccineNotGiven() {
		return reasonPolioVaccineNotGiven;
	}

	public void setReasonPolioVaccineNotGiven(String reasonPolioVaccineNotGiven) {
		this.reasonPolioVaccineNotGiven = reasonPolioVaccineNotGiven;
	}

	public String getReasonPolioVaccineNotGivenOther() {
		return reasonPolioVaccineNotGivenOther;
	}

	public void setReasonPolioVaccineNotGivenOther(
			String reasonPolioVaccineNotGivenOther) {
		this.reasonPolioVaccineNotGivenOther = reasonPolioVaccineNotGivenOther;
	}

	public Boolean getPcvGiven() {
		return pcvGiven;
	}

	public void setPcvGiven(Boolean pcvGiven) {
		this.pcvGiven = pcvGiven;
	}

	public Boolean getHasApprovedLottery() {
		return hasApprovedLottery;
	}

	public void setHasApprovedLottery(Boolean hasApprovedLottery) {
		this.hasApprovedLottery = hasApprovedLottery;
	}

	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public Date getVaccinationDate() {
		return vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public Date getVaccinationDuedate() {
		return vaccinationDuedate;
	}

	public void setVaccinationDuedate(Date vaccinationDuedate) {
		this.vaccinationDuedate = vaccinationDuedate;
	}

	public Boolean getIsVaccinationCenterChanged() {
		return isVaccinationCenterChanged;
	}

	public void setIsVaccinationCenterChanged(Boolean isVaccinationCenterChanged) {
		this.isVaccinationCenterChanged = isVaccinationCenterChanged;
	}

	public Boolean getIsFirstVaccination() {
		return isFirstVaccination;
	}

	public void setIsFirstVaccination(Boolean isFirstVaccination) {
		this.isFirstVaccination = isFirstVaccination;
	}

	public Short getTimelinessFactor() {
		return timelinessFactor;
	}

	public void setTimelinessFactor(Short timelinessFactor) {
		this.timelinessFactor = timelinessFactor;
	}

	public TimelinessStatus getTimelinessStatus() {
		return timelinessStatus;
	}

	public void setTimelinessStatus(TimelinessStatus timelinessStatus) {
		this.timelinessStatus = timelinessStatus;
	}

	public Integer getNextVaccinationRecordNum() {
		return nextVaccinationRecordNum;
	}

	public void setNextVaccinationRecordNum(Integer nextVaccinationRecordNum) {
		this.nextVaccinationRecordNum = nextVaccinationRecordNum;
	}

	/**
	 * Gets the previous vaccination record num.
	 *
	 * @return the previous vaccination record num
	 */
	public Integer getPreviousVaccinationRecordNum() {
		return previousVaccinationRecordNum;
	}

	/**
	 * Sets the previous vaccination record num.
	 *
	 * @param previousVaccinationRecordNum the new previous vaccination record num
	 */
	public void setPreviousVaccinationRecordNum(Integer previousVaccinationRecordNum) {
		this.previousVaccinationRecordNum = previousVaccinationRecordNum;
	}

	public String getPreferredReminderTiming() {
		return preferredReminderTiming;
	}

	public void setPreferredReminderTiming(String preferredReminderTiming) {
		this.preferredReminderTiming = preferredReminderTiming;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public Float getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(Float height) {
		this.height = height;
	}

	/**
	 * @return the broughtByRelationship
	 */
	public Relationship getBroughtByRelationship() {
		return broughtByRelationship;
	}

	/**
	 * @param broughtByRelationship the broughtByRelationship to set
	 */
	void setBroughtByRelationship(Relationship broughtByRelationship) {
		this.broughtByRelationship = broughtByRelationship;
	}

	/**
	 * @return the otherBroughtByRelationship
	 */
	public String getOtherBroughtByRelationship() {
		return otherBroughtByRelationship;
	}

	/**
	 * @param otherBroughtByRelationship the otherBroughtByRelationship to set
	 */
	public void setOtherBroughtByRelationship(String otherBroughtByRelationship) {
		this.otherBroughtByRelationship = otherBroughtByRelationship;
	}

	/**
	 * Gets the epi number.
	 *
	 * @return the epi number
	 */
	public String getEpiNumber() {
		return epiNumber;
	}

	/**
	 * Sets the epi number.
	 *
	 * @param epiNumber the new epi number
	 */
	public void setEpiNumber(String epiNumber) {
		this.epiNumber = epiNumber;
	}

	/**
	 * Gets the vaccination status.
	 *
	 * @return the vaccination status
	 */
	public VACCINATION_STATUS getVaccinationStatus() {
		return vaccinationStatus;
	}

	/**
	 * Sets the vaccination status.
	 *
	 * @param vaccinationStatus the new vaccination status
	 */
	public void setVaccinationStatus(VACCINATION_STATUS vaccinationStatus) {
		this.vaccinationStatus = vaccinationStatus;
	}

	/**
	 * Gets the reason not timely vaccination.
	 *
	 * @return the reason not timely vaccination
	 */
	public String getReasonNotTimelyVaccination() {
		return reasonNotTimelyVaccination;
	}

	/**
	 * Sets the reason not timely vaccination.
	 *
	 * @param reasonNotTimelyVaccination the new reason not timely vaccination
	 */
	public void setReasonNotTimelyVaccination(String reasonNotTimelyVaccination) {
		this.reasonNotTimelyVaccination = reasonNotTimelyVaccination;
	}

	public String getReasonNotTimelyVaccinationOther() {
		return reasonNotTimelyVaccinationOther;
	}

	public void setReasonNotTimelyVaccinationOther(
			String reasonNotTimelyVaccinationOther) {
		this.reasonNotTimelyVaccinationOther = reasonNotTimelyVaccinationOther;
	}

	/**
	 * Gets the next assigned date.
	 *
	 * @return the next assigned date
	 */
	public Date getNextAssignedDate() {
		return nextAssignedDate;
	}

	/**
	 * Sets the next assigned date.
	 *
	 * @param nextAssignedDate the new next assigned date
	 */
	public void setNextAssignedDate(Date nextAssignedDate) {
		this.nextAssignedDate = nextAssignedDate;
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