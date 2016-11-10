/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.Model.Gender;

/**
 * The Interface DAOChild.
 */
public interface DAOChild extends DAO{
	
	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	/**
	 * Find by id no joins.
	 *
	 * @param id the id
	 * @return the child
	 */
	Child findByIdNoJoins(int mappedId);
	
	Child findByIdNoJoins(String programId, boolean isreadonly);

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @param joinPersonalDetails the join personal details
	 * @param joinArm the join arm
	 * @param joinAddresses the join addresses
	 * @param joinContactNumbers the join contact numbers
	 * @return the child
	 */
	Child findById(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	Child findById(String programId, boolean isreadonly, String[] mappingsToJoin);
	
	/**
	 * Gets the all.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @param isreadonly the isreadonly
	 * @return the all
	 */
	List<Child> getAll(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	List<Child> findByCriteria(String programIdLike, String partOfName, Date birthdatelower, Date birthdateUpper
			, String nic, Gender gender, String ethnicity, String religion, String language, STATUS status, boolean putNotWithStatus
			, Date dateEnrolledlower, Date dateEnrolleduppr, String creatorId, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin);
	
}
