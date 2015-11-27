/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Religion;
import org.ird.unfepi.model.dao.DAOReligion;

/**
 * The Class DAOReligionImpl.
 */
public class DAOReligionImpl extends DAOHibernateImpl implements DAOReligion{

	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO religion impl.
	 *
	 * @param session the session
	 */
	public DAOReligionImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOReligion#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Religion> getAll() {
		return session.createQuery("from Religion order by religionId").list();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOReligion#getByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Religion getByName(String religionName) {
		List<Religion> list = session.createQuery("from Religion where religionName = '"+religionName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Religion getById(short religionId) {
		List<Religion> list = session.createQuery("from Religion where religionId = '"+religionId+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}
}
