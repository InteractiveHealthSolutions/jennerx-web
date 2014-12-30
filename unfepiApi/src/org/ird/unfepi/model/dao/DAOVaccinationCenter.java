/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenter.CenterType;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOVaccinationCenter.
 */
public interface DAOVaccinationCenter extends DAO{

	/**
	 * Find by id.
	 *
	 * @param Id the id
	 * @return the vaccination center
	 */
	VaccinationCenter findById(int Id);
	
	VaccinationCenter findById(int mappedId, boolean readonly, String[] mappingsToJoin);
	
	VaccinationCenter findById(String programId, boolean readonly, String[] mappingsToJoin);
	
	/**
	 * Find by name.
	 *
	 * @param vaccinationCenterName the vaccination center name
	 * @return the vaccination center
	 */
	VaccinationCenter findByName(String vaccinationCenterName, boolean readonly, String[] mappingsToJoin);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<VaccinationCenter> getAll(int firstResult, int fetchsize, boolean readonly,String[] mappingsToJoin);

	List<VaccinationCenter> getAll(boolean readonly, String[] mappingsToJoin);

	List<VaccinationCenter> findByCriteria(String programIdLike,
			String nameLike, CenterType centerType, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

}
