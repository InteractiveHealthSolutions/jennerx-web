/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.dao.DAORole;

public class DAORoleImpl extends DAOHibernateImpl implements DAORole{
	
	private Session session;

	public DAORoleImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORole#findById(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Role findById(short id, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Role.class)
				.add(Restrictions.eq("roleId", id)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Role> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORole#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Role findByName(String roleName, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Role.class)
				.add(Restrictions.eq("rolename", roleName)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Role> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORole#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Role.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Role> list = cri.addOrder(Order.desc("rolename")).list();
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORole#findByCriteria(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findByCriteria(String roleName, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Role.class).setReadOnly(isreadonly);
		
		if(roleName!=null){
			cri.add(Restrictions.like("rolename", roleName,MatchMode.START));
		}
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Role> list = cri.addOrder(Order.desc("rolename")).list();
		return list;
	}
}
