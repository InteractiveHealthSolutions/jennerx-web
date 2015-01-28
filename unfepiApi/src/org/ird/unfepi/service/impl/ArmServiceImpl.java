/*
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.ArmDayReminderId;
import org.ird.unfepi.model.dao.DAOArm;
import org.ird.unfepi.model.dao.DAOArmDayReminder;
import org.ird.unfepi.service.ArmService;

/**
 * The Class ArmServiceImpl.
 */
public class ArmServiceImpl implements ArmService{

/** The LAST s_ row s_ returne d_ count. */
private Number LASTS_ROWS_RETURNED_COUNT;
	
	private ServiceContext sc;
	/** The daoarm. */
	private DAOArm daoarm;
	
	/** The daoardayrem. */
	private DAOArmDayReminder daoardayrem;
	//private DAOArmIdMap daoarmidmap;
	/**
	 * Instantiates a new arm service impl.
	 *
	 * @param daoarm the daoarm
	 * @param daoardayrem the daoardayrem
	 */
	public ArmServiceImpl(ServiceContext sc,DAOArm daoarm,DAOArmDayReminder daoardayrem/*,DAOArmIdMap daoarmidmap*/) {
		this.sc = sc;
		this.daoarm=daoarm;
		this.daoardayrem=daoardayrem;
		//this.daoarmidmap=daoarmidmap;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#getAll(boolean)
	 */
	@Override
	public List<Arm> getAll(boolean isReadOnly) {
		List<Arm> arml= daoarm.getAll(isReadOnly);
		setLASTS_ROWS_RETURNED_COUNT(arml.size());
		return arml;
	}
	
	/**
	 * Sets the lAST s_ row s_ returne d_ count.
	 *
	 * @param lASTS_ROWS_RETURNED_COUNT the new lAST s_ row s_ returne d_ count
	 */
	private void setLASTS_ROWS_RETURNED_COUNT(Number lASTS_ROWS_RETURNED_COUNT) {
		LASTS_ROWS_RETURNED_COUNT = lASTS_ROWS_RETURNED_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#LASTS_ROWS_RETURNED_COUNT()
	 */
	@Override
	public Number LASTS_ROWS_RETURNED_COUNT() {
		return LASTS_ROWS_RETURNED_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#getByName(java.lang.String, org.hibernate.FetchMode, boolean)
	 */
	@Override
	public Arm getByName(String armName) {
		Arm arm=daoarm.getByName(armName);
		setLASTS_ROWS_RETURNED_COUNT(arm==null?0:1);
		return arm;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#matchByCriteria(java.lang.String, org.hibernate.FetchMode, boolean)
	 */
	@Override
	public List<Arm> matchByCriteria(String armname, boolean joinArmday,boolean isreadonly) {
		List<Arm> arml=daoarm.matchByCriteria(armname, joinArmday,isreadonly);
		setLASTS_ROWS_RETURNED_COUNT(arml.size());
		return arml;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#addArm(org.ird.unfepi.model.Arm)
	 */
	@Override
	public Serializable addArm(Arm arm){
		return daoarm.save(arm);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#updateArm(org.ird.unfepi.model.Arm)
	 */
	@Override
	public void updateArm(Arm arm){
		daoarm.update(arm);
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#findArmDayReminderById(org.ird.unfepi.model.ArmDayReminderId)
	 */
	@Override
	public ArmDayReminder findArmDayReminderById(ArmDayReminderId armdayReminderID) {
		ArmDayReminder adr=daoardayrem.findById(armdayReminderID);
		setLASTS_ROWS_RETURNED_COUNT(adr==null?0:1);
		return adr;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#findArmDayReminderByIdsCriteria(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ArmDayReminder> findArmDayReminderByIdsCriteria(Short armId,
			Short vaccineId, Short reminderId, Short dayNum) {
		List<ArmDayReminder> adrl=daoardayrem.findByIdsCriteria(armId, vaccineId, reminderId, dayNum);
		setLASTS_ROWS_RETURNED_COUNT(adrl.size());
		return adrl;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.ArmService#getAllArmDayReminders()
	 */
	@Override
	public List<ArmDayReminder> getAllArmDayReminders() {
		List<ArmDayReminder> adrl=daoardayrem.getAll();
		setLASTS_ROWS_RETURNED_COUNT(adrl.size());
		return adrl;
	}
	/*@Override
	public ArmIdMap findByChildIdToMap(int id) {
		ArmIdMap map=daoarmidmap.findByChildIdToMap(id);
		setLASTS_ROWS_RETURNED_COUNT(map==null?0:1);
		return map;
	}
	@Override
	public List<ArmIdMap> getAllChildIdToMap() {
		List<ArmIdMap> armidmap=daoarmidmap.getAll();
		setLASTS_ROWS_RETURNED_COUNT(armidmap.size());
		return armidmap;
	}
*//** 
 * @param idsSwitchValue boolean
 * @return ArmIdMap
 * *//*
	@Override
	public List<ArmIdMap> getByChildIdsOccupied(boolean idsSwitchValue) {
		List<ArmIdMap> armidmap=daoarmidmap.getByIdsOccupied(idsSwitchValue);
		setLASTS_ROWS_RETURNED_COUNT(armidmap.size());
		return armidmap;
	}
	@Override
	public void addArmIdMap(ArmIdMap armidmap) {
		daoarmidmap.save(armidmap);
	}
	@Override
	public void updateArmIdMap(ArmIdMap armidmap) {
		daoarmidmap.update(armidmap);
	}
*/
}
