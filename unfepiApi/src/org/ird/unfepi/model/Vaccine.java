package org.ird.unfepi.model;

import java.util.Date;
import java.util.Set;

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
@Table (name = "vaccine")
public class Vaccine implements java.io.Serializable {

	private static final long serialVersionUID = 9015418858974117077L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short vaccineId;
	
	@Column(length = 30, unique = true)
	private String name;

	@Column(length = 50)
	private String fullName;
	
	@Column(length = 30)
	private String shortName;
	
	private Byte minGracePeriodDays;
	
	private Byte maxGracePeriodDays;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = VaccinePrerequisite.class)
	@JoinColumn(name="vaccineId")
	private Set<VaccinePrerequisite> prerequisites;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vaccine")
	private Set<VaccineGap> vaccineGaps;
	
	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "vaccine_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "vaccine_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	private Boolean voided;

	private String voidReason;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "voidedByUserId")
	@ForeignKey(name = "vaccine_voidedByUserId_user_mappedId_FK")
	private User voidedByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date voidedDate;
	
	public Vaccine() {
	}

	public Vaccine(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public short getVaccineId() {
		return vaccineId;
	}

	public void setVaccineId(short vaccineId) {
		this.vaccineId = vaccineId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Byte getMinGracePeriodDays() {
		return minGracePeriodDays;
	}

	public void setMinGracePeriodDays(Byte minGracePeriodDays) {
		this.minGracePeriodDays = minGracePeriodDays;
	}

	public Byte getMaxGracePeriodDays() {
		return maxGracePeriodDays;
	}

	public void setMaxGracePeriodDays(Byte maxGracePeriodDays) {
		this.maxGracePeriodDays = maxGracePeriodDays;
	}

	public Set<VaccinePrerequisite> getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(Set<VaccinePrerequisite> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public Set<VaccineGap> getVaccineGaps() {
		return vaccineGaps;
	}

	public void setVaccineGaps(Set<VaccineGap> vaccineGaps) {
		this.vaccineGaps = vaccineGaps;
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

	public Boolean getVoided() {
		return voided;
	}

	public void setVoided(Boolean voided) {
		this.voided = voided;
	}

	public String getVoidReason() {
		return voidReason;
	}

	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}

	public User getVoidedByUserId() {
		return voidedByUserId;
	}

	public void setVoidedByUserId(User voidedByUserId) {
		this.voidedByUserId = voidedByUserId;
	}

	public Date getVoidedDate() {
		return voidedDate;
	}

	public void setVoidedDate(Date voidedDate) {
		this.voidedDate = voidedDate;
	}

	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}

	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
	
	public void setVoider(User voider, String voidReason){
		setVoidedByUserId(voider);
		setVoidedDate(new Date());
		setVoidReason(voidReason);
		setVoided(true);
	}
	
}