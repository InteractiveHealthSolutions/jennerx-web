/*
 * 
 */
package org.ird.unfepi.web.utils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.TimeIntervalUnit;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.utils.ArmDayTiming;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;

/**
 * The Class IMRUtils.
 */
public class IMRUtils {

	public static Vaccine getNextVaccine(String currentVaccine, ServiceContext sc){
		return sc.getVaccinationService().getByName(getNextVaccineName(currentVaccine));
	}

	/** Simple helper method to get vaccine from vaccination object	 */
	public static Vaccine getVaccineObject(Vaccination vacc, ServiceContext sc){
		if(vacc == null) return null;
		
		if(vacc.getVaccine() != null) return vacc.getVaccine();
		
		if(vacc.getVaccineId() != null) return sc.getVaccinationService().findVaccineById(vacc.getVaccineId());
		
		return null;
	}
	
	public static boolean isMeasles2Given(List<VaccineSchedule> vaccineSchedule, Date visitDate) {
		boolean measles2Given = false;
		for (VaccineSchedule vs : vaccineSchedule) {
			if(vs.getVaccine().getName().equalsIgnoreCase("measles2")
					&& vs.getVaccination_date() != null && visitDate != null 
					&& DateUtils.datesEqual(vs.getVaccination_date(),visitDate)){
				measles2Given = true;
			}
		}
		return measles2Given;
	}
	
	public static boolean passVaccinePrerequisiteCheck(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule) {
		// if no prerequisite defined pass
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return true;
		}
		
