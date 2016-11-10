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
@Table(name = "vaccinatorincentivetransaction")
public class VaccinatorIncentiveTransaction {
	
	public enum TranscationStatus{
		DUE,
		PAID,
		CANCELLED,
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int vaccinatorIncentiveTransactionId;
	
	private Integer vaccinatorIncentiveEventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorIncentiveEventId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccincenttrans_vaccIncentEvId_vaccincentev_vaccIncentEvId_FK")
	private VaccinatorIncentiveEvent vaccinatorIncentiveEvent;
	
	private Integer vaccinatorId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Vaccinator.class)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccincenttrans_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator vaccinator;
	
	@Enumerated(EnumType.STRING)
	private TranscationStatus transactionStatus;
	
	private Float amountDue;
	
	private Float amountPaid;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date paidDate;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vaccincenttrans_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "vaccincenttrans_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public VaccinatorIncentiveTransaction() {
		
	}

	public int getVaccinatorIncentiveTransactionId() {
		return vaccinatorIncentiveTransactionId;
	}

	public void setVaccinatorIncentiveTransactionId(int vaccinatorIncentiveTransactionId) {
		this.vaccinatorIncentiveTransactionId = vaccinatorIncentiveTransactionId;
	}

	public Integer getVaccinatorIncentiveEventId() {
		return vaccinatorIncentiveEventId;
	}

	public void setVaccinatorIncentiveEventId(Integer vaccinatorIncentiveEventId) {
		this.vaccinatorIncentiveEventId = vaccinatorIncentiveEventId;
	}

	public VaccinatorIncentiveEvent getVaccinatorIncentiveEvent() {
		return vaccinatorIncentiveEvent;
	}

	void setVaccinatorIncentiveEvent(VaccinatorIncentiveEvent vaccinatorIncentiveEvent) {
		this.vaccinatorIncentiveEvent = vaccinatorIncentiveEvent;
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}

	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}

	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
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
