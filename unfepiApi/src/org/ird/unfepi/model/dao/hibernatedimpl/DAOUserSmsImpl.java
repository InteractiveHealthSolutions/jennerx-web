package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.UserSms.SmsType;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.model.dao.DAOUserSms;

public class DAOUserSmsImpl extends DAOHibernateImpl implements DAOUserSms{
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOUserSmsImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@Override
	public UserSms findById(int id) {
		UserSms usms= (UserSms) session.get(UserSms.class,id);
		setLAST_QUERY_TOTAL_ROW_COUNT(usms==null?0:1);
		return usms;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserSms> getAll(int firstResult, int fetchsize, boolean readonly) {
		setLAST_QUERY_TOTAL_ROW_COUNT(countAllRows());
		return session.createQuery("from UserSms order by dueDate desc").setFirstResult(firstResult).setMaxResults(fetchsize).setReadOnly(readonly).list();
	}
	
	public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from UserSms").uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserSms> findByCriteria(Date duedatesmaller,Date duedategreater,Date sentdatesmaller,
			Date sentdategreater,SmsStatus smsStatus, boolean putNotWithSmsStatus, SmsType smsType, 
			String recipient, Integer recipientId, Short recipientRole,  
			int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri=session.createCriteria(UserSms.class);

		if(duedatesmaller!=null && duedategreater!=null){
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if(sentdatesmaller!=null && sentdategreater !=null){
			cri.add(Restrictions.between("sentDate", sentdatesmaller, sentdategreater));
		}
		if(smsStatus!=null){
				if(putNotWithSmsStatus){
					cri.add(Restrictions.ne("smsStatus", smsStatus));
				}else{
					cri.add(Restrictions.eq("smsStatus", smsStatus));
				}
		}
		if(smsType!=null){
			cri.add(Restrictions.eq("smsType", smsType));
		}
		if(recipient != null){
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if(recipientId != null){
			cri.add(Restrictions.eq("recipientId", recipientId));
		}
		if(recipientRole != null){
			cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.roleId", recipientRole));
		}
		
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<UserSms> list =  cri.addOrder(Order.desc("dueDate")).setFirstResult(firstResult).setMaxResults(fetchsize).setReadOnly(readonly).list();
		return list;
	}
}
