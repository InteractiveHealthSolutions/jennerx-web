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
@Table (name = "vaccinatorincentiveparams")
public class VaccinatorIncentiveParams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALL INT NOT NULL AUTO_INCREMENT")*/
	private short vaccinatorIncentiveParamsId;
	
	private Integer criteriaRangeMin;
	
	private Integer criteriaRangeMax;
	
	private Float denomination;
	
	private Float probability;
	
	private Float commission;
	
	private String description;

	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vaccinatorincentiveparams_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	public VaccinatorIncentiveParams() {
		// TODO Auto-generated constructor stub
	}

	public short getVaccinatorIncentiveParamsId() {
		return vaccinatorIncentiveParamsId;
	}

	public void setVaccinatorIncentiveParamsId(short vaccinatorIncentiveParamsId) {
		this.vaccinatorIncentiveParamsId = vaccinatorIncentiveParamsId;
	}

	public Integer getCriteriaRangeMin() {
		return criteriaRangeMin;
	}

	public void setCriteriaRangeMin(Integer criteriaRangeMin) {
		this.criteriaRangeMin = criteriaRangeMin;
	}

	public Integer getCriteriaRangeMax() {
		return criteriaRangeMax;
	}

	public void setCriteriaRangeMax(Integer criteriaRangeMax) {
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

	public Float getCommission() {
		return commission;
	}

	public void setCommission(Float commission) {
		this.commission = commission;
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
}
