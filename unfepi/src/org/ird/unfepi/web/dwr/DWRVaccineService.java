package org.ird.unfepi.web.dwr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.web.utils.IMRUtils;
import org.ird.unfepi.web.utils.VaccinationCenterVisit;
import org.ird.unfepi.web.utils.VaccineSchedule;

import com.mysql.jdbc.StringUtils;

public class DWRVaccineService {
		
	public ArrayList<VaccineSchedule> generateDefaultSchedule(Date birthdate, Date centerVisitDate, Integer childId, int vaccinationCenterId, boolean ignoreCenter, String uuid) 
	{
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<VaccineSchedule> vsch = VaccineSchedule.generateDefaultSchedule(birthdate, centerVisitDate, childId, vaccinationCenterId, ignoreCenter);
		System.out.println(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid);
		req.getSession().setAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid, vsch);
		return vsch;
	}
	
	public ArrayList<VaccineSchedule> updateSchedule(ArrayList<VaccineSchedule> schedule, String uuid){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<VaccineSchedule> vsch = VaccineSchedule.updateSchedule(schedule);
		System.out.println(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid);
		req.getSession().setAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid, vsch);
		return vsch;
	}
	
	public ArrayList<VaccineSchedule> overrideSchedule(ArrayList<VaccineSchedule> schedule, String uuid){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid);
		req.getSession().setAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid, schedule);
		return schedule;
	}
	
	public Map<String, String> validateVaccineScheduleDates(ArrayList<VaccineSchedule> schedule){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Map<String, String> res = VaccineSchedule.validateVaccineScheduleDates(schedule);
		return res;
	}
	
	public ArrayList<VaccineSchedule> getSchedule(String uuid){
		HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<VaccineSchedule> vsch = (ArrayList<VaccineSchedule>) req.getSession().getAttribute(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid);
		System.out.println(VaccinationCenterVisit.VACCINE_SCHEDULE_KEY+uuid);
		return vsch;
	}
	
	public String getNextVaccine(String currentVaccineName) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){////if session has expired then it doesnot matter what ever result returned as user will be redirected to login page
			return "Session expired. Logout from application and login again";
		}

		try{
			return IMRUtils.getNextVaccineName(currentVaccineName);
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/*public String addVaccine(String vaccinename,String vaccineFullName,String vaccineShortName
			,String description,int gapFromPrev,String prevGapUnit,int gapToNext,String nextGapUnit) {
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			return "Session expired. Logout from application and login again";
		}
		ServiceContext sc = Context.getServices();

		Vaccine vacc=new Vaccine();
		vacc.setName(vaccinename);
		vacc.setFullName(vaccineFullName);
		vacc.setShortName(vaccineShortName);
		vacc.setDescription(description);
		vacc.setGapFromPreviousMilestone((byte) gapFromPrev);
		vacc.setPrevGapUnit(TimeIntervalUnit.valueOf(prevGapUnit));
		vacc.setGapFromNextMilestone((byte) gapToNext);
		vacc.setNextGapUnit(TimeIntervalUnit.valueOf(nextGapUnit));
		vacc.setCreator(user.getUser());
		try{
			sc.getVaccinationService().addVaccine(vacc);
			sc.commitTransaction();
		}catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		finally{
			sc.closeSession();
		}
		return "Vaccine added successfully";
	}*/
	
	public String validatePrivilegedEntryNextVaccine(String programId, String currentVaccine, String nextVaccine){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ServiceContext sc = Context.getServices();
		try{
			Vaccine currv = sc.getVaccinationService().getByName(currentVaccine);
			Vaccine nextv = sc.getVaccinationService().getByName(nextVaccine);

			if(currv == null){
				return "Current vaccine not exists.";
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(nextVaccine) && nextv == null){
				return "Next vaccine not exists.";
			}
			
			if(sc.getVaccinationService().findVaccinationRecordByCriteria(sc.getIdMapperService().findIdMapper(programId).getMappedId(), currentVaccine, null, null, null, null, null, null, null, null, null, false, 0, 1, true, null, null).size() > 0){
				return "Currently given vaccine already exists. Fill simple followup form if record was not updated.";
			}
			
			if(sc.getVaccinationService().findVaccinationRecordByCriteria(sc.getIdMapperService().findIdMapper(programId).getMappedId(), nextVaccine, null, null, null, null, null, null, null, null, null, false, 0, 1, true, null, null).size() > 0){
				return "Next scheduled vaccine already exists.";
			}
			
			return "OK";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Error: "+e.getMessage();
		}
		finally{
			sc.closeSession();
		}
	}
	
	public boolean hasRecievedVaccine(String programId, String vaccine){
		LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
		if(user==null){
			try {
				WebContextFactory.get().forwardToString("login.htm");
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ServiceContext sc = Context.getServices();
		try{
			if(sc.getVaccinationService().findVaccinationRecordByCriteria(sc.getIdMapperService().findIdMapper(programId).getMappedId(), vaccine, null, null, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 1, true, null, null).size() > 0){
				return true;
			}
			
			return false;
		}
		finally{
			sc.closeSession();
		}
	}
}
