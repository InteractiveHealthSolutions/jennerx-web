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
import org.hibernate.sql.JoinType;
import org.ird.unfepi.model.Response;
import org.ird.unfepi.model.Response.ResponseType;
import org.ird.unfepi.model.dao.DAOResponse;

import com.mysql.jdbc.StringUtils;

public class DAOResponseImpl extends DAOHibernateImpl implements DAOResponse{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOResponseImpl(Session session) {
		super(session);
		this.session=session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public Response findById(int id) {
		return (Response) session.get(Response.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Response> findByCriteria(Integer mappedId, Short[] roleId, boolean notWithRole, Date begindate, Date enddate, 
			String[] originator, String[] recipient, ResponseType[] responseType, String referenceNumber, 
			Class eventClass, Integer eventId, String bodyMatches, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter) 
	{
		Criteria cri= session.createCriteria(Response.class).createAlias("idMapper", "idm", JoinType.LEFT_OUTER_JOIN);
		if(mappedId != null){
			cri.add(Restrictions.eq("mappedId", mappedId));
		}
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("responseDate", begindate, enddate));
		}
		if(roleId!=null){
			if(notWithRole){
				cri.add(Restrictions.not(Restrictions.in("idm.roleId", roleId)));
			}
			else {
				cri.add(Restrictions.in("idm.roleId", roleId));
			}
		}
		if(originator!=null){
			cri.add(Restrictions.in("originator", originator));
		}
		if(recipient!=null){
			cri.add(Restrictions.in("recipient", recipient));
		}
		if(responseType != null){
			cri.add(Restrictions.in("responseType", responseType));
		}
		if(referenceNumber != null){
			cri.add(Restrictions.eq("referenceNumber", referenceNumber));
		}
		if(eventClass != null){
			cri.add(Restrictions.eq("eventClass", eventClass.getName()));
		}
		if(eventId != null){
			cri.add(Restrictions.eq("eventId", eventId));
		}
		if(bodyMatches != null){
			cri.add(Restrictions.like("bodyMatches", bodyMatches, MatchMode.ANYWHERE));
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(sqlFilter)){
			cri.add(Restrictions.sqlRestriction(sqlFilter));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Response> list = cri.addOrder(Order.desc("responseDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Response> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(Response.class);
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Response> list = cri.addOrder(Order.desc("responseDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
