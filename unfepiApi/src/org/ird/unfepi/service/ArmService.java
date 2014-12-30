/*
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.ArmDayReminderId;

/**
 * The Interface ArmService.
 */
public interface ArmService {
	
	/**
	 * LAST s_ row s_ returne d_ count.
	 *
	 * @return the number
	 */
	Number LASTS_ROWS_RETURNED_COUNT();

	//ArmIdMap findByChildIdToMap(int id);

	//List<ArmIdMap> getAllChildIdToMap();

	//List<ArmIdMap> getByChildIdsOccupied(boolean idsSwitchValue);
	
	/**
	 * Gets the all.
	 *
	 * @param isReadOnly the is read only
	 * @return the all
	 */
	List<Arm> getAll(boolean isReadOnly);

	/**
	 * Gets the by arm by name, Arm object is readonly(modification to object will not be reflected in database) 
	 * and Fetched with mode JOIN on ArmDayReminder.
	 *
	 * @param armName the arm name
	 * @return the by name
	 */
	Arm getByName(String armName);
	
	/**
	 * Update arm.
	 *
	 * @param arm the arm
	 */
	void updateArm(Arm arm);

	/**
	 * Adds the arm.
	 *
	 * @param arm the arm
	 * @return the serializable
	 */
	Serializable addArm(Arm arm);

	/**
	 * donot seem to be very useful.
	 *
	 * @param armname the armname
	 * @param fetchmode the fetchmode
	 * @param isReadOnly the is read only
	 * @return the list
	 */
	List<Arm> matchByCriteria(String armname, boolean joinArmday, boolean isReadOnly);

	/**
	 * Find arm day reminder by id.
	 *
	 * @param armdayReminderID the armday reminder id
	 * @return the arm day reminder
	 */
	ArmDayReminder findArmDayReminderById(ArmDayReminderId armdayReminderID);

	/**
	 * Gets the all arm day reminders.
	 *
	 * @return the all arm day reminders
	 */
	List<ArmDayReminder> getAllArmDayReminders();

	/**
	 * Find arm day reminder by ids criteria.
	 *
	 * @param armId the arm id
	 * @param vaccineId the vaccine id
	 * @param reminderId the reminder id
	 * @param dayNum the day num
	 * @return the list
	 */
	List<ArmDayReminder> findArmDayReminderByIdsCriteria(Short armId,
			Short vaccineId, Short reminderId, Short dayNum);

	//void addArmIdMap(ArmIdMap armidmap);

	//void updateArmIdMap(ArmIdMap armidmap);

}
