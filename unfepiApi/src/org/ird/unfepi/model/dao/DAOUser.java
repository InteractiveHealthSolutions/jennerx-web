/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;

/**
 * The Interface DAOUser.
 */
public interface DAOUser extends DAO{

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the user
	 */
	User findById(int mappedId);
	
	User findById(int mappedId, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	User findById(String programid);
	
	User findById(String programid, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	/**
	 * Find by username.
	 *
	 * @param username the username
	 * @return the user
	 */
	User findByUsername(String username);

	User findByUsername(String username, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<User> findByCriteria(String programid, String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus, int firstResult,
			int fetchsize, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);
	/**
	 * Gets the all.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @return the all
	 */
	List<User> getAll(int firstResult, int fetchsize, boolean isreadonly, boolean joinIdMapper, String[] mappingsToJoin);

/*	List<User> getAll(int firstResult, int fetchsize, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions);
*/
}
