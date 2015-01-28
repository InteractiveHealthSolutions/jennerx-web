/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Ethnicity;

/**
 * The Interface DAOEthnicity.
 */
public interface DAOEthnicity extends DAO{

	Ethnicity getById(short ethnicityId);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Ethnicity> getAll();
	
	/**
	 * Gets the by name.
	 *
	 * @param ethnicityName the ethnicity name
	 * @return the by name
	 */
	Ethnicity getByName(String ethnicityName);
}
