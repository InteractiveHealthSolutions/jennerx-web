package org.ird.unfepi.web.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;

import com.mysql.jdbc.StringUtils;

public class VaccineSchedule {
	public enum VaccineScheduleKey{
		vaccine,
		birthdate_gap, 
		prerequisites,
		schedule_duedate, 
		assigned_duedate,
		is_allowed,
		is_next,
		vaccination_date,
		center,
		status,
		is_current_suspect,
		is_retro_suspect,
		is_already_vaccinated,
	}
	
	public enum VaccineStatusType {
		RETRO,
		RETRO_DATE_MISSING,
		VACCINATED,
		SCHEDULED,
		VACCINATED_EARLIER,
		NOT_ALLOWED
	}
	private Vaccine vaccine;
	private Date birthdate;
	private Set<VaccinePrerequisite> prerequisites;
	private VaccineGap birthdate_gap;
	private Date schedule_duedate;
	private Date auto_calculated_date;
	private Date assigned_duedate;
	private Date vaccination_date;
	private Date expiry_date;
	private Integer center;
	private String status;
	private Boolean is_current_suspect;
	private Boolean is_retro_suspect;
	private Vaccination vaccinationObjCurrentVisit;
	private Integer childId;
	
	public VaccineSchedule() {
	}
	
	public VaccineSchedule(Integer childId, Vaccine vaccine, Date assigned_duedate, Date vaccination_date, Integer center, VaccineStatusType status) {
		this.vaccine = vaccine;
		this.assigned_duedate = assigned_duedate;
		this.vaccination_date = vaccination_date;
		this.center = center;
		this.status = status.name();
	}
	
	public static ArrayList<VaccineSchedule> generateDefaultSchedule(Date birthdate, Date centerVisitDate, Integer childId, Integer vaccinationCenterId, boolean ignoreCenter){

		ServiceContext sc = Context.getServices();
		ArrayList<VaccineSchedule> schedule = new ArrayList<VaccineSchedule>();
		try {
			List<Vaccine> vaccinel = sc.getVaccinationService().getAll(true, new String[]{"prerequisites"}, GlobalParams.SQL_VACCINE_BIRTHDATE_GAP_ORDER);

			for (Vaccine vaccine : vaccinel) {
				VaccineSchedule vsch = new VaccineSchedule();
				vsch.setVaccine(vaccine);
				vsch.setPrerequisites(vaccine.getPrerequisites());
				VaccineGap bdg = IMRUtils.getBirthdateGap(vaccine);
				vsch.setBirthdate_gap(bdg);
				vsch.setBirthdate(birthdate);
				vsch.setExpiry_date(IMRUtils.calculateExpiryDate(vaccine, birthdate, sc));
				vsch.setChildId(childId);
				
				Date schduedate = null;
				
				if(birthdate != null && bdg != null){
					// ignore center
					schduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, null, null, null, sc);
					vsch.setSchedule_duedate(schduedate);
				}
				
				List<Vaccination> pvacc = null;
				List<Vaccination> vvacc = null;

				if(childId != null){
					pvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.PENDING, 0, 12, true, null);
					vvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.VACCINATED, 0, 12, true, null);
				}
				
				if(pvacc!=null && pvacc.size() > 1){
					throw new IllegalStateException(pvacc.size()+" vaccinations found Pending for "+vaccine.getName()+" for child "+childId);
				}
				
				// if vaccine already given donot move forward just set few variables, status and goto next vaccine 
				if(vvacc != null && vvacc.size() > 0){
					vsch.setStatus(VaccineStatusType.VACCINATED_EARLIER.name());
					vsch.setAssigned_duedate(vvacc.get(0).getVaccinationDuedate());
					vsch.setVaccination_date(vvacc.get(0).getVaccinationDate());
					vsch.setCenter(vvacc.get(0).getVaccinationCenterId());
				}
				else {// if not given
					boolean passedPrereqCheck = IMRUtils.passVaccinePrerequisiteCheck(vsch, schedule);

					Date asgnduedate = null;
					if(pvacc!=null && pvacc.size() > 0){
						asgnduedate = pvacc.get(0).getVaccinationDuedate();
					}
					else if(passedPrereqCheck){
						Date prevVaccDate = IMRUtils.getPrerequisiteVaccineDate(vsch, schedule);
						asgnduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, prevVaccDate, null, ignoreCenter?null:vaccinationCenterId, sc);
					}
					
					int minGracePeriod = vsch.getVaccine().getMinGracePeriodDays();
					int maxGracePeriod = vsch.getVaccine().getMaxGracePeriodDays();
					
