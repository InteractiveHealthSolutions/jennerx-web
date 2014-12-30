/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.dao.DAOEncounterResults;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOEncounterResultsImpl.
 */
public class DAOEncounterResultsImpl extends  DAOHibernateImpl implements DAOEncounterResults{

	/** The session. */
	private Session session;

	/**
	 * Instantiates a new dAO encounter results impl.
	 *
	 * @param session the session
	 */
	public DAOEncounterResultsImpl(Session session) {
		super(session);
		this.session=session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EncounterResults> findEncounterResults(long entityId, String encounterType) {
		throw new UnsupportedOperationException();
		//return session.createQuery("from EncounterResults er join Encounter e where er.id = e.id and e.EncounterType = '"+encounterType+"' ").setResultTransformer(RootEntityResultTransformer.INSTANCE).list();
	}

	@Override
	public List<EncounterResults> findEncounterResultsByElement(long entityId, String encounterType, String elementName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<EncounterResults> findEncounterResultsByElementValue(long entityId, String encounterType, String elementName, String value) {
		throw new UnsupportedOperationException();
	}
}
