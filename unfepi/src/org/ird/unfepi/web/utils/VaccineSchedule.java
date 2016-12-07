package org.ird.unfepi.web.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ird.unfepi.constants.WebGlobals;
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
		vaccination_date,
		center,
		status,
		is_current_suspect,
		is_retro_suspect,
		prerequisite_passed,
		expired,
		prerequisite_given_on_current_visit
	}
	
	public enum VaccineStatusType {
		RETRO,
		RETRO_DATE_MISSING,
		VACCINATED,
		SCHEDULED,
		CURRENT_RETRO,
		CURRENT_RETRO_DATE_MISSING,
		VACCINATED_EARLIER,
		NOT_ALLOWED,
		NOT_VACCINATED,
		INVALID_DOSE,
		NOT_GIVEN,
	}
	private Vaccine vaccine;
	private Date birthdate;
	private Date visitdate;
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
	private Vaccination vaccinationHistory;
	private Integer childId;
	private Boolean prerequisite_passed;
	private Boolean expired;
	private Boolean prerequisite_given_on_current_visit;
	private List<Short> prerequisiteFor;
	
	public VaccineSchedule() {
	}
	
	public VaccineSchedule(Integer childId, Vaccine vaccine, Date assigned_duedate, Date vaccination_date, Integer center, VaccineStatusType status) {
		this.vaccine = vaccine;
		this.assigned_duedate = assigned_duedate;
		this.vaccination_date = vaccination_date;
		this.center = center;
		this.status = status.name();
	}
	
	//TODO 
	public static ArrayList<VaccineSchedule> generateDefaultSchedule(Date birthdate, Date centerVisitDate, Integer childId, Integer vaccinationCenterId, boolean ignoreCenter, List<VaccineSchedule> scheduleRetro , Integer healthProgramId)
	{
		
		ServiceContext sc = Context.getServices();
		ArrayList<VaccineSchedule> schedule = new ArrayList<VaccineSchedule>();
		ArrayList<VaccineSchedule> invalid_schedule = new ArrayList<VaccineSchedule>();
		
		Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId = " + healthProgramId).get(0);
//		Integer calendarId = 2;
		try {
			
			ArrayList<Short> vis = (ArrayList<Short>) sc.getCustomQueryService().getDataBySQL("select distinct vaccine.vaccineId from vaccine inner join vaccinegap on vaccine.vaccineId = vaccinegap.vaccineId "
					+ "where vaccine.vaccine_entity like 'CHILD_%' and vaccinegap.vaccinationcalendarId = " +calendarId);
			
//			String[] vids = Context.getSetting("child.vaccine-schedule.vaccines-list", null).split(",");
//			ArrayList<Short>  vis = new ArrayList<Short>();
//			for (String vstr : vids) {
//				vis.add(Short.parseShort(vstr.trim()));
//			}
//			List<Vaccine> vaccinel = sc.getVaccinationService().getVaccinesById(vis.toArray(new Short[]{}), true, new String[]{"prerequisites"}, GlobalParams.SQL_VACCINE_BIRTHDATE_GAP_ORDER);
			
			System.out.println(calendarId + " "+ healthProgramId + "   calendarIdcalendarId");
			
			String SQL_VACCINE_BIRTHDATE_GAP_ORDER_C = " IFNULL((SELECT CASE WHEN gaptimeunit = 'month' THEN (30*value) "
					+ " WHEN gaptimeunit = 'week' THEN (7*value) "+ " WHEN gaptimeunit = 'day' THEN (0*value) "+ " WHEN gaptimeunit = 'year' THEN (365*value) "
					+ " ELSE 9999 END FROM vaccinegap WHERE vaccinegaptypeid=1 AND vaccineid=this_.vaccineId AND vaccinationcalendarid="+ calendarId +" ), 9999)";
			
			/*GlobalParams.SQL_VACCINE_BIRTHDATE_GAP_ORDER*/
			List<Vaccine> vaccinel = sc.getVaccinationService().getVaccinesById(vis.toArray(new Short[]{}), true, new String[]{"prerequisites"}, SQL_VACCINE_BIRTHDATE_GAP_ORDER_C);
			
			
			for (Vaccine vaccine : vaccinel) {
				
								
				VaccineSchedule vsch = new VaccineSchedule();
				vsch.setVaccine(vaccine);
				vsch.setPrerequisites(vaccine.getPrerequisites());
				VaccineGap bdg = IMRUtils.getBirthdateGap(vaccine, calendarId);
				vsch.setBirthdate_gap(bdg);
				vsch.setBirthdate(birthdate);
				vsch.setExpiry_date(IMRUtils.calculateExpiryDate(vaccine, birthdate, calendarId, sc));
				vsch.setChildId(childId);
				vsch.setVisitdate(centerVisitDate);
				
				List<Short> prerequisiteForVaccineIds =  sc.getCustomQueryService().getDataByHQL("select vaccinePrerequisiteId.vaccineId from VaccinePrerequisite where vaccinePrerequisiteId.vaccinePrerequisiteId = " + vaccine.getVaccineId());
				vsch.setPrerequisiteFor(prerequisiteForVaccineIds);
				
				Date schduedate = null;
				VaccineStatusType status = null;
				Date asgnduedate = null;
				Date vaccdate = null;
				Integer centid = null;
				boolean current_suspect = false;
				boolean retro_suspect = false;
				boolean prerequisite_passed = false;
				boolean prerequisite_given_today = false;
				boolean isexpired = false;
				
				boolean has_vaccination_history = false;

				// schedule duedate is applicable only in case of vaccines following WHO schedule
				if(bdg != null){
					schduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, null, null, null, calendarId, sc);
				}
				
				List<Vaccination> pvacc = null;
				List<Vaccination> vvacc = null;

				if(childId != null){
					pvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.SCHEDULED, 0, 12, true, null);
					vvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.VACCINATED, 0, 12, true, null);
					vvacc.addAll(sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.RETRO, 0, 12, true, null));
					vvacc.addAll(sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.RETRO_DATE_MISSING, 0, 12, true, null));

					if(pvacc.size() > 1){
						throw new IllegalStateException(pvacc.size()+" vaccinations found Pending for "+vaccine.getName()+" for child "+childId);
					}
				}
			
				if(scheduleRetro != null && scheduleRetro.size() > 0){
					for (VaccineSchedule vsr : scheduleRetro) {
						
//						System.out.println(vsr.getVaccinationHistory().getVaccineId() + "  " +  vsr.getVaccinationHistory().getVaccinationDate() + " " +  vsr.getVaccinationHistory().getVaccinationStatus());
						
						if(vsr.getVaccine().getVaccineId() == vaccine.getVaccineId() && (vsr.getVaccinationHistory().getVaccinationStatus() != VACCINATION_STATUS.INVALID_DOSE))
						{
							if(vvacc != null){
								vvacc.add(vsr.getVaccinationHistory());
								has_vaccination_history = true;
							}
							else{
								vvacc = new ArrayList<Vaccination>();
								vvacc.add(vsr.getVaccinationHistory());
								has_vaccination_history = true;
							}
						}
						
						else if(vsr.getVaccine().getVaccineId() == vaccine.getVaccineId() && (vsr.getVaccinationHistory().getVaccinationStatus() == VACCINATION_STATUS.INVALID_DOSE )){
							invalid_schedule.add(vsr);
							break;
						 }
					}
				}
				
				
				// if vaccine already given donot move forward just set few variables, status and goto next vaccine 
				if(vvacc != null && vvacc.size() > 0){
					
			
					if(has_vaccination_history){
						if( vvacc.get(0).getVaccinationStatus() == VACCINATION_STATUS.RETRO ) status = VaccineStatusType.CURRENT_RETRO;
						if( vvacc.get(0).getVaccinationStatus() == VACCINATION_STATUS.RETRO_DATE_MISSING ) status = VaccineStatusType.CURRENT_RETRO_DATE_MISSING;
						if( vvacc.get(0).getVaccinationStatus() == VACCINATION_STATUS.INVALID_DOSE ) status = VaccineStatusType.INVALID_DOSE;
					}else{
						status = VaccineStatusType.VACCINATED_EARLIER;
					}
					
					
					asgnduedate = vvacc.get(0).getVaccinationDuedate();
					vaccdate = vvacc.get(0).getVaccinationDate();
					centid = vvacc.get(0).getVaccinationCenterId();
					
				}
				else {// if not given
					prerequisite_passed = IMRUtils.passVaccinePrerequisiteCheck(vsch, schedule, calendarId);
					prerequisite_given_today = IMRUtils.isPrerequisiteVaccinatedOnCurrentVisit(vsch, schedule, calendarId);
					
					//TODO
					boolean isPrerequisiteOverAge = false;

					if(pvacc!=null && pvacc.size() > 0){
						asgnduedate = pvacc.get(0).getVaccinationDuedate();
					}
					else if(prerequisite_passed){
						
						VaccineGap oag = IMRUtils.getOverAgeGap(vaccine, calendarId);
						if(oag != null){
//							System.out.println(vaccine.getName() + " over " + oag.getValue() + " " + oag.getGapTimeUnit());
							
							isPrerequisiteOverAge = IMRUtils.isPrerequisiteOverAge(vaccine,  schedule, calendarId, vsch.getBirthdate());
//							System.out.println("isPrerequisiteOverAge " + isPrerequisiteOverAge);
						}
						
						Date prevVaccDate = IMRUtils.getPrerequisiteVaccineDate(vsch, schedule, calendarId);
//						asgnduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, prevVaccDate, null, ignoreCenter?null:vaccinationCenterId, calendarId, sc);
						asgnduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, prevVaccDate, null, ignoreCenter?null:vaccinationCenterId, calendarId,isPrerequisiteOverAge, sc);
					
					}
					
					//calculate retro/current status only if vaccine belong to schedule
					if(schduedate != null){
						int minGracePeriod = vsch.getVaccine().getMinGracePeriodDays()!=null?vsch.getVaccine().getMinGracePeriodDays():0;
//						int maxGracePeriod = vsch.getVaccine().getMaxGracePeriodDays();
						
						Date minGraceDate = new Date(centerVisitDate.getTime()+(-minGracePeriod* 24 * 60 * 60 * 1000));
//						Date maxGraceDate = new Date(centerVisitDate.getTime()+(maxGracePeriod* 24 * 60 * 60 * 1000));
						
						current_suspect = (asgnduedate != null 
								&& asgnduedate.compareTo(minGraceDate) >= 0 /*&& asgnduedate.compareTo(maxGraceDate) <= 0*/)
							    || (schduedate.compareTo(minGraceDate) >= 0 /*&& schduedate.compareTo(maxGraceDate) <= 0*/);
						
						retro_suspect = (asgnduedate != null && asgnduedate.compareTo(minGraceDate) == -1)
							|| (schduedate.compareTo(minGraceDate) == -1);
						
//						System.out.println("vaccine\tminGraceDate\tmaxGraceDate\tAssignDueDate\tschduedate\tcenterVisitDate\tcurrent_suspect\tretro_suspect");
//						System.out.println(vaccine.getName()+"\t"+minGraceDate+"\t"+maxGraceDate+"\t"+asgnduedate+"\t"+schduedate+"\t"+centerVisitDate+"\t"+current_suspect+"\t"+retro_suspect);
						
						if(asgnduedate != null && centerVisitDate.compareTo(new Date(asgnduedate.getTime()+(-minGracePeriod* 24 * 60 * 60 * 1000))) < 0){
							current_suspect = false;
							retro_suspect = false;
							status = VaccineStatusType.NOT_ALLOWED;
						}
					
					}
					
					
					
					
					// N/A or out of schedule or expiry date has passed
					if(schduedate == null){
						status = null;
						asgnduedate = null;
					} 
					else if(vsch.getExpiry_date() != null && vsch.getExpiry_date().before(centerVisitDate)){
						status = null;
						isexpired = true;
					}
					else if(!prerequisite_passed){
						status = VaccineStatusType.NOT_ALLOWED;
					}
					else if(current_suspect){
						status = VaccineStatusType.VACCINATED;
						vaccdate = centerVisitDate;
						centid = vaccinationCenterId;
						vsch.setCenter(vaccinationCenterId);
					}
					else if(retro_suspect){
						status = VaccineStatusType.RETRO;
						vsch.setCenter(vaccinationCenterId);
					}
					else if(prerequisite_passed){
						status = VaccineStatusType.SCHEDULED;
					}
				}
				
				vsch.setExpired(isexpired);
				vsch.setSchedule_duedate(schduedate);
				vsch.setStatus(status == null?"":status.name());
				vsch.setAssigned_duedate(asgnduedate);
				vsch.setVaccination_date(vaccdate);
				vsch.setCenter(centid);
				vsch.setIs_current_suspect(current_suspect);
				vsch.setIs_retro_suspect(retro_suspect);
				vsch.setPrerequisite_passed(prerequisite_passed);
				vsch.setPrerequisite_given_on_current_visit(prerequisite_given_today);
				
				schedule.add(vsch);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			sc.closeSession();
		}
		
		schedule.addAll(invalid_schedule);
		
