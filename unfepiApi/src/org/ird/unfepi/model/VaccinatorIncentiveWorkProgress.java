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
@Table(name = "vaccinatorincentiveworkprogress")
public class VaccinatorIncentiveWorkProgress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int serialNumber;
	
	private Integer vaccinatorIncentiveParticipantId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorIncentiveParticipantId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccincentworkpro_vaccIncentPartiId_vaccincentparti_slNum_FK")
	private VaccinatorIncentiveParticipant vaccinatorIncentiveParticipant;
	
	private String workType;
	
	private String workValue;
	
	public VaccinatorIncentiveWorkProgress() {
		
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getVaccinatorIncentiveParticipantId() {
		return vaccinatorIncentiveParticipantId;
	}

	public void setVaccinatorIncentiveParticipantId(
			Integer VaccinatorIncentiveParticipantId) {
		this.vaccinatorIncentiveParticipantId = VaccinatorIncentiveParticipantId;
	}

	public VaccinatorIncentiveParticipant getVaccinatorIncentiveParticipant() {
		return vaccinatorIncentiveParticipant;
	}

	void setVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant VaccinatorIncentiveParticipant) {
		this.vaccinatorIncentiveParticipant = VaccinatorIncentiveParticipant;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getWorkValue() {
		return workValue;
	}

	public void setWorkValue(String workValue) {
		this.workValue = workValue;
	}

}
