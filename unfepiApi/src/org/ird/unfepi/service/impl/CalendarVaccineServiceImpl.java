package org.ird.unfepi.service.impl;

import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.model.dao.DAOCalendarVaccine;
import org.ird.unfepi.service.CalendarVaccineService;

public class CalendarVaccineServiceImpl implements CalendarVaccineService {
	
	private DAOCalendarVaccine daocv;
	
	public CalendarVaccineServiceImpl(DAOCalendarVaccine daocv) {
		this.daocv = daocv;
	}
	
	@Override
	public void deleteVaccineGap(VaccineGap vaccineGap) {
		// TODO Auto-generated method stub
		daocv.delete(vaccineGap);
	}

	@Override
	public void deleteVaccinePrerequisite(VaccinePrerequisite vaccinePrerequisite) {
		// TODO Auto-generated method stub
		daocv.delete(vaccinePrerequisite);
	}

}
