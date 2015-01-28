/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;

/**
 * The Interface DAOEncounter.
 */
public interface DAOEncounter extends DAO{
	
	List<Encounter> findEncounter(int pid1, int pid2, String encounterType);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<Encounter> findByCriteria(Integer encounterId, String p1Id, String p2Id, String encounterType, DataEntrySource dataEntrySource,
			Date dateEncounterEnteredLower, Date dateEncounterEnteredUpper,	boolean isreadonly, int firstResult, int fetchsize,
			String[] mappingsToJoin);
}
