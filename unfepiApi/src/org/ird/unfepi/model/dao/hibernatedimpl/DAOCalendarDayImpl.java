/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.dao.DAOCalendarDay;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOCalendarDayImpl.
 */
public class DAOCalendarDayImpl extends DAOHibernateImpl implements DAOCalendarDay{

	/** The session. */
	private Session session ;

	/**
	 * Instantiates a new dAO calendar day impl.
	 *
	 * @param session the session
	 */
	public DAOCalendarDayImpl(Session session) {
		super(session);
		this.session=session;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCalendarDay#getByDayNumber(byte)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CalendarDay getByDayNumber(short dayNumber) {
		List<CalendarDay> list = session.createQuery("from CalendarDay where dayNumber='"+dayNumber+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCalendarDay#getByFullName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CalendarDay getByFullName(String fullName) {
		List<CalendarDay> list = session.createQuery("from CalendarDay where dayFullName='"+fullName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCalendarDay#getByShortName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CalendarDay getByShortName(String shortName) {
		List<CalendarDay> list = session.createQuery("from CalendarDay where dayShortName='"+shortName+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarDay> getAll(boolean readonly) {
		List<CalendarDay> list = session.createQuery("from CalendarDay").setReadOnly(readonly).list();
		return list;
	}
}
