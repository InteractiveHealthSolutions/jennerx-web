package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table (name = "vaccineprerequisite")
public class VaccinePrerequisite {
	
	@Id
	private VaccinePrerequisiteId vaccinePrerequisiteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinePrerequisiteId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccprereq_prereqId_vaccine_vaccineId_FK")
	private Vaccine	prerequisite;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCalendar.class)
	@JoinColumn(name = "vaccinationcalendarId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccineprerequisite_vaccinationcalendarId_vaccinationcalendar_calenderId_FK")
	private VaccinationCalendar vaccinationCalendar;
	
	private Boolean mandatory;
	
	public VaccinePrerequisiteId getVaccinePrerequisiteId() {
		return vaccinePrerequisiteId;
	}

	public void setVaccinePrerequisiteId(VaccinePrerequisiteId vaccinePrerequisiteId) {
		this.vaccinePrerequisiteId = vaccinePrerequisiteId;
	}
	
	public VaccinePrerequisite() {
		
	}

	public Vaccine getPrerequisite() {
		return prerequisite;
	}

	void setPrerequisite(Vaccine prerequisite) {
		this.prerequisite = prerequisite;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public VaccinationCalendar getVaccinationCalendar() {
		return vaccinationCalendar;
	}

	public void setVaccinationCalendar(VaccinationCalendar vaccinationCalendar) {
		this.vaccinationCalendar = vaccinationCalendar;
	}
}
