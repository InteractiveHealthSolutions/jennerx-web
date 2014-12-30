package org.ird.android.epi;

public enum VaccinationStatus {

	VACCINATED("Vaccinated"),
	RETRO("Retro"),
	RETRO_DATE_MISSING("Retro Date Missing"),
	SCHEDULED("Scheduled");

	private String status;

	VaccinationStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override public String toString(){
        return status;
    }
	

}
