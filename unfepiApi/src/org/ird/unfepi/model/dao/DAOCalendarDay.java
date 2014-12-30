/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.CalendarDay;

/**
 * The Interface DAOCalendarDay.
 */
public interface DAOCalendarDay extends DAO{

	/**
	 * Gets the by day number.
	 *
	 * @param dayNumber the day number
	 * @return the by day number
	 */
	CalendarDay getByDayNumber(short dayNumber);
	
	/**
	 * Gets the by full name.
	 *
	 * @param fullName the full name
	 * @return the by full name
	 */
	CalendarDay getByFullName(String fullName);
	
	/**
	 * Gets the by short name.
	 *
	 * @param shortName the short name
	 * @return the by short name
	 */
	CalendarDay getByShortName(String shortName);

	List<CalendarDay> getAll(boolean readonly);

}
