package org.ird.unfepi.model.dao.hibernatedimpl;
/*package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.ird.unfepi.model.ArmIdMap;
import org.ird.unfepi.model.dao.DAOArmIdMap;

public class DAOArmIdMapImpl extends DAOHibernateImpl implements DAOArmIdMap{

	private Session session;

	public DAOArmIdMapImpl(Session session) {
		super(session);
		this.session=session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArmIdMap findByChildIdToMap(int id) {
		List<ArmIdMap> armid= session.createQuery("from ArmIdMap where childIdToMap=? ").setInteger(0, id).list();
		if(armid.size()>0){
			return armid.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArmIdMap> getAll() {
		return session.createQuery("from ArmIdMap").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArmIdMap> getByIdsOccupied(boolean idsSwitchValue) {
		return session.createQuery("from ArmIdMap where isChildIdOccupied=?").setBoolean(0, idsSwitchValue).list();
	}
}
*/