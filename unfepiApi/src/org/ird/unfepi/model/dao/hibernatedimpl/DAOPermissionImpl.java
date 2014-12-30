/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.dao.DAOPermission;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOPermissionImpl.
 */
public class DAOPermissionImpl extends DAOHibernateImpl implements DAOPermission{
	
	/** The session. */
	private Session session;

	/**
	 * Instantiates a new dAO permission impl.
	 *
	 * @param session the session
	 */
	public DAOPermissionImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOPermission#findById(int)
	 */
	@Override
	public Permission findById(int id) {
		return (Permission) session.get(Permission.class,id);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOPermission#getAll(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> getAll(boolean isreadonly) {
		return session.createQuery("from Permission order by permissionname").setReadOnly(isreadonly).list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOPermission#findByPermissionName(java.lang.String, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Permission findByPermissionName(String permissionName,boolean isreadonly) {
		List<Permission> plist=session.createQuery("from Permission where permissionname=?").setString(0, permissionName).setReadOnly(isreadonly).list();
		if(plist.size()>0){
			return plist.get(0);
		}
		return null;
	}
}
