package org.ird.unfepi.model;

import javax.persistence.Column;
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
@Table (name = "vaccinegaptype")
public class VaccineGapType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short vaccineGapTypeId;
	
	private String name;
	
	@Column(nullable = false)
	private Integer vaccinationcalendarId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCalendar.class)
	@JoinColumn(name = "vaccinationcalendarId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinegaptype_vaccinationcalendarId_vaccinationcalendar_calenderId_FK")
	private VaccinationCalendar vaccinationCalendar;
	
	public VaccineGapType() {
		
	}

	public short getVaccineGapTypeId() {
		return vaccineGapTypeId;
	}

	public void setVaccineGapTypeId(short vaccineGapTypeId) {
		this.vaccineGapTypeId = vaccineGapTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
