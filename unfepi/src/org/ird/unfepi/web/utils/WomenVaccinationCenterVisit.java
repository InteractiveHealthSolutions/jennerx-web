/**
 * 
 */
package org.ird.unfepi.web.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ird.unfepi.model.WomenVaccination;

/**
 * @author Safwan
 *
 */
public class WomenVaccinationCenterVisit {
	
	public static final String WOMEN_VACCINE_SCHEDULE_KEY = "WOMEN_VACCINE_SCHEDULE";

	private Integer womenId;
	private Date visitDate;
	private Integer vaccinatorId;
	private Integer vaccinationCenterId;
	private String epiNumber;
	private String contactPrimary;
	private String contactSecondary;
	private String uuid;
	private WomenVaccination tt1;
	private WomenVaccination tt2;
	private WomenVaccination tt3;
	private WomenVaccination tt4;
	private WomenVaccination tt5;
	//private List<WomenVaccination> vaccinations;
	
	

	public WomenVaccinationCenterVisit() {
		setUuid(UUID.randomUUID().toString());
		tt1 = new WomenVaccination();
		tt2 = new WomenVaccination();
		tt3 = new WomenVaccination();
		tt4 = new WomenVaccination();
		tt5 = new WomenVaccination();
		
		/*vaccinations = new ArrayList<WomenVaccination>();
		vaccinations.add(tt1);
		vaccinations.add(tt2);
		vaccinations.add(tt3);
		vaccinations.add(tt4);
		vaccinations.add(tt5);*/
	}
	

	public WomenVaccinationCenterVisit(Integer womenId, Date visitDate,
			Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber,
			String contactPrimary, String contactSecondary, WomenVaccination tt1, WomenVaccination tt2,
			WomenVaccination tt3, WomenVaccination tt4, WomenVaccination tt5) {
		this.womenId = womenId;
		this.visitDate = visitDate;
		this.vaccinatorId = vaccinatorId;
		this.vaccinationCenterId = vaccinationCenterId;
		this.epiNumber = epiNumber;
		this.contactPrimary = contactPrimary;
		this.contactSecondary = contactSecondary;
		this.tt1 = tt1;
		this.tt2 = tt2;
		this.tt3 = tt3;
		this.tt4 = tt4;
		this.tt5 = tt5;
	}
	
	/*public List<WomenVaccination> getVaccinations() {
		return vaccinations;
	}


	public void setVaccinations(List<WomenVaccination> vaccinations) {
		this.vaccinations = vaccinations;
	}*/

	public Integer getWomenId() {
		return womenId;
	}

	public void setWomenId(Integer womenId) {
		this.womenId = womenId;
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}

	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}

	public Integer getVaccinationCenterId() {
		return vaccinationCenterId;
	}

	public void setVaccinationCenterId(Integer vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}

	public String getEpiNumber() {
		return epiNumber;
	}

	public void setEpiNumber(String epiNumber) {
		this.epiNumber = epiNumber;
	}

	public String getContactPrimary() {
		return contactPrimary;
	}

	public void setContactPrimary(String contactPrimary) {
		this.contactPrimary = contactPrimary;
	}

	public String getContactSecondary() {
		return contactSecondary;
	}

	public void setContactSecondary(String contactSecondary) {
		this.contactSecondary = contactSecondary;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public WomenVaccination getTt1() {
		return tt1;
	}

	public void setTt1(WomenVaccination tt1) {
		this.tt1 = tt1;
	}

	public WomenVaccination getTt2() {
		return tt2;
	}

	public void setTt2(WomenVaccination tt2) {
		this.tt2 = tt2;
	}

	public WomenVaccination getTt3() {
		return tt3;
	}

	public void setTt3(WomenVaccination tt3) {
		this.tt3 = tt3;
	}

	public WomenVaccination getTt4() {
		return tt4;
	}

	public void setTt4(WomenVaccination tt4) {
		this.tt4 = tt4;
	}

	public WomenVaccination getTt5() {
		return tt5;
	}

	public void setTt5(WomenVaccination tt5) {
		this.tt5 = tt5;
	}

}
