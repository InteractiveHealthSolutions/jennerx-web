package org.ird.unfepi.beans;

import org.ird.unfepi.model.Address;

public class RegistrationWrapper {
	private String projectId;
	private Object entity;
	private String birthdateOrAge;
	private String agey;
	private String agem;
	private String agew;
	private String aged;
	private Address address;
	private String contactPrimary;
	private String contactSecondary;
	
	public RegistrationWrapper() {
	}
	
	public RegistrationWrapper(Object entity) {
		entity = new Object();
		address = new Address();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public String getBirthdateOrAge() {
		return birthdateOrAge;
	}

	public void setBirthdateOrAge(String birthdateOrAge) {
		this.birthdateOrAge = birthdateOrAge;
	}

	public String getAgey() {
		return agey;
	}

	public void setAgey(String agey) {
		this.agey = agey;
	}

	public String getAgem() {
		return agem;
	}

	public void setAgem(String agem) {
		this.agem = agem;
	}

	public String getAgew() {
		return agew;
	}

	public void setAgew(String agew) {
		this.agew = agew;
	}

	public String getAged() {
		return aged;
	}

	public void setAged(String aged) {
		this.aged = aged;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getContactPrimary() {
		return contactPrimary;
	}

	public void setContactPrimary(String contactPrimary) {
		this.contactPrimary = contactPrimary;
	}

	public String getContactSecondary() {
		return contactSecondary;
	}

	public void setContactSecondary(String contactSecondary) {
		this.contactSecondary = contactSecondary;
	}
}
