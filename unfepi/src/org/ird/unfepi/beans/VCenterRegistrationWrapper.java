package org.ird.unfepi.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccine;

public class VCenterRegistrationWrapper {

	private VaccinationCenter vaccinationCenter;
	private List<Map<String,Object>> vaccineDayMapList;
	private List<CalendarDay> calendarDays;
	
	public VCenterRegistrationWrapper() {
		vaccinationCenter = new VaccinationCenter();
		ServiceContext sc = Context.getServices();
		
		try{
			calendarDays = new ArrayList<CalendarDay>();
			List<CalendarDay> clist = sc.getVaccinationService().getAllCalendarDay(true);
			for (CalendarDay calendarDay : clist) {
				calendarDays.add(calendarDay);
			}
			
			vaccineDayMapList = new ArrayList<Map<String,Object>>();
			List<Vaccine> vlist = sc.getVaccinationService().getAll(true, null, " name ");
			for (Vaccine vaccine : vlist) 
			{
				if(vaccine.getVaccineGaps() != null && vaccine.getVaccineGaps().size() > 0){
					Map<String,Object> vdmap = new HashMap<String, Object>();
					vdmap.put("vaccine", vaccine);
					vdmap.put("daylist", new String[7]);
					vaccineDayMapList.add(vdmap);
				}
			}
		}
		finally{
			sc.closeSession();
		}
	}
	
	public VCenterRegistrationWrapper(VaccinationCenter vaccinationCenter, List<VaccinationCenterVaccineDay> vcvd) {
		this.vaccinationCenter = vaccinationCenter;
		ServiceContext sc = Context.getServices();
		
		try{
			calendarDays = new ArrayList<CalendarDay>();
			List<CalendarDay> clist = sc.getVaccinationService().getAllCalendarDay(true);
			for (CalendarDay calendarDay : clist) {
				calendarDays.add(calendarDay);
			}
			
			vaccineDayMapList = new ArrayList<Map<String,Object>>();
			List<Vaccine> vlist = sc.getVaccinationService().getAll(true, null, " name ");
			for (Vaccine vaccine : vlist) 
			{
				if(vaccine.getVaccineGaps() != null && vaccine.getVaccineGaps().size() > 0){
					Map<String,Object> vdmap = new HashMap<String, Object>();
					vdmap.put("vaccine", vaccine);
					String[] dayarr = new String[7];
					
					for (int i = 0; i < calendarDays.size(); i++) {
						if(vcvd != null && isDaySelected(vaccine.getVaccineId(), calendarDays.get(i).getDayNumber(), vcvd)){
							dayarr[i] = calendarDays.get(i).getDayFullName();
						}
					}
					
					vdmap.put("daylist", dayarr);
					vaccineDayMapList.add(vdmap);
				}
			}
		}
		finally{
			sc.closeSession();
		}
	}
	
	private boolean isDaySelected(short vaccineId, short daynum, List<VaccinationCenterVaccineDay> vcvd){
		for (VaccinationCenterVaccineDay vaccinationCenterVaccineDay : vcvd) {
			if(vaccinationCenterVaccineDay.getId().getVaccineId() == vaccineId && vaccinationCenterVaccineDay.getId().getDayNumber() == daynum){
				return true;
			}
		}
		return false;
	}
	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}
	public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public List<CalendarDay> getCalendarDays() {
		return calendarDays;
	}

	public void setCalendarDays(List<CalendarDay> calendarDays) {
		this.calendarDays = calendarDays;
	}

	public List<Map<String, Object>> getVaccineDayMapList() {
		return vaccineDayMapList;
	}

	public void setVaccineDayMapList(List<Map<String, Object>> vaccineDayMapList) {
		this.vaccineDayMapList = vaccineDayMapList;
	}
}

