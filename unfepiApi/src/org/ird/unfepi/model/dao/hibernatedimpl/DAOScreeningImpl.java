/*
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
import org.ird.unfepi.model.Screening;
import org.ird.unfepi.model.dao.DAOScreening;

/**
 * The Class DAOScreeningImpl.
 */
public class DAOScreeningImpl extends DAOHibernateImpl implements DAOScreening{

	/** The session. */
	private Session session;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;
	
	/**
	 * Instantiates a new dAO screening impl.
	 *
	 * @param session the session
	 */
	public DAOScreeningImpl(Session session) {
		super(session);
		this.session=session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Screening findById(int screeningid, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Screening.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("screeningId", screeningid));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Screening> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Screening> findByMappedId(int mappedId, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Screening.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		List<Screening> list = cri.addOrder(Order.desc("screeningDate")).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Screening> getAll(boolean isreadonly, int firstResult,
			int fetchsize, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Screening.class).setReadOnly(isreadonly);
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		List<Screening> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).addOrder(Order.desc("screeningDate")).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Screening> findByProgramId(String programId, boolean isreadonly, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Screening.class).setReadOnly(isreadonly)
				.createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		List<Screening> list = cri.addOrder(Order.desc("screeningDate")).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Screening> findByCriteria(Integer vaccinatorId,
			Integer vaccinationCenterId,/* String epiNumber,*/
			Date screeningDatelower, Date screeningDateupper,
			boolean isreadonly, int firstResult, int fetchsize,String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(Screening.class).setReadOnly(isreadonly);		
		/*if(epiNumber != null){
			cri.add(Restrictions.eq("epiId", epiNumber));
		}*/
		if(screeningDatelower != null && screeningDateupper != null){
			cri.add(Restrictions.between("screeningDate", screeningDatelower, screeningDateupper));
		}
		if(vaccinationCenterId != null){
			cri.add(Restrictions.eq("vaccinationCenterId", vaccinationCenterId));
		}
		if(vaccinatorId != null){
			cri.add(Restrictions.eq("vaccinatorId", vaccinatorId));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Screening> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).addOrder(Order.desc("screeningDate")).list();
		return list;
	}
}
