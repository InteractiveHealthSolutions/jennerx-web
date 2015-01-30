/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Ethnicity;
import org.ird.unfepi.model.dao.DAOEthnicity;

/**
 * The Class DAOEthnicityImpl.
 */
public class DAOEthnicityImpl extends DAOHibernateImpl implements DAOEthnicity{
	
	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO ethnicity impl.
	 *
	 * @param session the session
	 */
	public DAOEthnicityImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOEthnicity#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Ethnicity> getAll() {
		return session.createQuery("from Ethnicity order by ethnicityId").list();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOEthnicity#getByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Ethnicity getByName(String ethnicityName) {
		List<Ethnicity> list = session.createQuery("from Ethnicity where ethnicityName = '"+ethnicityName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ethnicity getById(short ethnicityId) {
		List<Ethnicity> list = session.createQuery("from Ethnicity where ethnicityId = '"+ethnicityId+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

}
