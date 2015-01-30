/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.CsvUpload;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAOCsvUpload.
 */
public interface DAOCsvUpload extends DAO{

	/**
	 * Gets the csv file.
	 *
	 * @param recordNumber the record number
	 * @return the csv file
	 */
	List getCsvFile(long recordNumber);

	/**
	 * Gets the upload report.
	 *
	 * @param recordNumber the record number
	 * @return the upload report
	 */
	List getUploadReport(long recordNumber);

	/**
	 * Count all rows.
	 *
	 * @return the number
	 */
	Number countAllRows();

	/**
	 * Gets the all with projection.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @return the all with projection
	 */
	List<CsvUpload> getAllWithProjection(int firstResult, int fetchsize);

	/**
	 * LAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @return the number
	 */
	Number LAST_QUERY_TOTAL_ROW__COUNT();
}
