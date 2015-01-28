package org.ird.android.epi.model;

public enum VaccineName {

	BCG("BCG-1"), 
	OPV0("OPV-0"),
	Penta1("Penta-1"),
	PCV1("PCV-1"),
	OPV1("OPV-1"),
	Penta2("Penta-2"),
	PCV2("PCV-2"),
	OPV2("OPV-2"),
	Penta3("Penta-3"),
	PCV3("PCV-3"),
	OPV3("OPV-3"),
	Measles1("Measles-1"),
	Measles2("Measles-2");

	private String vaccine;
	
	
	VaccineName(String vaccineName){
		vaccine = vaccineName;
	}


	public String getVaccine() {
		return vaccine;
	}


	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}
	





	
	
	
}
