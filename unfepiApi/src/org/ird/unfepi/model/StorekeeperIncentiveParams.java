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
@Table (name = "storekeeperincentiveparams")
public class StorekeeperIncentiveParams {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALL INT NOT NULL AUTO_INCREMENT")*/
	private short storekeeperIncentiveParamsId;
	
	private Integer criteriaRangeMin;
	
	private Integer criteriaRangeMax;
	
	private Float commission;
	
	private Float easypaisaCharges;
	
	private String description;

	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "storekeeperincentiveparams_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	public StorekeeperIncentiveParams() {
		// TODO Auto-generated constructor stub
	}

	public short getStorekeeperIncentiveParamsId() {
		return storekeeperIncentiveParamsId;
	}

	public void setStorekeeperIncentiveParamsId(short storekeeperIncentiveParamsId) {
		this.storekeeperIncentiveParamsId = storekeeperIncentiveParamsId;
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

	public Float getCommission() {
		return commission;
	}

	public void setCommission(Float commission) {
		this.commission = commission;
	}

	public Float getEasypaisaCharges() {
		return easypaisaCharges;
	}

	public void setEasypaisaCharges(Float easypaisaCharges) {
		this.easypaisaCharges = easypaisaCharges;
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
