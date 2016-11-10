package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "identifiertype")
public class IdentifierType  {
	
	public enum LocationBehavior {
		/**
		 * Indicates that location is required for the current identifier type
		 */
		REQUIRED,
		/**
		 * Indicates that location is not used for the current identifier type
		 */
		NOT_USED
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer identifierTypeId;
	
	@Column(length = 45, unique = true)
	private String name;
	
	private String format;
	
	private Boolean required = Boolean.FALSE;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private LocationBehavior locationBehavior;
	
	public IdentifierType() {
	}

	public Integer getIdentifierTypeId() {
		return identifierTypeId;
	}

	public void setIdentifierTypeId(Integer identifierTypeId) {
		this.identifierTypeId = identifierTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocationBehavior getLocationBehavior() {
		return locationBehavior;
	}

	public void setLocationBehavior(LocationBehavior locationBehavior) {
		this.locationBehavior = locationBehavior;
	}
	
	
}
