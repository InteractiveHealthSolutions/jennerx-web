/*
 * 
 */
package org.ird.unfepi.web.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Model.TimeIntervalUnit;
import org.ird.unfepi.model.Reminder;
import org.ird.unfepi.model.Reminder.ReminderType;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccineGapId;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.utils.ArmDayTiming;
import org.ird.unfepi.utils.Utils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.web.utils.VaccineSchedule.VaccineStatusType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The Class IMRUtils.
 */
public class IMRUtils {
	
	 class Gap {
		String unit;
		int value;
		
		public Gap(String unit, int value) {
			super();
			this.unit = unit;
			this.value = value;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}

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
	
	public static boolean passVaccinePrerequisiteCheck(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule, Integer calendarId) {
		// if no prerequisite defined pass
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return true;
		}
		
		// if any pre-req is mandatory and not satified block it
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) 
		{
			if(prereq.getVaccinePrerequisiteId().getVaccinationcalendarId() == calendarId){
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
		}
		
		return true;
	}
	
	public static boolean isOverAgedVaccination(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule) {
		// if no prerequisite defined 
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return false;
		}
		
		System.out.println("----------------------");
		System.out.println("current vaccine " + vaccineSch.getVaccine().getName() + " " +vaccineSch.getVaccine().getVaccineId());
		
		// if any pre-req is mandatory and not satified block it
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) 
		{
			if(prereq.getMandatory() != null && prereq.getMandatory())
			{
				
				for (VaccineSchedule vaccineSchedule : schedule) {
					if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()){
						
						System.out.print("preReq " + prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() + " " + vaccineSchedule.getVaccine().getName() + " "+vaccineSchedule.getVaccine().getVaccineId());
						if(vaccineSchedule.getVaccine().getPrerequisites() != null && vaccineSchedule.getVaccine().getPrerequisites().size() > 0){
							
							System.out.print(" size " +vaccineSchedule.getVaccine().getPrerequisites().size());
								
							for (VaccinePrerequisite prereqee : vaccineSchedule.getVaccine().getPrerequisites()) {
								System.out.println(" " + prereqee.getVaccinePrerequisiteId().getVaccinePrerequisiteId());
							}
								
							isOverAgedVaccination(vaccineSchedule, schedule);
						}
						System.out.println();
						Date preReqVaccDate = vaccineSchedule.getVaccination_date();
						
						System.out.println("preReqVaccDate " + preReqVaccDate );
					}
				}
				
			}
			
		}
		
		return true;
	}
	
	public static boolean isPrerequisiteOverAge(Vaccine vaccine, List<VaccineSchedule> schedule, Integer calendarId, Date birthdate) {
		if(vaccine.getPrerequisites() == null || vaccine.getPrerequisites().size() == 0){
			return false;
		}
		else{
			for (VaccinePrerequisite prereq : vaccine.getPrerequisites()) {
				
				boolean check = isPrerequisiteOverAge(prereq.getPrerequisite(), schedule,calendarId, birthdate);
				
				System.out.println(vaccine.getName()+"  "+prereq.getPrerequisite().getName() + " "+ check);
				if(!check){
					for (VaccineSchedule vaccineSchedule : schedule) {
						if(prereq.getPrerequisite().getVaccineId() == vaccineSchedule.getVaccine().getVaccineId()){
							
							long diff_ms = vaccineSchedule.getVaccination_date().getTime() - birthdate.getTime();
							int days = (int) ((diff_ms / (1000*60*60*24)));
							System.out.println(vaccineSchedule.getVaccination_date() + " " + vaccineSchedule.getVaccine().getName()+ "   days " + days);
							
							// child's over age 12 months -> 365 days 
							if(days >= 365){
								return true;
							}
						}
					}
				}
				if(check){
					return true;
				}
			}
			return false;
		}
	}
	
//	public static Vaccination passVaccinePrerequisiteCheckDb(VaccineSchedule vaccineSch){
//		
//		ServiceContext sc = Context.getServices();
//		boolean prerequisitefound = false;
//		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) {
//			String query = "select * from vaccination where vaccineId = " + prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId()
//					+" and childId = "+ vaccineSch.getChildId() + " and vaccinationStatus in (" + "\"" + VaccineStatusType.VACCINATED + "\",\"" + VaccineStatusType.RETRO + "\",\"" + VaccineStatusType.RETRO_DATE_MISSING + "\"" + ")";
//			
//			List<Vaccination> records = sc.getCustomQueryService().getDataBySQL(query);
//			System.out.println("dblist size " + records.size() + " prereq " + prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId());
//			
//			if(records.size() > 0 ){
//				prerequisitefound = true ;
//				return records.get(0);
//			}
//		}
//		
//		return null;
//	}
	
