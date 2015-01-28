package org.ird.unfepi;

import java.util.HashSet;

import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierStatus;
import org.ird.unfepi.model.Notifier.NotifierType;
import org.junit.Test;

public class NotifierTest {

	@Test
	public void testNotifier(){
		ServiceContext sc = Context.getServices();
		
		Notifier notifier = new Notifier();
		//notifier.setColumnsHeaderList("childId");
		notifier.setColumnsHeaderList("---,Total Enrollments,Total Enrollment BCG,Total Enrollment Penta-1");

		//daily at 11
		notifier.setNotifierCron("0 0 23 ? * *");
		notifier.setNotifierMessage("");
		notifier.setNotifierName("EnrollmentSummaryByVaccinationCenterCSV");
		notifier.setNotifierQuery("select (select programId from idmapper where mappedId=v.mappedId) centerId, "+
"name centerName,"+
"(select count(distinct childId) from vaccination where vaccinationCenterId=v.mappedId and date(vaccinationDuedate)=date(CURDATE())) totalEnrollments,"+
"(select count(*) from vaccination where vaccineId=1 and vaccinationCenterId = v.mappedId and date(vaccinationDuedate)=date(CURDATE())) BCGEnrollments,"+
"(select count(distinct childId) from vaccination where vaccineId=2 and childId not in (select childId from vaccination where vaccineId=1) and vaccinationCenterId=v.mappedId and date(vaccinationDuedate)=date(CURDATE())) Penta1Enrollments"+
" from vaccinationcenter v");
		
		HashSet<String> rec = new HashSet<String>();
		rec.add("saira.khowaja@irdresearch.org");
		rec.add("saba.morshed@irdresearch.org");
		rec.add("fahad.khan@irdresearch.org");
		rec.add("maimoona.kausar@irdinformatics.org");

		notifier.setNotifierRecipient(rec);
		notifier.setNotifierStatus(NotifierStatus.ACTIVE);
		notifier.setNotifierSubject("EPIUNFP[daily autogen PDF]: Summary Enrollment by Vaccination Center");
		notifier.setNotifierType(NotifierType.EMAIL_CSV);
		//notifier.setQueryDescription(queryDescription);
		
		sc.getCustomQueryService().save(notifier);
		sc.commitTransaction();
		sc.closeSession();
	}
}
