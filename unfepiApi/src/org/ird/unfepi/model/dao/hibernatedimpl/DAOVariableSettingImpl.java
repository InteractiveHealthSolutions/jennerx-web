/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.VariableSetting;
import org.ird.unfepi.model.dao.DAOVariableSetting;

/**
 * The Class DAOEncounterImpl.
 */
public class DAOVariableSettingImpl extends  DAOHibernateImpl implements DAOVariableSetting{

	/** The session. */
	private Session session;
	
	/**
	 * Instantiates a new dAO encounter impl.
	 *
	 * @param session the session
	 */
	public DAOVariableSettingImpl(Session session) {
		super(session);
		this.session=session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VariableSetting findVariableSetting(String uid, boolean isreadonly, int firstResult, int fetchsize) {
		List<VariableSetting> res= session.createCriteria(VariableSetting.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("uid", uid)).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		
		if(res.size() == 0){
			return null;
		}
		return res.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VariableSetting> findVariableSettingByType(String type, boolean isreadonly, int firstResult, int fetchsize) {
		List<VariableSetting> res= session.createCriteria(VariableSetting.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("type", type)).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return res;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<VariableSetting> findByCriteria(String name, String type, String element, String value, Float rangeLower, Float rangeUpper,
			boolean isreadonly, int firstResult, int fetchsize) {
		Criteria cri= session.createCriteria(VariableSetting.class).setReadOnly(isreadonly);
		
		if(name != null){
			cri.add(Restrictions.eq("name", name));
		}
		if(type != null){
			cri.add(Restrictions.eq("type", type));
		}
		if(element != null){
			cri.add(Restrictions.eq("element", element));
		}
		if(value != null){
			cri.add(Restrictions.eq("value", value));
		}
		if(rangeLower != null && rangeUpper != null){
			cri.add(Restrictions.le("rangeLower", rangeLower))
			.add(Restrictions.ge("rangeUpper", rangeUpper));
		}
		
		List<VariableSetting> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
