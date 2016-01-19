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

/**
 * The Class ReminderSms.
 */
@Entity
@Table (name = "remindersms")
public class ReminderSms implements java.io.Serializable {

	public enum REMINDER_STATUS{
		
		SCHEDULED,
		LOGGED,
		SENT,
		FAILED,
		MISSED,
		CANCELLED,
		OPTED_OUT,
		NA;
	}
	
	/** The rsms record num. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rsmsRecordNum;
	
	@Column(nullable = false)
	private Short reminderId;
	
	/** The reminder. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reminderId", insertable = false, updatable = false)
	@ForeignKey(name = "remindersms_reminderId_reminder_reminderId_FK")
	private Reminder reminder;
	
	/** The text. */
	@Column(length = 2000)
	private String text;
	
	/** The cellnumber. */
	@Column(length = 20)
	private String originator;
	
	/** The cellnumber. */
	@Column(length = 20)
	private String recipient;
	
	/** The due date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	/** The day number. */
	private short dayNumber;
	
	/** The sent date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;
	
	/** The vaccine. *//*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccineId")
	@ForeignKey(name = "remindersms_vaccineId_vaccine_vaccineId_FK")
	private Vaccine vaccine;*/

	/** The vaccination record num. */
	@Column(length = 20)
	private Integer vaccinationRecordNum;
	
	/** The guardian relation. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinationRecordNum", insertable = false, updatable = false)
	@ForeignKey(name = "remindersms_vaccinationRecordNum_vaccination_vaccinationRecordNum_FK")
	private Vaccination vaccination;
	
	/** The sms cancel reason. */
	private String smsCancelReason;
	
	private String smsCancelReasonOther;
	
	/** The reminder status. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private REMINDER_STATUS reminderStatus;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "remindersms_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "remindersms_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	private String referenceNumber;
	
	/** The hours difference. */
	private Integer hoursDifference;
	
	/**
	 * Instantiates a new reminder sms.
	 */
	public ReminderSms() {
	}


	/**
	 * Gets the rsms record num.
	 *
	 * @return the rsms record num
	 */
	public int getRsmsRecordNum() {
		return rsmsRecordNum;
	}


	/**
	 * Sets the rsms record num.
	 *
	 * @param rsmsRecordNum the new rsms record num
	 */
	public void setRsmsRecordNum(int rsmsRecordNum) {
		this.rsmsRecordNum = rsmsRecordNum;
	}


	public String getOriginator() {
		return originator;
	}


	public void setOriginator(String originator) {
		this.originator = originator;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	/**
	 * Gets the reminder.
	 *
	 * @return the reminder
	 */
	public Reminder getReminder() {
		return reminder;
	}


	/**
	 * Sets the reminder.
	 *
	 * @param reminder the new reminder
	 */
	void setReminder(Reminder reminder) {
		this.reminder = reminder;
	}


	public Short getReminderId() {
		return reminderId;
	}


	public void setReminderId(Short reminderId) {
		this.reminderId = reminderId;
	}


	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the due date.
	 *
	 * @return the due date
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the due date.
	 *
	 * @param dueDate the new due date
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Gets the day number.
	 *
	 * @return the day number
	 */
	public short getDayNumber() {
		return dayNumber;
	}

	/**
	 * Sets the day number.
	 *
	 * @param dayNumber the new day number
	 */
	public void setDayNumber(short dayNumber) {
		this.dayNumber = dayNumber;
	}


	/**
	 * Gets the sent date.
	 *
	 * @return the sent date
	 */
	public Date getSentDate() {
		return sentDate;
	}


	/**
	 * Sets the sent date.
	 *
	 * @param sentDate the new sent date
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}


	/**
	 * Gets the vaccine.
	 *
	 * @return the vaccine
	 *//*
	public Vaccine getVaccine() {
		return vaccine;
	}


	*//**
	 * Sets the vaccine.
	 *
	 * @param vaccine the new vaccine
	 *//*
	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}
*/

	/**
	 * Gets the vaccination record num.
	 *
	 * @return the vaccination record num
	 */
	public Integer getVaccinationRecordNum() {
		return vaccinationRecordNum;
	}


	/**
	 * Sets the vaccination record num.
	 *
	 * @param vaccinationRecordNum the new vaccination record num
	 */
	public void setVaccinationRecordNum(Integer vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}


	public Vaccination getVaccination() {
		return vaccination;
	}


	void setVaccination(Vaccination vaccination) {
		this.vaccination = vaccination;
	}


	/**
	 * Gets the sms cancel reason.
	 *
	 * @return the sms cancel reason
	 */
	public String getSmsCancelReason() {
		return smsCancelReason;
	}


	/**
	 * Sets the sms cancel reason.
	 *
	 * @param smsCancelReason the new sms cancel reason
	 */
	public void setSmsCancelReason(String smsCancelReason) {
		this.smsCancelReason = smsCancelReason;
	}


	public String getSmsCancelReasonOther() {
		return smsCancelReasonOther;
	}


	public void setSmsCancelReasonOther(String smsCancelReasonOther) {
		this.smsCancelReasonOther = smsCancelReasonOther;
	}


	/**
	 * Gets the reminder status.
	 *
	 * @return the reminder status
	 */
	public REMINDER_STATUS getReminderStatus() {
		return reminderStatus;
	}


	/**
	 * Sets the reminder status.
	 *
	 * @param reminderStatus the new reminder status
	 */
	public void setReminderStatus(REMINDER_STATUS reminderStatus) {
		this.reminderStatus = reminderStatus;
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
	 * Gets the hours difference.
	 *
	 * @return the hours difference
	 */
	public Integer getHoursDifference() {
		return hoursDifference;
	}


	/**
	 * Sets the hours difference.
	 *
	 * @param hoursDifference the new hours difference
	 */
	public void setHoursDifference(Integer hoursDifference) {
		this.hoursDifference = hoursDifference;
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


	public String getReferenceNumber() {
		return referenceNumber;
	}


	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
}