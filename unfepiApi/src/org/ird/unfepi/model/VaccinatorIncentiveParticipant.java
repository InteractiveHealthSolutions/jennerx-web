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
@Table (name = "vaccinatorincentiveparticipant")
public class VaccinatorIncentiveParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int serialNumber;
	
	private Integer vaccinatorIncentiveEventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorIncentiveEventId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinatorincentiveparticipant_vaccinatorIncentiveEventId_vaccinatorincentiveevent_vaccinatorIncentiveEventId_FK")
	private VaccinatorIncentiveEvent vaccinatorIncentiveEvent;
	
	private Integer vaccinatorId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Vaccinator.class)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinatorincentiveparticipant_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator vaccinator;
	
	private Short vaccinatorIncentiveParamsId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorIncentiveParamsId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinatorincentiveparticipant_vaccinatorIncentiveParamsId_vaccinatorincentiveparams_vaccinatorIncentiveParamsId_FK")

	private VaccinatorIncentiveParams vaccinatorIncentiveParams;
	
	private Boolean isIncentivised;
	
	private Integer criteriaElementValue;
	
	private String description;
	
	public VaccinatorIncentiveParticipant() {
		// TODO Auto-generated constructor stub
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
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

	public Short getVaccinatorIncentiveParamsId() {
		return vaccinatorIncentiveParamsId;
	}

	public void setVaccinatorIncentiveParamsId(Short vaccinatorIncentiveParamsId) {
		this.vaccinatorIncentiveParamsId = vaccinatorIncentiveParamsId;
	}

	public VaccinatorIncentiveParams getVaccinatorIncentiveParams() {
		return vaccinatorIncentiveParams;
	}

	void setVaccinatorIncentiveParams(VaccinatorIncentiveParams vaccinatorIncentiveParams) {
		this.vaccinatorIncentiveParams = vaccinatorIncentiveParams;
	}

	public Boolean getIsIncentivised() {
		return isIncentivised;
	}

	public void setIsIncentivised(Boolean isIncentivised) {
		this.isIncentivised = isIncentivised;
	}

	public Integer getCriteriaElementValue() {
		return criteriaElementValue;
	}

	public void setCriteriaElementValue(Integer criteriaElementValue) {
		this.criteriaElementValue = criteriaElementValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
