/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.dao.DAOWomenVaccination;

import com.mysql.jdbc.StringUtils;

/**
 * @author Safwan
 *
 */
public class DAOWomenVaccinationImpl extends DAOHibernateImpl implements
		DAOWomenVaccination {

	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOWomenVaccinationImpl(Session session) {
		super(session);
		this.session = session;
	}

	@Override
	public Vaccination findById(int id, boolean isreadonly,
			String[] mappingsToJoin, String[] sqlFilter) {
		Criteria cri = session.createCriteria(Vaccination.class)
				.add(Restrictions.eq("vaccinationRecordNum", id))
				.setReadOnly(isreadonly);

		if (sqlFilter != null)
			for (String sqlf : sqlFilter) {
				if (!StringUtils.isEmptyOrWhitespaceOnly(sqlf)) {
					cri.add(Restrictions.sqlRestriction(sqlf));
				}
			}

		if (mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		List<Vaccination> pv = cri.list();

		setLAST_QUERY_TOTAL_ROW_COUNT(pv.size());
		return pv.size() == 0 ? null : pv.get(0);

	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

}
