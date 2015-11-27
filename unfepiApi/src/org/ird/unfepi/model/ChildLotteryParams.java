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
@Table (name = "childlotteryparams")
public class ChildLotteryParams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALL INT NOT NULL AUTO_INCREMENT")*/
	private short childLotteryParamsId;
	
	private Float criteriaRangeMin;
	
	private Float criteriaRangeMax;
	
	private Float denomination;
	
	private Float probability;
	
	private Short enrollmentVaccineId;

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "enrollmentVaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "childlotteryparams_enrollmentVaccineId_vaccine_vaccineId_FK")
	private Vaccine	enrollmentVaccine;
	
	private Short recievedVaccineId;

	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "enrollmentVaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "childlotteryparams_recievedVaccineId_vaccine_vaccineId_FK")
	private Vaccine	recievedVaccine;
	
	private String description;

	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "childlotteryparams_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	public ChildLotteryParams() {
		
	}


	public short getChildLotteryParamsId() {
		return childLotteryParamsId;
	}


	public void setChildLotteryParamsId(short childLotteryParamsId) {
		this.childLotteryParamsId = childLotteryParamsId;
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

	public Float getDenomination() {
		return denomination;
	}


	public void setDenomination(Float denomination) {
		this.denomination = denomination;
	}


	public Float getProbability() {
		return probability;
	}


	public void setProbability(Float probability) {
		this.probability = probability;
	}


	public Short getEnrollmentVaccineId() {
		return enrollmentVaccineId;
	}


	public void setEnrollmentVaccineId(Short enrollmentVaccineId) {
		this.enrollmentVaccineId = enrollmentVaccineId;
	}


	public Vaccine getEnrollmentVaccine() {
		return enrollmentVaccine;
	}


	public void setEnrollmentVaccine(Vaccine enrollmentVaccine) {
		this.enrollmentVaccine = enrollmentVaccine;
	}


	public Short getRecievedVaccineId() {
		return recievedVaccineId;
	}


	public void setRecievedVaccineId(Short recievedVaccineId) {
		this.recievedVaccineId = recievedVaccineId;
	}


	public Vaccine getRecievedVaccine() {
		return recievedVaccine;
	}


	public void setRecievedVaccine(Vaccine recievedVaccine) {
		this.recievedVaccine = recievedVaccine;
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

	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
}
