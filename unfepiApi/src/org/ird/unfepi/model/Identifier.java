package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;
@Entity
@Table(name = "identifier")
public class Identifier  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer identifierId;
	
	@OneToOne(targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId")
	@ForeignKey(name = "identifier_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	private String identifier;
	
	@OneToOne(targetEntity = IdentifierType.class)
	@JoinColumn(name = "identifierTypeId")
	@ForeignKey(name = "identifier_identifierTypeId_identifierType_identifierTypeId_FK")
	private IdentifierType identifierType;
	
	private Integer locationId;
	
	@OneToOne(targetEntity = Location.class)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "locationId", insertable = false, updatable = false)
	@ForeignKey(name = "identifier_locationId_location_locationId_FK")
	private Location location;
	
	private Boolean preferred = false;
	
	public Identifier() {
	}
	
	public Integer getIdentifierId() {
		return identifierId;
	}

	public void setIdentifierId(Integer identifierId) {
		this.identifierId = identifierId;
	}

	public IdMapper getIdMapper() {
		return idMapper;
	}

	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Boolean getPreferred() {
		return preferred;
	}

	public void setPreferred(Boolean preferred) {
		this.preferred = preferred;
	}

	
}
