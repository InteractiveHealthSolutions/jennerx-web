package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.exception.ChildDataException;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;

public interface ChildService {
	
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	LotterySms findLotterySmsBySerialNumber(int serialNumber, boolean isreadonly, String[] mappingsToJoin);

	List<LotterySms> findLotterySmsByChild(int mappedId, boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	List<LotterySms> findLotterySmsByCriteria(String programId, Date datePreferenceChangedlower, Date datePreferenceChangedUpper, String preferredSmsTiming, Boolean hasApprovedLottery, Boolean hasApprovedReminders, Boolean hasCellPhoneAccess, boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	Serializable saveLotterySms(LotterySms lotterySms);

	void updateLotterySms(LotterySms lotterySms);
	
	LotterySms mergeUpdateLotterySms(LotterySms lotterySms);
	
	List<Child> getAllChildren(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);	
	/**
	 * Checks if is cell number available.
	 *
	 * @param cellnumber the cellnumber
	 * @return true, if is cell number available
	 * @throws ChildDataException the child data exception
	 */
/*	boolean isCellNumberAvailable(String cellnumber) throws ChildDataException ;
*/	
	Child findChildByIdNoJoins(int mappedId);
	
	Child findChildByIdNoJoins(String programId, boolean isreadonly);	
	
	Child findChildById(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	Child findChildById(String programId, boolean isreadonly, String[] mappingsToJoin);

/*	Child getChildbyActiveCell(String currentCell,boolean isreadonly, boolean joinArm, boolean joinAddresses,FetchMode armFetchmode) throws ChildDataException;
*/	
	//List<Child> findByEpiOrMrNumber(String epiOrMrNumber);
	
	List<Child> findChildByCriteria(String programIdLike, String partOfName, Date birthdatelower, Date birthdateUpper
			, String nic, Gender gender, String ethnicity, String religion, String language, STATUS status, boolean putNotWithStatus
			, Date dateEnrolledlower, Date dateEnrolleduppr, String creatorId, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin);
	/**
	 * Update child.
	 *
	 * @param child the child
	 * @throws ChildDataInconsistencyException 
	 */
	void updateChild(Child child) throws ChildDataInconsistencyException;

	/**
	 * Save child after basic validation. child must have birthdate if 2nd parameter is true. other mendatory attributes are creator info, enrollment date, id length, arm
	 * If 2nd parameter is true a vaccintion record for Measles1 with Reminders will be created automatically
	 * 
	 * set timings null if you want default timings to be used. or if custom timing is needed use following syntax
	 * 
	 * ArmDayTiming a = new ArmDayTiming()p
	 * a.addDayTiming(1, time1); //time is java.sql.Time object
	 * a.addDayTiming(-3, time2);
	 * ....
	 * 
	 * remember that custom timing will be assigned if and only if Timing is editable for that day
	 * NOTE: vaccination and child must be saved before calling this method or alternatively after getting result 
	 * set vaccinationRecordNumber for each reminderSms
	 *
	 * @param child the child
	 * @param createMeasles1RecordWithSms
	 * @param timings
	 * @return the serializable
	 * @throws InvalidAttributeValueException 
	 * @throws ChildDataInconsistencyException 
	 */
	Serializable saveChild(Child child);
	
	//List<Child> findByCurrentCellIgnoreInconsistency(String currentCellNumber,boolean isreadonly/*,FetchMode armFetchmode*/);

	/**
	 * Merge update child.
	 *
	 * @param child the child
	 * @return the child
	 * @throws ChildDataInconsistencyException 
	 */
	Child mergeUpdateChild(Child child) throws ChildDataInconsistencyException;
	
	Serializable saveArm(Arm arm);
	void updateArm(Arm arm);
	Arm mergeArm(Arm arm);
	Arm findArmById(short armId,
			boolean readonly, String[] mappingsToJoin);
	List<Arm> getAllArm(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin);
    List<Arm> getAllArm (boolean readonly, String[] mappingsToJoin,	String orderBySqlFormula);
	
	
}
