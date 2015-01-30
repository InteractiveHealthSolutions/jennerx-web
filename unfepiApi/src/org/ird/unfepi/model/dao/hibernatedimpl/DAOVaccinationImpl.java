 package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.dao.DAOVaccination;

import com.mysql.jdbc.StringUtils;

public class DAOVaccinationImpl extends DAOHibernateImpl implements DAOVaccination{
	
	private  Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinationImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vaccination findById(int id,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		Criteria cri = session.createCriteria(Vaccination.class)
								.add(Restrictions.eq("vaccinationRecordNum", id))
								.setReadOnly(isreadonly);
		
		if(sqlFilter != null)
			for (String sqlf : sqlFilter) {
				if(!StringUtils.isEmptyOrWhitespaceOnly(sqlf)){
					cri.add(Restrictions.sqlRestriction(sqlf));
				}
			}
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<Vaccination> pv = cri.list();
								
		setLAST_QUERY_TOTAL_ROW_COUNT(pv.size());
		return pv.size()==0?null:pv.get(0);
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) {
		Criteria cri = session.createCriteria(Vaccination.class)
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
		
		List<Vaccination> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> findByCriteria(Integer childId,String vaccineName, Integer vaccinatorId, Integer vaccinationCenterId,
			String epiNumber, Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller ,Date vaccinationDategreater,
			TimelinessStatus timelinessStatus , VACCINATION_STATUS vaccinationStatus, boolean putNotWithVaccinationStatus, int firstResult, int fetchsize
			,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) 
	{
		Criteria cri=session.createCriteria(Vaccination.class);
		if(childId!=null){
			cri.createAlias("child", "p").add(Restrictions.eq("p.mappedId", childId));
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
		
		List<Vaccination> list = cri.addOrder(Order.desc("vaccinationDuedate"))
				.addOrder(Order.desc("vaccinationDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> findByCriteriaIncludeName(String partOfName,String vaccineName,String epiNumber,
			Date dueDatesmaller ,Date dueDategreater,Date vaccinationDatesmaller
			,Date vaccinationDategreater, TimelinessStatus timelinessStatus ,VACCINATION_STATUS vaccinationStatus,String armName
			, int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin, String[] sqlFilter) 
	{
		Criteria cri=session.createCriteria(Vaccination.class);
		
		cri.createAlias("child", "p");
		if(partOfName!=null){
			cri.add(
					Restrictions.or(Restrictions.like("p.firstName", partOfName,MatchMode.START)
									,Restrictions.like("p.lastName", partOfName,MatchMode.START)));
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
		if (timelinessStatus != null) {
			cri.add(Restrictions.eq("timelinessStatus", timelinessStatus));
		}
		if(vaccinationStatus!=null){
			cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
		}
		if(armName!=null){
			cri.createAlias("p.arm", "parm").add(Restrictions.like("parm.armName", armName,MatchMode.EXACT));
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
		
		List<Vaccination> list = cri.addOrder(Order.desc("vaccinationDuedate"))
				.addOrder(Order.desc("vaccinationDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccination> findByCriteria (int childId, Short vaccineId, VACCINATION_STATUS vaccinationStatus,
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri=session.createCriteria(Vaccination.class).add(Restrictions.eq("childId", childId));
		
		if(vaccineId!=null){
			cri.add(Restrictions.eq("vaccineId",vaccineId));
		}
		
		if(vaccinationStatus!=null){
			cri.add(Restrictions.eq("vaccinationStatus", vaccinationStatus));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		
		List<Vaccination> list = cri.addOrder(Order.desc("vaccinationDuedate"))
				.addOrder(Order.desc("vaccinationDate"))
				.setReadOnly(isreadonly)
				.setFirstResult(firstResult)
				.setMaxResults(fetchsize)
				.list();
		return list;
	}
}
