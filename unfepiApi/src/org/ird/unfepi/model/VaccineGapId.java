package org.ird.unfepi.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VaccineGapId implements Serializable{
	private static final long serialVersionUID = 1L;

	private short vaccineId;
	
	private short vaccineGapTypeId;
	
	private Integer vaccinationcalendarId;
	
	public VaccineGapId() {
		
	}

	public short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public short getVaccineGapTypeId() {
		return vaccineGapTypeId;
	}

	public void setVaccineGapTypeId(short vaccineGapTypeId) {
		this.vaccineGapTypeId = vaccineGapTypeId;
	}

	public Integer getVaccinationcalendarId() {
		return vaccinationcalendarId;
	}

	public void setVaccinationcalendarId(Integer vaccinationcalendarId) {
		this.vaccinationcalendarId = vaccinationcalendarId;
	}

}
