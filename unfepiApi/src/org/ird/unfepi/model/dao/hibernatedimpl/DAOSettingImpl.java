/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Setting;
import org.ird.unfepi.model.dao.DAOSetting;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOSettingImpl.
 */
public class DAOSettingImpl extends DAOHibernateImpl implements DAOSetting{

	/** The session. */
	private Session session;

	/**
	 * Instantiates a new dAO setting impl.
	 *
	 * @param session the session
	 */
	public DAOSettingImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOSetting#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Setting> getAll() {
		return session.createQuery("from Setting").list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOSetting#findById(int)
	 */
	@Override
	public Setting findById(int id) {
			return (Setting) session.get(Setting.class,id);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOSetting#getSetting(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Setting getSetting(String settingName) {
		List l= session.createCriteria(Setting.class)
								.add(Restrictions.like("name", settingName,MatchMode.EXACT)).list();
		if(l.size()>0){
			return (Setting) l.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOSetting#matchByCriteria(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Setting> matchByCriteria(String settingName) {
		Criteria cri= session.createCriteria(Setting.class)
								.add(Restrictions.like("name", settingName,MatchMode.ANYWHERE));
		return cri.list();
	}
}
