package org.ird.unfepi.autosys.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.DownloadableType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.model.DownloadableReport;
import org.ird.unfepi.report.CsveeRow;
import org.ird.unfepi.utils.FileUtils;
import org.ird.unfepi.utils.UnfepiUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataDumperJob implements Job 
{
	@Override
	public void execute(JobExecutionContext jxc) throws JobExecutionException {
		Session ses = Context.getNewSession();
		try {
			List dlis = ses.createSQLQuery("SELECT dmpProcedureName,dmpTableName,dmpFileNameInit FROM dmp_ WHERE isActive = true").list();

			for (Object object : dlis) {
				Object[] cols = (Object[]) object;
				String procedureName = (String) cols[0];
				String tableName = (String) cols[1];
				String fileNameInit = (String) cols[2];
				
				if (tableName.equals("jennerx_data_dump_")) {
					executeJennerX_Data_Dump(procedureName, tableName, fileNameInit,"select calenderId from vaccinationcalendar", ses );
				} else {
					executeSQL(procedureName, tableName, fileNameInit, ses );
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ses.close();
		}
	}
	
	public void executeSQL(String procedureName, String tableName, String fileNameInit, Session ses ){
		try {
			//CALL PROCEDURE TO REPLACE ALL DATA

			UnfepiUtils.executeDump(procedureName, ses);
			ses.clear();

			String fileName = fileNameInit+"_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date());
			File mfile = FileUtils.getFile(fileName+".zip", GlobalParams.DATA_DUMP_DIR);
			//File mfile = new File("g:\\"+fileName+".zip");

			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(mfile));

			zip.putNextEntry(new ZipEntry(fileName+".csv"));

			List<String> colNames = ses.createSQLQuery("SELECT distinct column_name FROM information_schema.columns WHERE table_name='"+tableName+"' ORDER BY ORDINAL_POSITION").list();
			CsveeRow headerRow = new CsveeRow();
			for (String string : colNames) {
				headerRow.addRowElement(string);
			}

			zip.write(headerRow.getRowAsSB().toString().getBytes());

			//SCROLL MASTER TABLE DATA TO CREATE CSV
			ScrollableResults results = ses.createSQLQuery("SELECT * FROM "+tableName).setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);

			int i = 0;
			while (results.next()) {
				if (i++ == 0 || i % 100 == 0) {
					ses.clear();
				}
				Object[] row = results.get();

				CsveeRow dataRow = new CsveeRow();
				for (Object string : row) {
					dataRow.addRowElement(string);
				}
				zip.write(dataRow.getRowAsSB().toString().getBytes());
			}

			try {
				results.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			zip.closeEntry();

			zip.flush();
			zip.close();

			Transaction tx = ses.beginTransaction();
			DownloadableReport dorp = new DownloadableReport();
			dorp.setCreatedDate(new Date());
			dorp.setDownloadableName(mfile.getName());
			dorp.setDownloadablePath(mfile.getPath());
			dorp.setDownloadableType(DownloadableType.DATA_DUMP.name());
			dorp.setSizeBytes((int) mfile.length());

			ses.save(dorp);

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ses.close();
		}
	}

	public void executeJennerX_Data_Dump(String procedureName, String tableName, String fileNameInit, String queryParameter, Session ses ){
		try {
			//CALL PROCEDURE TO REPLACE ALL DATA
			
			List<Integer> parametreList = ses.createSQLQuery(queryParameter).list();
			
			for (Integer parametre : parametreList) {
				UnfepiUtils.executeDump("JennerX_Data_Dump", parametre, ses);
				ses.clear();
				String fileName = fileNameInit+"_"+parametre+"_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date());
				File mfile = FileUtils.getFile(fileName+".zip", GlobalParams.DATA_DUMP_DIR);
				//File mfile = new File("g:\\"+fileName+".zip");

				ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(mfile));

				zip.putNextEntry(new ZipEntry(fileName+".csv"));
				
				
				List<String> colNames = ses.createSQLQuery("SELECT distinct column_name FROM information_schema.columns WHERE table_name='"+tableName+parametre+"' ORDER BY ORDINAL_POSITION").list();
				CsveeRow headerRow = new CsveeRow();
				for (String string : colNames) {
					headerRow.addRowElement(string);
				}
				
				zip.write(headerRow.getRowAsSB().toString().getBytes());
				//SCROLL MASTER TABLE DATA TO CREATE CSV
				ScrollableResults results = ses.createSQLQuery("SELECT * FROM "+tableName+parametre).setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
				
				System.out.println(results.toString());
				
				int i = 0;
				while (results.next()) {
					if (i++ == 0 || i % 100 == 0) {
						ses.clear();
					}
					Object[] row = results.get();

					CsveeRow dataRow = new CsveeRow();
					for (Object string : row) {
						dataRow.addRowElement(string);	
						System.out.print(string +"\t");
					}
					System.out.println();
					zip.write(dataRow.getRowAsSB().toString().getBytes());
				}

				try {
					results.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				zip.closeEntry();

				zip.flush();
				zip.close();

				Transaction tx = ses.beginTransaction();
				DownloadableReport dorp = new DownloadableReport();
				dorp.setCreatedDate(new Date());
				dorp.setDownloadableName(mfile.getName());
				dorp.setDownloadablePath(mfile.getPath());
				dorp.setDownloadableType(DownloadableType.DATA_DUMP.name());
				dorp.setSizeBytes((int) mfile.length());

				ses.save(dorp);
				tx.commit();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ses.close();
		}
	}


}
