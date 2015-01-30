package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;

public interface DAONotifier extends DAO{

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	Notifier getById(int notifierId, boolean readonly, String[] mappingsToJoin);
	
	List<Notifier> findByCritria(String notifierName, NotifierType notifierType, NotifierStatus notifierStatus
			, boolean readonly, int firstResult, int fetchsize, String[] mappingsToJoin);
}
