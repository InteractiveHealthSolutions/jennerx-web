package org.ird.unfepi.model.dao.hibernatedimpl;
/*
 * 
 
package org.ird.unfepi.model.dao.hibernatedimpl;

import org.hibernate.Session;
import org.ird.unfepi.model.Person;
import org.ird.unfepi.model.dao.DAOPerson;

*//**
 * The Class DAOPersonImpl.
 *//*
public class DAOPersonImpl extends DAOHibernateImpl implements DAOPerson{

	*//** The session. *//*
	private Session session ;

	*//**
	 * Instantiates a new dAO person impl.
	 *
	 * @param session the session
	 *//*
	public DAOPersonImpl(Session session) {
		super(session);
		this.session=session;
	}

	 (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOPerson#findById(long)
	 
	@Override
	public Person findById(long id) {
		return (Person) session.get(Person.class, id);
	}

}
*/