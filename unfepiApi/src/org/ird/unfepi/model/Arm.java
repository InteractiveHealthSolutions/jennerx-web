package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;


@Entity
@Table (name = "arm")
public class Arm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int armId;
	
	private String armName;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "arm_createdByUserId_user_mappedId_FK")
	private User createdByUserId;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "arm_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	public int getArmId() {
		return armId;
	}

	public void setArmId(int armId) {
		this.armId = armId;
	}

	public User getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getArmName() {
		return armName;
	}

	public void setArmName(String armName) {
		this.armName = armName;
	}

	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

}
