package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import org.ird.unfepi.model.exception.VaccinationDataException;

public interface VaccinationService {
	
	Vaccine findVaccineById(short id);

	Vaccine getByName(String vaccineName);

	List<Vaccine> getAll(boolean readonly, String[] mappingsToJoin, String commaSeparatedOrderByFields/*int firstResult, int fetchSize*/);

	List<Vaccine> findVaccine(String vaccineName, boolean readonly/*,int firstResult, int fetchSize*/);

	Serializable addVaccine(Vaccine vaccine) throws VaccinationDataException;

	void updateVaccine(Vaccine vaccine);

	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	Vaccination getVaccinationRecord(int recordId,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	Vaccination getRecievedVaccination(int childId, String vaccineName,boolean isreadonly, String[] mappingsToJoin);

	Vaccination getPendingVaccination(int childId, String vaccineName,boolean isreadonly, String[] mappingsToJoin) throws VaccinationDataException;

	List<Vaccination> findVaccinationRecordByCriteria(Integer childId,
			String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus,
			VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize,
			boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);
	
	List<Vaccination> findByCriteria (int childId, Short vaccineId, VACCINATION_STATUS vaccinationStatus,
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin);
	
	List<Vaccination> getAllVaccinationRecord(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter);

	List<Vaccination> findVaccinationRecordByCriteriaIncludeName(String partOfName,
			String vaccineName,String epiNumber, Date dueDatesmaller, Date dueDategreater,
			Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus,
			VACCINATION_STATUS vaccinationStatus, String armName, int firstResult,
			int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) throws VaccinationDataException;

	Serializable addVaccinationRecord(Vaccination vacinationRecord);

	void updateVaccinationRecord(Vaccination vacinationRecord);

	List<VaccinationCenterVaccineDay> getAllVaccinationCenterVaccineDay(boolean readonly);

	List<VaccinationCenterVaccineDay> findVaccinationCenterVaccineDayByCriteria(Integer vaccinationCenterId, Short vaccineId, Short dayNumber, boolean readonly);

	VaccinationCenter findVaccinationCenterById(int Id);
	
	List<VaccinationCenter> getAllVaccinationCenter(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	List<VaccinationCenter> getAllVaccinationCenter(boolean readonly, String[] mappingsToJoin);

	VaccinationCenter findVaccinationCenterById(int mappedId, boolean readonly, String[] mappingsToJoin);
	
	VaccinationCenter findVaccinationCenterById(String programId, boolean readonly, String[] mappingsToJoin);

	VaccinationCenter findVaccinationCenterByName(String vaccinationCenterName, boolean readonly, String[] mappingsToJoin);
	
	List<VaccinationCenter> findVaccinationCenterByCriteria(String programIdLike,
			String nameLike, CenterType centerType, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin);

	Serializable saveVaccinationCenter(VaccinationCenter vaccinationCenter);

	void updateVaccinationCenter(VaccinationCenter vaccinationCenter);

	Vaccinator findVaccinatorById(int id);

	List<Vaccinator> getAllVaccinator(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	Vaccinator findVaccinatorById(int mappedId, boolean readonly, String[] mappingsToJoin);

	Vaccinator findVaccinatorById(String programId, boolean readonly,	String[] mappingsToJoin);
	
	List<Vaccinator> findByCriteria(String partOfName, String nic, Gender gender, Integer vaccinationCenterId
			, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin);

	Serializable saveVaccinator(Vaccinator vaccinator);

	void updateVaccinator(Vaccinator vaccinator);

	Vaccinator mergeUpdateVaccinator(Vaccinator vaccinator);

	CalendarDay getByDayNumber(short dayNumber);

	CalendarDay getByFullName(String fullName);

	CalendarDay getByShortName(String shortName);
	
	List<CalendarDay> getAllCalendarDay(boolean readonly);
	
	VaccinationCenterVaccineDay findById(VaccinationCenterVaccineDayId id);

	Serializable saveVaccinationCenterVaccineDay(VaccinationCenterVaccineDay vaccinationCenterVaccineDay);
	
	void updateVaccinationCenterVaccineDay(VaccinationCenterVaccineDay vaccinationCenterVaccineDay);

	VaccinationCenterVaccineDay mergeUpdateVaccinationCenterVaccineDay(VaccinationCenterVaccineDay vaccinationCenterVaccineDay);

	void deleteVaccinationCenterVaccineDay(VaccinationCenterVaccineDay vaccinationCenterVaccineDay);
}
