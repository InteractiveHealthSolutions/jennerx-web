/**
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.TimelinessStatus;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
import org.ird.unfepi.model.dao.DAOCalendarDay;
import org.ird.unfepi.model.dao.DAOVaccination;
import org.ird.unfepi.model.dao.DAOVaccinationCenter;
import org.ird.unfepi.model.dao.DAOVaccinationCenterVaccineDay;
import org.ird.unfepi.model.dao.DAOVaccinator;
import org.ird.unfepi.model.dao.DAOVaccine;
import org.ird.unfepi.model.dao.DAOWomenVaccination;
import org.ird.unfepi.service.WomenVaccinationService;

/**
 * @author Safwan
 *
 */
public class WomenVaccinationServiceImpl implements WomenVaccinationService {

	private ServiceContext sc;
	private DAOVaccine daovacc;

	private DAOWomenVaccination daowomenvaccination;

	private DAOVaccinationCenter daovacccenter;

	private DAOVaccinator daovaccinator;

	private DAOCalendarDay daocalendar;

	private DAOVaccinationCenterVaccineDay daovaccday;

	public WomenVaccinationServiceImpl(ServiceContext sc, DAOVaccine daovacc,
			DAOWomenVaccination daowomenvaccination,
			DAOVaccinationCenter daovacccenter, DAOVaccinator daovaccinator,
			DAOCalendarDay daocalendar,
			DAOVaccinationCenterVaccineDay daovaccday) {
		this.sc = sc;
		this.daovacc = daovacc;
		this.daowomenvaccination = daowomenvaccination;
		this.daovacccenter = daovacccenter;
		this.daovaccinator = daovaccinator;
		this.daocalendar = daocalendar;
		this.daovaccday = daovaccday;
	}

	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if (clazz == Vaccine.class) {
			return daovacc.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == Vaccination.class) {
			return daowomenvaccination.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == VaccinationCenter.class) {
			return daovacccenter.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == Vaccinator.class) {
			return daovaccinator.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == CalendarDay.class) {
			throw new UnsupportedOperationException();
		} else if (clazz == VaccinationCenterVaccineDay.class) {
			throw new UnsupportedOperationException();
		}

		return null;
	}

	@Override
	public Vaccine findVaccineById(short id) {
		Vaccine obj = daovacc.findById(id);
		return obj;
	}

	@Override
	public Serializable save(WomenVaccination womenVaccination) {
		return daowomenvaccination.save(womenVaccination);
	}

	@Override
	public WomenVaccination getVaccinationRecord(int recordId,
			boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		WomenVaccination pv = daowomenvaccination.findById(recordId,
				isreadonly, mappingsToJoin, sqlFilter);
		return pv;
	}

	@Override
	public List<WomenVaccination> findByWomenId(int womenId) {
		List<WomenVaccination> pv = daowomenvaccination.findByWomenId(womenId);
		return pv;
	}

	@Override
	public List<WomenVaccination> findVaccinationRecordByCriteria(Integer womenId, String vaccineName, Integer vaccinatorId,Integer vaccinationCenterId, String epiNumber, Date dueDatesmaller,
			Date dueDategreater, Date vaccinationDatesmaller,Date vaccinationDategreater, TimelinessStatus timelinessStatus,WOMEN_VACCINATION_STATUS vaccinationStatus,
			boolean putNotWithVaccinationStatus, int firstResult,int fetchsize, boolean isreadonly, String[] mappingsToJoin,String[] sqlFilter) {
		List<WomenVaccination> pvl=daowomenvaccination.findByCriteria(womenId
				, vaccineName, vaccinatorId, vaccinationCenterId, epiNumber, dueDatesmaller, dueDategreater, vaccinationDatesmaller
				, vaccinationDategreater, timelinessStatus, vaccinationStatus, putNotWithVaccinationStatus, firstResult, fetchsize
				,isreadonly, mappingsToJoin, sqlFilter);
		return pvl;
	}

	@Override
	public void updateVaccinationRecord(WomenVaccination vaccinationRecord) {
		daowomenvaccination.update(vaccinationRecord);
	}

	@Override
	public List<WomenVaccination> getAllVaccinationRecord(int firstResult,int fetchsize, boolean isreadonly, String[] mappingsToJoin,
			String[] sqlFilter) {
		List<WomenVaccination> pvl = daowomenvaccination.getAll(firstResult, fetchsize
				,isreadonly, mappingsToJoin, sqlFilter);
		return pvl;
	}

}
