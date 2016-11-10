package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenter.CenterType;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.dao.DAOCalendarDay;
import org.ird.unfepi.model.dao.DAOVaccination;
import org.ird.unfepi.model.dao.DAOVaccinationCenter;
import org.ird.unfepi.model.dao.DAOVaccinationCenterVaccineDay;
import org.ird.unfepi.model.dao.DAOVaccinator;
import org.ird.unfepi.model.dao.DAOVaccine;
import org.ird.unfepi.model.exception.VaccinationDataException;
import org.ird.unfepi.service.VaccinationService;
import org.ird.unfepi.utils.date.DateUtils;

public class VaccinationServiceImpl implements VaccinationService{

	private ServiceContext sc;
	private DAOVaccine daovacc;
	
	private DAOVaccination daoptvaccination;
	
	private DAOVaccinationCenter daovacccenter;
	
	private DAOVaccinator daovaccinator;
	
	private DAOCalendarDay daocalendar;
	
	private DAOVaccinationCenterVaccineDay daovaccday;
	
	public VaccinationServiceImpl(ServiceContext sc, DAOVaccine daovacc, DAOVaccination daoptvaccination
			, DAOVaccinationCenter daovacccenter, DAOVaccinator daovaccinator, DAOCalendarDay daocalendar, DAOVaccinationCenterVaccineDay daovaccday) {
		this.sc = sc;
		this.daovacc=daovacc;
		this.daoptvaccination=daoptvaccination;
		this.daovacccenter = daovacccenter;
		this.daovaccinator = daovaccinator;
		this.daocalendar = daocalendar;
		this.daovaccday = daovaccday;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Vaccine.class){
			return daovacc.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == Vaccination.class){
			return daoptvaccination.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == VaccinationCenter.class){
			return daovacccenter.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == Vaccinator.class){
			return daovaccinator.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == CalendarDay.class){
			throw new UnsupportedOperationException();
		}
		else if(clazz == VaccinationCenterVaccineDay.class){
			throw new UnsupportedOperationException();
		}
		