//		System.out.println("\n generate default schedule ---------\n");
//		for (VaccineSchedule ivs : schedule) {
//			ivs.printVaccineSchedule();
//		}
//		System.out.println("\n ---------\n");
		
		return schedule;
	}

	public static ArrayList<VaccineSchedule> updateSchedule(ArrayList<VaccineSchedule> schedule, Integer calendarId){
		ArrayList<VaccineSchedule> updatedSchedule = new ArrayList<VaccineSchedule>();
		ServiceContext sc = Context.getServices();
		try {			
			for (VaccineSchedule schmap : schedule) {
				// if NOT  = vaccinated or ignorable
				if(!StringUtils.isEmptyOrWhitespaceOnly(schmap.getStatus()) 
						&& schmap.getSchedule_duedate() != null 
						&& !schmap.getExpired()
						&& !schmap.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name())){
					boolean passedPrereqCheck = IMRUtils.passVaccinePrerequisiteCheck(schmap, schedule, calendarId);
					boolean prereqGivenToday = IMRUtils.isPrerequisiteVaccinatedOnCurrentVisit(schmap, schedule, calendarId);
	
					if(!passedPrereqCheck){
						schmap.setStatus(VaccineStatusType.NOT_ALLOWED.name());
						schmap.setAuto_calculated_date(null);
						schmap.setAssigned_duedate(null);
						schmap.setCenter(null);
						schmap.setVaccination_date(null);
						schmap.setPrerequisite_passed(passedPrereqCheck);
					}
					// if not allowed change status otherwise preserve user selection
					else if(schmap.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())
							|| schmap.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name()))
					{
						Vaccine vac = sc.getVaccinationService().findVaccineById(schmap.getVaccine().getVaccineId());
						schmap.setAssigned_duedate(IMRUtils.calculateVaccineDuedate(vac, schmap.getBirthdate(), IMRUtils.getPrerequisiteVaccineDate(schmap, schedule, calendarId), null, schmap.getCenter(), calendarId, sc));
						
						if(IMRUtils.isPrerequisiteVaccinatedOnCurrentVisit(schmap, schedule, calendarId)){
							schmap.setStatus(VaccineStatusType.SCHEDULED.name());
							schmap.setCenter(null);
							schmap.setVaccination_date(null);
						}
						else if(schmap.getIs_retro_suspect()){
							schmap.setStatus(VaccineStatusType.RETRO.name());
							schmap.setCenter(null);
							schmap.setVaccination_date(null);
						}
						else if(schmap.getIs_current_suspect()){
							schmap.setStatus(VaccineStatusType.VACCINATED.name());
							schmap.setCenter(schmap.getCenter());
							schmap.setVaccination_date(schmap.getVisitdate());
						}
						else {
							schmap.setStatus(VaccineStatusType.SCHEDULED.name());
							schmap.setCenter(null);
							schmap.setVaccination_date(null);
						}
						schmap.setPrerequisite_passed(passedPrereqCheck);
						schmap.setPrerequisite_given_on_current_visit(prereqGivenToday);
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
	@SuppressWarnings("rawtypes")
	public static Map<String, String> validateVaccineScheduleDates(ArrayList<VaccineSchedule> schedule, Integer calendarId) {
		Map<String, String> map = new HashMap<String, String>();
		ServiceContext sc = Context.getServices();
		String errormsg = "";
		try{
			for (VaccineSchedule sch : schedule) {
				//only validate vaccines that has not been given yet and which are associated with schedule
				if(sch.getSchedule_duedate() != null 
						&& !StringUtils.isEmptyOrWhitespaceOnly(sch.getStatus()) 
						&& !sch.getStatus().equalsIgnoreCase(VaccineStatusType.NOT_ALLOWED.name())
						&& !sch.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO_DATE_MISSING.name())
						&& !sch.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED_EARLIER.name()))
				{
					Vaccine vac = sc.getVaccinationService().findVaccineById(sch.getVaccine().getVaccineId());
					Date lastVaccineDate = null;
					Date validatedDate = null;
					if(sch.getChildId() != null){
						List l = sc.getCustomQueryService().getDataBySQL("SELECT vaccinationDate FROM vaccination WHERE childId = "+sch.getChildId()+" AND vaccinationDate IS NOT NULL ORDER BY vaccinationDate DESC");
						lastVaccineDate = (Date) (l.size() > 0 ? l.get(0) : null);
					}
					Date shouldBeDate = IMRUtils.calculateVaccineDuedate(vac, sch.getBirthdate(), IMRUtils.getPrerequisiteVaccineDate(sch, schedule, calendarId), lastVaccineDate, sch.getCenter(), calendarId, sc);
					
					if(sch.getStatus().equalsIgnoreCase(VaccineStatusType.VACCINATED.name())
							|| sch.getStatus().equalsIgnoreCase(VaccineStatusType.RETRO.name()))
					{
						validatedDate = sch.getVaccination_date();
					}
					else if(sch.getStatus().equalsIgnoreCase(VaccineStatusType.SCHEDULED.name())){
						validatedDate = sch.getAssigned_duedate();
					}
					
					// if vaccination date is far away from shouldbe date 
					if(DateUtils.differenceBetweenIntervals(validatedDate, shouldBeDate, TIME_INTERVAL.DATE) < -7
							|| DateUtils.differenceBetweenIntervals(validatedDate, shouldBeDate, TIME_INTERVAL.DATE) > 14){
						map.put("ERROR", "ERROR");
						errormsg += "\nSpecified date for "+sch.getVaccine().getName()+" doesnot follow schedule and is far away expected date (actual : expected) - ("+WebGlobals.GLOBAL_JAVA_DATE_FORMAT.format(validatedDate)+" : "+WebGlobals.GLOBAL_JAVA_DATE_FORMAT.format(shouldBeDate)+")";
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
			map.put("ERROR", "ERROR");
			map.put("MESSAGE", errormsg+" - schedule validation threw error. Recheck your schedule.");
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

	public Date getVisitdate() {
		return visitdate;
	}

	public void setVisitdate(Date visitdate) {
		this.visitdate = visitdate;
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

	public Vaccination getVaccinationHistory() {
		return vaccinationHistory;
	}


	public void setVaccinationHistory(Vaccination vaccinationHistory) {
		this.vaccinationHistory = vaccinationHistory;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public Boolean getPrerequisite_passed() {
		return prerequisite_passed;
	}

	public void setPrerequisite_passed(Boolean prerequisite_passed) {
		this.prerequisite_passed = prerequisite_passed;
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public Boolean getPrerequisite_given_on_current_visit() {
		return prerequisite_given_on_current_visit;
	}

	public void setPrerequisite_given_on_current_visit(
			Boolean prerequisite_given_on_current_visit) {
		this.prerequisite_given_on_current_visit = prerequisite_given_on_current_visit;
	}


	public List<Short> getPrerequisiteFor() {
		return prerequisiteFor;
	}


	public void setPrerequisiteFor(List<Short> prerequisiteFor) {
		this.prerequisiteFor = prerequisiteFor;
	}

	//TODO JennerX
	public static ArrayList<VaccineSchedule> validateVaccineHistory(List<Vaccination> retroVaccinationL,
			Date birthdate, Date centerVisitDate, Integer childId, Integer vaccinationCenterId, boolean ignoreCenter, Integer healthProgramId) {

		ServiceContext sc = Context.getServices();
		ArrayList<VaccineSchedule> scheduleL = new ArrayList<VaccineSchedule>();
		ArrayList<VaccineSchedule> scheduleH = new ArrayList<VaccineSchedule>();
		
		
//		System.out.println( healthProgramId + " healthProgramId ");
		//TODO remove comment
		Integer calendarId = (Integer) sc.getCustomQueryService().getDataByHQL("select vaccinationcalendarId from HealthProgram where programId = " + healthProgramId).get(0);
		
//		Integer calendarId = 2;
		
//		System.out.println(calendarId + " " + healthProgramId + " healthProgramId calendarId");
		
		try {

			for (Vaccination va : retroVaccinationL) {
				
				VaccineSchedule schedule = new VaccineSchedule();
				Vaccine vaccine = sc.getVaccinationService().findVaccineById(va.getVaccineId());

				schedule.setVaccine(vaccine);
				schedule.setPrerequisites(vaccine.getPrerequisites());
				VaccineGap bdg = IMRUtils.getBirthdateGap(vaccine, calendarId);
				schedule.setBirthdate_gap(bdg);
				schedule.setBirthdate(birthdate);
				schedule.setExpiry_date(IMRUtils.calculateExpiryDate(vaccine, birthdate, calendarId, sc));
				schedule.setChildId(childId);
				schedule.setVisitdate(centerVisitDate);

				Date schduedate = null;
				VaccineStatusType status = null;
				Date asgnduedate = null;
				Date vaccdate = null;
				Integer centid = null;
				boolean current_suspect = false;
				boolean retro_suspect = false;
				boolean prerequisite_passed = false;
				boolean prerequisite_given_today = false;
				
				boolean isexpired = false;
				
				boolean is_retro_history = false;
				
				// schedule duedate is applicable only in case of vaccines following WHO schedule
				if(bdg != null){
					schduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, null, null, null, calendarId, sc);
				}

				List<Vaccination> pvacc = null;
				List<Vaccination> vvacc = null;

				if(childId != null){
					pvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.SCHEDULED, 0, 12, true, null);
					vvacc = sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.VACCINATED, 0, 12, true, null);
					vvacc.addAll(sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.RETRO, 0, 12, true, null));
					vvacc.addAll(sc.getVaccinationService().findByCriteria(childId, vaccine.getVaccineId(), VACCINATION_STATUS.RETRO_DATE_MISSING, 0, 12, true, null));

					if(pvacc.size() > 1){
						throw new IllegalStateException(pvacc.size()+" vaccinations found Pending for "+ vaccine.getName()+" for child "+childId);
					}
				}

				// if vaccine already given donot move forward just set few variables, status and goto next vaccine 
				if(vvacc != null && vvacc.size() > 0){
					status = VaccineStatusType.VACCINATED_EARLIER;
					asgnduedate = vvacc.get(0).getVaccinationDuedate();
					vaccdate = vvacc.get(0).getVaccinationDate();
					centid = vvacc.get(0).getVaccinationCenterId();
					
					schedule.setSchedule_duedate(schduedate);
					schedule.setStatus(status == null?"":status.name());
					schedule.setAssigned_duedate(asgnduedate);
					schedule.setVaccination_date(vaccdate);
					schedule.setCenter(centid);
					scheduleH.add(schedule);
				}
				else {
					// if not given
					List<VaccineSchedule> scheduleHL =  new ArrayList<VaccineSchedule>();
					if(scheduleH != null && scheduleH.size() > 0){	scheduleHL.addAll(scheduleH);	}
					if(scheduleL != null && scheduleL.size() > 0){	scheduleHL.addAll(scheduleL);	}
					
					prerequisite_passed = IMRUtils.passVaccinePrerequisiteCheck(schedule, scheduleHL, calendarId);
					prerequisite_given_today = IMRUtils.isPrerequisiteVaccinatedOnCurrentVisit(schedule, scheduleL, calendarId);
				
					//TODO
					boolean isPrerequisiteOverAge = false;
					
					if(pvacc!=null && pvacc.size() > 0){
						asgnduedate = pvacc.get(0).getVaccinationDuedate();
					}
					else if(prerequisite_passed){
						VaccineGap oag = IMRUtils.getOverAgeGap(vaccine, calendarId);
						if(oag != null){
//							System.out.println(vaccine.getName() + " over " + oag.getValue() + " " + oag.getGapTimeUnit());
							
							isPrerequisiteOverAge = IMRUtils.isPrerequisiteOverAge(vaccine,  scheduleHL, calendarId, schedule.getBirthdate());
//							System.out.println("isPrerequisiteOverAge " + isPrerequisiteOverAge);
						}
						
						Date prevVaccDate = IMRUtils.getPrerequisiteVaccineDate(schedule, scheduleHL, calendarId);
//						asgnduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, prevVaccDate, null, ignoreCenter?null:vaccinationCenterId, calendarId, sc);
						asgnduedate = IMRUtils.calculateVaccineDuedate(vaccine, birthdate, prevVaccDate, null, ignoreCenter?null:vaccinationCenterId, calendarId,isPrerequisiteOverAge, sc);
					}
					
					//calculate retro/current status only if vaccine belong to schedule
					if(schduedate != null && prerequisite_passed){
						int minGracePeriod = schedule.getVaccine().getMinGracePeriodDays()!=null?schedule.getVaccine().getMinGracePeriodDays():0;
						int maxGracePeriod = schedule.getVaccine().getMaxGracePeriodDays()!=null?schedule.getVaccine().getMaxGracePeriodDays():0;

						Date minGraceDate = new Date(centerVisitDate.getTime() + (-minGracePeriod * 24 * 60 * 60 * 1000));
//						Date maxGraceDate = new Date(centerVisitDate.getTime() + ( maxGracePeriod * 24 * 60 * 60 * 1000));

						current_suspect = (asgnduedate != null && 
								   asgnduedate.compareTo(minGraceDate) >= 0 /*&& asgnduedate.compareTo(maxGraceDate) <= 0*/)
								|| (schduedate.compareTo(minGraceDate) >= 0 /*&& schduedate.compareTo(maxGraceDate) <= 0*/);

						retro_suspect = (asgnduedate != null && asgnduedate.compareTo(minGraceDate) == -1)
								|| (schduedate.compareTo(minGraceDate) == -1);
						
						if(asgnduedate != null && va.getVaccinationDate() == null){
							va.setVaccinationDate(asgnduedate);
							va.setVaccinationStatus(VACCINATION_STATUS.RETRO_DATE_MISSING);
						}
						
						
						int minGracePeriod_rh = schedule.getVaccine().getMinGracePeriodDays()!=null?schedule.getVaccine().getMinGracePeriodDays():0;
						int maxGracePeriod_rh = schedule.getVaccine().getMaxGracePeriodDays()!=null?schedule.getVaccine().getMaxGracePeriodDays():0;

						Date minGraceDate_rh = new Date(va.getVaccinationDate().getTime() + (-minGracePeriod_rh * 24 * 60 * 60 * 1000));
						Date maxGraceDate_rh = new Date(va.getVaccinationDate().getTime() + ( maxGracePeriod_rh * 24 * 60 * 60 * 1000));

						is_retro_history = (asgnduedate != null && 
								   asgnduedate.compareTo(minGraceDate_rh) >= 0 && asgnduedate.compareTo(maxGraceDate_rh) <= 0)
								|| (schduedate.compareTo(minGraceDate_rh) >= 0 && schduedate.compareTo(maxGraceDate_rh) <= 0);
						
						if(is_retro_history && va.getVaccinationStatus() != VACCINATION_STATUS.RETRO_DATE_MISSING){
							va.setVaccinationStatus(VACCINATION_STATUS.RETRO);
						}
						

					// N/A or out of schedule or expiry date has passed
					if(schduedate == null){
						status = null;
						asgnduedate = null;
					} 
					else if(schedule.getExpiry_date() != null && schedule.getExpiry_date().before(centerVisitDate)){
						status = null;
						isexpired = true;
						
						if(/*schedule.getExpiry_date().compareTo(centerVisitDate) == 0*/
								schedule.getExpiry_date().before(va.getVaccinationDate())){
							status = null;
							isexpired = true;
						}else{
							status = VaccineStatusType.RETRO;
							isexpired = false;
						}
					}
					else if(!prerequisite_passed){
						status = VaccineStatusType.NOT_ALLOWED;
					}
					else if(current_suspect){
						status = VaccineStatusType.VACCINATED;
						vaccdate = centerVisitDate;
						centid = vaccinationCenterId;
					}
					else if(retro_suspect){
						status = VaccineStatusType.RETRO;
					}
					else if(prerequisite_passed){
						status = VaccineStatusType.SCHEDULED;
					}
				}
					
					if(!prerequisite_passed){
						status = VaccineStatusType.NOT_ALLOWED;
					}

					if(is_retro_history /*&& (status == VaccineStatusType.RETRO || status == VaccineStatusType.VACCINATED)*/){
					 
					status = (va.getVaccinationStatus() == VACCINATION_STATUS.RETRO_DATE_MISSING) ? VaccineStatusType.RETRO_DATE_MISSING : VaccineStatusType.RETRO ;
					
					if(schedule.getExpiry_date() != null && va.getVaccinationDate().before(schedule.getExpiry_date())
							&& va.getVaccinationDate().before(centerVisitDate) && !va.getVaccinationDate().before(birthdate)){
					
						vaccdate = va.getVaccinationDate();
						
					}
					else {
//						vaccdate = null; 
						vaccdate = va.getVaccinationDate();
						status = VaccineStatusType.INVALID_DOSE;
					}
				}
				else {
//					vaccdate = null; 
					vaccdate = va.getVaccinationDate();
					status = VaccineStatusType.INVALID_DOSE;
				}


				schedule.setExpired(isexpired);
				schedule.setSchedule_duedate(schduedate);
				schedule.setStatus(status == null?"":status.name());
				schedule.setAssigned_duedate(asgnduedate);
				schedule.setVaccination_date(vaccdate);
				schedule.setCenter(centid);
				schedule.setIs_current_suspect(current_suspect);
				schedule.setIs_retro_suspect(retro_suspect);
				schedule.setPrerequisite_passed(prerequisite_passed);
				schedule.setPrerequisite_given_on_current_visit(prerequisite_given_today);

//				if(status == VaccineStatusType.RETRO){
//					scheduleL.add(schedule);
//				}	
				
				if( status == VaccineStatusType.INVALID_DOSE){
					va.setVaccinationStatus(VACCINATION_STATUS.INVALID_DOSE);
				}
				
				if(is_retro_history || status == VaccineStatusType.INVALID_DOSE){
					schedule.setVaccinationHistory(va);
					scheduleL.add(schedule);
				}
				
//				System.out.println(schedule.getStatus() + " " + schedule.getVaccination_date() + " " + schedule.getVaccine().getName());
//				System.out.print(is_retro_history + "   - ");
//				schedule.printVaccineSchedule();
			
				}
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			sc.closeSession();
		}
		
//		System.out.println("\n validate vaccination history ***********\n");
//		for (VaccineSchedule ivs : scheduleL) {
//			ivs.printVaccineSchedule();
//		}
//		System.out.println("\n ***********\n");
		
		return scheduleL;
	}
	
	public void printVaccineSchedule(){
		
		System.out.printf("%-10s", this.getVaccine().getName());
		System.out.printf("%30s", this.getBirthdate());
		System.out.printf("%30s", this.getVisitdate());
		
		System.out.printf("%-55s", "\t" + this.getPrerequisites());
		
		if(this.getBirthdate_gap() != null){
			if(this.getBirthdate_gap().getVaccine() != null && this.getBirthdate_gap().getVaccineGapType() != null){
			System.out.printf("%-30s","\t[ " + this.getBirthdate_gap().getVaccine().getName()+" " +  this.getBirthdate_gap().getVaccineGapType().getName() + " ]");
			}
		} else {
			System.out.printf( "-%30s", "\t[]");
		}
		
		System.out.printf("%30s",this.getSchedule_duedate());
		System.out.printf("%30s",this.getAuto_calculated_date());
		System.out.printf("%30s",this.getAssigned_duedate());
		System.out.printf("%30s",this.getVaccination_date());
		System.out.printf("%30s",this.getExpiry_date());
		System.out.printf("%7s",this.getCenter());
		System.out.printf("%25s",this.getStatus());
		System.out.printf("%9s",this.getIs_current_suspect());
		System.out.printf("%9s",this.getIs_retro_suspect());
		System.out.printf("%9s",this.getVaccinationObjCurrentVisit());
		System.out.printf("%9s",this.getChildId());
		System.out.printf("%9s",this.getPrerequisite_passed());
		System.out.printf("%9s",this.getExpired());
		System.out.printf("%9s",this.getPrerequisite_given_on_current_visit());
		System.out.println();
	}
	
}
