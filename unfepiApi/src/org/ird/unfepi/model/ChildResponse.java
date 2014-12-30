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
 * The Class ChildResponse.
 */
@Entity
@Table (name = "childresponse")
public class ChildResponse implements java.io.Serializable {

	/**
	 * The Enum ChildResponseType.
	 */
	public enum ChildResponseType{
		M_CALL,
		
		R_CALL,
		
		SMS,
	}
	
	/** The response id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int responseId;
	
	/** The vaccination record num. */
	private int vaccinationRecordNum;
	
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
	private ChildResponseType responseType;
	
	private Integer childId;

	/** The child. */
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Child.class)
	@JoinColumn(name = "childId", insertable = false, updatable = false)
	@ForeignKey(name = "childresponse_childId_child_mappedId_FK")
	private Child child;

	private String referenceNumber;
	
	/** The system logging date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date systemLoggingDate;
	
	/**
	 * Instantiates a new child response.
	 */
	public ChildResponse() {
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
	 * Gets the vaccination record num.
	 *
	 * @return the vaccination record num
	 */
	public int getVaccinationRecordNum() {
		return vaccinationRecordNum;
	}

	/**
	 * Sets the vaccination record num.
	 *
	 * @param vaccinationRecordNum the new vaccination record num
	 */
	public void setVaccinationRecordNum(int vaccinationRecordNum) {
		this.vaccinationRecordNum = vaccinationRecordNum;
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
	public ChildResponseType getResponseType() {
		return responseType;
	}

	/**
	 * Sets the response type.
	 *
	 * @param responseType the new response type
	 */
	public void setResponseType(ChildResponseType responseType) {
		this.responseType = responseType;
	}

	/**
	 * Gets the child.
	 *
	 * @return the child
	 */
	public Child getChild() {
		return child;
	}

	/**
	 * Sets the child.
	 *
	 * @param child the new child
	 */
	void setChild(Child child) {
		this.child = child;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
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
