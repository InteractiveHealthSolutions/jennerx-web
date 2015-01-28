/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.model.dao.DAOVaccine;
import org.ird.unfepi.utils.OrderBySqlFormula;

/**
 * The Class DAOVaccineImpl.
 */
public class DAOVaccineImpl extends DAOHibernateImpl implements DAOVaccine{
	
	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO vaccine impl.
	 *
	 * @param session the session
	 */
	public DAOVaccineImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#findById(short)
	 */
	@Override
	public Vaccine findById(short id) {
		Vaccine vacc= (Vaccine) session.get(Vaccine.class,id);
		setLAST_QUERY_TOTAL_ROW_COUNT(vacc==null?0:1);
		return vacc;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#getByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Vaccine getByName(String vaccineName) {
		List<Vaccine> v=session.createQuery("from Vaccine where name ='"+vaccineName+"' order by name").list();
		setLAST_QUERY_TOTAL_ROW_COUNT(v.size());
		if(v.size()>0){
			return v.get(0);
		}
		return null;
	}

	/**
	 * Sets the lAS t_ quer y_ tota l_ ro w__ count.
	 *
	 * @param LAST_QUERY_TOTAL_ROW_COUNT the new lAS t_ quer y_ tota l_ ro w__ count
	 */
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#LAST_QUERY_TOTAL_ROW_COUNT()
	 */
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#getAll(int, int)
	 */
	/*@SuppressWarnings("unchecked")
	@Override
	public List<Vaccine> getAll(int firstResult, int fetchsize, boolean readonly) {
		setLAST_QUERY_TOTAL_ROW_COUNT(countAllRows());
		return session.createQuery("from Vaccine order by name").setFirstResult(firstResult).setMaxResults(fetchsize).setReadOnly(readonly).list();
	}*/
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#countAllRows()
	 */
	/*public Number countAllRows() {
		return (Number) session.createQuery("select count(*) from Vaccine").uniqueResult();
	}*/
	
	/**
	 * vaccineName should not be null.
	 *
	 * @param vaccineName the vaccine name
	 * @param firstResult the first result
	 * @param fetchsize the fetchsize
	 * @return the list
	 * @see org.ird.unfepi.model.dao.DAOVaccine#findVaccine(java.lang.String, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccine> findVaccine(String vaccineName, boolean readonly/*, int firstResult, int fetchsize*/) {
		List<Vaccine> list=session.createQuery("from Vaccine where name like '"+vaccineName+"%' order by name")
				/*.setFirstResult(firstResult).setMaxResults(fetchsize)*/.setReadOnly(readonly).list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccine> getAll(boolean readonly, String[] mappingsToJoin, String orderBySqlFormula) {
		Criteria cri = session.createCriteria(Vaccine.class).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Vaccine> list = cri.addOrder(OrderBySqlFormula.sqlFormula(orderBySqlFormula)).list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccine#countVaccineRows(java.lang.String)
	 */
/*	@Override
	public Number countVaccineRows(String vaccineName) {
		Query q=session.createQuery("select count(*) from Vaccine where name like '"+vaccineName+"%'");
		return (Number) q.uniqueResult();
	}*/
}
