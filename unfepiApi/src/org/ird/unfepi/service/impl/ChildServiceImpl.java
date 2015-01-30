package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Screening;
import org.ird.unfepi.model.dao.DAOChild;
import org.ird.unfepi.model.dao.DAOLotterySms;
import org.ird.unfepi.model.dao.DAOScreening;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;
import org.ird.unfepi.utils.date.DateUtils;

public class ChildServiceImpl implements ChildService{
	
	private ServiceContext sc;

	private DAOChild chldao;
	
	private DAOLotterySms daolotsms;
	
	private DAOScreening daoscr;
	
	public ChildServiceImpl(ServiceContext sc, DAOChild pat, DAOScreening daoscr, DAOLotterySms daolotsms/*,DAOReminder rem,DAOChildCell pcell*/){
		this.sc = sc;
		this.chldao=pat;
		this.daoscr = daoscr;
		this.daolotsms = daolotsms;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Child.class){
			return chldao.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == LotterySms.class){
			return daolotsms.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == Screening.class){
			return daoscr.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}
	
	@Override
	public List<Child> getAllChildren(boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin) {
		List<Child> plst= chldao.getAll(isreadonly, firstResult, fetchsize, mappingsToJoin);
		return plst;
	}

	@Override
	public LotterySms findLotterySmsBySerialNumber(int serialNumber, boolean isreadonly, String[] mappingsToJoin) {
		LotterySms obj= daolotsms.findBySerialNumber(serialNumber, isreadonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<LotterySms> findLotterySmsByChild(int mappedId,	boolean isreadonly
			, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		List<LotterySms> objl= daolotsms.findByChild(mappedId, isreadonly, firstResult, fetchsize, mappingsToJoin);
		return objl;
	}

	@Override
	public List<LotterySms> findLotterySmsByCriteria(String programId,
			Date datePreferenceChangedlower, Date datePreferenceChangedUpper,
			String preferredSmsTiming, Boolean hasApprovedLottery,
			Boolean hasApprovedReminders, Boolean hasCellPhoneAccess,
			boolean isreadonly, int firstResult, int fetchsize,	String[] mappingsToJoin) 
	{
		List<LotterySms> objl= daolotsms.findByCriteria(programId, datePreferenceChangedlower, datePreferenceChangedUpper, preferredSmsTiming, hasApprovedLottery, hasApprovedReminders, hasCellPhoneAccess, isreadonly, firstResult, fetchsize, mappingsToJoin);
		return objl;
	}
	
	@Override
	public Child findChildByIdNoJoins(int mappedId){
		Child p=chldao.findByIdNoJoins(mappedId);
		return p;
	}
	
	@Override
	public Child findChildByIdNoJoins(String programId, boolean isreadonly){
		Child p=chldao.findByIdNoJoins(programId, isreadonly);
		return p;
	}
	
	@Override
	public Child findChildById(int mappedId, boolean isreadonly, String[] mappingsToJoin){
		Child p=chldao.findById(mappedId, isreadonly, mappingsToJoin);
		return p;
	}
	
	@Override
	public Child findChildById(String programId, boolean isreadonly, String[] mappingsToJoin){
		Child p=chldao.findById(programId, isreadonly, mappingsToJoin);
		return p;
	}
	
	@Override
	public List<Child> findChildByCriteria(String programIdLike, String partOfName, Date birthdatelower, Date birthdateUpper,
			String nic, Gender gender, String ethnicity, String religion, String language, STATUS status, boolean putNotWithStatus
			, Date dateEnrolledlower, Date dateEnrolleduppr, String creatorId, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin) 
	{
		List<Child> plst= chldao.findByCriteria(programIdLike, partOfName, birthdatelower, birthdateUpper, nic, gender
				, ethnicity, religion, language, status, putNotWithStatus, dateEnrolledlower, dateEnrolleduppr, creatorId
				, isreadonly, firstResult, fetchsize, mappingsToJoin);
		return plst;
	}
	
	@Override
	public Serializable saveChild(Child child)
	{
		return chldao.save(child);
	}
	
	@Override
	public void updateChild(Child child) throws ChildDataInconsistencyException{
		if(child.getBirthdate() != null && child.getBirthdate().after(new Date())){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_BIRTHDATE_INVALID, ChildDataInconsistencyException.CHILD_BIRTHDATE_INVALID);
		}

		chldao.update(child);
	}
	
	@Override
	public Child mergeUpdateChild(Child child) throws ChildDataInconsistencyException{
		if(child.getBirthdate() != null && child.getBirthdate().after(new Date())){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_BIRTHDATE_INVALID, ChildDataInconsistencyException.CHILD_BIRTHDATE_INVALID);
		}
		
		return (Child) chldao.merge(child);
	}
	
	@Override
	public Screening findScreeningById(int screeningid, boolean isreadonly, String[] mappingsToJoin ){
		Screening obj = daoscr.findById(screeningid, isreadonly, mappingsToJoin);
		return obj;
	}
	
	@Override
	public List<Screening> getAllScreening(boolean isreadonly, int firstResult,
			int fetchsize, String[] mappingsToJoin) {
		List<Screening> list = daoscr.getAll(isreadonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}

	@Override
	public List<Screening> findScreeningByMappedId(int mappedId, boolean isreadonly, String[] mappingsToJoin) 
	{
		List<Screening> obj = daoscr.findByMappedId(mappedId, isreadonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<Screening> findScreeningByProgramId(String programId,
			boolean isreadonly, String[] mappingsToJoin) 
	{
		List<Screening> obj = daoscr.findByProgramId(programId, isreadonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<Screening> findScreeningByCriteria(Integer vaccinatorId,
			Integer vaccinationCenterId, /*String epiNumber,*/
			Date screeningDatelower, Date screeningDateupper,
			boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		if (screeningDatelower != null) {
			screeningDatelower=DateUtils.truncateDatetoDate(screeningDatelower);
		}
		if (screeningDateupper != null) {
			screeningDateupper=DateUtils.roundoffDatetoDate(screeningDateupper);
		}
		List<Screening> list = daoscr.findByCriteria(vaccinatorId, vaccinationCenterId/*, epiNumber*/, screeningDatelower, screeningDateupper, isreadonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveScreening(Screening screening) throws ChildDataInconsistencyException {
		/*if(!DataValidation.validate(REG_EX.NUMERIC, Long.toString(screening.getScreeningId()), Globals.CHILD_ID_LEN,  Globals.CHILD_ID_LEN)){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ID_LEN_INVALID, ChildDataInconsistencyException.CHILD_ID_LEN_INVALID);
		}*/
		return daoscr.save(screening);
	}
	
	@Override
	public void updateScreening(Screening screening) {
		daoscr.update(screening);
	}
	
	@Override
	public Screening mergeUpdateScreening(Screening screening) {
		return (Screening) daoscr.merge(screening);
	}

	@Override
	public Serializable saveLotterySms(LotterySms lotterySms){
		return daolotsms.save(lotterySms);
	}

	@Override
	public void updateLotterySms(LotterySms lotterySms) {
		daolotsms.update(lotterySms);
	}

	@Override
	public LotterySms mergeUpdateLotterySms(LotterySms lotterySms) {
		return (LotterySms) daolotsms.merge(lotterySms);
	}

}
