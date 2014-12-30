package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "storekeeperincentiveevent")
public class StorekeeperIncentiveEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int storekeeperIncentiveEventId;
	
	private Date dateOfEvent;
	
	private Date dataRangeDateLower;
	
	private Date dataRangeDateUpper;
	
	public StorekeeperIncentiveEvent() {
		
	}

	public int getStorekeeperIncentiveEventId() {
		return storekeeperIncentiveEventId;
	}

	public void setStorekeeperIncentiveEventId(int storekeeperIncentiveEventId) {
		this.storekeeperIncentiveEventId = storekeeperIncentiveEventId;
	}

	public Date getDateOfEvent() {
		return dateOfEvent;
	}

	public void setDateOfEvent(Date dateOfEvent) {
		this.dateOfEvent = dateOfEvent;
	}

	public Date getDataRangeDateLower() {
		return dataRangeDateLower;
	}

	public void setDataRangeDateLower(Date dataRangeDateLower) {
		this.dataRangeDateLower = dataRangeDateLower;
	}

	public Date getDataRangeDateUpper() {
		return dataRangeDateUpper;
	}

	public void setDataRangeDateUpper(Date dataRangeDateUpper) {
		this.dataRangeDateUpper = dataRangeDateUpper;
	}
}