					Date minGraceDate = new Date(centerVisitDate.getTime()+(-minGracePeriod* 24 * 60 * 60 * 1000));
					Date maxGraceDate = new Date(centerVisitDate.getTime()+(maxGracePeriod* 24 * 60 * 60 * 1000));
					boolean current_suspect = (asgnduedate != null 
							&& asgnduedate.compareTo(minGraceDate) >= 0
							&& asgnduedate.compareTo(maxGraceDate) <= 0)
						|| (schduedate != null 
								&& schduedate.compareTo(minGraceDate) >= 0
								&& schduedate.compareTo(maxGraceDate) <= 0);
					
					boolean retro_suspect = (asgnduedate != null 
							&& asgnduedate.compareTo(minGraceDate) == -1)
						||(schduedate != null 
								&& schduedate.compareTo(minGraceDate) == -1);
					
					// if vaccine`s date w.r.t birthdate has passed allow it provided that pre-requisite is not blocking it..
					/*if(schduedate != null && 
							(!passedPrereqCheck || !(current_suspect || retro_suspect))){
						isAllowed = false;
					}
					
					if(!isAllowed && passedPrereqCheck){
						isNext = true;
					}*/
					
					// N/A or out of schedule or expiry date has passed
					if(schduedate == null || 
							(vsch.getExpiry_date() != null && vsch.getExpiry_date().before(new Date()))){
						vsch.setStatus("");
					}
					else if(!passedPrereqCheck){
						vsch.setStatus(VaccineStatusType.NOT_ALLOWED.name());
					}
					else if(current_suspect){
						vsch.setStatus(VaccineStatusType.VACCINATED.name());
					}
					else if(retro_suspect){
						vsch.setStatus(VaccineStatusType.RETRO.name());
					}
					else if(passedPrereqCheck){
						vsch.setStatus(VaccineStatusType.SCHEDULED.name());
					}
					
