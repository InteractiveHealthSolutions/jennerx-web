/*
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.Date;
import java.util.List;

import org.ird.unfepi.model.CommunicationNote;
import org.ird.unfepi.model.CommunicationNote.CommunicationCategory;
import org.ird.unfepi.model.CommunicationNote.CommunicationEventType;

public interface DAOCommunicationNote extends DAO {
	
	CommunicationNote findById(int id);
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	List<CommunicationNote> getByGroupId(String groupId, CommunicationEventType[] type, CommunicationCategory[] category, boolean isreadonly, String[] mappingsToJoin);
	
	List<CommunicationNote> findByCriteria(Date begindate, Date enddate, String[] source, 
			String[] receiver, CommunicationEventType[] type, CommunicationCategory[] category, 
			Class probeClass, Integer probeId, 
			int firstResult, int fetchsize, boolean isreadonly, String[] mappingsToJoin, String sqlFilter);
	
	List<CommunicationNote> getAll(int firstResult, int fetchsize,boolean isreadonly, String[] mappingsToJoin);
}
