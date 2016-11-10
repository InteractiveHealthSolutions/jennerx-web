package org.ird.unfepi.model.dao.hibernatedimpl;

import org.hibernate.Session;
import org.ird.unfepi.model.dao.DAOCalendarVaccine;

public class DAOCalendarVaccineImpl extends DAOHibernateImpl implements DAOCalendarVaccine{

	private Session session;
	
	public DAOCalendarVaccineImpl(Session session) {
		super(session);
		this.session=session;
	}

}
