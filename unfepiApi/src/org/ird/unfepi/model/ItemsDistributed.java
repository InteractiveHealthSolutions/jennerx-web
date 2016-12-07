package org.ird.unfepi.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="itemsdistributed")
public class ItemsDistributed{
	
	@EmbeddedId
	private ItemDistributedId itemDistributedId;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Child.class)
	@JoinColumn(name = "mappedId", insertable = false, updatable = false)
	@ForeignKey(name = "itemsdistributed_mappedId_child_mappedId_FK")
	private Child child;
	
	private Integer itemRecordNum;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ItemStock.class)
	@JoinColumn(name = "itemRecordNum", insertable = false, updatable = false)
	@ForeignKey(name = "itemsdistributed_itemRecordNum_itemstock_itemRecordNum_FK")
	private ItemStock itemStock;
	
	private Integer quantity;

	/**
	 * @return the itemDistributedId
	 */
	public ItemDistributedId getItemDistributedId() {
		return itemDistributedId;
	}

	/**
	 * @param itemDistributedId the itemDistributedId to set
	 */
	public void setItemDistributedId(ItemDistributedId itemDistributedId) {
		this.itemDistributedId = itemDistributedId;
	}

	/**
	 * @return the child
	 */
	public Child getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Child child) {
		this.child = child;
	}

	/**
	 * @return the itemRecordNum
	 */
	public Integer getItemRecordNum() {
		return itemRecordNum;
	}

	/**
	 * @param itemRecordNum the itemRecordNum to set
	 */
	public void setItemRecordNum(Integer itemRecordNum) {
		this.itemRecordNum = itemRecordNum;
	}

	/**
	 * @return the itemStock
	 */
	public ItemStock getItemStock() {
		return itemStock;
	}

	/**
	 * @param itemStock the itemStock to set
	 */
	public void setItemStock(ItemStock itemStock) {
		this.itemStock = itemStock;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
