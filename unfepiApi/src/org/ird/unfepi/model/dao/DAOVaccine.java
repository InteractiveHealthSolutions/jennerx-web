/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.utils.OrderBySqlFormula;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOVaccine.
 */
public interface DAOVaccine extends DAO{

	Vaccine findById(short id);

	Vaccine getByName(String vaccineName);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<Vaccine> findVaccine(String vaccineName, boolean readonly/*, int firstResult, int fetchsize*/);

	List<Vaccine> getAll(boolean readonly, String[] mappingsToJoin,	String orderBySqlFormula);

	List<Vaccine> getById(Short[] vaccineIds, boolean readonly, String[] mappingsToJoin, String orderBySqlFormula) ;
}
