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
@Table(name = "round")
public class Round {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roundId;

	@Column(length = 30, unique = true, nullable = false)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

/*	private Integer centerProgramId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CenterProgram.class)
	@JoinColumn(name = "centerProgramId", insertable = false, updatable = false)
	@ForeignKey(name = "round_centerProgramId_centerprogram_centerProgramId_FK")
	private CenterProgram centerProgram;*/
	
	private Integer healthProgramId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = HealthProgram.class)
	@JoinColumn(name = "healthProgramId", insertable = false, updatable = false)
	@ForeignKey(name = "centerprogram_healthProgramId_healthprogram_programId_FK")
	private HealthProgram healthProgram;
	
	@Column(nullable = false)
	private Boolean isActive;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "round_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "round_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	/*public Integer getCenterProgramId() {
		return centerProgramId;
	}

	public void setCenterProgramId(Integer centerProgramId) {
		this.centerProgramId = centerProgramId;
	}

	public CenterProgram getCenterProgram() {
		return centerProgram;
	}

	public void setCenterProgram(CenterProgram centerProgram) {
		this.centerProgram = centerProgram;
	}*/

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
