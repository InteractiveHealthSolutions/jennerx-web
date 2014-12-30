package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "downloadablereport")
public class DownloadableReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int downloadableId;
	
	@Column(unique = true, nullable = false, length=100)
	private String downloadableName;
	
	@Column(unique = true, nullable = false)
	private String downloadablePath;
	
	private String downloadableType;
	
	private int sizeBytes;
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "downloadablereport_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	public DownloadableReport() {
		// TODO Auto-generated constructor stub
	}

	public int getDownloadableId() {
		return downloadableId;
	}

	public void setDownloadableId(int downloadableId) {
		this.downloadableId = downloadableId;
	}

	public String getDownloadableName() {
		return downloadableName;
	}

	public void setDownloadableName(String downloadableName) {
		this.downloadableName = downloadableName;
	}

	public String getDownloadablePath() {
		return downloadablePath;
	}

	public void setDownloadablePath(String downloadablePath) {
		this.downloadablePath = downloadablePath;
	}

	public String getDownloadableType() {
		return downloadableType;
	}

	public void setDownloadableType(String downloadableType) {
		this.downloadableType = downloadableType;
	}

	public int getSizeBytes() {
		return sizeBytes;
	}

	public void setSizeBytes(int sizeBytes) {
		this.sizeBytes = sizeBytes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
}
