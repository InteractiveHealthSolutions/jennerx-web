package org.ird.unfepi.model;
/*package org.ird.unfepi.model;

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
@Table(name = "financialtransactionsms")
public class FinancialTransactionSms {
	
	*//** The rsms record num. *//*
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")
	private int fsmsRecordNum;
	
	*//** The text. *//*
	@Column(length = 500)
	private String text;
	
	*//** The cellnumber. *//*
	@Column(length = 20)
	private String originator;
	
	*//** The cellnumber. *//*
	@Column(length = 20)
	private String recipient;
	
	*//** The due date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	*//** The sent date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date sentDate;
	
	*//** The sms cancel reason. *//*
	private String smsCancelReason;
	
	*//** The reminder status. *//*
	@Enumerated(EnumType.STRING)
	private SmsStatus smsStatus;
	
	*//** The description. *//*
	
	private String description;
	
	*//** The created by user id. *//*
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "CreatorUserIdFK")
	private User createdByUserId;

	*//** The created date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	*//** The last edited by user id. *//*
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "EditorUserIdFK")
	private User lastEditedByUserId;
	
	*//** The last edited date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	private String referenceNumber;
	
	*//** The hours difference. *//*
	private Integer hoursDifference;

	public FinancialTransactionSms() {
		// TODO Auto-generated constructor stub
	}
	public int getFsmsRecordNum() {
		return fsmsRecordNum;
	}

	public void setFsmsRecordNum(int fsmsRecordNum) {
		this.fsmsRecordNum = fsmsRecordNum;
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

	public SmsStatus getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(SmsStatus smsStatus) {
		this.smsStatus = smsStatus;
	}

	*//**
	 * Gets the description.
	 *
	 * @return the description
	 *//*
	public String getDescription() {
		return description;
	}

	*//**
	 * Sets the description.
	 *
	 * @param description the new description
	 *//*
	public void setDescription(String description) {
		this.description = description;
	}

	*//**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 *//*
	public User getCreatedByUserId() {
		return createdByUserId;
	}

	*//**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 *//*
	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	*//**
	 * Gets the created date.
	 *
	 * @return the created date
	 *//*
	public Date getCreatedDate() {
		return createdDate;
	}

	*//**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 *//*
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	*//**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 *//*
	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	*//**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 *//*
	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	*//**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 *//*
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	*//**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 *//*
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}


	public Integer getHoursDifference() {
		return hoursDifference;
	}

	public void setHoursDifference(Integer hoursDifference) {
		this.hoursDifference = hoursDifference;
	}

	*//**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 *//*
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	*//**
	 * Sets the editor.
	 *
	 * @param editor the new editor
	 *//*
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
*/