/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Relationship;

/**
 * The Interface DAORelationship.
 */
public interface DAORelationship extends DAO{
	
	Relationship getById(short relationId);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Relationship> getAll();
	
	/**
	 * Gets the by name.
	 *
	 * @param relationName the relation name
	 * @return the by name
	 */
	Relationship getByName(String relationName);
}
