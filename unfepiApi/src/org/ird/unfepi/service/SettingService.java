/*
 * 
 */
package org.ird.unfepi.service;

import java.util.List;

import org.ird.unfepi.model.Setting;
import org.ird.unfepi.model.VariableSetting;

public interface SettingService {
	
	VariableSetting findVariableSetting(String uid,	boolean isreadonly, int firstResult, int fetchsize);

	List<VariableSetting> findVariableSettingByType(String type, boolean isreadonly, int firstResult, int fetchsize);

	List<VariableSetting> findByCriteria(String name, String type, String element, String value, Float rangeLower, Float rangeUpper
			, boolean isreadonly, int firstResult, int fetchsize);
	
	Setting getIrSetting(String name);

	void updateIrSetting(Setting setting);

	List<Setting> getIrSettings();

	List<Setting> matchSetting(String settingName);
}
