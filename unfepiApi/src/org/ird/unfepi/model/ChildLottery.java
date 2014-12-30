 
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

@Entity
@Table(name = "childlottery")
public class ChildLottery {

	public enum CodeStatus {
		AVAILABLE, CONSUMED, EXPIRED, LOTTERY_LOST
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int childLotteryId;
	
	@Column(unique = true, nullable = false)
	private Integer vaccinationRecordNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinationRecordNum", insertable = false, updatable = false)
	@ForeignKey(name = "clott_vaccRecordNum_vaccination_vaccinationRecordNum_FK")
	private Vaccination vaccination;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lotteryDate;
	
	private Boolean hasWonLottery;
	
	private String code;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable = false)
	private CodeStatus codeStatus;

	private Float amount;
	
	private Integer storekeeperId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storekeeperId", insertable = false, updatable = false)
	@ForeignKey(name = "clott_storekeeperId_storekeeper_mappedId_FK")
	private Storekeeper storekeeper;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	transactionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	consumptionDate;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "clott_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "clott_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public ChildLottery() {
		
	}

	public int getChildLotteryId () {
		return childLotteryId;
	}

	public void setChildLotteryId (int childLotteryId) {
		this.childLotteryId = childLotteryId;
	}

	public Integer getVaccinationRecordNum () {
		return vaccinationRecordNum;
	}


	public void setVaccinationRecordNum (Integer vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
	}


	public Vaccination getVaccination () {
		return vaccination;
	}


	void setVaccination (Vaccination vaccination) {
		this.vaccination = vaccination;
	}


	public Date getLotteryDate () {
		return lotteryDate;
	}


	public void setLotteryDate (Date lotteryDate) {
		this.lotteryDate = lotteryDate;
	}


	public Boolean getHasWonLottery () {
		return hasWonLottery;
	}


	public void setHasWonLottery (Boolean hasWonLottery) {
		this.hasWonLottery = hasWonLottery;
	}


	public String getCode () {
		return code;
	}


	public void setCode (String code) {
		this.code = code;
	}


	public CodeStatus getCodeStatus () {
		return codeStatus;
	}


	public void setCodeStatus (CodeStatus codeStatus) {
		this.codeStatus = codeStatus;
	}


	public Float getAmount () {
		return amount;
	}


	public void setAmount (Float amount) {
		this.amount = amount;
	}


	public Integer getStorekeeperId () {
		return storekeeperId;
	}

	public void setStorekeeperId (Integer storekeeperId) {
		this.storekeeperId = storekeeperId;
	}

	public Storekeeper getStorekeeper () {
		return storekeeper;
	}

	void setStorekeeper (Storekeeper storekeeper) {
		this.storekeeper = storekeeper;
	}

	public Date getTransactionDate () {
		return transactionDate;
	}

	public void setTransactionDate (Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getConsumptionDate () {
		return consumptionDate;
	}

	public void setConsumptionDate (Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public String getDescription () {
		return description;
	}


	public void setDescription (String description) {
		this.description = description;
	}


	public User getCreatedByUserId () {
		return createdByUserId;
	}


	public void setCreatedByUserId (User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}


	public Date getCreatedDate () {
		return createdDate;
	}


	public void setCreatedDate (Date createdDate) {
		this.createdDate = createdDate;
	}


	public User getLastEditedByUserId () {
		return lastEditedByUserId;
	}


	public void setLastEditedByUserId (User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}


	public Date getLastEditedDate () {
		return lastEditedDate;
	}


	public void setLastEditedDate (Date lastEditedDate) {
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
