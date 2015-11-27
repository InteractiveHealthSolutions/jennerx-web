/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.ContactNumber;

/**
 * The Interface DAOContactNumber.
 */
public interface DAOContactNumber extends DAO{
	
	ContactNumber getContactNumberById(int contactNumberId, boolean isreadonly, String[] mappingsToJoin);
	/**
	 * Gets the contact number.
	 *
	 * @param entityId the entity id
	 * @param includeVoided the include voided
	 * @return the contact number
	 */
	List<ContactNumber> getContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	/**
	 * Gets the primary contact number.
	 *
	 * @param entityId the entity id
	 * @param includeVoided the include voided
	 * @return the primary contact number
	 */
	List<ContactNumber> getPrimaryUniqueContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin);

	//List<ContactNumber> getUniqueContactNumber(int mappedId);

	//List<ContactNumber> getUniqueContactNumber(String number);

	List<ContactNumber> findContactNumber(String number, boolean isreadonly, String[] mappingsToJoin) ;
}
