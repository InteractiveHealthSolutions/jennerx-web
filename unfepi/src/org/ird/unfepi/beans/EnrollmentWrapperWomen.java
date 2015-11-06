/**
 * 
 */
package org.ird.unfepi.beans;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.web.utils.WomenVaccinationCenterVisit;

/**
 * @author Safwan
 *
 */
public class EnrollmentWrapperWomen {
	
	private String projectId;
	private Women women;
	private Address address;
	private String birthdateOrAge;
	private String womenNamed;
	private String womenagey;
	private String womenagem;
	private String womenagew;
	private String womenaged;
	private String completeCourseFromCenter;
	private WomenVaccinationCenterVisit centerVisit;
	private Vaccine vaccine;
	
	public EnrollmentWrapperWomen() {
		women = new Women();
		address = new Address();
		centerVisit = new WomenVaccinationCenterVisit();
		vaccine = new Vaccine();
	}
	
	
	public String getProjectId() {
		return projectId;
	}
	
	
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	
	public Women getWomen() {
		return women;
	}
	
	
	public void setWomen(Women women) {
		this.women = women;
	}
	
	
	public Address getAddress() {
		return address;
	}
	
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	public String getBirthdateOrAge() {
		return birthdateOrAge;
	}
	
	
	public void setBirthdateOrAge(String birthdateOrAge) {
		this.birthdateOrAge = birthdateOrAge;
	}
	
	
	public String getWomenNamed() {
		return womenNamed;
	}
	
	
	public void setWomenNamed(String womenNamed) {
		this.womenNamed = womenNamed;
	}
	
	
	public String getwomenagey() {
		return womenagey;
	}
	
	
	public void setwomenagey(String womenagey) {
		this.womenagey = womenagey;
	}
	
	
	public String getwomenagem() {
		return womenagem;
	}
	
	
	public void setwomenagem(String womenagem) {
		this.womenagem = womenagem;
	}
	
	
	public String getwomenagew() {
		return womenagew;
	}
	
	
	public void setwomenagew(String womenagew) {
		this.womenagew = womenagew;
	}
	
	
	public String getwomenaged() {
		return womenaged;
	}
	
	
	public void setwomenaged(String womenaged) {
		this.womenaged = womenaged;
	}
	
	
	public String getCompleteCourseFromCenter() {
		return completeCourseFromCenter;
	}
	
	
	public void setCompleteCourseFromCenter(String completeCourseFromCenter) {
		this.completeCourseFromCenter = completeCourseFromCenter;
	}
	
	
	public WomenVaccinationCenterVisit getCenterVisit() {
		return centerVisit;
	}
	
	
	public void setCenterVisit(WomenVaccinationCenterVisit centerVisit) {
		this.centerVisit = centerVisit;
	}


	public Vaccine getVaccine() {
		return vaccine;
	}


	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

}
