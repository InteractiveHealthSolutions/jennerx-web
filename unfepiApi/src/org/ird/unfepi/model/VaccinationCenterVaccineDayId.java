package org.ird.unfepi.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VaccinationCenterVaccineDayId implements Serializable{

	private int vaccinationCenterId;
	
	private short vaccineId;
	
	private short dayNumber;


	public VaccinationCenterVaccineDayId() {
		// TODO Auto-generated constructor stub
	}

	public VaccinationCenterVaccineDayId(int vaccinationCenterId, short vaccineId, short dayNumber) {
		this.vaccinationCenterId = vaccinationCenterId;
		this.vaccineId = vaccineId;
		this.dayNumber = dayNumber;
	}


	public int getVaccinationCenterId() {
		return vaccinationCenterId;
	}


	public void setVaccinationCenterId(int vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}


	public short getVaccineId() {
		return vaccineId;
	}


	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}


	public short getDayNumber() {
		return dayNumber;
	}


	public void setDayNumber(short dayNumber) {
		this.dayNumber = dayNumber;
	}
	

}
