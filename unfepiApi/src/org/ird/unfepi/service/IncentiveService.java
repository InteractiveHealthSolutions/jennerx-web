package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.IncentiveParams;
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.StorekeeperIncentiveEvent;
import org.ird.unfepi.model.StorekeeperIncentiveParticipant;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinatorIncentive;
import org.ird.unfepi.model.VaccinatorIncentiveEvent;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction.TranscationStatus;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
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
	
	ChildIncentive findChildIncentiveById(int childIncentiveId, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findChildIncentiveByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findChildIncentiveByArm(int armId, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findChildIncentiveByVaccination(short vaccineId, int childId, VACCINATION_STATUS vaccinationStatus, boolean readonly, String[] mappingsToJoin) throws VaccinationDataException;

	List<ChildIncentive> getAllChildIncentive(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<ChildIncentive> findChildIncentiveByCriteria(/*String code,*/ Integer armId,Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, IncentiveStatus incentiveStatus, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	
	List<ChildIncentive> findChildIncentiveByCriteria(/*String code,*/Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	Serializable saveChildIncentive(ChildIncentive childIncentive);
	
	void updateChildIncentive(ChildIncentive childIncentive);
	
	ChildIncentive mergeChildIncentive(ChildIncentive childIncentive);
	
	
	Serializable saveIncentiveParams(IncentiveParams incentiveParams);
	void updateIncentiveParams(IncentiveParams incentiveParams);
	IncentiveParams mergeIncenvtiveParams(IncentiveParams incentiveParams);
	IncentiveParams findIncentiveParamsById(short incentiveParamsId,
			boolean readonly, String[] mappingsToJoin);
	List<IncentiveParams> getAllIncentiveParams(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
	List<IncentiveParams> findIncentiveParamsByCriteria(Short vaccineId, Integer armId,
			Short roleId, Date createdDateLower, Date createdDateUpper,
			Float criteriaRangeLower, Float criteriaRangeUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findVaccinatorIncentiveByVaccination(int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> getAllVaccinatorIncentive(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findVaccinatorIncentiveByCriteriaVaccinatorIncentivized(Integer vaccinatorId, Boolean isIncentivized, IncentiveStatus incentiveStatus, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findVaccinatorIncentiveByCriteria(Integer armId, Integer vaccinator, Short vaccineId,  
			Boolean isIncentivized, IncentiveStatus incentiveStatus, Date incentiveDateFrom, Date incentiveDateTo,
			 Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);
	
	public List<VaccinatorIncentive> findVaccinatorIncentiveByCriteriaVaccinatorRecordNum(Integer vaccinatorRecordNum, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinatorIncentive> findVaccinatorIncentiveByArm(int armId, boolean readonly, String[] mappingsToJoin);
	Serializable saveVaccinatorIncentive(VaccinatorIncentive vaccinatorIncentive);
	
	void updateVaccinatorIncentive(VaccinatorIncentive vaccinatorIncentive);
	
	VaccinatorIncentive mergeVaccinatorIncentive(VaccinatorIncentive vaccinatorIncentive);
}
