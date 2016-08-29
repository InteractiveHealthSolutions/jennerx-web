package org.ird.unfepi.model.dao.hibernatedimpl;

import org.hibernate.Session;
import org.ird.unfepi.model.dao.DAOCenterProgram;

public class DAOCenterProgramImpl extends DAOHibernateImpl implements DAOCenterProgram {

	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOCenterProgramImpl(Session session) {
		super(session);
		this.session = session;
	}

	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	public void setLAST_QUERY_TOTAL_ROW_COUNT(Number lAST_QUERY_TOTAL_ROW_COUNT) {
		LAST_QUERY_TOTAL_ROW_COUNT = lAST_QUERY_TOTAL_ROW_COUNT;
	}
}
