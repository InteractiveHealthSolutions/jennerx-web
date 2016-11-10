package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
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
import org.ird.unfepi.model.dao.DAOChildIncentive;
import org.ird.unfepi.model.dao.DAOIncentiveParams;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveEvent;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOStorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentive;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveEvent;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveParticipant;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveTransaction;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.exception.VaccinationDataException;
import org.ird.unfepi.service.IncentiveService;

public class IncentiveServiceImpl implements IncentiveService{

	private ServiceContext sc;

	private DAOVaccinatorIncentiveEvent daovlottevent;
	private DAOVaccinatorIncentiveParticipant daovlottparti;
	private DAOVaccinatorIncentiveTransaction daovlotttrans;
	private DAOVaccinatorIncentiveWorkProgress daovlottwinrcords;
	
	private DAOStorekeeperIncentiveEvent daosincentevent;
	private DAOStorekeeperIncentiveParticipant daosincentparti;
	private DAOStorekeeperIncentiveTransaction daosincenttrans;
	private DAOStorekeeperIncentiveWorkProgress daosincentwinrcords;
	
	private DAOChildIncentive daochildincentive;
	
	private DAOIncentiveParams daoincentiveparams;
	private DAOVaccinatorIncentive daovaccinatorincentive;

	public IncentiveServiceImpl(DAOVaccinatorIncentiveEvent daovlottevent, DAOVaccinatorIncentiveParticipant daovlottparti
			, DAOVaccinatorIncentiveTransaction daovlotttrans
			, DAOVaccinatorIncentiveWorkProgress daovlottwinrcords,
			DAOStorekeeperIncentiveEvent daosincentevent, DAOStorekeeperIncentiveParticipant daosincentparti
			, DAOStorekeeperIncentiveTransaction daosincenttrans
			, DAOStorekeeperIncentiveWorkProgress daosincentwinrcords, 
			DAOChildIncentive daochildincentive,DAOVaccinatorIncentive daovincentive, DAOIncentiveParams daoincentiveparams, ServiceContext sc) {
		this.daovlottevent = daovlottevent;
		this.daovlottparti = daovlottparti;
		this.daovlotttrans = daovlotttrans;
		this.daovlottwinrcords = daovlottwinrcords;
		
		this.daosincentevent = daosincentevent;
		this.daosincentparti = daosincentparti;
		this.daosincenttrans = daosincenttrans;
		this.daosincentwinrcords = daosincentwinrcords;
		
		this.daochildincentive = daochildincentive;
		
		this.daovaccinatorincentive = daovincentive;
		
		this.daoincentiveparams = daoincentiveparams;
		
		this.sc = sc;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == VaccinatorIncentiveEvent.class){
			return daovlottevent.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentiveParticipant.class){
			return daovlottparti.LAST_QUERY_TOTAL_ROW_COUNT();
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
		else if(clazz == StorekeeperIncentiveTransaction.class){
			return daosincenttrans.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == StorekeeperIncentiveWorkProgress.class){
			return daosincentwinrcords.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		else if(clazz == ChildIncentive.class){
			return daochildincentive.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinatorIncentive.class){
			return daovaccinatorincentive.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == IncentiveParams.class){
			return daoincentiveparams.LAST_QUERY_TOTAL_ROW_COUNT();
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
	public ChildIncentive findChildIncentiveById(int childIncentiveId, boolean readonly, String[] mappingsToJoin) {
		ChildIncentive obj = daochildincentive.findById(childIncentiveId, readonly, mappingsToJoin);
		return obj;
	}
	
	@Override
	public List<ChildIncentive> findChildIncentiveByArm(int armId,
			boolean readonly, String[] mappingsToJoin) {
		List<ChildIncentive> objl = daochildincentive.findByArm(armId, readonly, mappingsToJoin);
		return objl;
	}

	@Override
	public List<ChildIncentive> findChildIncentiveByVaccination( int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin) {
		List<ChildIncentive> objl = daochildincentive.findByVaccination(vaccinationRecordNum, readonly, mappingsToJoin);
		return objl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<ChildIncentive> findChildIncentiveByVaccination(short vaccineId, int childId, VACCINATION_STATUS vaccinationStatus, boolean readonly, String[] mappingsToJoin) throws VaccinationDataException {
		List vl = sc.getCustomQueryService().getDataBySQL("SELECT vaccinationRecordNum FROM vaccination WHERE childId="+childId+" AND vaccineId="+vaccineId+" AND vaccinationStatus='"+vaccinationStatus+"' ");
		if(vl.size() > 1){
			throw new VaccinationDataException(VaccinationDataException.MULITPLE_VACCINES_WITH_SAME_STATUS_FOR_CHILD);
		}
		
		int vRecNum = vl.size()==0?-99:Integer.parseInt(vl.get(0).toString());
		
		List<ChildIncentive> objl = daochildincentive.findByVaccination(vRecNum, readonly, mappingsToJoin);

		return objl;
	}

	@Override
	public List<ChildIncentive> getAllChildIncentive(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<ChildIncentive> vl = daochildincentive.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}

	@Override
	public List<ChildIncentive> findChildIncentiveByCriteria(/*String code,*/ Integer armId, Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, IncentiveStatus incentiveStatus, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<ChildIncentive> vl = daochildincentive.findByCriteria(/*code,*/ armId, childId, vaccineId, hasWonIncentive, incentiveStatus, incentiveDateFrom, incentiveDateTo, transactionDateFrom, transactionDateTo, /*consumptionDateFrom, consumptionDateTo, codeStatus,*/ /*storekeeperId,*/ amountFrom, amountTo, areaLocationId, firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}
	
	@Override
	public List<ChildIncentive> findChildIncentiveByCriteria(/*String code,*/ Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*//* Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<ChildIncentive> vl = daochildincentive.findByCriteria(/*code,*/  childId, vaccineId, hasWonIncentive, incentiveDateFrom, incentiveDateTo, transactionDateFrom, transactionDateTo, /*consumptionDateFrom, consumptionDateTo, codeStatus,*/ /*storekeeperId,*/ amountFrom, amountTo, areaLocationId, firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}
	
	@Override
	public Serializable saveChildIncentive(ChildIncentive childIncentive) {
		return daochildincentive.save(childIncentive);
	}

	@Override
	public void updateChildIncentive(ChildIncentive childIncentive) {
		daochildincentive.update(childIncentive);
	}

	@Override
	public ChildIncentive mergeChildIncentive(ChildIncentive childIncentive) {
		return (ChildIncentive) daochildincentive.merge(childIncentive);
	}
	
	
	
	/* Incentive Params */
	
	@Override
	public Serializable saveIncentiveParams(IncentiveParams incentiveParams) {
		return daoincentiveparams.save(incentiveParams);
	}

	@Override
	public void updateIncentiveParams(IncentiveParams incentiveParams) {
		daoincentiveparams.update(incentiveParams);
	}

	@Override
	public IncentiveParams mergeIncenvtiveParams(IncentiveParams incentiveParams) {
		return (IncentiveParams) daoincentiveparams.merge(incentiveParams);
	}

	@Override
	public IncentiveParams findIncentiveParamsById(short incentiveParamsId,
			boolean readonly, String[] mappingsToJoin) {
		IncentiveParams obj = daoincentiveparams.findById(incentiveParamsId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<IncentiveParams> getAllIncentiveParams(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<IncentiveParams> vl = daoincentiveparams.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	}

	@Override
	public List<IncentiveParams> findIncentiveParamsByCriteria(Short vaccineId,
			Integer armId, Short roleId, Date createdDateLower,
			Date createdDateUpper, Float criteriaRangeLower,
			Float criteriaRangeUpper, int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		List<IncentiveParams> list = daoincentiveparams.findByCriteria(vaccineId, armId, roleId, createdDateLower, createdDateUpper, criteriaRangeLower, criteriaRangeUpper, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}
	
	/* VaccinatorIncentive */
	
	@Override
	public List<VaccinatorIncentive> findVaccinatorIncentiveByVaccination(
			int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin) {
		List<VaccinatorIncentive> objl = daovaccinatorincentive.findByVaccination(vaccinationRecordNum, readonly, mappingsToJoin);
		return objl;
	}

	@Override
	public List<VaccinatorIncentive> getAllVaccinatorIncentive(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		List<VaccinatorIncentive> vl = daovaccinatorincentive.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return vl;
	
	}

	@Override
	public List<VaccinatorIncentive> findVaccinatorIncentiveByCriteriaVaccinatorIncentivized(
			Integer vaccinatorId, Boolean isIncentivized, IncentiveStatus incentiveStatus, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentive> vl = daovaccinatorincentive.findByCriteriaVaccinatorIncentivized(vaccinatorId,isIncentivized,incentiveStatus, readonly, mappingsToJoin);
		return vl;
	}

	@Override
	public List<VaccinatorIncentive> findVaccinatorIncentiveByCriteria(Integer armId,
			Integer vaccinator, Short vaccineId, Boolean isIncentivized, IncentiveStatus incentiveStatus, 
			Date incentiveDateFrom, Date incentiveDateTo, Integer amountFrom,
			Integer amountTo, Integer areaLocationId, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<VaccinatorIncentive> vl = daovaccinatorincentive.findByCriteria(armId,vaccinator,vaccineId, isIncentivized, incentiveStatus, incentiveDateFrom, incentiveDateTo,amountFrom, amountTo, areaLocationId,firstResult,fetchsize, readonly,  mappingsToJoin);
		return vl;
	}
	
	@Override
	public List<VaccinatorIncentive> findVaccinatorIncentiveByCriteriaVaccinatorRecordNum(
			Integer vaccinatorRecordNum, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentive> vl = daovaccinatorincentive.findByCriteriaVaccinatorRecordNum(vaccinatorRecordNum, readonly,  mappingsToJoin);
		return vl;
	}

	@Override
	public List<VaccinatorIncentive> findVaccinatorIncentiveByArm(int armId, boolean readonly,
			String[] mappingsToJoin) {
		List<VaccinatorIncentive> vl = daovaccinatorincentive.findByArm(armId, readonly, mappingsToJoin);

		return vl;
	}

	@Override
	public Serializable saveVaccinatorIncentive(
			VaccinatorIncentive vaccinatorIncentive) {
		return daovaccinatorincentive.save(vaccinatorIncentive);
	}

	@Override
	public void updateVaccinatorIncentive(
			VaccinatorIncentive vaccinatorIncentive) {
		daovaccinatorincentive.update(vaccinatorIncentive);
	}

	@Override
	public VaccinatorIncentive mergeVaccinatorIncentive(
			VaccinatorIncentive vaccinatorIncentive) {
		return (VaccinatorIncentive) daovaccinatorincentive.merge(vaccinatorIncentive);
	}

	

	


}
