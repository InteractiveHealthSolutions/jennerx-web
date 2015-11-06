/**
 * 
 */
package org.ird.unfepi.web.utils;

import java.util.Date;
import java.util.UUID;

/**
 * @author Safwan
 *
 */
public class WomenVaccinationCenterVisit {
	
	public static final String VACCINE_SCHEDULE_KEY = "VACCINE_SCHEDULE";

	private Integer womenId;
	private Date visitDate;
	private Integer vaccinatorId;
	private Integer vaccinationCenterId;
	private String epiNumber;
	private String contactPrimary;
	private String contactSecondary;
	private String uuid;
	
	public WomenVaccinationCenterVisit() {
		setUuid(UUID.randomUUID().toString());
	}

	public WomenVaccinationCenterVisit(Integer womenId, Date visitDate,
			Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber,
			String contactPrimary, String contactSecondary ) {
		this.womenId = womenId;
		this.visitDate = visitDate;
		this.vaccinatorId = vaccinatorId;
		this.vaccinationCenterId = vaccinationCenterId;
		this.epiNumber = epiNumber;
		this.contactPrimary = contactPrimary;
		this.contactSecondary = contactSecondary;
	}

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

}
