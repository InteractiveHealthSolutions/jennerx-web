package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "variablesetting")
public class VariableSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short serialId;
	
	@Column(length = 50)
	private String type;

	private String name;
	
	private String element;

	private String value;

	private Float rangeLower;

	private Float rangeUpper;

	private String uid;

	public VariableSetting() {
		// TODO Auto-generated constructor stub
	}

	public short getSerialId() {
		return serialId;
	}

	public void setSerialId(short serialId) {
		this.serialId = serialId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Float getRangeLower() {
		return rangeLower;
	}

	public void setRangeLower(Float rangeLower) {
		this.rangeLower = rangeLower;
	}

	public Float getRangeUpper() {
		return rangeUpper;
	}

	public void setRangeUpper(Float rangeUpper) {
		this.rangeUpper = rangeUpper;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
}
