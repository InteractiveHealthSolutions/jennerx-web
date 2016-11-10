package org.ird.unfepi.service;

import org.ird.unfepi.model.VaccineGap;
import org.ird.unfepi.model.VaccinePrerequisite;

public interface CalendarVaccineService {
	
	void deleteVaccineGap(VaccineGap vaccineGap);
	
	void deleteVaccinePrerequisite(VaccinePrerequisite vaccinePrerequisite);
}
