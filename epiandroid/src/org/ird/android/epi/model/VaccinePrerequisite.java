package org.ird.android.epi.model;

public class VaccinePrerequisite {
	Vaccine vaccine;
	Vaccine prereq;
	boolean isMandatory;
	
	public VaccinePrerequisite (Vaccine vaccine, Vaccine prereq, boolean isMandatory)
	{
		this.vaccine = vaccine;
		this.prereq = prereq;
		this.isMandatory = isMandatory;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public Vaccine getPrereq() {
		return prereq;
	}

	public void setPrereq(Vaccine prereqs) {
		this.prereq = prereqs;
	}

	public boolean isMandatory()
	{
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory)
	{
		this.isMandatory = isMandatory;
	}
}
