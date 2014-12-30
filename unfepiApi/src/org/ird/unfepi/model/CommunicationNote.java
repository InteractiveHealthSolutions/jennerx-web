package org.ird.unfepi.model;

import java.util.Date;

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

@Entity
@Table(name = "communicationnote")
public class CommunicationNote {

	public enum CommunicationEventType{
		M_CALL,
		D_CALL,
		R_CALL,
		OUTBOUND,
		INBOUND,
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int communicationNoteId;

	private String receiver;

	private String source;
	
	private String subject;
	
	private String problem;
	
	private String problemGroup;
	
	private String solution;
	
	private String solutionGroup;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date eventDate;
	
	private String probeId;
	
	private String probeClass;
	
	@Enumerated(EnumType.STRING)
	private CommunicationEventType communicationEventType;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "commnote_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	public CommunicationNote() {
	}
	
	public int getCommunicationNoteId() {
		return communicationNoteId;
	}

	public void setCommunicationNoteId(int communicationNoteId) {
		this.communicationNoteId = communicationNoteId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getProblemGroup() {
		return problemGroup;
	}

	public void setProblemGroup(String problemGroup) {
		this.problemGroup = problemGroup;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getSolutionGroup() {
		return solutionGroup;
	}

	public void setSolutionGroup(String solutionGroup) {
		this.solutionGroup = solutionGroup;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getProbeId() {
		return probeId;
	}

	public void setProbeId(String probeId) {
		this.probeId = probeId;
	}

	public String getProbeClass() {
		return probeClass;
	}

	public void setProbeClass(String probeClass) {
		this.probeClass = probeClass;
	}

	public CommunicationEventType getCommunicationEventType() {
		return communicationEventType;
	}

	public void setCommunicationEventType(
			CommunicationEventType communicationEventType) {
		this.communicationEventType = communicationEventType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
}
