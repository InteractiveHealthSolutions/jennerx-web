package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.dao.DAOLocationAttributeType;

@SuppressWarnings({"unchecked"})
public class DAOLocationAttributeTypeImpl  extends DAOHibernateImpl implements DAOLocationAttributeType {
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationAttributeTypeImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public LocationAttributeType findById(int locationAttributeTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("locationAttributeTypeId", locationAttributeTypeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("attributeName", name)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public LocationAttributeType findByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("category", category)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.LocationAttributeType#getAll()
	 */
	@Override
	public List<LocationAttributeType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.addOrder(Order.asc("locationAttributeTypeId")).list();
		return list;
	}

	
}
