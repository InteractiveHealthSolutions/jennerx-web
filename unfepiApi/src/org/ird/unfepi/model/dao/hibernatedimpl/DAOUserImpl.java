/*
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.model.dao.DAOUser;

/**
 * The Class DAOUserImpl.
 */
public class DAOUserImpl extends DAOHibernateImpl implements DAOUser{
	
	/** The session. */
	private Session session ;
	
	/** The LAS t_ quer y_ tota l_ ro w__ count. */
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO user impl.
	 *
	 * @param session the session
	 */
	public DAOUserImpl(Session session) {
		super(session);
		this.session=session;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOUser#findById(int)
	 */
	@Override
	public User findById(int id) {
		User u= (User)session.get(User.class,id);
		setLAST_QUERY_TOTAL_ROW_COUNT(u==null?0:1);
		return u;
	}
	@SuppressWarnings("unchecked")
	@Override
	public User findById(int mappedId, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(User.class).setReadOnly(isreadonly);
				
		if(joinIdMapper){
			cri.createAlias("idMapper", "idmp").createAlias("idmp.identifiers", "idm");
			
			if(joinRoles){
				cri.createAlias("idMapper.role", "role")
					.setFetchMode("role",(joinRoles ? FetchMode.JOIN : FetchMode.SELECT));
				
				if(joinRolePermissions){
					cri.createAlias("role.permissions", "perms",JoinType.LEFT_OUTER_JOIN)
						.setFetchMode("perms",(joinRolePermissions ? FetchMode.JOIN : FetchMode.SELECT));
				}
			}
		}
		else{
			cri.addOrder(Order.asc("username"));
		}

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<User> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findById(String programid) {
		Criteria cri = session.createCriteria(User.class);
		
		cri.setFetchMode("idMapper",FetchMode.JOIN ).createAlias("idMapper.identifiers", "idm").add(Restrictions.eq("idm.identifier", programid));
			
		List<User> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findById(String programid, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(User.class).setReadOnly(isreadonly);
		
		if(joinIdMapper || programid != null){
			cri.createAlias("idMapper", "idmp").createAlias("idmp.identifiers", "idm");
			
			if(programid != null){
				cri.add(Restrictions.eq("idm.identifier", programid));
			}
			
			if(joinRoles){
				cri.createAlias("idMapper.role", "role")
					.setFetchMode("role",(joinRoles ? FetchMode.JOIN : FetchMode.SELECT));
				
				if(joinRolePermissions){
					cri.createAlias("role.permissions", "perms",JoinType.LEFT_OUTER_JOIN)
						.setFetchMode("perms",(joinRolePermissions ? FetchMode.JOIN : FetchMode.SELECT));
				}
			}
		}
		else{
			cri.addOrder(Order.asc("username"));
		}

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<User> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOUser#findByUsername(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User findByUsername(String username) {
		List<User> ulist=session.createQuery("from User where username=? ").setString(0, username).list();
		setLAST_QUERY_TOTAL_ROW_COUNT(ulist.size());
		if(ulist.size()>0){
			return ulist.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User findByUsername(String username, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) 
	{
		Criteria cri = session.createCriteria(User.class).setReadOnly(isreadonly);
		
		cri.add(Restrictions.eq("username", username));

		if(joinIdMapper ){
			cri.createAlias("idMapper", "idmp").createAlias("idmp.identifiers", "idm");
			
			if(joinRoles){
				cri.createAlias("idMapper.role", "role")
					.setFetchMode("role",(joinRoles ? FetchMode.JOIN : FetchMode.SELECT));
				
				if(joinRolePermissions){
					cri.createAlias("role.permissions", "perms",JoinType.LEFT_OUTER_JOIN)
						.setFetchMode("perms",(joinRolePermissions ? FetchMode.JOIN : FetchMode.SELECT));
				}
			}
		}
		else{
			cri.addOrder(Order.asc("username"));
		}

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<User> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
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
	 * @see org.ird.unfepi.model.dao.DAOUser#LAST_QUERY_TOTAL_ROW_COUNT()
	 */
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	/* (non-Javadoc)
	 * @see org.ird.unfepi.model.dao.DAOUser#getAll(int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll(int firstResult, int fetchsize, boolean isreadonly, boolean joinIdMapper, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(User.class).setReadOnly(isreadonly);
				
		if(joinIdMapper){
			cri.createAlias("idMapper", "idmp").createAlias("idmp.identifiers", "idm");
		}
		else{
			cri.addOrder(Order.asc("username"));
		}

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<User> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findByCriteria(String programid, String email,
			String partOfFirstOrLastName, UserStatus userStatus,
			boolean putNotWithUserStatus, int firstResult, int fetchsize, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) 
	{
		Criteria cri= session.createCriteria(User.class)
				.setFetchMode("idMapper",FetchMode.JOIN )
				.createAlias("idMapper", "idmp").createAlias("idmp.identifiers", "idm").setReadOnly(isreadonly);

		if(joinIdMapper || programid != null){
			
			if(programid != null){
				cri.add(Restrictions.eq("idm.identifier", programid));
			}
			
			if(joinRoles){
				cri.createAlias("idMapper.role", "role")
					.setFetchMode("role",(joinRoles ? FetchMode.JOIN : FetchMode.SELECT));
				
				if(joinRolePermissions){
					cri.createAlias("role.permissions", "perms",JoinType.LEFT_OUTER_JOIN)
						.setFetchMode("perms",(joinRolePermissions ? FetchMode.JOIN : FetchMode.SELECT));
				}
			}
		}
		else{
			cri.addOrder(Order.asc("username"));
		}
		if(partOfFirstOrLastName!=null){
			cri.add(Restrictions.or(Restrictions.like("firstName", partOfFirstOrLastName,MatchMode.START)
									,Restrictions.like("lastName", partOfFirstOrLastName,MatchMode.START)));
		}
		if(email!=null){
			cri.add(Restrictions.like("email", email,MatchMode.EXACT));
		}
		if(userStatus!=null){
			if(putNotWithUserStatus){
				cri.add(Restrictions.ne("status", userStatus));
			}else{
				cri.add(Restrictions.eq("status", userStatus));
			}
		}
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<User> list = cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
}
