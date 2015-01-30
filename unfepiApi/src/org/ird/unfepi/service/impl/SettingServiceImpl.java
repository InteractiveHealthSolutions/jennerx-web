/*
 * 
 */
package org.ird.unfepi.service.impl;

import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Setting;
import org.ird.unfepi.model.VariableSetting;
import org.ird.unfepi.model.dao.DAOSetting;
import org.ird.unfepi.model.dao.DAOVariableSetting;
import org.ird.unfepi.service.SettingService;

public class SettingServiceImpl implements SettingService{
	private ServiceContext sc;

	private DAOSetting dao;
	private DAOVariableSetting daovs;
	
	public SettingServiceImpl(ServiceContext sc, DAOSetting dao, DAOVariableSetting daovs) {
		this.sc = sc;
		this.dao=dao;
		this.daovs = daovs;
	}
	
	@Override
	public Setting getIrSetting(String name) {
		return dao.getSetting(name);
	}

	@Override
	public List<Setting> getIrSettings() {
		return dao.getAll();
	}
	
	@Override
	public List<Setting> matchSetting(String settingName){
		return dao.matchByCriteria(settingName);
	}
	
	@Override
	public void updateIrSetting(Setting setting) {
		dao.update(setting);
	}

	@Override
	public VariableSetting findVariableSetting(String uid, boolean isreadonly, int firstResult, int fetchsize) {
		return daovs.findVariableSetting(uid, isreadonly, firstResult, fetchsize);
	}

	@Override
	public List<VariableSetting> findVariableSettingByType(String type,	boolean isreadonly, int firstResult, int fetchsize) {
		return daovs.findVariableSettingByType(type, isreadonly, firstResult, fetchsize);
	}

	@Override
	public List<VariableSetting> findByCriteria(String name, String type, String element, String value, Float rangeLower, Float rangeUpper,
			boolean isreadonly, int firstResult, int fetchsize) {
		return daovs.findByCriteria(name, type, element, value, rangeLower, rangeUpper, isreadonly, firstResult, fetchsize);
	}
}
