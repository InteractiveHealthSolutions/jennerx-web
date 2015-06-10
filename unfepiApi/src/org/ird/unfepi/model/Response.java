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

@Entity
@Table(name = "response")
public class Response {
	
	public enum ResponseType{
		M_CALL,
		R_CALL,
		SMS,
		VACCINE_SCHEDULE_INQUIRY
	}
	
	public enum ResponseStatus{
		ACCEPTED,
		REJECTED,
		IN_PROGRESS,
		WAITING_ID_CONFIRMATION,
		N_A
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int responseId;
	
	private Integer mappedId;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false, nullable = true)
	@ForeignKey(name = "response_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	@Column(length = 20)
	private String originator;
	
	@Column(length = 20)
	private String recipient;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ResponseType responseType;
	
	@Enumerated(EnumType.STRING)
	private ResponseStatus responseStatus;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date responseDate;
	
	@Column(length = 2000)
	private String responseBody;

	private String referenceNumber;
	
	private Date systemLoggingDate;
	
	private String eventClass;
	
	private Integer eventId;


	public Response() {
	}

	public int getResponseId() {
		return responseId;
	}

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

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(ResponseType responseType) {
		this.responseType = responseType;
	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getSystemLoggingDate() {
		return systemLoggingDate;
	}

	public void setSystemLoggingDate(Date systemLoggingDate) {
		this.systemLoggingDate = systemLoggingDate;
	}

	public String getEventClass() {
		return eventClass;
	}

	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

}
