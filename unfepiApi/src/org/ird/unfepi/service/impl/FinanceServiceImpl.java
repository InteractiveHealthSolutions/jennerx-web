/*package org.ird.unfepi.service.impl;

import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.VerificationCode;
import org.ird.unfepi.model.dao.DAOVerificationCode;
import org.ird.unfepi.service.FinanceService;

public class FinanceServiceImpl implements FinanceService{

	private ServiceContext sc;
	
	private DAOVerificationCode daovercode;
	
	public FinanceServiceImpl(ServiceContext sc, DAOVerificationCode daovercode) {
		this.sc = sc;
		this.daovercode = daovercode;
	}
	
	@Override
	public VerificationCode findVerificationCode(long verificationCode) {
		return daovercode.findVerificationCode(verificationCode);
	}

	@Override
	public VerificationCode findFirstAvailableCode() {
		return daovercode.findFirstAvailableCode();
	}
}
*/