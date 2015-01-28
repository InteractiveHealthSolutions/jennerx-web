package org.ird.unfepi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.management.InstanceAlreadyExistsException;

import org.hibernate.CacheMode;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ird.unfepi.GlobalParams.DownloadableType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.model.DownloadableReport;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DataDumperJobTest 
{
	private static final String MASTER_ALL_TABLE_NAME = "dmp_masterall";
	final static long  MEGABYTE = 1024L * 1024L;
	
	public static void main (String[] args) throws InstanceAlreadyExistsException, IOException {
		Context.instantiate(null);

		while(true){
			test();
			
			System.gc();
			System.out.println("GC GC GC");

			StringBuilder sb1111=new StringBuilder();
			sb1111.append("\n*********IN MB*******" +
					  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
			sb1111.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
			sb1111.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
			
			System.out.println(sb1111.toString());
			
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
//	@Test
	public static void test () throws InstanceAlreadyExistsException, IOException  {
		
		StringBuilder sb=new StringBuilder();
		sb.append("\n*********IN MB*******" +
				  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
		sb.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
		sb.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
		
		System.out.println(sb.toString());
		
		Session ses = Context.getNewSession();
		try{
			List dlis = ses.createSQLQuery("SELECT dmpProcedureName,dmpTableName,dmpFileNameInit FROM dmp_ WHERE isActive = true").list();
			
			for (Object object : dlis) {
				Object[] cols = (Object[])object;
				String procedureName = (String) cols[0];
				String tableName = (String) cols[1];
				String fileNameInit = (String) cols[2];
				
				//CALL PROCEDURE TO REPLACE ALL DATA
				ses.createSQLQuery("CALL "+procedureName+"();").executeUpdate();
				
				//SCROLL MASTER TABLE DATA TO CREATE CSV
				ScrollableResults results = ses.createSQLQuery("SELECT * FROM "+tableName)
			            .setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);
				
				String fileName = fileNameInit+"_"+GlobalParams.CSV_FILENAME_DATE_FORMAT.format(new Date());
				File mfile = FileUtils.getFile(fileName+".zip", GlobalParams.DATA_DUMP_DIR);
				FileOutputStream fop = null;
				try {
					fop = new FileOutputStream(mfile);
				}
				catch (IOException e) {
					e.printStackTrace();
				}  
				
				ZipOutputStream zip = new ZipOutputStream(fop);

				zip.putNextEntry(new ZipEntry(fileName+".csv"));
				
				List<String> colNames = ses.createSQLQuery("SELECT column_name FROM information_schema.columns WHERE table_name='"+tableName+"' ORDER BY ORDINAL_POSITION").list();
				CsveeRow headerRow = new CsveeRow();
				for (String string : colNames) {
					headerRow.addRowElement(string);
				}
				
				zip.write(headerRow.getRowAsSB().toString().getBytes());
				
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
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			ses.close();
		}
		
		StringBuilder sb111=new StringBuilder();
		sb111.append("\n*********IN MB*******" +
				  "\nAVAILABLE MEMORY : "+Runtime.getRuntime().freeMemory()/MEGABYTE);
		sb111.append("\nTOTAL MEMORY   : "+Runtime.getRuntime().totalMemory()/MEGABYTE);
		sb111.append("\nMAXIMUM MEMORY : "+Runtime.getRuntime().maxMemory()/MEGABYTE);
		
		System.out.println(sb111.toString());
	}
}
