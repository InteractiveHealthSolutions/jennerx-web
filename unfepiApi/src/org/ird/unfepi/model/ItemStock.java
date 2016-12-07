package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="itemstock")
public class ItemStock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemRecordNum;
	
	@Column(length = 30)
	private String name;
	
	private Integer quantity;
	private Integer unit_per_pack;
//	private Integer quantity_avaliable;
//	private Integer quantity_distributed;
//	private Integer quantity_damaged;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date date_items_in;
//	
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date date_items_end;
	
	public Integer getItemRecordNum() {
		return itemRecordNum;
	}
	public void setItemRecordNum(Integer itemRecordNum) {
		this.itemRecordNum = itemRecordNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
//	public Integer getQuantity_avaliable() {
//		return quantity_avaliable;
//	}
//	public void setQuantity_avaliable(Integer quantity_avaliable) {
//		this.quantity_avaliable = quantity_avaliable;
//	}
//	public Integer getQuantity_distributed() {
//		return quantity_distributed;
//	}
//	public void setQuantity_distributed(Integer quantity_distributed) {
//		this.quantity_distributed = quantity_distributed;
//	}
//	public Integer getQuantity_damaged() {
//		return quantity_damaged;
//	}
//	public void setQuantity_damaged(Integer quantity_damaged) {
//		this.quantity_damaged = quantity_damaged;
//	}
	public Integer getUnit_per_pack() {
		return unit_per_pack;
	}
	public void setUnit_per_pack(Integer unit_per_pack) {
		this.unit_per_pack = unit_per_pack;
	}
//	public Date getDate_items_in() {
//		return date_items_in;
//	}
//	public void setDate_items_in(Date date_items_in) {
//		this.date_items_in = date_items_in;
//	}
//	public Date getDate_items_end() {
//		return date_items_end;
//	}
//	public void setDate_items_end(Date date_items_end) {
//		this.date_items_end = date_items_end;
//	}
}
