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
import org.ird.unfepi.model.Model.SmsStatus;

@Entity
@Table(name = "usersms")
public class UserSms {
	public enum SmsType{
		VACCINE_SCHEDULE_INQUIRY,
		N_A
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int usmsRecordNum;
	
	/** The text. */
	@Column(length = 2000)
	private String text;
	
	@Column(length = 20)
	private String originator;
	
	@Column(length = 20)
	private String recipient;
	
	private Integer recipientId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "recipientId", insertable = false, updatable = false)
	@ForeignKey(name = "usersms_recipientId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	/** The due date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	/** The sent date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;
	
	/** The sms cancel reason. */
	private String smsCancelReason;
	
	private String smsCancelReasonOther;

	/** The reminder status. */
	@Enumerated(EnumType.STRING)
	@Column(length = 120)
	private SmsStatus smsStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 120)
	private SmsType smsType;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "usersms_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "usersms_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	private String referenceNumber;
	
	public UserSms() {
		
	}
	public int getUsmsRecordNum() {
		return usmsRecordNum;
	}

	public void setUsmsRecordNum(int usmsRecordNum) {
		this.usmsRecordNum = usmsRecordNum;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public Integer getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}
	public IdMapper getIdMapper() {
		return idMapper;
	}
	void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getSmsCancelReason() {
		return smsCancelReason;
	}

	public void setSmsCancelReason(String smsCancelReason) {
		this.smsCancelReason = smsCancelReason;
	}

	public String getSmsCancelReasonOther() {
		return smsCancelReasonOther;
	}
	public void setSmsCancelReasonOther(String smsCancelReasonOther) {
		this.smsCancelReasonOther = smsCancelReasonOther;
	}
	public SmsStatus getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(SmsStatus smsStatus) {
		this.smsStatus = smsStatus;
	}

	public SmsType getSmsType() {
		return smsType;
	}
	public void setSmsType(SmsType smsType) {
		this.smsType = smsType;
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
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
}
