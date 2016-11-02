package org.ird.unfepi.beans;

import java.util.List;

import org.ird.unfepi.model.VaccinationCalendar;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;


public class VaccineRegistrationWrapper {
	
	private List<VaccineGap> vaccineGapList;
//	private List<VaccinePrerequisite> vaccinePrerequisiteList;
	private String[] vaccinePrerequisites;
	private Integer vaccinationCalendarId;
	private Integer vaccineId;
	
	public VaccineRegistrationWrapper(){
	}	
	
	/**
	 * @return the vaccineGapList
	 */
	public List<VaccineGap> getVaccineGapList() {
		return vaccineGapList;
	}
	/**
	 * @param vaccineGapList the vaccineGapList to set
	 */
	public void setVaccineGapList(List<VaccineGap> vaccineGapList) {
		this.vaccineGapList = vaccineGapList;
	}
//	/**
//	 * @return the vaccinePrerequisiteList
//	 */
//	public List<VaccinePrerequisite> getVaccinePrerequisiteList() {
//		return vaccinePrerequisiteList;
//	}
//	/**
//	 * @param vaccinePrerequisiteList the vaccinePrerequisiteList to set
//	 */
//	public void setVaccinePrerequisiteList(
//			List<VaccinePrerequisite> vaccinePrerequisiteList) {
//		this.vaccinePrerequisiteList = vaccinePrerequisiteList;
//	}

	public String[] getVaccinePrerequisites() {
		return vaccinePrerequisites;
	}

	public void setVaccinePrerequisites(String[] vaccinePrerequisites) {
		this.vaccinePrerequisites = vaccinePrerequisites;
	}

	public Integer getVaccinationCalendarId() {
		return vaccinationCalendarId;
	}

	public void setVaccinationCalendarId(Integer vaccinationCalendarId) {
		this.vaccinationCalendarId = vaccinationCalendarId;
	}

	public Integer getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(Integer vaccineId) {
		this.vaccineId = vaccineId;
	}

}
