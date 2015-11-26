package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.ird.unfepi.model.Arm;
import org.ird.unfepi.model.dao.DAOArm;
import org.ird.unfepi.utils.OrderBySqlFormula;


public class DAOArmImpl extends DAOHibernateImpl implements DAOArm  {
	
	private Session session;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	public DAOArmImpl(Session session) {
		super(session);
		this.session = session;
	}

	public void setLAST_QUERY_TOTAL_ROW_COUNT(Number lAST_QUERY_TOTAL_ROW_COUNT) {
		LAST_QUERY_TOTAL_ROW_COUNT = lAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public Arm findById(int id) {
		Arm arm= (Arm) session.get(Arm.class,id);
		setLAST_QUERY_TOTAL_ROW_COUNT(arm==null?0:1);
		return arm;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Arm getByName(String armName) {
		List<Arm> v= session.createQuery("from Arm where name ='"+armName+"' order by arm_name").list();
		setLAST_QUERY_TOTAL_ROW_COUNT(v.size());
		if(v.size()>0){
			return v.get(0);
		}
		return null;
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Arm> getAll(boolean readonly,String[] mappingsToJoin, String orderBySqlFormula) {
		Criteria cri = session.createCriteria(Arm.class).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Arm> list = cri.addOrder(OrderBySqlFormula.sqlFormula(orderBySqlFormula)).list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Arm> getAll() {
		Criteria cri = session.createCriteria(Arm.class);
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Arm> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Arm> getAllArm(int firstResult, int fetchsize,
			boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Arm.class)
				.setReadOnly(readonly)
				.addOrder(Order.desc("createdDate"));
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Arm> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

}
