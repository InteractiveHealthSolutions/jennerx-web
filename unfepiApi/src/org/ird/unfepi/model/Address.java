/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
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
import org.ird.unfepi.model.Model.ContactType;

/**
 * The Class Address.
 *
 * @author maimoonak
 */
@Entity
@Table(name = "address")
public class Address {

	/** The address id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int addressId;
	
	private Integer mappedId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false, nullable = false)
	@ForeignKey(name = "address_mappedId_IdMapper_mappedId_FK")
	private IdMapper idMapper;

	/** The address type. */
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ContactType addressType;
	
	/** The add house number. */
	@Column(length = 30)
	private String addHouseNumber;
	
	/** The add street. */
	@Column(length = 30)
	private String addStreet;
	
	/** The add sector. */
	@Column(length = 30)
	private String addSector;
	
	/** The add area. */
	@Column(length = 30)
	private String addArea;
	
	/** The add district. */
	@Column(length = 30)
	private String addDistrict;
	
	/** The add colony. */
	@Column(length = 30)
	private String addColony;
	
	@Column(length = 50)
	private String addtown;
	
	@Column(length = 30)
	private String addUc;
	
	/** The add landmark. */
	@Column(length = 50)
	private String addLandmark;
	
	private Integer cityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityId", insertable = false, updatable = false)
	@ForeignKey(name = "address_cityId_location_locationId_FK")
	private Location city;
	
	/** The epi id. */
	@Column(length = 30)
	private String cityName;
	
	/** The province. */
	@Column(length = 30)
	private String province;
	
	/** The zipcode. */
	@Column(length = 30)
	private String zipcode;
	
	/** The country. */
	@Column(length = 30)
	private String country;
	
	/** The region. */
	@Column(length = 30)
	private String region;
	
	/** The lat. */
	private Double lat;
	
	/** The lon. */
	private Double lon;
	
	/** The phone1. */
	@Column(length = 20)
	private String phone1;
	
	/** The phone1 owner. */
	@Column(length = 30)
	private String phone1Owner;
	
	/** The phone2. */
	@Column(length = 20)
	private String phone2;
	
	/** The phone2 owner. */
	@Column(length = 30)
	private String phone2Owner;
	
	/** The weburl. */
	@Column(length = 100)
	private String weburl;
	
	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "address_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "address_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public Address() {
		
	}
	/**
	 * 
	 * Gets the address id.
	 *
	 * @return the address id
	 */
	public int getAddressId() {
		return addressId;
	}

	/**
	 * Sets the address id.
	 *
	 * @param addressId the new address id
	 */
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public Integer getMappedId() {
		return mappedId;
	}
	public void setMappedId(Integer mappedId) {
		this.mappedId = mappedId;
	}
	public IdMapper getIdMapper() {
		return idMapper;
	}

	void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	/**
	 * Gets the address type.
	 *
	 * @return the address type
	 */
	public ContactType getAddressType() {
		return addressType;
	}

	/**
	 * Sets the address type.
	 *
	 * @param addressType the new address type
	 */
	public void setAddressType(ContactType addressType) {
		this.addressType = addressType;
	}

	/**
	 * Gets the adds the house number.
	 *
	 * @return the adds the house number
	 */
	public String getAddHouseNumber() {
		return addHouseNumber;
	}

	/**
	 * Sets the adds the house number.
	 *
	 * @param addHouseNumber the new adds the house number
	 */
	public void setAddHouseNumber(String addHouseNumber) {
		this.addHouseNumber = addHouseNumber;
	}

	/**
	 * Gets the adds the street.
	 *
	 * @return the adds the street
	 */
	public String getAddStreet() {
		return addStreet;
	}

	/**
	 * Sets the adds the street.
	 *
	 * @param addStreet the new adds the street
	 */
	public void setAddStreet(String addStreet) {
		this.addStreet = addStreet;
	}

	/**
	 * Gets the adds the sector.
	 *
	 * @return the adds the sector
	 */
	public String getAddSector() {
		return addSector;
	}

	/**
	 * Sets the adds the sector.
	 *
	 * @param addSector the new adds the sector
	 */
	public void setAddSector(String addSector) {
		this.addSector = addSector;
	}

	/**
	 * Gets the adds the area.
	 *
	 * @return the adds the area
	 */
	public String getAddArea() {
		return addArea;
	}

