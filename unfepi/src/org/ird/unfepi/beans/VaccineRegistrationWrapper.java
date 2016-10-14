package org.ird.unfepi.beans;

import java.util.List;

import org.ird.unfepi.model.VaccinationCalendar;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;


public class VaccineRegistrationWrapper {
	
	private Vaccine vaccine;
	private List<VaccineGap> vaccineGapList;
	private List<VaccinePrerequisite> vaccinePrerequisiteList;
	private VaccinationCalendar vaccinationCalendar;;
	
	public VaccineRegistrationWrapper(){
		vaccine = new Vaccine();
	}
	
	
	/**
	 * @return the vaccine
	 */
	public Vaccine getVaccine() {
		return vaccine;
	}
	/**
	 * @param vaccine the vaccine to set
	 */
	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
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
	/**
	 * @return the vaccinePrerequisiteList
	 */
	public List<VaccinePrerequisite> getVaccinePrerequisiteList() {
		return vaccinePrerequisiteList;
	}
	/**
	 * @param vaccinePrerequisiteList the vaccinePrerequisiteList to set
	 */
	public void setVaccinePrerequisiteList(
			List<VaccinePrerequisite> vaccinePrerequisiteList) {
		this.vaccinePrerequisiteList = vaccinePrerequisiteList;
	}


	/**
	 * @return the vaccinationCalendar
	 */
	public VaccinationCalendar getVaccinationCalendar() {
		return vaccinationCalendar;
	}


	/**
	 * @param vaccinationCalendar the vaccinationCalendar to set
	 */
	public void setVaccinationCalendar(VaccinationCalendar vaccinationCalendar) {
		this.vaccinationCalendar = vaccinationCalendar;
	}

}
