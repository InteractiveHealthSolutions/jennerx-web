package org.ird.unfepi.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="muacmeasurement")
public class MuacMeasurement {
	//TODO
//	muac range unit cm/mm datatype double/int
	
	public enum MalnutritionColor{
		GREEN,
		YELLOW,
		ORANGE,
		RED
	}
	
	public enum MalnutritionStatus{
		NORMAL,
		RISK,
		MODERATE_MALNUTRITION,
		SERVE_MALNUTRITION		
	}
	
	@EmbeddedId
	MuacMeasurementId muacId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Child.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false)
	@ForeignKey(name = "muacmeasurement_mappedId_child_mappedId_FK")
	private Child child;
	
	private Double circumference;

	/**
	 * @return the muacId
	 */
	public MuacMeasurementId getMuacId() {
		return muacId;
	}

	/**
	 * @param muacId the muacId to set
	 */
	public void setMuacId(MuacMeasurementId muacId) {
		this.muacId = muacId;
	}

	/**
	 * @return the child
	 */
	public Child getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Child child) {
		this.child = child;
	}

	/**
	 * @return the circumference
	 */
	public Double getCircumference() {
		return circumference;
	}

	/**
	 * @param circumference the circumference to set
	 */
	public void setCircumference(Double circumference) {
		this.circumference = circumference;
	}
	
}
