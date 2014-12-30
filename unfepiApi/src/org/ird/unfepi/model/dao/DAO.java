/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Interface DAO.
 */
public interface DAO {
	
	Serializable save(Object objectinstance);

	void delete(Object objectinstance);

	Object merge(Object objectinstance);

	void update(Object objectinstance);
}
