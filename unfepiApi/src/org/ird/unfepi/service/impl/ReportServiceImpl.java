package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;
import org.ird.unfepi.model.dao.DAODailySummary;
import org.ird.unfepi.model.dao.DAODailySummaryVaccineGiven;
import org.ird.unfepi.model.dao.DAODownloadableReport;
import org.ird.unfepi.model.dao.DAONotifier;
import org.ird.unfepi.service.ReportService;

public class ReportServiceImpl implements ReportService{

	DAONotifier daonot;
	DAODailySummary daodsum;
	DAODailySummaryVaccineGiven daodsumvaccgvn;
	DAODownloadableReport daodownl;
	
	public ReportServiceImpl(DAONotifier daonot, DAODailySummary daodsum, DAODailySummaryVaccineGiven daodsumvaccgvn, DAODownloadableReport daodownl) {
		this.daonot = daonot;
		this.daodsum = daodsum;
		this.daodsumvaccgvn = daodsumvaccgvn;
		this.daodownl = daodownl;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == Notifier.class){
			return daonot.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == DailySummary.class){
			return daodsum.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == DailySummaryVaccineGiven.class){
			return daodsumvaccgvn.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		else if(clazz == DownloadableReport.class){
			return daodownl.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		
		return null;
	}
	
	@Override
	public Notifier getById(int notifierId, boolean readonly, String[] mappingsToJoin) {
		Notifier obj = daonot.getById(notifierId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<Notifier> findByCritria(String notifierName,
			NotifierType notifierType, NotifierStatus notifierStatus,
			boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		List<Notifier> list = daonot.findByCritria(notifierName, notifierType, notifierStatus, readonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}

	@Override
	public DailySummary findDailySummaryBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin) 
	{
		DailySummary obj = daodsum.findBySerialId(serialNumber, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<DailySummary> findDailySummaryByCriteria(
			Integer vaccinationCenterId, Integer vaccinatorId, Date datelower,
			Date dateUpper, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		List<DailySummary> list = daodsum.findByCriteria(vaccinationCenterId, vaccinatorId, datelower, dateUpper, readonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveDailySummary(DailySummary dailySummary) {
		return daodsum.save(dailySummary);
	}

	@Override
	public DailySummary mergeUpdateDailySummary(DailySummary dailySummary) {
		return (DailySummary) daodsum.merge(dailySummary);
	}

	@Override
	public void updateDailySummary(DailySummary dailySummary) {
		daodsum.update(dailySummary);
	}

	@Override
	public Serializable saveNotifier(Notifier notifier) {
		return daonot.save(notifier);
	}

	@Override
	public Notifier mergeUpdateNotifier(Notifier notifier) {
		return (Notifier) daonot.merge(notifier);
	}

	@Override
	public void updateNotifier(Notifier notifier) {
		daonot.update(notifier);
	}

	@Override
	public DownloadableReport getDownloadableReportById(int downloadableId,	boolean readonly, String[] mappingsToJoin) {
		DownloadableReport obj = daodownl.getById(downloadableId, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<DownloadableReport> findDownloadableReportByCritria(String downlaodableReportNameLike,
			String downlaodableReportPathLike, String downloadableReportType,
			Integer sizeBytesLower, Integer sizeBytesUpper,
			Date createdDateLower, Date createdDateUpper, boolean readonly,
			int firstResult, int fetchsize, String[] mappingsToJoin) 
	{
		List<DownloadableReport> list = daodownl.findByCritria(downlaodableReportNameLike, downlaodableReportPathLike, downloadableReportType, sizeBytesLower, sizeBytesUpper, createdDateLower, createdDateUpper, readonly, firstResult, fetchsize, mappingsToJoin);
		return list;
	}

	@Override
	public Serializable saveDownloadableReport(DownloadableReport downloadableReport) {
		return daodownl.save(downloadableReport);
	}

	@Override
	public DownloadableReport mergeUpdateDownloadableReport(DownloadableReport downloadableReport) {
		return (DownloadableReport) daodownl.merge(downloadableReport);
	}

	@Override
	public void updateDownloadableReport(DownloadableReport downloadableReport) {
		daodownl.update(downloadableReport);
	}

	@Override
	public DailySummaryVaccineGiven findDailySummaryVaccineGivenBySerialId(	int serialNumber, boolean readonly, String[] mappingsToJoin) {
		DailySummaryVaccineGiven obj = daodsumvaccgvn.findBySerialId(serialNumber, readonly, mappingsToJoin);
		return obj;
	}

	@Override
	public List<DailySummaryVaccineGiven> findDailySummaryVaccineGivenByCriteria(Integer vaccineId, String vaccineName, Integer dailySummaryId,
			Boolean vaccineExists, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin) {
		List<DailySummaryVaccineGiven> objl = daodsumvaccgvn.findByCriteria(vaccineId, vaccineName, dailySummaryId, vaccineExists, readonly, firstResult, fetchsize, mappingsToJoin);
		return objl;
	}

	@Override
	public Serializable saveDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven) {
		return daodsumvaccgvn.save(dailySummaryVaccineGiven);
	}

	@Override
	public DailySummaryVaccineGiven mergeUpdateDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven) {
		return (DailySummaryVaccineGiven) daodsumvaccgvn.merge(dailySummaryVaccineGiven);
	}

	@Override
	public void updateDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven) {
		daodsumvaccgvn.update(dailySummaryVaccineGiven);
	}
}
