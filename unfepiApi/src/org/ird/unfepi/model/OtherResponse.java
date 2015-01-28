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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class OtherResponse.
 */
@Entity
@Table(name = "otherresponse")
public class OtherResponse {
	
	public enum OtherResponseType{
		M_CALL,
		
		R_CALL,
		
		SMS,
	}
	/** The response id. */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private int responseId;
	
	/** The person id. */
	private Integer mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false, nullable = true)
	@ForeignKey(name = "otherresponse_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	/** The originator cell number. */
	@Column(length = 20)
	private String originatorCellNumber;
	
	/** The recipient cell number. */
	@Column(length = 20)
	private String recipientCellNumber;
	
	/** The response type. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private OtherResponseType responseType;
	
	/** The recieved date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date recievedDate;
	
	/** The response text. */
	@Column(length = 2000)
	private String responseText;

	private String referenceNumber;
	
	public OtherResponse() {
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

	public Integer getMappedId() {
		return mappedId;
	}

	public void setMappedId(Integer mappedId) {
		this.mappedId = mappedId;
	}

	public IdMapper getIdMapper() {
		return idMapper;
	}
	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
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

	public OtherResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(OtherResponseType responseType) {
		this.responseType = responseType;
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

	/** The system logging date. */
	
	private Date systemLoggingDate;
}
