package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.ChildLottery;
import org.ird.unfepi.model.ChildLottery.CodeStatus;
import org.ird.unfepi.model.dao.DAOChildLottery;

import com.sun.java_cup.internal.runtime.virtual_parse_stack;

public class DAOChildLotteryImpl extends DAOHibernateImpl implements DAOChildLottery{

	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOChildLotteryImpl(Session session) {
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
	public ChildLottery findById(int childLotteryId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildLottery.class)
				.add(Restrictions.eq("childLotteryId", childLotteryId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildLottery> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildLottery> findByVaccination(int vaccinationRecordNum,	boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildLottery.class)
				.add(Restrictions.eq("vaccinationRecordNum", vaccinationRecordNum)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildLottery> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildLottery> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildLottery.class).setReadOnly(readonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<ChildLottery> list = cri.addOrder(Order.desc("lotteryDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChildLottery> findByCriteria(String code, Integer childId, Short vaccineId,  
			Boolean hasWonLottery, Date lotteryDateFrom, Date lotteryDateTo, Date transactionDateFrom, Date transactionDateTo, 
			Date consumptionDateFrom, Date consumptionDateTo, CodeStatus codeStatus, Integer storekeeperId, Integer amountFrom, Integer amountTo, 
			Integer areaLocationId, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(ChildLottery.class).setReadOnly(readonly);
		cri.createAlias("vaccination", "vac");

		if (code != null) {
			cri.add(Restrictions.eq("code", code));
		}
		
		if(childId != null || vaccineId != null){
			if(childId != null){
				cri.add(Restrictions.eq("vac.childId", childId));
			}
			
			if(vaccineId !=  null){
				cri.add(Restrictions.eq("vac.vaccineId", vaccineId));
			}
		}
		
		if(lotteryDateFrom != null && lotteryDateTo != null){
			cri.add(Restrictions.between("lotteryDate", lotteryDateFrom, lotteryDateTo));
		}
		
		if(transactionDateFrom != null && transactionDateTo != null){
			cri.add(Restrictions.between("transactionDate", transactionDateFrom, transactionDateTo));
		}
		
		if(consumptionDateFrom != null && consumptionDateTo != null){
			cri.add(Restrictions.between("consumptionDate", consumptionDateFrom, consumptionDateTo));
		}
		
		if(hasWonLottery != null){
			cri.add(Restrictions.eq("hasWonLottery", hasWonLottery));
		}
		
		if(codeStatus != null){
			cri.add(Restrictions.eq("codeStatus", codeStatus));
		}
		
		if(amountFrom != null && amountTo != null){
			cri.add(Restrictions.between("amount", amountFrom, amountTo));
		}
		
		if(storekeeperId != null){
			cri.createAlias("storekeeper", "stk").add(Restrictions.eq("stk.mappedId", storekeeperId));
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
		
		List<ChildLottery> list = cri.addOrder(Order.desc("lotteryDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
