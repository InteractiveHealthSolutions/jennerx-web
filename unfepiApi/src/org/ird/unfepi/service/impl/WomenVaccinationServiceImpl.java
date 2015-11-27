/**
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CalendarDay;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;
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
	
	public WomenVaccinationServiceImpl(ServiceContext sc, DAOVaccine daovacc, DAOWomenVaccination daowomenvaccination
			, DAOVaccinationCenter daovacccenter, DAOVaccinator daovaccinator, DAOCalendarDay daocalendar, DAOVaccinationCenterVaccineDay daovaccday) {
		this.sc = sc;
		this.daovacc=daovacc;
		this.daowomenvaccination=daowomenvaccination;
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
			return daowomenvaccination.LAST_QUERY_TOTAL_ROW_COUNT();
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
	public Vaccine findVaccineById(short id) {
		Vaccine obj = daovacc.findById(id);
		return obj;
	}

	@Override
	public Serializable save(WomenVaccination womenVaccination) {
		return daowomenvaccination.save(womenVaccination);
	}

}
