package org.ird.unfepi.model.dao;

import org.ird.unfepi.model.IdMapper;

public interface DAOIdMapper extends DAO{

	IdMapper findIdMapper(String programId);
	
	IdMapper findIdMapper(int mappedId);
}
