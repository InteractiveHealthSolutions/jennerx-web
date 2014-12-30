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
import org.ird.unfepi.model.ChildResponse;
import org.ird.unfepi.model.ChildResponse.ChildResponseType;
import org.ird.unfepi.model.dao.DAOChildResponse;

public class DAOChildResponseImpl extends DAOHibernateImpl implements DAOChildResponse{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOChildResponseImpl(Session session) {
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
	public ChildResponse findById(int id) {
		return (ChildResponse) session.get(ChildResponse.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildResponse> findByCriteriaIncludeName(String partOfFirstOrLastName, Date begindate, Date enddate,
			String cellNumber, String armName, ChildResponseType responseType,
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(ChildResponse.class);
		
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recievedDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("originatorCellNumber", cellNumber,MatchMode.END));
		}
		if(partOfFirstOrLastName!=null){
			cri.createAlias("child", "p").add(
					Restrictions.or(Restrictions.like("p.firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(armName!=null){
			cri.createAlias("child", "p").createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildResponse> list = cri.addOrder(Order.desc("recievedDate"))				
					.setReadOnly(isreadonly)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildResponse> findByCriteria(Integer childId, Date begindate,
			Date enddate, String cellNumber, ChildResponseType responseType, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(ChildResponse.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recievedDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("originatorCellNumber", cellNumber, MatchMode.END));
		}
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.mappedId", childId));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildResponse> list = cri.addOrder(Order.desc("recievedDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildResponse> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(ChildResponse.class)
					.addOrder(Order.desc("recievedDate"));
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildResponse> list = cri.addOrder(Order.desc("recievedDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildResponse> findByVaccinationRecord(int vacciantionRecordNum, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildResponse.class)
				.add(Restrictions.eq("vaccinationRecordNum", vacciantionRecordNum))
				.addOrder(Order.desc("recievedDate"));
		
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildResponse> list = cri.addOrder(Order.desc("recievedDate"))				
				.setReadOnly(isreadonly)
				.list();
		return list;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildResponse> findByCriteria(String programId, Date begindate,
			Date enddate, String cellNumber, ChildResponseType responseType,
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(ChildResponse.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("recievedDate", begindate, enddate));
		}
		if(cellNumber!=null){
			cri.add(Restrictions.like("originatorCellNumber", cellNumber,MatchMode.END));
		}
		if (programId != null) {
			cri.createAlias("child", "chl").createAlias("chl.idMapper", "im").add(Restrictions.eq("im.programId", programId));
		}
		if(responseType != null){
			cri.add(Restrictions.eq("responseType", responseType));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildResponse> list = cri.addOrder(Order.desc("recievedDate"))				
					.setReadOnly(isreadonly)
					.setFirstResult(firstResult)
					.setMaxResults(fetchsize)
					.list();
		return list;
	}
}
*/