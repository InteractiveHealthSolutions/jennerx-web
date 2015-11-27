/*
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Address;
import org.ird.unfepi.model.ContactNumber;
import org.ird.unfepi.model.Ethnicity;
import org.ird.unfepi.model.Language;
import org.ird.unfepi.model.Model.ContactType;
import org.ird.unfepi.model.Relationship;
import org.ird.unfepi.model.Religion;
import org.ird.unfepi.model.dao.DAOAddress;
import org.ird.unfepi.model.dao.DAOContactNumber;
import org.ird.unfepi.model.dao.DAOEthnicity;
import org.ird.unfepi.model.dao.DAOLanguage;
import org.ird.unfepi.model.dao.DAORelationship;
import org.ird.unfepi.model.dao.DAOReligion;
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
	
	/** The daoethn. */
	private DAOEthnicity daoethn;
	
	/** The daorelation. */
	private DAORelationship daorelation;
	
	/** The daoreligion. */
	private DAOReligion daoreligion;
	
	private DAOLanguage daolang;
	
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
	public DemographicDetailsServiceImpl(ServiceContext sc, /*DAOPerson daoper,*/	DAOAddress daoaddr,	DAOContactNumber daocont,
			DAOEthnicity daoethn, DAORelationship daorelation, DAOReligion daoreligion, DAOLanguage daolang) {
		this.sc = sc;
		/*this.daoper = daoper;*/
		this.daoaddr = daoaddr;
		this.daocont = daocont;
		this.daoethn = daoethn;
		this.daorelation = daorelation;
		this.daoreligion = daoreligion;
		this.daolang = daolang;
	}

	@Override
	public List<Ethnicity> getAllEthnicity() {
		return daoethn.getAll();
	}

	@Override
	public Ethnicity getEthnicityByName(String ethnicityName) {
		return daoethn.getByName(ethnicityName);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#findPersonById(long)
	 */
	/*@Override
	public Person findPersonById(int mappedId) {
		return daoper.findById(mappedId);
	}*/

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getAllRelationship()
	 */
	@Override
	public List<Relationship> getAllRelationship() {
		return daorelation.getAll();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getRelationshipByName(java.lang.String)
	 */
	@Override
	public Relationship getRelationshipByName(String relationName) {
		return daorelation.getByName(relationName);
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getAllReligion()
	 */
	@Override
	public List<Religion> getAllReligion() {
		return daoreligion.getAll();
	}

	/* (non-Javadoc)
	 * @see org.ird.unfepi.service.DemographicDetailsService#getReligionByName(java.lang.String)
	 */
	@Override
	public Religion getReligionByName(String religionName) {
		return daoreligion.getByName(religionName);
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
	public Serializable saveEthnicity(Ethnicity ethnicity) {
		return daoethn.save(ethnicity);
	}

	@Override
	public Ethnicity mergeUpdateEthnicity(Ethnicity ethnicity) {
		return (Ethnicity) daoethn.merge(ethnicity);
	}

	@Override
	public void updateEthnicity(Ethnicity ethnicity) {
		daoethn.update(ethnicity);
	}

	@Override
	public Serializable saveRelationship(Relationship relationship) {
		return daorelation.save(relationship);
	}

	@Override
	public Relationship mergeUpdateRelationship(Relationship relationship) {
		return (Relationship) daorelation.merge(relationship);
	}

	@Override
	public void updateRelationship(Relationship relationship) {
		daorelation.update(relationship);
	}

	@Override
	public Serializable saveReligion(Religion religion) {
		return daoreligion.save(religion);
	}

	@Override
	public Religion mergeUpdateReligion(Religion religion) {
		return (Religion) daoreligion.merge(religion);
	}

	@Override
	public void updateReligion(Religion religion) {
		daoreligion.update(religion);
	}

	@Override
	public List<Language> getAllLanguage() {
		return daolang.getAll();
	}

	@Override
	public Language getLanguageByName(String languageName) {
		return daolang.getByName(languageName);
	}
	
	/*@Override
	public Serializable savePerson(Person person) {
		return daoper.save(person);
	}

	@Override
	public Person mergeUpdatePerson(Person person) {
		return (Person) daoper.merge(person);
	}

	@Override
	public void updatePerson(Person person) {
		daoper.update(person);
	}*/

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
	
	@Override
	public Ethnicity getEthnicityById(short ethnicityId) {
		return daoethn.getById(ethnicityId);
	}

	@Override
	public Relationship getRelationshipById(short relationId) {
		return daorelation.getById(relationId);
	}

	@Override
	public Religion getReligionById(short religionId) {
		return daoreligion.getById(religionId);
	}

	@Override
	public Language getLanguageById(short languageId) {
		return daolang.getById(languageId);
	}
}
