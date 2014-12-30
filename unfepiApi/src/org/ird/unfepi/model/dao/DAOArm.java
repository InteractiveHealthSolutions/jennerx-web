/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Arm;

/**
 * The Interface DAOArm.
 */
public interface DAOArm extends DAO{
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the arm
	 */
	Arm findById(int id) ;
	
	/**
	 * Gets the all.
	 *
	 * @param isReadOnly the is read only
	 * @return the all
	 */
	List<Arm> getAll(boolean isReadOnly);
	
	/**
	 * Match by criteria.
	 *
	 * @param armname the armname
	 * @param armDayfetchmode the arm dayfetchmode
	 * @param isReadOnly the is read only
	 * @return the list
	 */
	List<Arm> matchByCriteria(String armname, boolean joinArmday, boolean isReadOnly);
	
	/**
	 * Gets the by arm by name, Arm object is readonly(modification to object will not be reflected in database) 
	 * and Fetched with mode JOIN on ArmDayReminder.
	 *
	 * @param armName the arm name
	 * @return the by name
	 */
	Arm getByName(String armName);
}
