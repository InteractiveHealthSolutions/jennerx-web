/**
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.TimelinessStatus;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;

/**
 * @author Safwan
 *
 */
public interface DAOWomenVaccination extends DAO {
	
	WomenVaccination findById(int id,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<WomenVaccination> findByWomenId(int id);
	
	List<WomenVaccination> findByCriteria(Integer womenId,
			String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber,Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus ,
			WOMEN_VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	List<WomenVaccination> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);



}
