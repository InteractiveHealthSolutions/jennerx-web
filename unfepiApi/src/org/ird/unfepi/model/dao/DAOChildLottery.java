package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ChildLottery.CodeStatus;

public interface DAOChildLottery extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	ChildLottery findById(int verificationCodeId, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> findByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> findByCriteria(String code, Integer childId, Short vaccineId,  
			Boolean hasWonLottery, Date lotteryDateFrom, Date lotteryDateTo, Date transactionDateFrom, Date transactionDateTo, 
			Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus, Integer storekeeperId, Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
}
