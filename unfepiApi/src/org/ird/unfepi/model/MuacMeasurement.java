package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	public enum COLOR_RANGE{
		GREEN("GREEN"),
		YELLOW("YELLOW"),
		ORANGE("ORANGE"),
		RED("RED");
		
		private String stringValue;
		private COLOR_RANGE(String stringValue) {
			this.stringValue = stringValue;
		}
		
		@Override
		public String toString() {
			return stringValue;
		}
	}
	
//	public enum COLOR_RANGE{
//		GREEN,
//		YELLOW,
//		ORANGE,
//		RED;
//	}
	
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
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private COLOR_RANGE colorrange;
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

	public COLOR_RANGE getColorrange() {
		return colorrange;
	}

	public void setColorrange(COLOR_RANGE colorrange) {
		this.colorrange = colorrange;
	}

	
}
