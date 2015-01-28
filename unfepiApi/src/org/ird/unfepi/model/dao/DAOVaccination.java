package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;

public interface DAOVaccination extends DAO{
	
	Vaccination findById(int id,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<Vaccination> findByCriteria(int childId, Short vaccineId, VACCINATION_STATUS vaccinationStatus, 
			int firstResult, int fetchsize ,boolean isreadonly, String[] mappingsToJoin);

	List<Vaccination> findByCriteria(Integer childId,
			String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber,Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus ,
			VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	List<Vaccination> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	List<Vaccination> findByCriteriaIncludeName(String partOfName,
			String vaccineName, String epiNumber, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus ,
			VACCINATION_STATUS vaccinationStatus, String armName, int firstResult,
			int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) ;
	
}
