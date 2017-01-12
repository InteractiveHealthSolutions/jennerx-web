package org.ird.unfepi.web.utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ird.unfepi.model.ItemsDistributed;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.MuacMeasurement;

public class VaccinationCenterVisit {
	public static final String VACCINE_SCHEDULE_KEY = "VACCINE_SCHEDULE";

	private Integer childId;
	private Date visitDate;
	private Integer vaccinatorId;
	private Integer vaccinationCenterId;
	private String epiNumber;
	private String contactPrimary;
	private String contactSecondary;
	private LotterySms preference = new LotterySms();
	private String uuid;
	private Integer healthProgramId;
	private List<ItemsDistributed> itemsDistributedL;
	private MuacMeasurement muacMeasurement;

	//private Date nextAssignedDate;
	
	public VaccinationCenterVisit() {
		setUuid(UUID.randomUUID().toString());

		/*this.vaccineNext = MapUtils.lazyMap(new HashMap<String,NextVaccineMap>(), new Factory() {
	          public Object create() {
	              return new NextVaccineMap();
	          }
	      });*/
	}

	
	public VaccinationCenterVisit(Integer childId, Date visitDate,
			Integer vaccinatorId, Integer vaccinationCenterId,
			String epiNumber, Boolean hasApprovedLottery,
			String contactPrimary, String contactSecondary,
			LotterySms preference ) {
		this.childId = childId;
		this.visitDate = visitDate;
		this.vaccinatorId = vaccinatorId;
		this.vaccinationCenterId = vaccinationCenterId;
		this.epiNumber = epiNumber;
		this.contactPrimary = contactPrimary;
		this.contactSecondary = contactSecondary;
		this.preference = preference;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
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

	public LotterySms getPreference() {
		return preference;
	}

	public void setPreference(LotterySms preference) {
		this.preference = preference;
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


	public Integer getHealthProgramId() {
		return healthProgramId;
	}


	public void setHealthProgramId(Integer healthProgramId) {
		this.healthProgramId = healthProgramId;
	}


	public List<ItemsDistributed> getItemsDistributedL() {
		return itemsDistributedL;
	}


	public void setItemsDistributedL(List<ItemsDistributed> itemsDistributedL) {
		this.itemsDistributedL = itemsDistributedL;
	}


	public MuacMeasurement getMuacMeasurement() {
		return muacMeasurement;
	}


	public void setMuacMeasurement(MuacMeasurement muacMeasurement) {
		this.muacMeasurement = muacMeasurement;
	}
}