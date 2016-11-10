/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.EncounterResults;

/**
 * The Interface DAOEncounterResults.
 */
public interface DAOEncounterResults extends DAO{
	
	List<EncounterResults> findEncounterResults(long entityId, String encounterType);
	
	List<EncounterResults> findEncounterResultsByElement(long entityId, String encounterType, String elementName);

	List<EncounterResults> findEncounterResultsByElementValue(long entityId, String encounterType, String elementName, String value);
}
