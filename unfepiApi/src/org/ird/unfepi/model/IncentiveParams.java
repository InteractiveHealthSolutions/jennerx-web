package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Entity;
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
@Table (name = "incentiveparams")

public class IncentiveParams {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short incentiveParamsId;
	
	private Float criteriaRangeMin;
	
	private Float criteriaRangeMax;
	
	private Float amount;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "incentiveparams_vaccineId_vaccine_vaccineId_FK")
	private Vaccine	vaccine;
	
	/*@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccine_id", insertable = false, updatable = false)
	@ForeignKey(name = "incentiveparams_vaccineId_vaccine_vaccineId_FK")*/
	private Short vaccineId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "armId", insertable = false, updatable = false)
	@ForeignKey(name = "incentiveparams_armId_arm_armId_FK")
	private Arm arm;

/*	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "arm_id")
	@ForeignKey(name = "incentiveparams_armId_arm_armId_FK")*/
	private int armId;
	
	private Short roleId;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "incentiveparams_createdByUserId_user_mappedId_FK")
	private User createdByUserId;
	
	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	
	
	public short getIncentiveParamsId() {
		return incentiveParamsId;
	}

	public void setIncentiveParamsId(short incentiveParamsId) {
		this.incentiveParamsId = incentiveParamsId;
	}

	public Float getCriteriaRangeMin() {
		return criteriaRangeMin;
	}

	public void setCriteriaRangeMin(Float criteriaRangeMin) {
		this.criteriaRangeMin = criteriaRangeMin;
	}

	public Float getCriteriaRangeMax() {
		return criteriaRangeMax;
	}

	public void setCriteriaRangeMax(Float criteriaRangeMax) {
		this.criteriaRangeMax = criteriaRangeMax;
	}

	public short getRole() {
		return roleId;
	}

	public void setRole(short roleId) {
		this.roleId = roleId;
	}
	
	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}
	
	public short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public Arm getArm() {
		return arm;
	}

	public void setArmId(Arm arm) {
		this.arm = arm;
	}

	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}

	public User getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
}
