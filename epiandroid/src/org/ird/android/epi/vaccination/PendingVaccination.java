package org.ird.android.epi.vaccination;

import java.util.Date;

public class PendingVaccination
{
	private Date vaccinationDate;
	private String vaccineName;

	public Date getVaccinationDate()
	{
		return vaccinationDate;
	}
	public void setVaccinationDate(Date vaccinationDate)
	{
		this.vaccinationDate = vaccinationDate;
	}
	public String getVaccineName()
	{
		return vaccineName;
	}
	public void setVaccineName(String vaccineName)
	{
		this.vaccineName = vaccineName;
	}
	
}