		// if any pre-req is mandatory and not satified block it
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) 
		{
			if(prereq.getMandatory() != null && prereq.getMandatory())
			{
				boolean prereqVaccineFoundInlist = false;// if prereq not in given schedule say prereq not satisfied
				
				for (VaccineSchedule vaccineSchedule : schedule) {
					if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()){
						prereqVaccineFoundInlist = true;
						if(!vaccineSchedule.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name()) 
							&& vaccineSchedule.getVaccination_date() == null){
							return false;
						}
					}
				}
				
				//if vaccine is mandatory and vaccine is missing from schedule say Prereq not satisfied
				if(!prereqVaccineFoundInlist){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static Date getPrerequisiteVaccineDate(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule) {
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return null;
		}
		//for now it assumes that only one prereq is possible
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) {
			for (VaccineSchedule vaccineSchedule : schedule) {
				if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()){
					return vaccineSchedule.getVaccination_date();
				}
			}
		}
		
		return null;
	}
	public static boolean isPrerequisiteVaccinatedOnCurrentVisit(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule) {
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return false;
		}
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) {
			for (VaccineSchedule vaccineSchedule : schedule) {
				if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()
						&& vaccineSchedule.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())
						&& vaccineSchedule.getVaccination_date() != null && DateUtils.datesEqual(vaccineSchedule.getVaccination_date(), vaccineSch.getVisitdate())){
					return true;
				}
			}
		}
		
		return false;
	}
	public static String getNextVaccineName(String currentVaccine){
		return Context.getSetting("vaccine.next-vaccine."+currentVaccine.trim().toLowerCase(), "Not Found");
	}
	
	public static VaccineGap getBirthdateGap(Vaccine vaccine){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getVaccineGapType().getName().toLowerCase().contains("birthdate")){
				return gap;
			}
		}
		return null;
	}
	
	public static VaccineGap getVaccineExpiryGap(Vaccine vaccine){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getVaccineGapType().getName().toLowerCase().contains("expir")){
				return gap;
			}
		}
		return null;
	}
	
	public static VaccineGap getBirthdateGap(short vaccineId){
		ServiceContext sc = Context.getServices();
		try{
			for (VaccineGap gap : Utils.initializeAndUnproxy(sc.getVaccinationService().findVaccineById(vaccineId).getVaccineGaps())) {
				if(gap.getVaccineGapType().getName().toLowerCase().contains("birthdate")){
					return gap;
				}
			}
		}
		finally {
			sc.closeSession();
		}
		return null;
	}
	
	public static VaccineGap getStandardGap(Vaccine vaccine){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getVaccineGapType().getName().toLowerCase().contains("standard")){
				return gap;
			}
		}
		return null;
	}
	
	public static VaccineGap getPreviousVaccineGap(Vaccine vaccine){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getVaccineGapType().getName().toLowerCase().contains("previous vaccine")){
				return gap;
			}
		}
		return null;
	}
	public static Date calculateVaccineDuedate(Vaccine vaccine, Date birthdate, Date prereqVaccineDate, Date previousVaccineDate, Integer vaccinationCenterId, ServiceContext sc)
	{
		Calendar calculatedDuedate = Calendar.getInstance();
		// Calculate date for next vaccine wrt birthdate first.
		Calendar actDuedateWrtBirthdate = Calendar.getInstance();
		actDuedateWrtBirthdate.setTime(birthdate);
		
		List<VaccinationCenterVaccineDay> vcdl = new ArrayList<VaccinationCenterVaccineDay>();
		if(vaccinationCenterId != null){
			vcdl = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccinationCenterId, vaccine.getVaccineId(), null, true);
		}
		
		VaccineGap gap = getBirthdateGap(vaccine);
		TimeIntervalUnit unit = null;
		
		if(gap != null){
			unit = gap.getGapTimeUnit();
			
			if (unit.equals(TimeIntervalUnit.DAY)) {
				actDuedateWrtBirthdate.add(Calendar.DATE, gap.getValue());
	
			} else if (unit.equals(TimeIntervalUnit.WEEK)) {
				actDuedateWrtBirthdate.add(Calendar.DATE, gap.getValue() * 7);
	
			} else if (unit.equals(TimeIntervalUnit.MONTH)) {
				actDuedateWrtBirthdate.add(Calendar.MONTH, gap.getValue());
	
			} else if (unit.equals(TimeIntervalUnit.YEAR)) {
				actDuedateWrtBirthdate.add(Calendar.YEAR, gap.getValue());
			}
		}
		
		// Calculate date for next vaccine wrt previous vaccination date first.
		Calendar actDuedateWrtPrevVDate = null;
		gap = getPreviousVaccineGap(vaccine);
		if(gap != null && prereqVaccineDate != null){
			actDuedateWrtPrevVDate = Calendar.getInstance();
			actDuedateWrtPrevVDate.setTime(prereqVaccineDate);
			
			unit = gap.getGapTimeUnit();
			
			if(unit.equals(TimeIntervalUnit.DAY)){
				actDuedateWrtPrevVDate.add(Calendar.DATE, gap.getValue());

			}else if(unit.equals(TimeIntervalUnit.WEEK)){
				actDuedateWrtPrevVDate.add(Calendar.DATE, gap.getValue()*7);

			}else if(unit.equals(TimeIntervalUnit.MONTH)){
				actDuedateWrtPrevVDate.add(Calendar.MONTH, gap.getValue());

			}else if(unit.equals(TimeIntervalUnit.YEAR)){
				actDuedateWrtPrevVDate.add(Calendar.YEAR, gap.getValue());
			}
		}
		
		// Calculate date for next vaccine wrt previous vaccination date first.
		Calendar actDuedateWrtPreviousEvent = null;
		gap = getStandardGap(vaccine);
		if (gap != null && previousVaccineDate != null) {
			actDuedateWrtPreviousEvent = Calendar.getInstance();
			actDuedateWrtPreviousEvent.setTime(previousVaccineDate);

			unit = gap.getGapTimeUnit();

			if (unit.equals(TimeIntervalUnit.DAY)) {
				actDuedateWrtPreviousEvent.add(Calendar.DATE, gap.getValue());

			} else if (unit.equals(TimeIntervalUnit.WEEK)) {
				actDuedateWrtPreviousEvent.add(Calendar.DATE, gap.getValue() * 7);

			} else if (unit.equals(TimeIntervalUnit.MONTH)) {
				actDuedateWrtPreviousEvent.add(Calendar.MONTH, gap.getValue());

			} else if (unit.equals(TimeIntervalUnit.YEAR)) {
				actDuedateWrtPreviousEvent.add(Calendar.YEAR, gap.getValue());
			}
		}
		
		calculatedDuedate.setTime(actDuedateWrtBirthdate.getTime());
		
		if(actDuedateWrtPrevVDate != null && calculatedDuedate.getTime().before(actDuedateWrtPrevVDate.getTime())){
			calculatedDuedate.setTime(actDuedateWrtPrevVDate.getTime());
		}
		
		if(actDuedateWrtPreviousEvent != null && calculatedDuedate.getTime().before(actDuedateWrtPreviousEvent.getTime())){
			calculatedDuedate.setTime(actDuedateWrtPreviousEvent.getTime());
		}
		
		// Move date to closest allowed vaccine day for given center
		boolean dayAllowed = false;
		for (VaccinationCenterVaccineDay alvd : vcdl) {
			if(calculatedDuedate.get(Calendar.DAY_OF_WEEK) == alvd.getId().getDayNumber()){
				dayAllowed = true;
				break;
			}
		}
		
		if(!dayAllowed){
			int firstAllowedDay = vcdl.size()>0?vcdl.get(0).getId().getDayNumber():2;//else monday
			int currday = calculatedDuedate.get(Calendar.DAY_OF_WEEK);

			int dayToAdd = firstAllowedDay >= currday ? (firstAllowedDay-currday) : (7-currday+firstAllowedDay);
			calculatedDuedate .add(Calendar.DATE, Math.abs(dayToAdd));
		}

		return calculatedDuedate.getTime();
	}
	
	public static Date calculateExpiryDate(Vaccine vaccine, Date birthdate, ServiceContext sc)
	{
		Calendar calculatedDate = Calendar.getInstance();
		calculatedDate.setTime(birthdate);
		
		VaccineGap gap = getVaccineExpiryGap(vaccine);
		TimeIntervalUnit unit = null;
		
		if(gap != null){
			unit = gap.getGapTimeUnit();
			
			if (unit.equals(TimeIntervalUnit.DAY)) {
				calculatedDate.add(Calendar.DATE, gap.getValue());
	
			} else if (unit.equals(TimeIntervalUnit.WEEK)) {
				calculatedDate.add(Calendar.DATE, gap.getValue() * 7);
	
			} else if (unit.equals(TimeIntervalUnit.MONTH)) {
				calculatedDate.add(Calendar.MONTH, gap.getValue());
	
			} else if (unit.equals(TimeIntervalUnit.YEAR)) {
				calculatedDate.add(Calendar.YEAR, gap.getValue());
			}
		}
		else {
			return null;
		}
		
		return calculatedDate.getTime();
	}
