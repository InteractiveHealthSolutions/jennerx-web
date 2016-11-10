/*
 * 
 */
package org.ird.unfepi.service;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;

public interface DemographicDetailsService {

	Address getAddressById(int addressId, boolean isreadonly, String[] mappingsToJoin);

	List<Address> getAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin);

	List<Address> getPrimaryAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin);
	
	ContactNumber getContactNumberById(int contactNumberId, boolean isreadonly, String[] mappingsToJoin);

	List<ContactNumber> getContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) ;
	
	List<ContactNumber> findContactNumber(String number, boolean isreadonly, String[] mappingsToJoin) ;

	ContactNumber getUniquePrimaryContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) ;

	List<ContactNumber> getUniqueContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) ;

	List<ContactNumber> getUniqueContactNumber(String number, boolean isreadonly, String[] mappingsToJoin);
	
	Serializable saveAddress(Address address);

	Address mergeUpdateAddress(Address address);

	void updateAddress(Address address);
	
	//Serializable saveNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;
	
	//Serializable saveUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

	Serializable saveContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

	//ContactNumber mergeUpdateNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;
	
	//ContactNumber mergeUpdateUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

	ContactNumber mergeUpdateContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

	//void updateNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;
	
	//void updateUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

	void updateContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException;

}
