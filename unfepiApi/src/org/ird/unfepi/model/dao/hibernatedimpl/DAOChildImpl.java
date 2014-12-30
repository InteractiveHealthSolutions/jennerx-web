/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.Child.STATUS;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.dao.DAOChild;

/**
 * The Class DAOChildImpl.
 */
public class DAOChildImpl extends DAOHibernateImpl implements DAOChild{
	
	/** The session. */
	private Session session;
	
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO child impl.
	 *
	 * @param session the session
	 */
	public DAOChildImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	@Override
	public Child findByIdNoJoins(int mappedId) {
		Child p=(Child) session.get(Child.class,mappedId);
		setLAST_QUERY_TOTAL_ROW_COUNT(p == null ? 0 : 1);
		return p;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Child findByIdNoJoins(String programId, boolean isreadonly) {
		Criteria cri = session.createCriteria(Child.class).setReadOnly(isreadonly)
				.createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
		
		List<Child> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public	Child findById(int mappedId, boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(Child.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("mappedId", mappedId));
			
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Child> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Child findById(String programId, boolean isreadonly, String[] mappingsToJoin){
		Criteria cri = session.createCriteria(Child.class).setReadOnly(isreadonly)
				.createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programId));
			
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Child> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
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
	public List<Child> getAll(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin)
	{
		Criteria cri = session.createCriteria(Child.class).setReadOnly(isreadonly);

		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Child> list = cri.addOrder(Order.desc("dateEnrolled")).addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Child> findByCriteria(String programIdLike, String partOfName, Date birthdatelower, Date birthdateUpper
			, String nic, Gender gender, String ethnicity, String religion, String language, STATUS status, boolean putNotWithStatus
			, Date dateEnrolledlower, Date dateEnrolleduppr, String creatorId, boolean isreadonly, int firstResult, int fetchsize
			, String[] mappingsToJoin)
	{
		Criteria cri= session.createCriteria(Child.class).setReadOnly(isreadonly);
		if(programIdLike != null){
			cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.like("idm.identifier", programIdLike, MatchMode.START));
		}
		if (partOfName != null) {
			Criterion fn = Restrictions.like("firstName", partOfName, MatchMode.START);
			Criterion ln = Restrictions.like("lastName", partOfName, MatchMode.START);
			Criterion mn = Restrictions.like("middleName", partOfName, MatchMode.START);

			cri.add(Restrictions.disjunction().add(fn).add(mn).add(ln));
		}
		if(birthdatelower!=null && birthdateUpper!=null){
			cri.add(Restrictions.between("birthdate", birthdatelower, birthdateUpper));
		}
		if(dateEnrolledlower!=null && dateEnrolleduppr!=null){
			cri.add(Restrictions.between("dateEnrolled", dateEnrolledlower, dateEnrolleduppr));
		}
		if(nic != null){
			cri.add(Restrictions.eq("nic", nic));
		}
		if(gender != null){
			cri.add(Restrictions.eq("gender", gender));
		}
		if(ethnicity != null){
			cri.createAlias("ethnicity", "e", JoinType.LEFT_OUTER_JOIN);
			Criterion eth = Restrictions.eq("e.ethnicityName", ethnicity);
			Criterion oeth = Restrictions.eq("otherEthnicity", ethnicity);
			cri.add(Restrictions.disjunction().add(eth).add(oeth));
		}
		if(language != null){
			cri.createAlias("language", "ln", JoinType.LEFT_OUTER_JOIN);
			Criterion lang = Restrictions.eq("ln.language", language);
			Criterion olang = Restrictions.eq("otherLanguage", language);
			cri.add(Restrictions.disjunction().add(lang).add(olang));
		}
		if(religion != null){
			cri.createAlias("religion", "r", JoinType.LEFT_OUTER_JOIN);
			Criterion rel = Restrictions.eq("r.religionName", religion);
			Criterion orel = Restrictions.eq("otherReligion", religion);
			cri.add(Restrictions.disjunction().add(rel).add(orel));
		}
		if(status!=null){
			if(putNotWithStatus){
				cri.add(Restrictions.not(Restrictions.eq("status", status)));
			}else{
				cri.add(Restrictions.eq("status", status));
			}
		}
		
		if(creatorId != null){
			cri.createAlias("createdByUserId.idMapper.identifiers", "creator", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("creator.identifier", creatorId));
		}
	
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Child> list = cri.addOrder(Order.desc("dateEnrolled")).addOrder(Order.desc("createdDate")).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
