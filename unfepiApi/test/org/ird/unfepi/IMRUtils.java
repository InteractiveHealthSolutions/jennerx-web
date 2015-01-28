/*
 * 
 */
package org.ird.unfepi;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.ArmDayReminder;
import org.ird.unfepi.model.Model.TimeIntervalUnit;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.ArmDayTiming;

/**
 * The Class IMRUtils.
 */
public class IMRUtils {

	public static Vaccine getNextVaccine(String currentVaccine, ServiceContext sc){
		return sc.getVaccinationService().getByName(getNextVaccineName(currentVaccine));
	}

	public static String getNextVaccineName(String currentVaccine){
		return "";//Context.getIRSetting("vaccine.next-vaccine."+currentVaccine.trim().toLowerCase(), "Not Found");
	}
	
	public static Date calculateNextVaccinationDateExceptM2(Date previousMileStoneDate, Date vaccinationDate, int vaccinationCenterId,String vaccineName)
	{
		Calendar actNextAssignedDate=Calendar.getInstance();
		actNextAssignedDate.setTime(previousMileStoneDate);
		
		int gaptovvac = 0;
		TimeIntervalUnit unit = null;
		
		ServiceContext sc = Context.getServices();

		Vaccine vaccine = sc.getVaccinationService().getByName(vaccineName);
		
		List<VaccinationCenterVaccineDay> vcdl = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccinationCenterId, vaccine.getVaccineId(), null, true);
		
		//gaptovvac=vaccine.getGapFromPreviousMilestone();
		//unit=vaccine.getPrevGapUnit();
		
		if(unit.equals(TimeIntervalUnit.DAY)){
			actNextAssignedDate.add(Calendar.DATE, gaptovvac);

		}else if(unit.equals(TimeIntervalUnit.WEEK)){
			actNextAssignedDate.add(Calendar.DATE, gaptovvac*7);

		}else if(unit.equals(TimeIntervalUnit.MONTH)){
			actNextAssignedDate.add(Calendar.MONTH, gaptovvac);

		}else if(unit.equals(TimeIntervalUnit.YEAR)){
			actNextAssignedDate.add(Calendar.YEAR, gaptovvac);
		}
		
		boolean dayAllowed = false;
		for (VaccinationCenterVaccineDay alvd : vcdl) {
			if(actNextAssignedDate.get(Calendar.DAY_OF_WEEK) == alvd.getId().getDayNumber()){
				dayAllowed = true;
				break;
			}
		}
		
		if(!dayAllowed){
			int firstAllowedDay = vcdl.size()>0?vcdl.get(0).getId().getDayNumber():2;//else monday
			int currday = actNextAssignedDate.get(Calendar.DAY_OF_WEEK);

			int dayToAdd = firstAllowedDay >= currday ? (firstAllowedDay-currday) : (7-currday+firstAllowedDay);
			actNextAssignedDate .add(Calendar.DATE, Math.abs(dayToAdd));
		}

		return actNextAssignedDate.getTime().before(vaccinationDate)?vaccinationDate:actNextAssignedDate.getTime();
	}
	/** set timings null if you want default timings to be used. or if custom timing is needed use following syntax
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
	 * @param vaccination
	 * @param arm
	 * @param timings
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	public static List<ReminderSms> createReminderSms(Vaccination vaccination, Arm arm, ArmDayTiming timings){
		List<ReminderSms> reminderSmses = new ArrayList<ReminderSms>();
		for (ArmDayReminder armday : arm.getArmday()) {
			ReminderSms rs = new ReminderSms();
			rs.setDescription("MANUAL");
			rs.setCreatedDate(new Date());
			rs.setDayNumber(armday.getId().getDayNumber());
			Time dueTime = armday.getDefaultReminderTime();
			if(timings != null && armday.getIsDefaultTimeEditable() && timings.getTiming(armday.getId().getDayNumber()) != null){
				dueTime= timings.getTiming(armday.getId().getDayNumber());
			}
			

			Calendar cal=Calendar.getInstance();
			cal.setTime(vaccination.getVaccinationDuedate());
			cal.set(Calendar.HOUR_OF_DAY, dueTime.getHours());
			cal.set(Calendar.MINUTE, dueTime.getMinutes());
			cal.set(Calendar.SECOND, dueTime.getSeconds());
			cal.add(Calendar.DATE, armday.getId().getDayNumber());
			
			rs.setDueDate(cal.getTime());
			rs.setReminderStatus(REMINDER_STATUS.PENDING);
			rs.setVaccinationRecordNum(vaccination.getVaccinationRecordNum());
			
			reminderSmses.add(rs);
		}
		return reminderSmses;
	}

	public static void createMeasles1(int childId, Date birthdate, Date enrollmentDate, int vaccinationCenterId, boolean createReminders, User creator, ServiceContext sc) throws InvalidAttributeValueException{
		Vaccination mv = new Vaccination();
		mv.setChildId(childId);
		mv.setCreator(creator);
		Vaccine measles1 = sc.getVaccinationService().getByName(Globals.MEASLES1_NAME_IN_DB);
		mv.setVaccineId(measles1.getVaccineId());
		mv.setVaccinationDuedate(calculateNextVaccinationDateExceptM2(birthdate, enrollmentDate, vaccinationCenterId, Globals.MEASLES1_NAME_IN_DB));
		mv.setVaccinationStatus(VACCINATION_STATUS.PENDING);
		
		int mid = Integer.parseInt(sc.getVaccinationService().addVaccinationRecord(mv).toString());
		mv.setVaccinationRecordNum(mid);
		
		if(createReminders){
			for (ReminderSms rem : createReminderSms(mv, Globals.DEFAULT_ARM, null)) {
				sc.getReminderService().addReminderSmsRecord(rem);
			}
		}
	}
	
}
