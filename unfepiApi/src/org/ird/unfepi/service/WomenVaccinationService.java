/**
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;

import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.WomenVaccination;

/**
 * @author Safwan
 *
 */
public interface WomenVaccinationService {
	
	Vaccine findVaccineById(short id);
	
	Serializable save(WomenVaccination womenVaccination);

}
