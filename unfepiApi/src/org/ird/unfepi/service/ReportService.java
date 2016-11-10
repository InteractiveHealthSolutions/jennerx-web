package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.DailySummaryVaccineGiven;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;

public interface ReportService 
{
	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);

	Notifier getById(int notifierId, boolean readonly, String[] mappingsToJoin);
	
	List<Notifier> findByCritria(String notifierName, NotifierType notifierType, NotifierStatus notifierStatus
			, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	DailySummary findDailySummaryBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin);
	
	List<DailySummary> findDailySummaryByCriteria(Integer vaccinationCenterId, Integer vaccinatorId, Date datelower, Date dateUpper, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	Serializable saveDailySummary(DailySummary dailySummary);

	DailySummary mergeUpdateDailySummary(DailySummary dailySummary);

	void updateDailySummary(DailySummary dailySummary);
	
	DailySummaryVaccineGiven findDailySummaryVaccineGivenBySerialId(int serialNumber, boolean readonly, String[] mappingsToJoin);
	
	List<DailySummaryVaccineGiven> findDailySummaryVaccineGivenByCriteria(Integer vaccineId, String vaccineName, Integer dailySummaryId, Boolean vaccineExists, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	Serializable saveDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven);

	DailySummaryVaccineGiven mergeUpdateDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven);

	void updateDailySummaryVaccineGiven(DailySummaryVaccineGiven dailySummaryVaccineGiven);
	
	Serializable saveNotifier(Notifier notifier);

	Notifier mergeUpdateNotifier(Notifier notifier);

	void updateNotifier(Notifier notifier);
	
	DownloadableReport getDownloadableReportById(int downloadableId, boolean readonly, String[] mappingsToJoin);
	
	List<DownloadableReport> findDownloadableReportByCritria(String downlaodableReportNameLike, String downlaodableReportPathLike
			, String downloadableReportType, Integer sizeBytesLower, Integer sizeBytesUpper
			, Date createdDateLower, Date createdDateUpper, boolean readonly
			, int firstResult, int fetchsize, String[] mappingsToJoin);
	
	Serializable saveDownloadableReport(DownloadableReport downloadableReport);

	DownloadableReport mergeUpdateDownloadableReport(DownloadableReport downloadableReport);

	void updateDownloadableReport(DownloadableReport downloadableReport);
}
