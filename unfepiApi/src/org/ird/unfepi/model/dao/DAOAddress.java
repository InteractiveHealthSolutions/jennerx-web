/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Address;

/**
 * The Interface DAOAddress.
 */
public interface DAOAddress extends DAO{

	Address getAddressById(int addressId, boolean isreadonly, String[] mappingsToJoin);
	/**
	 * Gets the address.
	 *
	 * @param entityId the entity id
	 * @param includeVoided the include voided
	 * @return the address
	 */
	List<Address> getAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	/**
	 * Gets the primary address.
	 *
	 * @param entityId the entity id
	 * @param includeVoided the include voided
	 * @return the primary address
	 */
	List<Address> getPrimaryAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin);

}
