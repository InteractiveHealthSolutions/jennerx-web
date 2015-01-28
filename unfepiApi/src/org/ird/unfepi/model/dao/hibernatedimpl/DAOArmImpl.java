/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.dao.DAOArm;

/**
 * The Class DAOArmImpl.
 */
public class DAOArmImpl extends DAOHibernateImpl implements DAOArm{

	/** The session. */
	private Session session ;

	/**
	 * Instantiates a new dAO arm impl.
	 *
	 * @param session the session
	 */
	public DAOArmImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOArm#findById(int)
	 */
	@Override
	public Arm findById(int id) {
		return (Arm) session.get("Arm",id);
	}

	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOArm#getAll(boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Arm> getAll(boolean isReadOnly) {
		return session.createQuery("from Arm order by armName")
							.setReadOnly(true)
							.list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOArm#getByName(java.lang.String, org.hibernate.FetchMode, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Arm getByName(String armName) {
		List<Arm> alist= session.createCriteria(Arm.class)
						.add(Restrictions.like("armName", armName,MatchMode.EXACT))
						.setFetchMode("armday", FetchMode.JOIN)
						.setReadOnly(true)
						.list();

		if(alist.size()>0){
			return alist.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.immunizationreminder.dao.DAOArm#matchByCriteria(java.lang.String, org.hibernate.FetchMode, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Arm> matchByCriteria(String armname, boolean joinArmDay,boolean isreadonly) {
		return session.createCriteria(Arm.class)
						.add(Restrictions.like("armName", armname,MatchMode.START))
						.setFetchMode("armday", (joinArmDay ? FetchMode.JOIN : FetchMode.SELECT))
						.setReadOnly(isreadonly)
						.list();
	}
}
