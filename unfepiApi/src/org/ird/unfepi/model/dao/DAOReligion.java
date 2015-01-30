/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Religion;

/**
 * The Interface DAOReligion.
 */
public interface DAOReligion extends DAO{
	
	Religion getById(short religionId);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Religion> getAll();
	
	/**
	 * Gets the by name.
	 *
	 * @param religionName the religion name
	 * @return the by name
	 */
	Religion getByName(String religionName);
}
