package org.ird.unfepi.model.dao;

import java.util.List;

public interface DAODirectQuery {

	List getDataByHQL(String hql);

	List getDataBySQL(String sql);
	
	List getDataByHQLMapResult(String hql);

	List getDataBySQLMapResult(String sql);
}
