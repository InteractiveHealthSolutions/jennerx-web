package org.ird.unfepi.model;

import java.sql.Time;
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
@Table (name = "reminder")
public class Reminder implements java.io.Serializable {

	public enum ReminderType{
		NEXT_VACCINATION_REMINDER,
		LOTTERY_WON_REMINDER,
		LOTTERY_CONSUMED_REMINDER
	}
	
	private static final long	serialVersionUID	= -5225086305187569559L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short reminderId;
	
	@Column(length = 30, unique = true, nullable = false)
	private String remindername;
	
	private short gapEventDay;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private ReminderType reminderType;
	
	private Time defaultReminderTime;
	
	private Boolean isDefaultTimeEditable;
	
	private String description;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "reminder_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "reminder_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	@ElementCollection
	@CollectionTable(name = "reminder_text", joinColumns = @JoinColumn(name = "reminderId"))
	@Column(length = 2000)
	private Set<String> reminderText = new HashSet<String>();

	public Reminder() {
	}
	
	public Reminder(short reminderId) {
		this.reminderId = reminderId;
	}
	
	public short getReminderId() {
		return reminderId;
	}

	public void setReminderId(short reminderId) {
		this.reminderId = reminderId;
	}

	public String getRemindername() {
		return remindername;
	}

	public void setRemindername(String remindername) {
		this.remindername = remindername;
	}

	public short getGapEventDay () {
		return gapEventDay;
	}

	public void setGapEventDay (short gapEventDay) {
		this.gapEventDay = gapEventDay;
	}

	public ReminderType getReminderType () {
		return reminderType;
	}

	public void setReminderType (ReminderType reminderType) {
		this.reminderType = reminderType;
	}

	public Time getDefaultReminderTime () {
		return defaultReminderTime;
	}

	public void setDefaultReminderTime (Time defaultReminderTime) {
		this.defaultReminderTime = defaultReminderTime;
	}

	public Boolean getIsDefaultTimeEditable () {
		return isDefaultTimeEditable;
	}

	public void setIsDefaultTimeEditable (Boolean isDefaultTimeEditable) {
		this.isDefaultTimeEditable = isDefaultTimeEditable;
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

	public Set<String> getReminderText() {
		return reminderText;
	}

	public void setReminderText(Set<String> reminderText) {
		this.reminderText = reminderText;
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
