/**
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.WomenVaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;

/**
 * @author Safwan
 *
 */
public interface WomenVaccinationService {
	
	Vaccine findVaccineById(short id);
	
	Serializable save(WomenVaccination womenVaccination);
	
	WomenVaccination getVaccinationRecord(int recordId,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	List<WomenVaccination> findByWomenId(int womenId);
	
	List<WomenVaccination> findVaccinationRecordByCriteria(Integer womenId,
			String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus,
			WOMEN_VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize,
			boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	void updateVaccinationRecord(WomenVaccination vaccinationRecord);
	
	List<WomenVaccination> getAllVaccinationRecord(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

}
