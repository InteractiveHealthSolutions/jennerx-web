/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Permission;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOPermission.
 */
public interface DAOPermission extends DAO{
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the permission
	 */
	Permission findById(int id);
	
	/**
	 * Gets the all.
	 *
	 * @param isreadonly the isreadonly
	 * @return the all
	 */
	List<Permission> getAll(boolean isreadonly);
	
	/**
	 * Find by permission name.
	 *
	 * @param permissionName the permission name
	 * @param isreadonly the isreadonly
	 * @return the permission
	 */
	Permission findByPermissionName(String permissionName,boolean isreadonly);
}
