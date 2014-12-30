package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildLotteryParams;
import org.ird.unfepi.model.StorekeeperIncentiveEvent;
import org.ird.unfepi.model.StorekeeperIncentiveParams;
import org.ird.unfepi.model.StorekeeperIncentiveParticipant;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinatorIncentiveEvent;
import org.ird.unfepi.model.VaccinatorIncentiveParams;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ChildLottery.CodeStatus;
import org.ird.unfepi.model.exception.VaccinationDataException;

public interface IncentiveService {

	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	List<VaccinatorIncentiveEvent> findVaccinatorIncentiveEventByCriteria(Date dateOfEventLower,
			Date dateOfEventUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveEvent> getAllVaccinatorIncentiveEvent(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	VaccinatorIncentiveEvent findVaccinatorIncentiveEventById(int vaccinatorIncentiveEventId,
			boolean readonly, String[] mappingsToJoin);
	
	Serializable saveVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance);

	VaccinatorIncentiveEvent mergeVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance);

	void updateVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance);
	
	VaccinatorIncentiveParticipant findVaccinatorIncentiveParticipantById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParticipant> getAllVaccinatorIncentiveParticipant(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParticipant> findVaccinatorIncentiveParticipantByCriteria(
			Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			Short vaccinatorIncentiveParamsId, Boolean isIncentivised,
			Integer criteriaElementValueLower, Integer criteriaElementValueUpper, Date incentiveDateLower, Date incentiveDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance);

	VaccinatorIncentiveParticipant mergeVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance);

	void updateVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance);

	VaccinatorIncentiveWorkProgress findVaccinatorIncentiveWorkProgressById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveWorkProgress> getAllVaccinatorIncentiveWorkProgress(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveWorkProgress> findVaccinatorIncentiveWorkProgressByCriteria(
			Integer VaccinatorIncentiveParticipantId,
			String workType, String workValue,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance);

	VaccinatorIncentiveWorkProgress mergeVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance);

	void updateVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance);
	
	VaccinatorIncentiveParams findVaccinatorIncentiveParamsById(short vaccinatorIncentiveParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParams> getAllVaccinatorIncentiveParams(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveParams> findVaccinatorIncentiveParamsByCriteria(Date createdDateLower,
			Date createdDateUpper, Integer criteriaRangeLower,
			Integer criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
	
	Serializable saveVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance);

	VaccinatorIncentiveParams mergeVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance);

	void updateVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance);
	
	VaccinatorIncentiveTransaction findVaccinatorIncentiveTransactionById(int VaccinatorIncentiveTransactionId,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveTransaction> getAllVaccinatorIncentiveTransaction(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<VaccinatorIncentiveTransaction> findVaccinatorIncentiveTransactionByCriteria(
			Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance);

	VaccinatorIncentiveTransaction mergeVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance);

	void updateVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance);
	
	
	
	List<StorekeeperIncentiveEvent> findStorekeeperIncentiveEventByCriteria(Date dateOfEventLower,
			Date dateOfEventUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveEvent> getAllStorekeeperIncentiveEvent(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	StorekeeperIncentiveEvent findStorekeeperIncentiveEventById(int storekeeperIncentiveEventId,
			boolean readonly, String[] mappingsToJoin);
	
	Serializable saveStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance);

	StorekeeperIncentiveEvent mergeStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance);

	void updateStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance);
	
	StorekeeperIncentiveParticipant findStorekeeperIncentiveParticipantById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParticipant> getAllStorekeeperIncentiveParticipant(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParticipant> findStorekeeperIncentiveParticipantByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			Short storekeeperIncentiveParamsId, Boolean isIncentivised,
			Float criteriaElementValueLower, Float criteriaElementValueUpper, Date incentiveDateLower, Date incentiveDateUpper, 
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance);

	StorekeeperIncentiveParticipant mergeStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance);

	void updateStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance);

	StorekeeperIncentiveWorkProgress findStorekeeperIncentiveWorkProgressById(int serialNumber,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveWorkProgress> getAllStorekeeperIncentiveWorkProgress(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveWorkProgress> findStorekeeperIncentiveWorkProgressByCriteria(
			Integer StorekeeperIncentiveParticipantId,
			Integer transactionsLower, Integer transactionsUpper, Float transactionsAmountLower, Float transactionsAmountUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveStorekeeperIncentiveWorkProgress(StorekeeperIncentiveWorkProgress objectinstance);

	StorekeeperIncentiveWorkProgress mergeStorekeeperIncentiveWorkProgress(StorekeeperIncentiveWorkProgress objectinstance);

	void updateStorekeeperIncentiveWorkProgress(StorekeeperIncentiveWorkProgress objectinstance);
	
	StorekeeperIncentiveParams findStorekeeperIncentiveParamsById(short storekeeperIncentiveParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParams> getAllStorekeeperIncentiveParams(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveParams> findStorekeeperIncentiveParamsByCriteria(Date createdDateLower,
			Date createdDateUpper, Integer criteriaRangeLower,
			Integer criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
	
	Serializable saveStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance);

	StorekeeperIncentiveParams mergeStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance);

	void updateStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance);
	
	StorekeeperIncentiveTransaction findStorekeeperIncentiveTransactionById(int StorekeeperIncentiveTransactionId,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveTransaction> getAllStorekeeperIncentiveTransaction(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<StorekeeperIncentiveTransaction> findStorekeeperIncentiveTransactionByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	Serializable saveStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance);

	StorekeeperIncentiveTransaction mergeStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance);

	void updateStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance);
	
	ChildLotteryParams findChildLotteryParamsById(short childLotteryParamsId,
			boolean readonly, String[] mappingsToJoin);

	List<ChildLotteryParams> getAllChildLotteryParams(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);

	List<ChildLotteryParams> findChildLotteryParamsByCriteria(Short enrollmentVaccine, Short receivedVaccine, 
			Date createdDateLower,	Date createdDateUpper, Float criteriaRangeLower,
			Float criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
	
	Serializable saveChildLotteryParams(ChildLotteryParams objectinstance);

	ChildLotteryParams mergeChildLotteryParams(ChildLotteryParams objectinstance);

	void updateChildLotteryParams(ChildLotteryParams objectinstance);
	
	ChildLottery findChildLotteryById(int childLotteryId, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> findChildLotteryByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> findChildLotteryByVaccination(short vaccineId, int childId, VACCINATION_STATUS vaccinationStatus, boolean readonly, String[] mappingsToJoin) throws VaccinationDataException;

	List<ChildLottery> getAllChildLottery(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<ChildLottery> findChildLotteryByCriteria(String code, Integer childId, Short vaccineId,  
			Boolean hasWonLottery, Date lotteryDateFrom, Date lotteryDateTo, Date transactionDateFrom, Date transactionDateTo, 
			Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus, Integer storekeeperId, Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	Serializable saveChildLottery(ChildLottery childLottery);
	
	void updateChildLottery(ChildLottery childLottery);
	
	ChildLottery mergeChildLottery(ChildLottery childLottery);
}
