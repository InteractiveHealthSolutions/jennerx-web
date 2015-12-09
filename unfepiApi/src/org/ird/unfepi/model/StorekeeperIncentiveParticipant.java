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
@Table (name = "storekeeperincentiveparticipant")
public class StorekeeperIncentiveParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int serialNumber;
	
	private Integer storekeeperIncentiveEventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storekeeperIncentiveEventId", insertable = false, updatable = false)
	@ForeignKey(name = "stkIncntparti_stkIncntEvtId_stkincntk_stkrIncntEvtId_FK")
	private StorekeeperIncentiveEvent storekeeperIncentiveEvent;
	
	private Integer storekeeperId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Storekeeper.class)
	@JoinColumn(name = "storekeeperId", insertable = false, updatable = false)
	@ForeignKey(name = "stkIncntparti_storekeeperId_storekeeper_mappedId_FK")
	private Storekeeper storekeeper;
	
	private Short storekeeperIncentiveParamsId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "storekeeperIncentiveParamsId", insertable = false, updatable = false)
	@ForeignKey(name = "stkIncntparti_stkIncentParamsId_incntparams_incntParamsId_FK")
	private IncentiveParams storekeeperIncentiveParams;
	
	private Boolean isIncentivised;
	
	private Float criteriaElementValue;
	
	private String description;
	
	public StorekeeperIncentiveParticipant() {
		
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getStorekeeperIncentiveEventId() {
		return storekeeperIncentiveEventId;
	}

	public void setStorekeeperIncentiveEventId(Integer storekeeperIncentiveEventId) {
		this.storekeeperIncentiveEventId = storekeeperIncentiveEventId;
	}

	public StorekeeperIncentiveEvent getStorekeeperIncentiveEvent() {
		return storekeeperIncentiveEvent;
	}

	void setStorekeeperIncentiveEvent(StorekeeperIncentiveEvent storekeeperIncentiveEvent) {
		this.storekeeperIncentiveEvent = storekeeperIncentiveEvent;
	}

	public Integer getStorekeeperId() {
		return storekeeperId;
	}

	public void setStorekeeperId(Integer storekeeperId) {
		this.storekeeperId = storekeeperId;
	}

	public Storekeeper getStorekeeper() {
		return storekeeper;
	}

	void setStorekeeper(Storekeeper storekeeper) {
		this.storekeeper = storekeeper;
	}

	public Short getStorekeeperIncentiveParamsId() {
		return storekeeperIncentiveParamsId;
	}

	public void setStorekeeperIncentiveParamsId(Short storekeeperIncentiveParamsId) {
		this.storekeeperIncentiveParamsId = storekeeperIncentiveParamsId;
	}

	public IncentiveParams getStorekeeperIncentiveParams() {
		return storekeeperIncentiveParams;
	}

	void setStorekeeperIncentiveParams(IncentiveParams storekeeperIncentiveParams) {
		this.storekeeperIncentiveParams = storekeeperIncentiveParams;
	}

	public Boolean getIsIncentivised() {
		return isIncentivised;
	}

	public void setIsIncentivised(Boolean isIncentivised) {
		this.isIncentivised = isIncentivised;
	}

	public Float getCriteriaElementValue() {
		return criteriaElementValue;
	}

	public void setCriteriaElementValue(Float criteriaElementValue) {
		this.criteriaElementValue = criteriaElementValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
