package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.EncounterResults;
import org.ird.unfepi.model.dao.DAOEncounter;
import org.ird.unfepi.model.dao.DAOEncounterResults;
import org.ird.unfepi.service.EncounterService;

public class EncounterServiceImpl implements EncounterService{

	DAOEncounter daoenc;
	DAOEncounterResults daoencres;
	
	public EncounterServiceImpl(DAOEncounter daoenc, DAOEncounterResults daoencres) {
		this.daoenc = daoenc;
		this.daoencres = daoencres;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Encounter.class){
			return daoenc.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == EncounterResults.class){
			throw new UnsupportedOperationException();
		}
		
		return null;
	}
	
	@Override
	public List<Encounter> findEncounter(int pid1, int pid2, String encounterType) {
		return daoenc.findEncounter(pid1, pid2, encounterType);
	}

	@Override
	public Serializable saveEncounter(Encounter encounter) {
		return daoenc.save(encounter);
	}

	@Override
	public Serializable saveEncounterResult(EncounterResults encounterresult) {
		return daoencres.save(encounterresult);
	}
	
	@Override
	public List<Encounter> findEncounterByCriteria(Integer encounterId, String p1Id, String p2Id, String encounterType, DataEntrySource dataEntrySource,
			Date dateEncounterEnteredLower, Date dateEncounterEnteredUpper,	boolean isreadonly, int firstResult, int fetchsize,
			String[] mappingsToJoin){
		List<Encounter> list = daoenc.findByCriteria(encounterId, p1Id, p2Id, encounterType, dataEntrySource, dateEncounterEnteredLower, dateEncounterEnteredUpper, isreadonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}
}