					vsch.setIs_current_suspect(current_suspect);
					vsch.setIs_retro_suspect(retro_suspect);
					vsch.setAssigned_duedate(asgnduedate);
				}
				schedule.add(vsch);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			sc.closeSession();
		}
		return schedule;
	}

	public static ArrayList<VaccineSchedule> updateSchedule(ArrayList<VaccineSchedule> schedule){

		ArrayList<VaccineSchedule> updatedSchedule = new ArrayList<VaccineSchedule>();
		ServiceContext sc = Context.getServices();
		try {
			//List<Vaccine> vaccinel = sc.getVaccinationService().getAll(true, GlobalParams.SQL_VACCINE_BIRTHDATE_GAP_ORDER);
			
			for (VaccineSchedule schmap : schedule) {
				// if NOT  = vaccinated or ignorable
				if(!StringUtils.isEmptyOrWhitespaceOnly(schmap.getStatus()) && !schmap.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())){
					boolean passedPrereqCheck = IMRUtils.passVaccinePrerequisiteCheck(schmap, schedule);
	
					if(!passedPrereqCheck){
						schmap.setStatus(VaccineStatusType.NOT_ALLOWED.name());
						schmap.setAuto_calculated_date(null);
						schmap.setAssigned_duedate(null);
						schmap.setCenter(null);
						schmap.setVaccination_date(null);
					}
					// if not allowed change status otherwise preserve user selection
					else if(schmap.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())){
						if(!schmap.getIs_current_suspect() && !schmap.getIs_retro_suspect() && passedPrereqCheck){
							Vaccine vac = sc.getVaccinationService().findVaccineById(schmap.getVaccine().getVaccineId());
							schmap.setAssigned_duedate(IMRUtils.calculateVaccineDuedate(vac, schmap.getBirthdate(), IMRUtils.getPrerequisiteVaccineDate(schmap, schedule), null, schmap.getCenter(), sc));
							schmap.setAuto_calculated_date(schmap.getAssigned_duedate());
							schmap.setStatus(VaccineStatusType.SCHEDULED.name());
						}
						else if(schmap.getIs_current_suspect()){
							schmap.setStatus(VaccineStatusType.VACCINATED.name());
						}
						else if(schmap.getIs_retro_suspect()){
							schmap.setStatus(VaccineStatusType.RETRO.name());
						}
					}
				}
				
				updatedSchedule.add(schmap);
			}
		}
		/*catch(Exception e){
			e.printStackTrace();
		}*/
		finally {
			sc.closeSession();
		}
		return updatedSchedule;
	}
	
	/**
	 * ONLY TO VALIDATE DATE GAPS
	 * @param schedule
	 * @return 
	 */
	public static Map<String, String> validateVaccineScheduleDates(ArrayList<VaccineSchedule> schedule) {
		Map<String, String> map = new HashMap<String, String>();
		ServiceContext sc = Context.getServices();
		String errormsg = "";
		try{
			for (VaccineSchedule sch : schedule) {
				Vaccine vac = sc.getVaccinationService().findVaccineById(sch.getVaccine().getVaccineId());
				//only validate vaccies that has not given yet
				if(!StringUtils.isEmptyOrWhitespaceOnly(sch.getStatus())
						&& !sch.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())){
					if(sch.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())
							|| sch.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO.name())){
						if(sch.getVaccination_date() != null){
							Date lastVaccineDate = null;
							if(sch.getChildId() != null){
								List l = sc.getCustomQueryService().getDataBySQL("SELECT vaccinationDate FROM vaccination WHERE childId = "+sch.getChildId()+" AND vaccinationDate IS NOT NULL ORDER BY vaccinationDate DESC");
								lastVaccineDate = (Date) (l.size() > 0 ? l.get(0) : null);
							}
							Date shouldBeDate = IMRUtils.calculateVaccineDuedate(vac, sch.getBirthdate(), IMRUtils.getPrerequisiteVaccineDate(sch, schedule), lastVaccineDate, sch.getCenter(), sc);
							
							// if vaccination date is far before should be date 
							if(DateUtils.differenceBetweenIntervals(sch.getVaccination_date(), shouldBeDate, TIME_INTERVAL.DATE) < -7){
								map.put("ERROR", "ERROR");
								errormsg += "\nVaccination date for "+sch.getVaccine().getName()+" doesnot follow schedule and is less than expected date (vaccinated:expected): ("+sch.getVaccination_date()+":"+shouldBeDate+")";
							}
						}
					}
					else if(sch.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())){
						if(sch.getAssigned_duedate() != null){
							Date lastVaccineDate = null;
							if(sch.getChildId() != null){
								List l = sc.getCustomQueryService().getDataBySQL("SELECT vaccinationDate FROM vaccination WHERE childId = "+sch.getChildId()+" AND vaccinationDate IS NOT NULL ORDER BY vaccinationDate DESC");
								lastVaccineDate = (Date) (l.size() > 0 ? l.get(0) : null);
							}
							Date shouldBeDate = IMRUtils.calculateVaccineDuedate(vac, sch.getBirthdate(), IMRUtils.getPrerequisiteVaccineDate(sch, schedule), lastVaccineDate, sch.getCenter(), sc);
							
							// if vaccination date is far before should be date 
							if(DateUtils.differenceBetweenIntervals(sch.getAssigned_duedate(), shouldBeDate, TIME_INTERVAL.DATE) < -7){
								map.put("ERROR", "ERROR");
								errormsg += "\nNext scheduled date for "+sch.getVaccine().getName()+" doesnot follow schedule and is less than expected date (vaccinated:expected): ("+sch.getAssigned_duedate()+":"+shouldBeDate+")";
							}
						}
					}
				}
			}
			
			if(map.get("ERROR") != null){
				map.put("MESSAGE", errormsg);
			}
			else {
				map.put("SUCCESS", "SUCCESS");
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			sc.closeSession();
		}
		
		return map;
	}
	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Set<VaccinePrerequisite> getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(Set<VaccinePrerequisite> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public VaccineGap getBirthdate_gap() {
		return birthdate_gap;
	}

	public void setBirthdate_gap(VaccineGap birthdate_gap) {
		this.birthdate_gap = birthdate_gap;
	}

	public Date getSchedule_duedate() {
		return schedule_duedate;
	}

	public void setSchedule_duedate(Date schedule_duedate) {
		this.schedule_duedate = schedule_duedate;
	}

	public Date getAuto_calculated_date() {
		return auto_calculated_date;
	}

	public void setAuto_calculated_date(Date auto_calculated_date) {
		this.auto_calculated_date = auto_calculated_date;
	}

	public Date getAssigned_duedate() {
		return assigned_duedate;
	}

	public void setAssigned_duedate(Date assigned_duedate) {
		this.assigned_duedate = assigned_duedate;
	}

	public Date getVaccination_date() {
		return vaccination_date;
	}

	public void setVaccination_date(Date vaccination_date) {
		this.vaccination_date = vaccination_date;
	}

	public Date getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(Date expiry_date) {
		this.expiry_date = expiry_date;
	}

	public Integer getCenter() {
		return center;
	}

	public void setCenter(Integer center) {
		this.center = center;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIs_current_suspect() {
		return is_current_suspect;
	}

	public void setIs_current_suspect(Boolean is_current_suspect) {
		this.is_current_suspect = is_current_suspect;
	}

	public Boolean getIs_retro_suspect() {
		return is_retro_suspect;
	}

	public void setIs_retro_suspect(Boolean is_retro_suspect) {
		this.is_retro_suspect = is_retro_suspect;
	}
	public Vaccination getVaccinationObjCurrentVisit() {
		return vaccinationObjCurrentVisit;
	}

	public void setVaccinationObjCurrentVisit(Vaccination vaccinationObjCurrentVisit) {
		this.vaccinationObjCurrentVisit = vaccinationObjCurrentVisit;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

}
