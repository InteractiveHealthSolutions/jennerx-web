/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Setting;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOSetting.
 */
public interface DAOSetting extends DAO{
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the setting
	 */
	Setting findById(int id);
	
	/**
	 * Match by criteria.
	 *
	 * @param settingName the setting name
	 * @return the list
	 */
	List<Setting> matchByCriteria(String settingName);
	
	/**
	 * Gets the all.
	 *
	 * @return the all
	 */
	List<Setting> getAll();
	
	/**
	 * Gets the setting.
	 *
	 * @param settingName the setting name
	 * @return the setting
	 */
	Setting getSetting(String settingName);
}
