/*
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.dao.DAOAddress;
import org.ird.unfepi.model.dao.DAOContactNumber;
import org.ird.unfepi.service.DemographicDetailsService;
import org.ird.unfepi.service.exception.ChildDataInconsistencyException;

/**
 * The Class DemographicDetailsServiceImpl.
 */
public class DemographicDetailsServiceImpl implements DemographicDetailsService{

	private ServiceContext sc;
	/** The daoper. */
	/*private DAOPerson daoper;*/
	
	/** The daoaddr. */
	private DAOAddress daoaddr;
	
	/** The daocont. */
	private DAOContactNumber daocont;
	
	/**
	 * Instantiates a new demographic details service impl.
	 *
	 * @param daoper the daoper
	 * @param daoaddr the daoaddr
	 * @param daocont the daocont
	 * @param daoethn the daoethn
	 * @param daorelation the daorelation
	 * @param daoreligion the daoreligion
	 */
	public DemographicDetailsServiceImpl(ServiceContext sc, DAOAddress daoaddr,	DAOContactNumber daocont) {
		this.sc = sc;
		this.daoaddr = daoaddr;
		this.daocont = daocont;
	}

	@Override
	public Address getAddressById(int addressId, boolean isreadonly, String[] mappingsToJoin) {
		return daoaddr.getAddressById(addressId, isreadonly, mappingsToJoin);
	}
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getAddress(long, boolean)
	 */
	@Override
	public List<Address> getAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		return daoaddr.getAddress(mappedId, isreadonly, mappingsToJoin);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getPrimaryAddress(long, boolean)
	 */
	@Override
	public List<Address> getPrimaryAddress(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		return daoaddr.getPrimaryAddress(mappedId, isreadonly, mappingsToJoin);
	}

	@Override
	public ContactNumber getContactNumberById(int contactNumberId, boolean isreadonly, String[] mappingsToJoin) {
		return daocont.getContactNumberById(contactNumberId, isreadonly, mappingsToJoin);
	}
	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getContactNumber(long, boolean)
	 */
	@Override
	public List<ContactNumber> getContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		return daocont.getContactNumber(mappedId, isreadonly, mappingsToJoin);
	}

	@Override
	public List<ContactNumber> findContactNumber(String number, boolean isreadonly, String[] mappingsToJoin) {
		return daocont.findContactNumber(number, isreadonly, mappingsToJoin);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getPrimaryContactNumber(long, boolean)
	 */
	@Override
	public ContactNumber getUniquePrimaryContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		List<ContactNumber> list = daocont.getPrimaryUniqueContactNumber(mappedId, isreadonly, mappingsToJoin);
		
		if(list.size() > 0){
			return list.get(0);
		}
		
		return null;	
	}

	@Override
	public List<ContactNumber> getUniqueContactNumber(int mappedId, boolean isreadonly, String[] mappingsToJoin) {
		return daocont.getContactNumber(mappedId, isreadonly, mappingsToJoin);
	}
	@Override
	public List<ContactNumber> getUniqueContactNumber(String cellNumber, boolean isreadonly, String[] mappingsToJoin)  {
		return daocont.findContactNumber(cellNumber, isreadonly, mappingsToJoin);
	}

	@Override
	public Serializable saveAddress(Address address) {
		return daoaddr.save(address);
	}

	@Override
	public Address mergeUpdateAddress(Address address) {
		return (Address) daoaddr.merge(address);
	}

	@Override
	public void updateAddress(Address address) {
		daoaddr.update(address);
	}

	/*@Override
	public Serializable saveNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(validateNonUniqueContactNumber(contactNumber)){
			return daocont.save(contactNumber);
		}
		return null;
	}

	@Override
	public ContactNumber mergeUpdateNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(validateNonUniqueContactNumber(contactNumber)){
			return (ContactNumber) daocont.merge(contactNumber);
		}
		return null;
	}

	@Override
	public void updateNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(validateNonUniqueContactNumber(contactNumber)){
			daocont.update(contactNumber);
		}
	}*/

	/*@Override
	private boolean validateNonUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException{
		if(contactNumber.getNumberType().equals(ContactType.PRIMARY)
				|| contactNumber.getNumberType().equals(ContactType.SECONDARY)){
			throw new IllegalArgumentException("Contact number have ContactType Unique");
		}
		
		if(getUniqueContactNumber(contactNumber.getNumber(), false).size() > 0){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED, ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED);
		}
		return true;
	}*/

/*	@Override
	public Serializable saveUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(!contactNumber.getNumberType().equals(ContactType.PRIMARY) &&
				!contactNumber.getNumberType().equals(ContactType.SECONDARY)){
			throw new IllegalArgumentException("Contact number have ContactType other than Unique");
		}

		if(findContactNumber(contactNumber.getNumber(), true, null).size() > 0){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED, ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED);
		}
		
		if(contactNumber.getNumberType().equals(ContactType.PRIMARY)
				&& getUniquePrimaryContactNumber(contactNumber.getMappedId(), true, null) != null){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS, ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS);
		}
		
		return daocont.save(contactNumber);
	}

	@Override
	public ContactNumber mergeUpdateUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(!contactNumber.getNumberType().equals(ContactType.PRIMARY) &&
				!contactNumber.getNumberType().equals(ContactType.SECONDARY)){
			throw new IllegalArgumentException("Contact number have ContactType other than Unique");
		}
		TODO if(findContactNumber(contactNumber.getNumber(), false).size() > 0){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED, ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED);
		}
		
		if(contactNumber.getNumberType().equals(ContactType.PRIMARY_UNIQUE)
				|| getUniquePrimaryContactNumber(contactNumber.getEntityId()) != null){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS, ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS);
		}		
		
		return (ContactNumber) daocont.merge(contactNumber);
	}

	@Override
	public void updateUniqueContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(!contactNumber.getNumberType().equals(ContactType.PRIMARY) &&
				!contactNumber.getNumberType().equals(ContactType.SECONDARY)){
			throw new IllegalArgumentException("Contact number have ContactType other than Unique");
		}
		TODO if(findContactNumber(contactNumber.getNumber(), false).size() > 0){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED, ChildDataInconsistencyException.CHILD_UNIQUE_NUMBER_ALREADY_OCCUPIED);
		}
		
		if(contactNumber.getNumberType().equals(ContactType.PRIMARY_UNIQUE)
				|| getUniquePrimaryContactNumber(contactNumber.getEntityId()) != null){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS, ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBERS);
		}		
		
		daocont.update(contactNumber);
	}*/

	@Override
	public Serializable saveContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		if(contactNumber.getNumberType().equals(ContactType.PRIMARY)
				&& getUniquePrimaryContactNumber(contactNumber.getMappedId(), true, null) != null){
			throw new ChildDataInconsistencyException(ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBER, ChildDataInconsistencyException.CHILD_ALREADY_HAVING_A_PRIMARY_CONTACT_NUMBER);
		}
		
		return daocont.save(contactNumber);
	}

	@Override
	public ContactNumber mergeUpdateContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		return (ContactNumber) daocont.merge(contactNumber);
	}

	@Override
	public void updateContactNumber(ContactNumber contactNumber) throws ChildDataInconsistencyException {
		daocont.update(contactNumber);
	}
}
