package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;

public interface EncounterService {

	List<Encounter> findEncounter(int pid1, int pid2, String encounterType);
	
	Serializable saveEncounter(Encounter encounter);
	
	Serializable saveEncounterResult(EncounterResults encounterresult);

	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	List<Encounter> findEncounterByCriteria(Integer encounterId, String p1Id, String p2Id,
			String encounterType, DataEntrySource dataEntrySource,
			Date dateEncounterEnteredLower, Date dateEncounterEnteredUpper,
			boolean isreadonly, int firstResult, int fetchsize,	String[] mappingsToJoin);
}
