/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class Arm.
 */
@Entity 
@Table(name = "arm")
public class Arm implements java.io.Serializable {

	/** The arm id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short armId;
	
	/** The arm name. */
	@Column(length = 30, unique = true)
	private String armName;
	
	/** The reminder days sequence. */
	private String reminderDaysSequence;
	
	/** The send sms. */
	private Boolean sendSms;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "arm_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "arm_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	/** The armday. */
	@OneToMany(fetch = FetchType.LAZY, targetEntity=ArmDayReminder.class)
	@JoinColumn(name = "armId")
	@ForeignKey(name = "arm_armId_ArmDayReminder_armId_FK")
	//@Fetch (FetchMode.SELECT)
	//@Basic(fetch = FetchType.LAZY)
	private Set<ArmDayReminder> armday=new HashSet<ArmDayReminder>();

	/**
	 * Instantiates a new arm.
	 */
	public Arm() {
	}

	/**
	 * Instantiates a new arm.
	 *
	 * @param armId the arm id
	 * @param armName the arm name
	 * @param description the description
	 */
	public Arm(short armId, String armName, String description) {
		this.armId = armId;
		this.armName = armName;
		this.description = description;
	}

	/**
	 * Gets the arm id.
	 *
	 * @return the arm id
	 */
	public short getArmId() {
		return armId;
	}

	/**
	 * Sets the arm id.
	 *
	 * @param armId the new arm id
	 */
	public void setArmId(short armId) {
		this.armId = armId;
	}

	/**
	 * Gets the arm name.
	 *
	 * @return the arm name
	 */
	public String getArmName() {
		return armName;
	}

	/**
	 * Sets the arm name.
	 *
	 * @param armName the new arm name
	 */
	public void setArmName(String armName) {
		this.armName = armName;
	}

	/**
	 * Gets the reminder days sequence.
	 *
	 * @return the reminder days sequence
	 */
	public String getReminderDaysSequence() {
		return reminderDaysSequence;
	}

	/**
	 * Sets the reminder days sequence.
	 *
	 * @param reminderDaysSequence the new reminder days sequence
	 */
	public void setReminderDaysSequence(String reminderDaysSequence) {
		this.reminderDaysSequence = reminderDaysSequence;
	}

	/**
	 * Gets the send sms.
	 *
	 * @return the send sms
	 */
	public Boolean getSendSms() {
		return sendSms;
	}

	/**
	 * Sets the send sms.
	 *
	 * @param sendSms the new send sms
	 */
	public void setSendSms(Boolean sendSms) {
		this.sendSms = sendSms;
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
	
	/**
	 * Gets the armday.
	 *
	 * @return the armday
	 */
	public Set<ArmDayReminder> getArmday() {
		return armday;
	}

	/**
	 * Sets the armday.
	 *
	 * @param armday the new armday
	 */
	public void setArmday(Set<ArmDayReminder> armday) {
		this.armday = armday;
	}
	
}