package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.VaccinatorIncentive;
import org.ird.unfepi.model.dao.DAOVaccinatorIncentive;


public class DAOVaccinatorIncentiveImpl extends DAOHibernateImpl implements DAOVaccinatorIncentive  {
	
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOVaccinatorIncentiveImpl(Session session) {
		super(session);
		this.session = session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}


	@Override
	public List<VaccinatorIncentive> findByVaccination(
			int vaccinationRecordNum, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("vaccinationRecordNum", vaccinationRecordNum)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentive> getAll(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class).setReadOnly(readonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentive> list = cri.addOrder(Order.desc("incentiveDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@Override
	public List<VaccinatorIncentive> findByCriteriaVaccinatorIncentivized(
			Integer vaccinatorId, Boolean isIncentivized, boolean readonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinatorIncentive.class).setReadOnly(readonly);


		if (vaccinatorId != null) {
		cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
		}
		
		if (isIncentivized != null) {
			cri.add(Restrictions.eq("isIncentivized", isIncentivized));
			}
			
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinatorIncentive> findByCriteria(Integer armId,
			Integer vaccinator, Short vaccineId, Boolean isIncentivized,
			Date incentiveDateFrom, Date incentiveDateTo, Integer amountFrom,
			Integer amountTo, Integer areaLocationId, int firstResult,
			int fetchsize, boolean readonly, String[] mappingsToJoin) {
		
		Criteria cri = session.createCriteria(ChildIncentive.class).setReadOnly(readonly);
		cri.createAlias("vaccinator", "vac");

		
		if(armId != null){
			cri.add(Restrictions.eq("armId", armId));
		}
		
		
		if(vaccinator != null || vaccineId != null){
			if(vaccinator != null){
				cri.add(Restrictions.eq("vac.vaccinatorId", vaccinator));
			}
			
			if(vaccineId !=  null){
				cri.add(Restrictions.eq("vac.vaccineId", vaccineId));
			}
		}
		
		if(incentiveDateFrom != null && incentiveDateTo != null){
			cri.add(Restrictions.between("incentiveDate", incentiveDateFrom, incentiveDateTo));
		}
		
		if(isIncentivized != null){
			cri.add(Restrictions.eq("isIncentivized", isIncentivized));
		}
		
		
		if(amountFrom != null && amountTo != null){
			cri.add(Restrictions.between("amount", amountFrom, amountTo));
		}
		
		
		if(areaLocationId != null){
			cri.add(Restrictions.sqlRestriction(
					" EXISTS (SELECT locationId FROM identifier i WHERE i.mappedId = vac1_.vaccinationCenterId "
					+ "			AND (locationId = "+areaLocationId+" OR GetAncestry(locationId) REGEXP CONCAT('[[:<:]]',"+areaLocationId+",'[[:>:]]'))) "));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentive> list = cri.addOrder(Order.desc("incentiveDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@Override
	public List<VaccinatorIncentive> findByArm(int armId, boolean readonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("armId", armId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinatorIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@Override
	public List<VaccinatorIncentive> findByCriteriaVaccinatorRecordNum(
			Integer vaccinatorRecordNum, boolean readonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("vaccinationRecordNum", vaccinatorRecordNum)).setReadOnly(readonly);
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		List<VaccinatorIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

}
