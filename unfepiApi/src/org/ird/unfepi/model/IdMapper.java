package org.ird.unfepi.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "idmapper")
public class IdMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "INT NOT NULL AUTO_INCREMENT")*/
	private int mappedId;
	
	private short roleId;
	
	@OneToMany(targetEntity = Identifier.class, cascade = CascadeType.ALL)
	@JoinColumn(name="mappedId")
	@Fetch(FetchMode.JOIN)
	@OrderBy("preferred DESC")
	private List<Identifier> identifiers;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = Role.class)
	@JoinColumn(name = "roleId", insertable = false, updatable = false)
	@ForeignKey(name = "idmapper_roleId_Role_roleId_FK")
	private Role role;

	public IdMapper() {
		// TODO Auto-generated constructor stub
	}
	public int getMappedId() {
		return mappedId;
	}

	/**
	 * @param mappedId the mappedId to set
	 */
	public void setMappedId(int mappedId) {
		this.mappedId = mappedId;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	void setRole(Role role) {
		this.role = role;
	}
	public short getRoleId() {
		return roleId;
	}
	public void setRoleId(short roleId) {
		this.roleId = roleId;
	}
	public List<Identifier> getIdentifiers() {
		return identifiers;
	}
	public void setIdentifiers(List<Identifier> identifiers) {
		this.identifiers = identifiers;
	}

}
