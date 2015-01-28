package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "storekeeperincentiveworkprogress")
public class StorekeeperIncentiveWorkProgress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int serialNumber;
	
	private Integer storekeeperIncentiveParticipantId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storekeeperIncentiveParticipantId", insertable = false, updatable = false)
	@ForeignKey(name = "storekeeperincentiveworkprogress_storekeeperIncentiveParticipantId_storekeeperincentiveparticipant_serialNumber_FK")
	private StorekeeperIncentiveParticipant storekeeperIncentiveParticipant;
	
	private Integer transactions;
	
	private Float totalTransactionsAmount;
	
	public StorekeeperIncentiveWorkProgress() {
		
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getStorekeeperIncentiveParticipantId() {
		return storekeeperIncentiveParticipantId;
	}

	public void setStorekeeperIncentiveParticipantId(
			Integer StorekeeperIncentiveParticipantId) {
		this.storekeeperIncentiveParticipantId = StorekeeperIncentiveParticipantId;
	}

	public StorekeeperIncentiveParticipant getStorekeeperIncentiveParticipant() {
		return storekeeperIncentiveParticipant;
	}

	void setStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant StorekeeperIncentiveParticipant) {
		this.storekeeperIncentiveParticipant = StorekeeperIncentiveParticipant;
	}

	public Integer getTransactions() {
		return transactions;
	}

	public void setTransactions(Integer transactions) {
		this.transactions = transactions;
	}

	public Float getTotalTransactionsAmount() {
		return totalTransactionsAmount;
	}

	public void setTotalTransactionsAmount(Float totalTransactionsAmount) {
		this.totalTransactionsAmount = totalTransactionsAmount;
	}

}
