/*
 * 
 */
package org.ird.unfepi.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

/**
 * The Class User.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * The Enum UserStatus.
	 */
	public enum UserStatus {
		
		/** The ACTIVE. */
		ACTIVE,
		
		/** The DISABLED. */
		DISABLED;
	}

	/** The user id. */
	@Id
	private int mappedId;

	/** The login name of the user. */

	@Column(length = 50 , unique = true, nullable = false)
	private String username;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = IdMapper.class, optional = false)
	@PrimaryKeyJoinColumn(name = "mappedId")
	@ForeignKey(name = "user_mappedId_idmapper_mappedId_FK")
	private IdMapper idMapper;
	
	/** The user hashed password. */

	private String password;

	@Column(length = 30, nullable = false)
	private String firstName;

	/** The middle name of the user. */

	@Column(length = 30)
	private String middleName;

	/** The last name of the user. */

	@Column(length = 30)
	private String lastName;
	
	/** The List of roles that the user has. *//*

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER ,cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@JoinTable(name = "user_role", joinColumns=@JoinColumn(name="userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> roles  = new HashSet<Role>();*/

	/** The email address of the user. */

	@Column(length = 50)
	private String email;

	/** Flag to determine if <code>User</code> is disabled or not. */

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private UserStatus status;

	/** The description. */
	
	private String description;
	
	/** The created by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "user_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	/** The created date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	/** The last edited by user id. */
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "user_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	/** The last edited date. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	/** The user's clear text password. This is just a means of transfer to the
	 * service layer because it's never persisted.
	 */
	@Transient
	private String clearTextPassword;

	/** Constructor used to create a new user object.
	 */
	public User() {

	}
	
	public User(int mappedId) {
		this.setMappedId(mappedId);
	}
	/**
	 * Constructor used to create a user with a given login name and password.
	 *
	 * @param userId the user id
	 * @param name the login name.
	 * @param password the password
	 */


	public User(int mappedId, String username, String clearTextpassword) {
		this.setMappedId(mappedId);
		this.username = username;
		this.clearTextPassword = clearTextpassword;
	}

	/**
	 * Constructor used to create a user with a given database id and login
	 * name.
	 * @param userId
	 *            the database id.
	 * @param name
	 *            the login name.
	 */

	public User(int mappedId, String username) {
		this.setMappedId(mappedId);
		this.username = username;
	}

	public int getMappedId() {
		return mappedId;
	}

	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	public IdMapper getIdMapper() {
		return idMapper;
	}

	public void setIdMapper(IdMapper idMapper) {
		this.idMapper = idMapper;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Gets the full name.
	 *
	 * @return the full name
	 */
	public String getFullName() {
		String fullName = "";
		if (firstName != null)
			fullName += firstName + " ";
		if (middleName != null)
			fullName += middleName + " ";
		if (lastName != null)
			fullName += lastName;
		return fullName;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 */
	public User getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 */
	public void setCreatedByUserId(User createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the last edited by user id.
	 *
	 * @return the last edited by user id
	 */
	public User getLastEditedByUserId() {
		return lastEditedByUserId;
	}

	/**
	 * Sets the last edited by user id.
	 *
	 * @param lastEditedByUserId the new last edited by user id
	 */
	public void setLastEditedByUserId(User lastEditedByUserId) {
		this.lastEditedByUserId = lastEditedByUserId;
	}

	/**
	 * Gets the last edited date.
	 *
	 * @return the last edited date
	 */
	public Date getLastEditedDate() {
		return lastEditedDate;
	}

	/**
	 * Sets the last edited date.
	 *
	 * @param lastEditedDate the new last edited date
	 */
	public void setLastEditedDate(Date lastEditedDate) {
		this.lastEditedDate = lastEditedDate;
	}

	/**
	 * Gets the clear text password.
	 *
	 * @return the clear text password
	 */
	public String getClearTextPassword() {
		return clearTextPassword;
	}
	
	/**
	 * Sets the clear text password.
	 *
	 * @param clearTextPassword the new clear text password
	 */
	public void setClearTextPassword(String clearTextPassword) {
		this.clearTextPassword = clearTextPassword;
	}
	
	/**
	 * Checks for new password.
	 *
	 * @return true, if successful
	 */
	public boolean hasNewPassword() {
		return (clearTextPassword != null && clearTextPassword.trim().length() > 0);
	}

	/**
	 * Retrieves all the <code>User Roles.</code>
	 * @return <code>Set</code> of <code>User Roles.</code>
	 *//*

	public synchronized Set<Role> getRoles() {
		synchronized (this) {
			return roles;
		}
	}

	*//**
	 * Sets the <code>User Roles.</code>
	 * @param roles
	 *            <code>User Roles.</code>
	 *//*

	public synchronized void setRoles(Set<Role> roles) {
		synchronized (this) {
			this.roles = roles;
		}
	}

	*//**
	 * Adds a <code>Role</code> to the set of roles mapped to this
	 * <code>User.</code>
	 * @param role
	 *            <code>Role</code> to map to the <code>User.</code>
	 *//*

	public synchronized void addRole(Role role) {
		synchronized (this) {
			if (role != null) {
				for (Role x : this.roles) {
					if (x.getRolename().equalsIgnoreCase(role.getRolename()))
						return;
				}
				roles.add(role);
			}
		}
	}

	*//**
	 * Removes a <code>Role</code> from the set of roles mapped to this
	 * <code>User.</code>
	 * @param role
	 *            <code>Role</code> to remove from the <code>User.</code>
	 *//*

	public synchronized void removeRole(Role role) {
		synchronized (this) {
			if (role != null) {
				for (Role x : roles) {
					if (x.getRolename().equals(role.getRolename())) {
						roles.remove(x);
						break;
					}
				}
			}
		}
	}

	
	 * public boolean hasNewPassword(){
	 * 
	 * return (clearTextPassword != null && clearTextPassword.trim().length() >
	 * 0);
	 * 
	 * }
	 

	*//**
	 * Determines if this user has the administrator role.
	 *
	 * @return true, if successful
	 *//*

	public boolean hasAdministrativePrivileges() {
		boolean ret = false;
		if (roles != null) {
			for (Role r : roles) {
				if (r.getRolename().equalsIgnoreCase("Role_Administrator")
					|| r.getRolename().equalsIgnoreCase("Administrator")
						|| r.getRolename().equalsIgnoreCase("Admin")) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	*//**
	 * Determines if this user has the specified permission.
	 *
	 * @param permission String permission (e.g. "Perm_Data_Edit")
	 * @return true, if successful
	 *//*

	public boolean hasPermission(String permission) {
		boolean ret = false;
		if (permission != null) {
			if (roles != null) {
				for (Role r : roles) {
					if (r.getPermissions().size() != 0) {
						for (Permission p : r.getPermissions()) {
							if (permission.equalsIgnoreCase(p.getPermissionname())) {
								ret = true;
								break;
							}
						}
					}
				}
			}
		}
		return ret;
	}

	*//**
	 * Ascertains if the current <code>User</code>
	 * is the default administrator that ships with the system.
	 *
	 * @return <code>true if(user is default administrator)</code>
	 * @see #hasAdministrativePrivileges() to check if a user
	 * is an administrator (sees if a user has the admin role).
	 *//*

	public boolean isDefaultAdministrator() {
		boolean defaultAdmin = false;
		if (this.firstName != null && this.middleName != null) {
			if (this.username.equalsIgnoreCase("admin")
					|| this.username.equalsIgnoreCase("administrator"))
				defaultAdmin = true;
		}
		return defaultAdmin;
	}

	*//**
	 * Checks if this <code>User</code> has a given <code>Role</code>.
	 * @param role
	 *            <code>Role</code> to check.
	 * @return <code>True only and only if Role exists in the list of roles assigned to User.</code>
	 *//*

	public synchronized boolean hasRole(String role) {
		synchronized (this) {
			for (Role xRole : roles) {
				if (xRole.getRolename().equalsIgnoreCase(role))
					return true;
			}
			return false;
		}
	}*/
	
	public static boolean isUserDefaultAdministrator(String username, String rolename){
		return ((username.equalsIgnoreCase("administrator")
				|| username.equalsIgnoreCase("admin"))
				&&
				(rolename.equalsIgnoreCase("administrator")
				|| rolename.equalsIgnoreCase("admin"))
				);
	}
	
	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator){
		setCreatedByUserId(creator);
		setCreatedDate(new Date());
	}
	
	/**
	 * Sets the editor.
	 *
	 * @param editor the new editor
	 */
	public void setEditor(User editor){
		setLastEditedByUserId(editor);
		setLastEditedDate(new Date());
	}
	
}