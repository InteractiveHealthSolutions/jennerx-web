package org.ird.unfepi.beans;

import java.util.List;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Identifier;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;

public class EnrollmentWrapper {
	private String childIdentifier;
	private Child child;
	private Address address;
	private Boolean childNamed;
	private String birthdateOrAge;
	private String village;
	private Identifier identifier;
	private String childagey;
	private String childagem;
	private String childagew;
	private String childaged;
	private String completeCourseFromCenter;
	private VaccinationCenterVisit centerVisit;
	private List<Vaccine> vaccines;
	

	public List<Vaccine> getVaccines() {
		return vaccines;
	}
	public void setVaccines(List<Vaccine> vaccines) {
		this.vaccines = vaccines;
	}
	public EnrollmentWrapper() {
	child = new Child();
	identifier=new Identifier();
		address = new Address();
		centerVisit = new VaccinationCenterVisit();
	}
	public String getChildIdentifier() {
		return childIdentifier;
	}
	public void setChildIdentifier(String childIdentifier) {
		this.childIdentifier = childIdentifier;
	}
	public Child getChild() {
		return child;
	}
	public void setChild(Child child) {
		this.child = child;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Boolean getChildNamed () {
		return childNamed;
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
	public String getCompleteCourseFromCenter() {
		return completeCourseFromCenter;
	}
	public void setCompleteCourseFromCenter(String completeCourseFromCenter) {
		this.completeCourseFromCenter = completeCourseFromCenter;
	}
	public VaccinationCenterVisit getCenterVisit() {
		return centerVisit;
	}
	public void setCenterVisit(VaccinationCenterVisit centerVisit) {
		this.centerVisit = centerVisit;
	}
}
