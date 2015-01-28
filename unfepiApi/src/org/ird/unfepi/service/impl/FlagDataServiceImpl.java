package org.ird.unfepi.service.impl;
/*package org.ird.immunizationreminder.service;

import java.util.List;

import ird.xoutTB.dao.DAOFlagData;
import ird.xoutTB.db.entity.FlagData;
import ird.xoutTB.reporting.exceptions.FlagDataException;
import ird.xoutTB.service.FlagDataService;

public class FlagDataServiceImpl implements FlagDataService{

	private DAOFlagData dao;
	public FlagDataServiceImpl(DAOFlagData dao) {
		this.dao=dao;
	}
	@Override
	public void deleteFlagRule(FlagData flag) {
		dao.deleteFlagRule(flag);
	}

	@Override
	public void flagChildren(FlagData data) throws FlagDataException {
		List<FlagData> l=dao.getFlagData(data.getFlagRecordName());
		if(l.size()>0){
			throw new FlagDataException(FlagDataException.FLAG_RULE_ALREADY_EXISTS,FlagDataException.FLAG_RULE_ALREADY_EXISTS);
		}
		dao.flagChildren(data);
	}
	@Override
	public List<FlagData> getAllFlagData() {
		return dao.getAllFlagData();
	}
	@Override
	public List<FlagData> getFlagData(String ruleName) {
		return dao.getFlagData(ruleName);
	}
	@Override
	public void updateFlagData(FlagData flag) {
		dao.updateFlagData(flag);
	}

}
*/