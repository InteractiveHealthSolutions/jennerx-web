package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.DownloadableReport;

public interface DAODownloadableReport extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	DownloadableReport getById(int downloadableId, boolean readonly, String[] mappingsToJoin);
	
	List<DownloadableReport> findByCritria(String downlaodableReportNameLike, String downlaodableReportPathLike
			, String downloadableReportType, Integer sizeBytesLower, Integer sizeBytesUpper
			, Date createdDateLower, Date createdDateUpper, boolean readonly
			, int firstResult, int fetchsize, String[] mappingsToJoin);

}
