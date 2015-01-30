/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.dao.DAOAddress;

/**
 * The Class DAOAddressImpl.
 */
public class DAOAddressImpl extends DAOHibernateImpl implements DAOAddress{

	/** The session. */
	private Session session ;

	/**
	 * Instantiates a new dAO address impl.
	 *
	 * @param session the session
	 */
	public DAOAddressImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Address getAddressById(int addressId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Address.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("addressId", addressId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Address> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOAddress#getAddress(long, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Address> getAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Address.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Address> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOAddress#getPrimaryAddress(long, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Address> getPrimaryAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Address.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId))
				.add(Restrictions.eq("addressType", ContactType.PRIMARY));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Address> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
}
