package org.ird.unfepi.beans;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;

public class EnrollmentWrapper {
	private String projectId;
	private Child child;
	private Address address;
	private Boolean childNamed;
	private String birthdateOrAge;
	private String childagey;
	private String childagem;
	private String childagew;
	private String childaged;
	private String completeCourseFromCenter;
	private VaccinationCenterVisit centerVisit;
	

	public EnrollmentWrapper() {
		child = new Child();
		address = new Address();
		centerVisit = new VaccinationCenterVisit();
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
