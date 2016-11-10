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
@Table(name = "vaccinatorincentive")
public class VaccinatorIncentive {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int vaccinatorIncentiveId;
	
	private Integer vaccinatorId;
	
	/** The vaccinator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "vincent_vaccinatorId_vaccinator_vaccinatorId_FK")
	private Vaccinator	vaccinator;

	private Integer armId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "armId", insertable = false, updatable = false)
	@ForeignKey(name = "vincent_armId_arm_armId_FK")
	private Arm arm;
	
	private Short incentiveParamId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incentiveParamId", insertable = false, updatable = false)
	@ForeignKey(name = "vincent_incentiveParamId_incentiveparam_incentiveParamId_FK")
	private IncentiveParams incentiveParams;
	
	@Column(unique = true, nullable = false)
	private Integer vaccinationRecordNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinationRecordNum", insertable = false, updatable = false)
	@ForeignKey(name = "vincent_vaccRecordNum_vaccination_vaccinationRecordNum_FK")
	private Vaccination vaccination;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date incentiveDate;
	
	private Boolean isIncentivized;
	
	private String code;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30, nullable = false)
	private IncentiveStatus incentiveStatus;
	
	private Float amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date	consumptionDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date	transactionDate;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vincent_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	
	public VaccinatorIncentive() {
		
	}

	public int getVaccinatorIncentiveId () {
		return vaccinatorIncentiveId;
	}

	public void setVaccinatorIncentiveId(int vaccinatorIncentiveId) {
		this.vaccinatorIncentiveId = vaccinatorIncentiveId;
	}
	
	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	public void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
	}
	
	public Integer getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}
	
	public Arm getArm() {
		return arm;
	}

	public void setArm(Arm arm) {
		this.arm = arm;
	}

	public Short getIncentiveParamId() {
		return incentiveParamId;
	}

	public void setIncentiveParamId(Short incentiveParamId) {
		this.incentiveParamId = incentiveParamId;
	}

	public IncentiveParams getIncentiveParams() {
		return incentiveParams;
	}

	public void setIncentiveParams(IncentiveParams incentiveParams) {
		this.incentiveParams = incentiveParams;
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


	public Date getIncentiveDate () {
		return incentiveDate;
	}


	public void setIncentiveDate (Date incentiveDate) {
		this.incentiveDate = incentiveDate;
	}


	public Boolean getIsIncentivized () {
		return isIncentivized;
	}


	public void setIsIncentivized (Boolean isIncentivized) {
		this.isIncentivized = isIncentivized;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public IncentiveStatus getIncentiveStatus() {
		return incentiveStatus;
	}

	public void setIncentiveStatus(IncentiveStatus incentiveStatus) {
		this.incentiveStatus = incentiveStatus;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setArmId(Integer armId) {
		this.armId = armId;
	}

	public Float getAmount () {
		return amount;
	}


	public void setAmount (Float amount) {
		this.amount = amount;
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

	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}

	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}

}
