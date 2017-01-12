package org.ird.unfepi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MuacMeasurementId  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int	mappedId;
	
	@Column(columnDefinition="DATE")
	private Date measureDate;
	
	public int getMappedId() {
		return mappedId;
	}
	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}
	public Date getMeasureDate() {
		return measureDate;
	}
	public void setMeasureDate(Date measureDate) {
		this.measureDate = measureDate;
	}

}
