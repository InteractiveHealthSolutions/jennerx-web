package org.ird.unfepi.service.impl;

import java.io.Serializable;

import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.dao.DAOIdMapper;
import org.ird.unfepi.service.IdMapperService;

public class IdMapperServiceImpl implements IdMapperService{

	DAOIdMapper daoidmapper;

	public IdMapperServiceImpl(DAOIdMapper daoidmapper) {
		this.daoidmapper = daoidmapper;
	}
	
	@Override
	public IdMapper findIdMapper(String programId) {
		return daoidmapper.findIdMapper(programId);
	}

	@Override
	public IdMapper findIdMapper(int mappedId) {
		return daoidmapper.findIdMapper(mappedId);
	}

	@Override
	public Serializable saveIdMapper(IdMapper idMapper) {
		return daoidmapper.save(idMapper);
	}

	@Override
	public void updateIdMapper(IdMapper idMapper) {
		daoidmapper.update(idMapper);
	}

	@Override
	public IdMapper mergeUpdateIdMapper(IdMapper idMapper) {
		return (IdMapper) daoidmapper.merge(idMapper);
	}
}
