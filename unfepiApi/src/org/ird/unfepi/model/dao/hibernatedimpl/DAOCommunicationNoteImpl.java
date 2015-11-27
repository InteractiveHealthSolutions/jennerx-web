package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;
import org.ird.unfepi.model.dao.DAOCommunicationNote;

import com.mysql.jdbc.StringUtils;

public class DAOCommunicationNoteImpl extends DAOHibernateImpl implements DAOCommunicationNote{

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOCommunicationNoteImpl(Session session) {
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
	public CommunicationNote findById(int id) {
		return (CommunicationNote) session.get(CommunicationNote.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CommunicationNote> getByGroupId(String groupId, CommunicationEventType[] type, CommunicationCategory[] category, boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(CommunicationNote.class);
		cri.add(Restrictions.eq("groupId", groupId));

		if(type != null && type.length > 0){
			cri.add(Restrictions.in("communicationEventType", type));
		}
		if(category != null && category.length > 0){
			cri.add(Restrictions.in("communicationCategory", category));
		}
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<CommunicationNote> list = cri.setReadOnly(isreadonly).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommunicationNote> findByCriteria(Date begindate, Date enddate, String[] source, 
			String[] receiver, CommunicationEventType[] type, CommunicationCategory[] category, 
			Class probeClass, Integer probeId, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter)
	{
		Criteria cri= session.createCriteria(CommunicationNote.class);
		if(begindate!=null && enddate!=null){
			cri.add(Restrictions.between("eventDate", begindate, enddate));
		}
		if(source!=null && source.length > 0){
			cri.add(Restrictions.in("source", source));
		}
		if(receiver!=null && receiver.length > 0){
			cri.add(Restrictions.in("receiver", receiver));
		}
		if(type != null && type.length > 0){
			cri.add(Restrictions.in("communicationEventType", type));
		}
		if(category != null && category.length > 0){
			cri.add(Restrictions.in("communicationCategory", category));
		}
		if(probeClass != null){
			cri.add(Restrictions.eq("probeClass", probeClass.getName()));
		}
		if(probeId != null){
			cri.add(Restrictions.eq("probeId", probeId+""));//otherwise throws classcast exception in uniqueresult
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
		
		List<CommunicationNote> list = cri.addOrder(Order.desc("eventDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommunicationNote> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(CommunicationNote.class);
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<CommunicationNote> list = cri.addOrder(Order.desc("eventDate"))				
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
