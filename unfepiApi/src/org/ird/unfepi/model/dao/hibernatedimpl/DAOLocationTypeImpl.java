package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.dao.DAOLocationType;

@SuppressWarnings({"unchecked"})
public class DAOLocationTypeImpl extends DAOHibernateImpl implements DAOLocationType{
	
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationTypeImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public LocationType findById(int locationTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("locationTypeId", locationTypeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOLocationType#findByName(java.lang.String)
	 */
	@Override
	public LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("typeName", name)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public LocationType findByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("level", level)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public LocationType findByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("description", description)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
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
	 * @see org.ird.unfepi.model.dao.DAOLocationType#getAll()
	 */
	@Override
	public List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.addOrder(Order.asc("locationTypeId")).list();
		return list;
	}
}
