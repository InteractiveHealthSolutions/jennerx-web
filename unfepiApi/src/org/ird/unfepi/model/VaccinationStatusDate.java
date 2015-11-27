/**
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

/**
 * @author Safwan
 *
 */
public class VaccinationStatusDate {
	
	private String name;
	
	private Date date;
	
	public VaccinationStatusDate(String name, Date date){
		this.name = name;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
