package org.ird.unfepi.model;
/*package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table (name = "arm_id_map")
public class ArmIdMap {

	@Id
	@Column (name = "child_id_to_map")
	private int	childIdToMap;
	
	@OneToOne (targetEntity = Arm.class,fetch = FetchType.EAGER )
	@JoinColumn(name = "arm_id")
	private Arm		arm;
	
	@Column (name = "is_child_id_occupied")
	private Boolean	isChildIdOccupied;		// occupied then is non editable
	
	@Column (name = "is_mapping_editable")
	private Boolean	isMappingEditable;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "created_by_user_id")
	private String createdByUserId;
	
	@Column(name = "created_by_user_name")
	private String createdByUserName;
	
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_edited_by_user_id")
	private String lastEditedByUserId;
	
	@Column(name = "last_edited_by_user_name")
	private String lastEditedByUserName;
	
	
	@Column(name = "last_updated")
	private Date lastUpdated;
	
	public void setchildIdToMap(int childIdToMap) {
		this.childIdToMap = childIdToMap;
	}
	public int getchildIdToMap() {
		return childIdToMap;
	}
	public void setArm(Arm arm) {
		this.arm = arm;
	}
	public Arm getArm() {
		return arm;
	}
	public void setIsChildIdOccupied(Boolean isChildIdOccupied) {
		this.isChildIdOccupied = isChildIdOccupied;
	}
	public Boolean getIsChildIdOccupied() {
		return isChildIdOccupied;
	}
	public void setIsMappingEditable(Boolean isMappingEditable) {
		this.isMappingEditable = isMappingEditable;
	}
	public Boolean getIsMappingEditable() {
		return isMappingEditable;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public String getCreatedByUserId() {
		return this.createdByUserId;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getCreatedByUserName() {
		return this.createdByUserName;
	}
	public void setLastEditor(User editor){
		lastEditedByUserId=editor.getName();
		lastEditedByUserName=editor.getFullName();
		lastUpdated=new Date();
	}
	public void setCreator(User creator){
		createdByUserId=creator.getName();
		createdByUserName=creator.getFullName();
		createdDate=new Date();
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastEditedByUserId() {
		return this.lastEditedByUserId;
	}

	public void setLastEditedByUserId(String lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public String getLastEditedByUserName() {
		return this.lastEditedByUserName;
	}

	public void setLastEditedByUserName(String lastEditedByUserName) {
		this.lastEditedByUserName = lastEditedByUserName;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
*/