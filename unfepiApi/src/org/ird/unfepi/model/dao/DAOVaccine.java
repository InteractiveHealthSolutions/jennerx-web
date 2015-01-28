/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.OrderBySqlFormula;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOVaccine.
 */
public interface DAOVaccine extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the vaccine
	 */
	Vaccine findById(short id);

	/**
	 * Gets the by name. matches full name.
	 *
	 * @param vaccineName the vaccine name
	 * @return the by name
	 */
	Vaccine getByName(String vaccineName);

	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	/**
	 * Find vaccine by matching name.
	 *
	 * @param vaccineName the vaccine name
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @return the list
	 */
	List<Vaccine> findVaccine(String vaccineName, boolean readonly/*, int firstResult, int fetchsize*/);

	/**
	 * Gets the all.
	 *
	 * @param firstResult the first result
	 * @param fetchSize the fetch size
	 * @return the all
	 */
	//List<Vaccine> getAll(int firstResult, int fetchSize, boolean readonly);
	
	List<Vaccine> getAll(boolean readonly, String[] mappingsToJoin,	String orderBySqlFormula);
}
