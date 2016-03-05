package org.ird.unfepi.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ReminderSms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.utils.UserSessionUtils;

public class UploadDataServlet extends HttpServlet {

	public static String	CSV_REPORT_FIELD_SEPARATOR	= "\t>>";

	@Override
	protected void doGet( HttpServletRequest req , HttpServletResponse resp )
			throws ServletException , IOException {
		doPost( req , resp );
	}

	@Override
	protected void doPost( HttpServletRequest req , HttpServletResponse resp )
			throws ServletException , IOException {
		/*
		 * //All Columns"\"ID\"","\"study_id\"","\"relation\""
		 * ,"\"relation_oth\""
		 * ,"\"name_child\"","\"name_father\"","\"name_mother\""
		 * ,"\"gender\"","\"dob_child\""
		 * ,"\"age_child\"","\"age_child_unit\"","\"vacc\""
		 * ,"\"vacc_oth\"","\"allot_dat\"","\"breastfeed\"","\"transport\""
		 * ,"\"transport_oth\"","\"rch_time\"","\"ethn\"","\"bro\"","\"sis\""
		 * ,"\"age_father\""
		 * ,"\"age_mother\"","\"occup_father\"","\"occup_mother\""
		 * ,"\"edu_father\"","\"edu_mother\"","\"ppl_hom\"","\"cell_prim\""
		 * ,"\"name_oth_cell\"","\"relation_oth_cell\"","\"cell_num\"","\"sms\""
		 * ,
		 * "\"sms_oth\"","\"services\"","\"remind_tim\"","\"remind_tim_rang_st\""
		 * ,"\"remind_tim_rang_st_unit\"","\"remind_tim_rang_end\""
		 * ,"\"remind_tim_rang_end_unit\"","\"cell_sec\"","\"name_sec_cell\""
		 * ,"\"relation_sec_cell\""
		 * ,"\"cell_sec_num\"","\"phone_num\"","\"add_house\""
		 * ,"\"add_street\"",
		 * "\"add_sector\"","\"add_colony\"","\"add_town\"","\"add_uc\""
		 * ,"\"add_landmark\"","\"epi_reg\"","\"comments\""
		 */

		LoggedInUser user = UserSessionUtils.getActiveUser( req );
		if (user == null) {
			resp.sendRedirect( req.getContextPath() + "/login.htm" );
			return;
		}
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent( req );
/*		if (isMultipart) {
			List<String> uploadErrorMessage = new ArrayList<String>();

			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold( 2097152 );// 2MB
			File tempDir = new File( Context.getIRSetting( "logs.root-path" , "" ) );

			File fileTempRep = new File( tempDir.getPath() + WebGlobals.UPLOAD_TEMP_DIR );

			try {
				if (fileTempRep.exists()) {
					fileTempRep.mkdirs();
				}
				factory.setRepository( fileTempRep );
			}
			catch ( Exception e ) {
				uploadErrorMessage.add( "\nerror occurred while creating repository for temporary dir." + e.getMessage() );
				LoggerUtil.logIt( "Error occurred while creating repository for upload temporary dir :"
								+ ExceptionUtil.getStackTrace( e ) );
			}
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload( factory );

			// Set overall request size constraint
			// /////upload.setSizeMax(yourMaxRequestSize);

			// Parse the request

			List  FileItem items;
			try {
				 FileItem items = upload.parseRequest( req );
			}
			catch ( FileUploadException e1 ) {
				uploadErrorMessage.add( "\nerror occurred in request." + e1.getMessage() );
				req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
				resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
				return;
			}

			FileItem fileInputItem = null;
			//boolean rejectAll = false;

			String clinic = null;
			String programEnrolled = null;

			// Process the uploaded items
			Iterator iter = items.iterator();
			while ( iter.hasNext() ) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase( "programEnrolled" )) {
						programEnrolled = item.getString();
					}
					else if (item.getFieldName().equalsIgnoreCase( "center" )) {
						clinic = item.getString();
					}
					else if (item.getFieldName().equalsIgnoreCase( "rejectAll" )) {
						//rejectAll = !StringUtils.isEmptyOrWhitespaceOnly( item.getString() ) ? true : false;
					}
				}
				else {
					fileInputItem = item;
				}
			}
			Map<String , Integer> mandatoryColumnsIndexMap = ImporterFieldMap.getMendatoryColumnMap();

			List<String> columnNamesList = new ArrayList<String>();
			List<DataObjects> dataObjects = new ArrayList<DataObjects>();

			ServiceContext sc = Context.getServices();

			String line = null;
			try {
				BufferedReader input = new BufferedReader(
						new InputStreamReader( fileInputItem.getInputStream() ));
				boolean rejectFile = false;
				// first verify if all mandatory columns are specified
				while ( true ) {
					line = input.readLine();
					
					if (line == null) {
						uploadErrorMessage.add( "\nfile was found to be empty." );
						req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
						resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
						return;
					}
					
					if (!StringUtils.isEmptyOrWhitespaceOnly( line )) {
						String[] columnNames = line.split( WebGlobals.CSV_DELIMITER_CHARACTER , -1 );
						columnNamesList = new ArrayList<String>( Arrays.asList( columnNames ) );
						
						for ( String string : mandatoryColumnsIndexMap.keySet() ) {
							if (columnNamesList.indexOf( "\"" + string + "\"" ) == -1) {
								rejectFile = true;
								uploadErrorMessage.add( "\ncolumn not found in file :" + string );
							}
							else {
								mandatoryColumnsIndexMap.put( string , columnNamesList.indexOf( "\"" + string + "\"" ) );
							}
						}
						
						for ( String string : columnNamesList ) {
							if (StringUtils.isEmptyOrWhitespaceOnly( string )) {
								rejectFile = true;
								uploadErrorMessage.add( "\na column name was found to be empty or whitespace in file :"
												+ string );
								req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
								resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
								return;
							}
						}
						
						if (rejectFile) {
							uploadErrorMessage.add( 0 ,
									"\n----file was not parsed because of errors.----".toUpperCase() );
							req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
							resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
							return;
						}
						break;// if file not rejected then read file
					}
				}
				
				int numberOfDataRows = 0;
				boolean isAnyLineRejected = false;
				while ( ( line = input.readLine() ) != null ) {
					// if line is not empty
					if (!StringUtils.isEmptyOrWhitespaceOnly( line )) {
						Child child 					= new Child();
						List<ReminderSms> nvcreminderSms	= new ArrayList<ReminderSms>();
						List<ReminderSms> measlesreminderSms	= new ArrayList<ReminderSms>();
						Vaccination vaccination 		= new Vaccination();
						Vaccination nextVaccination 	= new Vaccination();
						Vaccination measlesVaccination  = new Vaccination();

						List<String> lineValues = new ArrayList<String>(
								Arrays.asList( line.split(WebGlobals.CSV_DELIMITER_CHARACTER ,-1 ) ) 
								);

						if (lineValues.size() != columnNamesList.size()) {
							isAnyLineRejected = true;// ///in real this error
														// will never occur
							uploadErrorMessage.add( "\n--#-- line should have number of column values("
											+ lineValues.size()
											+ ") equal to number of specified columns("
											+ columnNamesList.size()
											+ ") in file : line is :"
											+ line.substring( 0 , line.length() < 60 ? line.length() : 60 )
											+ "...................." );
						}
						else {
							boolean isErrorIgnoreable = true;
							List<String> errMsg = new ArrayList<String>();
							DataFieldValidator validator = new DataFieldValidator();

							child.setClinic( clinic );
							child.setCreator( user.getUser() );
							child.setHasCompleted( false );
							child.setProgrammeEnrolled( programEnrolled );
							child.setStatus(Child.STATUS.FOLLOW_UP );
							
							for ( String fieldNameInFile 
									: ImporterFieldMap.getImporterFieldMap( Child.class.getName() ).keySet() ) {
								if (!validator.validateAndSetChildFieldValue(
										fieldNameInFile , ImporterFieldMap.getImporterFieldMap(Child.class.getName() ).get( fieldNameInFile ) ,
										lineValues , mandatoryColumnsIndexMap , child )) {
									isAnyLineRejected = true;
									// uploadErrorMessage.add("\n--#-- "+validator.ERROR_MESSAGE);
									// uploadErrorMessage.add("in line "+line.substring(0,line.length()<60?line.length():60)+"....................");
									errMsg.add( validator.ERROR_MESSAGE );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = validator.IS_ERROR_IGNOREABLE;
									}
								}
								else if (validator.WARNING_MESSAGE != null) {
									errMsg.add( validator.WARNING_MESSAGE );
								}
							}
							if (child.getDateEnrolled() != null
									&& child.getMrNumber() != null) {
								EntityValidation ev = new EntityValidation();
								if(!ev.validateMrNumber(child)){
									if (isErrorIgnoreable) {
										isErrorIgnoreable = false;
										errMsg.add( ev.ERROR_MESSAGE );
									}
								}
							}
							
							boolean isChildExisting = false;
							try {
								Child c = sc.getChildService().getChildbyChildId(child.getChildId() , true );
								
								if (c != null) {
									errMsg.add( 0 , "child id already exists.".toUpperCase() );
									isErrorIgnoreable = false;
									isChildExisting = true;
								}
								else {
									Child c2 = sc.getChildService().getChildbyCurrentCell(child.getCurrentCellNo() , true );
									if (c2 != null) {
										errMsg.add( 0 ,( "current cell number occupied by another child.("
																+ c2.getChildId()
																+ " : "
																+ c2.getFirstName() + ")" ).toUpperCase() );
										isErrorIgnoreable = false;
									}
									else {
										List<Child> c3 = sc.getChildService().findByEpiOrMrNumber( child.getMrNumber() );
										if (c3.size() > 0) {
											errMsg.add( 0 , "epi reg number occupied.".toUpperCase() );
											isErrorIgnoreable = false;
										}
									}
								}
							}
							catch ( Exception e ) {
								e.printStackTrace();
								errMsg.add( 0 , e.getMessage() );
							}
							if (!isChildExisting) {
								try {
									Arm arm = sc.getArmService().findByChildIdToMap(Integer.parseInt( child.getChildId() ) ).getArm();
									child.setArm( arm );
								}
								catch ( Exception e ) {
									e.printStackTrace();
									isAnyLineRejected = true;
									// uploadErrorMessage.add("\nArm for child '"+child.getChildId()+"' was not set. Error is :"+e.getMessage());
									// uploadErrorMessage.add("in line "+line.substring(0,line.length()<60?line.length():60)+"....................");
									errMsg.add( "arm for child '" + child.getChildId()
											+ "' was not set. error is :" + e.getMessage() );
									if (isErrorIgnoreable) {
										isErrorIgnoreable = false;
									}
								}

								vaccination.setChildResponded( false );
								vaccination.setCreator( user.getUser() );
								vaccination.setIsLastVaccination( false );
								vaccination.setIsFirstVaccination( true );
								// //for first vaccine vaccinationduedate set to
								// vaccination date
								// //to avoid nulls in data
								vaccination.setVaccinationDuedate( child.getDateEnrolled() );
								// //have to look into it
								vaccination.setVaccinationDate( child.getDateEnrolled() );
								vaccination.setVaccinationStatus( VACCINATION_STATUS.VACCINATED );

								if (!validator.getAndValidateFieldValue("vaccine" , lineValues ,
										mandatoryColumnsIndexMap , vaccination , user )) {
									isAnyLineRejected = true;
									// uploadErrorMessage.add("\n"+validator.ERROR_MESSAGE);
									// uploadErrorMessage.add("in line "+line.substring(0,line.length()<60?line.length():60)+"....................");
									errMsg.add( validator.ERROR_MESSAGE );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = validator.IS_ERROR_IGNOREABLE;
									}
								}
								else {
									vaccination.setVaccine( (Vaccine) validator.NEW_EDITED_VALUE );
									if (validator.WARNING_MESSAGE != null) {
										errMsg.add( validator.WARNING_MESSAGE );
									}
								}

								if (!validator.validateAndSetVacciantionFieldValue(
												"allot_dat" ,
												"nextAssignedDate" ,
												lineValues , mandatoryColumnsIndexMap ,
												vaccination )) {
									isAnyLineRejected = true;
									// uploadErrorMessage.add("\n"+validator.ERROR_MESSAGE);
									// uploadErrorMessage.add("in line "+line.substring(0,line.length()<60?line.length():60)+"....................");
									errMsg.add( validator.ERROR_MESSAGE );
								}
								else if (validator.WARNING_MESSAGE != null) {
									errMsg.add( validator.WARNING_MESSAGE );
								}
								if (vaccination.getVaccinationDate() != null
										&& vaccination.getVaccine() != null) {

									Date actnextAssgnDate = IMRUtils.calcNextVaccDuedate(
													vaccination.getVaccinationDate() ,
													vaccination.getVaccine() ,
													VACCINATION_GAP.USE_GAP_TO_NEXT_VACCINE_FIELD ,
													true );
									if (!StringUtils.isEmptyOrWhitespaceOnly(IMRUtils.WARNING_MESSAGE)) {
										errMsg.add( IMRUtils.WARNING_MESSAGE );
									}

									if (vaccination.getNextAssignedDate() == null) {
										errMsg.add( "allot_date for next vaccine was found to be null/invalid in file, hence assigning system calculated date "
														+ actnextAssgnDate );
									}
									else if (!DateUtils.datesEqual( vaccination.getNextAssignedDate() , actnextAssgnDate )) {
										errMsg.add( "allot_date ("
														+ vaccination.getNextAssignedDate()
														+ ") for next vaccine was overridden by system calculated date ("
														+ actnextAssgnDate
														+ ")" );
									}
									vaccination.setNextAssignedDate( actnextAssgnDate );
								}
								else {
									errMsg.add( "some problem occurred. check in csv if correct date enrolled and vaccine id is provided." );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = false;
									}
								}

								nextVaccination.setCreator( user.getUser() );
								nextVaccination.setIsFirstVaccination( false );
								nextVaccination.setIsLastVaccination( false );
								nextVaccination.setVaccine(sc.getVaccinationService().getByName((vaccination.getVaccine().getName().contains("bcg")?"Penta1/OPV":"Penta2/OPV")));

								if (vaccination.getNextAssignedDate() != null) {
									Calendar pvduedate = Calendar.getInstance();
									
									pvduedate.setTime( vaccination.getNextAssignedDate() );// should be
																		// sure that vacc due date
																		// will not be null.
									if (pvduedate.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY) {
										pvduedate.add( Calendar.DATE , 1 );
									}
									nextVaccination.setVaccinationDuedate( pvduedate.getTime() );
								}
								else {
									errMsg.add( "next assigned date not calculated. check in csv if correct date_enrolled is provided." );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = false;
									}
								}

								nextVaccination.setVaccinationStatus( VACCINATION_STATUS.PENDING );

								Time userTime = null;

								if (!validator.getAndValidateFieldValue( "reminder_time" , lineValues ,
													mandatoryColumnsIndexMap , vaccination , user )) {
									isAnyLineRejected = true;
									// uploadErrorMessage.add("\n"+validator.ERROR_MESSAGE);
									// uploadErrorMessage.add("in line "+line.substring(0,line.length()<60?line.length():60)+"....................");
									errMsg.add( validator.ERROR_MESSAGE );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = validator.IS_ERROR_IGNOREABLE;
									}
								}
								else {
									userTime = (Time) validator.NEW_EDITED_VALUE;
									
									if (validator.WARNING_MESSAGE != null) {
										errMsg.add( validator.WARNING_MESSAGE );
									}
								}
								try {
									for ( ArmDayReminder armdayrem : child.getArm().getArmday() ) {
										ReminderSms remSms = new ReminderSms();
										remSms.setCreator( user.getUser() );
										if (userTime != null
												&& armdayrem.getIsDefaultTimeEditable()) {
											remSms.setDueTime( userTime );
										}
										else {
											remSms.setDueTime( armdayrem.getDefaultReminderTime() );
										}
										
										if (nextVaccination.getVaccinationDuedate() != null) {
											Calendar cal = Calendar.getInstance();
											cal.setTime( nextVaccination.getVaccinationDuedate() );
											cal.set( Calendar.HOUR_OF_DAY ,remSms.getDueTime().getHours() );
											cal.set( Calendar.MINUTE , remSms.getDueTime().getMinutes() );
											cal.set( Calendar.SECOND , remSms.getDueTime().getSeconds() );
											cal.add( Calendar.DATE , armdayrem.getId().getDayNumber() );
											
											remSms.setDueDate( cal.getTime() );
										}
										else {
											errMsg.add( "next vaccination due date not calculated. check in csv if correct date_enrolled is provided." );
											
											if (isErrorIgnoreable) {
												isErrorIgnoreable = false;
											}
										}

										remSms.setStatus( REMINDER_STATUS.PENDING );
										remSms.setDayNumber( armdayrem.getId().getDayNumber() );

										nvcreminderSms.add( remSms );
									}
								}
								catch ( Exception e ) {
									e.printStackTrace();
								}
								measlesVaccination.setChild(child);
								measlesVaccination.setCreator(user.getUser());
								measlesVaccination.setIsFirstVaccination(false);
								measlesVaccination.setIsLastVaccination(false);
								measlesVaccination.setPreviousVaccinationRecordNum(0);
								if (child.getBirthdate() != null) {
									Calendar md = Calendar.getInstance();
									
									md.setTime( child.getBirthdate() );// should be sure that vacc due date
																		// will not be null.
									md.add( Calendar.MONTH , 9 );
									if (md.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY) {
										md.add( Calendar.DATE , 1 );
									}
									measlesVaccination.setVaccinationDuedate( md.getTime() );
								}
								else {
									errMsg.add( "measles1 due date not calculated. check in csv if correct date of birth for child is provided." );
									
									if (isErrorIgnoreable) {
										isErrorIgnoreable = false;
									}
								}
								
								try {
									for ( ArmDayReminder armdayrem : child.getArm().getArmday() ) {
										ReminderSms remSms = new ReminderSms();
										remSms.setCreator( user.getUser() );
										if (userTime != null
												&& armdayrem.getIsDefaultTimeEditable()) {
											remSms.setDueTime( userTime );
										}
										else {
											remSms.setDueTime( armdayrem.getDefaultReminderTime() );
										}
										
										if (measlesVaccination.getVaccinationDuedate() != null) {
											Calendar cal = Calendar.getInstance();
											cal.setTime( measlesVaccination.getVaccinationDuedate() );
											cal.set( Calendar.HOUR_OF_DAY ,remSms.getDueTime().getHours() );
											cal.set( Calendar.MINUTE , remSms.getDueTime().getMinutes() );
											cal.set( Calendar.SECOND , remSms.getDueTime().getSeconds() );
											cal.add( Calendar.DATE , armdayrem.getId().getDayNumber() );
											
											remSms.setDueDate( cal.getTime() );
										}
										else {
											errMsg.add( "measles1 due date not calculated. check in csv if correct birthdate is provided." );
											
											if (isErrorIgnoreable) {
												isErrorIgnoreable = false;
											}
										}

										remSms.setStatus( REMINDER_STATUS.PENDING );
										remSms.setDayNumber( armdayrem.getId().getDayNumber() );

										measlesreminderSms.add( remSms );
									}
								}
								catch ( Exception e ) {
									e.printStackTrace();
								}
								measlesVaccination.setVaccinationStatus(VACCINATION_STATUS.PENDING);
								measlesVaccination.setVaccine(sc.getVaccinationService().getByName("measles1"));
								if(measlesVaccination.getVaccine() == null){
									errMsg.add( "measles1 was not properly set. plz edit the record manually by navigating to Edit Vaccination and finding vaccine scheduled after 9 months of birthdate for this child." );
								}
							}
							
							dataObjects.add( new DataObjects( child , vaccination ,nextVaccination , nvcreminderSms ,
									measlesVaccination, measlesreminderSms,line , errMsg , isErrorIgnoreable ) );
						}
					}
					numberOfDataRows++;
				}
				
				List<String> savedlines = new ArrayList<String>();
				List<String> unsavedlines = new ArrayList<String>();
				SimpleDateFormat sd = new SimpleDateFormat( "MMM dd, yyyy" );

				String currentData = "";
				try {
					for ( DataObjects dobj : dataObjects ) {
						if (dobj.isLineErrorIgnoreable()) {
							currentData = " line :" + dobj.getLine().substring(
											0 , dobj.getLine().length() < 60 ? dobj.getLine().length() : 60 )
											+ "....................";

							try {
								sc.getChildService().addChild( dobj.getChilddo() );
								
								dobj.getCurVaccinationdo().setChild(dobj.getChilddo() );
								dobj.getCurVaccinationdo().setPreviousVaccinationRecordNum( 0 );
								
								Serializable vid = sc.getVaccinationService().addVaccinationRecord(dobj.getCurVaccinationdo() );
								
								dobj.getNextVaccinationdo().setChild(dobj.getChilddo() );
								dobj.getNextVaccinationdo().setPreviousVaccinationRecordNum(Long.parseLong( vid.toString() ) );
								
								Serializable nxtvid = sc.getVaccinationService().addVaccinationRecord(dobj.getNextVaccinationdo() );
								
								for ( ReminderSms remsms : dobj.getNxvcReminderSmsdo()) {
									remsms.setChild( dobj.getChilddo() );
									remsms.setVaccinationRecordNum( Long.parseLong( nxtvid.toString() ) );
									
									sc.getReminderService().addReminderSmsRecord( remsms );
								}
								
								dobj.getCurVaccinationdo().setNextVaccinationRecordNum( Long.parseLong( nxtvid.toString() ) );
								
								sc.getVaccinationService().updateVaccinationRecord( dobj.getCurVaccinationdo() );

								dobj.getMeaslesVaccinationdo().setChild(dobj.getChilddo());
								Serializable mvid = sc.getVaccinationService().addVaccinationRecord(dobj.getMeaslesVaccinationdo());
								
								for ( ReminderSms remsms : dobj.getMeaslesReminderSmsdo() ) {
									remsms.setChild( dobj.getChilddo() );
									remsms.setVaccinationRecordNum( Long.parseLong( mvid.toString() ) );
									
									sc.getReminderService().addReminderSmsRecord( remsms );
								}
								sc.commitTransaction();

								savedlines.add( ( dobj.getErrorAsText() == "" ? "NIL"
												: dobj.getErrorAsText() )
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getChilddo().getChildId()
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getChilddo().getFirstName()
												+ CSV_REPORT_FIELD_SEPARATOR
												+ ( dobj.getChilddo().getDateEnrolled() == null ? null
														: sd.format( dobj.getChilddo().getDateEnrolled() ) )
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getLine() );

							}
							catch ( ChildDataException e ) {
								e.printStackTrace();
								// uploadErrorMessage.add("\nAdding child threw exception : "+e.getMessage());
								// uploadErrorMessage.add("\n--In line : "+dobj.getLine().substring(0,dobj.getLine().length()<60?dobj.getLine().length():60)+"....................");
								
								 * if(rejectAll){ throw new
								 * Exception(e.getMessage()+
								 * " . Reject All Data is checked, hence leaving data with out saving any entry into database."
								 * ); }
								 
								unsavedlines.add( e.getMessage()
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getChilddo().getChildId()
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getChilddo().getFirstName()
												+ CSV_REPORT_FIELD_SEPARATOR
												+ ( dobj.getChilddo().getDateEnrolled() == null ? null
														: sd.format( dobj.getChilddo().getDateEnrolled() ) )
												+ CSV_REPORT_FIELD_SEPARATOR
												+ dobj.getLine() );
							}
						}
						else {
							unsavedlines.add( dobj.getErrorAsText()
											+ CSV_REPORT_FIELD_SEPARATOR
											+ dobj.getChilddo().getChildId()
											+ CSV_REPORT_FIELD_SEPARATOR
											+ dobj.getChilddo().getFirstName()
											+ CSV_REPORT_FIELD_SEPARATOR
											+ ( dobj.getChilddo().getDateEnrolled() == null ? null
													: sd.format( dobj.getChilddo().getDateEnrolled() ) )
											+ CSV_REPORT_FIELD_SEPARATOR
											+ dobj.getLine() );
						}
					}
				}
				catch ( Exception e ) {
					e.printStackTrace();
					uploadErrorMessage.add( "\nAn error occurred while saving '"
									+ currentData
									+ "'. Plz see if data is polluted by half executed queries.\nError is :"
									+ e.getMessage() );
					
					req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
					resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
					return;
				}
				
				List<String> copyUnsavedlines = new ArrayList<String>();
				for ( int i = unsavedlines.size() ; i > 0 ; i-- ) {
					copyUnsavedlines.add( unsavedlines.get( i - 1 ) );
				}
				
				CsvUpload csvdata = new CsvUpload();
				String dataFilePath = Context.getIRSetting("csv.uploaded-file.file-name.path-pattern"+new Date().getTime()+".csv"
						, System.getProperty("user.home")+"/imrscsvfiles/datafiles/csv"+new Date().getTime()+".csv");
				try{
					File file = new File(dataFilePath);
					if(!file.exists()){
						File par = new File(file.getParent());
						if(!par.exists()){
							par.mkdirs();
						}
					}
					OutputStream os = new FileOutputStream(file);
					os.write(Utils.convertStreamToStringBuilder(fileInputItem.getInputStream() ).toString().getBytes());
					os.flush();
					os.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					uploadErrorMessage.add("CSV file was not saved due to error "+ e.getMessage());
				}
				csvdata.setCsvFile( dataFilePath );
				csvdata.setDateUploaded( new Date() );
				csvdata.setNumberOfRows( numberOfDataRows );
				csvdata.setNumberOfRowsRejected( unsavedlines.size() );
				csvdata.setNumberOfRowsSaved( savedlines.size() );
				csvdata.setUploader( user.getUser() );
				csvdata.setUploadErrors( Utils.getListAsString( uploadErrorMessage , "\n" ) );
				String reportFilePath = Context.getIRSetting("csv.upload-report.file-name.path-pattern"+new Date().getTime()+".txt"
						, System.getProperty("user.home")+"/imrscsvfiles/uploadreport/report"+new Date().getTime()+".txt");
				try {
					File file = new File(reportFilePath);
					if (!file.exists()) {
						File par = new File(file.getParent());
						if (!par.exists()) {
							par.mkdirs();
						}
					}
					OutputStream os = new FileOutputStream(file);
					os.write(CSVUtil.getCsvUploadReport(uploadErrorMessage, savedlines,	copyUnsavedlines).toString().getBytes());
					os.flush();
					os.close();
				}
				catch (Exception e) {
					e.printStackTrace();
					uploadErrorMessage.add("error report file was not saved due to error "+ e.getMessage());
				}
				csvdata.setUploadReport( reportFilePath);
				
				try {
					sc.getTransactionLogService().saveCsvUpload( csvdata );
					sc.commitTransaction();
				}
				catch ( Exception e ) {
					LoggerUtil.logIt( ExceptionUtil.getStackTrace( e ) );
				}
				
				 * uploadErrorMessage.add("\nData upload ended, See results for details.."
				 * ); for (String string : savedlines) {
				 * uploadErrorMessage.add("\n"+string); }
				 
				req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
				req.getSession().setAttribute( "savedlines" , savedlines );
				req.getSession().setAttribute( "unsavedlines" , copyUnsavedlines );

			}
			catch ( Exception e ) {
				uploadErrorMessage.add( "\nData upload faild with errors : " + e.getMessage() );
				req.getSession().setAttribute( "uploadDataMessage" , uploadErrorMessage );
				LoggerUtil.logIt( "Data upload failed by '" + user.getUser().getName() + ":"
						+ user.getUser().getFullName() + "' was failed with error \n"
						+ ExceptionUtil.getStackTrace( e ) );
			}
			finally {
				sc.closeSession();
			}
			
			resp.sendRedirect( req.getContextPath() + "/uploadData.htm" );
		}*/
	}

