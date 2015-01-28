/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.VariableSetting;

/**
 * The Interface DAOEncounter.
 */
public interface DAOVariableSetting extends DAO{
	
	VariableSetting findVariableSetting(String uid,	boolean isreadonly, int firstResult, int fetchsize);

	List<VariableSetting> findVariableSettingByType(String type, boolean isreadonly, int firstResult, int fetchsize);

	List<VariableSetting> findByCriteria(String name, String type, String element, String value, Float rangeLower, Float rangeUpper
			, boolean isreadonly, int firstResult, int fetchsize);
}
