package org.ird.unfepi.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
@Table(name = "notifier")
public class Notifier {

	public enum NotifierType{
		EMAIL_CSV,
		EMAIL_PDF,
		EMAIL_SIMPLE,
		SMS,
		MMS
	}
	
	public enum NotifierStatus{
		ACTIVE,
		SUSPENDED,
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int notifierId;
	
	@Column(nullable = false, unique = true)
	private String notifierName;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private NotifierType notifierType;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private NotifierStatus notifierStatus;
	
	private String notifierQuery;

	private String queryDescription;
	
	private String notifierHeaderQuery;

	private String notifierFooterQuery;
	
	private String notifierSubject;
	
	private String columnsHeaderList;
	
	private String notifierMessage;
	
	@Column(nullable = false)
	private String notifierCron;
	
	@ElementCollection
	@CollectionTable(name = "notifier_recipient", joinColumns = @JoinColumn(name = "notifierId"))
	@Column(length = 60)
	private Set<String> notifierRecipient = new HashSet<String>();
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "notifier_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "notifier_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public Notifier() {
		
	}
	
	public int getNotifierId() {
		return notifierId;
	}

	public void setNotifierId(int notifierId) {
		this.notifierId = notifierId;
	}

	public String getNotifierName() {
		return notifierName;
	}

	public void setNotifierName(String notifierName) {
		this.notifierName = notifierName;
	}

	public NotifierType getNotifierType() {
		return notifierType;
	}

	public void setNotifierType(NotifierType notifierType) {
		this.notifierType = notifierType;
	}

	public NotifierStatus getNotifierStatus() {
		return notifierStatus;
	}

	public void setNotifierStatus(NotifierStatus notifierStatus) {
		this.notifierStatus = notifierStatus;
	}

	public String getNotifierQuery() {
		return notifierQuery;
	}

	public void setNotifierQuery(String notifierQuery) {
		this.notifierQuery = notifierQuery;
	}

	public String getQueryDescription() {
		return queryDescription;
	}

	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}

	public String getNotifierHeaderQuery() {
		return notifierHeaderQuery;
	}

	public void setNotifierHeaderQuery(String notifierHeaderQuery) {
		this.notifierHeaderQuery = notifierHeaderQuery;
	}

	public String getNotifierFooterQuery() {
		return notifierFooterQuery;
	}

	public void setNotifierFooterQuery(String notifierFooterQuery) {
		this.notifierFooterQuery = notifierFooterQuery;
	}

	public String getNotifierSubject() {
		return notifierSubject;
	}

	public void setNotifierSubject(String notifierSubject) {
		this.notifierSubject = notifierSubject;
	}

	public String getColumnsHeaderList() {
		return columnsHeaderList;
	}

	public void setColumnsHeaderList(String columnsHeaderList) {
		this.columnsHeaderList = columnsHeaderList;
	}

	public String getNotifierMessage() {
		return notifierMessage;
	}

	public void setNotifierMessage(String notifierMessage) {
		this.notifierMessage = notifierMessage;
	}

	public String getNotifierCron() {
		return notifierCron;
	}

	public void setNotifierCron(String notifierCron) {
		this.notifierCron = notifierCron;
	}

	public Set<String> getNotifierRecipient() {
		return notifierRecipient;
	}

	public void setNotifierRecipient(Set<String> notifierRecipient) {
		this.notifierRecipient = notifierRecipient;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}
	
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
}