	class DataObjects {
		private String				line;
		private List<String>		errorMessages			= new ArrayList<String>();
		private boolean				isLineErrorIgnoreable	= true;
		private Child				childdo;
		private Vaccination			curVaccinationdo;
		private Vaccination			measlesVaccinationdo;
		private List<ReminderSms>	nxvcReminderSmsdo;
		private List<ReminderSms>	measlesReminderSmsdo;
		private Vaccination			nextVaccinationdo;

		public DataObjects( Child child , Vaccination currentVacc , Vaccination nextVacc, 
				List<ReminderSms> nextVaccReminder , Vaccination measlesVaccination, 
				List<ReminderSms>	measlesReminder, String line , List<String> eMessages ,
				boolean islineErrIgn ) {
			setChilddo(child);
			setCurVaccinationdo(currentVacc);
			setNextVaccinationdo(nextVacc);
			setNxvcReminderSmsdo(nextVaccReminder);
			setMeaslesVaccinationdo(measlesVaccination);
			setMeaslesReminderSmsdo(measlesReminder);
			this.setLine(line);
			errorMessages = eMessages;
			setLineErrorIgnoreable(islineErrIgn);
		}
		public String getErrorAsText() {
			String msg = "";
			for ( String st : errorMessages ) {
				msg += "\n" + st;
			}
			return msg;
		}
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}
		public List<String> getErrorMessages() {
			return errorMessages;
		}
		public void setErrorMessages(List<String> errorMessages) {
			this.errorMessages = errorMessages;
		}
		public boolean isLineErrorIgnoreable() {
			return isLineErrorIgnoreable;
		}
		public void setLineErrorIgnoreable(boolean isLineErrorIgnoreable) {
			this.isLineErrorIgnoreable = isLineErrorIgnoreable;
		}
		public Child getChilddo() {
			return childdo;
		}
		public void setChilddo(Child childdo) {
			this.childdo = childdo;
		}
		public Vaccination getCurVaccinationdo() {
			return curVaccinationdo;
		}
		public void setCurVaccinationdo(Vaccination curVaccinationdo) {
			this.curVaccinationdo = curVaccinationdo;
		}
		public Vaccination getMeaslesVaccinationdo() {
			return measlesVaccinationdo;
		}
		public void setMeaslesVaccinationdo(Vaccination measlesVaccinationdo) {
			this.measlesVaccinationdo = measlesVaccinationdo;
		}
		public List<ReminderSms> getNxvcReminderSmsdo() {
			return nxvcReminderSmsdo;
		}
		public void setNxvcReminderSmsdo(List<ReminderSms> nxvcReminderSmsdo) {
			this.nxvcReminderSmsdo = nxvcReminderSmsdo;
		}
		public List<ReminderSms> getMeaslesReminderSmsdo() {
			return measlesReminderSmsdo;
		}
		public void setMeaslesReminderSmsdo(List<ReminderSms> measlesReminderSmsdo) {
			this.measlesReminderSmsdo = measlesReminderSmsdo;
		}
		public Vaccination getNextVaccinationdo() {
			return nextVaccinationdo;
		}
		public void setNextVaccinationdo(Vaccination nextVaccinationdo) {
			this.nextVaccinationdo = nextVaccinationdo;
		}
	}

}
