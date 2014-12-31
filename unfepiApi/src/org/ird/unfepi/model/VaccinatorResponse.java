/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

/**
 * The Class VaccinatorResponse.
 */
@Entity
@Table(name = "vaccinatorresponse")
public class VaccinatorResponse {

	/**
	 * The Enum VaccinatorResponseType.
	 */
	public enum VaccinatorResponseType{
		M_CALL,
		
		R_CALL,
		
		SMS,
	}
	
	/** The response id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int responseId;
	
	/** The originator cell number. */
	@Column(length = 20)
	private String originatorCellNumber;
	
	/** The recipient cell number. */
	@Column(length = 20)
	private String recipientCellNumber;
	
	/** The recieved date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date recievedDate;
	
	/** The response text. */
	@Column(length = 2000)
	private String responseText;
	
	/** The response type. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private VaccinatorResponseType responseType;
	
	private Integer vaccinatorId; 
	/** The vaccinator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccinatorresponse_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator vaccinator;

	private String referenceNumber;
	
	/** The system logging date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date systemLoggingDate;

	public VaccinatorResponse() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Gets the response id.
	 *
	 * @return the response id
	 */
	public int getResponseId() {
		return responseId;
	}

	/**
	 * Sets the response id.
	 *
	 * @param responseId the new response id
	 */
	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	/**
	 * Gets the originator cell number.
	 *
	 * @return the originator cell number
	 */
	public String getOriginatorCellNumber() {
		return originatorCellNumber;
	}

	/**
	 * Sets the originator cell number.
	 *
	 * @param originatorCellNumber the new originator cell number
	 */
	public void setOriginatorCellNumber(String originatorCellNumber) {
		this.originatorCellNumber = originatorCellNumber;
	}

	/**
	 * Gets the recipient cell number.
	 *
	 * @return the recipient cell number
	 */
	public String getRecipientCellNumber() {
		return recipientCellNumber;
	}

	/**
	 * Sets the recipient cell number.
	 *
	 * @param recipientCellNumber the new recipient cell number
	 */
	public void setRecipientCellNumber(String recipientCellNumber) {
		this.recipientCellNumber = recipientCellNumber;
	}

	/**
	 * Gets the recieved date.
	 *
	 * @return the recieved date
	 */
	public Date getRecievedDate() {
		return recievedDate;
	}

	/**
	 * Sets the recieved date.
	 *
	 * @param recievedDate the new recieved date
	 */
	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}

	/**
	 * Gets the response text.
	 *
	 * @return the response text
	 */
	public String getResponseText() {
		return responseText;
	}

	/**
	 * Sets the response text.
	 *
	 * @param responseText the new response text
	 */
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	/**
	 * Gets the response type.
	 *
	 * @return the response type
	 */
	public VaccinatorResponseType getResponseType() {
		return responseType;
	}

	/**
	 * Sets the response type.
	 *
	 * @param responseType the new response type
	 */
	public void setResponseType(VaccinatorResponseType responseType) {
		this.responseType = responseType;
	}

	/**
	 * Gets the vaccinator.
	 *
	 * @return the vaccinator
	 */
	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	/**
	 * Sets the vaccinator.
	 *
	 * @param vaccinator the new vaccinator
	 */
	void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}
	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}
	/**
	 * Gets the system logging date.
	 *
	 * @return the system logging date
	 */
	public Date getSystemLoggingDate() {
		return systemLoggingDate;
	}

	/**
	 * Sets the system logging date.
	 *
	 * @param systemLoggingDate the new system logging date
	 */
	public void setSystemLoggingDate(Date systemLoggingDate) {
		this.systemLoggingDate = systemLoggingDate;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
}