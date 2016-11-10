package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Arm;

public interface DAOArm extends DAO {
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the arm
	 */
	Arm findById(int id);
	
	/**
	 * Gets the by name. matches full name.
	 *
	 * @param armName the arm name
	 * @return the by name
	 */
	Arm getByName(String armName);
	
	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<Arm> getAll(boolean readonly, String[] mappingsToJoin,	String orderBySqlFormula);
	
	List<Arm> getAll();
	
	List<Arm> getAllArm(int firstResult, int fetchsize,	boolean readonly, String[] mappingsToJoin);

}
