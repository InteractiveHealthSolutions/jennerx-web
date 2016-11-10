package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VaccinationCenterVaccineDay;
import org.ird.unfepi.model.VaccinationCenterVaccineDayId;
import org.ird.unfepi.model.dao.DAOVaccinationCenterVaccineDay;

public class DAOVaccinationCenterVaccineDayImpl extends DAOHibernateImpl implements DAOVaccinationCenterVaccineDay{

	private Session session;

	public DAOVaccinationCenterVaccineDayImpl(Session session) {
		super(session);
		this.session=session;
	}

	@Override
	public VaccinationCenterVaccineDay findById(VaccinationCenterVaccineDayId id) {
		return (VaccinationCenterVaccineDay) session.get(VaccinationCenterVaccineDay.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenterVaccineDay> getAll(boolean readonly){
		return session.createQuery("from VaccinationCenterVaccineDay").setReadOnly(readonly).list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenterVaccineDay> findByCriteria(Integer vaccinationCenterId, Short vaccineId, Short dayNumber, boolean readonly){
		Criteria cri = session.createCriteria(VaccinationCenterVaccineDay.class).setReadOnly(readonly);
		if(vaccinationCenterId != null){
			cri.add(Restrictions.eq("id.vaccinationCenterId", vaccinationCenterId));
		}
		
		if(vaccineId != null){
			cri.add(Restrictions.eq("id.vaccineId", vaccineId));
		}
		
		if(dayNumber != null){
			cri.add(Restrictions.eq("id.dayNumber", dayNumber));
		}
		
		return cri.list();
	}
}
