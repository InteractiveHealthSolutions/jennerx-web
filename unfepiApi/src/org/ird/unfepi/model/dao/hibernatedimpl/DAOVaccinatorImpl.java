/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Vaccinator;
import org.ird.unfepi.model.dao.DAOVaccinator;

/**
 * The Class DAOVaccinatorImpl.
 */
public class DAOVaccinatorImpl extends DAOHibernateImpl implements DAOVaccinator{

	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOVaccinatorImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccinator#findById(short)
	 */
	@Override
	public Vaccinator findById(int id) {
		return (Vaccinator) session.get(Vaccinator.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vaccinator findById(int mappedId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Vaccinator.class)
				.add(Restrictions.eq("mappedId", mappedId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Vaccinator> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vaccinator findById(String programId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Vaccinator.class).setReadOnly(readonly);
		
		if(programId != null){
			cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
		}
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		List<Vaccinator> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccinator> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Vaccinator.class)
				.setReadOnly(readonly)
				.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm");
				
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Vaccinator> list = cri.addOrder(Order.asc("idm.identifier")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Vaccinator> findByCriteria(String partOfName, String nic, Gender gender
			, Integer vaccinationCenterId, int firstResult, int fetchsize, boolean readonly
			, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Vaccinator.class).setReadOnly(readonly);
		
		if (partOfName != null) {
			Criterion fn = Restrictions.like("firstName", partOfName, MatchMode.START);
			Criterion mn = Restrictions.like("middleName", partOfName, MatchMode.START);
			Criterion ln = Restrictions.like("lastName", partOfName, MatchMode.START);

			cri.add(Restrictions.disjunction().add(fn).add(mn).add(ln));
		}
		if(nic != null){
			cri.add(Restrictions.eq("nic", nic));
		}
		if(gender != null){
			cri.add(Restrictions.eq("gender", gender));
		}
		if(vaccinationCenterId != null){
			cri.add(Restrictions.eq("vaccinationCenterId", vaccinationCenterId));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Vaccinator> list = cri.addOrder(Order.asc("vaccinationCenterId")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
