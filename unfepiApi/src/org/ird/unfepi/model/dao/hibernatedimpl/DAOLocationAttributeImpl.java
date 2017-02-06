package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.dao.DAOLocationAttribute;

import com.mysql.jdbc.StringUtils;

@SuppressWarnings({"unchecked"})
public class DAOLocationAttributeImpl  extends DAOHibernateImpl implements DAOLocationAttribute {
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationAttributeImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public LocationAttribute findById(int locationAttributeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttribute.class)
				.add(Restrictions.eq("locationAttributeId", locationAttributeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttribute> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.LocationAttribute#getAll()
	 */
	@Override
	public List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttribute.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttribute> list = cri.addOrder(Order.asc("locationAttributeId")).list();
		return list;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize ,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		
		Criteria cri=session.createCriteria(LocationAttribute.class);
		if(locationId!=null){
			cri.createAlias("location", "l").add(Restrictions.eq("l.locationId", locationId));
		}
		if(locationAttributeTypeId!=null){
			cri.createAlias("locationAttributeType", "lat").add(Restrictions.eq("lat.locationAttributeTypeId", locationAttributeTypeId));
		}
		if(locationAttributeId!=null){
			cri.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		}
		if((value!=null) && (!(value.equals("")))){
			cri.add(Restrictions.eq("value", value));
		}
		
		if(sqlFilter != null)
			for (String sqlf : sqlFilter) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(sqlf)){
					cri.add(Restrictions.sqlRestriction(sqlf));
				}
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<LocationAttribute> list = cri.setReadOnly(isreadonly).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

}
