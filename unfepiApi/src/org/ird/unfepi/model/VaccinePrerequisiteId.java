package org.ird.unfepi.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VaccinePrerequisiteId implements Serializable{
	private static final long serialVersionUID = 1L;

	private short vaccineId;
	
	private short vaccinePrerequisiteId;

	public VaccinePrerequisiteId() {
		
	}

	public short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public short getVaccinePrerequisiteId() {
		return vaccinePrerequisiteId;
	}

	public void setVaccinePrerequisiteId(short vaccinePrerequisiteId) {
		this.vaccinePrerequisiteId = vaccinePrerequisiteId;
	}
}
