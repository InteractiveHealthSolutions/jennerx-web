package org.ird.unfepi.model;

import java.util.Date;

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

@Entity
@Table(name="lotterysms")
public class LotterySms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int serialNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	datePreferenceChanged;
	
	private Integer mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false)
	@ForeignKey(name = "lotterysms_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	private String preferredSmsTiming;

	/** The has approved lottery. */
	private Boolean hasApprovedLottery;
	
	/** The reason lottery rejection. */
	private String reasonLotteryRejection;
	
	private String reasonLotteryRejectionOther;

	/** The has approved lottery. */
	private Boolean hasApprovedReminders;
	
	/** The reason lottery rejection. */
	private String reasonRemindersRejection;
	
	private String reasonRemindersRejectionOther;
	
	/** The has cell phone access. */
	private Boolean hasCellPhoneAccess;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "lotterysms_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "lotterysms_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public LotterySms() {
		
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getDatePreferenceChanged() {
		return datePreferenceChanged;
	}

	public void setDatePreferenceChanged(Date datePreferenceChanged) {
		this.datePreferenceChanged = datePreferenceChanged;
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

	public String getPreferredSmsTiming() {
		return preferredSmsTiming;
	}

	public void setPreferredSmsTiming(String preferredSmsTiming) {
		this.preferredSmsTiming = preferredSmsTiming;
	}

	/**
	 * Gets the checks for approved lottery.
	 *
	 * @return the checks for approved lottery
	 */
	public Boolean getHasApprovedLottery() {
		return hasApprovedLottery;
	}

	/**
	 * Sets the checks for approved lottery.
	 *
	 * @param hasApprovedLottery the new checks for approved lottery
	 */
	public void setHasApprovedLottery(Boolean hasApprovedLottery) {
		this.hasApprovedLottery = hasApprovedLottery;
	}

	/**
	 * Gets the reason lottery rejection.
	 *
	 * @return the reason lottery rejection
	 */
	public String getReasonLotteryRejection() {
		return reasonLotteryRejection;
	}

	/**
	 * Sets the reason lottery rejection.
	 *
	 * @param reasonLotteryRejection the new reason lottery rejection
	 */
	public void setReasonLotteryRejection(String reasonLotteryRejection) {
		this.reasonLotteryRejection = reasonLotteryRejection;
	}

	public String getReasonLotteryRejectionOther() {
		return reasonLotteryRejectionOther;
	}

	public void setReasonLotteryRejectionOther(
			String reasonLotteryRejectionOther) {
		this.reasonLotteryRejectionOther = reasonLotteryRejectionOther;
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
	}
	
	public String getReasonRemindersRejectionOther() {
		return reasonRemindersRejectionOther;
	}

	public void setReasonRemindersRejectionOther(
			String reasonRemindersRejectionOther) {
		this.reasonRemindersRejectionOther = reasonRemindersRejectionOther;
	}

	/**
	 * Gets the checks for cell phone access.
	 *
	 * @return the checks for cell phone access
	 */
	public Boolean getHasCellPhoneAccess() {
		return hasCellPhoneAccess;
	}

	/**
	 * Sets the checks for cell phone access.
	 *
	 * @param hasCellPhoneAccess the new checks for cell phone access
	 */
	public void setHasCellPhoneAccess(Boolean hasCellPhoneAccess) {
		this.hasCellPhoneAccess = hasCellPhoneAccess;
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
