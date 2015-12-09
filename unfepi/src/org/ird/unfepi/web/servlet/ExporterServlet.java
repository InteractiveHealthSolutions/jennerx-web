package org.ird.unfepi.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jmatrix.eproperties.EProperties;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.input.BOMInputStream;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.ird.unfepi.DataQuery;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.IncentiveWorkType;
import org.ird.unfepi.GlobalParams.SearchFilter;
import org.ird.unfepi.GlobalParams.VariableSettingType;
import org.ird.unfepi.constants.SystemPermissions;
import org.ird.unfepi.constants.WebGlobals;
import org.ird.unfepi.constants.WebGlobals.DGInconsistenciesFieldNames;
import org.ird.unfepi.constants.WebGlobals.DWRParamsGeneral;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.LoggedInUser;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Child;
import org.ird.unfepi.model.ChildIncentive;
import org.ird.unfepi.model.DailySummary;
import org.ird.unfepi.model.IdMapper;
import org.ird.unfepi.model.IncentiveStatus;
import org.ird.unfepi.model.Model.Gender;
import org.ird.unfepi.model.Model.SmsStatus;
import org.ird.unfepi.model.ReminderSms.REMINDER_STATUS;
import org.ird.unfepi.model.StorekeeperIncentiveParticipant;
import org.ird.unfepi.model.StorekeeperIncentiveTransaction;
import org.ird.unfepi.model.StorekeeperIncentiveWorkProgress;
import org.ird.unfepi.model.UserSms;
import org.ird.unfepi.model.Vaccination;
import org.ird.unfepi.model.Vaccination.TimelinessStatus;
import org.ird.unfepi.model.Vaccination.VACCINATION_STATUS;
import org.ird.unfepi.model.VaccinatorIncentiveParticipant;
import org.ird.unfepi.model.VaccinatorIncentiveTransaction;
import org.ird.unfepi.model.VaccinatorIncentiveWorkProgress;
import org.ird.unfepi.model.Vaccine;
import org.ird.unfepi.report.Csvee;
import org.ird.unfepi.report.CsveeRow;
import org.ird.unfepi.utils.IRUtils;
import org.ird.unfepi.utils.LoggerUtils;
import org.ird.unfepi.utils.LoggerUtils.LogType;
import org.ird.unfepi.utils.UnfepiUtils;
import org.ird.unfepi.utils.UserSessionUtils;
import org.ird.unfepi.utils.date.DateUtils;
import org.ird.unfepi.utils.date.DateUtils.TIME_INTERVAL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;
@SuppressWarnings({"unchecked", "rawtypes"})
public class ExporterServlet extends HttpServlet
{
	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)	throws ServletException, IOException 
	{
		LoggedInUser user=UserSessionUtils.getActiveUser(req);
		if(user==null){
			response.sendRedirect(req.getContextPath() +"/login.htm");
			return;
		}
		
		String extype = req.getParameter("extype");
		
		if(!authenticateAction(user, extype, req, response)){
			return;
		}
		
		if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_VACCINATION_CSV.name()))
		{
			 exportVaccination(req, response);
		}
		/*else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_PROJECT_CSV.name()))
		{
			exportSummaryEnrollment(req, response);
		}*/
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_ENROLLMENT_BY_CENTER_CSV.name()))
		{
			exportSummaryEnrollmentByCenters(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_IMUNIZATION_AGE_APROPRIATE_CSV.name()))
		{
			exportSummaryImmunizationAgeAppropriate(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_IMUNIZATION_AGE_APROPRIATE_WITH_RETRO_CSV.name()))
		{
			exportSummaryFollowups(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_IMMUNIZATION_BY_VACCINATOR_CSV.name()))
		{
			exportSummaryImmunizationByVaccinator(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SUMMARY_IMMUNIZATION_BY_CENTER_CSV.name()))
		{
			exportSummaryImmunizationByCenter(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_CHILD_UNCONSUMED_INCENTIVE_CSV.name()))
		{
			exportUnconsumedChildIncentive(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.UPLOAD_CHILD_PROCESSED_INCENTIVE_CSV.name()))
		{
			importProcessedChildIncentive(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_CHILD_CSV.name()))
		{
			exportChildren(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_REMINDERSMS_CSV.name()))
		{
			exportVaccineSms(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_REMINDERSMS_CSV.name()))
		{
			//exportLotterySms(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_DATA_INCONSISTENCY_CSV.name()))
		{
			exportDataInconsistencyReport(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_DAILY_SUMMARY_CSV.name()))
		{
			exportDailySummary(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_VACCINATOR_CSV.name()))
		{
			exportVaccinators(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_STOREKEEPER_CSV.name()))
		{
			exportStorekeepers(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_VACCINATOR_INCENTIVE_CSV.name()))
		{
			exportVaccinatorIncentive(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_CHILD_LOTTERY_CSV.name()))
		{
			exportChildLottery(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_CHILD_GENERATED_LOTTERY_CSV.name()))
		{
			exportLotteryGenerated(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_STOREKEEPER_INCENTIVE_CSV.name()))
		{
			exportStorekeeperIncentive(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_SYSTEM_KEYVALUE_PAIRS_CSV.name()))
		{
			exportKeyValuePairs(req, response);
		}
		else if(extype.equalsIgnoreCase(SystemPermissions.DOWNLOAD_CUSTOM_SMS_CSV.name()))
		{
			exportUserSms(req, response);
		}
		
		GlobalParams.DBLOGGER.info(extype, LoggerUtils.getLoggerParams(LogType.DATA_EXPORT, null, user.getUser().getUsername()));
	}

	private void exportDataInconsistencyReport(HttpServletRequest req,HttpServletResponse response) {
		ServiceContext sc = Context.getServices();

		try{
			String uniqueName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter(DGInconsistenciesFieldNames.uniqueName.name()))?null:req.getParameter(DGInconsistenciesFieldNames.uniqueName.name());
	
			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_datainconsistency"+uniqueName+"_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 
			
			List<Object> list = new ArrayList<Object>();
			list = sc.getCustomQueryService().getDataBySQL(sc.getIRSettingService().findByCriteria(uniqueName, VariableSettingType.DATA_INCONSISTENCY.name(), "DATA_QUERY", null, null, null, true, 0, 1).get(0).getValue());

			Csvee csv = new Csvee();
			
			int i=0;
			for(Object object : list){
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	private boolean authenticateAction(LoggedInUser user, String permissionSQLFilterWithoutQuotes, HttpServletRequest req, HttpServletResponse response) throws IOException {
		ServiceContext sc = Context.getServices();
		try{
			List plist = sc.getCustomQueryService().getDataByHQL("select p.permissionId from User u join u.idMapper i join i.role r join r.permissions p where u.mappedId="+user.getUser().getMappedId()+" and p.permissionname like '"+permissionSQLFilterWithoutQuotes+"'");
			if(plist.size()>0){
				return true;
			}
			response.getOutputStream().write("You donot have permission to carry out requested operation. Contact your system administrator.".getBytes());
		}
		catch (Exception e) {
			e.printStackTrace();
			response.getOutputStream().write(("Error while getting permissions. "+e.getMessage()+". Contact your system administrator.").getBytes());
		}
		finally{
			sc.closeSession();
		}
		response.setContentType("text/plain"); 
		response.setHeader("Content-Disposition", "attachment; filename=DownloadError_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".txt"); 

		response.getOutputStream().flush();
		response.getOutputStream().close();
		return false;
	}
	
	private void exportKeyValuePairs(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_systemkeyvaluepairs_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			
			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("Vaccine ID");
			headerrow.addRowElement("Vaccine Name");
			
			csv.addHeader(headerrow );
			
			List list = sc.getCustomQueryService().getDataBySQL("SELECT vaccineId,name FROM vaccine order by vaccineId");
			for (Object object : list) {
				CsveeRow datarow = new CsveeRow();
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
			}
			
			zip.putNextEntry(new ZipEntry("VACCINE.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			
			Csvee csvrelation = new Csvee();
			
			CsveeRow headerrowrelation = new CsveeRow();
			headerrowrelation.addRowElement("Relationship ID");
			headerrowrelation.addRowElement("Relationship Name");
			
			csvrelation.addHeader(headerrowrelation );
			
			List listrelation = sc.getCustomQueryService().getDataBySQL("SELECT relationshipId,relationName FROM relationship order by relationshipId");
			for (Object object : listrelation) {
				CsveeRow datarow = new CsveeRow();
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csvrelation.addData(datarow);
			}
			
			zip.putNextEntry(new ZipEntry("RELATIONSHIP.csv"));
			zip.write(csvrelation.getCsv(true));
			zip.closeEntry();
			
			Csvee csvreligion = new Csvee();
			
			CsveeRow headerrowreligion = new CsveeRow();
			headerrowreligion.addRowElement("Religion ID");
			headerrowreligion.addRowElement("Religion Name");
			
			csvreligion.addHeader(headerrowreligion );
			
			List listreligion = sc.getCustomQueryService().getDataBySQL("SELECT religionId,religionName FROM religion order by religionId");
			for (Object object : listreligion) {
				CsveeRow datarow = new CsveeRow();
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csvreligion.addData(datarow);
			}
			
			zip.putNextEntry(new ZipEntry("RELIGION.csv"));
			zip.write(csvreligion.getCsv(true));
			zip.closeEntry();
		
			Csvee csvlang = new Csvee();
			
			CsveeRow headerrowlang = new CsveeRow();
			headerrowlang.addRowElement("Language ID");
			headerrowlang.addRowElement("Language Name");
			
			csvlang.addHeader(headerrowlang );
			
			List listlang = sc.getCustomQueryService().getDataBySQL("SELECT languageId,languageName FROM language order by languageId");
			for (Object object : listlang) {
				CsveeRow datarow = new CsveeRow();
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csvlang.addData(datarow);
			}
			
			zip.putNextEntry(new ZipEntry("LANGUAGE.csv"));
			zip.write(csvlang.getCsv(true));
			zip.closeEntry();
			
			Csvee csvremind = new Csvee();
			
			CsveeRow headerrowremind = new CsveeRow();
			headerrowremind.addRowElement("Reminder ID");
			headerrowremind.addRowElement("Reminder Name");
			
			csvremind.addHeader(headerrowremind );
			
			List listremind = sc.getCustomQueryService().getDataBySQL("SELECT reminderId,reminderName FROM reminder order by reminderId");
			for (Object object : listremind) {
				CsveeRow datarow = new CsveeRow();
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csvremind.addData(datarow);
			}
			
			zip.putNextEntry(new ZipEntry("REMINDER.csv"));
			zip.write(csvremind.getCsv(true));
			zip.closeEntry();
			
			zip.flush();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	private void exportChildLottery(HttpServletRequest req,	HttpServletResponse response) {
		Session ses = Context.getNewSession();
		try{
			ScrollableResults results = ses.createSQLQuery(DataQuery.EXPORT_CHILD_INCENTIVE_QUERY)
		            .setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
			
			String fileName = "ChildIncentive_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date());
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_childincentive_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());

			zip.putNextEntry(new ZipEntry(fileName+".csv"));
			
			while(results.next()){
				Object[] row = results.get();
				
				CsveeRow dataRow = new CsveeRow();
				for (Object string : row) {
					dataRow.addRowElement(string);
				}
				
				zip.write(dataRow.getRowAsSB().toString().getBytes());
			}
			
			zip.closeEntry();
			
			zip.flush();
			zip.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ses.close();
		}
	}
	
	private void exportLotteryGenerated(HttpServletRequest req, HttpServletResponse response) {
		Session ss = Context.getNewSession();
		//ServiceContext sc = null;
		try{
			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_LotteryGenerated_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			List res = null;
			Query q = ss.createSQLQuery("CALL LotteryGeneratedProc()").setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			res = q.list();
			res.get(0);
			Object[] keys = null;
			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			if(res.size()>0){
				keys = ((HashMap)res.get(0)).keySet().toArray();
				for (Object object : keys) {
					headerrow.addRowElement(object);
				}
			}
			
			csv.addHeader(headerrow );
			int i=1;
			for (Object object : res) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				HashMap map = (HashMap)object;
				for (int ind = 0; ind < keys.length; ind++) {
					datarow.addRowElement(map.get(keys[ind]));
				}
				
				csv.addData(datarow);

				i++;
			}
			
			/*ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("childlostlottery.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();*/
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
			ss.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			//sc.closeSession();
		}
	}
	
	private void importProcessedChildIncentive(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException{
		boolean isMultipart = ServletFileUpload.isMultipartContent( req );
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold( 2097152 );// 2MB

		String dataFilePath = System.getProperty("user.home");
		if(!dataFilePath.endsWith(System.getProperty("file.separator"))){
			dataFilePath += System.getProperty("file.separator");
		}
		
		String finalpath = dataFilePath+"epitemp";
		
		File fileTempRep = new File(finalpath);

		if (fileTempRep.exists()) {
			fileTempRep.mkdirs();
		}
		factory.setRepository( fileTempRep );
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload( factory );

		List items = null;
		try {
			items = upload.parseRequest( req );
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Process the uploaded items
		Iterator iter = items.iterator();
		FileItem fileInputItem = null;
		while ( iter.hasNext() ) {
			FileItem item = (FileItem) iter.next();

			if (!item.isFormField()) {
				fileInputItem = item;
			}
		}
		
		response.setContentType("text/csv"); 
		response.setHeader("Content-Disposition", "attachment; filename=Importer_childincentive_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

		ServiceContext sc = Context.getServices();
		try{
		if (fileInputItem != null) {
			BOMInputStream bomIn = new BOMInputStream(fileInputItem.getInputStream(), false);
			Scanner snr = new Scanner(bomIn, "UTF8");

			JSONArray json = UnfepiUtils.getCSVToJson(snr);
			
			List<String[]> updates= new ArrayList<String[]>();
			updates.add(new String[] {"CNIC", "AMOUNT", "LAST_UPDATE", "MSISDN", "REASON", "EPI_RESULT"});
			for (int i = 0; i < json.length(); i++) {
				String[] rowres = new String[6];
				JSONObject jo = json.getJSONObject(i);
				String cnic = jo.getString("CNIC");
				Integer amount = Integer.parseInt(jo.getString("AMOUNT"));
				String dt = jo.getString("LAST_UPDATE");
				String phone = jo.getString("MSISDN");
				String reason = jo.has("REASON")?jo.getString("REASON"):"";
				
				rowres[0] = cnic;
				rowres[1] = amount.toString();
				rowres[2] = dt;
				rowres[3] = phone;
				rowres[4] = reason;
				
				List<Child> chl = sc.getChildService().findChildByCriteria(null, null, null, null, cnic, null, null, null, null, null, false, null, null, null, true, 0, 10, null);
				if(chl.size() == 0){
					rowres[5] = "NO Child found on CNIC";
				}
				if(chl.size() > 1){
					rowres[5] = chl.size()+" Children found on CNIC";
				}
				
				if(dt.length() > 18){
					dt = dt.substring(0, 18);
				}
				
				if(chl.size() == 1){
					Child ch = chl.get(0);
					Date disdate = new SimpleDateFormat("dd-MMM-yy HH.mm.ss").parse(dt);
					List<ChildIncentive> incent = sc.getIncentiveService().findChildIncentiveByCriteria(null, ch.getMappedId(), null, null, IncentiveStatus.AVAILABLE, null, null, null, null, amount, amount, null, 0, 2, false, null);
					if(incent.size() == 0){
						rowres[5] = "NO Available incentive found";
					}
					else if(!jo.has("REASON") || StringUtils.isEmptyOrWhitespaceOnly(jo.getString("REASON"))){
						incent.get(0).setIncentiveStatus(IncentiveStatus.CONSUMED);
						incent.get(0).setConsumptionDate(disdate);
						incent.get(0).setTransactionDate(disdate);
						incent.get(0).setLastEditedDate(new Date());
						
						
						rowres[5] = "SUCCESS: FOUND "+incent.size()+"records. MARKED record # "+incent.get(0).getChildIncentiveId()+" CONSUMED";
					}
					else {
						incent.get(0).setIncentiveStatus(IncentiveStatus.WAITING);
						incent.get(0).setTransactionDate(disdate);
						incent.get(0).setDescription(jo.getString("REASON"));
						incent.get(0).setLastEditedDate(new Date());
						
						rowres[5] = "ERROR: FOUND "+incent.size()+"records. MARKED record # "+incent.get(0).getChildIncentiveId()+" WAITING for correction";
					}
					sc.getIncentiveService().updateChildIncentive(incent.get(0));
				}
				updates.add(rowres);
			}
			snr.close();

			Csvee csv = new Csvee();
			for (String[] data : updates) {
				CsveeRow datarow = new CsveeRow();
				for (int ind = 0; ind < data.length; ind++) {
					datarow.addRowElement(data[ind]);
				}
				
				csv.addData(datarow);
			}
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		else {
			req.setAttribute("message", "No file found");
			req.getRequestDispatcher("s_upload_processed_child_incentive.htm").forward(req, response);
		}
		} catch (JSONException e) {
			e.printStackTrace();
			response.getOutputStream().write(("CSV Parsing threw error"+e.getMessage()).getBytes());
		} catch (ParseException e) {
			e.printStackTrace();
			response.getOutputStream().write(("Unrecognized date format").getBytes());
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportVaccinatorIncentive(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	
			Date incentiveDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("incentiveDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("incentiveDatefrom"));	
			Date incentiveDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("incentiveDateto"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("incentiveDateto"));	
			Boolean hasWonCheck=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("hasWonCheck")))?null:true;	

			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_vaccinatorincentive_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Vaccinator");
			headerrow.addRowElement("Name");
			headerrow.addRowElement("Date Of Incentivization");
			headerrow.addRowElement("Incentivization Date Range Lower");
			headerrow.addRowElement("Incentivization Date Range Upper");
			headerrow.addRowElement("Is Incentivized ?");
			headerrow.addRowElement("Total Transactions Done");
			headerrow.addRowElement("Total Mothers Benefited");
			headerrow.addRowElement("Total Amount Transferred");
			headerrow.addRowElement("Commission");
			headerrow.addRowElement("Incentive Amount Realized");
			headerrow.addRowElement("Transaction Status");
			headerrow.addRowElement("Incentive Calculation Formula");
			
			csv.addHeader(headerrow );
			
			List<VaccinatorIncentiveParticipant> list = sc.getIncentiveService().findVaccinatorIncentiveParticipantByCriteria(null, vaccinator, null, hasWonCheck, null, null, incentiveDatefrom, incentiveDateto, 0, Integer.MAX_VALUE, true, new String[]{"vaccinatorIncentiveEvent","vaccinatorIncentiveParams"});
			int i=1;
			for (VaccinatorIncentiveParticipant parti : list) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				datarow.addRowElement(parti.getVaccinator().getIdMapper().getIdentifiers().get(0).getIdentifier());
				datarow.addRowElement(parti.getVaccinator().getFullName());
				datarow.addRowElement(parti.getVaccinatorIncentiveEvent().getDateOfEvent());
				datarow.addRowElement(parti.getVaccinatorIncentiveEvent().getDataRangeDateLower());
				datarow.addRowElement(parti.getVaccinatorIncentiveEvent().getDataRangeDateUpper());
				datarow.addRowElement(parti.getIsIncentivised());
				List<VaccinatorIncentiveWorkProgress> workdonettr = sc.getIncentiveService().findVaccinatorIncentiveWorkProgressByCriteria(parti.getSerialNumber(), IncentiveWorkType.CHILD_LOTTERY_WINNINGS.name(), null, 0, Integer.MAX_VALUE, true, null);
				datarow.addRowElement(workdonettr.size()>0?workdonettr.get(0).getWorkValue():null);
				List<VaccinatorIncentiveWorkProgress> workdonetmb = sc.getIncentiveService().findVaccinatorIncentiveWorkProgressByCriteria(parti.getSerialNumber(), IncentiveWorkType.CHILD_LOTTERY_DISTINCT_CHILDREN.name(), null, 0, Integer.MAX_VALUE, true, null);
				datarow.addRowElement(workdonetmb.size()>0?workdonetmb.get(0).getWorkValue():null);
				List<VaccinatorIncentiveWorkProgress> workdonetat = sc.getIncentiveService().findVaccinatorIncentiveWorkProgressByCriteria(parti.getSerialNumber(), IncentiveWorkType.CHILD_LOTTERY_WON_AMOUNT.name(), null, 0, Integer.MAX_VALUE, true, null);
				datarow.addRowElement(workdonetat.size()>0?workdonetat.get(0).getWorkValue():null);

				if(parti.getVaccinatorIncentiveParams() != null){
				datarow.addRowElement(parti.getVaccinatorIncentiveParams().getAmount());
				}
				else{
					datarow.addRowElement(null);
				}
				
				List<VaccinatorIncentiveTransaction> transl = sc.getIncentiveService().findVaccinatorIncentiveTransactionByCriteria(parti.getVaccinatorIncentiveEventId(), parti.getVaccinatorId(), null, null, null, null, null, 0, Integer.MAX_VALUE, true, null);
				VaccinatorIncentiveTransaction trans = transl.size()>0?transl.get(0):null;
				if(trans != null){
				datarow.addRowElement(trans.getAmountDue());
				datarow.addRowElement(trans.getTransactionStatus());
				datarow.addRowElement(trans.getDescription());
				}
				else{
					datarow.addRowElement(null);
					datarow.addRowElement(null);
					datarow.addRowElement(null);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			/*ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("vaccinatorincentive.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();*/
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	private void exportStorekeeperIncentive(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			
			response.setContentType("text/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_storekeeperincentive_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Storekeeper");
			headerrow.addRowElement("Name");
			headerrow.addRowElement("Date Of Incentivization");
			headerrow.addRowElement("Incentivization Date Range Lower");
			headerrow.addRowElement("Incentivization Date Range Upper");
			headerrow.addRowElement("Is Incentivized ?");
			headerrow.addRowElement("Total Transactions Done");
			headerrow.addRowElement("Total Amount Transferred");
			headerrow.addRowElement("Commission");
			headerrow.addRowElement("Incentive Amount Realized");
			headerrow.addRowElement("Transaction Status");
			headerrow.addRowElement("Incentive Calculation Formula");
			
			csv.addHeader(headerrow );
			
			Integer storekeeper = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("storekeeperddp"))) ? null : Integer.parseInt(req.getParameter("storekeeperddp"));	
			Date incentiveDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("incentiveDatefrom"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("incentiveDatefrom"));	
			Date incentiveDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("incentiveDateto"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("incentiveDateto"));	
			Boolean isIncentivizedCheck=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("isIncentivizedCheck")))?null:true;	
			Float transactionsdonerangeL = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("transactionsdonerangeL"))) ? null : Float.parseFloat(req.getParameter("transactionsdonerangeL"));	
			Float transactionsdonerangeU = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("transactionsdonerangeU"))) ? null : Float.parseFloat(req.getParameter("transactionsdonerangeU"));	

			
			List<StorekeeperIncentiveParticipant> list = sc.getIncentiveService().findStorekeeperIncentiveParticipantByCriteria(null, storekeeper, null, isIncentivizedCheck, transactionsdonerangeL, transactionsdonerangeU, incentiveDatefrom, incentiveDateto, 0, Integer.MAX_VALUE, true, new String[]{"storekeeperIncentiveEvent","storekeeperIncentiveParams"});
			int i=1;
			for (StorekeeperIncentiveParticipant parti : list) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				datarow.addRowElement(parti.getStorekeeper().getIdMapper().getIdentifiers().get(0).getIdentifier());
				datarow.addRowElement(parti.getStorekeeper().getFullName());
				datarow.addRowElement(parti.getStorekeeperIncentiveEvent().getDateOfEvent());
				datarow.addRowElement(parti.getStorekeeperIncentiveEvent().getDataRangeDateLower());
				datarow.addRowElement(parti.getStorekeeperIncentiveEvent().getDataRangeDateUpper());
				datarow.addRowElement(parti.getIsIncentivised());
				List<StorekeeperIncentiveWorkProgress> workdone = sc.getIncentiveService().findStorekeeperIncentiveWorkProgressByCriteria(parti.getSerialNumber(), null, null, null, null, 0, Integer.MAX_VALUE, true, null);
				datarow.addRowElement(workdone.size()>0?workdone.get(0).getTransactions():null);
				datarow.addRowElement(workdone.size()>0?workdone.get(0).getTotalTransactionsAmount():null);

				if(parti.getStorekeeperIncentiveParams() != null){
				datarow.addRowElement(parti.getStorekeeperIncentiveParams().getAmount());
				}
				else{
					datarow.addRowElement(null);
				}
				
				List<StorekeeperIncentiveTransaction> transl = sc.getIncentiveService().findStorekeeperIncentiveTransactionByCriteria(parti.getStorekeeperIncentiveEventId(), parti.getStorekeeperId(), null, null, null, null, null, 0, Integer.MAX_VALUE, true, null);
				StorekeeperIncentiveTransaction trans = transl.size()>0?transl.get(0):null;
				if(trans != null){
				datarow.addRowElement(trans.getAmountDue());
				datarow.addRowElement(trans.getTransactionStatus());
				datarow.addRowElement(trans.getDescription());
				}
				else{
					datarow.addRowElement(null);
					datarow.addRowElement(null);
					datarow.addRowElement(null);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			/*ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("vaccinatorlottery.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();*/
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportVaccinators(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String vaccinatorid=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorid")))?null:req.getParameter("vaccinatorid").trim();
			String vaccinatorname=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorname")))?null:req.getParameter("vaccinatorname").trim();		
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Gender gender=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("gender")))?null:Gender.valueOf(req.getParameter("gender"));	
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_vaccinator_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			Csvee csv = new Csvee();
			
			String qury = DataQuery.EXPORT_VACCINATOR_QUERY + " WHERE 1=1 ";
			
			if(vaccinatorid != null){
				qury += " and vtoridm.programId ="+vaccinatorid+" ";
			}
			if(vaccinatorname != null){
				qury += " and (firstName like '%"+vaccinatorname+"%' OR lastName like '%"+vaccinatorname+"%' )";
			}
			
			if(gender != null){
				qury += " and gender ='"+gender+"' ";
			}
			
			if (vaccCenter != null) {
				qury += " and vidm.programId = "+vaccCenter+" ";
			}
			
			List list = sc.getCustomQueryService().getDataBySQL(qury);
			int i=0;
			for (Object object : list) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("vaccinator.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	/*private void exportVaccinationCenters(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			List<VaccinationCenter> list=new ArrayList<VaccinationCenter>();

			String centerid=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("centerid")))?null:req.getParameter("centerid").trim();
			String centername=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("centername")))?null:req.getParameter("centername").trim();		
			CenterType centerType=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("centerType")))?null:CenterType.valueOf(req.getParameter("centerType"));	
			
			list = sc.getVaccinationService().findVaccinationCenterByCriteria(centerid, centername, centerType, 0, Integer.MAX_VALUE, true, new String[]{"idMapper", "broughtByRelationship"});
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_vaccinationcenter_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Vaccinator ID");
			headerrow.addRowElement("Date Registered");
			headerrow.addRowElement("First Name");
			headerrow.addRowElement("Last Name");
			headerrow.addRowElement("Vaccination Center");
			headerrow.addRowElement("Gender");
			headerrow.addRowElement("Birthdate");
			headerrow.addRowElement("Is Estimated Birthdate");
			headerrow.addRowElement("Qualification");
			headerrow.addRowElement("Additional description");

			csv.addHeader(headerrow );
			
			int i=1;
			for (Vaccinator vaccinator : list) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				datarow.addRowElement(vaccinator.getIdMapper().getIdentifiers().get(0).getIdentifier());
				datarow.addRowElement(vaccinator.getDateRegistered());
				datarow.addRowElement(vaccinator.getFirstName());
				datarow.addRowElement(vaccinator.getLastName());
				
				VaccinationCenter vc = sc.getVaccinationService().findVaccinationCenterById(vaccinator.getVaccinationCenterId(), true, new String[]{"idMapper"});
				datarow.addRowElement(vc==null?null:vc.getIdMapper().getIdentifiers().get(0).getIdentifier());
				datarow.addRowElement(vaccinator.getGender());
				datarow.addRowElement(vaccinator.getBirthdate());
				datarow.addRowElement(vaccinator.getEstimatedBirthdate());
				datarow.addRowElement(vaccinator.getQualification());
				datarow.addRowElement(vaccinator.getDescription());

				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("vaccinator.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}*/
	
	private void exportStorekeepers(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String storekeeperid=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("storekeeperid")))?null:req.getParameter("storekeeperid").trim();
			String storekeepername=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("storekeepername")))?null:req.getParameter("storekeepername").trim();		
			Gender gender=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("gender")))?null:Gender.valueOf(req.getParameter("gender"));	
			String storename=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("storename")))?null:req.getParameter("storename").trim();		
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_storekeeper_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			Csvee csv = new Csvee();
			
			String qury = DataQuery.EXPORT_STOREKEEPER_QUERY + " WHERE 1=1 ";
			
			if(storekeeperid != null){
				qury += " and stkidm.programId ="+storekeeperid+" ";
			}
			if(storekeepername != null){
				qury += " and (firstName like '%"+storekeepername+"%' OR lastName like '%"+storekeepername+"%' )";
			}
			
			if(storename != null){
				qury += " and storeName like '%"+storename+"%' ";
			}
			
			if(gender != null){
				qury += " and gender ='"+gender+"' ";
			}
			
			if (vaccCenter != null) {
				qury += " and vidm.programId = "+vaccCenter+" ";
			}
			
			List list = sc.getCustomQueryService().getDataBySQL(qury);
			int i=0;
			for (Object object : list) {
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("storekeeper.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	private void exportDailySummary(HttpServletRequest req,	HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		
		try{
			List<DailySummary> list =new ArrayList<DailySummary>();
			
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	
			Date summaryDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("summaryDatefrom"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("summaryDatefrom"));	
			Date summaryDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("summaryDateto"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("summaryDateto"));	

			String sql = "SELECT d.summaryDate, cent.programId center, vtor.programId vaccinator, usr.username, d.createdDate, " +
					" sum(case when dsvg.vaccineName = 'BCG' then dsvg.quantityGiven else null end) bcg, " +
					" sum(case when dsvg.vaccineName = 'PENTA1' then dsvg.quantityGiven else null end) p1, " +
					" sum(case when dsvg.vaccineName = 'PENTA2' then dsvg.quantityGiven else null end) p2, " +
					" sum(case when dsvg.vaccineName = 'PENTA3' then dsvg.quantityGiven else null end) p3, " +
					" sum(case when dsvg.vaccineName = 'MEASLES1' then dsvg.quantityGiven else null end) m1, " +
					" sum(case when dsvg.vaccineName = 'MEASLES2' then dsvg.quantityGiven else null end) m2, " +
					" sum(case when dsvg.vaccineName = 'OPV0' then dsvg.quantityGiven else null end) opv0, " +
					" sum(case when dsvg.vaccineName = 'OPV1' then dsvg.quantityGiven else null end) opv1, " +
					" sum(case when dsvg.vaccineName = 'OPV2' then dsvg.quantityGiven else null end) opv2, " +
					" sum(case when dsvg.vaccineName = 'OPV3' then dsvg.quantityGiven else null end) opv3, " +
					" sum(case when dsvg.vaccineName = 'TT1' then dsvg.quantityGiven else null end) tt1, " +
					" sum(case when dsvg.vaccineName = 'TT2' then dsvg.quantityGiven else null end) tt2, " +
					" sum(case when dsvg.vaccineName = 'TT3' then dsvg.quantityGiven else null end) tt3, " +
					" sum(case when dsvg.vaccineName = 'TT4' then dsvg.quantityGiven else null end) tt4, " +
					" sum(case when dsvg.vaccineName = 'TT5' then dsvg.quantityGiven else null end) tt5, " +
					" sum(case when dsvg.vaccineName = 'TOTAL_VACCINATED' then dsvg.quantityGiven else null end) total, " +
					" d.bcgVisited, d.bcgEnrolledTotal, d.bcgEnrolledWithReminder, d.bcgEnrolledWithLottery, " +
					" d.penta1Visited, d.penta1EnrolledTotal, d.penta1EnrolledWithReminder, d.penta1EnrolledWithLottery, d.penta1Followuped, " +
					" d.penta2Visited, d.penta2Followuped, " +
					" d.penta3Visited, d.penta3Followuped, " +
					" d.measles1Visited, d.measles1Followuped, " +
					" d.measles2Visited, d.measles2Followuped, " +
					" d.ttGivenTotal, d.opvGivenTotal, " +
					" d.totalEnrolled, d.totalFollowuped, d.totalVisited, " +
					" d.description " +
					" FROM dailysummary d" +
					" left join idmapper cent on d.vaccinationCenterId = cent.mappedId " +
					" left join idmapper vtor on d.vaccinatorId = vtor.mappedId " +
					" left join dailysummaryvaccinegiven dsvg on d.serialNumber = dsvg.dailySummaryId " +
					" left join user usr on d.createdByUserId = usr.mappedId " +
					" where serialNumber is not null " ;
					if(vaccCenter != null){
						sql += " and d.vaccinationCenterId="+vaccCenter;
					}
					if(vaccinator != null){
						sql += " and d.vaccinatorId="+vaccinator;
					}
					if(summaryDatefrom != null && summaryDateto != null){
						sql += " and d.summaryDate between '"+GlobalParams.SQL_DATE_FORMAT.format(summaryDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(summaryDateto)+"' ";
					}
					
					sql += " group by serialNumber order by summaryDate desc";
					
			list = sc.getCustomQueryService().getDataBySQL(sql);

			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_dailysummary_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Summary Date");
			headerrow.addRowElement("Vaccination Center");
			headerrow.addRowElement("Vaccinator");
			headerrow.addRowElement("Creator");
			headerrow.addRowElement("Created Date");
			
			headerrow.addRowElement("Total BCG");
			headerrow.addRowElement("Total Penta1");
			headerrow.addRowElement("Total Penta2");
			headerrow.addRowElement("Total Penta3");
			headerrow.addRowElement("Total Measles1");
			headerrow.addRowElement("Total Measles2");
			headerrow.addRowElement("Total OPV0");
			headerrow.addRowElement("Total OPV1");
			headerrow.addRowElement("Total OPV2");
			headerrow.addRowElement("Total OPV3");
			headerrow.addRowElement("Total TT1");
			headerrow.addRowElement("Total TT2");
			headerrow.addRowElement("Total TT3");
			headerrow.addRowElement("Total TT4");
			headerrow.addRowElement("Total TT5");
			headerrow.addRowElement("Total Vaccinated");
			
			headerrow.addRowElement("OLD: BCG Visited");
			headerrow.addRowElement("OLD: BCG Enrolled Total");
			headerrow.addRowElement("OLD: BCG Enrolled With Reminder");
			headerrow.addRowElement("OLD: BCG Enrolled With Lottery");
			headerrow.addRowElement("OLD: Penta1 Visited");
			headerrow.addRowElement("OLD: Penta1 Enrolled Total");
			headerrow.addRowElement("OLD: Penta1 Enrolled With Reminder");
			headerrow.addRowElement("OLD: Penta1 Enrolled With Lottery");
			headerrow.addRowElement("OLD: Penta1 Followed up");
			headerrow.addRowElement("OLD: Penta2 Visited");
			headerrow.addRowElement("OLD: Penta2 Followed up");
			headerrow.addRowElement("OLD: Penta3 Visited");
			headerrow.addRowElement("OLD: Penta3 Followed up");
			headerrow.addRowElement("OLD: Measles1 Visited");
			headerrow.addRowElement("OLD: Measles1 Followed up");
			headerrow.addRowElement("OLD: Measles2 Visited");
			headerrow.addRowElement("OLD: Measles2 Followed up");
			headerrow.addRowElement("OLD: Total TT");
			headerrow.addRowElement("OLD: Total OPV");
			headerrow.addRowElement("OLD: Total Enrolled");
			headerrow.addRowElement("OLD: Total Followed up");
			headerrow.addRowElement("OLD: Total Visited/Vaccinated");
			headerrow.addRowElement("Additional description");

			csv.addHeader(headerrow );
			
			int i=1;
			for(Object object : list){
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("dailysummary.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportChildren(HttpServletRequest req,	HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		
		try{
			String childId=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childid")))?null:req.getParameter("childid").trim();
			String childNamepart=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childnamepart")))?null:req.getParameter("childnamepart").trim();		
			String cellNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("cellNumber")))?null:req.getParameter("cellNumber").trim();
			//STATUS status=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatus")))?null:STATUS.valueOf(req.getParameter("followupstatus"));	
			//boolean isnotChecked=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("followupstatusNotchk")))?false:true;	
			Gender gender=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("gender")))?null:Gender.valueOf(req.getParameter("gender"));	
			//String epiNumber=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("epiNumber")))?null:req.getParameter("epiNumber");	
			Date enrollmentDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enrollmentDatefrom"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("enrollmentDatefrom"));	
			Date enrollmentDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("enrollmentDateto"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("enrollmentDateto"));	
			Date birthDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("birthDatefrom"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("birthDatefrom"));	
			Date birthDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("birthDateto"))) ? null : WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("birthDateto"));	
			
			// our child may be mapped onto different centers but the center on which it was enrolled
			// will be only one and the very first and will be part of child's id hence for finding children 
			// of a center we will set child id digits as center id
			String vaccCenter = req.getParameter("vaccinationCenterddp");
			if(childId == null && !StringUtils.isEmptyOrWhitespaceOnly(vaccCenter)){
				childId = vaccCenter;
			}
						
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_children_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 

			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Child ID");
			headerrow.addRowElement("Date Enrolled");
			headerrow.addRowElement("Enrollment Vaccine ID");
			headerrow.addRowElement("Enrollment Vaccine");
			headerrow.addRowElement("Enrollment Center");
			headerrow.addRowElement("Enrollment Timeliness");
			headerrow.addRowElement("Enrollment Timeliness Factor");
			headerrow.addRowElement("Enrollment Vaccinator");
			/*headerrow.addRowElement("Enrollment Brought By Relationship ID");
			headerrow.addRowElement("Enrollment Brought By");
			headerrow.addRowElement("Enrollment Brought By Other");*/
			headerrow.addRowElement("Enrollment OPV Given");
			headerrow.addRowElement("Enrollment PCV Given");
			/*headerrow.addRowElement("Enrollment Weight");
			headerrow.addRowElement("Enrollment Height");*/
			headerrow.addRowElement("Enrollment EPI number");
			headerrow.addRowElement("Child First Name");
			headerrow.addRowElement("Child Last Name");
			headerrow.addRowElement("Father First Name");
			headerrow.addRowElement("Father Last Name");
			headerrow.addRowElement("Mother First Name");
			headerrow.addRowElement("Mother Last Name");
			headerrow.addRowElement("Gender");
			headerrow.addRowElement("Birthdate");
			headerrow.addRowElement("Is Estimated Birthdate");
			headerrow.addRowElement("Religion ID");
			headerrow.addRowElement("Religion");
			headerrow.addRowElement("Other Religion");
			headerrow.addRowElement("Language ID");
			headerrow.addRowElement("Language");
			headerrow.addRowElement("Other Language");
			
			headerrow.addRowElement("Address Type");
			headerrow.addRowElement("House Number");
			headerrow.addRowElement("Street Numbr");
			headerrow.addRowElement("Sector");
			headerrow.addRowElement("Colony");
			headerrow.addRowElement("Town");
			headerrow.addRowElement("UC");
			headerrow.addRowElement("Landmark");
			headerrow.addRowElement("City ID");
			headerrow.addRowElement("City");
			headerrow.addRowElement("Other City");

			headerrow.addRowElement("Date Of Preference");
			headerrow.addRowElement("Approved Lottery");
			headerrow.addRowElement("Lottery Rejection Reason");
			headerrow.addRowElement("Other Lottery Rejection Reason");
			headerrow.addRowElement("Approved Reminders");
			headerrow.addRowElement("Reminders Rejection Reason");
			headerrow.addRowElement("Other Reminders Rejection Reason");
			headerrow.addRowElement("Has Cell Phone Access");
			headerrow.addRowElement("Preferred Sms Timings");
			
			headerrow.addRowElement("Contact1 Number Type");
			headerrow.addRowElement("Owner Relation ID");
			headerrow.addRowElement("Owner Relation");
			headerrow.addRowElement("Other Owner Relation");
			headerrow.addRowElement("Number");
			headerrow.addRowElement("Contact2 Number Type");
			headerrow.addRowElement("Line Type");
			headerrow.addRowElement("Number");
			headerrow.addRowElement("Date Created");
			headerrow.addRowElement("Data Entry User");
			headerrow.addRowElement("Data Entry Source");
			headerrow.addRowElement("Description");

			csv.addHeader(headerrow );
			
			String qury = "select cid.programId , " +
					" c.dateEnrolled , ifnull(CAST(venr.vaccineId AS char(2)), 'NOT FOUND') EnrVaccineId, ifnull(vc.name, 'NOT FOUND') EnrVaccineName, ifnull(enrvcntid.programId, 'NOT FOUND') EnrCenter, " +
					" venr.timelinessStatus as Timeliness, venr.timelinessFactor as TimelinessFactor, ifnull(enrvtorid.programId, 'NOT FOUND') EnrVaccinator, venr.polioVaccineGiven EnrPolioGiven, venr.pcvGiven EnrPCVGiven, ifnull(venr.epiNumber, 'NOT FOUND') EnrEpiNumber, " +
					" c.firstName , c.lastName , c.fatherFirstName , c.fatherLastName , " +
					" c.motherFirstName , c.motherLastName ,  c.gender , c.birthdate , c.estimatedBirthdate, " +
					" c.religionId , rlg.religionName , c.otherReligion , c.languageId , lng.languageName , c.otherLanguage , " +
					" ifnull(ad.addressType, 'NOT FOUND') addressType, ad.addHouseNumber , ad.addStreet , ad.addSector , ad.addColony , ifnull(ad.addtown, 'NOT FOUND') addressTown, ad.addUc , ad.addLandmark , ifnull(CAST(ad.cityId AS char(2)), 'NOT FOUND') cityId, cty.cityName , ad.cityName as othercity, " +
					" ifnull(CAST(prf.datePreferenceChanged AS char(20)), '') dateOfPreference, ifnull(CAST(venr.hasApprovedLottery AS char(2)), '') HasApprovedLottery, prf.reasonLotteryRejection , prf.reasonLotteryRejectionOther , " +
					" ifnull(CAST(prf.hasApprovedReminders AS char(2)), '') HasApprovedReminders, prf.reasonRemindersRejection , prf.reasonRemindersRejectionOther , prf.hasCellPhoneAccess , prf.preferredSmsTiming , " +
					" ifnull(c1.numberType, '') Contact1Type, c1.ownerRelationId , c1rl.relationName , c1.otherOwnerRelation , ifnull(c1.number, '') Contact1Number, " +
					" c2.numberType Contact2Type, c2.telelineType , c2.number , " +
					" c.createdDate , u.username , e.dataEntrySource , c.description " +
					" from child c" +
					" left join idmapper cid on c.mappedId = cid.mappedId " +
					" left join religion rlg on c.religionId = rlg.religionId " +
					" left join language lng on c.languageId = lng.languageId " +
					" left join user u on c.createdByUserId = u.mappedId " +
					" left join (select * from lotterysms lin where lin.datepreferencechanged=(select max(datepreferencechanged) from lotterysms where mappedid=lin.mappedid) " +
					"		and lin.createdDate=(select max(createdDate) from lotterysms where mappedid=lin.mappedid and date(datepreferencechanged)=date(lin.datepreferencechanged))) " +
					" 		prf on c.mappedId=prf.mappedId " +
					" left join contactnumber c1 on c.mappedId = c1.mappedId and c1.numberType='PRIMARY' " +
					" left join contactnumber c2 on c.mappedId = c2.mappedId and c2.numberType='SECONDARY' " +
					" left join address ad on c.mappedId = ad.mappedId " +
					" left join vaccination venr on c.mappedId = venr.childId and venr.vaccinationDate is not null and date(venr.vaccinationDate) = (select min(date(vaccinationDate)) from vaccination where childId= venr.childId) " +
					" 						and venr.vaccinationRecordNum = (select min(vaccinationRecordNum) from vaccination where childId=venr.childId and date(vaccinationDate)=date(venr.vaccinationDate)) " +
					" left join vaccine vc on venr.vaccineId = vc.vaccineId " +
					" left join idmapper enrvtorid on venr.vaccinatorId = enrvtorid.mappedId " +
					" left join idmapper enrvcntid on venr.vaccinationCenterId = enrvcntid.mappedId " +
					" left join city cty on ad.cityId = cty.cityId " +
					" left join relationship c1rl on c1.ownerRelationId = c1rl.relationshipId " +
					" left join encounter e on c.mappedId = e.p1id and e.encounterType = 'Enrollment' " +
					" where c.mappedId is not null ";
			
			if(childId != null ){
				qury += " and cid.programId like '" + childId +"%' ";
			}
			if(enrollmentDatefrom != null && enrollmentDateto != null){
				qury += " and dateEnrolled between '"+GlobalParams.SQL_DATE_FORMAT.format(enrollmentDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(enrollmentDateto)+"' ";
			}
			if(birthDatefrom != null && birthDateto != null){
				qury += " and birthDate between '"+GlobalParams.SQL_DATE_FORMAT.format(birthDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(birthDateto)+"' ";
			}
			if(childNamepart != null){
				qury += " and (c.firstName like '"+childNamepart+"%' OR c.lastName like '"+childNamepart+"%' )";
			}
			if(cellNumber != null){
				qury += " and (c1.number like '%"+cellNumber+"' OR c2.number like '%"+cellNumber+"' )";
			}
			if(gender != null){
				qury += " and gender = '"+gender.name()+"' ";
			}

			qury += " order by dateEnrolled desc, createdDate desc";

			//System.out.println(qury);
			
			List list = sc.getCustomQueryService().getDataBySQL(qury);

			int i=1;
			for(Object object : list){
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("children.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	@SuppressWarnings("rawtypes")
	private void exportSummaryFollowups(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryFollowupByCohortAndVaccine_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Follow-up");
			csv.addData(datarow1);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("");
			datarow2.addRowElement("");
			datarow2.addRowElement("Total-Cohort");
			datarow2.addRowElement("Total-Cohort %");
			datarow2.addRowElement("BCG-Cohort");
			datarow2.addRowElement("BCG-Cohort %");
			datarow2.addRowElement("Penta-1 Cohort");
			datarow2.addRowElement("Penta-1 Cohort %");
			datarow2.addRowElement("Penta-2 Cohort");
			datarow2.addRowElement("Penta-2 Cohort %");
			datarow2.addRowElement("Penta-3 Cohort");
			datarow2.addRowElement("Penta-3 Cohort %");
			datarow2.addRowElement("Measles-1 Cohort");
			datarow2.addRowElement("Measles-1 Cohort %");
			datarow2.addRowElement("Measles-2 Cohort");
			datarow2.addRowElement("Measles-2 Cohort %");
			csv.addData(datarow2);
			
			List rssummaryFupByCohorttotal = sc.getCustomQueryService().getDataBySQL("CALL SummaryFupByCohort()");
			
			for (int i = 0; i < rssummaryFupByCohorttotal.size(); i++) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])rssummaryFupByCohorttotal.get(i);
				for (int j = 0; j < objarr.length; j++) {
					int denominatorVal = 0;
					if(i > 0 && j > 0 && (i%2) != 0){
						denominatorVal = Integer.parseInt(((Object[])rssummaryFupByCohorttotal.get(i-1))[j].toString());
					}
					String addPercent = "";
					if(j == 0 && (i%2) != 0){
						addPercent = " %";
					}
					else if((i%2) !=0 && denominatorVal != 0){
						addPercent = " "+Math.round(100*Integer.parseInt(objarr[j].toString())/denominatorVal)+"%";
					}
					else if((i%2) !=0 && denominatorVal == 0){
						addPercent = " 0%";
					}
					
					datarow.addRowElement(objarr[j]);
					datarow.addRowElement(addPercent);
				}
				csv.addData(datarow);
			}
			
			/*for (Object object : rssummaryFupByCohorttotal) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}*/
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	private void exportUnconsumedChildIncentive(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Export_CaregiverIncentivesUnconsumed_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			String date1from = GlobalParams.SQL_DATE_FORMAT.format(UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req));
			String date1to = GlobalParams.SQL_DATE_FORMAT.format(UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req));
			
			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Caregiver Incentives Still Unconsumed");
			csv.addData(datarow1);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Scheme");
			datarow2.addRowElement("AmountWon");
			datarow2.addRowElement("EPCharges");
			datarow2.addRowElement("AmountDue");
			datarow2.addRowElement("CNIC");
			datarow2.addRowElement("CellNumber");
			datarow2.addRowElement("IncentiveRealizedDate");
			datarow2.addRowElement("ConsumptionDate");
			datarow2.addRowElement("TransactionDate");
			datarow2.addRowElement("ProgramId");
			datarow2.addRowElement("ChildName");
			datarow2.addRowElement("CenterId");
			datarow2.addRowElement("Center");
			datarow2.addRowElement("IncentiveRecordCreatedDate");
			datarow2.addRowElement("IncentiveStatus");
			datarow2.addRowElement("IncentiveRecordId");
			datarow2.addRowElement("Vaccine");
			datarow2.addRowElement("VaccineDate");
			datarow2.addRowElement("VaccineStatus");
			datarow2.addRowElement("VaccinatorId");
			datarow2.addRowElement("Vaccinator");
			csv.addData(datarow2);
			
			List rssummaryFupByCohorttotal = sc.getCustomQueryService().getDataBySQL("CALL ChildIncentiveRealizedCalculate('"+date1from+"', '"+date1to+"');");
			
			for (Object object : rssummaryFupByCohorttotal) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	public static void main (String[] args) throws IOException, InstanceAlreadyExistsException {
		InputStream f = Thread.currentThread().getContextClassLoader().getResourceAsStream("unfepi.properties");
		// Java Properties donot seem to support substitutions hence EProperties are used to accomplish the task
		EProperties root = new EProperties();
		root.load(f);

		// Java Properties to send to context and other APIs for configuration
		Properties prop = new Properties();
		prop.putAll(IRUtils.convertEntrySetToMap(root.entrySet()));

		Context.instantiate(prop);
		ServiceContext sc = Context.getServices();

		try {
			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Follow-up");
			csv.addData(datarow1);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("");
			datarow2.addRowElement("");
			datarow2.addRowElement("Total-Cohort");
			datarow2.addRowElement("Total %");
			datarow2.addRowElement("BCG-Cohort");
			datarow2.addRowElement("BCG %");
			datarow2.addRowElement("Penta-1 Cohort");
			datarow2.addRowElement("Penta-1 %");
			datarow2.addRowElement("Penta-2 Cohort");
			datarow2.addRowElement("Penta-2 %");
			datarow2.addRowElement("Penta-3 Cohort");
			datarow2.addRowElement("Penta-3 %");
			datarow2.addRowElement("Measles-1 Cohort");
			datarow2.addRowElement("Measles-1 %");
			datarow2.addRowElement("Measles-2 Cohort");
			datarow2.addRowElement("Measles-2 %");
			csv.addData(datarow2);
			
			List rssummaryFupByCohorttotal = sc.getCustomQueryService().getDataBySQL("CALL SummaryFupByCohort()");
			
			for (int i = 0; i < rssummaryFupByCohorttotal.size(); i++) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])rssummaryFupByCohorttotal.get(i);
				for (int j = 0; j < objarr.length; j++) {
					int denominatorVal = 0;
					if(i > 0 && j > 0 && (i%2) != 0){
						denominatorVal = Integer.parseInt(((Object[])rssummaryFupByCohorttotal.get(i-1))[j].toString());
					}
					String addPercent = "";
					if(j == 0 && (i%2) != 0){
						addPercent = " %";
					}
					else if((i%2) !=0 && denominatorVal != 0){
						addPercent = ""+Math.round(100*Integer.parseInt(objarr[j].toString())/denominatorVal)+"%";
					}
					else if((i%2) !=0 && denominatorVal == 0){
						addPercent = " 0%";
					}
					
					datarow.addRowElement(objarr[j]);
					datarow.addRowElement(addPercent);
				}
				csv.addData(datarow);
			}
			
			FileOutputStream fop = new FileOutputStream("D:\\sumrfile.csv");
			fop.write(csv.getCsv(true));
			fop.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sc.closeSession();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void exportSummaryEnrollmentByCohort(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryEnrollmentByCohortAndGender_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Enrollment by Cohort and Gender");
			csv.addData(datarow1);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("");
			datarow2.addRowElement("Total");
			datarow2.addRowElement("Total BCG");
			datarow2.addRowElement("Total Penta-1");
			datarow2.addRowElement("Total Penta-2");
			datarow2.addRowElement("Total Penta-3");
			datarow2.addRowElement("Total Measles-1");
			datarow2.addRowElement("Total Measles-2");
			csv.addData(datarow2);
			
			List rscohortbygender = sc.getCustomQueryService().getDataBySQL("CALL SummaryEnrByGenderCohort()");

			for (Object object : rscohortbygender) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	@SuppressWarnings("rawtypes")
	private void exportSummaryEnrollment(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			List startDatel = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_START_DATE);
			Date startDate = startDatel.size()>0?new SimpleDateFormat("yyyy-MM-dd").parse(startDatel.get(0).toString()):null;
			
			Integer week = startDate == null? null : DateUtils.differenceBetweenIntervals(new Date(), startDate, TIME_INTERVAL.WEEK)+1;
			
			List targetEnrl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_TARGET_ENROLLMENTS);
			String targetEnr = targetEnrl.size()>0?targetEnrl.get(0).toString(): null;
			
			List targetEvntl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_PROJECT_TARGET_EVENTS);
			String targetEvnt = targetEvntl.size()>0?targetEvntl.get(0).toString(): null;
			
			List totalEnrollmentsl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_TOTAL_ENROLLMENTS);
			Integer totalEnrollments = Integer.parseInt(totalEnrollmentsl.get(0).toString());
			
			List totalSuccessEvntsl = sc.getCustomQueryService().getDataBySQL(GlobalParams.QUERY_VACCINATIONS_RECEIVED);
			Integer totalSuccessEvnts = Integer.parseInt(totalSuccessEvntsl.get(0).toString());
			
			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryProject_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Enrollment");
			csv.addData(datarow1);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Start Date of Project");
			datarow2.addRowElement(startDate==null?null:startDate);
			csv.addData(datarow2);
			
			CsveeRow datarow3 = new CsveeRow();
			datarow3.addRowElement("Current week of project");
			datarow3.addRowElement(week==null?null:week);
			csv.addData(datarow3);
			
			CsveeRow datarow4 = new CsveeRow();
			datarow4.addRowElement("Target Enrollment");
			datarow4.addRowElement(targetEnr);
			csv.addData(datarow4);
			
			CsveeRow datarow5 = new CsveeRow();
			datarow5.addRowElement("Total Enrollment");
			datarow5.addRowElement(totalEnrollments==null?null:totalEnrollments);
			csv.addData(datarow5);
			
			CsveeRow datarow6 = new CsveeRow();
			datarow6.addRowElement("Target Total Events (Follow-up + Enrollment)");
			datarow6.addRowElement(targetEvnt);
			csv.addData(datarow6);
			
			CsveeRow datarow7 = new CsveeRow();
			datarow7.addRowElement("Total Events");
			datarow7.addRowElement(totalSuccessEvnts==null?null:totalSuccessEvnts);
			csv.addData(datarow7);
			
			CsveeRow datarow8 = new CsveeRow();
			datarow8.addRowElement("Avg. enrollment per week");
			datarow8.addRowElement((week==null||totalEnrollments==null)?"":new Float(totalEnrollments/(week+0.0)));
			csv.addData(datarow8);
			
			CsveeRow datarow9 = new CsveeRow();
			datarow9.addRowElement("Avg. Events per week");
			datarow9.addRowElement((week==null||totalSuccessEvnts==null)?"":new Float(totalSuccessEvnts/(week+0.0)));
			csv.addData(datarow9);

			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}

	@SuppressWarnings("rawtypes")
	private void exportSummaryEnrollmentByCenters(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String center = req.getParameter(DWRParamsGeneral.vaccinationCenter.name());
			String date1from = req.getParameter(DWRParamsGeneral.date1from.name());
			String date1to = req.getParameter(DWRParamsGeneral.date1to.name());
			
			String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
			String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

			List centersummary = sc.getCustomQueryService().getDataBySQL("CALL SummaryEnrByCenterCohort2('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , 0, "+Integer.MAX_VALUE+", '', '')");

			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryEnrollmentByCohortAndCenter_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Enrollment by Vaccination Center");
			csv.addData(datarow1);
			
			CsveeRow datarowf1 = new CsveeRow();
			datarowf1.addRowElement("Filters Applied : ");
			csv.addData(datarowf1);
			
			CsveeRow datarowf2 = new CsveeRow();
			datarowf2.addRowElement("Centers : "+(center==null?"not filtered":center.trim()));
			csv.addData(datarowf2);
			
			CsveeRow datarowf3 = new CsveeRow();
			datarowf3.addRowElement("Enrollment Date : "+(d1f==null||d1t==null?"not filtered":(d1f+"  -  "+d1t))+"");
			csv.addData(datarowf3);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Center ID");
			datarow2.addRowElement("Center Name");
			datarow2.addRowElement("Total Enrollments");
			datarow2.addRowElement("Enrollments BCG");
			datarow2.addRowElement("% BCG");
			datarow2.addRowElement("Enrollments Penta-1");
			datarow2.addRowElement("% Penta-1");
			datarow2.addRowElement("Enrollments Penta-2");
			datarow2.addRowElement("% Penta-2");
			datarow2.addRowElement("Enrollments Penta-3");
			datarow2.addRowElement("% Penta-3");
			datarow2.addRowElement("Enrollments Measles-1");
			datarow2.addRowElement("% Measles-1");
			datarow2.addRowElement("Enrollments Measles-2");
			datarow2.addRowElement("% Measles-2");
			csv.addData(datarow2);
			
			for (Object object : centersummary) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportSummaryImmunizationAgeAppropriate(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String center = req.getParameter(DWRParamsGeneral.vaccinationCenter.name());
			String date1from = req.getParameter(DWRParamsGeneral.date1from.name());
			String date1to = req.getParameter(DWRParamsGeneral.date1to.name());
			
			String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
			String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

			List centersummary = sc.getCustomQueryService().getDataBySQL("CALL SummaryFollowupAgeAppropriate('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , 0, "+Integer.MAX_VALUE+", '', '')");

			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryFUPAgeAppropriate_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Followup Age Appropriate");
			csv.addData(datarow1);
			
			CsveeRow datarowf1 = new CsveeRow();
			datarowf1.addRowElement("Filters Applied : ");
			csv.addData(datarowf1);
			
			CsveeRow datarowf2 = new CsveeRow();
			datarowf2.addRowElement("Centers : "+(center==null?"not filtered":center.trim()));
			csv.addData(datarowf2);
			
			CsveeRow datarowf3 = new CsveeRow();
			datarowf3.addRowElement("Enrollment Date : "+(d1f==null||d1t==null?"not filtered":(d1f+"  -  "+d1t))+"");
			csv.addData(datarowf3);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Vaccine ID");
			datarow2.addRowElement("Cohort");
			datarow2.addRowElement("Total");
			datarow2.addRowElement("BCG due");
			datarow2.addRowElement("Penta-1 due");
			datarow2.addRowElement("Penta-2 due");
			datarow2.addRowElement("Penta-3 due");
			datarow2.addRowElement("Measles-1 due");
			datarow2.addRowElement("Measles-2 due");
			datarow2.addRowElement("BCG done");
			datarow2.addRowElement("Penta-1 done");
			datarow2.addRowElement("Penta-2 done");
			datarow2.addRowElement("Penta-3 done");
			datarow2.addRowElement("Measles-1 done");
			datarow2.addRowElement("Measles-2 done");

			csv.addData(datarow2);
			
			for (Object object : centersummary) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportSummaryImmunizationByVaccinator(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String center = req.getParameter(DWRParamsGeneral.vaccinationCenter.name());
			String date1from = req.getParameter(DWRParamsGeneral.date1from.name());
			String date1to = req.getParameter(DWRParamsGeneral.date1to.name());
			
			String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
			String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

			List summary = sc.getCustomQueryService().getDataBySQL("CALL SummaryImmunizationByVaccinator('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , 0, "+Integer.MAX_VALUE+", '', '')");

			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryImmunizationByVaccinator_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Immunization by Vaccinator");
			csv.addData(datarow1);
			
			CsveeRow datarowf1 = new CsveeRow();
			datarowf1.addRowElement("Filters Applied : ");
			csv.addData(datarowf1);
			
			CsveeRow datarowf2 = new CsveeRow();
			datarowf2.addRowElement("Centers : "+(center==null?"not filtered":center.trim()));
			csv.addData(datarowf2);
			
			CsveeRow datarowf3 = new CsveeRow();
			datarowf3.addRowElement("Date : "+(d1f==null||d1t==null?"not filtered":(d1f+"  -  "+d1t))+"");
			csv.addData(datarowf3);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Vaccinator ID");
			datarow2.addRowElement("Name");
			datarow2.addRowElement("Incentives");
			datarow2.addRowElement("Total");
			datarow2.addRowElement("BCG (%)");
			datarow2.addRowElement("Penta-1 (%)");
			datarow2.addRowElement("Penta-2 (%)");
			datarow2.addRowElement("Penta-3 (%)");
			datarow2.addRowElement("Measles-1 (%)");
			datarow2.addRowElement("Measles-2 (%)");
			datarow2.addRowElement("OPV-0 (%)");
			datarow2.addRowElement("OPV-1 (%)");
			datarow2.addRowElement("OPV-2 (%)");
			datarow2.addRowElement("OPV-3 (%)");
			datarow2.addRowElement("PCV-1 (%)");
			datarow2.addRowElement("PCV-2 (%)");
			datarow2.addRowElement("PCV-3 (%)");

			csv.addData(datarow2);
			
			for (Object object : summary) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportSummaryImmunizationByCenter(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String center = req.getParameter(DWRParamsGeneral.vaccinationCenter.name());
			String date1from = req.getParameter(DWRParamsGeneral.date1from.name());
			String date1to = req.getParameter(DWRParamsGeneral.date1to.name());
			
			String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
			String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

			List summary = sc.getCustomQueryService().getDataBySQL("CALL SummaryImmunizationByCenter('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , 0, "+Integer.MAX_VALUE+", '', '')");

			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryImmunizationByCenter_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Immunization by Center");
			csv.addData(datarow1);
			
			CsveeRow datarowf1 = new CsveeRow();
			datarowf1.addRowElement("Filters Applied : ");
			csv.addData(datarowf1);
			
			CsveeRow datarowf2 = new CsveeRow();
			datarowf2.addRowElement("Centers : "+(center==null?"not filtered":center.trim()));
			csv.addData(datarowf2);
			
			CsveeRow datarowf3 = new CsveeRow();
			datarowf3.addRowElement("Date : "+(d1f==null||d1t==null?"not filtered":(d1f+"  -  "+d1t))+"");
			csv.addData(datarowf3);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Center ID");
			datarow2.addRowElement("Name");
			datarow2.addRowElement("Total");
			datarow2.addRowElement("BCG (%)");
			datarow2.addRowElement("Penta-1 (%)");
			datarow2.addRowElement("Penta-2 (%)");
			datarow2.addRowElement("Penta-3 (%)");
			datarow2.addRowElement("Measles-1 (%)");
			datarow2.addRowElement("Measles-2 (%)");
			datarow2.addRowElement("OPV-0 (%)");
			datarow2.addRowElement("OPV-1 (%)");
			datarow2.addRowElement("OPV-2 (%)");
			datarow2.addRowElement("OPV-3 (%)");
			datarow2.addRowElement("PCV-1 (%)");
			datarow2.addRowElement("PCV-2 (%)");
			datarow2.addRowElement("PCV-3 (%)");

			csv.addData(datarow2);
			
			for (Object object : summary) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	private void exportSummaryImmunizationAgeAppropriateWithRetro(HttpServletRequest req, HttpServletResponse response) {
		ServiceContext sc = Context.getServices();
		try{
			String center = req.getParameter(DWRParamsGeneral.vaccinationCenter.name());
			String date1from = req.getParameter(DWRParamsGeneral.date1from.name());
			String date1to = req.getParameter(DWRParamsGeneral.date1to.name());
			
			String d1f = StringUtils.isEmptyOrWhitespaceOnly(date1from)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1from))+"'");
			String d1t = StringUtils.isEmptyOrWhitespaceOnly(date1to)?null:("'"+new SimpleDateFormat("yyyy-MM-dd").format(WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(date1to))+"'");

			List centersummary = sc.getCustomQueryService().getDataBySQL("CALL SummaryFollowupAgeAppropriateWRetro('"+(center==null?"":center.trim())+"', "+d1f+", "+d1t+" , 0, "+Integer.MAX_VALUE+", '', '')");

			response.setContentType("application/csv"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_SummaryFUPAgeAppropriateWRetro_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".csv"); 

			Csvee csv = new Csvee();
			
			CsveeRow datarow1 = new CsveeRow();
			datarow1.addRowElement("Summary Followup Age Appropriate with Retro");
			csv.addData(datarow1);
			
			CsveeRow datarowf1 = new CsveeRow();
			datarowf1.addRowElement("Filters Applied : ");
			csv.addData(datarowf1);
			
			CsveeRow datarowf2 = new CsveeRow();
			datarowf2.addRowElement("Centers : "+(center==null?"not filtered":center.trim()));
			csv.addData(datarowf2);
			
			CsveeRow datarowf3 = new CsveeRow();
			datarowf3.addRowElement("Enrollment Date : "+(d1f==null||d1t==null?"not filtered":(d1f+"  -  "+d1t))+"");
			csv.addData(datarowf3);
			
			CsveeRow datarow2 = new CsveeRow();
			datarow2.addRowElement("Vaccine ID");
			datarow2.addRowElement("Cohort");
			datarow2.addRowElement("Total");
			datarow2.addRowElement("BCG due");
			datarow2.addRowElement("Penta-1 due");
			datarow2.addRowElement("Penta-2 due");
			datarow2.addRowElement("Penta-3 due");
			datarow2.addRowElement("Measles-1 due");
			datarow2.addRowElement("Measles-2 due");
			datarow2.addRowElement("BCG done");
			datarow2.addRowElement("Penta-1 done");
			datarow2.addRowElement("Penta-2 done");
			datarow2.addRowElement("Penta-3 done");
			datarow2.addRowElement("Measles-1 done");
			datarow2.addRowElement("Measles-2 done");

			csv.addData(datarow2);
			
			for (Object object : centersummary) {
				CsveeRow datarow = new CsveeRow();

				Object[] objarr = (Object[])object;
				for (Object object2 : objarr) {
					datarow.addRowElement(object2);
				}
				csv.addData(datarow);
			}
			
			response.getOutputStream().write(csv.getCsv(true));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void exportVaccination(HttpServletRequest req, HttpServletResponse response) throws IOException{
		ServiceContext sc = Context.getServices();
		Session ss = Context.getNewSession();

		try{
			String childIdName=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("childIdName")))?null:req.getParameter("childIdName");
			String recordNum=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("recordNum")))?null:req.getParameter("recordNum");
			Date vaccinationDuedatefrom=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDuedatefrom")))?null:WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("vaccinationDuedatefrom"));
			Date vaccinationDuedateto;
			if(vaccinationDuedatefrom==null){
				vaccinationDuedateto=null;
			}
			else{
				vaccinationDuedateto=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDuedateto")))?new Date():WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("vaccinationDuedateto"));
			}
			
			Date vaccinationDatefrom=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDatefrom")))?null:WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("vaccinationDatefrom"));
			Date vaccinationDateto;
			if(vaccinationDatefrom==null){
				vaccinationDateto=null;
			}else{
				vaccinationDateto=(StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationDateto")))?new Date():WebGlobals.GLOBAL_JAVA_DATE_FORMAT.parse(req.getParameter("vaccinationDateto"));
			}
			
			Integer vaccCenter = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinationCenterddp"))) ? null : Integer.parseInt(req.getParameter("vaccinationCenterddp"));
			Integer vaccinator = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccinatorddp"))) ? null : Integer.parseInt(req.getParameter("vaccinatorddp"));	
			String epiNumber=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("epiNumber"))?null:req.getParameter("epiNumber");
			String vaccName=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccName"))?null:req.getParameter("vaccName");
			VACCINATION_STATUS vaccstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("vaccstatus"))?null:VACCINATION_STATUS.valueOf(req.getParameter("vaccstatus"));
			TimelinessStatus timelinessstatus=StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("timelinessstatus"))?null:TimelinessStatus.valueOf(req.getParameter("timelinessstatus"));		
			String exclusionCriteria = req.getParameter("exclusionCriteria")==null?"excludeEnrollments":req.getParameter("exclusionCriteria");
	
			List<Object> list = new ArrayList<Object>();
			
			try{
				Integer childId = null;
				if(recordNum != null){
					try{
						Vaccination obj = sc.getVaccinationService().getVaccinationRecord(Integer.parseInt(recordNum), true, new String[]{"child"}, null);
						if(obj != null) list.add(obj);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					if(childIdName != null){
						try{
							IdMapper chl = sc.getIdMapperService().findIdMapper(childIdName);
							childId = chl == null?-1:chl.getMappedId();
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					String qury = null ;
					
					if(exclusionCriteria.toLowerCase().contains("enrollment"))
					{
						qury +=	" where "+GlobalParams.SQL_FOLLOWUP_FILTER_v;
					}
					else if(exclusionCriteria.toLowerCase().contains("followup"))
					{
						qury += " where "+GlobalParams.SQL_ENROLLMENT_FILTER_v;
					}
					else if(exclusionCriteria.toLowerCase().contains("nothing"))
					{
						qury += " where v.vaccinationRecordNum is not NULL ";
					}
					
					
					if(vaccinationDuedatefrom != null && vaccinationDuedateto != null){
						qury += " and vaccinationDueDate between '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDuedatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDuedateto)+"' ";
					}
					if(vaccinationDatefrom != null && vaccinationDateto != null){
						qury += " and vaccinationDate between '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(vaccinationDateto)+"' ";
					}
					if(childId != null){
						qury += " and childId ="+childId+" ";
					}
					if(epiNumber != null){
						qury += " and epiNumber ='"+epiNumber+"' ";
					}
					if(vaccName != null){
						Vaccine vacc = sc.getVaccinationService().getByName(vaccName);
						qury += " and v.vaccineId ="+(vacc==null?-1:vacc.getVaccineId())+" ";
					}
					//////////////////FOR searching VACCINATION CENTERS
					if(vaccstatus != null && vaccstatus.equals(VACCINATION_STATUS.PENDING) && vaccCenter != null){
//TODO						qury += " and v.child.idMapper.identifiers[0].identifier like '"+sc.getIdMapperService().findIdMapper(vaccCenter).getProgramId()+"%' ";
					}
					else if(vaccCenter != null){
						qury += " and v.vaccinationCenterId ="+vaccCenter+" ";
					}
					
					if (timelinessstatus != null) {
						qury += " and timelinessStatus ='"+timelinessstatus+"' ";
					}
					if(vaccCenter != null){
						qury += " and v.vaccinationCenterId ='"+vaccCenter+"' ";
					}
					if(vaccinator != null){
						qury += " and v.vaccinatorId ='"+vaccinator+"' ";
					}
					qury += " order by vaccinationDuedate desc, vaccinationDate desc";

					//System.out.println(qury);
					try{
						Query q = ss.createSQLQuery(qury);
						list = q.setReadOnly(true)
								.setFirstResult(0)
								.setMaxResults(Integer.MAX_VALUE)
								.list();
					}
					finally{
						
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_vaccination_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 
			
			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Child ID");
			headerrow.addRowElement("Child Name");
			headerrow.addRowElement("Child Father Name");
			headerrow.addRowElement("Enrollment Vaccine ID");
			headerrow.addRowElement("Enrollment Vaccine Name");
			headerrow.addRowElement("Contact Numbers");
			headerrow.addRowElement("Address");
			headerrow.addRowElement("Vaccination Record Number");
			headerrow.addRowElement("Is First Vaccination ?");
			headerrow.addRowElement("Due date");
			headerrow.addRowElement("Vaccination Date");
			headerrow.addRowElement("Num of Days DUEDATE Passed");
			headerrow.addRowElement("Last EPI assigned");
			headerrow.addRowElement("Last Center visited");
			headerrow.addRowElement("Status");
			headerrow.addRowElement("Timeliness");
			headerrow.addRowElement("Timeliness Days");
			headerrow.addRowElement("Epi Reg/MR Number");
			headerrow.addRowElement("Next Assigned Date");
			headerrow.addRowElement("Center");
			headerrow.addRowElement("Vaccinator");
			headerrow.addRowElement("Vaccine ID");
			headerrow.addRowElement("Vaccine");
			headerrow.addRowElement("Lottery Approval");
			headerrow.addRowElement("FormOrEncounterType");
			headerrow.addRowElement("Data entry source");
			headerrow.addRowElement("Polio Vaccine Given");
			headerrow.addRowElement("PCV Given");
			headerrow.addRowElement("Brought By Relationship ID");
			headerrow.addRowElement("Brought By");
			headerrow.addRowElement("Other Brought By");
			headerrow.addRowElement("Weight");
			headerrow.addRowElement("Height");
			headerrow.addRowElement("Description");
	
			csv.addHeader(headerrow );
			
			int i=1;
			for(Object object : list){
				CsveeRow datarow = new CsveeRow();
				datarow.addRowElement(i);
				
				if(object instanceof Object[]){
					Object[] coldata = (Object[]) object;
					for (int ind = 0 ; ind < coldata.length ; ind++) {
						datarow.addRowElement(coldata[ind]);
					}
				}
				else{
					datarow.addRowElement(object);
				}
				
				csv.addData(datarow);
				
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("vaccination"+"_exclude_"+exclusionCriteria+".csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
			ss.close();
		}
	}
	
	private void exportVaccineSms(HttpServletRequest req, HttpServletResponse response) throws IOException{
		Session ses = Context.getNewSession();
		ServiceContext sc = Context.getServices();
		try{
			String childId_Name = UnfepiUtils.getStringFilter(SearchFilter.PROGRAM_ID, req);
			String cellNumber = UnfepiUtils.getStringFilter(SearchFilter.CELL_NUMBER, req);
			Date dueDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE1_FROM, req);
			Date dueDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE1_TO, req);
			Date sentDatefrom = UnfepiUtils.getDateFilter(SearchFilter.DATE2_FROM, req);
			Date sentDateto = UnfepiUtils.getDateFilter(SearchFilter.DATE2_TO, req);
			REMINDER_STATUS remstatus = (REMINDER_STATUS) UnfepiUtils.getEnumFilter(SearchFilter.SMS_STATUS, REMINDER_STATUS.class, req);	
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_vaccinesms_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());

			zip.putNextEntry(new ZipEntry("vaccinesms.csv"));
			
			String qury = DataQuery.EXPORT_VACCINE_SMS_QUERY + "  ";
			
			if(dueDatefrom != null && dueDateto != null){
				qury += " and dueDate between '"+GlobalParams.SQL_DATE_FORMAT.format(dueDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(dueDateto)+"' ";
			}
			if(sentDatefrom != null && sentDateto != null){
				qury += " and sentDate between '"+GlobalParams.SQL_DATE_FORMAT.format(sentDatefrom)+"' and '"+GlobalParams.SQL_DATE_FORMAT.format(sentDateto)+"' ";
			}
			if(childId_Name != null){
				int childId = sc.getIdMapperService().findIdMapper(childId_Name).getMappedId();
				qury += " and v.childId ="+childId+" ";
			}
			if(cellNumber != null){
				qury += " and recipient like '%"+cellNumber+"' ";
			}
			
			if (remstatus != null) {
				qury += " and reminderStatus like '"+remstatus+"' ";
			}
			
			ScrollableResults results = ses.createSQLQuery(qury)
		            .setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
			while(results.next()){
				Object[] row = results.get();
				
				CsveeRow dataRow = new CsveeRow();
				for (Object string : row) {
					dataRow.addRowElement(string);
				}
				
				zip.write(dataRow.getRowAsSB().toString().getBytes());
			}
			
			zip.closeEntry();
			
			zip.flush();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
			ses.close();
		}
	}
	
	private void exportUserSms(HttpServletRequest req, HttpServletResponse response) throws IOException{
		List<UserSms> list = new ArrayList<UserSms>();
		
		ServiceContext sc = Context.getServices();
		try{
			Date dueDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("dueDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("dueDatefrom"));
			Date dueDateto;
			if (dueDatefrom == null) {
				dueDateto = null;
			} else {
				dueDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("dueDateto"))) ? new Date() : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("dueDateto"));
			}
			
			Date sentDatefrom = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("sentDatefrom"))) ? null : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("sentDatefrom"));
			Date sentDateto;
			if (sentDatefrom == null) {
				sentDateto = null;
			} else {
				sentDateto = (StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("sentDateto"))) ? new Date() : new SimpleDateFormat(WebGlobals.GLOBAL_DATE_FORMAT_JAVA).parse(req.getParameter("sentDateto"));
			}
			SmsStatus smsstatus = StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("smsstatus")) ? null : SmsStatus.valueOf(req.getParameter("smsstatus"));
			boolean isNotChecked = StringUtils.isEmptyOrWhitespaceOnly(req.getParameter("smsstatusNotchk")) ? false : true;
			
			list = sc.getUserSmsService().findUserSmsByCriteria(dueDatefrom, dueDateto, sentDatefrom, sentDateto, smsstatus, isNotChecked, null, null, 0, null, Integer.MAX_VALUE, 0, true, new String[]{"idMapper","createdByUserId"});
			
			response.setContentType("application/zip"); 
			response.setHeader("Content-Disposition", "attachment; filename=Exporter_customsms_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date())+".zip"); 
			
			Csvee csv = new Csvee();
			
			CsveeRow headerrow = new CsveeRow();
			headerrow.addRowElement("S. no.");
			headerrow.addRowElement("Recipient ID");
			headerrow.addRowElement("Recipient Number");
			headerrow.addRowElement("Due Date");
			headerrow.addRowElement("Sent Date");
			headerrow.addRowElement("Text");
			headerrow.addRowElement("Sms Status");
			headerrow.addRowElement("Sms Cancel Reason");
			headerrow.addRowElement("Sms Cancel Reason Other");
			headerrow.addRowElement("Description");
			headerrow.addRowElement("Creator");
			headerrow.addRowElement("Created Date");
			
			csv.addHeader(headerrow );
			
			int i=1;
			for(UserSms rec:list){
				CsveeRow datarow = new CsveeRow();
				
				datarow.addRowElement(i);
				datarow.addRowElement(rec.getIdMapper()== null?"":rec.getIdMapper().getIdentifiers().get(0).getIdentifier());
				datarow.addRowElement(rec.getRecipient());
				datarow.addRowElement(rec.getDueDate());
				datarow.addRowElement(rec.getSentDate());	
				datarow.addRowElement(rec.getText());	
				datarow.addRowElement(rec.getSmsStatus());
				datarow.addRowElement(rec.getSmsCancelReason());
				datarow.addRowElement(rec.getSmsCancelReasonOther());
				datarow.addRowElement(StringUtils.isEmptyOrWhitespaceOnly(rec.getDescription())?"":rec.getDescription().replace(",",",,").replace("\"", "'").trim());
				datarow.addRowElement(rec.getCreatedByUserId().getUsername());
				datarow.addRowElement(rec.getCreatedDate());

				csv.addData(datarow);
	
				i++;
			}
			
			ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());
			zip.putNextEntry(new ZipEntry("customsms.csv"));
			zip.write(csv.getCsv(true));
			zip.closeEntry();
			zip.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			sc.closeSession();
		}
	}
}
