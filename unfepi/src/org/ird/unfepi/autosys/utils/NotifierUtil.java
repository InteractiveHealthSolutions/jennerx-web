package org.ird.unfepi.autosys.utils;

import ird.xoutTB.emailer.emailServer.EmailServer.AttachmentType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.OperationNotSupportedException;

import org.ird.unfepi.EmailEngine;
import org.ird.unfepi.GlobalParams;
import org.ird.unfepi.GlobalParams.DownloadableType;
import org.ird.unfepi.context.Context;
import org.ird.unfepi.context.ServiceContext;
import org.ird.unfepi.model.Notifier;
import org.ird.unfepi.model.Notifier.NotifierType;
import org.ird.unfepi.report.PdfDocument;
import org.ird.unfepi.report.PdfTable;
import org.ird.unfepi.utils.FileUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.mysql.jdbc.StringUtils;

public class NotifierUtil 
{
	private static String NOTIFIER_HEADER_FIELDS_SEPARATOR = ",";
	
	public static boolean notifyViaSms(Notifier notifier) throws OperationNotSupportedException{
		if(notifier.getNotifierType().equals(NotifierType.SMS))
		{
			String text = "";
			if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierMessage()))
			{
				text += notifier.getNotifierMessage();
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierQuery())){
				ServiceContext sc = Context.getServices();
				List result = null;
				try{
					result = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierQuery());
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.NOTIFIERJOBLOGGER.error(notifier.getNotifierName() ,e);
				}
				finally{
					sc.closeSession();
				}
				for (Object object : result) 
				{
					if(object instanceof Object[]){
						Object[] coldata = (Object[]) object;
						for (int i = 0 ; i < coldata.length ; i++) {
							text += coldata[i]==null?"":coldata[i].toString();
							if(i < (coldata.length -1)){
								text +=',';
							}
						}
						text +='\n';
					}
					else{
						text += (object==null?"":object.toString());
						text +='\n';
					}
					
				}
			}
			
			if(text.length() < 500){
				TarseelServices tsc = TarseelContext.getServices();
				try{
					for (String recp : notifier.getNotifierRecipient()) {
						String referenceNumber = tsc.getSmsService().createNewOutboundSms(recp, text, new Date(), Priority.MEDIUM, 5, PeriodType.HOUR, GlobalParams.SMS_TARSEEL_PROJECT_ID, null);
					}
					tsc.commitTransaction();
				}
				catch (Exception e) {
					e.printStackTrace();
					GlobalParams.NOTIFIERJOBLOGGER.error(notifier.getNotifierName() ,e);
				}
				finally{
					tsc.closeSession();
				}
			}
			else{
				throw new OperationNotSupportedException("Outbound message have exceeded the length of 500 in notifier sms");
			}
		}
		return false;
	}
	
	public static boolean notifyViaCsvEmail(Notifier notifier) throws IOException, MessagingException{
		if(notifier.getNotifierType().equals(NotifierType.EMAIL_CSV))
		{
			ByteArrayOutputStream fw = new ByteArrayOutputStream();
			
			ServiceContext sc = Context.getServices();
			List list = new ArrayList();
			try{
				String[] headerFields = notifier.getColumnsHeaderList().split(NOTIFIER_HEADER_FIELDS_SEPARATOR);
				
				for (int i = 0 ; i < headerFields.length ; i++) 
				{
					fw.write(("\""+(StringUtils.isEmptyOrWhitespaceOnly(headerFields[i])?"":headerFields[i].replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
					if(i < (headerFields.length -1)){
						fw.write(',');
					}
				}
				
				fw.write('\n');
				
				if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierHeaderQuery())){
					List headerlist = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierHeaderQuery());
					for (Object object : headerlist) 
					{
						if(object instanceof Object[]){
							Object[] coldata = (Object[]) object;
							for (int i = 0 ; i < coldata.length ; i++) {
								fw.write(("\""+(coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
								if(i < (coldata.length -1)){
									fw.write(',');
								}
							}
							fw.write('\n');
						}
						else{
							fw.write(("\""+(object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
							fw.write('\n');
						}
						
					}
				}
				
				list = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierQuery());
			
				for (Object object : list) 
				{
					if(object instanceof Object[]){
						Object[] coldata = (Object[]) object;
						for (int i = 0 ; i < coldata.length ; i++) {
							fw.write(("\""+(coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
							if(i < (coldata.length -1)){
								fw.write(',');
							}
						}
						fw.write('\n');
					}
					else{
						fw.write(("\""+(object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
						fw.write('\n');
					}
					
				}
				
				if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierFooterQuery())){
					List footerlist = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierFooterQuery());
					for (Object object : footerlist) 
					{
						if(object instanceof Object[]){
							Object[] coldata = (Object[]) object;
							for (int i = 0 ; i < coldata.length ; i++) {
								fw.write(("\""+(coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
								if(i < (coldata.length -1)){
									fw.write(',');
								}
							}
							fw.write('\n');
						}
						else{
							fw.write(("\""+(object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim())+"\"").getBytes());
							fw.write('\n');
						}
						
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				fw.write(e.getMessage().getBytes());
				GlobalParams.NOTIFIERJOBLOGGER.error(notifier.getNotifierName() ,e);
			}
			finally{
				sc.closeSession();
			}
			byte[] b=fw.toString().replace("\"null\"", "\"\"").getBytes();
			
			ByteArrayOutputStream zo = new ByteArrayOutputStream();
			zo.write(b);
			
			FileUtils.saveDownloadable(zo, notifier.getNotifierName()+"_"+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+".csv", GlobalParams.NOTIFIER_CSV_DIR, null, DownloadableType.CSV_NOTIFIER);

			EmailEngine.getInstance().getEmailer().postEmailWithAttachment(notifier.getNotifierRecipient().toArray(new String[0]), notifier.getNotifierSubject(), notifier.getNotifierMessage(), "unfepi@ird.org", zo.toByteArray(), notifier.getNotifierName()+".csv",AttachmentType.CSV);
			
			zo.close();
		}
		return false;
	}
	
	public static boolean notifyViaPdfEmail(Notifier notifier) throws IOException, DocumentException, MessagingException{
		if(notifier.getNotifierType().equals(NotifierType.EMAIL_PDF))
		{
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();

			PdfDocument pdf = new PdfDocument(ostream, notifier.getNotifierName(), "", "", new Rectangle(0, 0, 500, 500));
			String[] headerFields = notifier.getColumnsHeaderList().split(NOTIFIER_HEADER_FIELDS_SEPARATOR);
			
			PdfTable table = new PdfTable(headerFields.length, notifier.getNotifierSubject());
			table.addHeader(headerFields);
			
			ServiceContext sc = Context.getServices();
			try{
				if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierHeaderQuery())){
					List headerlist = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierHeaderQuery());

					for (Object object : headerlist) 
					{
						ArrayList<String> rowdata = new ArrayList<String>();
						
						if(object instanceof Object[]){
							Object[] coldata = (Object[]) object;
							for (int i = 0 ; i < coldata.length ; i++) {
								rowdata.add((coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim()));
							}
						}
						else{
							rowdata.add((object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim()));
						}
						
						table.addRow(rowdata.toArray(new String[]{}));
					}
				}
				
				List list = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierQuery());

				for (Object object : list) 
				{
					ArrayList<String> rowdata = new ArrayList<String>();
					
					if(object instanceof Object[]){
						Object[] coldata = (Object[]) object;
						for (int i = 0 ; i < coldata.length ; i++) {
							rowdata.add((coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim()));
						}
					}
					else{
						rowdata.add((object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim()));
					}
					
					table.addRow(rowdata.toArray(new String[]{}));
				}
			

				if(!StringUtils.isEmptyOrWhitespaceOnly(notifier.getNotifierFooterQuery())){
					List footerlist = sc.getCustomQueryService().getDataBySQL(notifier.getNotifierFooterQuery());

					for (Object object : footerlist) 
					{
						ArrayList<String> rowdata = new ArrayList<String>();
						
						if(object instanceof Object[]){
							Object[] coldata = (Object[]) object;
							for (int i = 0 ; i < coldata.length ; i++) {
								rowdata.add((coldata[i]==null?"":coldata[i].toString().replace(",",",,").replace("\"", "'").trim()));
							}
						}
						else{
							rowdata.add((object==null?"":object.toString().replace(",",",,").replace("\"", "'").trim()));
						}
						
						table.addRow(rowdata.toArray(new String[]{}));
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				GlobalParams.NOTIFIERJOBLOGGER.error(notifier.getNotifierName() ,e);
			}
			finally{
				sc.closeSession();
			}
			pdf.addTable(table);
			pdf.finishAndCloseDocument();
			
			FileUtils.saveDownloadable(ostream, notifier.getNotifierName()+"_"+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+".pdf", GlobalParams.NOTIFIER_PDF_DIR, null, DownloadableType.PDF_NOTIFIER);

			EmailEngine.getInstance().getEmailer().postEmailWithAttachment(notifier.getNotifierRecipient().toArray(new String[0]), notifier.getNotifierSubject(), notifier.getNotifierMessage(), "epi@ird.org", ostream.toByteArray(), "emailfile.pdf",AttachmentType.PDF);

			ostream.close();
		}
		return false;
	}
}
