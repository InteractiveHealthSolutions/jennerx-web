package org.ird.unfepi.model.dao.hibernatedimpl;

/*
 * 
 
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.FinancialTransaction;
import org.ird.unfepi.model.FinancialTransaction.FinancialTranscationStatus;
import org.ird.unfepi.model.FinancialTransaction.PaymentMode;
import org.ird.unfepi.model.dao.DAOFinancialTransaction;

*//**
 * The Class DAOFinancialTransactionImpl.
 *//*
public class DAOFinancialTransactionImpl extends DAOHibernateImpl implements DAOFinancialTransaction{

	*//** The session. *//*
	private Session session;
	
	*//** The LAS t_ quer y_ tota l_ ro w__ count. *//*
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOFinancialTransactionImpl(Session session) {
		super(session);
		this.session=session;
	}

	*//**
	 * Sets the lAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW__COUNT the new lAS t_ quer y_ tota l_ ro w_ count
	 *//*
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	 (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOChild#LAST_QUERY_TOTAL_ROW_COUNT()
	 
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialTransaction> getAll(int firstResult, int fetchsize, boolean isreadonly) {
		setLAST_QUERY_TOTAL_ROW_COUNT(countAllRows());

		return session.createQuery("from FinancialTransaction ORDER BY paidDate DESC, amountDue DESC")
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
	}

	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from FinancialTransaction").uniqueResult();
	}
	
	@Override
	public FinancialTransaction getById(long id) {
		FinancialTransaction trans = (FinancialTransaction) session.get(FinancialTransaction.class, id);
		setLAST_QUERY_TOTAL_ROW_COUNT(trans == null ? 0 : 1);
		return trans;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialTransaction> getByBeneficiaryId(long beneficiaryId, int firstResult, int fetchsize, boolean isreadonly) {
		setLAST_QUERY_TOTAL_ROW_COUNT(countRows(beneficiaryId));

		return session.createQuery("from FinancialTransaction where beneficiaryId = ? ORDER BY paidDate DESC, amountDue DESC")
				.setLong(0, beneficiaryId)
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
	}

	public Number countRows(long beneficiaryId) {
		return (Number) session.createQuery("select count(*) from FinancialTransaction where beneficiaryId = ?").setLong(0, beneficiaryId).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinancialTransaction getByVerificationCode(long verificationCode, boolean isreadonly) {
		List<FinancialTransaction> list = session.createQuery("from FinancialTransaction where verificationCode = ?")
				.setLong(0, verificationCode)
				.setReadOnly(isreadonly)
				.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinancialTransaction getByFinancialSmsId(long financialSmsId, boolean isreadonly) {
		List<FinancialTransaction> list = session.createQuery("from FinancialTransaction where financialSmsId = ?")
				.setLong(0, financialSmsId)
				.setReadOnly(isreadonly)
				.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public FinancialTransaction getByDraftNumber(String draftNumber, boolean isreadonly) {
		List<FinancialTransaction> list = session.createQuery("from FinancialTransaction where draftNumber = ?")
				.setString(0, draftNumber)
				.setReadOnly(isreadonly)
				.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialTransaction> findByCriteria(Long beneficiaryId, Boolean paymentConfirmed, 
			Double amountLowerLimit, Double amountUpperLimit, FinancialTranscationStatus transactionStatus,
			PaymentMode paymentMode, Long paidByUserId, Date paidDateLower, Date paidDateUpper,
			int firstResult, int fetchsize, boolean isreadonly) 
	{
		Criteria cri = session.createCriteria(FinancialTransaction.class);
		
		if(beneficiaryId != null){
			cri.add(Restrictions.eq("beneficiaryId", beneficiaryId));
		}
		if(paymentConfirmed != null){
			cri.add(Restrictions.eq("paymentConfirmed", paymentConfirmed));
		}
		if(amountLowerLimit != null && amountUpperLimit != null){
			cri.add(Restrictions.between("amountDue", amountLowerLimit, amountUpperLimit));
		}
		if(transactionStatus != null){
			cri.add(Restrictions.eq("transactionStatus", transactionStatus));
		}
		if(paymentMode != null){
			cri.add(Restrictions.eq("paymentMode", paymentMode));
		}
		if(paidByUserId != null){
			cri.add(Restrictions.eq("paidByUserId", paidByUserId));
		}
		if(paidDateLower != null && paidDateUpper != null){
			cri.add(Restrictions.between("paidDate", paidDateLower, paidDateUpper));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<FinancialTransaction> list = cri.addOrder(Order.desc("paidDate"))
				.addOrder(Order.desc("amountDue"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
*/