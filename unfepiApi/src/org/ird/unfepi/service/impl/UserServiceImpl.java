package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.model.dao.DAOPermission;
import org.ird.unfepi.model.dao.DAORole;
import org.ird.unfepi.model.dao.DAOUser;
import org.ird.unfepi.service.UserService;
import org.ird.unfepi.service.exception.UserServiceException;
import org.ird.unfepi.service.validations.UserValidations;
import org.ird.unfepi.utils.SecurityUtils;

public class UserServiceImpl implements UserService{

	DAOUser u;
	
	DAORole r;
	
	DAOPermission perm;
	
	public UserServiceImpl(DAOUser user,DAORole role,DAOPermission per){
		u=user;
		r=role;
		perm=per;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz) {
		if(clazz == User.class){
			return u.LAST_QUERY_TOTAL_ROW_COUNT();
		}
		else if(clazz == Role.class){
			throw new UnsupportedOperationException();
		}
		else if(clazz == Permission.class){
			throw new UnsupportedOperationException();
		}
		
		return null;
	}

	@Override
	public void changePassword(String username,String oldpwd, String pw1, String pw2) throws Exception{
		User user=u.findByUsername(username);

		if(user!=null){
			if(pw1.compareTo(pw2)!=0){
				throw new UserServiceException(UserServiceException.PASSWORDS_NOT_MATCH);
			}
			if(!UserValidations.validatePassword(pw1)){
				throw new UserServiceException(UserServiceException.INVALID_PASSWORD_CHARACTERS);
			}
			if(oldpwd.compareTo(SecurityUtils.decrypt(user.getPassword(),user.getUsername()))!=0){
				throw new UserServiceException(UserServiceException.WRONG_PASSWORD);
			}
			user.setClearTextPassword(pw1);
			updateUser(user);
		}
	}

	@Override
	public Serializable createUser(User user) throws Exception {
		if(user.hasNewPassword()){
			user.setPassword(SecurityUtils.encrypt(user.getClearTextPassword(), user.getUsername()));
		}else{
			throw new UserServiceException(UserServiceException.USER_PASSWORD_MISSING);
		}
		UserValidations.validateUser(user);
		if(u.findByUsername(user.getUsername())!=null){
			throw new UserServiceException(UserServiceException.USER_EXISTS);
		}
		return u.save(user);
		
	}

	@Override
	public void deleteRole(Role role)  {
		r.delete(role);
	}

	@Override
	public void deleteUser(User user)  {
		u.delete(user);
	}

	@Override
	public List<Permission> getAllPermissions(boolean isreadonly) {
		List<Permission> plst= perm.getAll(isreadonly);
		return plst;
	}

	@Override
	public List<Role> getAllRoles(boolean isreadonly, String[] mappingsToJoin) {
		List<Role> rlst= r.getAll(isreadonly, mappingsToJoin);
		return rlst;
	}

	@Override
	public List<User> getAllUser(int firstResult, int fetchSize, boolean isreadonly, boolean joinIdMapper, String[] mappingsToJoin)  {
		List<User> ulst= u.getAll(firstResult,fetchSize, isreadonly, joinIdMapper, mappingsToJoin);
		return ulst;
	}

	@Override
	public Role getRole(short roleId, boolean isreadonly, String[] mappingsToJoin){
		return r.findById(roleId, isreadonly, mappingsToJoin);
	}

	@Override
	public User findUserByUsername(String userName) {
		User usr= u.findByUsername(userName);
		return usr;
	}
	
	@Override
	public User findUserByUsername(String username, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) {
		User usr= u.findByUsername(username, isreadonly, joinIdMapper, joinRoles, joinRolePermissions, mappingsToJoin);
		return usr;
	}
	
	@Override
	public List<User> findUserByCriteria(String programid, String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus, int firstResult,
			int fetchsize, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin){
		List<User> ulst=  u.findByCriteria(programid, email, partOfFirstOrLastName, userStatus, putNotWithUserStatus, firstResult, fetchsize, isreadonly, joinIdMapper, joinRoles, joinRolePermissions, mappingsToJoin);
		return ulst;
	}

	@Override
	public void updateRole(Role role) {
		r.update(role);
	}

	@Override
	public User updateUser(User user) throws Exception{
		if(user.hasNewPassword()){
			user.setPassword(SecurityUtils.encrypt(user.getClearTextPassword(), user.getUsername()));
		}
		return (User) u.merge(user);
	}

	@Override
	public Role getRole(String rolename, boolean isreadonly, String[] mappingsToJoin) {
		Role rol= r.findByName(rolename, isreadonly, mappingsToJoin);
		return rol;
	}

	@Override
	public Serializable addRole(Role role) {
		return r.save(role);
	}

	@Override
	public Permission getPermission(String permission,boolean isreadonly) {
		Permission p= perm.findByPermissionName(permission,true);
		return p;
	}

	@Override
	public List<Role> getRolesByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		List<Role> rlst=r.findByCriteria(name, isreadonly, mappingsToJoin);
		return rlst;
	}

	@Override
	public User findUser(int mappedId) {
		return u.findById(mappedId);
	}

	@Override
	public User findUser(int mappedId, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) {
		return u.findById(mappedId, isreadonly, joinIdMapper, joinRoles, joinRolePermissions, mappingsToJoin);
	}

	@Override
	public User findUser(String programid) {
		return u.findById(programid);
	}

	@Override
	public User findUser(String programid, boolean isreadonly,
			boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin) {
		return u.findById(programid, isreadonly, joinIdMapper, joinRoles, joinRolePermissions, mappingsToJoin);
	}
}
