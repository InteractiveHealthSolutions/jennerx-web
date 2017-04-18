package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table (name = "vialcount")
public class VialCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Short vaccineId;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "vialcount_vaccineId_vaccine_vaccineId_FK")
	private Vaccine	vaccine;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	private Integer startCount;
	private Integer endCount;
	
	private Integer centreId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "centreId", insertable = false, updatable = false)
	@ForeignKey(name = "vialcount_centreId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter	vaccinationCenter;
	
	private Integer roundId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Round.class)
	@JoinColumn(name = "roundId", insertable = false, updatable = false)
	@ForeignKey(name = "vialcount_roundId_round_roundId_FK")
	private Round round;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(Short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCentreId() {
		return centreId;
	}

	public void setCentreId(Integer centreId) {
		this.centreId = centreId;
	}

	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public Integer getRoundId() {
		return roundId;
	}

	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public Integer getStartCount() {
		return startCount;
	}

	public void setStartCount(Integer startCount) {
		this.startCount = startCount;
	}

	public Integer getEndCount() {
		return endCount;
	}

	public void setEndCount(Integer endCount) {
		this.endCount = endCount;
	}

}
