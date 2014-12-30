package org.ird.unfepi.web.validator;

import java.util.List;
import java.util.Map;

public class DataFieldValidator {

	public Object NEW_EDITED_VALUE=null;
	public String ERROR_MESSAGE=null;
	public String WARNING_MESSAGE=null;
	public boolean IS_ERROR_IGNOREABLE=true;
	
	public boolean validateAndSetChildFieldValue(String fieldNameInFile,String fieldNameInClass, List<String> lineValues, Map<String, Integer> columnNameIndexes,Object classObject) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
		NEW_EDITED_VALUE=null;
		ERROR_MESSAGE=null;
		WARNING_MESSAGE=null;
		IS_ERROR_IGNOREABLE=true;
		
		return false;//TODO
		/*try{
			Child child=(Child)classObject;
		}
		catch (ClassCastException e) {
			ERROR_MESSAGE="expecting child field and object while setting "+fieldNameInFile+"-"+fieldNameInClass+". ";
			return false;
		}
		Class<Child> pcls = (Class<Child>) Class.forName("org.ird.immunizationreminder.datamodel.entities.Child");
		
		String value = null;
			value = lineValues.get( columnNameIndexes.get(fieldNameInFile) );
			value = value == null ? "" : value.replace("\"", "");
		
		if( fieldNameInClass.equalsIgnoreCase("childId") ) {
			EntityValidation evd = new EntityValidation();
			
			if( !evd.validateChildId(value , false) ){
				ERROR_MESSAGE = evd.ERROR_MESSAGE;
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			
			setChildFieldValue(pcls, classObject, "childId", value.trim());
			return true;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("firstName")) {
			if( !DataValidation.validate(REG_EX.NAME_CHARACTERS, value.trim()) ){
				ERROR_MESSAGE = "child`s first name is not valid.";
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			
			setChildFieldValue(pcls, classObject, "firstName", value.trim());
			return true;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("fatherName")) {
			if( !DataValidation.validate(REG_EX.NAME_CHARACTERS, value.trim()) ){
				ERROR_MESSAGE = "child`s father name is not valid.";
				return false;
			}
			
			setChildFieldValue(pcls, classObject, "fatherName", value.trim());
			return true;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("gender")) {
			try{
				int genVal=Integer.parseInt(value);
				if(genVal == 1){
					setChildFieldValue(pcls, classObject, "gender", Gender.MALE);
					return true;
				}
				if(genVal == 2){
					setChildFieldValue(pcls, classObject, "gender", Gender.FEMALE);
					return true;
				}
				//if none is found set default to male
				setChildFieldValue(pcls, classObject, "gender", Gender.MALE);
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			ERROR_MESSAGE = "child`s gender value must be 1 for Male or 2 for Female. default value MALE set for child.";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("birthdate")) {
			IS_ERROR_IGNOREABLE = false;
			if( !StringUtils.isEmptyOrWhitespaceOnly(value) ){
				SimpleDateFormat sd = new SimpleDateFormat();
				String[] dateFormats = WebGlobals.EXPECTED_CSV_DATE_FORMATS;
				boolean validated = false;
				Date date = null;
				
				for (String string : dateFormats) {
					sd.applyPattern(string);
					sd.setLenient(false);
					
					try{
						date = sd.parse(value.trim());
						validated = true;
						break;
					}
					catch (Exception e) {
						
					}
				}
				if(!validated){
					ERROR_MESSAGE = "format for date of birth could only be "+Arrays.toString(WebGlobals.EXPECTED_CSV_DATE_FORMATS)+". birthdate not set.";
					return false;
				}
				if(date != null && date.after(DateUtils.truncateDatetoDate(new Date()))){
					ERROR_MESSAGE="date of birth can not be after todays date. birthdate not set.";
					return false;
				}
				
				setChildFieldValue(pcls, classObject, "birthdate", date);
				return true;
			}
			else{
				String ageVal = lineValues.get(columnNameIndexes.get("age_child"));
				String ageUnitVal = lineValues.get(columnNameIndexes.get("age_child_unit"));
				
				if(!Utils.isNumberBetween(ageVal, 0, 300)){
					ERROR_MESSAGE = "age value must lie in the range 0-300 or provide date of birth. birthdate not set.";
					return false;
				}
				
				if(!Utils.isNumberBetween(ageUnitVal, 1, 2)){
					ERROR_MESSAGE = "age_unit must be 1 for weeks or 2 for months or provide date of birth. birthdate not set.";
					return false;
				}
				
				Calendar dob = Calendar.getInstance();
				try{
					if(Integer.parseInt(ageUnitVal) == 1){
						dob.add( Calendar.DATE , -( Integer.parseInt(ageVal)*7 ) );
					}
					
					if(Integer.parseInt(ageUnitVal) == 2){
						dob.add( Calendar.MONTH, -Integer.parseInt(ageVal) );
					}
				}
				catch (Exception e) {
					ERROR_MESSAGE = "age_unit must be 1 for weeks or 2 for months or provide date of birth. birthdate not set.";
					return false;
				}
				
				setChildFieldValue(pcls, classObject, "birthdate", dob.getTime());
				setChildFieldValue(pcls, classObject, "estimatedBirthdate", true);
				return true;
			}
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("currentCellNo")) {
			if( DataValidation.validate(REG_EX.CELL_NUMBER, value) ){
				setChildFieldValue(pcls, classObject, "currentCellNo", value.trim());
				return true;
			}
			else if( DataValidation.validate(REG_EX.PTCL_WIRELESS_NUMBER, value) ){
				WARNING_MESSAGE = "cell number was found to be "+value+". change immediately if unacceptable.";
				setChildFieldValue(pcls, classObject, "currentCellNo", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "cell number is missing or format is invalid ";
			IS_ERROR_IGNOREABLE = false;
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("alternateCellNo")) {
			if( StringUtils.isEmptyOrWhitespaceOnly(value)
					|| DataValidation.validate(REG_EX.CELL_NUMBER, value)){
				setChildFieldValue(pcls, classObject, "alternateCellNo", value.trim());
				return true;
			}
			else if( DataValidation.validate(REG_EX.PTCL_WIRELESS_NUMBER, value) ){
				WARNING_MESSAGE = "alternate cell number was found to be "+value+". change immediately if unacceptable.";
				setChildFieldValue(pcls, classObject, "alternateCellNo", value.trim());
				return true;
			}
			ERROR_MESSAGE = "alternate cell number format is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("phoneNo")) {
			if( StringUtils.isEmptyOrWhitespaceOnly(value)
					|| DataValidation.validate(REG_EX.PHONE_NUMBER, value) ){
				setChildFieldValue(pcls, classObject, "phoneNo", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "phone number format is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("houseNum")) {
			value = value.replace("'" , "`");
			value = value.replace("\"" , "`");
			
			if( StringUtils.isEmptyOrWhitespaceOnly(value) 
					|| DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, value) ){
				setChildFieldValue(pcls, classObject, "houseNum", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "house number is invalid";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("streetNum")) {
			value  =value.replace("'", "`");
			value = value.replace("\"", "`");
			
			if( StringUtils.isEmptyOrWhitespaceOnly(value) 
					|| DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, value) ){
				setChildFieldValue(pcls, classObject, "streetNum", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "street number is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("sector")) {
			value = value.replace("'", "`");
			value = value.replace("\"", "`");
			if(StringUtils.isEmptyOrWhitespaceOnly(value)||DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, value)){
				setChildFieldValue(pcls, classObject, "sector", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "sector is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase("colony")) {
			value = value.replace("'", "`");
			value = value.replace("\"", "`");
			if( StringUtils.isEmptyOrWhitespaceOnly(value)
					|| DataValidation.validate(REG_EX.NO_SPECIAL_CHAR, value) ){
				setChildFieldValue(pcls, classObject, "colony", value.trim());
				return true;
			}
			
			ERROR_MESSAGE = "colony is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "town" )) {
			value = value.replace( "'" , "`" );
			value = value.replace( "\"" , "`" );
			
			if (StringUtils.isEmptyOrWhitespaceOnly( value )
					|| DataValidation.validate( REG_EX.NO_SPECIAL_CHAR , value )) {
				setChildFieldValue( pcls , classObject , "town" , value.trim() );
				return true;
			}

			ERROR_MESSAGE = "town is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "ucNum" )) {
			value = value.replace( "'" , "`" );
			value = value.replace( "\"" , "`" );
			
			if (StringUtils.isEmptyOrWhitespaceOnly( value )
					|| DataValidation.validate( REG_EX.NO_SPECIAL_CHAR , value )) {
				setChildFieldValue( pcls , classObject , "ucNum" , value.trim() );
				return true;
			}
			
			ERROR_MESSAGE = "uc number is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "landmark" )) {
			value = value.replace( "'" , "`" );
			value = value.replace( "\"" , "`" );
			
			if (StringUtils.isEmptyOrWhitespaceOnly( value )
					|| DataValidation.validate( REG_EX.NO_SPECIAL_CHAR , value )) {
				setChildFieldValue( pcls , classObject , "landmark" , value.trim() );
				return true;
			}
			
			ERROR_MESSAGE = "landmark is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "mrNumber" )) {
			if (DataValidation.validate( REG_EX.NUMERIC , value.trim() , 8 , 8 )) {
				setChildFieldValue( pcls , classObject , "mrNumber" , value.trim() );
				return true;
			}
			
			IS_ERROR_IGNOREABLE = false;
			ERROR_MESSAGE = "epi register number is invalid ";
			return false;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "description" )) {
			value = value.replace( "'" , "`" );
			value = value.replace( "\"" , "`" );

			setChildFieldValue( pcls , classObject , "description" , value.trim() );
			return true;
		}
		
		else if (fieldNameInClass.equalsIgnoreCase( "dateEnrolled" )) {
			
			SimpleDateFormat sd = new SimpleDateFormat();
			String[] dateFormats = WebGlobals.EXPECTED_CSV_DATE_FORMATS;
			boolean validated = false;
			Date date = null;
			
			for ( String string : dateFormats ) {
				sd.applyPattern( string );
				sd.setLenient( false );
				
				try {
					date = sd.parse( value.trim() );
					validated = true;
					break;
				}
				catch ( Exception e ) {
				}
			}
			
			if(!validated){
				ERROR_MESSAGE = "date enrolled format is not valid. Only acceptable formats are "
						+ Arrays.toString( WebGlobals.EXPECTED_CSV_DATE_FORMATS )
						+ ". Please check date format before uploading csv.";
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			if (date != null
					&& date.after( DateUtils.truncateDatetoDate( new Date() ) )) {
				ERROR_MESSAGE = "date enrolled can not be after than todays date.";
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			
			setChildFieldValue( pcls , classObject , "dateEnrolled" , date );
			return true;
		}
		
		ERROR_MESSAGE = "No Field Found for :" + fieldNameInFile + ":" + fieldNameInClass;
		IS_ERROR_IGNOREABLE = false;
		return false;
	}
	
	public boolean getAndValidateFieldValue(String fieldName, List<String> lineValues
			, Map<String, Integer> columnNameIndexes,Object classObject,LoggedInUser user) {
		NEW_EDITED_VALUE=null;
		ERROR_MESSAGE=null;
		WARNING_MESSAGE=null;
		IS_ERROR_IGNOREABLE=true;
		
		if (fieldName.equalsIgnoreCase( "vaccine" )) {
			try {
				Vaccination vaccination = (Vaccination) classObject;
			}
			catch ( ClassCastException e ) {
				ERROR_MESSAGE = "vaccines can only be get/set for vaccination records only. " +
						"invalid object found for setting field :" + fieldName;
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			ServiceContext sc = Context.getServices(null);
			try {
				int vaccNumberInForm = Integer.parseInt( lineValues.get(columnNameIndexes.get( "vacc" ) ).replace( "\"" , "" ) );
				Vaccine vaccineO = sc.getVaccinationService().getByNumberInForm(vaccNumberInForm );

				if (vaccineO == null) {
					ERROR_MESSAGE = "no vaccine found in database with given vaccine id " + vaccNumberInForm;
					IS_ERROR_IGNOREABLE = false;
					return false;
				}
				
				if (vaccineO.getVaccineNameInForm().equalsIgnoreCase( "other" )) {
					String otherVaccName = lineValues.get( 
							columnNameIndexes.get( "vacc_oth" ) ).replace( "\"" , "" );
					
					if (StringUtils.isEmptyOrWhitespaceOnly( otherVaccName )) {
						ERROR_MESSAGE = "vacc_oth must be specified for vaccine option other.";
						IS_ERROR_IGNOREABLE = false;
						return false;
					}

					Vaccine newVaccine = sc.getVaccinationService().getByName( otherVaccName );
					if (newVaccine != null) {
						NEW_EDITED_VALUE = newVaccine;
						return true;
					}
					
					newVaccine = new Vaccine();
					newVaccine.setCreator( user.getUser() );
					newVaccine.setVaccineNumberInForm( 0 );
					newVaccine.setVaccineNameInForm( null );
					newVaccine.setName( otherVaccName.trim() );

					try {
						sc.getVaccinationService().addVaccine( newVaccine );
						sc.commitTransaction();
					}
					catch ( VaccinationDataException e ) {
						ERROR_MESSAGE = "setting vaccine threw exception." + e.getMessage();
						IS_ERROR_IGNOREABLE = false;
						return false;
					}
					
					newVaccine = sc.getVaccinationService().getByName(newVaccine.getName() );

					NEW_EDITED_VALUE = newVaccine;
					return true;
				}
				
				NEW_EDITED_VALUE = vaccineO;
				return true;
			}
			catch ( NumberFormatException e ) {
				ERROR_MESSAGE = "vacc number was not a valid vaccine id. ";
				IS_ERROR_IGNOREABLE = false;
				return false;
			}
			finally {
				sc.closeSession();
			}
		}
		
		else if (fieldName.equalsIgnoreCase( "reminder_time" )) {
			try {
				if (!Utils.isNumberBetween( lineValues.get(
						columnNameIndexes.get( "remind_tim" ) ).replace( "\"" , "" ) , 0 , 6 )) {
					ERROR_MESSAGE = "reminder time was not a valid integer between 1-6.";
					return false;
				}

				int remTimeNum = Integer.parseInt( lineValues.get(
						columnNameIndexes.get( "remind_tim" ) ).replace( "\"" , "" ) );
				Time time = new Time( System.currentTimeMillis() );

				if (remTimeNum == 2) {
					time = new Time( Utils.getRandomNumber( 9 , 18 ) , Utils
							.getRandomNumber( 2 , 55 ) , 12 );
				}

				if (remTimeNum == 3) {
					time = new Time( Utils.getRandomNumber( 7 , 8 ) , Utils
							.getRandomNumber( 2 , 55 ) , 12 );
				}

				if (remTimeNum == 4) {
					time = new Time( Utils.getRandomNumber( 12 , 14 ) , Utils
							.getRandomNumber( 2 , 55 ) , 12 );
				}

				if (remTimeNum == 5) {
					time = new Time( Utils.getRandomNumber( 17 , 20 ) , Utils
							.getRandomNumber( 2 , 55 ) , 12 );
				}

				if (remTimeNum == 6 || remTimeNum == 1) {
					int timeRangStart;
					int timeRangeEnd;

					try {
						timeRangStart = Integer.parseInt( lineValues.get(
								columnNameIndexes.get( "remind_tim_rang_st" ) )
								.replace( "\"" , "" ).substring( 0 , 2 ) );
						if (Integer.parseInt( lineValues.get(
								columnNameIndexes
										.get( "remind_tim_rang_st_unit" ) )
								.replace( "\"" , "" ) ) == 1) {
							// do nothing
						}
						else if (Integer.parseInt( lineValues.get(
								columnNameIndexes
										.get( "remind_tim_rang_st_unit" ) )
								.replace( "\"" , "" ) ) == 2) {
							timeRangStart = timeRangStart + 12;
						}
						else {
							throw new Exception(
									"remind_tim_rang_st_unit found to be invalid" );
						}

						if (timeRangStart <= 0 || timeRangStart > 24) {
							ERROR_MESSAGE = "reminder time start out of range .";
							return false;
						}

						if (timeRangStart == 24) {
							timeRangStart = 00;
						}
					}
					catch ( Exception e ) {
						ERROR_MESSAGE = "error in time range start check if time values missing!"
								+ e.getMessage();
						return false;
					}

					try {
						timeRangeEnd = Integer.parseInt( lineValues.get(
								columnNameIndexes.get( "remind_tim_rang_end" ) )
								.replace( "\"" , "" ).substring( 0 , 2 ) );
						if (Integer.parseInt( lineValues.get(
								columnNameIndexes
										.get( "remind_tim_rang_end_unit" ) )
								.replace( "\"" , "" ) ) == 1) {
							// do nothing
						}
						else if (Integer.parseInt( lineValues.get(
								columnNameIndexes
										.get( "remind_tim_rang_end_unit" ) )
								.replace( "\"" , "" ) ) == 2) {
							timeRangeEnd = timeRangeEnd + 12;
						}
						else {
							throw new Exception(
									"remind_tim_rang_end_unit found to be invalid" );
						}

						if (timeRangeEnd <= 0 || timeRangeEnd > 24) {
							ERROR_MESSAGE = "reminder time end out of range .";
							return false;
						}

						if (timeRangeEnd == 24) {
							timeRangeEnd = 00;
						}
					}
					catch ( Exception e ) {
						ERROR_MESSAGE = "error in time range end values."
								+ e.getMessage();
						return false;
					}

					if (timeRangStart > timeRangeEnd) {
						ERROR_MESSAGE = "error in time range start is greater than time range end.";
						return false;
					}

					NEW_EDITED_VALUE = time = new Time( Utils.getRandomNumber(
							timeRangStart , timeRangeEnd ) , 0 , 12 );
				}

				NEW_EDITED_VALUE = time;
				return true;
			}
			catch ( Exception e ) {
				ERROR_MESSAGE = "error in time range values." + e.getMessage();
				return false;
			}
		}
		return false;
	}

	public boolean validateAndSetVacciantionFieldValue( String fieldNameInFile ,
			String fieldNameInClass , List<String> lineValues ,
			Map<String , Integer> columnNameIndexes , Object classObject )
			throws IllegalArgumentException , SecurityException ,
			IllegalAccessException , NoSuchFieldException ,
			ClassNotFoundException {
		NEW_EDITED_VALUE = null;
		ERROR_MESSAGE = null;
		WARNING_MESSAGE = null;
		IS_ERROR_IGNOREABLE = true;

		try {
			Vaccination vaccination = (Vaccination) classObject;
		}
		catch ( ClassCastException e ) {
			e.printStackTrace();
			ERROR_MESSAGE = "expecting vaccination field and object while setting "
					+ fieldNameInClass + "-" + fieldNameInFile;
			return false;
		}

		Class<Vaccination> vaccls = (Class<Vaccination>) Class
				.forName( "org.ird.immunizationreminder.datamodel.entities.Vaccination" );

		String value = null;
		value = lineValues.get( columnNameIndexes.get( fieldNameInFile ) )
				.replace( "\"" , "" );
		value = value == null ? "" : value;

		if (fieldNameInClass.equalsIgnoreCase( "nextAssignedDate" )) {
			SimpleDateFormat sd = new SimpleDateFormat();
			String[] dateFormats = WebGlobals.EXPECTED_CSV_DATE_FORMATS;
			boolean validated = false;
			Date date = null;
			for ( String string : dateFormats ) {
				sd.applyPattern( string );
				sd.setLenient( false );
				try {
					date = sd.parse( value.trim() );
					validated = true;
					break;
				}
				catch ( Exception e ) {
				}
			}
			if (validated) {
				Calendar cal = Calendar.getInstance();
				cal.setTime( date );
				cal.add( Calendar.DATE , -4 );
				if (cal.getTime().before( new Date() )) {
					ERROR_MESSAGE = "next Assigned Date must be atleast after 4/5 days of today`s date.";
					setVaccinationFieldValue( vaccls , classObject ,
							"nextAssignedDate" , date );
					return false;
				}
				setVaccinationFieldValue( vaccls , classObject ,
						"nextAssignedDate" , date );
				return true;
			}
			ERROR_MESSAGE = "allot_date format is not valid. only acceptable formats are "
					+ Arrays.toString( WebGlobals.EXPECTED_CSV_DATE_FORMATS )
					+ ".";
			return false;
		}
		ERROR_MESSAGE = "no field found.";
		IS_ERROR_IGNOREABLE = false;
		return false;
	}

	private void setChildFieldValue( Class<Child> pcls , Object object ,
			String fieldNameToSet , Object value )
			throws IllegalArgumentException , IllegalAccessException ,
			SecurityException , NoSuchFieldException {
		Field[] allFields = pcls.getDeclaredFields();
		for ( Field field : allFields ) {
			if (field.getName().equalsIgnoreCase( fieldNameToSet )) {
				field.setAccessible( true );

				field.set( object , value );
				break;
			}
		}
	}

	private void setVaccinationFieldValue( Class<Vaccination> vaccls ,
			Object object , String fieldNameToSet , Object value )
			throws IllegalArgumentException , IllegalAccessException ,
			SecurityException , NoSuchFieldException {
		Field[] allFields = vaccls.getDeclaredFields();
		for ( Field field : allFields ) {
			if (field.getName().equalsIgnoreCase( fieldNameToSet )) {
				field.setAccessible( true );

				field.set( object , value );
				break;
			}
		}*/
	}
}
