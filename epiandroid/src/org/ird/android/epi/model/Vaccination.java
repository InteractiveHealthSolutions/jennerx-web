package org.ird.android.epi.model;

import java.util.Comparator;
import java.util.Date;

public class Vaccination implements Comparable<Vaccination> {

	private boolean isGiven;
	private Date dueDate;
	private Date vaccinationDate;
	private String centre;
	private Vaccine vaccineGiven;
	private Child child;
	private Boolean isEditable;

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isGiven() {
		return isGiven;
	}

	public void setGiven(boolean isGiven) {
		this.isGiven = isGiven;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getVaccinationDate() {
		return vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public String getCentre() {
		return centre;
	}

	public void setCentre(String centre) {
		this.centre = centre;
	}

	public Vaccine getGivenVaccine() {
		return vaccineGiven;
	}

	public void setGivenVaccine(Vaccine givenVaccine) {
		this.vaccineGiven = givenVaccine;
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	@Override
	public int compareTo(Vaccination another) {
		// checking for pending vaccinations
		if ((!another.isGiven()) && (!this.isGiven())) {
			if (another.getDueDate().getTime() > this.dueDate.getTime()) {
				return -1;
			} else if (another.getDueDate().getTime() < this.dueDate.getTime()) {
				return 1;
			} else if (another.getDueDate().getTime() == this.dueDate.getTime()) {
				return 0;
			}
		}
		// checking for vaccinated ones
		else if (another.isGiven() && this.isGiven()) {
			if (another.getVaccinationDate().getTime() > this.vaccinationDate.getTime()) {
				return -1;
			} else if (another.getVaccinationDate().getTime() < this.vaccinationDate.getTime()) {
				return 1;
			} else if (another.getVaccinationDate().getTime() == this.vaccinationDate.getTime()) {
				return 0;
			}
		} else if (another.isGiven == true && this.isGiven == false) {
			return -1;
		} else if (another.isGiven == false && this.isGiven == true) {
			return 1;
		}

		return 0;
	}

}
