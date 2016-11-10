package org.ird.unfepi.service.impl;

import java.io.Serializable;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.LotterySms;
import org.ird.unfepi.model.Round;
import org.ird.unfepi.model.dao.DAOCenterProgram;
import org.ird.unfepi.model.dao.DAOHealthProgram;
import org.ird.unfepi.model.dao.DAORound;
import org.ird.unfepi.service.HealthProgramService;

public class HealthProgramServiceImpl implements HealthProgramService {

	private ServiceContext sc;
	private DAOHealthProgram hpDao;
	private DAOCenterProgram cpDao;
	private DAORound rdDao;
	
	public HealthProgramServiceImpl(ServiceContext sc, DAOHealthProgram hpDao, DAOCenterProgram cpDao, DAORound rdDao){
		this.sc = sc;
		this.hpDao=hpDao;
		this.cpDao = cpDao;
		this.rdDao = rdDao;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if (clazz == HealthProgram.class) {
			return hpDao.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == CenterProgram.class) {
			return cpDao.LAST_QUERY_TOTAL_ROW_COUNT();
		} else if (clazz == Round.class) {
			return rdDao.LAST_QUERY_TOTAL_ROW_COUNT();
		}

		return null;
	}
	
	@Override
	public Serializable saveHealthProgram(HealthProgram healthProgram) {
		return hpDao.save(healthProgram);
	}

	@Override
	public Serializable saveCenterProgram(CenterProgram centerProgram) {
		return cpDao.save(centerProgram);
	}

	@Override
	public Serializable saveRound(Round round) {
		return rdDao.save(round);
	}

}
