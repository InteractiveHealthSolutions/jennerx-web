package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.LotterySms;

public interface DAOLotterySms extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	LotterySms findBySerialNumber(int serialNumber, boolean isreadonly, String[] mappingsToJoin);

	List<LotterySms> findByChild(int mappedId, boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);

	List<LotterySms> findByCriteria(String programId, Date datePreferenceChangedlower, Date datePreferenceChangedUpper, String preferredSmsTiming, Boolean hasApprovedLottery, Boolean hasApprovedReminders, Boolean hasCellPhoneAccess, boolean isreadonly, int firstResult, int fetchsize, String[] mappingsToJoin);
}
