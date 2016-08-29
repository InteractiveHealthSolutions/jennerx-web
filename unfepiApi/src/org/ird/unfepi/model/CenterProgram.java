package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
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

/**
 * 
 * @author Hera Rafique
 *
 */
@Entity
@Table(name = "centerprogram")
public class CenterProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer centerProgramId;

	private Integer vaccinationCenterId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCenter.class)
	@JoinColumn(name = "vaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "centerprogram_vaccinationCenterId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter vaccinationCenter;

	private Integer healthProgramId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = HealthProgram.class)
	@JoinColumn(name = "healthProgramId", insertable = false, updatable = false)
	@ForeignKey(name = "centerprogram_healthProgramId_healthprogram_programId_FK")
	private HealthProgram healthProgram;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(nullable = false)
	private Boolean isActive;

	@Temporal(TemporalType.TIMESTAMP)
	private Date terminateDate;

	private String terminateReason;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "centerprogram_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "centerprogram_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public Integer getCenterProgramId() {
		return centerProgramId;
	}

	public void setCenterProgramId(Integer centerProgramId) {
		this.centerProgramId = centerProgramId;
	}

	public Integer getVaccinationCenterId() {
		return vaccinationCenterId;
	}

	public void setVaccinationCenterId(Integer vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}

	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public Integer getHealthProgramId() {
		return healthProgramId;
	}

	public void setHealthProgramId(Integer healthProgramId) {
		this.healthProgramId = healthProgramId;
	}

	public HealthProgram getHealthProgram() {
		return healthProgram;
	}

	public void setHealthProgram(HealthProgram healthProgram) {
		this.healthProgram = healthProgram;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getTerminateDate() {
		return terminateDate;
	}

	public void setTerminateDate(Date terminateDate) {
		this.terminateDate = terminateDate;
	}

	public String getTerminateReason() {
		return terminateReason;
	}

	public void setTerminateReason(String terminateReason) {
		this.terminateReason = terminateReason;
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
