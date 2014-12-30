package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "dailysummaryvaccinegiven")
public class DailySummaryVaccineGiven {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private Integer summaryVaccineId;
	
	private Short vaccineId;
	
	private Short quantityGiven;
	
	private String quantityUnit;

	private String	vaccineName;
	
	private Boolean	vaccineExists;
	
	private String encounterType;
	
	private Integer dailySummaryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dailySummaryId", insertable = false, updatable = false)
	@ForeignKey(name = "dailysumvacgvn_dailySummaryId_dailysum_serialNum_FK")
	private DailySummary	dailySummary;
	
	
	public DailySummaryVaccineGiven() {
		
	}


	public Integer getSummaryVaccineId() {
		return summaryVaccineId;
	}


	public void setSummaryVaccineId(Integer summaryVaccineId) {
		this.summaryVaccineId = summaryVaccineId;
	}


	public Short getVaccineId() {
		return vaccineId;
	}


	public void setVaccineId(Short vaccineId) {
		this.vaccineId = vaccineId;
	}


	public Short getQuantityGiven() {
		return quantityGiven;
	}


	public void setQuantityGiven(Short quantityGiven) {
		this.quantityGiven = quantityGiven;
	}


	public String getQuantityUnit() {
		return quantityUnit;
	}


	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}


	public String getVaccineName() {
		return vaccineName;
	}


	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}


	public Boolean getVaccineExists() {
		return vaccineExists;
	}


	public void setVaccineExists(Boolean vaccineExists) {
		this.vaccineExists = vaccineExists;
	}


	public String getEncounterType() {
		return encounterType;
	}


	public void setEncounterType(String encounterType) {
		this.encounterType = encounterType;
	}


	public Integer getDailySummaryId() {
		return dailySummaryId;
	}


	public void setDailySummaryId(Integer dailySummaryId) {
		this.dailySummaryId = dailySummaryId;
	}


	public DailySummary getDailySummary() {
		return dailySummary;
	}


	public void setDailySummary(DailySummary dailySummary) {
		this.dailySummary = dailySummary;
	}
}
