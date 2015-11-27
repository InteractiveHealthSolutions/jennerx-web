 
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class VerificationCode.
 */
@Entity
@Table(name = "verificationcode")
public class VerificationCode {

	/**
	 * The Enum VerificationCodeStatus.
	 */
	public enum VerificationCodeStatus{
		
		/** The AVAILABLE. */
		AVAILABLE,
		
		/** The ASSIGNED. */
		ASSIGNED,
		
		/** The CONSUMED. */
		CONSUMED
	}
	
	/** The verification code. */
	@Id
	private long verificationCode;
	
	/** The code status. */
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private VerificationCodeStatus codeStatus;
	
	/** The assigner user id. */
	private String assignerUserId;
	
	/** The assigner user name. */
	private String assignerUserName;
	
	/** The assigned date. */
	private Date assignedDate;
	
	public VerificationCode() {
		
	}
	/**
	 * Gets the verification code.
	 *
	 * @return the verification code
	 */
	public long getVerificationCode() {
		return verificationCode;
	}
	
	/**
	 * Sets the verification code.
	 *
	 * @param verificationCode the new verification code
	 */
	public void setVerificationCode(long verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	/**
	 * Gets the code status.
	 *
	 * @return the code status
	 */
	public VerificationCodeStatus getCodeStatus() {
		return codeStatus;
	}
	
	/**
	 * Sets the code status.
	 *
	 * @param codeStatus the new code status
	 */
	public void setCodeStatus(VerificationCodeStatus codeStatus) {
		this.codeStatus = codeStatus;
	}
	
	/**
	 * Gets the assigner user id.
	 *
	 * @return the assigner user id
	 */
	public String getAssignerUserId() {
		return assignerUserId;
	}
	
	/**
	 * Sets the assigner user id.
	 *
	 * @param assignerUserId the new assigner user id
	 */
	public void setAssignerUserId(String assignerUserId) {
		this.assignerUserId = assignerUserId;
	}
	
	/**
	 * Gets the assigner user name.
	 *
	 * @return the assigner user name
	 */
	public String getAssignerUserName() {
		return assignerUserName;
	}
	
	/**
	 * Sets the assigner user name.
	 *
	 * @param assignerUserName the new assigner user name
	 */
	public void setAssignerUserName(String assignerUserName) {
		this.assignerUserName = assignerUserName;
	}
	
	/**
	 * Gets the assigned date.
	 *
	 * @return the assigned date
	 */
	public Date getAssignedDate() {
		return assignedDate;
	}
	
	/**
	 * Sets the assigned date.
	 *
	 * @param assignedDate the new assigned date
	 */
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	/**
	 * Sets the assigner.
	 *
	 * @param assigner the new assigner
	 */
	public void setAssigner(User assigner){
		setAssignerUserId(assigner.getUsername());
		setAssignedDate(new Date());
	}
}
