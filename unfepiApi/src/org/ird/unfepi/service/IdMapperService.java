package org.ird.unfepi.service;

import java.io.Serializable;

import org.ird.unfepi.model.IdMapper;

public interface IdMapperService {
	
	IdMapper findIdMapper(String programId);
	
	IdMapper findIdMapper(int mappedId);
	
	Serializable saveIdMapper(IdMapper idMapper);
	
	void updateIdMapper(IdMapper idMapper);
	
	IdMapper mergeUpdateIdMapper(IdMapper idMapper);
}
