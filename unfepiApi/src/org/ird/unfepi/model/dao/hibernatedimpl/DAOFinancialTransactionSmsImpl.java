package org.ird.unfepi.model.dao.hibernatedimpl;
/*package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.FinancialTransactionSms;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.dao.DAOFinancialTransactionSms;

public class DAOFinancialTransactionSmsImpl extends DAOHibernateImpl implements DAOFinancialTransactionSms{

	*//** The session. *//*
	private Session session;
	
	*//** The LAS t_ quer y_ tota l_ ro w__ count. *//*
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOFinancialTransactionSmsImpl(Session session) {
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

	@Override
	public FinancialTransactionSms getById(long id) {
		FinancialTransactionSms sms = (FinancialTransactionSms) session.get(FinancialTransactionSms.class, id);
		setLAST_QUERY_TOTAL_ROW_COUNT(sms == null ? 0 : 1);
		return sms;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialTransactionSms> getAll(int firstResult, int fetchsize,	boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri =  session.createCriteria(FinancialTransactionSms.class);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<FinancialTransactionSms> list = cri.addOrder(Order.desc("dueDate"))
				.addOrder(Order.desc("sentDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FinancialTransactionSms> findByCriteria(String recipient,
			SmsStatus smsStatus, Date dueDateLower, Date dueDateUpper,
			Boolean isSmsLate, String textContainingString, int firstResult,
			int fetchsize, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(FinancialTransactionSms.class);
		
		if(recipient != null){
			cri.add(Restrictions.eq("recipient", recipient));
		}
		if(smsStatus != null){
			cri.add(Restrictions.eq("smsStatus", smsStatus));
		}
		if(dueDateLower != null && dueDateUpper != null){
			cri.add(Restrictions.between("dueDate", dueDateLower, dueDateUpper));
		}
		if(isSmsLate != null){
			cri.add(Restrictions.eq("isSmsLate", isSmsLate));
		}
		if(textContainingString != null){
			cri.add(Restrictions.like("text", textContainingString, MatchMode.ANYWHERE));
		}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<FinancialTransactionSms> list = cri.addOrder(Order.desc("dueDate"))
				.addOrder(Order.desc("sentDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

}
*/