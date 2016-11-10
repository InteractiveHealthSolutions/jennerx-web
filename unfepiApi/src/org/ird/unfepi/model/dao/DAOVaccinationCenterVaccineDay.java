package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;

public interface DAOVaccinationCenterVaccineDay extends DAO{

	VaccinationCenterVaccineDay findById(VaccinationCenterVaccineDayId id);

	List<VaccinationCenterVaccineDay> getAll(boolean readonly);

	List<VaccinationCenterVaccineDay> findByCriteria(Integer vaccinationCenterId, Short vaccineId, Short dayNumber, boolean readonly);
}
