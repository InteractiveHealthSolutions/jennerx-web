package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Language;
import org.ird.unfepi.model.dao.DAOLanguage;

public class DAOLanguageImpl extends DAOHibernateImpl implements DAOLanguage{
	
	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO language impl.
	 *
	 * @param session the session
	 */
	public DAOLanguageImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOLanguage#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Language> getAll() {
		return session.createQuery("from Language order by languageId").list();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOLanguage#getByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Language getByName(String languageName) {
		List<Language> list = session.createQuery("from Language where languageName = '"+languageName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Language getById(short languageId) {
		List<Language> list = session.createQuery("from Language where languageId = '"+languageId+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

}
