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
import org.ird.unfepi.model.Encounter;
import org.ird.unfepi.model.Encounter.DataEntrySource;
import org.ird.unfepi.model.dao.DAOEncounter;

/**
 * The Class DAOEncounterImpl.
 */
public class DAOEncounterImpl extends  DAOHibernateImpl implements DAOEncounter{

	/** The session. */
	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO encounter impl.
	 *
	 * @param session the session
	 */
	public DAOEncounterImpl(Session session) {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Encounter> findEncounter(int pid1, int pid2, String encounterType) {
		return session.createQuery("from Encounter where id.p1id = "+pid1+" and id.p2id = "+pid2+" "+(encounterType == null?"":("and EncounterType = '"+encounterType+"' "))).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Encounter> findByCriteria(Integer encounterId, String p1Id, String p2Id, String encounterType, DataEntrySource dataEntrySource
			, Date dateEncounterEnteredLower, Date dateEncounterEnteredUpper, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin)
	{
		Criteria cri= session.createCriteria(Encounter.class).setReadOnly(isreadonly)
				.addOrder(Order.desc("dateEncounterEntered"));
		
		if(encounterId != null){
			cri.add(Restrictions.eq("id.encounterId", encounterId));
		}
		if(p1Id != null){
			cri.createAlias("p1", "p11").createAlias("p11.identifiers", "p1idm").add(Restrictions.eq("p1idm.identifier", p1Id));
		}
		if(p2Id != null){
			cri.createAlias("p2", "p22").createAlias("p22.identifiers", "p2idm").add(Restrictions.eq("p2idm.identifier", p2Id));
		}
		if(encounterType != null){
			cri.add(Restrictions.eq("encounterType", encounterType));
		}
		if(dataEntrySource != null){
			cri.add(Restrictions.eq("dataEntrySource", dataEntrySource));
		}
		if(dateEncounterEnteredLower!=null && dateEncounterEnteredUpper!=null){
			cri.add(Restrictions.between("dateEncounterEntered", dateEncounterEnteredLower, dateEncounterEnteredUpper));
		}
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Encounter> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
