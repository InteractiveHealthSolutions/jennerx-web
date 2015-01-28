/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Role;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAORole.
 */
public interface DAORole extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the role
	 */
	Role findById(short id, boolean isreadonly, String[] mappingsToJoin);

	/**
	 * Find by name.
	 *
	 * @param roleName the role name
	 * @return the role
	 */
	Role findByName(String roleName, boolean isreadonly, String[] mappingsToJoin);

	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Role> getAll(boolean isreadonly, String[] mappingsToJoin);

	/**
	 * Find by criteria.
	 *
	 * @param roleName the role name
	 * @return the list
	 */
	List<Role> findByCriteria(String roleName, boolean isreadonly, String[] mappingsToJoin);

}
