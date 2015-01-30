package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.dao.DAOIdMapper;

public class DAOIdMapperImpl extends DAOHibernateImpl implements DAOIdMapper{

	private Session session;

	public DAOIdMapperImpl(Session session) {
		super(session);
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IdMapper findIdMapper(int mappedId) {
		List<IdMapper> list = session.createQuery("from IdMapper where mappedId =" + mappedId).list();
		return (list.size() > 0 ? list.get(0) : null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IdMapper findIdMapper(String programId) {
		List<IdMapper> list = session.createQuery("from IdMapper i LEFT JOIN FETCH i.identifiers id where id.identifier ='" + programId+"'").list();
		return (list.size() > 0 ? list.get(0) : null);
	}
}
