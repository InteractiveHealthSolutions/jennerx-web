package org.ird.android.epi.model;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Vaccine implements Serializable {

	private String name;
	private boolean isGiven;
	private Date dueDate;
	private Date vaccinationDate;
	private String centre;
	
	private Parcel in;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (o instanceof Vaccine) {
			if (((Vaccine) o).name.equals(this.name)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}



	
	
}
