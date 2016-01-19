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
	
	private String address1;
	
	private String address2;
	
	private String address3;
	
	private String address4;
	
	private String address5;
	
	private String district;

	private String town;
	
	private String uc;
	
	private String landmark;
	
	private Integer cityId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cityId", insertable = false, updatable = false)
	@ForeignKey(name = "address_cityId_location_locationId_FK")
	private Location city;
	
	private String cityName;
	
	private String province;
	
	private String zipcode;
	
	private String country;
	
	private String lat;
	
	private String lon;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "address_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "address_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
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
	public String getAddress1() {
		return address1;
	}

	/**
	 * Sets the adds the house number.
	 *
	 * @param address1 the new adds the house number
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * Gets the adds the street.
	 *
	 * @return the adds the street
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * Sets the adds the street.
	 *
	 * @param address2 the new adds the street
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * Gets the adds the sector.
	 *
	 * @return the adds the sector
	 */
	public String getAddress3() {
		return address3;
	}

	/**
	 * Sets the adds the sector.
	 *
	 * @param address3 the new adds the sector
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	/**
	 * Gets the adds the area.
	 *
	 * @return the adds the area
	 */
	public String getAddress4() {
		return address4;
	}

	/**
	 * Sets the adds the area.
	 *
	 * @param address4 the new adds the area
	 */
	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	/**
	 * Gets the adds the colony.
	 *
	 * @return the adds the colony
	 */
	public String getAddress5() {
		return address5;
	}

	/**
	 * Sets the adds the colony.
	 *
	 * @param address5 the new adds the colony
	 */
	public void setAddress5(String address5) {
		this.address5 = address5;
	}

	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getUc() {
		return uc;
	}
	public void setUc(String uc) {
		this.uc = uc;
	}
	public String getLandmark() {
		return landmark;
	}
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Location getCity() {
		return city;
	}
	void setCity(Location city) {
		this.city = city;
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
	
}
