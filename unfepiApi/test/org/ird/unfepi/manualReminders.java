package org.ird.unfepi;


public class manualReminders {

	/*@Test
	public void createReminders(){
		ServiceContext sc = Context.getServices();
		String query = "SELECT v.vaccinationRecordNum FROM unfepi.vaccination v " +
				" left join (select * from lotterysms lin where lin.datepreferencechanged=(select max(datepreferencechanged) from lotterysms where mappedid=lin.mappedid) " +
				"			and lin.createdDate=(select max(createdDate) from lotterysms where mappedid=lin.mappedid and date(datepreferencechanged)=date(lin.datepreferencechanged))) " +
				"		prf on v.childid=prf.mappedId " +
				" left join remindersms r on v.vaccinationrecordnum=r.vaccinationrecordnum " +
				" left join child c on v.childid=c.mappedid " +
				" where prf.hasApprovedReminders = true " +
				" and r.rsmsRecordNum is null " +
				" and v.vaccineid <> c.enrollmentvaccineid " +
				" and v.vaccinationDuedate >= curdate() order by vaccinationdate ";
		
		List list = sc.getCustomQueryService().getDataBySQL(query);
		for (Object object : list) {
			Vaccination vacc = sc.getVaccinationService().getVaccinationRecord(Integer.parseInt(object.toString()), true, null, null);
			List<ReminderSms> remindersmses = IMRUtils.createReminderSms(vacc, Globals.DEFAULT_ARM, null);
			
			for (ReminderSms reminderSms : remindersmses) {
				sc.getReminderService().addReminderSmsRecord(reminderSms);
			}
		}
		sc.commitTransaction();
		sc.closeSession();
	}*/
}
