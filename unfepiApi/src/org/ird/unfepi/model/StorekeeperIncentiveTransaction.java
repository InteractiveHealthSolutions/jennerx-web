package org.ird.unfepi.model;

import java.util.Date;

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
@Table(name = "storekeeperincentivetransaction")
public class StorekeeperIncentiveTransaction {
	
	public enum TranscationStatus{
		DUE,
		PAID,
		CANCELLED,
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int storekeeperIncentiveTransactionId;
	
	private Integer storekeeperIncentiveEventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storekeeperIncentiveEventId", insertable = false, updatable = false)
	@ForeignKey(name = "storekeeperincentivetransaction_storekeeperIncentiveEventId_storekeeperincentiveevent_storekeeperIncentiveEventId_FK")
	private StorekeeperIncentiveEvent storekeeperIncentiveEvent;
	
	private Integer storekeeperId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Storekeeper.class)
	@JoinColumn(name = "storekeeperId", insertable = false, updatable = false)
	@ForeignKey(name = "storekeeperincentivetransaction_storekeeperId_storekeeper_mappedId_FK")
	private Storekeeper storekeeper;
	
	@Enumerated(EnumType.STRING)
	private TranscationStatus transactionStatus;
	
	private Float amountDue;
	
	private Float amountPaid;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date paidDate;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "storekeeperincentivetransaction_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "storekeeperincentivetransaction_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public StorekeeperIncentiveTransaction() {
		// TODO Auto-generated constructor stub
	}

	public int getStorekeeperIncentiveTransactionId() {
		return storekeeperIncentiveTransactionId;
	}

	public void setStorekeeperIncentiveTransactionId(int storekeeperIncentiveTransactionId) {
		this.storekeeperIncentiveTransactionId = storekeeperIncentiveTransactionId;
	}

	public Integer getStorekeeperIncentiveEventId() {
		return storekeeperIncentiveEventId;
	}

	public void setStorekeeperIncentiveEventId(Integer storekeeperIncentiveEventId) {
		this.storekeeperIncentiveEventId = storekeeperIncentiveEventId;
	}

	public StorekeeperIncentiveEvent getStorekeeperIncentiveEvent() {
		return storekeeperIncentiveEvent;
	}

	void setStorekeeperIncentiveEvent(StorekeeperIncentiveEvent storekeeperIncentiveEvent) {
		this.storekeeperIncentiveEvent = storekeeperIncentiveEvent;
	}

	public Integer getStorekeeperId() {
		return storekeeperId;
	}

	public void setStorekeeperId(Integer storekeeperId) {
		this.storekeeperId = storekeeperId;
	}

	public Storekeeper getStorekeeper() {
		return storekeeper;
	}

	void setStorekeeper(Storekeeper storekeeper) {
		this.storekeeper = storekeeper;
	}

	public TranscationStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TranscationStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Float getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(Float amountDue) {
		this.amountDue = amountDue;
	}

	public Float getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Float amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
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
}
