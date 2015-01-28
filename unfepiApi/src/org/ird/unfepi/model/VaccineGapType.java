package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "vaccinegaptype")
public class VaccineGapType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short vaccineGapTypeId;
	
	private String name;
	
	public VaccineGapType() {
		
	}

	public short getVaccineGapTypeId() {
		return vaccineGapTypeId;
	}

	public void setVaccineGapTypeId(short vaccineGapTypeId) {
		this.vaccineGapTypeId = vaccineGapTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