public static boolean validateBirthGap(Date birthDate, Date vaccinatedDate,short vaccineId) {
	//VaccineGap gapFromBirth=new VaccineGap();
	ServiceContext sc = Context.getServices();	
try{
		List<Map> list = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vg.gapTimeUnit, vg.value ,vg.vaccineId,vgt.vaccineGapTypeId FROM vaccinegap vg inner join vaccinegaptype vgt on vg.vaccineGapTypeId=vgt.vaccinegaptypeid"+
				" where vg.vaccineId="+vaccineId+" and vgt.name='Birthdate Gap';");
		if( list.size()==0){
			return true;
		}
		String unit=(String)list.get(0).get("gapTimeUnit");
		int value=(Short)list.get(0).get("value");
		//Gap gapFromBirth=new Gap(unit,value );
		VaccineGapId id =new VaccineGapId();
				id.setVaccineId((Short) list.get(0).get("vaccineId"));
		id.setVaccineGapTypeId((Short) list.get(0).get("vaccineGapTypeId"));//vaccineGapTypeId
		//gapFromBirth.setId(id);
		
		if(value == 0) {
			return true;
		}
		
		if(vaccinatedDate.before(birthDate)) {
			return false;
		}
		
		long duration  = vaccinatedDate.getTime() - birthDate.getTime();
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		
		//String unit = gapFromBirth.getUnit();
		long gapInHours = 0;
		if(unit.equals("DAY")) {
			gapInHours =value*24;
		} else if(unit.equals("WEEK")) {
			gapInHours = value*7*24;
		} else if(unit.equals("MONTH")) {
			gapInHours = value*30*24;
		} else if(unit.equals("YEAR")) {
			gapInHours = value*12*30*24;
		}
		
		if(diffInHours >= gapInHours) {
			return true;
		} else if((gapInHours-diffInHours)<=WebGlobals.GRACE_HOURS) {
            return true;
        }
	
}finally{
	sc.closeSession();
}
		return false;
	}
	
	
	public static boolean validatePreRequisiteGap(short vaccineId, Date birthDate, Date preRequisiteVaccinationDate, Date vaccineDate) {
		ServiceContext sc = Context.getServices();	
		try{
			List<Map> listPrequisite = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vg.gapTimeUnit, vg.value ,vg.vaccineId,vgt.vaccineGapTypeId  FROM vaccinegap vg inner join vaccinegaptype vgt on vg.vaccineGapTypeId=vgt.vaccinegaptypeid"+
					" where vg.vaccineId="+vaccineId+" and vgt.name='Previous Vaccine Gap';");
			List<Map> listOverAge = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vg.gapTimeUnit, vg.value ,vg.vaccineId,vgt.vaccineGapTypeId  FROM vaccinegap vg inner join vaccinegaptype vgt on vg.vaccineGapTypeId=vgt.vaccinegaptypeid"+
					" where vg.vaccineId="+vaccineId+" and vgt.name='Over Age Gap';");
			
			//Gap gapFromPreReq=new Gap((String)listPrequisite.get(0).get("gapTimeUnit"), (Integer)listPrequisite.get(0).get("value"));
		
			if (listPrequisite.size() == 0) {
				return true;
			}
			
			String preReqUnit = (String) listPrequisite.get(0).get("gapTimeUnit");
			int preReqValue = (Short) listPrequisite.get(0).get("value");

			// Gap gapOverAgeGap=new Gap((String)listOverAge.get(0).get("gapTimeUnit"), (Integer)listOverAge.get(0).get("value"));
			String overAgeUnit = null;
			int overAgeValue = 0;
			
			if (listOverAge != null && listOverAge.size() > 0) {
				if (listOverAge.get(0) != null) {
					overAgeUnit = (String) listOverAge.get(0).get("gapTimeUnit");
					overAgeValue = (Short) listOverAge.get(0).get("value");
				}
			}

			// sc.closeSession();

			if (vaccineDate.before(preRequisiteVaccinationDate)) {
				return false;
			}
		
			long duration = vaccineDate.getTime() - preRequisiteVaccinationDate.getTime();
			long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
			String unit = preReqUnit;
			int gapValue = preReqValue;

			if (listOverAge.size() > 0 && listOverAge.get(0) != null) {
				long preRequisiteAge = preRequisiteVaccinationDate.getTime() - birthDate.getTime();
				long preRequisiteAgeHours = TimeUnit.MILLISECONDS.toHours(preRequisiteAge);

				if (preRequisiteAgeHours >= 8760) {
					unit = overAgeUnit;
					gapValue = overAgeValue;
				}
			}
		
			long gapInHours = 0;
			
			if (unit.equals("DAY")) {
				gapInHours = gapValue * 24;
			} else if (unit.equals("WEEK")) {
				gapInHours = gapValue * 7 * 24;
			} else if (unit.equals("MONTH")) {
				gapInHours = gapValue * 30 * 24;
			} else if (unit.equals("YEAR")) {
				gapInHours = gapValue * 12 * 30 * 24;
			}

			if (diffInHours >= gapInHours) {
				return true;
			} else if ((gapInHours - diffInHours) <= WebGlobals.GRACE_HOURS) {
				return true;
			}
		} finally {
			sc.closeSession();
		}
		return false;
	}

	public static boolean validateMaxBirthGap(short vaccineId,Date birthDate, Date vaccineDate) {
		ServiceContext sc = Context.getServices();	
		try{List<Map> listPrequisite = sc.getCustomQueryService().getDataBySQLMapResult("SELECT vg.gapTimeUnit, vg.value ,vg.vaccineId,vgt.vaccineGapTypeId  FROM vaccinegap vg inner join vaccinegaptype vgt on vg.vaccineGapTypeId=vgt.vaccinegaptypeid"+
				" where vg.vaccineId="+vaccineId+" and vgt.name='Vaccine Expiry Gap';");
		//Gap gapMax=new Gap((String)listPrequisite.get(0).get("gapTimeUnit"), (Integer)listPrequisite.get(0).get("value"));
		if( listPrequisite.size()==0){
			return true;
		}
		
		String gapUnit=(String)listPrequisite.get(0).get("gapTimeUnit");
		int gapValue=(Short)listPrequisite.get(0).get("value");
		sc.closeSession();
		
		if(vaccineDate.before(birthDate)) {
			return false;
		}
		
		long duration  = vaccineDate.getTime() - birthDate.getTime();
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		
		String unit = gapUnit;
		long gapInHours = 0;
		
		if(unit.equals("DAY")) {
			gapInHours = gapValue*24;
		} else if(unit.equals("WEEK")) {
			gapInHours = gapValue*7*24;
		} else if(unit.equals("MONTH")) {
			gapInHours = gapValue*30*24;
		} else if(unit.equals("YEAR")) {
			gapInHours = gapValue*12*30*24;
		}
		
		if(diffInHours <= gapInHours) {
			return true;
		} else if((diffInHours-gapInHours)<=WebGlobals.GRACE_HOURS) {
            return true;
        }
	}finally{
		sc.closeSession();
	}
		return false;
	}
	
	
	public static Date getPrerequisiteVaccineDate(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule, Integer calendarId) {
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return null;
		}
		//for now it assumes that only one prereq is possible
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) {
			for (VaccineSchedule vaccineSchedule : schedule) {
				if(prereq.getVaccinePrerequisiteId().getVaccinationcalendarId() == calendarId){
					if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()){
						return vaccineSchedule.getVaccination_date();
					}
				}
			}
		}
		
		return null;
	}
	
	public static boolean isPrerequisiteVaccinatedOnCurrentVisit(VaccineSchedule vaccineSch, List<VaccineSchedule> schedule, Integer calendarId) {
		if(vaccineSch.getPrerequisites() == null || vaccineSch.getPrerequisites().size() == 0){
			return false;
		}
		for (VaccinePrerequisite prereq : vaccineSch.getPrerequisites()) {
			for (VaccineSchedule vaccineSchedule : schedule) {
				if(prereq.getVaccinePrerequisiteId().getVaccinationcalendarId() == calendarId){
					if(prereq.getVaccinePrerequisiteId().getVaccinePrerequisiteId() == vaccineSchedule.getVaccine().getVaccineId()
							&& vaccineSchedule.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())
							&& vaccineSchedule.getVaccination_date() != null && DateUtils.datesEqual(vaccineSchedule.getVaccination_date(), vaccineSch.getVisitdate())){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	public static String getNextVaccineName(String currentVaccine){
		return Context.getSetting("vaccine.next-vaccine."+currentVaccine.trim().toLowerCase(), "Not Found");
	}
	
	public static VaccineGap getBirthdateGap(Vaccine vaccine, Integer calendarId){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getId().getVaccinationcalendarId() == calendarId){
				if(gap.getVaccineGapType().getName().toLowerCase().contains("birthdate")){
					return gap;
				}
			}
		}
		return null;
	}
	
	public static VaccineGap getVaccineExpiryGap(Vaccine vaccine, Integer calendarId){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getId().getVaccinationcalendarId() == calendarId){
				if(gap.getVaccineGapType().getName().toLowerCase().contains("expir")){
					return gap;
				}
			}
		}
		return null;
	}
	
	public static VaccineGap getOverAgeGap(Vaccine vaccine, Integer calendarId){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getId().getVaccinationcalendarId() == calendarId){
				if(gap.getVaccineGapType().getName().toLowerCase().contains("over")){
					return gap;
				}
			}
		}
		return null;
	}
	
	public static VaccineGap getBirthdateGap(short vaccineId, Integer calendarId){
		ServiceContext sc = Context.getServices();
		try{
			for (VaccineGap gap : Utils.initializeAndUnproxy(sc.getVaccinationService().findVaccineById(vaccineId).getVaccineGaps())) {
				if(gap.getId().getVaccinationcalendarId() == calendarId){
					if(gap.getVaccineGapType().getName().toLowerCase().contains("birthdate")){
						return gap;
					}
				}
			}
		}
		finally {
			sc.closeSession();
		}
		return null;
	}
	
	public static VaccineGap getStandardGap(Vaccine vaccine, Integer calendarId){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getId().getVaccinationcalendarId() == calendarId){
				if(gap.getVaccineGapType().getName().toLowerCase().contains("standard")){
					return gap;
				}
			}
		}
		return null;
	}
	
	public static VaccineGap getPreviousVaccineGap(Vaccine vaccine, Integer calendarId){
		for (VaccineGap gap : vaccine.getVaccineGaps()) {
			if(gap.getId().getVaccinationcalendarId() == calendarId){
				if(gap.getVaccineGapType().getName().toLowerCase().contains("previous vaccine")){
					return gap;
				}
			}
		}
		return null;
	}
	public static Date calculateVaccineDuedate(Vaccine vaccine, Date birthdate, Date prereqVaccineDate, Date previousVaccineDate, Integer vaccinationCenterId, Integer calendarId, ServiceContext sc)
	{
		Calendar calculatedDuedate = Calendar.getInstance();
		// Calculate date for next vaccine wrt birthdate first.
		Calendar actDuedateWrtBirthdate = Calendar.getInstance();
		actDuedateWrtBirthdate.setTime(birthdate);
		
		VaccineGap gap = getBirthdateGap(vaccine, calendarId);
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
		gap = getPreviousVaccineGap(vaccine, calendarId);
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
		gap = getStandardGap(vaccine, calendarId);
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
		
		return calculatedDuedate.getTime();
	}
	
	public static Date calculateVaccineDuedate(Vaccine vaccine, Date birthdate, Date prereqVaccineDate, Date previousVaccineDate, Integer vaccinationCenterId, Integer calendarId,boolean preqOverAge, ServiceContext sc)
	{
		Calendar calculatedDuedate = Calendar.getInstance();
		// Calculate date for next vaccine wrt birthdate first.
		Calendar actDuedateWrtBirthdate = Calendar.getInstance();
		actDuedateWrtBirthdate.setTime(birthdate);
		
		VaccineGap gap = getBirthdateGap(vaccine, calendarId);
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
		gap = getPreviousVaccineGap(vaccine, calendarId);
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
		if(preqOverAge){
			gap = getOverAgeGap(vaccine, calendarId);
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
			
		}
		
		// Calculate date for next vaccine wrt previous vaccination date first.
		Calendar actDuedateWrtPreviousEvent = null;
		gap = getStandardGap(vaccine, calendarId);
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
		
		return calculatedDuedate.getTime();
	}
	
	public static Date calculateExpiryDate(Vaccine vaccine, Date birthdate, Integer calendarId, ServiceContext sc)
	{
		Calendar calculatedDate = Calendar.getInstance();
		calculatedDate.setTime(birthdate);
		
		VaccineGap gap = getVaccineExpiryGap(vaccine, calendarId);
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

	public static Date calculateOverAgedDate(Vaccine vaccine, Date birthdate, ServiceContext sc){
		
		return null; 
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

	
	/*
	 *  creates json for MSF vaccine Schedule calender
	 * 
	 */
	public static JSONArray buildChildVaccineJSON(List<Vaccine> vaccines,
			 List<Vaccination> vaccinations) {
		
		JSONArray json = new JSONArray();
		for (Vaccine v : vaccines) {
			
			JSONObject obj = new JSONObject();
			obj.put("vaccineId", v.getVaccineId());
			obj.put("name", v.getName());
			JSONObject prerequesite = new JSONObject();
			for (VaccinePrerequisite vp : v.getPrerequisites()) {
				obj.put("preReqId", vp.getPrerequisite().getVaccineId());
				obj.put("hasPreReq", true);
				break;

			}
			for (VaccineGap vg : v.getVaccineGaps()) {
				JSONObject gap = new JSONObject();
				gap.put("unit", vg.getGapTimeUnit().name());
				gap.put("value", vg.getValue());

				obj.put(vg.getVaccineGapType().getName(), gap);
			}
			if (vaccinations != null) {
				for (Vaccination va : vaccinations) {
					if (va.getVaccineId() == v.getVaccineId()) {
						obj.put("status", "vaccinated");
						SimpleDateFormat sdf = new SimpleDateFormat();
						sdf.applyPattern("dd/MM/yyyy");
						obj.put("dateGiven",
								sdf.format(va.getVaccinationDate()));
					}
				}
			}
			json.add(obj);
		}

		return json;
	}

}