/*	
	public static Date calculateNextVaccinationDateExceptM2(Date previousMileStoneDate, Date vaccinationDate, int vaccinationCenterId,String vaccineName)
	{
		Calendar actNextAssignedDate=Calendar.getInstance();
		actNextAssignedDate.setTime(previousMileStoneDate);
		
		int gaptovvac = 0;
		TimeIntervalUnit unit = null;
		
		ServiceContext sc = Context.getServices();

		Vaccine vaccine = sc.getVaccinationService().getByName(vaccineName);
		
		List<VaccinationCenterVaccineDay> vcdl = sc.getVaccinationService().findVaccinationCenterVaccineDayByCriteria(vaccinationCenterId, vaccine.getVaccineId(), null, true);
		
		gaptovvac=vaccine.getGapFromPreviousMilestone();
		unit=vaccine.getPrevGapUnit();
		
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
	}*/
	/** 
 	 * NOTE: DONOT save reminders jusr creates and returns. MAKE SURE to save explicitly
	 * set timings null if you want default timings to be used. or if custom timing is needed use following syntax
	 * 
	 * ArmDayTiming a = new ArmDayTiming();
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
	public static List<ReminderSms> createNextVaccineReminderSms(Vaccination vaccination, ArmDayTiming timings, ServiceContext sc){
		List<Reminder> remis = sc.getReminderService().findRemindersByName(null, ReminderType.NEXT_VACCINATION_REMINDER, 0, 10, true, null);
		List<ReminderSms> reminderSmses = new ArrayList<ReminderSms>();
		for (Reminder r : remis) {
			ReminderSms rsms = new ReminderSms();
			rsms.setCreatedByUserId(vaccination.getCreatedByUserId());
			rsms.setCreatedDate(vaccination.getCreatedDate());
			rsms.setDayNumber(r.getGapEventDay());
			rsms.setReminderId(r.getReminderId());
			Time dueTime = r.getDefaultReminderTime();
			if(timings != null && r.getIsDefaultTimeEditable() && timings.getTiming(r.getGapEventDay()) != null){
				dueTime= timings.getTiming(r.getGapEventDay());
			}

			Calendar cal=Calendar.getInstance();
			cal.setTime(vaccination.getVaccinationDuedate());
			cal.set(Calendar.HOUR_OF_DAY, dueTime.getHours());
			cal.set(Calendar.MINUTE, dueTime.getMinutes());
			cal.set(Calendar.SECOND, dueTime.getSeconds());
			cal.add(Calendar.DATE, r.getGapEventDay());
			
			rsms.setDueDate(cal.getTime());
			rsms.setReminderStatus(REMINDER_STATUS.SCHEDULED);
			rsms.setVaccinationRecordNum(vaccination.getVaccinationRecordNum());
			
			reminderSmses.add(rsms);
		}
		return reminderSmses;
	}

	/**
	 * NOTE: DONOT save reminders jusr creates and returns. MAKE SURE to save explicitly
	 * @param vaccinationRecordNum
	 * @param transactionDate
	 * @param user
	 * @param timings
	 * @param sc
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<ReminderSms> createLotteryWonReminderSms(String reminderNme, int vaccinationRecordNum, Date lotteryDate, User user, ArmDayTiming timings, ServiceContext sc){
		List<Reminder> remis = sc.getReminderService().findRemindersByName(reminderNme, ReminderType.LOTTERY_WON_REMINDER, 0, 10, true, null);
		List<ReminderSms> reminderSmses = new ArrayList<ReminderSms>();
		for (Reminder r : remis) {
			ReminderSms rsms = new ReminderSms();
			rsms.setCreator(user);
			rsms.setDayNumber(r.getGapEventDay());
			rsms.setReminderId(r.getReminderId());
			Time dueTime = r.getDefaultReminderTime();
			if(timings != null && r.getIsDefaultTimeEditable() && timings.getTiming(r.getGapEventDay()) != null){
				dueTime= timings.getTiming(r.getGapEventDay());
			}

			Calendar cal=Calendar.getInstance();
			cal.setTime(lotteryDate);
			cal.set(Calendar.HOUR_OF_DAY, dueTime.getHours());
			cal.set(Calendar.MINUTE, dueTime.getMinutes());
			cal.set(Calendar.SECOND, dueTime.getSeconds());
			cal.add(Calendar.DATE, r.getGapEventDay());
			
			rsms.setDueDate(cal.getTime());
			rsms.setReminderStatus(REMINDER_STATUS.SCHEDULED);
			rsms.setVaccinationRecordNum(vaccinationRecordNum);
			
			reminderSmses.add(rsms);
		}

		return reminderSmses;
	}
	
	/**
	 * NOTE: DONOT save reminders jusr creates and returns. MAKE SURE to save explicitly
	 * @param vaccinationRecordNum
	 * @param transactionDate
	 * @param user
	 * @param timings
	 * @param sc
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<ReminderSms> createLotteryConsumedReminderSms(int vaccinationRecordNum, Date transactionDate, User user, ArmDayTiming timings, ServiceContext sc){
		List<Reminder> remis = sc.getReminderService().findRemindersByName(null, ReminderType.LOTTERY_CONSUMED_REMINDER, 0, 10, true, null);
		List<ReminderSms> reminderSmses = new ArrayList<ReminderSms>();
		for (Reminder r : remis) {
			ReminderSms rsms = new ReminderSms();
			rsms.setCreator(user);
			rsms.setDayNumber(r.getGapEventDay());
			rsms.setReminderId(r.getReminderId());
			Time dueTime = r.getDefaultReminderTime();
			if(timings != null && r.getIsDefaultTimeEditable() && timings.getTiming(r.getGapEventDay()) != null){
				dueTime= timings.getTiming(r.getGapEventDay());
			}

			Calendar cal=Calendar.getInstance();
			cal.setTime(transactionDate);
			cal.set(Calendar.HOUR_OF_DAY, dueTime.getHours());
			cal.set(Calendar.MINUTE, dueTime.getMinutes());
			cal.set(Calendar.SECOND, dueTime.getSeconds());
			cal.add(Calendar.DATE, r.getGapEventDay());
			
			rsms.setDueDate(cal.getTime());
			rsms.setReminderStatus(REMINDER_STATUS.SCHEDULED);
			rsms.setVaccinationRecordNum(vaccinationRecordNum);
			
			reminderSmses.add(rsms);
		}

		return reminderSmses;
	}
	
}
