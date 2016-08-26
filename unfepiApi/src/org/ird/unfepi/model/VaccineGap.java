package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.ird.unfepi.model.Model.TimeIntervalUnit;

@Entity
@Table (name = "vaccinegap")
public class VaccineGap {
	
	@Id
	private VaccineGapId id;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccineGapTypeId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinegap_vaccineId_vaccinegaptype_vaccinegaptypeId_FK")
	private VaccineGapType	vaccineGapType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinegap_vaccineId_vaccine_vaccineId_FK")
	private Vaccine	vaccine;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private TimeIntervalUnit gapTimeUnit;
	
	@Column(nullable = false)
	private short value;
	
	@Column(nullable = false)
	private short priority;
	
	private Boolean mandatory;
	
	@Column(nullable = false)
	private Integer vaccinationcalendarId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCalendar.class)
	@JoinColumn(name = "vaccinationcalendarId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinegap_vaccinationcalendarId_vaccinationcalendar_calenderId_FK")
	private VaccinationCalendar vaccinationCalendar;
	
	
	public VaccineGap() {
		
	}

	public VaccineGapId getId() {
		return id;
	}

	public void setId(VaccineGapId id) {
		this.id = id;
	}

	public VaccineGapType getVaccineGapType() {
		return vaccineGapType;
	}

	public void setVaccineGapType(VaccineGapType vaccineGapType) {
		this.vaccineGapType = vaccineGapType;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public TimeIntervalUnit getGapTimeUnit() {
		return gapTimeUnit;
	}

	public void setGapTimeUnit(TimeIntervalUnit gapTimeUnit) {
		this.gapTimeUnit = gapTimeUnit;
	}

	public short getValue() {
		return value;
	}

	public void setValue(short value) {
		this.value = value;
	}

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public Integer getVaccinationcalendarId() {
		return vaccinationcalendarId;
	}

	public void setVaccinationcalendarId(Integer vaccinationcalendarId) {
		this.vaccinationcalendarId = vaccinationcalendarId;
	}

	public VaccinationCalendar getVaccinationCalendar() {
		return vaccinationCalendar;
	}

	public void setVaccinationCalendar(VaccinationCalendar vaccinationCalendar) {
		this.vaccinationCalendar = vaccinationCalendar;
	}
}
