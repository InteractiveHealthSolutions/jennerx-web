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
@Table (name = "roundvaccine")
public class RoundVaccine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Short vaccineId;
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "vaccineId", insertable = false, updatable = false)
	@ForeignKey(name = "roundvaccine_vaccineId_vaccine_vaccineId_FK")
	private Vaccine	vaccine;
	
	private Integer roundId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Round.class)
	@JoinColumn(name = "roundId", insertable = false, updatable = false)
	@ForeignKey(name = "roundvaccine_roundId_round_roundId_FK")
	private Round round;
	
	private Boolean status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the vaccineId
	 */
	public Short getVaccineId() {
		return vaccineId;
	}

	/**
	 * @param vaccineId the vaccineId to set
	 */
	public void setVaccineId(Short vaccineId) {
		this.vaccineId = vaccineId;
	}

	/**
	 * @return the vaccine
	 */
	public Vaccine getVaccine() {
		return vaccine;
	}

	/**
	 * @param vaccine the vaccine to set
	 */
	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	/**
	 * @return the roundId
	 */
	public Integer getRoundId() {
		return roundId;
	}

	/**
	 * @param roundId the roundId to set
	 */
	public void setRoundId(Integer roundId) {
		this.roundId = roundId;
	}

	/**
	 * @return the round
	 */
	public Round getRound() {
		return round;
	}

	/**
	 * @param round the round to set
	 */
	public void setRound(Round round) {
		this.round = round;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

}
