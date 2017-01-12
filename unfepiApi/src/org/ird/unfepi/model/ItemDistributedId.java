package org.ird.unfepi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ItemDistributedId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int	mappedId;
	
	@Column(columnDefinition="DATE")
	private Date distributedDate;
	
	private Integer itemRecordNum;
	
	public int getMappedId() {
		return mappedId;
	}
	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}
	public Date getDistributedDate() {
		return distributedDate;
	}
	public void setDistributedDate(Date distributedDate) {
		this.distributedDate = distributedDate;
	}
	public Integer getItemRecordNum() {
		return itemRecordNum;
	}
	public void setItemRecordNum(Integer itemRecordNum) {
		this.itemRecordNum = itemRecordNum;
	}
	
}
