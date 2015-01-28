package org.ird.unfepi.beans;

import java.util.HashMap;
import java.util.Map;

import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Vaccination;

public class ImporterFieldMap {

	private static Map<String , Map<String , String>>	classes;

	public static Map<String , Integer> getMendatoryColumnMap(){
		Map<String , Integer> mandatoryColumns = new HashMap<String , Integer>();
		mandatoryColumns.put( "study_id" , -1 );
		mandatoryColumns.put( "name_child" , -1 );
		mandatoryColumns.put( "name_father" , -1 );
		mandatoryColumns.put( "gender" , -1 );
		mandatoryColumns.put( "dob_child" , -1 );
		mandatoryColumns.put( "age_child" , -1 );
		mandatoryColumns.put( "age_child_unit" , -1 );
		mandatoryColumns.put( "vacc" , -1 );
		mandatoryColumns.put( "vacc_oth" , -1 );
		mandatoryColumns.put( "allot_dat" , -1 );
		mandatoryColumns.put( "cell_num" , -1 );
		mandatoryColumns.put( "cell_sec_num" , -1 );
		mandatoryColumns.put( "remind_tim" , -1 );
		mandatoryColumns.put( "remind_tim_rang_st" , -1 );
		mandatoryColumns.put( "remind_tim_rang_st_unit" , -1 );
		mandatoryColumns.put( "remind_tim_rang_end" , -1 );
		mandatoryColumns.put( "remind_tim_rang_end_unit" , -1 );
		mandatoryColumns.put( "phone_num" , -1 );
		mandatoryColumns.put( "add_house" , -1 );
		mandatoryColumns.put( "add_street" , -1 );
		mandatoryColumns.put( "add_sector" , -1 );
		mandatoryColumns.put( "add_colony" , -1 );
		mandatoryColumns.put( "add_town" , -1 );
		mandatoryColumns.put( "add_uc" , -1 );
		mandatoryColumns.put( "add_landmark" , -1 );
		mandatoryColumns.put( "epi_reg" , -1 );
		mandatoryColumns.put( "comments" , -1 );
		mandatoryColumns.put( "date_enr" , -1 );
		return mandatoryColumns;
	}
	public static Map<String , String> getImporterFieldMap( String className ) {

		if (classes == null) {
			classes = new HashMap<String , Map<String , String>>();
			Map<String , String> childfieldMap = new HashMap<String , String>();
			childfieldMap.put( "study_id" , "childId" );
			childfieldMap.put( "name_child" , "firstName" );
			childfieldMap.put( "name_father" , "fatherName" );
			childfieldMap.put( "gender" , "gender" );
			childfieldMap.put( "dob_child" , "birthdate" );
			childfieldMap.put( "cell_num" , "currentCellNo" );
			childfieldMap.put( "cell_sec_num" , "alternateCellNo" );
			childfieldMap.put( "phone_num" , "phoneNo" );
			childfieldMap.put( "add_house" , "houseNum" );
			childfieldMap.put( "add_street" , "streetNum" );
			childfieldMap.put( "add_sector" , "sector" );
			childfieldMap.put( "add_colony" , "colony" );
			childfieldMap.put( "add_town" , "town" );
			childfieldMap.put( "add_uc" , "ucNum" );
			childfieldMap.put( "add_landmark" , "landmark" );
			childfieldMap.put( "epi_reg" , "mrNumber" );
			childfieldMap.put( "comments" , "description" );
			childfieldMap.put( "date_enr" , "dateEnrolled" );

			classes.put( Child.class.getName() , childfieldMap );

			Map<String , String> vaccfieldMap = new HashMap<String , String>();
			vaccfieldMap.put( "vacc" , "vaccineId" );
			vaccfieldMap.put( "vacc_oth" , "other_vaccine" );
			vaccfieldMap.put( "allot_dat" , "nextAssignedDate" );
			classes.put( Vaccination.class.getName() , vaccfieldMap );

			Map<String , String> remSmsfieldMap = new HashMap<String , String>();
			classes.put( ReminderSms.class.getName() , remSmsfieldMap );

		}
		return classes.get( className );
	}
}
