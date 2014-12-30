package org.ird.unfepi.model.dao;
/*
 * 
 
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.FinancialTransaction;
import org.ird.unfepi.model.FinancialTransaction.FinancialTranscationStatus;
import org.ird.unfepi.model.FinancialTransaction.PaymentMode;

*//**
 * The Interface DAOFinancialTransaction.
 *//*
public interface DAOFinancialTransaction extends DAO{
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<FinancialTransaction> getAll(int firstResult, int fetchsize, boolean isreadonly);

	FinancialTransaction getById(long id);
	
	List<FinancialTransaction> getByBeneficiaryId(long beneficiaryId, int firstResult, int fetchsize, boolean isreadonly);
	
	FinancialTransaction getByVerificationCode(long verificationCode, boolean isreadonly);
	
	FinancialTransaction getByFinancialSmsId(long financialSmsId, boolean isreadonly);
	
	FinancialTransaction getByDraftNumber(String draftNumber, boolean isreadonly);
	
	List<FinancialTransaction> findByCriteria(Long beneficiaryId, Boolean paymentConfirmed
			, Double amountLowerLimit, Double amountUpperLimit, FinancialTranscationStatus transactionStatus
			, PaymentMode paymentMode, Long paidByUserId, Date paidDateLower, Date paidDateUpper
			, int firstResult, int fetchsize, boolean isreadonly);
}
*/