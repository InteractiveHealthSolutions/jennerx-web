package org.ird.unfepi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "dailysummary")
public class DailySummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private Integer serialNumber;
	
	@Column(nullable = false)
	private Date summaryDate;
	
	@Column(nullable = false)
	private Integer vaccinationCenterId;
	
	/** The vaccination center. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinationCenterId", insertable = false, updatable = false)
	@ForeignKey(name = "dailysummary_vaccinationCenterId_vaccinationcenter_mappedId_FK")
	private VaccinationCenter	vaccinationCenter;
	
	@Column(nullable = false)
	private Integer vaccinatorId;
	
	/** The vaccinator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccinatorId", insertable = false, updatable = false)
	@ForeignKey(name = "dailysummary_vaccinatorId_vaccinator_mappedId_FK")
	private Vaccinator	vaccinator;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity=DailySummaryVaccineGiven.class)
	@JoinColumn(name = "dailySummaryId")
	@ForeignKey(name = "dsum_serialNumber_dailysumvacgvn_dailySummaryId_FK")
	private List<DailySummaryVaccineGiven> dailySummaryVaccineGiven=new ArrayList<DailySummaryVaccineGiven>();
	
	private Integer bcgVisited;
	
	private Integer bcgEnrolledTotal;
	
	private Integer bcgEnrolledWithReminder;
	
	private Integer bcgEnrolledWithLottery;
	
	private Integer bcgEnrolledWithBoth;
	
	private Integer bcgEnrolledWithNone;
	
	private Integer penta1Visited;

	private Integer penta1EnrolledTotal;
	
	private Integer penta1EnrolledWithReminder;
	
	private Integer penta1EnrolledWithLottery;
	
	private Integer penta1EnrolledWithBoth;
	
	private Integer penta1EnrolledWithNone;
	
	private Integer penta1Followuped;
	
	private Integer penta2Visited;
	
	private Integer penta2Followuped;
	
	private Integer penta3Visited;
	
	private Integer penta3Followuped;
	
	private Integer measles1Visited;
	
	private Integer measles1Followuped;
	
	private Integer measles2Visited;
	
	private Integer measles2Followuped;
	
	private Integer opvGivenTotal;

	private Integer ttGivenTotal;

	private Integer totalEnrolled;
	
	private Integer totalFollowuped;
	
	private Integer totalVisited;
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "dailysummary_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "dailysummary_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	public DailySummary() {
		
	}
	
	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getSummaryDate() {
		return summaryDate;
	}

	public void setSummaryDate(Date summaryDate) {
		this.summaryDate = summaryDate;
	}

	public Integer getVaccinationCenterId() {
		return vaccinationCenterId;
	}

	public void setVaccinationCenterId(Integer vaccinationCenterId) {
		this.vaccinationCenterId = vaccinationCenterId;
	}

	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public Integer getVaccinatorId() {
		return vaccinatorId;
	}

	public void setVaccinatorId(Integer vaccinatorId) {
		this.vaccinatorId = vaccinatorId;
	}

	public Vaccinator getVaccinator() {
		return vaccinator;
	}

	public void setVaccinator(Vaccinator vaccinator) {
		this.vaccinator = vaccinator;
	}

	public List<DailySummaryVaccineGiven> getDailySummaryVaccineGiven() {
		return dailySummaryVaccineGiven;
	}

	public void setDailySummaryVaccineGiven(List<DailySummaryVaccineGiven> dailySummaryVaccineGiven) {
		this.dailySummaryVaccineGiven = dailySummaryVaccineGiven;
	}

	public Integer getBcgVisited() {
		return bcgVisited;
	}

	public void setBcgVisited(Integer bcgVisited) {
		this.bcgVisited = bcgVisited;
	}

	public Integer getBcgEnrolledTotal() {
		return bcgEnrolledTotal;
	}

	public void setBcgEnrolledTotal(Integer bcgEnrolledtotal) {
		this.bcgEnrolledTotal = bcgEnrolledtotal;
	}

	public Integer getBcgEnrolledWithReminder() {
		return bcgEnrolledWithReminder;
	}

	public void setBcgEnrolledWithReminder(Integer bcgEnrolledWithReminder) {
		this.bcgEnrolledWithReminder = bcgEnrolledWithReminder;
	}

	public Integer getBcgEnrolledWithLottery() {
		return bcgEnrolledWithLottery;
	}

	public void setBcgEnrolledWithLottery(Integer bcgEnrolledWithLottery) {
		this.bcgEnrolledWithLottery = bcgEnrolledWithLottery;
	}

	public Integer getBcgEnrolledWithBoth() {
		return bcgEnrolledWithBoth;
	}

	public void setBcgEnrolledWithBoth(Integer bcgEnrolledWithBoth) {
		this.bcgEnrolledWithBoth = bcgEnrolledWithBoth;
	}

	public Integer getBcgEnrolledWithNone() {
		return bcgEnrolledWithNone;
	}

	public void setBcgEnrolledWithNone(Integer bcgEnrolledWithNone) {
		this.bcgEnrolledWithNone = bcgEnrolledWithNone;
	}

	public Integer getPenta1Visited() {
		return penta1Visited;
	}

	public void setPenta1Visited(Integer penta1Visited) {
		this.penta1Visited = penta1Visited;
	}

	public Integer getPenta1EnrolledTotal() {
		return penta1EnrolledTotal;
	}

	public void setPenta1EnrolledTotal(Integer penta1Enrolledtotal) {
		this.penta1EnrolledTotal = penta1Enrolledtotal;
	}

	public Integer getPenta1EnrolledWithReminder() {
		return penta1EnrolledWithReminder;
	}

	public void setPenta1EnrolledWithReminder(Integer penta1EnrolledWithReminder) {
		this.penta1EnrolledWithReminder = penta1EnrolledWithReminder;
	}

	public Integer getPenta1EnrolledWithLottery() {
		return penta1EnrolledWithLottery;
	}

	public void setPenta1EnrolledWithLottery(Integer penta1EnrolledWithLottery) {
		this.penta1EnrolledWithLottery = penta1EnrolledWithLottery;
	}

	public Integer getPenta1EnrolledWithBoth() {
		return penta1EnrolledWithBoth;
	}

	public void setPenta1EnrolledWithBoth(Integer penta1EnrolledWithBoth) {
		this.penta1EnrolledWithBoth = penta1EnrolledWithBoth;
	}

	public Integer getPenta1EnrolledWithNone() {
		return penta1EnrolledWithNone;
	}

	public void setPenta1EnrolledWithNone(Integer penta1EnrolledWithNone) {
		this.penta1EnrolledWithNone = penta1EnrolledWithNone;
	}

	public Integer getPenta1Followuped() {
		return penta1Followuped;
	}

	public void setPenta1Followuped(Integer penta1Followuped) {
		this.penta1Followuped = penta1Followuped;
	}

	public Integer getPenta2Visited() {
		return penta2Visited;
	}

	public void setPenta2Visited(Integer penta2Visited) {
		this.penta2Visited = penta2Visited;
	}

	public Integer getPenta2Followuped() {
		return penta2Followuped;
	}

	public void setPenta2Followuped(Integer penta2Followuped) {
		this.penta2Followuped = penta2Followuped;
	}

	public Integer getPenta3Visited() {
		return penta3Visited;
	}

	public void setPenta3Visited(Integer penta3Visited) {
		this.penta3Visited = penta3Visited;
	}

	public Integer getPenta3Followuped() {
		return penta3Followuped;
	}

	public void setPenta3Followuped(Integer penta3Followuped) {
		this.penta3Followuped = penta3Followuped;
	}

	public Integer getMeasles1Visited() {
		return measles1Visited;
	}

	public void setMeasles1Visited(Integer measles1Visited) {
		this.measles1Visited = measles1Visited;
	}

	public Integer getMeasles1Followuped() {
		return measles1Followuped;
	}

	public void setMeasles1Followuped(Integer measles1Followuped) {
		this.measles1Followuped = measles1Followuped;
	}

	public Integer getMeasles2Visited() {
		return measles2Visited;
	}

	public void setMeasles2Visited(Integer measles2Visited) {
		this.measles2Visited = measles2Visited;
	}

	public Integer getMeasles2Followuped() {
		return measles2Followuped;
	}

	public void setMeasles2Followuped(Integer measles2Followuped) {
		this.measles2Followuped = measles2Followuped;
	}

	public Integer getOpvGivenTotal() {
		return opvGivenTotal;
	}

	public void setOpvGivenTotal(Integer opvGivenTotal) {
		this.opvGivenTotal = opvGivenTotal;
	}

	public Integer getTtGivenTotal() {
		return ttGivenTotal;
	}

	public void setTtGivenTotal(Integer ttGivenTotal) {
		this.ttGivenTotal = ttGivenTotal;
	}

	public Integer getTotalEnrolled() {
		return totalEnrolled;
	}

	public void setTotalEnrolled(Integer totalEnrolled) {
		this.totalEnrolled = totalEnrolled;
	}

	public Integer getTotalFollowuped() {
		return totalFollowuped;
	}

	public void setTotalFollowuped(Integer totalFollowuped) {
		this.totalFollowuped = totalFollowuped;
	}

	public Integer getTotalVisited() {
		return totalVisited;
	}

	public void setTotalVisited(Integer totalVisited) {
		this.totalVisited = totalVisited;
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

	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}

	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
}
