package org.ird.unfepi.service;

import java.util.List;

import org.ird.unfepi.model.dao.DAO;

public interface CustomQueryService extends DAO{
	
	List getDataByHQL(String hql);
	
	List getDataBySQL(String sql);
	
	List getDataByHQLMapResult(String hql);

	List getDataBySQLMapResult(String sql);
	
}
