/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.dao.DAOWomen;

/**
 * @author Safwan
 *
 */
public class DAOWomenImpl extends DAOHibernateImpl implements DAOWomen {
	
	/** The session. */
	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOWomenImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public Women findById(int mappedId) {
		Criteria criteria = session.createCriteria(Women.class).add(Restrictions.eq("mappedId", mappedId));
		@SuppressWarnings("unchecked")
		List<Women> women = criteria.list();
		return (women.size() == 0 ? null : women.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Women> getAllWomen(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Women.class).setReadOnly(isreadonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Women> list = cri.addOrder(Order.desc("dateEnrolled")).addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;

	}

	@Override
	public List<Women> getWomen(String partialName, String nic) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Women findById(int mappedId, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Women.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId));
			
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Women> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(int LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
		
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public Women findWomenByIdentifier(String programId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Women.class).setReadOnly(isreadonly)
				.setFetchMode("idMapper",FetchMode.JOIN)
				.createAlias("idMapper.identifiers", "idm")
				.add(Restrictions.eq("idm.identifier", programId));

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Women> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	
}

