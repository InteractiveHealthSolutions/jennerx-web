package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.dao.DAOArm;
import org.ird.unfepi.model.dao.DAOChild;
import org.ird.unfepi.model.dao.DAOLotterySms;
import org.ird.unfepi.service.ChildService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;

public class ChildServiceImpl implements ChildService{
	
	private ServiceContext sc;

	private DAOChild chldao;
	
	private DAOLotterySms daolotsms;
	
	private DAOArm daoarm;
	
	public ChildServiceImpl(ServiceContext sc, DAOChild pat, DAOLotterySms daolotsms, DAOArm daoarm/*,DAOReminder rem,DAOChildCell pcell*/){
		this.sc = sc;
		this.chldao=pat;
		this.daolotsms = daolotsms;
		this.daoarm = daoarm;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Child.class){
			return chldao.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == LotterySms.class){
			return daolotsms.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == Arm.class){
			return daoarm.LAST_QUERY_TOTAL_ROW_COUNT();
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
	
	
	/* Arm */
	@Override
	public Serializable saveArm(Arm arm) {
		return daoarm.save(arm);
	}

	@Override
	public void updateArm(Arm arm) {
		daoarm.update(arm);
	}

	@Override
	public Arm mergeArm(Arm arm) {
		return (Arm) daoarm.merge(arm);
	}

	@Override
	public Arm findArmById(short armId, boolean readonly,
			String[] mappingsToJoin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Arm> getAllArm(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		List<Arm> list = daoarm.getAllArm(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public List<Arm> getAllArm(boolean readonly, String[] mappingsToJoin,
			String orderBySqlFormula) {
		List<Arm> list = daoarm.getAll(readonly, mappingsToJoin,orderBySqlFormula);
		return list;
	}

}
