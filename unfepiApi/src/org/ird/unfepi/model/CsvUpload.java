/*
 * 
 */
package org.ird.unfepi.model;

import java.util.Date;

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

/**
 * The Class CsvUpload.
 */
@Entity 
@Table(name = "csvupload")
public class CsvUpload {
	
	/** The record number. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int recordNumber;
	
	/** The description. */
	private String description;
	
	/** The csv file. */
	private String csvFile;
	
	/** The number of rows. */
	private int numberOfRows;
	
	/** The number of rows rejected. */
	private int numberOfRowsRejected;
	
	/** The number of rows saved. */
	private int numberOfRowsSaved;
	
	/** The upload errors. */
	private String uploadErrors;
	
	/** The upload report. */
	private String uploadReport;
	
	/** The uploaded by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "EditorUserIdFK")
	private User uploadedByUserId;
	
	/** The date uploaded. */
	@Temporal (TemporalType.TIMESTAMP)
	private Date dateUploaded;

	public CsvUpload() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Sets the record number.
	 *
	 * @param recordNumber the new record number
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
	
	/**
	 * Gets the record number.
	 *
	 * @return the record number
	 */
	public int getRecordNumber() {
		return recordNumber;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the date uploaded.
	 *
	 * @param dateUploaded the new date uploaded
	 */
	public void setDateUploaded(Date dateUploaded) {
		this.dateUploaded = dateUploaded;
	}
	
	/**
	 * Gets the date uploaded.
	 *
	 * @return the date uploaded
	 */
	public Date getDateUploaded() {
		return dateUploaded;
	}
	
	/**
	 * Sets the csv file.
	 *
	 * @param csvFile the new csv file
	 */
	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}
	
	/**
	 * Gets the csv file.
	 *
	 * @return the csv file
	 */
	public String getCsvFile() {
		return csvFile;
	}
	
	/**
	 * Sets the number of rows.
	 *
	 * @param numberOfRows the new number of rows
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	/**
	 * Gets the number of rows.
	 *
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * Sets the uploader.
	 *
	 * @param creator the new uploader
	 */
	public void setUploader(User creator){
		uploadedByUserId=creator;
		dateUploaded=new Date();
	}
	
	/**
	 * Sets the number of rows rejected.
	 *
	 * @param numberOfRowsRejected the new number of rows rejected
	 */
	public void setNumberOfRowsRejected(int numberOfRowsRejected) {
		this.numberOfRowsRejected = numberOfRowsRejected;
	}
	
	/**
	 * Gets the number of rows rejected.
	 *
	 * @return the number of rows rejected
	 */
	public int getNumberOfRowsRejected() {
		return numberOfRowsRejected;
	}
	
	/**
	 * Sets the number of rows saved.
	 *
	 * @param numberOfRowsSaved the new number of rows saved
	 */
	public void setNumberOfRowsSaved(int numberOfRowsSaved) {
		this.numberOfRowsSaved = numberOfRowsSaved;
	}
	
	/**
	 * Gets the number of rows saved.
	 *
	 * @return the number of rows saved
	 */
	public int getNumberOfRowsSaved() {
		return numberOfRowsSaved;
	}
	
	/**
	 * Sets the upload report.
	 *
	 * @param uploadReport the new upload report
	 */
	public void setUploadReport(String uploadReport) {
		this.uploadReport = uploadReport;
	}
	
	/**
	 * Gets the upload report.
	 *
	 * @return the upload report
	 */
	public String getUploadReport() {
		return uploadReport;
	}
	
	/**
	 * Sets the uploaded by user id.
	 *
	 * @param uploadedByUserId the new uploaded by user id
	 */
	public void setUploadedByUserId(User uploadedByUserId) {
		this.uploadedByUserId = uploadedByUserId;
	}
	
	/**
	 * Gets the uploaded by user id.
	 *
	 * @return the uploaded by user id
	 */
	public User getUploadedByUserId() {
		return uploadedByUserId;
	}
	
	/**
	 * Sets the upload errors.
	 *
	 * @param uploadErrors the new upload errors
	 */
	public void setUploadErrors(String uploadErrors) {
		this.uploadErrors = uploadErrors;
	}
	
	/**
	 * Gets the upload errors.
	 *
	 * @return the upload errors
	 */
	public String getUploadErrors() {
		return uploadErrors;
	}
}
