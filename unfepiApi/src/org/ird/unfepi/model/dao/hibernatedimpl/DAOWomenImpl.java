/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.dao.DAOWomen;

/**
 * @author Safwan
 *
 */
public class DAOWomenImpl extends DAOHibernateImpl implements DAOWomen {
	
	/** The session. */
	private Session session;

	public DAOWomenImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public Women findById(int mappedId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Women> getAllWomen(boolean readOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Women> getWomen(String partialName, String nic) {
		// TODO Auto-generated method stub
		return null;
	}
}

