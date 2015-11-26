package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.VaccinatorIncentive;

public interface DAOVaccinatorIncentive extends DAO{
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
//	VaccinatorIncentive findById(int verificationCodeId, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findByCriteriaVaccinatorIncentivized(Integer vaccinatorId, Boolean isIncentivized, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findByCriteriaVaccinatorRecordNum(Integer vaccinatorRecordNum,boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findByCriteria(Integer armId, Integer vaccinator, Short vaccineId,  
			Boolean isIncentivized, Date incentiveDateFrom, Date incentiveDateTo,
			 Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findByArm(int armId, boolean readonly, String[] mappingsToJoin);
}
