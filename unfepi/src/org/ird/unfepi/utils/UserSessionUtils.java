package org.ird.unfepi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Permission;
import org.ird.unfepi.model.Role;
import org.ird.unfepi.model.User;
import org.ird.unfepi.service.exception.UserServiceException;

public class UserSessionUtils {
	public static final String USERNAME_KEY = "username";
	private static Map<String, LoggedInUser> currentlyLoggedInUsers = new HashMap<String, LoggedInUser>();
	/*private static Map<String, Role> rolesPermissions = loadRolesAndPermissions();
	
	private static Map<String, Role> loadRolesAndPermissions () {
		HashMap<String, Role> role;
		TarseelServices tsc = TarseelContext.getServices();
		try{
			List<Role> rl = tsc.getUserService().getAllRoles();
			for (Role rol : rl) {
				role.put(rol.getName(), rol);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}*/
	public static Map<String, LoggedInUser> getCurrentlyLoggedInUsers() {
		return currentlyLoggedInUsers;
	}
	
	private static void keepUserAlive(String username, HttpServletRequest req){
		getLoggedInUser(username).refreshDateTime();
		
		req.setAttribute( "user" , getLoggedInUser( username ) );
		req.getSession().setAttribute( USERNAME_KEY , username );
		req.getSession().setAttribute( "fullname" , getLoggedInUser(username).getUser().getFullName());
	}

	private static LoggedInUser getLoggedInUser(String username){
		return currentlyLoggedInUsers.get(username);
	}
	
	public static void login(String username, LoggedInUser user, HttpServletRequest req , HttpServletResponse resp ){
		currentlyLoggedInUsers.put(username, user);
		
		resp.addCookie(createCookie(USERNAME_KEY, username, 60*60*8));
		
		keepUserAlive(username, req);
	}
	
	public static void logout(String username, HttpServletRequest req , HttpServletResponse resp){
		currentlyLoggedInUsers.remove(username);
		
		removeCookie(username, req, resp);
		
		clearSession(req);
	}
	
	private static void clearSession( HttpServletRequest req ) {
		try {
			req.getSession().removeAttribute( USERNAME_KEY );
			req.getSession().removeAttribute( "user" );
		}
		catch ( Exception e ) {
		}
		
		req.getSession().invalidate();
	}

	public static LoggedInUser getActiveUser( HttpServletRequest req ) {
		LoggedInUser user = null;
		try {
			String username = (String) getUsername( req );
			user = getLoggedInUser( username );
			keepUserAlive(username, req);
		}
		catch ( Exception e ) {
			clearSession( req );
			req.getSession().setAttribute( "logmessage" , UserServiceException.SESSION_EXPIRED );
		}
		return user;
	}

	public static boolean hasActiveUserPermission(SystemPermissions permission, HttpServletRequest request) throws UserServiceException {
		boolean perm = false;
		try {
			perm = getActiveUser( request ).hasPermission( permission.name() );
		}
		catch ( Exception e ) {// throw null pointer exception only
											// if user is null means not active
			throw new UserServiceException( UserServiceException.SESSION_EXPIRED );
		}
		return perm;
	}

	
	public static boolean isUserSessionActive( HttpServletRequest req ) {
		try {
			String username = (String) getUsername( req );
			
			if(username != null && currentlyLoggedInUsers.get(username) != null){
				keepUserAlive( username, req );
				return true;
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
			clearSession( req );
			req.getSession().setAttribute( "logmessage" , UserServiceException.SESSION_EXPIRED );
		}
		return false;
	}

	private static Object getUsername( HttpServletRequest req ) {
		Object uname = req.getSession().getAttribute( USERNAME_KEY );
		if (uname == null) {
			for ( Cookie c : req.getCookies() ) {
				if (c.getName().compareTo( USERNAME_KEY ) == 0) {
					uname = c.getValue();
					break;
				}
			}
		}
		return uname;
	}

	private static Cookie createCookie( String name , String value , int age ) {
		Cookie cok = new Cookie( name , value );
		cok.setMaxAge( age );
		return cok;
	}

	private static void removeCookie( String name, HttpServletRequest req, HttpServletResponse resp ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				c.setMaxAge( -1 );
				resp.addCookie( c );
				break;
			}
		}
	}

	private static Cookie getCookie( String name , HttpServletRequest req ) {
		for ( Cookie c : req.getCookies() ) {
			if (c.getName().compareTo( name ) == 0) {
				return c;
			}
		}
		return null;
	}
	
	public static Map<String, ArrayList<Role>> getRolesDistinction(User editorUser, Role editorRole, ServiceContext sc){
		List<Role> allowedRoles=new ArrayList<Role>();
		List<Role> notAllowedRoles=new ArrayList<Role>();
		List<Role> allroles = new ArrayList<Role>();
		
		// DefaultAdmin can edit and assign ALL roles
		if(User.isUserDefaultAdministrator(editorUser.getUsername(), editorRole.getRolename())){
			allowedRoles = sc.getUserService().getAllRoles(true, null);
		}
		// DefaultAdmin User should only be editable by DefaultAdmin and its status and role should never be editable.
		else {
			allroles=sc.getUserService().getAllRoles(true, new String[]{"permissions"});
			
			for (Role r : allroles) {
				if(editorRole.getRolename().equalsIgnoreCase(r.getRolename())){
					allowedRoles.add(r);
				}
				else {
					boolean addr=true;
					for (Permission p : r.getPermissions()) {
						if(!editorRole.hasPermission(p.getPermissionname())){
							addr=false;
							break;
						}
					}
					if(addr){
						allowedRoles.add(r);
					}else{
						notAllowedRoles.add(r);
					}
				}
			}
		}
		
		Map<String, ArrayList<Role>> map = new HashMap<String, ArrayList<Role>>();
		map.put("ALLOWED_ROLES", (ArrayList<Role>) allowedRoles);
		map.put("NOT_ALLOWED_ROLES", (ArrayList<Role>) notAllowedRoles);
		return map;
	}
}
