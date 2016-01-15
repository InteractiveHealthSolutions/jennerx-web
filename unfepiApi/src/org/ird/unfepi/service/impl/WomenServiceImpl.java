/**
 * 
 */
package org.ird.unfepi.service.impl;

import java.io.Serializable;
import java.util.List;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Women;
import org.ird.unfepi.model.dao.DAOWomen;
import org.ird.unfepi.service.WomenService;

/**
 * @author Safwan
 *
 */
public class WomenServiceImpl implements WomenService {
	
	private ServiceContext sc;

	private DAOWomen daoWomen;
	
	
	public WomenServiceImpl(ServiceContext sc, DAOWomen pat){
		this.sc = sc;
		this.daoWomen=pat;
	}


	@Override
	public Women findById(int mappedId) {
		Women women = daoWomen.findById(mappedId);
		return women;
	}


	@Override
	public List<Women> getAllWomen(boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin) {
		List<Women> women = daoWomen.getAllWomen(isreadonly, firstResult, fetchsize, mappingsToJoin);
		return women;
	}


	@Override
	public List<Women> getWomen(String partialName, String nic) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Serializable save(Women women) {
		return daoWomen.save(women);
	}


	@Override
	public Women findWomenById(int mappedId, boolean isreadonly,
			String[] mappingsToJoin) {
		Women p = daoWomen.findById(mappedId, isreadonly, mappingsToJoin);
		return p;
	}


	@Override
	public Women findWomenByIdentifier(String programId, boolean isreadonly, String[] mappingsToJoin) {
		Women p = daoWomen.findWomenByIdentifier(programId, isreadonly, mappingsToJoin);
		return p;
	}


}


