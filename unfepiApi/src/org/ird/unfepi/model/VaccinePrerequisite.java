package org.ird.unfepi.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table (name = "vaccineprerequisite")
public class VaccinePrerequisite {
	
	@Id
	private VaccinePrerequisiteId vaccinePrerequisiteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinePrerequisiteId", insertable = false, updatable = false)
	@ForeignKey(name = "vaccprereq_prereqId_vaccine_vaccineId_FK")
	private Vaccine	prerequisite;
	
	private Boolean mandatory;

	public VaccinePrerequisiteId getVaccinePrerequisiteId() {
		return vaccinePrerequisiteId;
	}

	public void setVaccinePrerequisiteId(VaccinePrerequisiteId vaccinePrerequisiteId) {
		this.vaccinePrerequisiteId = vaccinePrerequisiteId;
	}
	
	public VaccinePrerequisite() {
		
	}

	public Vaccine getPrerequisite() {
		return prerequisite;
	}

	void setPrerequisite(Vaccine prerequisite) {
		this.prerequisite = prerequisite;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
}
