package org.ird.unfepi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "role")
public class Role implements Serializable {

	/** The role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private short roleId;

	/** The name of the role. */

	@Column(length = 30, unique = true)
	private String rolename;

	/** The set of permissions that the role has. */

	@ManyToMany(targetEntity = Permission.class, fetch = FetchType.LAZY, cascade = {
			CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "role_permission", joinColumns=@JoinColumn(name="roleId"),inverseJoinColumns=@JoinColumn(name="permissionId"))
	private Set<Permission> permissions = new HashSet<Permission>();

//	/** The users. */
//	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "roles", targetEntity = User.class)
//	private List<User> users = new ArrayList<User>();

	private String description;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUserId")
	@ForeignKey(name = "role_createdByUserId_user_mappedId_FK")
	private User createdByUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "lastEditedByUserId")
	@ForeignKey(name = "role_lastEditedByUserId_user_mappedId_FK")
	private User lastEditedByUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastEditedDate;

	public Role() {
		
	}

	public Role(short roleId) {
		this.roleId = roleId;
	}
	/**
	 * Instantiates a new role.
	 *
	 * @param name the name
	 */
	public Role(String name) {
		this.rolename = name;
	}

	/**
	 * Checks if is new.
	 *
	 * @return true, if is new
	 */
	public boolean isNew() {
		return roleId == 0;
	}

	/**
	 * Adds the permission.
	 *
	 * @param permission the permission
	 */
	public void addPermission(Permission permission) {
		if (permission != null && !this.permissions.contains(permission)) {
			this.permissions.add(permission);
		}
	}

	/**
	 * Adds the permissions.
	 *
	 * @param permissions the permissions
	 */
	public void addPermissions(List<Permission> permissions) {
		for (Permission x : permissions) {
			if (x != null && !this.permissions.contains(x))
				this.permissions.add(x);
		}
	}

	/**
	 * Removes the permission.
	 *
	 * @param permission the permission
	 */
	public void removePermission(Permission permission) {
		if (permission != null) {
			for (Permission x : permissions) {
				if (x.getPermissionname().equals(permission.getPermissionname())) {
					permissions.remove(x);
					break;
				}
			}
		}
	}

	/**
	 * Checks for permission.
	 *
	 * @param permissionName the permission name
	 * @return true, if successful
	 */
	public boolean hasPermission(String permissionName) {
		if (permissions != null) {
			for (Permission p : permissions) {
				if (p.getPermissionname().equals(permissionName))
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	public short getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 *
	 * @param roleId the new role id
	 */
	public void setRoleId(short roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the rolename.
	 *
	 * @return the rolename
	 */
	public String getRolename() {
		return rolename;
	}

	/**
	 * Sets the rolename.
	 *
	 * @param rolename the new rolename
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	/**
	 * Gets the permissions.
	 *
	 * @return the permissions
	 */
	public Set<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * Sets the permissions.
	 *
	 * @param permissions the new permissions
	 */
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Gets the users.
	 *
	 * @return the users
	 *//*
	public List<User> getUsers() {
		return users;
	}

	*//**
	 * Sets the users.
	 *
	 * @param users the new users
	 *//*
	public void setUsers(List<User> users) {
		this.users = users;
	}*/

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