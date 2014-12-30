package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ChildLottery.CodeStatus;
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
import org.ird.unfepi.model.dao.DAOChildLottery;
import org.ird.unfepi.model.dao.DAOChildLotteryParams;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveEvent;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParams;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveEvent;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParams;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.exception.VaccinationDataException;
import org.ird.unfepi.service.IncentiveService;

public class IncentiveServiceImpl implements IncentiveService{

	private ServiceContext sc;

	private DAOVaccinatorIncentiveEvent daovlottevent;
	private DAOVaccinatorIncentiveParticipant daovlottparti;
	private DAOVaccinatorIncentiveParams daovlottparams;
	private DAOVaccinatorIncentiveTransaction daovlotttrans;
	private DAOVaccinatorIncentiveWorkProgress daovlottwinrcords;
	
	private DAOStorekeeperIncentiveEvent daosincentevent;
	private DAOStorekeeperIncentiveParticipant daosincentparti;
	private DAOStorekeeperIncentiveParams daosincentparams;
	private DAOStorekeeperIncentiveTransaction daosincenttrans;
	private DAOStorekeeperIncentiveWorkProgress daosincentwinrcords;
	
	private DAOChildLotteryParams daoclottparams;
	private DAOChildLottery daochildlott;

	public IncentiveServiceImpl(DAOVaccinatorIncentiveEvent daovlottevent, DAOVaccinatorIncentiveParticipant daovlottparti
			, DAOVaccinatorIncentiveParams daovlottparams, DAOVaccinatorIncentiveTransaction daovlotttrans
			, DAOVaccinatorIncentiveWorkProgress daovlottwinrcords,
			DAOStorekeeperIncentiveEvent daosincentevent, DAOStorekeeperIncentiveParticipant daosincentparti
			, DAOStorekeeperIncentiveParams daosincentparams, DAOStorekeeperIncentiveTransaction daosincenttrans
			, DAOStorekeeperIncentiveWorkProgress daosincentwinrcords, 
			DAOChildLotteryParams daoclottparams, DAOChildLottery daoverifcode, ServiceContext sc) {
		this.daovlottevent = daovlottevent;
		this.daovlottparti = daovlottparti;
		this.daovlottparams = daovlottparams;
		this.daovlotttrans = daovlotttrans;
		this.daovlottwinrcords = daovlottwinrcords;
		
		this.daosincentevent = daosincentevent;
		this.daosincentparti = daosincentparti;
		this.daosincentparams = daosincentparams;
		this.daosincenttrans = daosincenttrans;
		this.daosincentwinrcords = daosincentwinrcords;
		
		this.daoclottparams = daoclottparams;
		
		this.daochildlott = daoverifcode;
		
		this.sc = sc;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == VaccinatorIncentiveEvent.class){
			return daovlottevent.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentiveParticipant.class){
			return daovlottparti.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentiveParams.class){
			return daovlottparams.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentiveTransaction.class){
			return daovlotttrans.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentiveWorkProgress.class){
			return daovlottwinrcords.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		else if(clazz == StorekeeperIncentiveEvent.class){
			return daosincentevent.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == StorekeeperIncentiveParticipant.class){
			return daosincentparti.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == StorekeeperIncentiveParams.class){
			return daosincentparams.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == StorekeeperIncentiveTransaction.class){
			return daosincenttrans.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == StorekeeperIncentiveWorkProgress.class){
			return daosincentwinrcords.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		else if(clazz == ChildLotteryParams.class){
			return daoclottparams.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == ChildLottery.class){
			return daochildlott.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}

	@Override
	public List<VaccinatorIncentiveEvent> findVaccinatorIncentiveEventByCriteria(Date dateOfEventLower, Date dateOfEventUpper, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<VaccinatorIncentiveEvent> list = daovlottevent.findByCriteria(dateOfEventLower, dateOfEventUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveEvent> getAllVaccinatorIncentiveEvent(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveEvent> list = daovlottevent.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public VaccinatorIncentiveEvent findVaccinatorIncentiveEventById(int vaccinatorIncentiveEventId, boolean readonly,
			String[] mappingsToJoin) {
		VaccinatorIncentiveEvent list = daovlottevent.findById(vaccinatorIncentiveEventId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance) {
		return daovlottevent.save(objectinstance);
	}

	@Override
	public VaccinatorIncentiveEvent mergeVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance) {
		return (VaccinatorIncentiveEvent) daovlottevent.merge(objectinstance);
	}

	@Override
	public void updateVaccinatorIncentiveEvent(VaccinatorIncentiveEvent objectinstance) {
		daovlottevent.update(objectinstance);
	}

	@Override
	public VaccinatorIncentiveParticipant findVaccinatorIncentiveParticipantById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		VaccinatorIncentiveParticipant obj = daovlottparti.findById(serialNumber, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<VaccinatorIncentiveParticipant> getAllVaccinatorIncentiveParticipant(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveParticipant> list = daovlottparti.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveParticipant> findVaccinatorIncentiveParticipantByCriteria(Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			Short vaccinatorIncentiveParamsId, Boolean isIncentivised,
			Integer criteriaElementValueLower, Integer criteriaElementValueUpper, Date lotteryDateLower, Date lotteryDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveParticipant> list = daovlottparti.findByCriteria(vaccinatorIncentiveEventId, vaccinatorId, vaccinatorIncentiveParamsId, isIncentivised, criteriaElementValueLower, criteriaElementValueUpper, lotteryDateLower, lotteryDateUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance) {
		return daovlottparti.save(objectinstance);
	}

	@Override
	public VaccinatorIncentiveParticipant mergeVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance) {
		return (VaccinatorIncentiveParticipant) daovlottparti.merge(objectinstance);
	}

	@Override
	public void updateVaccinatorIncentiveParticipant(VaccinatorIncentiveParticipant objectinstance) {
		daovlottparti.update(objectinstance);
	}

	@Override
	public VaccinatorIncentiveWorkProgress findVaccinatorIncentiveWorkProgressById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		VaccinatorIncentiveWorkProgress list = daovlottwinrcords.findById(serialNumber, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveWorkProgress> getAllVaccinatorIncentiveWorkProgress(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveWorkProgress> list = daovlottwinrcords.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveWorkProgress> findVaccinatorIncentiveWorkProgressByCriteria(Integer VaccinatorIncentiveParticipantId,
			String workType, String workValue,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveWorkProgress> list = daovlottwinrcords.findByCriteria(VaccinatorIncentiveParticipantId, workType, workValue, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance) {
		return daovlottwinrcords.save(objectinstance);
	}

	@Override
	public VaccinatorIncentiveWorkProgress mergeVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance) {
		return (VaccinatorIncentiveWorkProgress) daovlottwinrcords.merge(objectinstance);
	}

	@Override
	public void updateVaccinatorIncentiveWorkProgress(VaccinatorIncentiveWorkProgress objectinstance) {
		daovlottwinrcords.update(objectinstance);
	}

	@Override
	public VaccinatorIncentiveParams findVaccinatorIncentiveParamsById(short vaccinatorIncentiveParamsId, boolean readonly,
			String[] mappingsToJoin) {
		VaccinatorIncentiveParams list = daovlottparams.findById(vaccinatorIncentiveParamsId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveParams> getAllVaccinatorIncentiveParams(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveParams> list = daovlottparams.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveParams> findVaccinatorIncentiveParamsByCriteria(Date createdDateLower, Date createdDateUpper,
			Integer criteriaRangeLower, Integer criteriaRangeUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveParams> list = daovlottparams.findByCriteria(createdDateLower, createdDateUpper, criteriaRangeLower, criteriaRangeUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance) {
		return daovlottparams.save(objectinstance);
	}

	@Override
	public VaccinatorIncentiveParams mergeVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance) {
		return (VaccinatorIncentiveParams) daovlottparams.merge(objectinstance);
	}

	@Override
	public void updateVaccinatorIncentiveParams(VaccinatorIncentiveParams objectinstance) {
		daovlottparams.update(objectinstance);
	}

	@Override
	public VaccinatorIncentiveTransaction findVaccinatorIncentiveTransactionById(int VaccinatorIncentiveTransactionId, boolean readonly,
			String[] mappingsToJoin) {
		VaccinatorIncentiveTransaction list = daovlotttrans.findById(VaccinatorIncentiveTransactionId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveTransaction> getAllVaccinatorIncentiveTransaction(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveTransaction> list = daovlotttrans.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<VaccinatorIncentiveTransaction> findVaccinatorIncentiveTransactionByCriteria(Integer vaccinatorIncentiveEventId, Integer vaccinatorId,
			TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentiveTransaction> list = daovlotttrans.findByCriteria(vaccinatorIncentiveEventId, vaccinatorId, transactionStatus, amountDueLower, amountDueUpper, createdDateLower, createdDateUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance) {
		return daovlotttrans.save(objectinstance);
	}

	@Override
	public VaccinatorIncentiveTransaction mergeVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance) {
		return (VaccinatorIncentiveTransaction) daovlotttrans.merge(objectinstance);
	}

	@Override
	public void updateVaccinatorIncentiveTransaction(VaccinatorIncentiveTransaction objectinstance) {
		daovlotttrans.update(objectinstance);
	}

	@Override
	public List<StorekeeperIncentiveEvent> findStorekeeperIncentiveEventByCriteria(Date dateOfEventLower, Date dateOfEventUpper, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<StorekeeperIncentiveEvent> list = daosincentevent.findByCriteria(dateOfEventLower, dateOfEventUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveEvent> getAllStorekeeperIncentiveEvent(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveEvent> list = daosincentevent.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public StorekeeperIncentiveEvent findStorekeeperIncentiveEventById(int storekeeperIncentiveEventId, boolean readonly,
			String[] mappingsToJoin) {
		StorekeeperIncentiveEvent list = daosincentevent.findById(storekeeperIncentiveEventId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance) {
		return daosincentevent.save(objectinstance);
	}

	@Override
	public StorekeeperIncentiveEvent mergeStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance) {
		return (StorekeeperIncentiveEvent) daosincentevent.merge(objectinstance);
	}

	@Override
	public void updateStorekeeperIncentiveEvent(StorekeeperIncentiveEvent objectinstance) {
		daosincentevent.update(objectinstance);
	}

	@Override
	public StorekeeperIncentiveParticipant findStorekeeperIncentiveParticipantById(int serialNumber, boolean readonly, String[] mappingsToJoin) {
		StorekeeperIncentiveParticipant obj = daosincentparti.findById(serialNumber, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<StorekeeperIncentiveParticipant> getAllStorekeeperIncentiveParticipant(
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveParticipant> list = daosincentparti.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveParticipant> findStorekeeperIncentiveParticipantByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			Short storekeeperIncentiveParamsId, Boolean isIncentivised,
			Float criteriaElementValueLower, Float criteriaElementValueUpper, Date incentiveDateLower, Date incentiveDateUpper, 
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveParticipant> list = daosincentparti.findByCriteria(storekeeperIncentiveEventId, storekeeperId, storekeeperIncentiveParamsId, isIncentivised, criteriaElementValueLower, criteriaElementValueUpper, incentiveDateLower, incentiveDateUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance) {
		return daosincentparti.save(objectinstance);
	}

	@Override
	public StorekeeperIncentiveParticipant mergeStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance) {
		return (StorekeeperIncentiveParticipant) daosincentparti.merge(objectinstance);
	}

	@Override
	public void updateStorekeeperIncentiveParticipant(StorekeeperIncentiveParticipant objectinstance) {
		daosincentparti.update(objectinstance);
	}

	@Override
	public StorekeeperIncentiveWorkProgress findStorekeeperIncentiveWorkProgressById(
			int serialNumber, boolean readonly, String[] mappingsToJoin) {
		StorekeeperIncentiveWorkProgress list = daosincentwinrcords.findById(serialNumber, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveWorkProgress> getAllStorekeeperIncentiveWorkProgress(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveWorkProgress> list = daosincentwinrcords.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveWorkProgress> findStorekeeperIncentiveWorkProgressByCriteria(
			Integer StorekeeperIncentiveParticipantId,
			Integer transactionsLower, Integer transactionsUpper, Float transactionsAmountLower, Float transactionsAmountUpper, 
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveWorkProgress> list = daosincentwinrcords.findByCriteria(StorekeeperIncentiveParticipantId, transactionsLower, transactionsUpper, transactionsAmountLower, transactionsAmountUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveStorekeeperIncentiveWorkProgress(StorekeeperIncentiveWorkProgress objectinstance) {
		return daosincentwinrcords.save(objectinstance);
	}

	@Override
	public StorekeeperIncentiveWorkProgress mergeStorekeeperIncentiveWorkProgress(
			StorekeeperIncentiveWorkProgress objectinstance) {
		return (StorekeeperIncentiveWorkProgress) daosincentwinrcords.merge(objectinstance);
	}

	@Override
	public void updateStorekeeperIncentiveWorkProgress(StorekeeperIncentiveWorkProgress objectinstance) {
		daosincentwinrcords.update(objectinstance);
	}

	@Override
	public StorekeeperIncentiveParams findStorekeeperIncentiveParamsById(short storekeeperIncentiveParamsId, boolean readonly,
			String[] mappingsToJoin) {
		StorekeeperIncentiveParams list = daosincentparams.findById(storekeeperIncentiveParamsId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveParams> getAllStorekeeperIncentiveParams(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveParams> list = daosincentparams.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveParams> findStorekeeperIncentiveParamsByCriteria(Date createdDateLower, Date createdDateUpper,
			Integer criteriaRangeLower, Integer criteriaRangeUpper, int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveParams> list = daosincentparams.findByCriteria(createdDateLower, createdDateUpper, criteriaRangeLower, criteriaRangeUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance) {
		return daosincentparams.save(objectinstance);
	}

	@Override
	public StorekeeperIncentiveParams mergeStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance) {
		return (StorekeeperIncentiveParams) daosincentparams.merge(objectinstance);
	}

	@Override
	public void updateStorekeeperIncentiveParams(StorekeeperIncentiveParams objectinstance) {
		daosincentparams.update(objectinstance);
	}

	@Override
	public StorekeeperIncentiveTransaction findStorekeeperIncentiveTransactionById(int StorekeeperIncentiveTransactionId, boolean readonly,
			String[] mappingsToJoin) {
		StorekeeperIncentiveTransaction list = daosincenttrans.findById(StorekeeperIncentiveTransactionId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveTransaction> getAllStorekeeperIncentiveTransaction(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveTransaction> list = daosincenttrans.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<StorekeeperIncentiveTransaction> findStorekeeperIncentiveTransactionByCriteria(
			Integer storekeeperIncentiveEventId, Integer storekeeperId,
			org.ird.unfepi.model.StorekeeperIncentiveTransaction.TranscationStatus transactionStatus, Float amountDueLower,
			Float amountDueUpper, Date createdDateLower, Date createdDateUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<StorekeeperIncentiveTransaction> list = daosincenttrans.findByCriteria(storekeeperIncentiveEventId, storekeeperId, transactionStatus, amountDueLower, amountDueUpper, createdDateLower, createdDateUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance) {
		return daosincenttrans.save(objectinstance);
	}

	@Override
	public StorekeeperIncentiveTransaction mergeStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance) {
		return (StorekeeperIncentiveTransaction) daosincenttrans.merge(objectinstance);
	}

	@Override
	public void updateStorekeeperIncentiveTransaction(StorekeeperIncentiveTransaction objectinstance) {
		daosincenttrans.update(objectinstance);
	}

	@Override
	public ChildLotteryParams findChildLotteryParamsById(short childLotteryParamsId, boolean readonly, String[] mappingsToJoin) {
		ChildLotteryParams list = daoclottparams.findById(childLotteryParamsId, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<ChildLotteryParams> getAllChildLotteryParams(int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<ChildLotteryParams> list = daoclottparams.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<ChildLotteryParams> findChildLotteryParamsByCriteria(Short enrollmentVaccine, Short receivedVaccine, 
			Date createdDateLower, Date createdDateUpper,
			Float criteriaRangeLower, Float criteriaRangeUpper,
			int firstResult, int fetchsize, boolean readonly,
			String[] mappingsToJoin) {
		List<ChildLotteryParams> list = daoclottparams.findByCriteria(enrollmentVaccine, receivedVaccine, createdDateLower, createdDateUpper, criteriaRangeLower, criteriaRangeUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveChildLotteryParams(ChildLotteryParams objectinstance) {
		return daoclottparams.save(objectinstance);
	}

	@Override
	public ChildLotteryParams mergeChildLotteryParams(ChildLotteryParams objectinstance) {
		return (ChildLotteryParams) daoclottparams.merge(objectinstance);
	}

	@Override
	public void updateChildLotteryParams(ChildLotteryParams objectinstance) {
		daoclottparams.update(objectinstance);
	}

	@Override
	public ChildLottery findChildLotteryById(int childLotteryId, boolean readonly, String[] mappingsToJoin) {
		ChildLottery obj = daochildlott.findById(childLotteryId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<ChildLottery> findChildLotteryByVaccination( int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin) {
		List<ChildLottery> objl = daochildlott.findByVaccination(vaccinationRecordNum, readonly, mappingsToJoin);
		return objl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ChildLottery> findChildLotteryByVaccination(short vaccineId, int childId, VACCINATION_STATUS vaccinationStatus, boolean readonly, String[] mappingsToJoin) throws VaccinationDataException {
		List vl = sc.getCustomQueryService().getDataBySQL("SELECT vaccinationRecordNum FROM vaccination WHERE childId="+childId+" AND vaccineId="+vaccineId+" AND vaccinationStatus='"+vaccinationStatus+"' ");
		if(vl.size() > 1){
			throw new VaccinationDataException(VaccinationDataException.MULITPLE_VACCINES_WITH_SAME_STATUS_FOR_CHILD);
		}
		
		int vRecNum = vl.size()==0?-99:Integer.parseInt(vl.get(0).toString());
		
		List<ChildLottery> objl = daochildlott.findByVaccination(vRecNum, readonly, mappingsToJoin);

		return objl;
	}

	@Override
	public List<ChildLottery> getAllChildLottery(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<ChildLottery> vl = daochildlott.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}

	@Override
	public List<ChildLottery> findChildLotteryByCriteria(String code, Integer childId, Short vaccineId,  
			Boolean hasWonLottery, Date lotteryDateFrom, Date lotteryDateTo, Date transactionDateFrom, Date transactionDateTo, 
			Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus, Integer storekeeperId, Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<ChildLottery> vl = daochildlott.findByCriteria(code, childId, vaccineId, hasWonLottery, lotteryDateFrom, lotteryDateTo, transactionDateFrom, transactionDateTo, consumptionDateFrom, consumptionDateTo, codeStatus, storekeeperId, amountFrom, amountTo, areaLocationId, firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}

	@Override
	public Serializable saveChildLottery(ChildLottery childLottery) {
		return daochildlott.save(childLottery);
	}

	@Override
	public void updateChildLottery(ChildLottery childLottery) {
		daochildlott.update(childLottery);
	}

	@Override
	public ChildLottery mergeChildLottery(ChildLottery childLottery) {
		return (ChildLottery) daochildlott.merge(childLottery);
	}
}