		return null;
	}

	@Override
	public Vaccine getByName(String vaccineName){
		Vaccine v= daovacc.getByName(vaccineName);
		return v;
	}

	@Override
	public List<Vaccine> getAll(boolean readonly, String[] mappingsToJoin, String commaSeparatedOrderByFields/*int firstResult,int fetchSize*/){
		List<Vaccine> vlst= daovacc.getAll(readonly/*firstResult,fetchSize*/, mappingsToJoin, commaSeparatedOrderByFields);
		return vlst;
	}
	
	@Override
	public Vaccine findVaccineById(short id) {
		Vaccine obj = daovacc.findById(id);
		return obj;
	}
	
	@Override
	public List<Vaccine> getVaccinesById(Short[] vaccineIds, boolean readonly, String[] mappingsToJoin, String orderBySqlFormula) {
		return daovacc.getById(vaccineIds, readonly, mappingsToJoin, orderBySqlFormula);
	}

	@Override
	public List<Vaccine> findVaccine(String vaccineName, boolean readonly/*,int firstResult, int fetchSize*/){
		List<Vaccine> vlst= daovacc.findVaccine(vaccineName, readonly/*,firstResult,fetchSize*/);
		return vlst;
	}

	@Override
	public Serializable addVaccine(Vaccine vaccine) throws VaccinationDataException {
		if(daovacc.getByName(vaccine.getName().trim())!=null){
			throw new VaccinationDataException(VaccinationDataException.VACCINE_ALREADY_EXISTS);
		}
		return daovacc.save(vaccine);
	}

	@Override
	public void updateVaccine(Vaccine vaccine){
		daovacc.update(vaccine);
	}

	@Override
	public Serializable addVaccinationRecord(Vaccination vacinationRecord) {
		return daoptvaccination.save(vacinationRecord);
	}

	@Override
	public List<Vaccination> findVaccinationRecordByCriteria(
			Integer childId, String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber, Date dueDatesmaller,
			Date dueDategreater, Date vaccinationDatesmaller, Date vaccinationDategreater, 
			TimelinessStatus timelinessStatus, VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus,
			int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		if(dueDatesmaller!=null){
			dueDatesmaller=DateUtils.truncateDatetoDate(dueDatesmaller);
		}
		if(dueDategreater!=null){
			dueDategreater=DateUtils.roundoffDatetoDate(dueDategreater);
		}
		if(vaccinationDatesmaller!=null){
			vaccinationDatesmaller=DateUtils.truncateDatetoDate(vaccinationDatesmaller);
		}
		if(vaccinationDategreater!=null){
			vaccinationDategreater=DateUtils.roundoffDatetoDate(vaccinationDategreater);
		}
		List<Vaccination> pvl=daoptvaccination.findByCriteria(childId
				, vaccineName, vaccinatorId, vaccinationCenterId, epiNumber, dueDatesmaller, dueDategreater, vaccinationDatesmaller
				, vaccinationDategreater, timelinessStatus, vaccinationStatus, putNotWithVaccinationStatus, firstResult, fetchsize
				,isreadonly, mappingsToJoin, sqlFilter);
		return pvl;
	}

	@Override
	public List<Vaccination> findVaccinationRecordByCriteriaIncludeName(
						String partOfName, String vaccineName,String epiNumber, Date dueDatesmaller,
						Date dueDategreater, Date vaccinationDatesmaller, Date vaccinationDategreater, 
						TimelinessStatus timelinessStatus, VACCINATION_STATUS vaccinationStatus,
						String armName, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter){
		if(dueDatesmaller!=null){
			dueDatesmaller=DateUtils.truncateDatetoDate(dueDatesmaller);
		}
		if(dueDategreater!=null){
			dueDategreater=DateUtils.roundoffDatetoDate(dueDategreater);
		}
		if(vaccinationDatesmaller!=null){
			vaccinationDatesmaller=DateUtils.truncateDatetoDate(vaccinationDatesmaller);
		}
		if(vaccinationDategreater!=null){
			vaccinationDategreater=DateUtils.roundoffDatetoDate(vaccinationDategreater);
		}
		List<Vaccination> pvl=daoptvaccination.findByCriteriaIncludeName(partOfName
				, vaccineName, epiNumber, dueDatesmaller, dueDategreater, vaccinationDatesmaller
				, vaccinationDategreater, timelinessStatus, vaccinationStatus, armName, firstResult, fetchsize
				,isreadonly, mappingsToJoin, sqlFilter);
		return pvl;
	}
	

	@Override
	public List<Vaccination> findByCriteria (int childId, Short vaccineId, VACCINATION_STATUS vaccinationStatus,
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) {
		List<Vaccination> pvl=daoptvaccination.findByCriteria(childId, vaccineId, vaccinationStatus, firstResult, fetchsize, isreadonly, mappingsToJoin);
		return pvl;
	}
	
	@Override
	public List<Vaccination> getAllVaccinationRecord(int firstResult,int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		List<Vaccination> pvl=daoptvaccination.getAll(firstResult, fetchsize
				,isreadonly, mappingsToJoin, sqlFilter);
		return pvl;
	}
	
	@Override
	public Vaccination getPendingVaccination(int childId, String vaccineName,boolean isreadonly, String[] mappingsToJoin) throws VaccinationDataException{
		List<Vaccination> list = daoptvaccination.findByCriteria(childId, vaccineName, null, null, null, null, null, null, null, null, VACCINATION_STATUS.SCHEDULED, false, 0, 3, isreadonly, mappingsToJoin,null);
		
		if(list.size() == 1){
			return list.get(0);
		}
		else if(list.size() > 1){
			throw new VaccinationDataException(VaccinationDataException.MULITPLE_VACCINE_RECORDS_FOUND_PENDING, VaccinationDataException.MULITPLE_VACCINE_RECORDS_FOUND_PENDING);
		}
		
		return null;
	}
	@Override
	public Vaccination getRecievedVaccination(int childId, String vaccineName,boolean isreadonly, String[] mappingsToJoin){
		List<Vaccination> list = daoptvaccination.findByCriteria(childId, vaccineName, null, null, null, null, null, null, null, null, VACCINATION_STATUS.VACCINATED, false, 0, 10, isreadonly, mappingsToJoin,null);
		//List<Vaccination> list2 = daoptvaccination.findByCriteria(childId, vaccineName, null, null, null, null, null, VACCINATION_STATUS.LATE_VACCINATED, false, 0, 10, isreadonly, mappingsToJoin);
		if(list.size() > 0){
			return list.get(0);
		}
		/*else if(list2.size() > 0){
			return list2.get(0);
		}*/
		
		return null;
	}

	@Override
	public Vaccination getVaccinationRecord(int recordId,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		Vaccination pv=daoptvaccination.findById(recordId,isreadonly, mappingsToJoin, sqlFilter);
		return pv;
	}

	@Override
	public void updateVaccinationRecord(Vaccination vacinationRecord) {
		daoptvaccination.update(vacinationRecord);
	}
	
	public List<VaccinationCenterVaccineDay> getAllVaccinationCenterVaccineDay(boolean readonly){
		List<VaccinationCenterVaccineDay> list = daovaccday.getAll(readonly);
		return list;
	}
	
	public List<VaccinationCenterVaccineDay> findVaccinationCenterVaccineDayByCriteria(Integer vaccinationCenterId, Short vaccineId, Short dayNumber, boolean readonly){
		List<VaccinationCenterVaccineDay> list = daovaccday.findByCriteria(vaccinationCenterId, vaccineId, dayNumber, readonly);
		return list;
	}	

	@Override
	public VaccinationCenter findVaccinationCenterById(int Id) {
		return daovacccenter.findById(Id);
	}
	

	@Override
	public List<VaccinationCenter> getAllVaccinationCenter(int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<VaccinationCenter> list = daovacccenter.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}
	
	@Override
	public List<VaccinationCenter> getAllVaccinationCenter(boolean readonly, String[] mappingsToJoin) {
		List<VaccinationCenter> list = daovacccenter.getAll(readonly, mappingsToJoin);
		return list;
	}
	
	@Override
	public List<VaccinationCenter> getAllVaccinationCenterOrdered(boolean readonly, String[] mappingsToJoin) {
		List<VaccinationCenter> list = daovacccenter.getAllOrdered(readonly, mappingsToJoin);
		return list;
	}
	
	@Override
	public VaccinationCenter findVaccinationCenterById(int mappedId,
			boolean readonly, String[] mappingsToJoin) {
		VaccinationCenter obj = daovacccenter.findById(mappedId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public VaccinationCenter findVaccinationCenterById(String programId,
			boolean readonly, String[] mappingsToJoin) {
		VaccinationCenter obj = daovacccenter.findById(programId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public VaccinationCenter findVaccinationCenterByName(String vaccinationCenterName, boolean readonly, String[] mappingsToJoin) {
		return daovacccenter.findByName(vaccinationCenterName, readonly, mappingsToJoin);
	}
	
	@Override
	public List<VaccinationCenter> findVaccinationCenterByCriteria(
			String programIdLike, String nameLike, CenterType centerType,
			int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		List<VaccinationCenter> list = daovacccenter.findByCriteria(programIdLike, nameLike, centerType, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinationCenter(VaccinationCenter vaccinationCenter) {
		return daovacccenter.save(vaccinationCenter);
	}

	@Override
	public void updateVaccinationCenter(VaccinationCenter vaccinationCenter) {
		daovacccenter.update(vaccinationCenter);
	}

	@Override
	public Vaccinator findVaccinatorById(int id) {
		return daovaccinator.findById(id);
	}
	
	@Override
	public List<Vaccinator> getAllVaccinator(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		List<Vaccinator> list = daovaccinator.getAll(firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}
	
	@Override
	public Vaccinator findVaccinatorById(int mappedId, boolean readonly,
			String[] mappingsToJoin) {
		Vaccinator obj = daovaccinator.findById(mappedId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public Vaccinator findVaccinatorById(String programId, boolean readonly,
			String[] mappingsToJoin) {
		Vaccinator obj = daovaccinator.findById(programId, readonly, mappingsToJoin);
		return obj;
	}
	@Override
	public List<Vaccinator> findByCriteria(String partOfName, String nic, Gender gender, Integer vaccinationCenterId
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin){
		List<Vaccinator> list = daovaccinator.findByCriteria(partOfName, nic, gender, vaccinationCenterId, firstResult, fetchsize, readonly, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveVaccinator(Vaccinator vaccinator) {
		return daovaccinator.save(vaccinator);
	}

	@Override
	public void updateVaccinator(Vaccinator vaccinator) {
		daovaccinator.update(vaccinator);
	}

	@Override
	public Vaccinator mergeUpdateVaccinator(Vaccinator vaccinator) {
		return (Vaccinator) daovaccinator.merge(vaccinator);
	}

	@Override
	public CalendarDay getByDayNumber(short dayNumber) {
		return daocalendar.getByDayNumber(dayNumber);
	}

	@Override
	public CalendarDay getByFullName(String fullName) {
		return daocalendar.getByFullName(fullName);
	}

	@Override
	public CalendarDay getByShortName(String shortName) {
		return daocalendar.getByShortName(shortName);
	}
	
	@Override
	public List<CalendarDay> getAllCalendarDay(boolean readonly){
		return daocalendar.getAll(readonly);
	}

	@Override
	public VaccinationCenterVaccineDay findById(VaccinationCenterVaccineDayId id) {
		return daovaccday.findById(id);
	}

	@Override
	public Serializable saveVaccinationCenterVaccineDay(
			VaccinationCenterVaccineDay vaccinationCenterVaccineDay) {
		return daovaccday.save(vaccinationCenterVaccineDay);
	}

	@Override
	public void updateVaccinationCenterVaccineDay(
			VaccinationCenterVaccineDay vaccinationCenterVaccineDay) {
		daovaccday.update(vaccinationCenterVaccineDay);
	}

	@Override
	public VaccinationCenterVaccineDay mergeUpdateVaccinationCenterVaccineDay(
			VaccinationCenterVaccineDay vaccinationCenterVaccineDay) {
		return (VaccinationCenterVaccineDay) daovaccday.merge(vaccinationCenterVaccineDay);
	}
	@Override
	public void deleteVaccinationCenterVaccineDay(VaccinationCenterVaccineDay vaccinationCenterVaccineDay){
		daovaccday.delete(vaccinationCenterVaccineDay);
	}
}
