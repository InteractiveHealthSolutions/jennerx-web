package org.ird.unfepi.model;

/*
 * 
 
package org.ird.unfepi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

// TODO: Auto-generated Javadoc
*//**
 * The Class FinancialTransaction.
 *//*
@Entity
@Table(name = "financialtransaction")
public class FinancialTransaction {

	*//**
	 * The Enum FinancialTranscationStatus.
	 *//*
	public enum FinancialTranscationStatus{
		
		*//** The DUE. *//*
		DUE,
		
		*//** The PAID. *//*
		PAID,
		
		*//** The CANCELLED. *//*
		CANCELLED,
		
		*//** The TRANSFERRED. *//*
		TRANSFERRED
	}
	
	*//**
	 * The Enum PaymentMode.
	 *//*
	public enum PaymentMode{
		
		*//** The FINANCIA l_ sms. *//*
		FINANCIAL_SMS,
		
		*//** The VERIFICATIO n_ code. *//*
		VERIFICATION_CODE,
		
		*//** The DRAFT. *//*
		DRAFT,
		
		*//** The CASH. *//*
		CASH
	}
	
	*//** The transaction id. *//*
	@Id
	private int transactionId;
	
	*//** The beneficiary id. *//*
	private long beneficiaryId;
	
	*//** The beneficiary details. *//*
	private String beneficiaryDetails;

	*//** The transaction status. *//*
	@Enumerated(EnumType.STRING)
	private FinancialTranscationStatus transactionStatus;
	
	*//** The payment mode. *//*
	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;
	
	*//** The amount due. *//*
	private Double amountDue;
	
	*//** The amount paid. *//*
	private Double amountPaid;
	
	*//** The verification code. *//*
	@Column(unique = true)
	private Long verificationCode;
	
	*//** The financial sms id. *//*
	@Column(unique = true)
	private Long financialSmsId;
	
	*//** The draft number. *//*
	@Column(unique = true)
	private String draftNumber;
	
	*//** The draft details. *//*
	private String draftDetails;
	
	*//** The cash details. *//*
	private String cashDetails;
	
	*//** The is payment confirmed. *//*
	private Boolean isPaymentConfirmed;
	
	*//** The paid by user id. *//*
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "paidByUserId")
	@ForeignKey(name = "PayerUserIdFK")
	private Long paidByUserId;

	*//** The paid date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date paidDate;
	
	*//** The description. *//*
	
	private String description;
	
	*//** The created by user id. *//*
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "CreatorUserIdFK")
	private User createdByUserId;

	*//** The created date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	*//** The last edited by user id. *//*
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "EditorUserIdFK")
	private User lastEditedByUserId;
	
	*//** The last edited date. *//*
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;
	
	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 *//*
	public int getTransactionId() {
		return transactionId;
	}
	
	*//**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 *//*
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	*//**
	 * Gets the beneficiary id.
	 *
	 * @return the beneficiary id
	 *//*
	public long getBeneficiaryId() {
		return beneficiaryId;
	}
	
	*//**
	 * Sets the beneficiary id.
	 *
	 * @param beneficiaryId the new beneficiary id
	 *//*
	public void setBeneficiaryId(long beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}
	
	*//**
	 * Gets the beneficiary details.
	 *
	 * @return the beneficiary details
	 *//*
	public String getBeneficiaryDetails() {
		return beneficiaryDetails;
	}
	
	*//**
	 * Sets the beneficiary details.
	 *
	 * @param beneficiaryDetails the new beneficiary details
	 *//*
	public void setBeneficiaryDetails(String beneficiaryDetails) {
		this.beneficiaryDetails = beneficiaryDetails;
	}
	
	*//**
	 * Gets the transaction status.
	 *
	 * @return the transaction status
	 *//*
	public FinancialTranscationStatus getTransactionStatus() {
		return transactionStatus;
	}
	
	*//**
	 * Sets the transaction status.
	 *
	 * @param transactionStatus the new transaction status
	 *//*
	public void setTransactionStatus(FinancialTranscationStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	*//**
	 * Gets the payment mode.
	 *
	 * @return the payment mode
	 *//*
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	
	*//**
	 * Sets the payment mode.
	 *
	 * @param paymentMode the new payment mode
	 *//*
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	
	*//**
	 * Gets the amount due.
	 *
	 * @return the amount due
	 *//*
	public Double getAmountDue() {
		return amountDue;
	}
	
	*//**
	 * Sets the amount due.
	 *
	 * @param amountDue the new amount due
	 *//*
	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}
	
	*//**
	 * Gets the amount paid.
	 *
	 * @return the amount paid
	 *//*
	public Double getAmountPaid() {
		return amountPaid;
	}
	
	*//**
	 * Sets the amount paid.
	 *
	 * @param amountPaid the new amount paid
	 *//*
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	*//**
	 * Gets the verification code.
	 *
	 * @return the verification code
	 *//*
	public Long getVerificationCode() {
		return verificationCode;
	}
	
	*//**
	 * Sets the verification code.
	 *
	 * @param verificationCode the new verification code
	 *//*
	public void setVerificationCode(Long verificationCode) {
		this.verificationCode = verificationCode;
	}
	
	*//**
	 * Gets the finacial sms id.
	 *
	 * @return the finacial sms id
	 *//*
	public Long getFinancialSmsId() {
		return financialSmsId;
	}
	
	*//**
	 * Sets the finacial sms id.
	 *
	 * @param finacialSmsId the new finacial sms id
	 *//*
	public void setFinancialSmsId(Long finacialSmsId) {
		this.financialSmsId = finacialSmsId;
	}
	
	*//**
	 * Gets the draft number.
	 *
	 * @return the draft number
	 *//*
	public String getDraftNumber() {
		return draftNumber;
	}
	
	*//**
	 * Sets the draft number.
	 *
	 * @param draftNumber the new draft number
	 *//*
	public void setDraftNumber(String draftNumber) {
		this.draftNumber = draftNumber;
	}
	
	*//**
	 * Gets the draft details.
	 *
	 * @return the draft details
	 *//*
	public String getDraftDetails() {
		return draftDetails;
	}
	
	*//**
	 * Sets the draft details.
	 *
	 * @param draftDetails the new draft details
	 *//*
	public void setDraftDetails(String draftDetails) {
		this.draftDetails = draftDetails;
	}
	
	*//**
	 * Gets the cash details.
	 *
	 * @return the cash details
	 *//*
	public String getCashDetails() {
		return cashDetails;
	}
	
	*//**
	 * Sets the cash details.
	 *
	 * @param cashDetails the new cash details
	 *//*
	public void setCashDetails(String cashDetails) {
		this.cashDetails = cashDetails;
	}
	
	*//**
	 * Gets the checks if is payment confirmed.
	 *
	 * @return the checks if is payment confirmed
	 *//*
	public Boolean getIsPaymentConfirmed() {
		return isPaymentConfirmed;
	}
	
	*//**
	 * Sets the checks if is payment confirmed.
	 *
	 * @param isPaymentConfirmed the new checks if is payment confirmed
	 *//*
	public void setIsPaymentConfirmed(Boolean isPaymentConfirmed) {
		this.isPaymentConfirmed = isPaymentConfirmed;
	}
	
	*//**
	 * Gets the paid by user id.
	 *
	 * @return the paid by user id
	 *//*
	public Long getPaidByUserId() {
		return paidByUserId;
	}
	
	*//**
	 * Sets the paid by user id.
	 *
	 * @param paidByUserId the new paid by user id
	 *//*
	public void setPaidByUserId(Long paidByUserId) {
		this.paidByUserId = paidByUserId;
	}
	
	*//**
	 * Gets the paid date.
	 *
	 * @return the paid date
	 *//*
	public Date getPaidDate() {
		return paidDate;
	}
	
	*//**
	 * Sets the paid date.
	 *
	 * @param paidDate the new paid date
	 *//*
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	
	*//**
	 * Gets the description.
	 *
	 * @return the description
	 *//*
	public String getDescription() {
		return description;
	}

	*//**
	 * Sets the description.
	 *
	 * @param description the new description
	 *//*
	public void setDescription(String description) {
		this.description = description;
	}

	*//**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 *//*
	public User getCreatedByUserId() {
		return createdByUserId;
	}

	*//**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 *//*
	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	*//**
	 * Gets the created date.
	 *
	 * @return the created date
	 *//*
	public Date getCreatedDate() {
		return createdDate;
	}

	*//**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 *//*
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	*//**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 *//*
	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	*//**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 *//*
	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	*//**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 *//*
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	*//**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 *//*
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}

	*//**
	 * Gets the checks if is voided.
	 *
	 * @return the checks if is voided
	 *//*
	public Boolean getIsVoided() {
		return isVoided;
	}

	*//**
	 * Sets the checks if is voided.
	 *
	 * @param isVoided the new checks if is voided
	 *//*
	public void setIsVoided(Boolean isVoided) {
		this.isVoided = isVoided;
	}

	*//**
	 * Gets the voided reason.
	 *
	 * @return the voided reason
	 *//*
	public String getVoidedReason() {
		return voidedReason;
	}

	*//**
	 * Sets the voided reason.
	 *
	 * @param voidedReason the new voided reason
	 *//*
	public void setVoidedReason(String voidedReason) {
		this.voidedReason = voidedReason;
	}

	*//**
	 * Gets the voided by user id.
	 *
	 * @return the voided by user id
	 *//*
	public User getVoidedByUserId() {
		return voidedByUserId;
	}

	*//**
	 * Sets the voided by user id.
	 *
	 * @param voidedByUserId the new voided by user id
	 *//*
	public void setVoidedByUserId(User voidedByUserId) {
		this.voidedByUserId = voidedByUserId;
	}

	*//**
	 * Gets the voided date.
	 *
	 * @return the voided date
	 *//*
	public Date getVoidedDate() {
		return voidedDate;
	}

	*//**
	 * Sets the voided date.
	 *
	 * @param voidedDate the new voided date
	 *//*
	public void setVoidedDate(Date voidedDate) {
		this.voidedDate = voidedDate;
	}
	
	*//**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 *//*
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	*//**
	 * Sets the editor.
	 *
	 * @param editor the new editor
	 *//*
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
	
	*//**
	 * Sets the editor.
	 *
	 * @param voider the voider
	 * @param voidReason the void reason
	 *//*
	public void setVoider(User voider, String voidReason){
		setIsVoided(true);
		setVoidedByUserId(voider);
		setVoidedDate(new Date());
		setVoidedReason(voidReason);
	}
}
*/