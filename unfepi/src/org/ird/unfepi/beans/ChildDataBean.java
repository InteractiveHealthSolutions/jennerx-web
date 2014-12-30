package org.ird.unfepi.beans;

import java.util.List;
import java.util.UUID;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Vaccination;

public class ChildDataBean {
	private String projectId;
	private Child child;
	private Address address;
	private Boolean childNamed;
	private String birthdateOrAge;
	private String childagey;
	private String childagem;
	private String childagew;
	private String childaged;
	private String contactPrimary;
	private String contactSecondary;
	private LotterySms preference = new LotterySms();
	private List<Vaccination> vaccinations;
	private String uuid;
	
	public ChildDataBean() {
		child = new Child();
		birthdateOrAge = "birthdate";
		setAddress(new Address());
		setUuid(UUID.randomUUID().toString());
	}
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Child getChild() {
		return child;
	}
	public void setChild(Child child) {
		this.child = child;
	}
	public Boolean getChildNamed () {
		return childNamed == null?!child.getFirstName().trim().equalsIgnoreCase(""):childNamed;
	}
	public void setChildNamed (Boolean childNamed) {
		this.childNamed = childNamed;
	}
	public String getBirthdateOrAge () {
		return birthdateOrAge;
	}
	public void setBirthdateOrAge (String birthdateOrAge) {
		this.birthdateOrAge = birthdateOrAge;
	}
	public String getChildagey() {
		return childagey;
	}
	public void setChildagey(String childagey) {
		this.childagey = childagey;
	}
	public String getChildagem() {
		return childagem;
	}
	public void setChildagem(String childagem) {
		this.childagem = childagem;
	}
	public String getChildagew() {
		return childagew;
	}
	public void setChildagew(String childagew) {
		this.childagew = childagew;
	}
	public String getChildaged() {
		return childaged;
	}
	public void setChildaged(String childaged) {
		this.childaged = childaged;
	}
	public LotterySms getPreference() {
		return preference;
	}
	public void setPreference(LotterySms preference) {
		this.preference = preference;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<Vaccination> getVaccinations() {
		return vaccinations;
	}

	public void setVaccinations(List<Vaccination> vaccinations) {
		this.vaccinations = vaccinations;
	}
}
