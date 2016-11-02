package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.dao.DAO;
import org.ird.unfepi.model.dao.DAODirectQuery;
import org.ird.unfepi.service.CustomQueryService;

public class CustomQueryServiceImpl implements CustomQueryService{

	private ServiceContext sc;
	private DAODirectQuery daoqur;

	private DAO dao;
	
	public CustomQueryServiceImpl(ServiceContext sc, DAODirectQuery daoqur, DAO dao) {
		this.sc = sc;
		this.daoqur = daoqur;
		this.dao = dao;
	}

	@Override
	public List getDataByHQL(String hql) {
		return daoqur.getDataByHQL(hql);
	}

	@Override
	public List getDataBySQL(String sql) {
		return daoqur.getDataBySQL(sql);
	}
	
	@Override
	public List getDataByHQLMapResult(String hql) {
		return daoqur.getDataByHQLMapResult(hql);
	}

	@Override
	public List getDataBySQLMapResult(String sql) {
		return daoqur.getDataBySQLMapResult(sql);
	}

	@Override
	public Serializable save(Object objectinstance) {
		return dao.save(objectinstance);
	}

	@Override
	public void delete(Object objectinstance) {
		throw new UnsupportedOperationException("you can not delete any data");
	}

	@Override
	public Object merge(Object objectinstance) {
		return dao.merge(objectinstance);
	}

	@Override
	public void update(Object objectinstance) {
		dao.update(objectinstance);
	}

	@Override
	public void saveOrUpdate(Object objectinstance) {
		dao.saveOrUpdate(objectinstance);
		
	}
}
