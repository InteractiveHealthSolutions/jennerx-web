/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Relationship;
import org.ird.unfepi.model.dao.DAORelationship;

/**
 * The Class DAORelationshipImpl.
 */
public class DAORelationshipImpl extends DAOHibernateImpl implements DAORelationship{
	
	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO relationship impl.
	 *
	 * @param session the session
	 */
	public DAORelationshipImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORelationship#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Relationship> getAll() {
		return session.createQuery("from Relationship order by relationshipId").list();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAORelationship#getByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Relationship getByName(String relationName) {
		List<Relationship> list = session.createQuery("from Relationship where relationName = '"+relationName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Relationship getById(short relationId) {
		List<Relationship> list = session.createQuery("from Relationship where relationshipId = '"+relationId+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}
}
