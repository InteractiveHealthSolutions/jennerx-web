package org.ird.android.epi.alert;

import java.io.Serializable;
import java.util.Date;

public class VaccineScheduleRow implements Serializable {
	private static final long serialVersionUID = 1L;

	String childId;
	Date dateOfBirth;
	String vaccineName;
	Date dueDate;
	Date vaccinationDate;
	String center;
	String status;

	/**
	 * Vaccine was selected and was vaccinated
	 */
	boolean isGiven;

	/**
	 * Flag that is switched on and off based if the vaccine has been given or
	 * not. No change is allowed for already ad vaccines.
	 */
	boolean isEditable;

	/**
	 * Flag set to true until the row is selected. After the vaccination is
	 * given, this is set to false TODO: this will toggle once the UNDO
	 * functionality is added to system
	 */
	boolean isSelected;

	/**
	 * Flag set to true if the vaccine is Eligible for administered or scheduled
	 */
	boolean isEligible;

	/**
	 * Flag set to true if the vaccine is not applicable with regards to the
	 * date of birth.
	 * 
	 */

	boolean isApplicable;

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date assginedDate) {
		this.dueDate = assginedDate;
	}

	public Date getVaccinationDate() {
		return vaccinationDate;
	}

	public void setVaccinationDate(Date vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public boolean isGiven() {
		return isGiven;
	}

	public void setGiven(boolean isGiven) {
		this.isGiven = isGiven;
	}

	public boolean isIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean idEditable) {
		this.isEditable = idEditable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isEligible() {
		return isEligible;
	}

	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isApplicable() {
		return isApplicable;
	}

	public void setApplicable(boolean isApplicable) {
		this.isApplicable = isApplicable;
	}

}
