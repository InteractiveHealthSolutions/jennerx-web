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
import org.ird.unfepi.model.VaccinationCenter;
import org.ird.unfepi.model.VaccinationCenter.CenterType;
import org.ird.unfepi.model.dao.DAOVaccinationCenter;

/**
 * The Class DAOVaccinationCenterImpl.
 */
public class DAOVaccinationCenterImpl extends DAOHibernateImpl implements DAOVaccinationCenter{

	/** The session. */
	private Session session;

	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO vaccination center impl.
	 *
	 * @param session the session
	 */
	public DAOVaccinationCenterImpl(Session session) {
		super(session);
		this.session=session;
	}
	
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
	 * @see org.ird.unfepi.model.dao.DAOVaccinationCenter#findById(short)
	 */
	@Override
	public VaccinationCenter findById(int Id) {
		return (VaccinationCenter) session.get(VaccinationCenter.class, Id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public VaccinationCenter findById(int mappedId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class)
				.add(Restrictions.eq("mappedId", mappedId)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinationCenter> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public VaccinationCenter findById(String programId, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class).setReadOnly(readonly)
				.createAlias("idMapper.identifiers", "idm");
		
		if(programId != null){
			cri.add(Restrictions.eq("idm.identifier", programId));
		}

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinationCenter> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOVaccinationCenter#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VaccinationCenter findByName(String vaccinationCenterName, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class)
				.add(Restrictions.eq("name", vaccinationCenterName)).setReadOnly(readonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		List<VaccinationCenter> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenter> getAll(int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class).setReadOnly(readonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<VaccinationCenter> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenter> getAll(boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class).setReadOnly(readonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<VaccinationCenter> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenter> getAllOrdered(boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class).setReadOnly(readonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		cri.addOrder(Order.asc("name"));
		
		List<VaccinationCenter> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VaccinationCenter> findByCriteria(String programIdLike, String nameLike, CenterType centerType, int firstResult, int fetchsize, boolean readonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(VaccinationCenter.class).setReadOnly(readonly)
				.createAlias("idMapper.identifiers", "idm");

		if(programIdLike != null){
			cri.add(Restrictions.like("idm.identifier", programIdLike, MatchMode.START));
		}
		
		if (nameLike != null) {
			Criterion n = Restrictions.like("name", nameLike, MatchMode.ANYWHERE);
			Criterion fn = Restrictions.like("fullName", nameLike, MatchMode.ANYWHERE);
			Criterion sn = Restrictions.like("shortName", nameLike, MatchMode.ANYWHERE);

			cri.add(Restrictions.disjunction().add(fn).add(n).add(sn));
		}
		
		if(centerType != null){
			cri.add(Restrictions.eq("centerType", centerType));
		}
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<VaccinationCenter> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
