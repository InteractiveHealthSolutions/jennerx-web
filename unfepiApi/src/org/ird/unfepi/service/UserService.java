package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.ird.unfepi.model.User.UserStatus;
import org.ird.unfepi.service.exception.UserServiceException;

public interface UserService {

	Number LAST_QUERY_TOTAL_ROW_COUNT(Class clazz);
	
	User findUser(int mappedId);
	
	User findUser(int mappedId, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	User findUser(String programid);
	
	User findUser(String programid, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	User findUserByUsername(String username);

	User findUserByUsername(String username, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	List<User> findUserByCriteria(String programid, String email, String partOfFirstOrLastName,
			UserStatus userStatus, boolean putNotWithUserStatus, int firstResult,
			int fetchsize, boolean isreadonly, boolean joinIdMapper, boolean joinRoles, boolean joinRolePermissions, String[] mappingsToJoin);

	List<User> getAllUser(int firstResult, int fetchsize, boolean isreadonly, boolean joinIdMapper, String[] mappingsToJoin);

	List<Permission> getAllPermissions(boolean isreadonly);

	List<Role> getAllRoles(boolean isreadonly, String[] mappingsToJoin);
	
	Role getRole(short roleId, boolean isreadonly, String[] mappingsToJoin);

	Role getRole(String rolename, boolean isreadonly, String[] mappingsToJoin);

	List<Role> getRolesByName(String name, boolean isreadonly, String[] mappingsToJoin);

	Serializable createUser(User user) throws UserServiceException, Exception;

	Serializable addRole(Role role);

	void deleteUser(User user) ;

	void deleteRole(Role role) ;

	User updateUser(User user) throws Exception ;

	void updateRole(Role role) ;

	void changePassword(String username,String oldpwd, String pw1, String pw2) throws UserServiceException, Exception;

	Permission getPermission(String permission,boolean isreadonly);
}
