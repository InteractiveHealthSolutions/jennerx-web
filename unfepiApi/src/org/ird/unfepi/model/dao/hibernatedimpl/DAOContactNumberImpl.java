/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.dao.DAOContactNumber;

/**
 * The Class DAOContactNumberImpl.
 */
public class DAOContactNumberImpl extends DAOHibernateImpl implements DAOContactNumber{

	/** The session. */
	private Session session;
	//private Number LAST_QUERY_TOTAL_ROW__COUNT;

	/**
	 * Instantiates a new dAO contact number impl.
	 *
	 * @param session the session
	 */
	public DAOContactNumberImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@Override
	public ContactNumber getContactNumberById(int contactNumberId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ContactNumber.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("contactNumberId", contactNumberId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ContactNumber> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOContactNumber#getContactNumber(long, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContactNumber> getContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ContactNumber.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ContactNumber> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOContactNumber#getPrimaryContactNumber(long, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContactNumber> getPrimaryUniqueContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ContactNumber.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId))
				.add(Restrictions.eq("numberType", ContactType.PRIMARY));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ContactNumber> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactNumber> findContactNumber(String number, boolean isreadonly, String[] mappingsToJoin)  {
		Criteria cri = session.createCriteria(ContactNumber.class).setReadOnly(isreadonly)
				.add(Restrictions.like("number", number,MatchMode.END));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ContactNumber> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<ContactNumber> getUniqueContactNumber(int mappedId) {
		return session.createQuery("from ContactNumber where mappedId = "+mappedId+" and numberType in ('"+ContactType.PRIMARY_UNIQUE+"', '"+ContactType.SECONDARY_UNIQUE+"') "+(includeVoided?"":" and (isVoided IS NULL OR isVoided = false)")).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContactNumber> getUniqueContactNumber(String number) {
		return session.createQuery("from ContactNumber where number like '%"+number+"' and numberType in ('"+ContactType.PRIMARY_UNIQUE+"', '"+ContactType.SECONDARY_UNIQUE+"') "+(includeVoided?"":" and (isVoided IS NULL OR isVoided = false)")).list();
	}*/
}