	/**
	 * Sets the adds the area.
	 *
	 * @param addArea the new adds the area
	 */
	public void setAddArea(String addArea) {
		this.addArea = addArea;
	}

	/**
	 * Gets the adds the district.
	 *
	 * @return the adds the district
	 */
	public String getAddDistrict() {
		return addDistrict;
	}

	/**
	 * Sets the adds the district.
	 *
	 * @param addDistrict the new adds the district
	 */
	public void setAddDistrict(String addDistrict) {
		this.addDistrict = addDistrict;
	}

	/**
	 * Gets the adds the colony.
	 *
	 * @return the adds the colony
	 */
	public String getAddColony() {
		return addColony;
	}

	/**
	 * Sets the adds the colony.
	 *
	 * @param addColony the new adds the colony
	 */
	public void setAddColony(String addColony) {
		this.addColony = addColony;
	}

	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getAddtown() {
		return addtown;
	}
	public void setAddtown(String addtown) {
		this.addtown = addtown;
	}
	public String getAddUc() {
		return addUc;
	}
	public void setAddUc(String addUc) {
		this.addUc = addUc;
	}
	public Location getCity() {
		return city;
	}
	void setCity(Location city) {
		this.city = city;
	}
	/**
	 * Gets the adds the landmark.
	 *
	 * @return the adds the landmark
	 */
	public String getAddLandmark() {
		return addLandmark;
	}

	/**
	 * Sets the adds the landmark.
	 *
	 * @param addLandmark the new adds the landmark
	 */
	public void setAddLandmark(String addLandmark) {
		this.addLandmark = addLandmark;
	}

	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * Gets the province.
	 *
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * Sets the province.
	 *
	 * @param province the new province
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * Gets the zipcode.
	 *
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * Sets the zipcode.
	 *
	 * @param zipcode the new zipcode
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 *
	 * @param country the new country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Gets the region.
	 *
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the region.
	 *
	 * @param region the new region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * Gets the phone1.
	 *
	 * @return the phone1
	 */
	public String getPhone1() {
		return phone1;
	}

	/**
	 * Sets the phone1.
	 *
	 * @param phone1 the new phone1
	 */
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	/**
	 * Gets the phone1 owner.
	 *
	 * @return the phone1 owner
	 */
	public String getPhone1Owner() {
		return phone1Owner;
	}

	/**
	 * Sets the phone1 owner.
	 *
	 * @param phone1Owner the new phone1 owner
	 */
	public void setPhone1Owner(String phone1Owner) {
		this.phone1Owner = phone1Owner;
	}

	/**
	 * Gets the phone2.
	 *
	 * @return the phone2
	 */
	public String getPhone2() {
		return phone2;
	}

	/**
	 * Sets the phone2.
	 *
	 * @param phone2 the new phone2
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * Gets the phone2 owner.
	 *
	 * @return the phone2 owner
	 */
	public String getPhone2Owner() {
		return phone2Owner;
	}

	/**
	 * Sets the phone2 owner.
	 *
	 * @param phone2Owner the new phone2 owner
	 */
	public void setPhone2Owner(String phone2Owner) {
		this.phone2Owner = phone2Owner;
	}

	/**
	 * Gets the weburl.
	 *
	 * @return the weburl
	 */
	public String getWeburl() {
		return weburl;
	}

	/**
	 * Sets the weburl.
	 *
	 * @param weburl the new weburl
	 */
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 */
	public User getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 */
	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 */
	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	/**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 */
	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	/**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 */
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	/**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 */
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}

	
	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	/**
	 * Sets the editor.
	 *
	 * @param editor the new editor
	 */
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
	
/*	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(getClass().getName());
		s.append("[");
		s.append(getAddressId());
		s.append(getAddressType());
		s.append(getAddArea());
		s.append(getAddColony());
		s.append(getAddDistrict());
		s.append(getAddHouseNumber());
		s.append(getAddLandmark());
		s.append(getAddSector());
		s.append(getAddStreet());
		s.append(getAddTown());
		s.append(getAddUc());
		s.append(getCity());
		s.append(getCountry());
		s.append(getCreatedByUserId());
		s.append(getCreatedByUserName());
		s.append(getDescription());
		s.append(get);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		s.append(arg0);
		
		int i = s.lastIndexOf(";");
		if (i != -1)
			s.deleteCharAt(i);

		s.append("]");

		return s.toString();
	}*/
}
