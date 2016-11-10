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

/**
 * 
 * @author Hera Rafique
 *
 */

@Entity
@Table(name = "healthprogram")
public class HealthProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer programId;

	@Column(length = 30, unique = true, nullable = false)
	private String name;

	@Column(length = 300)
	private String description;
	
	private Integer enrollmentLimit;
	
//	@Column(nullable = false)
	private Integer vaccinationcalendarId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCalendar.class)
	@JoinColumn(name = "vaccinationcalendarId", insertable = false, updatable = false)
	@ForeignKey(name = "healthprogram_vaccinationcalendarId_vaccinationcalendar_calenderId_FK")
	private VaccinationCalendar vaccinationCalendar;

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEnrollmentLimit() {
		return enrollmentLimit;
	}

	public void setEnrollmentLimit(Integer enrollmentLimit) {
		this.enrollmentLimit = enrollmentLimit;
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
