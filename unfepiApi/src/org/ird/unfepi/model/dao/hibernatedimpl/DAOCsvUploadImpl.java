/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.ird.unfepi.model.CsvUpload;
import org.ird.unfepi.model.dao.DAOCsvUpload;

// TODO: Auto-generated Javadoc
/**
 * The Class DAOCsvUploadImpl.
 */
public class DAOCsvUploadImpl extends DAOHibernateImpl implements DAOCsvUpload{

	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW__COUNT;

	/**
	 * Instantiates a new dAO csv upload impl.
	 *
	 * @param session the session
	 */
	public DAOCsvUploadImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/**
	 * Sets the lAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW__COUNT the new lAS t_ quer y_ tota l_ ro w__ count
	 */
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCsvUpload#LAST_QUERY_TOTAL_ROW__COUNT()
	 */
	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	/**
	 * Gets the all with projection.
	 *
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @return the all with projection
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CsvUpload> getAllWithProjection(int firstResult,int fetchsize){
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return session.createQuery("select recordNumber as recordNumber, description as description,numberOfRows as numberOfRows,numberOfRowsRejected as numberOfRowsRejected,numberOfRowsSaved as numberOfRowsSaved,uploadErrors as uploadErrors,uploadedByUserId as uploadedByUserId,uploadedByUserName as uploadedByUserName,dateUploaded as dateUploaded from CsvUpload order by dateUploaded desc").setFirstResult(firstResult).setMaxResults(fetchsize).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCsvUpload#countAllRows()
	 */
	@Override
	public Number countAllRows(){
		return (Number) session.createQuery("select count(*) from CsvUpload").uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCsvUpload#getUploadReport(long)
	 */
	@Override
	public List getUploadReport(long record_number){
		Query q=session.createQuery("select uploadReport from CsvUpload where recordNumber=?").setLong(0, record_number);
		return q.list();
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOCsvUpload#getCsvFile(long)
	 */
	@Override
	public List getCsvFile(long record_number){
		Query q=session.createQuery("select csvFile from CsvUpload where recordNumber=?").setLong(0, record_number);
		return q.list();
	}
}
