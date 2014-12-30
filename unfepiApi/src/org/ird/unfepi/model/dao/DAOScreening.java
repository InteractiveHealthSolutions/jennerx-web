/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Screening;

/**
 * The Interface DAOScreening.
 */
public interface DAOScreening extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the screening
	 */
	Screening findById(int screeningid, boolean isreadonly, String[] mappingsToJoin);
	
	List<Screening> getAll(boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin);
	
	List<Screening> findByMappedId(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	List<Screening> findByProgramId(String programId, boolean isreadonly, String[] mappingsToJoin);

	List<Screening> findByCriteria(Integer vaccinatorId, Integer vaccinationCenterId/*, String epiNumber*/
			, Date screeningDatelower, Date screeningDateupper, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin);
}
