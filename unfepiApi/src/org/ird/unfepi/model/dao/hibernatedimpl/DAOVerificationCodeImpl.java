/*package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Session;
import org.ird.unfepi.model.VerificationCode;
import org.ird.unfepi.model.VerificationCode.VerificationCodeStatus;
import org.ird.unfepi.model.dao.DAOVerificationCode;

public class DAOVerificationCodeImpl extends DAOHibernateImpl implements DAOVerificationCode{

	private Session session;

	public DAOVerificationCodeImpl(Session session) {
		super(session);
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VerificationCode findVerificationCode(long verificationCode) {
		List<VerificationCode> list = session.createQuery("from VerificationCode where verificationCode = "+verificationCode).list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public VerificationCode findFirstAvailableCode() {
		List<VerificationCode> list = session.createQuery("from VerificationCode where codeStatus = '"+VerificationCodeStatus.AVAILABLE+"'").list();
		return (list.size() == 0 ? null : list.get(0));
	}

}
*/