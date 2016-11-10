/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.WomenVaccination;
import org.ird.unfepi.model.WomenVaccination.TimelinessStatus;
import org.ird.unfepi.model.WomenVaccination.WOMEN_VACCINATION_STATUS;
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
	public WomenVaccination findById(int id, boolean isreadonly,
			String[] mappingsToJoin, String[] sqlFilter) {
		Criteria cri = session.createCriteria(WomenVaccination.class)
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

		List<WomenVaccination> pv = cri.list();

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

	@Override
	public List<WomenVaccination> findByWomenId(int womenId) {
		session.clear();
		Criteria criteria = session.createCriteria(WomenVaccination.class).add(Restrictions.eq("womenId", womenId));
		List<WomenVaccination> list = criteria.list();
		
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WomenVaccination> findByCriteria(Integer womenId,String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId, String epiNumber, Date dueDatesmaller,
			Date dueDategreater, Date vaccinationDatesmaller, Date vaccinationDategreater, TimelinessStatus timelinessStatus,
			WOMEN_VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin,
			String[] sqlFilter) {
		Criteria cri=session.createCriteria(WomenVaccination.class);
		if(womenId!=null){
			cri.add(Restrictions.eq("womenId", womenId));
		}
		if(vaccineName!=null){
			cri.createAlias("vaccine", "v").add(Restrictions.eq("v.name",vaccineName));
		}
		if(epiNumber != null){
			cri.add(Restrictions.eq("epiNumber", epiNumber));
		}
		if(dueDatesmaller!=null && dueDategreater!=null){
			cri.add(Restrictions.between("vaccinationDuedate", dueDatesmaller, dueDategreater));
		}
		if(vaccinationDatesmaller!=null && vaccinationDategreater !=null){
			cri.add(Restrictions.between("vaccinationDate", vaccinationDatesmaller, vaccinationDategreater));
		}
		if(vaccinationCenterId != null){
			cri.add(Restrictions.eq("vaccinationCenterId", vaccinationCenterId));
		}
		if(vaccinatorId != null){
			cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
		}
		
		if (timelinessStatus != null) {
			cri.add(Restrictions.eq("timelinessStatus", timelinessStatus));
		}
		if(vaccinationStatus!=null){
			if(putNotWithVaccinationStatus){
				cri.add(Restrictions.ne("vaccinationStatus", vaccinationStatus));
			}
			else{
				cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
			}
		}
		
		if(sqlFilter != null)
			for (String sqlf : sqlFilter) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(sqlf)){
					cri.add(Restrictions.sqlRestriction(sqlf));
				}
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<WomenVaccination> list = cri.addOrder(Order.desc("vaccinationDuedate")).addOrder(Order.desc("vaccinationDate")).setReadOnly(isreadonly)
				.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<WomenVaccination> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		Criteria cri = session.createCriteria(WomenVaccination.class)
							.setReadOnly(isreadonly)
							.addOrder(Order.desc("vaccinationDuedate"))
							.addOrder(Order.desc("vaccinationDate"));
		
		if(sqlFilter != null)
			for (String sqlf : sqlFilter) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(sqlf)){
					cri.add(Restrictions.sqlRestriction(sqlf));
				}
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<WomenVaccination> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	

}
