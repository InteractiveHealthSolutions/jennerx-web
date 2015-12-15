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
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.dao.DAOChildIncentive;

import com.sun.java_cup.internal.runtime.virtual_parse_stack;

public class DAOChildIncentiveImpl extends DAOHibernateImpl implements DAOChildIncentive{

	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOChildIncentiveImpl(Session session) {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public ChildIncentive findById(int childIncentiveId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("childIncentiveId", childIncentiveId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildIncentive> findByVaccination(int vaccinationRecordNum,	boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("vaccinationRecordNum", vaccinationRecordNum)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildIncentive> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class).setReadOnly(readonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildIncentive> list = cri.addOrder(Order.desc("incentiveDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildIncentive> findByCriteria(/*String code,*/ Integer armId, Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, IncentiveStatus incentiveStatus, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*//* Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class).setReadOnly(readonly);
		cri.createAlias("vaccination", "vac");

		/*if (code != null) {
			cri.add(Restrictions.eq("code", code));
		}*/
		
		if(incentiveStatus != null){
			cri.add(Restrictions.eq("incentiveStatus", incentiveStatus));
		}
		if(armId != null) {
			cri.add(Restrictions.eq("armId",armId));
		}
		
		if(childId != null || vaccineId != null){
			if(childId != null){
				cri.add(Restrictions.eq("vac.childId", childId));
			}
			
			if(vaccineId !=  null){
				cri.add(Restrictions.eq("vac.vaccineId", vaccineId));
			}
		}
		
		if(incentiveDateFrom != null && incentiveDateTo != null){
			cri.add(Restrictions.between("incentiveDate", incentiveDateFrom, incentiveDateTo));
		}
		
		if(transactionDateFrom != null && transactionDateTo != null){
			cri.add(Restrictions.between("transactionDate", transactionDateFrom, transactionDateTo));
		}
		
		/*if(consumptionDateFrom != null && consumptionDateTo != null){
			cri.add(Restrictions.between("consumptionDate", consumptionDateFrom, consumptionDateTo));
		}*/
		
		if(hasWonIncentive != null){
			cri.add(Restrictions.eq("hasWonIncentive", hasWonIncentive));
		}
		
		/*if(codeStatus != null){
			cri.add(Restrictions.eq("codeStatus", codeStatus));
		}*/
		
		if(amountFrom != null && amountTo != null){
			cri.add(Restrictions.between("amount", amountFrom.floatValue(), amountTo.floatValue()));
		}
		
		/*if(storekeeperId != null){
			cri.createAlias("storekeeper", "stk").add(Restrictions.eq("stk.mappedId", storekeeperId));
		}*/
		
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
		
		List<ChildIncentive> list = cri.addOrder(Order.desc("incentiveDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ChildIncentive> findByCriteria(/*String code,*/  Integer childId, Short vaccineId,  
			Boolean hasWonIncentive, Date incentiveDateFrom, Date incentiveDateTo, Date transactionDateFrom, Date transactionDateTo, 
			/*Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus,*/ /*Integer storekeeperId,*/ Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class).setReadOnly(readonly);
		cri.createAlias("vaccination", "vac");

		/*if (code != null) {
			cri.add(Restrictions.eq("code", code));
		}*/
		
		
		if(childId != null || vaccineId != null){
			if(childId != null){
				cri.add(Restrictions.eq("vac.childId", childId));
			}
			
			if(vaccineId !=  null){
				cri.add(Restrictions.eq("vac.vaccineId", vaccineId));
			}
		}
		
		if(incentiveDateFrom != null && incentiveDateTo != null){
			cri.add(Restrictions.between("incentiveDate", incentiveDateFrom, incentiveDateTo));
		}
		
		if(transactionDateFrom != null && transactionDateTo != null){
			cri.add(Restrictions.between("transactionDate", transactionDateFrom, transactionDateTo));
		}
		
		/*if(consumptionDateFrom != null && consumptionDateTo != null){
			cri.add(Restrictions.between("consumptionDate", consumptionDateFrom, consumptionDateTo));
		}*/
		
		if(hasWonIncentive != null){
			cri.add(Restrictions.eq("hasWonIncentive", hasWonIncentive));
		}
		
		/*if(codeStatus != null){
			cri.add(Restrictions.eq("codeStatus", codeStatus));
		}*/
		
		if(amountFrom != null && amountTo != null){
			cri.add(Restrictions.between("amount", amountFrom.floatValue(), amountTo.floatValue()));
		}
		
		/*if(storekeeperId != null){
			cri.createAlias("storekeeper", "stk").add(Restrictions.eq("stk.mappedId", storekeeperId));
		}*/
		
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
		
		List<ChildIncentive> list = cri.addOrder(Order.desc("incentiveDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ChildIncentive> findByArm(int armId, boolean readonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildIncentive.class)
				.add(Restrictions.eq("armId", armId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildIncentive> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
}
