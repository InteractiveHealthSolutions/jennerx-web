package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildIncentive;


public interface DAOChildIncentive extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	ChildIncentive findById(int childIncentiveId, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findByCriteria(/*String code,*/ Integer armId, Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	
	List<ChildIncentive> findByCriteria(/*String code,*/  Integer childId, Short vaccineId,  
			Boolean hasWonLottery, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findByArm(int armId, boolean readonly, String[] mappingsToJoin);
}
