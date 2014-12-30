/*
 * 
 
package org.ird.unfepi.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.CsvUpload;
import org.ird.unfepi.model.dao.DAOCsvUpload;
import org.ird.unfepi.service.TransactionLogService;

// TODO: Auto-generated Javadoc
*//**
 * The Class TransactionLogServiceImpl.
 *//*
public class TransactionLogServiceImpl implements TransactionLogService{
	private ServiceContext sc;
	*//** The daocsv. *//*
	private DAOCsvUpload daocsv;
	
	*//** The LAS t_ quer y_ tota l_ ro w__ count. *//*
	private Number LAST_QUERY_TOTAL_ROW__COUNT;
	
	*//**
	 * Instantiates a new transaction log service impl.
	 *
	 * @param daocsv the daocsv
	 *//*
	public TransactionLogServiceImpl(ServiceContext sc, DAOCsvUpload daocsv){
		this.sc = sc;
		this.daocsv=daocsv;
	}
	
	*//**
	 * Sets the lAST s_ row s_ returne d_ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW__COUNT the new lAST s_ row s_ returne d_ count
	 *//*
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	 (non-Javadoc)
	 * @see org.ird.unfepi.service.TransactionLogService#LAST_QUERY_TOTAL_ROW__COUNT()
	 
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}

	 (non-Javadoc)
	 * @see org.ird.unfepi.service.TransactionLogService#getAllWithProjection(int, int)
	 
	@Override
	public List<CsvUpload> getAllWithProjection(int firstResult, int fetchsize) {
		List<CsvUpload> csvl = daocsv.getAllWithProjection(firstResult, fetchsize);
		setLASTS_ROWS_RETURNED_COUNT(daocsv.LAST_QUERY_TOTAL_ROW__COUNT());
		return csvl;
	}

	 (non-Javadoc)
	 * @see org.ird.unfepi.service.TransactionLogService#getCsvFile(long)
	 
	@Override
	public String getCsvFile(long recordNumber) throws SQLException, Exception {
		List csfile = daocsv.getCsvFile(recordNumber);
		if(csfile.size()==0){
			return null;
		}
		return (String) csfile.get(0);
		//return Utils.convertStreamToStringBuilder(((Clob)csfile.get(0)).getAsciiStream());
	}

	 (non-Javadoc)
	 * @see org.ird.unfepi.service.TransactionLogService#getUploadReport(long)
	 
	@Override
	public String getUploadReport(long recordNumber) throws SQLException, Exception {
		List report = daocsv.getUploadReport(recordNumber);
		if(report.size()==0){
			return null;
		}
		return (String) report.get(0);
		//return Utils.convertStreamToStringBuilder(((Clob)report.get(0)).getAsciiStream());
	}
	
	 (non-Javadoc)
	 * @see org.ird.unfepi.service.TransactionLogService#saveCsvUpload(org.ird.unfepi.model.CsvUpload)
	 
	@Override
	public void saveCsvUpload(CsvUpload csvUpload){
		daocsv.save(csvUpload);
	}
}
*/