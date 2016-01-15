/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.dao.DAOLocation;

/**
 * @author Safwan
 *
 */
public class DAOLocationImpl extends DAOHibernateImpl implements DAOLocation {
	
	/** The session. */
	private Session session ;

	/**
	 * Instantiates a new dAO address impl.
	 *
	 * @param session the session
	 */
	public DAOLocationImpl(Session session) {
		super(session);
		this.session=session;
	}

	@Override
	public Location getCityById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("locationId", cityId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

}
