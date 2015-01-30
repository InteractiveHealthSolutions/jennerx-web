/*
 * 
 */
package org.ird.unfepi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class Relationship.
 */
@Entity
@Table(name = "relationship")
public class Relationship {

	/** The relationship id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    /*@Column(columnDefinition = "SMALLINT NOT NULL AUTO_INCREMENT")*/
	private short relationshipId;
	
	/** The relation name. */
	@Column(length = 30, unique = true)
	private String relationName;

	public Relationship() {
		// TODO Auto-generated constructor stub
	}
	
	public Relationship(short relationshipId) {
		this.relationshipId = relationshipId;
	}
	/**
	 * Gets the relationship id.
	 *
	 * @return the relationship id
	 */
	public short getRelationshipId() {
		return relationshipId;
	}

	/**
	 * Sets the relationship id.
	 *
	 * @param relationshipId the new relationship id
	 */
	public void setRelationshipId(short relationshipId) {
		this.relationshipId = relationshipId;
	}

	/**
	 * Gets the relation name.
	 *
	 * @return the relation name
	 */
	public String getRelationName() {
		return relationName;
	}

	/**
	 * Sets the relation name.
	 *
	 * @param relationName the new relation name
	 */
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
}
