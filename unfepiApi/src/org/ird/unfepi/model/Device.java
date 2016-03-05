package org.ird.unfepi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device")
public class Device  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private int deviceId;
	@Column(name="androidId")
	private String androidId;
	@Column(name="serialId")
	private String serialId;
	@Column(name="macId")
	private String macId;
	@Column(name="lastcount")
	private int lastCount;
	@Column(name="lastSyncDate")
	private Date lastSyncDate;
	
	
	public Device(int deviceId, String androidId, String serialId,
			String macId, int lastCount ,Date lastSyncDate ) {
		super();
		this.deviceId = deviceId;
		this.androidId = androidId;
		this.serialId = serialId;
		this.macId = macId;
		this.lastCount = lastCount;
		this.lastSyncDate=lastSyncDate;
	}
	
	
	public Date getLastSyncDate() {
		return lastSyncDate;
	}


	public void setLastSyncDate(Date lastSyncDate) {
		this.lastSyncDate = lastSyncDate;
	}


	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	public String getMacId() {
		return macId;
	}
	public void setMacId(String macId) {
		this.macId = macId;
	}
	public int getLastCount() {
		return lastCount;
	}
	public void setLastCount(int lastCount) {
		this.lastCount = lastCount;
	}
	
	
}
