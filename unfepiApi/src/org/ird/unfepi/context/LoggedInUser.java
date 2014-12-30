/*
 * 
 */
package org.ird.unfepi.context;

import java.util.Date;

import org.ird.unfepi.model.User;

/**
 * The Class LoggedInUser.
 */
public class LoggedInUser{
	
	/** The user. */
	private User user;
	
	/** The date. */
	private Date date;

	/**
	 * Instantiates a new logged in user.
	 *
	 * @param user the user
	 */
	LoggedInUser(User user){
		this.user=user;
		this.date=new Date();
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser(){
		refreshDateTime();
		return user;
	}
	
	/**
	 * Gets the date time.
	 *
	 * @return the date time
	 */
	public Date getDateTime(){
		return date;
	}
	
	/**
	 * Refresh date time.
	 */
	public void refreshDateTime(){
		this.date=new Date();
	}
	
	/**
	 * Gets the roles.
	 *
	 * @return the roles
	 */
	public String getRole(){
		return user.getIdMapper().getRole().getRolename();
	}
	
	/**
	 * Checks for role.
	 *
	 * @param role the role
	 * @return true, if successful
	 */
	public boolean hasRole(String role){
		return user.getIdMapper().getRole().getRolename().trim().equalsIgnoreCase(role.trim());
	}
	
	/**
	 * Checks for permission.
	 *
	 * @param permission the permission
	 * @return true, if successful
	 */
	public boolean hasPermission(String permission){
		return user.getIdMapper().getRole().hasPermission(permission);
	}
	
	public boolean isDefaultAdministrator(){
		return User.isUserDefaultAdministrator(user.getUsername(), user.getIdMapper().getRole().getRolename());
	}
}